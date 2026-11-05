# `DockTheme`

Styling theme for the docking system configuring palette colors (DockColor), tab geometry, split handles, shadows, and window resize grips.

### API

- `accentThickness()` → float — Returns the thickness of the active tab accent stripe.
- `accentThickness(float)` → DockTheme — Sets the thickness of the active tab accent stripe.
- `classic()` → DockTheme — Creates a classic blue-tinted docking theme instance.
- `closeGap()` → float — Returns the gap between tab text and close button.
- `closeSize()` → float — Returns the size of the tab close button.
- `closeSize(float)` → DockTheme — Sets the size of the tab close button.
- `color(DockColor)` → MutableColor — Returns the live mutable color corresponding to the specified role.
- `color(DockColor, ColorView)` → DockTheme — Sets the color assigned to a specific role.
- `copy()` → DockTheme — Clones this theme into an independent mutable copy.
- `defaultTheme()` → DockTheme — Returns the global default docking theme.
- `defaultTheme(DockTheme)` → void — Replaces the global default docking theme.
- `editor()` → DockTheme — Creates a modern dark editor docking theme instance.
- `gripSize()` → float — Returns the side size of the bottom-right window resize grip.
- `gripSize(float)` → DockTheme — Sets the side size of the window resize grip.
- `iconGap()` → float — Returns the gap between tab icon and title text.
- `iconSize()` → float — Returns the tab icon dimension in pixels.
- `iconSize(float)` → DockTheme — Sets the tab icon size.
- `resizeGrip()` → ResizeGripStyle — Returns the current window resize grip style.
- `resizeGrip(ResizeGripStyle)` → DockTheme — Sets the window resize grip visual style.
- `splitStyle()` → SplitHandleStyle — Returns the split handle visual style (BLOCK or SEAM).
- `splitStyle(SplitHandleStyle)` → DockTheme — Sets the split handle visual style.
- `splitThickness()` → float — Returns the thickness of split handles between docked leaves.
- `splitThickness(float)` → DockTheme — Sets the thickness of split handles.
- `tabHeight()` → float — Returns the height of tab strips and window title bars in pixels.
- `tabHeight(float)` → DockTheme — Sets the height of tab strips and title bars.
- `tabMaxWidth()` → float — Returns the maximum width of a tab.
- `tabMaxWidth(float)` → DockTheme — Sets the maximum tab width.
- `tabMinWidth()` → float — Returns the minimum width of a tab.
- `tabMinWidth(float)` → DockTheme — Sets the minimum tab width.
- `tabPaddingX()` → float — Returns the horizontal inner padding of tabs.
- `tabPaddingX(float)` → DockTheme — Sets the horizontal inner padding of tabs.
- `tabRadius()` → float — Returns the top corner radius of tabs.
- `tabRadius(float)` → DockTheme — Sets the top corner radius of tabs.
- `tearOffHeight()` → float — Returns the tear-off preview window height.
- `tearOffPreviewEnabled()` → boolean — Returns true if tear-off compact preview sizing is enabled.
- `tearOffPreviewSize(float, float)` → DockTheme — Configures compact preview dimensions while a tab is being torn off from the dock.
- `tearOffWidth()` → float — Returns the tear-off preview window width.
- `windowRadius()` → float — Returns the corner radius of floating dock windows.
- `windowRadius(float)` → DockTheme — Sets the corner radius of floating dock windows.
- `windowShadowSize()` → float — Returns the blur size of floating window drop shadows.
- `windowShadowSize(float)` → DockTheme — Sets the blur size of floating window drop shadows (0 to disable).

