---
title: "UniHudRender"
slug: "uni-hud-render"
category: "Core/Screens"
locale: "en"
order: 30
---

# UniHudRender

Minecraft-independent entry point for registering and managing in-game HUD layers.

## Overview

`UniHudRender` provides static helper methods to bind widget trees or `UniRenderLayer` instances to the game's in-game HUD (`UniRenderTarget.HUD`). Layers can define dynamic visibility predicates and z-ordering priority.

## Z-Ordering and Visibility Filtering

Layers with higher priority integers render on top of lower priority layers.

- **Default Priority**: 0
- **Visibility Predicate**: Controls conditional visibility per frame (e.g. hiding when F1 mode is active).

## Registering a HUD [Widget](<../../widgets/widget.md>)

```java
Widget healthBar = new HealthBarWidget();
UniRenderLayerRegistration reg = UniHudRender.register(
    new UniRenderLayer(healthBar),
    ctx -> true,
    10
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`clear()` {: #clear data-toc-label="clear()" }

**clear()**

Clears and closes all registered HUD layers.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(UniRenderLayer)` {: #registerunirenderlayer data-toc-label="register(UniRenderLayer)" }

**register([UniRenderLayer](<uni-render-layer.md>))**

Registers a configured [UniRenderLayer](<uni-render-layer.md>) as a HUD layer with default visibility and priority 0.

| Parameter | Type | Description |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Configured [UniRenderLayer](<uni-render-layer.md>). |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(UniRenderLayer, Predicate, int)` {: #registerunirenderlayer-predicate-int data-toc-label="register(UniRenderLayer, Predicate, int)" }

**register([UniRenderLayer](<uni-render-layer.md>), Predicate<Object>, int)**

Registers a [UniRenderLayer](<uni-render-layer.md>) in HUD with visibility condition and priority.

| Parameter | Type | Description |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Render layer to register. |
| `visibility` | `Predicate` | Visibility predicate tested before rendering. |
| `priority` | `int` | Rendering order priority. |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(Widget)` {: #registerwidget data-toc-label="register(Widget)" }

**register([Widget](<../../widgets/widget.md>))**

Registers a root widget as a HUD layer with default settings.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `Widget` | Root widget to render. |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(Widget, UIContext)` {: #registerwidget-uicontext data-toc-label="register(Widget, UIContext)" }

**register([Widget](<../../widgets/widget.md>), UIContext)**

Registers a root widget with an explicit UIContext as a HUD layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `Widget` | Root widget to render. |
| `context` | `UIContext` | UIContext to bind. |

Returns `UniRenderLayerRegistration`: Registration handle..

</div>

