---
title: "UiPostEffectBlendMode"
slug: "ui-post-effect-blend-mode"
category: "Core/PostEffects"
locale: "en"
order: 70
---

# UiPostEffectBlendMode

Blending behavior for a post-processing shader pass.

## Overview

`UiPostEffectBlendMode` instructs the rendering backend how to blend the output of a shader pass into the target framebuffer:

- `REPLACE`: Overwrites target pixels directly without alpha blending. Standard for intermediate ping-pong buffer passes.
- `ALPHA_BLEND`: Standard transparency blending (`SRC_ALPHA, ONE_MINUS_SRC_ALPHA`).
- `ADDITIVE`: Additive blending for glow, laser, and light bloom overlays.

## Blend Mode Selection

```java
UiPostEffectBlendMode mode = UiPostEffectBlendMode.REPLACE;
boolean blend = mode.blendEnabled();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectBlendMode::</span>`blendEnabled()` {: #blendenabled data-toc-label="blendEnabled()" }

**blendEnabled()**

Returns true if alpha blending should be enabled by the backend.

Returns `boolean`: True if blending is enabled..

</div>

