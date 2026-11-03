---
title: "UniScreenOverlayRender"
slug: "uni-screen-overlay-render"
category: "Core/Screens"
locale: "en"
order: 40
---

# UniScreenOverlayRender

Minecraft-independent entry point for overlays rendered on top of active GUI screens.

## Overview

`UniScreenOverlayRender` registers layers targeting `UniRenderTarget.SCREEN_OVERLAY`. These layers render on top of any open screen (vanilla inventories, mod containers, custom screens) without replacing the underlying screen hierarchy.

## Use Cases for Screen Overlays

Screen overlays are suited for features that remain persistent across menu transitions:

- **Recipe and item browsers** (JEI/REI/EMI style sidebars).
- **In-game notifications and toasts** displayed above open menus.
- **Debug inspectors** and UI hierarchy visualizers.
- **Pinned tooltips** or assistance windows.

## Registering a Screen Overlay

```java
Widget sidebar = new RecipeSidebarWidget();
UniRenderLayerRegistration reg = UniScreenOverlayRender.register(
    new UniRenderLayer(sidebar),
    screen -> true,
    100
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`clear()` {: #clear data-toc-label="clear()" }

**clear()**

Clears and closes all registered screen overlays.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(UniRenderLayer)` {: #registerunirenderlayer data-toc-label="register(UniRenderLayer)" }

**register([UniRenderLayer](<uni-render-layer.md>))**

Registers a configured [UniRenderLayer](<uni-render-layer.md>) as a screen overlay with default visibility and priority 0.

| Parameter | Type | Description |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Configured [UniRenderLayer](<uni-render-layer.md>). |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(UniRenderLayer, Predicate, int)` {: #registerunirenderlayer-predicate-int data-toc-label="register(UniRenderLayer, Predicate, int)" }

**register([UniRenderLayer](<uni-render-layer.md>), Predicate<Object>, int)**

Registers a [UniRenderLayer](<uni-render-layer.md>) as a screen overlay with visibility condition and priority.

| Parameter | Type | Description |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Render layer to register. |
| `visibility` | `Predicate` | Visibility predicate tested before rendering. |
| `priority` | `int` | Rendering order priority. |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(Widget)` {: #registerwidget data-toc-label="register(Widget)" }

**register([Widget](<../../widgets/widget.md>))**

Registers a root widget as a screen overlay with default settings.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `Widget` | Root widget to render. |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(Widget, UIContext)` {: #registerwidget-uicontext data-toc-label="register(Widget, UIContext)" }

**register([Widget](<../../widgets/widget.md>), UIContext)**

Registers a root widget with an explicit UIContext as a screen overlay.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `Widget` | Root widget to render. |
| `context` | `UIContext` | UIContext to bind. |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

