---
title: "FlexBox"
slug: "flex-box"
category: "Widgets/Containers"
locale: "en"
order: 70
---

# FlexBox

CSS-like flex container configured directly via [LayoutStyle](<../../core/layout/layout-style.md>) flexbox properties.

## Overview

`FlexBox` is the direct UniGUI equivalent to CSS `display: flex`. Unlike `LinearBox` and `WrapPanel`, it does not store secondary orientation or spacing states.

Everything — direction, wrapping, main-axis justification (`justifyContent`), cross-axis alignment (`alignItems`), and item gaps (`rowGap`, `columnGap`) — is configured directly on its `layoutStyle()`.

## Flex Justification & Alignment

```java
FlexBox flex = new FlexBox();
flex.layout(style -> style
        .flexDirection(FlexDirection.ROW)
        .justifyContent(Justify.SPACE_BETWEEN)
        .alignItems(Align.CENTER)
        .gap(8.0f));
```

## Related links

- [LayoutStyle](<../../core/layout/layout-style.md>)
- [LinearBox](<linear-box.md>)

## API

<div class="api-member-card" markdown="1">

### `FlexBox()` {: #flexbox data-toc-label="FlexBox()" }

**Constructor**

Creates an unconstrained FlexBox container with ROW direction.

</div>

