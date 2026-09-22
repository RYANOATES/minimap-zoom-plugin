package minimapzoom;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Auto Minimap Zoom % and Hotkey",
        description = "Hotkey that sets a Minimap Zoom percentage and Automatically applies it after loading areas or changing worlds",
		tags = {"minimap", "zoom", "hotkey", "utility", "auto"}
)
public class MinimapZoomPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private MinimapZoomConfig config;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ConfigManager configManager;

    private HotkeyListener hotkeyListener;
    private volatile boolean active;
    private volatile boolean zoomPending;
    private boolean applied;
    private boolean previousZoomEnabled;
    private double previousZoom;
    private double appliedZoom;

    @Override
	protected void startUp()
	{
		active = true;
		applied = false;
		normalizeZoomSetting();
		zoomPending = true;
		hotkeyListener = new HotkeyListener(config::zoomHotkey)
		{
			@Override
			public void hotkeyPressed()
			{
				clientThread.invokeLater(MinimapZoomPlugin.this::setConfiguredMinimapZoom);
			}
		};

        keyManager.registerKeyListener(hotkeyListener);
    }

    @Override
	protected void shutDown()
	{
		active = false;
		zoomPending = false;
		keyManager.unregisterKeyListener(hotkeyListener);
		clientThread.invokeLater(() ->
		{
			// Leave a subsequent manual zoom change alone.
			if (!active && applied && client.isMinimapZoom()
				&& Double.compare(client.getMinimapZoom(), appliedZoom) == 0)
			{
				client.setMinimapZoom(previousZoom);
				client.setMinimapZoom(previousZoomEnabled);
			}
			if (!active)
			{
				applied = false;
			}
		});
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		// Loading a region, entering an instance, logging in and hopping worlds
		// all return to LOGGED_IN. Apply before the first rendered game frame.
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			zoomPending = true;
		}
	}

	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		if (active && zoomPending && client.getGameState() == GameState.LOGGED_IN)
		{
			zoomPending = false;
			setConfiguredMinimapZoom();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (MinimapZoomConfig.GROUP.equals(event.getGroup()) && "zoomLevel".equals(event.getKey()))
		{
			normalizeZoomSetting();
			zoomPending = true;
		}
	}

	@Subscribe
	public void onProfileChanged(ProfileChanged event)
	{
		normalizeZoomSetting();
		zoomPending = true;
	}

	private int normalizeZoomSetting()
	{
		String saved = configManager.getConfiguration(MinimapZoomConfig.GROUP, "zoomLevel");
		int percentage = normalizePercentage(saved);
		// Also migrate previously saved decimal percentages to integer storage.
		// Write only when changed, since this emits another ConfigChanged event.
		if (saved != null && !Integer.toString(percentage).equals(saved))
		{
			configManager.setConfiguration(MinimapZoomConfig.GROUP, "zoomLevel", percentage);
		}
		return percentage;
	}

	static int normalizePercentage(String saved)
	{
		if (saved == null)
		{
			return 50;
		}
		try
		{
			double value = Double.parseDouble(saved);
			if (Double.isNaN(value))
			{
				return 50;
			}
			return (int) Math.round(Math.max(0, Math.min(100, value)));
		}
		catch (NumberFormatException ex)
		{
			return 50;
		}
	}

	private void setConfiguredMinimapZoom()
	{
		if (!active || client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		if (!applied)
		{
			previousZoomEnabled = client.isMinimapZoom();
			previousZoom = client.getMinimapZoom();
		}
		double zoomLevel = toPixelsPerTile(normalizeZoomSetting());
		// The numeric setter does nothing unless minimap zoom is enabled.
		client.setMinimapZoom(true);
		client.setMinimapZoom(zoomLevel);
		appliedZoom = client.getMinimapZoom();
		applied = true;
		log.debug("Minimap zoom set to: {}", zoomLevel);
    }

	static double toPixelsPerTile(double configuredLevel)
	{
		// Preserve the percentage conversion and RuneLite's supported range.
		if (!Double.isFinite(configuredLevel))
		{
			return 2.0;
		}
		return Math.max(2.0, Math.min(8.0, configuredLevel * 0.08));
	}

    @Provides
    MinimapZoomConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(MinimapZoomConfig.class);
    }
}
