---
title: "StackPanel"
slug: "stack-panel"
category: "Widgets/Containers"
locale: "en"
order: 90
---

# StackPanel

Overlay container stacking all children within a shared rectangular slot.

## Overview

`StackPanel` is an overlay container where every non-collapsed child is placed in the same shared rectangular slot. Each child is aligned according to its own margin, preferred size, and alignment rules (`Alignment.STRETCH`, `CENTER`, etc.).

This makes `StackPanel` ideal for composition: layering decorative backdrops, content, notification badges, or watermark overlays on top of one another.

## Overlay Badge over Avatar

```java
StackPanel stack = new StackPanel();
stack.addChild(avatarImage);
badge.layout(style -> style
        .position(PositionType.ABSOLUTE)
        .inset(0.0f, Float.NaN, Float.NaN, 0.0f));
stack.addChild(badge);
```

## Related links

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">StackPanel::</span>`arrangeChild(Widget, float, float, float, float)` {: #arrangechildwidget-float-float-float-float data-toc-label="arrangeChild(Widget, float, float, float, float)" }

**Arrange Child**

Arranges a child inside assigned slot bounds applying margin and alignment.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | Child widget to add |
| `slotX` | `float` | slot X coordinate |
| `slotY` | `float` | slot Y coordinate |
| `slotWidth` | `float` | slot width |
| `slotHeight` | `float` | slot height |

</div>

