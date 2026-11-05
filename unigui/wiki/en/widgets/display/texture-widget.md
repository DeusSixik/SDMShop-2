---
title: "TextureWidget"
slug: "texture-widget"
category: "Widgets/Display"
locale: "en"
order: 90
---

# TextureWidget

Base widget for rendering textures, UV regions, nine-slice borders, and image handles.

## Overview

`TextureWidget` displays textures represented by `TextureHandle`. Supports texture filtering modes (linear/nearest), wrap modes (clamp/repeat), mipmapping, and source UV coordinates.

## Rendering Texture

```java
TextureWidget icon = new TextureWidget(TextureHandle.of("mod:textures/gui/icon.png"))
        .textureSampling(TextureFilter.NEAREST);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureWidget::</span>`texture(TextureHandle)` {: #texturetexturehandle data-toc-label="texture(TextureHandle)" }

**Texture Handle**

Returns or sets [TextureHandle](<../../core/textures/texture-handle.md>) referenced by widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | — |

Returns `TextureWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureWidget::</span>`textureSampling(TextureFilter)` {: #texturesamplingtexturefilter data-toc-label="textureSampling(TextureFilter)" }

**Texture Sampling**

Sets texture filtering mode (NEAREST or LINEAR).

| Parameter | Type | Description |
| --- | --- | --- |
| `filter` | `TextureFilter` | — |

Returns `TextureWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureWidget::</span>`textureWrap(TextureWrap)` {: #texturewraptexturewrap data-toc-label="textureWrap(TextureWrap)" }

**Texture Wrap**

Sets texture wrapping mode (CLAMP or REPEAT).

| Parameter | Type | Description |
| --- | --- | --- |
| `wrap` | `TextureWrap` | — |

Returns `TextureWidget`.

</div>

