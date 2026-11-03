---
title: "Box"
slug: "box"
category: "Widgets/Containers"
locale: "en"
order: 30
---

# Box

Standard styled container combining background color, texture, shader, border, radius, and child layout.

## Overview

`Box` is the standard concrete visual container in UniGUI. It extends `SurfaceWidget<Box>` and has surface rendering active by default.

Use `Box` whenever you need a styled card, panel, background wrapper, or modal container.

## Card with [Border](<border.md>) and Radius

```java
Box card = new Box()
        .background(ColorView.ofHex("#2C2F33"))
        .border(ColorView.ofHex("#7289DA"))
        .borderWidth(2.0f)
        .radius(8.0f);
card.addChild(titleLabel);
```

## Related links

- [Border](<border.md>)
- [SurfaceWidget](<surface-widget.md>)

## API

<div class="api-member-card" markdown="1">

### `Box()` {: #box data-toc-label="Box()" }

**Constructor**

Creates an empty Box container with visual surface enabled.

</div>

