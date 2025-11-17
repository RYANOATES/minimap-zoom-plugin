package minimapzoom;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.Keybind;

import java.awt.event.KeyEvent;

@ConfigGroup("minimapzoom")
public interface MinimapZoomConfig extends Config
{
    @net.runelite.client.config.ConfigItem(
            keyName = "zoomHotkey",
            name = "Zoom Hotkey",
            description = "Set the hotkey to maximize minimap zoom",
            position = 1
    )
    default Keybind zoomHotkey()
    {
        return new Keybind(KeyEvent.VK_Z, 0);
    }

    @SuppressWarnings("SameReturnValue")
    @net.runelite.client.config.ConfigItem(
            keyName = "zoomLevel",
            name = "Zoom Level",
            description = "Set the maximum zoom level for the minimap (higher = more zoomed)",
            position = 2
    )
    default double zoomLevel()
    {
        return 40.0;
    }
}
