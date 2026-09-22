package minimapzoom;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MinimapZoomPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MinimapZoomPlugin.class);
		RuneLite.main(args);
	}
}
