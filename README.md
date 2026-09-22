# Minimap Zoom

Minimap Zoom automatically applies your chosen minimap zoom percentage when you log in, load a new area or dungeon, or change worlds.

## Use

1. Enable **Minimap Zoom** in the development client's plugin list. This project has not yet been published to the Plugin Hub.
2. Choose a **Zoom level** percentage. Changes apply immediately while logged in, and after loading if you are currently on a loading screen.
3. Optionally choose a **Zoom hotkey** to manually reapply your setting.

Lower Zoom Level values show a wider area:

- **0–25%**: furthest supported zoom out, twice the normal distance across.
- **50%** (default): normal view.
- **100%**: closest supported zoom in.

The arrows change the percentage by 1. Values above 100 are corrected to 100 and values below 0 to 0. Previously saved decimal percentages are rounded to the nearest whole percentage; invalid text falls back to 50. The configuration group and key are unchanged.

The conversion remains percentage × 0.08 pixels per tile. RuneLite's current client limits minimap zoom to 2–8 pixels per tile (normal is 4), so 0–25% all reach the same lower limit. It cannot extend that limit or show terrain that the game has not loaded. API reference: [minimap zoom](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/Client.html#setMinimapZoom(double)).

The plugin applies the setting when enabled, when you change the percentage or configuration profile, and before the first rendered game frame after returning to the logged-in state. It enables minimap zoom if necessary. You can still scroll the minimap manually between loads. Disabling this plugin restores the previous view and zoom-enabled state if its applied zoom has not subsequently been changed. Other minimap plugins may override the zoom; disable those while testing if the view keeps resetting.

The plugin does not send game input, communicate with external services, or automate gameplay.

## License

This project is released under the [BSD 2-Clause License](LICENSE).
