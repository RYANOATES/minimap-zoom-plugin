package minimapzoom;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimapZoomScaleTest
{
	@Test
	public void convertsPercentageUsingPointZeroEight()
	{
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(20.0), 0.0);
		assertEquals(4.0, MinimapZoomPlugin.toPixelsPerTile(50.0), 0.0);
		assertEquals(8.0, MinimapZoomPlugin.toPixelsPerTile(100.0), 0.0);
		assertEquals(4.08, MinimapZoomPlugin.toPixelsPerTile(51), 0.00001);
	}

	@Test
	public void constrainsPreviouslySavedOutOfRangeValues()
	{
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(-10), 0.0);
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(0), 0.0);
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(10), 0.0);
		assertEquals(8.0, MinimapZoomPlugin.toPixelsPerTile(100), 0.0);
	}

	@Test
	public void rejectsNonFiniteValues()
	{
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(Double.NaN), 0.0);
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(2.0, MinimapZoomPlugin.toPixelsPerTile(Double.NEGATIVE_INFINITY), 0.0);
	}

	@Test
	public void normalizesStoredPercentages()
	{
		assertEquals(100, MinimapZoomPlugin.normalizePercentage("101"));
		assertEquals(0, MinimapZoomPlugin.normalizePercentage("-1"));
		assertEquals(50, MinimapZoomPlugin.normalizePercentage("50.0"));
		assertEquals(51, MinimapZoomPlugin.normalizePercentage("50.6"));
		assertEquals(0, MinimapZoomPlugin.normalizePercentage("0"));
		assertEquals(100, MinimapZoomPlugin.normalizePercentage("100"));
		assertEquals(50, MinimapZoomPlugin.normalizePercentage(null));
		assertEquals(50, MinimapZoomPlugin.normalizePercentage("invalid"));
		assertEquals(50, MinimapZoomPlugin.normalizePercentage("NaN"));
		assertEquals(100, MinimapZoomPlugin.normalizePercentage("Infinity"));
		assertEquals(0, MinimapZoomPlugin.normalizePercentage("-Infinity"));
	}
}
