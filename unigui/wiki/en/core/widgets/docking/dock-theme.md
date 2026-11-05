---
title: "DockTheme"
slug: "dock-theme"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockTheme

Styling theme for the docking system configuring palette colors ([DockColor](<dock-color.md>)), tab geometry, split handles, shadows, and window resize grips.

## Overview

`DockTheme` configures all visual aspects of the docking subsystem.

### Factory Presets

| Theme Factory | Palette & [Style](<../../styling/style.md>) |
| --- | --- |
| `DockTheme.defaultTheme()` | Standard dark IDE theme. |
| `DockTheme.editor()` | High-contrast code editor palette. |
| `DockTheme.classic()` | Clean light-mode palette. |

### Metrics & Styling

Configure tab height (`tabHeight`), corner radii (`tabRadius`), split handle thickness (`splitThickness`), floating window shadows, and resize grips.

## Customizing Docking Theme Colors and Metrics

```java
DockTheme theme = DockTheme.editor().copy()
        .tabHeight(24.0f)
        .splitThickness(6.0f);
```

## Related links

- [DockColor](<dock-color.md>): Semantic color roles used throughout DockTheme to style tabs, split bars, floating window chrome, and drop preview zones.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`accentThickness()` {: #accentthickness data-toc-label="accentThickness()" }

Returns the thickness of the active tab accent stripe.

Returns `float`: Thickness of active tab indicator in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`accentThickness(float)` {: #accentthicknessfloat data-toc-label="accentThickness(float)" }

Sets the thickness of the active tab accent stripe.

| Parameter | Type | Description |
| --- | --- | --- |
| `accentThickness` | `float` | Thickness of the active tab accent indicator. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`classic()` {: #classic data-toc-label="classic()" }

Creates a classic blue-tinted docking theme instance.

Returns `DockTheme`: A new or modified DockTheme instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`closeGap()` {: #closegap data-toc-label="closeGap()" }

Returns the gap between tab text and close button.

Returns `float`: Gap between title text and close button in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`closeSize()` {: #closesize data-toc-label="closeSize()" }

Returns the size of the tab close button.

Returns `float`: Close button icon dimension in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`closeSize(float)` {: #closesizefloat data-toc-label="closeSize(float)" }

Sets the size of the tab close button.

| Parameter | Type | Description |
| --- | --- | --- |
| `closeSize` | `float` | Size of the close button in tab headers. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`color(DockColor)` {: #colordockcolor data-toc-label="color(DockColor)" }

Returns the live mutable color corresponding to the specified role.

| Parameter | Type | Description |
| --- | --- | --- |
| `role` | `DockColor` | Semantic color role. |

Returns `MutableColor`: MutableColorView instance for modification..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`color(DockColor, ColorView)` {: #colordockcolor-colorview data-toc-label="color(DockColor, ColorView)" }

Sets the color assigned to a specific role.

| Parameter | Type | Description |
| --- | --- | --- |
| `role` | `DockColor` | Semantic color role. |
| `color` | `ColorView` | Color view to assign. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`copy()` {: #copy data-toc-label="copy()" }

Clones this theme into an independent mutable copy.

Returns `DockTheme`: A new or modified DockTheme instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`defaultTheme()` {: #defaulttheme data-toc-label="defaultTheme()" }

Returns the global default docking theme.

Returns `DockTheme`: A new or modified DockTheme instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`defaultTheme(DockTheme)` {: #defaultthemedocktheme data-toc-label="defaultTheme(DockTheme)" }

Replaces the global default docking theme.

| Parameter | Type | Description |
| --- | --- | --- |
| `theme` | `DockTheme` | Source theme to copy from. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`editor()` {: #editor data-toc-label="editor()" }

Creates a modern dark editor docking theme instance.

Returns `DockTheme`: A new or modified DockTheme instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`gripSize()` {: #gripsize data-toc-label="gripSize()" }

Returns the side size of the bottom-right window resize grip.

Returns `float`: Resize grip dimension in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`gripSize(float)` {: #gripsizefloat data-toc-label="gripSize(float)" }

Sets the side size of the window resize grip.

| Parameter | Type | Description |
| --- | --- | --- |
| `gripSize` | `float` | Size of window resize grips. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`iconGap()` {: #icongap data-toc-label="iconGap()" }

Returns the gap between tab icon and title text.

Returns `float`: Gap between icon and title text in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`iconSize()` {: #iconsize data-toc-label="iconSize()" }

Returns the tab icon dimension in pixels.

Returns `float`: Tab header icon dimension in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`iconSize(float)` {: #iconsizefloat data-toc-label="iconSize(float)" }

Sets the tab icon size.

| Parameter | Type | Description |
| --- | --- | --- |
| `iconSize` | `float` | Size of icons in tab headers. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`resizeGrip()` {: #resizegrip data-toc-label="resizeGrip()" }

Returns the current window resize grip style.

Returns `ResizeGripStyle`: Configured resize grip style..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`resizeGrip(ResizeGripStyle)` {: #resizegripresizegripstyle data-toc-label="resizeGrip(ResizeGripStyle)" }

Sets the window resize grip visual style.

| Parameter | Type | Description |
| --- | --- | --- |
| `resizeGrip` | `ResizeGripStyle` | Resize grip painter style. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitStyle()` {: #splitstyle data-toc-label="splitStyle()" }

Returns the split handle visual style (BLOCK or SEAM).

Returns `SplitHandleStyle`: Visual style of split handles..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitStyle(SplitHandleStyle)` {: #splitstylesplithandlestyle data-toc-label="splitStyle(SplitHandleStyle)" }

Sets the split handle visual style.

| Parameter | Type | Description |
| --- | --- | --- |
| `splitStyle` | `SplitHandleStyle` | Visual style of split handles. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitThickness()` {: #splitthickness data-toc-label="splitThickness()" }

Returns the thickness of split handles between docked leaves.

Returns `float`: Split handle bar thickness in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitThickness(float)` {: #splitthicknessfloat data-toc-label="splitThickness(float)" }

Sets the thickness of split handles.

| Parameter | Type | Description |
| --- | --- | --- |
| `splitThickness` | `float` | Thickness of split handles between dock nodes. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabHeight()` {: #tabheight data-toc-label="tabHeight()" }

Returns the height of tab strips and window title bars in pixels.

Returns `float`: Tab header bar height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabHeight(float)` {: #tabheightfloat data-toc-label="tabHeight(float)" }

Sets the height of tab strips and title bars.

| Parameter | Type | Description |
| --- | --- | --- |
| `tabHeight` | `float` | Height of docking tab headers in pixels. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMaxWidth()` {: #tabmaxwidth data-toc-label="tabMaxWidth()" }

Returns the maximum width of a tab.

Returns `float`: Maximum tab width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMaxWidth(float)` {: #tabmaxwidthfloat data-toc-label="tabMaxWidth(float)" }

Sets the maximum tab width.

| Parameter | Type | Description |
| --- | --- | --- |
| `tabMaxWidth` | `float` | Maximum tab width in pixels. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMinWidth()` {: #tabminwidth data-toc-label="tabMinWidth()" }

Returns the minimum width of a tab.

Returns `float`: Minimum tab width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMinWidth(float)` {: #tabminwidthfloat data-toc-label="tabMinWidth(float)" }

Sets the minimum tab width.

| Parameter | Type | Description |
| --- | --- | --- |
| `tabMinWidth` | `float` | Minimum tab width in pixels. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabPaddingX()` {: #tabpaddingx data-toc-label="tabPaddingX()" }

Returns the horizontal inner padding of tabs.

Returns `float`: Horizontal padding inside tabs in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabPaddingX(float)` {: #tabpaddingxfloat data-toc-label="tabPaddingX(float)" }

Sets the horizontal inner padding of tabs.

| Parameter | Type | Description |
| --- | --- | --- |
| `tabPaddingX` | `float` | Horizontal padding inside docking tabs. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabRadius()` {: #tabradius data-toc-label="tabRadius()" }

Returns the top corner radius of tabs.

Returns `float`: Tab corner radius in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabRadius(float)` {: #tabradiusfloat data-toc-label="tabRadius(float)" }

Sets the top corner radius of tabs.

| Parameter | Type | Description |
| --- | --- | --- |
| `tabRadius` | `float` | Corner radius of docking tabs in pixels. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffHeight()` {: #tearoffheight data-toc-label="tearOffHeight()" }

Returns the tear-off preview window height.

Returns `float`: Default height of torn-off windows in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffPreviewEnabled()` {: #tearoffpreviewenabled data-toc-label="tearOffPreviewEnabled()" }

Returns true if tear-off compact preview sizing is enabled.

Returns `boolean`: True if tear-off preview is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffPreviewSize(float, float)` {: #tearoffpreviewsizefloat-float data-toc-label="tearOffPreviewSize(float, float)" }

Configures compact preview dimensions while a tab is being torn off from the dock.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Default width of torn-off floating windows. |
| `height` | `float` | Default height of torn-off floating windows. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffWidth()` {: #tearoffwidth data-toc-label="tearOffWidth()" }

Returns the tear-off preview window width.

Returns `float`: Default width of torn-off windows in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowRadius()` {: #windowradius data-toc-label="windowRadius()" }

Returns the corner radius of floating dock windows.

Returns `float`: Floating window corner radius in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowRadius(float)` {: #windowradiusfloat data-toc-label="windowRadius(float)" }

Sets the corner radius of floating dock windows.

| Parameter | Type | Description |
| --- | --- | --- |
| `windowRadius` | `float` | Corner radius for floating windows. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowShadowSize()` {: #windowshadowsize data-toc-label="windowShadowSize()" }

Returns the blur size of floating window drop shadows.

Returns `float`: Floating window shadow size in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowShadowSize(float)` {: #windowshadowsizefloat data-toc-label="windowShadowSize(float)" }

Sets the blur size of floating window drop shadows (0 to disable).

| Parameter | Type | Description |
| --- | --- | --- |
| `windowShadowSize` | `float` | Shadow blur size for floating windows. |

Returns `DockTheme`: This theme instance for method chaining..

</div>

