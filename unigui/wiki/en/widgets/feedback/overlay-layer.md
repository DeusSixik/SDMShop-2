---
title: "OverlayLayer"
slug: "overlay-layer"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# OverlayLayer

Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.

## Overview

`OverlayLayer` is a top-level container dedicated to rendering floating visual elements above standard UI widgets.

Responsibilities:
- Hosts popups, dropdowns, context menus, tooltips, and floating windows.
- Manages modal scrim dimming backdrops with customizable renderers and colors.
- Routes mouse and keyboard input with layer-aware hit testing and outside-click dismissal.
- Integrates with `WindowManager` for desktop window hierarchies.

## Hosting Overlays and Floating Widgets

```java
OverlayLayer overlay = new OverlayLayer(mainContent);
overlay.addOverlay(toastWidget);
```

## Related links

- [WindowManager](<../../core/widgets/feedback/window-manager.md>): Manager controlling floating window z-order, focus stack, active states, and desktop window arrangements.
- [WindowWidget](<window-widget.md>): Desktop-style floating window widget supporting title bar, dragging, resizing, collapse, modal dialogs, and docking integrations.

## API

<div class="api-member-card" markdown="1">

### `OverlayLayer()` {: #overlaylayer data-toc-label="OverlayLayer()" }

Constructs an empty OverlayLayer.

</div>

<div class="api-member-card" markdown="1">

### `OverlayLayer(Widget)` {: #overlaylayerwidget data-toc-label="OverlayLayer(Widget)" }

Constructs an OverlayLayer hosting the specified base content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | Content widget hosted inside. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`addOverlay(Widget)` {: #addoverlaywidget data-toc-label="addOverlay(Widget)" }

Adds a floating widget to the top of the overlay layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `overlay` | `Widget` | Floating overlay widget to add or remove. |

Returns `OverlayLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`applyQueuedMutations()` {: #applyqueuedmutations data-toc-label="applyQueuedMutations()" }

Flushes pending overlay additions and removals immediately.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`clearOverlays()` {: #clearoverlays data-toc-label="clearOverlays()" }

Removes all floating overlays currently displayed.

Returns `OverlayLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`content()` {: #content data-toc-label="content()" }

Returns the primary base content widget hosted underneath all overlays.

Returns `Widget`: The hosted content widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Replaces the primary base content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | Content widget hosted inside. |

Returns `OverlayLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`modalScrimColor()` {: #modalscrimcolor data-toc-label="modalScrimColor()" }

Returns the mutable background scrim tint color displayed beneath modal dialogs.

Returns `MutableColor`: Mutable color applied to the modal scrim backdrop..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`modalScrimRenderer()` {: #modalscrimrenderer data-toc-label="modalScrimRenderer()" }

Returns the custom modal scrim background renderer, or null if default.

Returns `WidgetRender`: Custom renderer used for drawing the modal scrim backdrop..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`modalScrimRenderer(WidgetRender)` {: #modalscrimrendererwidgetrender data-toc-label="modalScrimRenderer(WidgetRender)" }

Sets a custom modal scrim background renderer.

| Parameter | Type | Description |
| --- | --- | --- |
| `modalScrimRenderer` | `WidgetRender` | Custom renderer for drawing the modal scrim dimming backdrop. |

Returns `OverlayLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`removeOverlay(Widget)` {: #removeoverlaywidget data-toc-label="removeOverlay(Widget)" }

Removes a floating widget from the overlay layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `overlay` | `Widget` | Floating overlay widget to add or remove. |

Returns `OverlayLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`windowManager()` {: #windowmanager data-toc-label="windowManager()" }

Synonym returning the [WindowManager](<../../core/widgets/feedback/window-manager.md>) controller.

Returns `WindowManager`: The window manager coordinating floating windows in this overlay layer..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`windows()` {: #windows data-toc-label="windows()" }

Returns the [WindowManager](<../../core/widgets/feedback/window-manager.md>) controller governing floating windows.

Returns `WindowManager`: The window manager coordinating floating windows in this overlay layer..

</div>

