package minimapzoom;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Minimap Zoom",
        description = "Sets the minimap to maximum zoom with a hotkey",
        tags = {"minimap", "zoom", "utility"},
        enabledByDefault = true
)
public class MinimapZoomPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private MinimapZoomConfig config;

    @Inject
    private KeyManager keyManager;

    private HotkeyListener hotkeyListener;

    @Override
    protected void startUp() {
        hotkeyListener = new HotkeyListener(() -> config.zoomHotkey())
        {
            @Override
            public void hotkeyPressed()
            {
                setMaximumMinimapZoom();
            }

            @Override
            public void hotkeyReleased()
            {
                // Optional: Reset zoom on release, or leave empty to keep zoom active
            }
        };

        keyManager.registerKeyListener(hotkeyListener);
    }

    @Override
    protected void shutDown() {
        keyManager.unregisterKeyListener(hotkeyListener);
    }

    private void setMaximumMinimapZoom()
    {
        if (client != null)
        {
            double zoomLevel = config.zoomLevel();
            client.setMinimapZoom(zoomLevel);
            log.info("Minimap zoom set to: {}", zoomLevel);
        }
    }

    @Provides
    MinimapZoomConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(MinimapZoomConfig.class);
    }
}
