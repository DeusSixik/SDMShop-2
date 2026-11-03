---
title: "UiRenderPolicy"
slug: "ui-render-policy"
category: "Core/Screens"
locale: "en"
order: 15
---

# UiRenderPolicy

Frame-rate and invalidation policy controlling how frequently retained UI trees are rebuilt and rendered.

## Overview

`UiRenderPolicy` dictates whether the runtime redraws the UI on every game tick, only when widget state changes (dirty signal), at a capped FPS, or locked to display VSYNC. This prevents unnecessary GPU and CPU overhead for static interfaces.

## Render Modes

- `CONTINUOUS`: Rebuilds and renders every frame. Ideal for fluid 60+ FPS animations, procedural shaders, and editor viewports.
- `ON_DIRTY`: Renders only when an explicit invalidation/dirty signal is raised. Conserves significant GPU/CPU cycles for static menus and HUDs.
- `FIXED_FPS`: Clamps rendering frequency to a specified target frame rate (e.g. 30 FPS).
- `VSYNC`: Synchronizes rendering intervals with the window display refresh rate.

## Performance and Power Efficiency

Static screens with complex layout trees can cause substantial overhead if re-rendered every tick. Switching to `onDirty()` reduces draw-call generation to zero during idle states, re-rendering only when user input or data events arrive.

## Configuring On-Dirty Rendering for Static Menus

```java
UiRenderPolicy policy = UiRenderPolicy.onDirty();
window.renderPolicy(policy);
```

## Enabling VSYNC for Smooth Animations

```java
UiRenderPolicy policy = UiRenderPolicy.vsync();
window.renderPolicy(policy);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`continuous()` {: #continuous data-toc-label="continuous()" }

**continuous()**

Returns a policy that renders every frame continuously.

Returns `UiRenderPolicy`: Continuous rendering policy instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`mode()` {: #mode data-toc-label="mode()" }

**mode()**

Returns the active mode of this render policy.

Returns `Mode`: Policy mode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`onDirty()` {: #ondirty data-toc-label="onDirty()" }

**onDirty()**

Returns a policy that renders only upon dirty invalidation signals.

Returns `UiRenderPolicy`: On-dirty rendering policy instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`fixedFps(float)` {: #fixedfpsfloat data-toc-label="fixedFps(float)" }

**fixedFps(float)**

Creates a policy that limits UI rendering to a fixed target frame rate.

| Parameter | Type | Description |
| --- | --- | --- |
| `fps` | `float` | Target frames per second; must be finite and positive. |

Returns `UiRenderPolicy`: Fixed FPS rendering policy instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`vsync()` {: #vsync data-toc-label="vsync()" }

**vsync()**

Returns a policy synchronized with the window display refresh rate.

Returns `UiRenderPolicy`: VSYNC-synchronized policy instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`fps()` {: #fps data-toc-label="fps()" }

**fps()**

Returns the target FPS for FIXED_FPS mode, or 0.0 for other modes.

Returns `float`: Target FPS value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`intervalNanos()` {: #intervalnanos data-toc-label="intervalNanos()" }

**intervalNanos()**

Returns the minimum nanosecond interval between renders in FIXED_FPS mode.

Returns `long`: Interval in nanoseconds, or 0 if not FIXED_FPS..

</div>

