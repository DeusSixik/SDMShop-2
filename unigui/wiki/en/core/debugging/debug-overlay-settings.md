---
title: "DebugOverlaySettings"
slug: "debug-overlay-settings"
category: "Core/Debugging"
locale: "en"
order: 10
---

# DebugOverlaySettings

Configuration for placement, scaling, and sampling of the built-in performance overlay.

## Overview

`DebugOverlaySettings` is the mutable configuration returned by `UIContext#debugOverlaySettings`. It controls where the built-in performance overlay appears, its UI scale, the number of retained chart samples, and its distance from the viewport edge. Setters normalize unsafe values, so configuration can be updated directly from editor controls or hotkeys.

## Debug hotkeys

Minecraft screens and render layers provide the following built-in shortcuts:

| Shortcut | Action |
| --- | --- |
| `Ctrl+Shift+P` | Toggles the profiler overlay. |
| `Ctrl+Shift++`, `Ctrl+Shift+=`, or numpad `+` | Increases overlay scale by `0.25`. |
| `Ctrl+Shift+-` or numpad `-` | Decreases overlay scale by `0.25`. |

The main-row plus key is reported as `=` by GLFW, so both plus/equal notation refers to the same binding. A shortcut runs once per physical key press; key-repeat callbacks are consumed without applying the action again. These bindings require the standard `DefaultUIContext`. Scale changes remain clamped by `DebugOverlaySettings` to the inclusive range `0.25` through `4.0`.

## Configure the overlay

```java
context.debugOverlaySettings()
        .anchor(DebugOverlayAnchor.TOP_RIGHT)
        .scale(1.0f)
        .sampleWindow(240)
        .margin(8.0f);
```

## API

### Configuration

| Method | Description |
| --- | --- |
| [`anchor(DebugOverlayAnchor)`](#anchordebugoverlayanchor) | Anchors the overlay to a viewport corner; null selects TOP_LEFT. |
| [`scale(float)`](#scalefloat) | Sets overlay UI scale, clamped to the inclusive range 0.25 through 4.0. |
| [`sampleWindow(int)`](#samplewindowint) | Sets retained chart history, clamped to 1 through 10,000 frames. |
| [`margin(float)`](#marginfloat) | Sets non-negative distance from the anchored viewport edges in UI pixels. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`anchor(DebugOverlayAnchor)` {: #anchordebugoverlayanchor data-toc-label="anchor(DebugOverlayAnchor)" }

**anchor(DebugOverlayAnchor)**

Anchors the overlay to a viewport corner; null selects TOP_LEFT.

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `DebugOverlayAnchor` | Viewport corner used as the overlay origin; may be null. |

Returns `DebugOverlaySettings`: The same settings instance for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`scale(float)` {: #scalefloat data-toc-label="scale(float)" }

**scale(float)**

Sets overlay UI scale, clamped to the inclusive range 0.25 through 4.0.

| Parameter | Type | Description |
| --- | --- | --- |
| `scale` | `float` | Requested scale; non-finite values restore the default 0.75. |

Returns `DebugOverlaySettings`: The same settings instance for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`sampleWindow(int)` {: #samplewindowint data-toc-label="sampleWindow(int)" }

**sampleWindow(int)**

Sets retained chart history, clamped to 1 through 10,000 frames.

| Parameter | Type | Description |
| --- | --- | --- |
| `sampleWindow` | `int` | Requested number of frame samples. |

Returns `DebugOverlaySettings`: The same settings instance for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

**margin(float)**

Sets non-negative distance from the anchored viewport edges in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `margin` | `float` | Margin in UI pixels; non-finite values restore 4 and negative values become 0. |

Returns `DebugOverlaySettings`: The same settings instance for fluent configuration.

</div>

