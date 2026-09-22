package minimapzoom;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("minimapzoom")
public interface MinimapZoomConfig extends Config
{
    String GROUP = "minimapzoom";
    @net.runelite.client.config.ConfigItem(
            keyName = "zoomHotkey",
		name = "Zoom hotkey",
		description = "Hotkey that applies the configured minimap zoom level.",
            position = 1
    )
    default Keybind zoomHotkey()
    {
		return Keybind.NOT_SET;
    }

    @SuppressWarnings("SameReturnValue")
    @net.runelite.client.config.ConfigItem(
            keyName = "zoomLevel",
		name = "Zoom level",
		description = "Applied automatically after loading. 0-25% = furthest out, 50% = normal, 100% = closest in.",
            position = 2
    )
    @Range(min = 0, max = 100)
    @Units(Units.PERCENT)
    default int zoomLevel()
    {
        return 50;
    }
}
