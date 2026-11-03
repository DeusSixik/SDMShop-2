---
title: "WrapPanel"
slug: "wrap-panel"
category: "Widgets/Containers"
locale: "en"
order: 100
---

# WrapPanel

Flow container wrapping children to a new row or column when space is exceeded.

## Overview

`WrapPanel` is a multi-line flow container (`FlexWrap.WRAP`). In horizontal orientation, children flow left-to-right and wrap onto new lines when the container boundary is reached. In vertical orientation, children flow top-to-bottom and wrap into new columns.

- **`spacing(px)`**: Distance between items within the same line.
- **`lineSpacing(px)`**: Distance between consecutive wrapped lines.

## Tag Chip Cloud

```java
WrapPanel chips = new WrapPanel()
        .spacing(6.0f)
        .lineSpacing(6.0f);
chips.addChild(new Chip("Action"));
chips.addChild(new Chip("Adventure"));
chips.addChild(new Chip("RPG"));
```

## Related links

- [FlexBox](<flex-box.md>)
- [LinearBox](<linear-box.md>)

## API

<div class="api-member-card" markdown="1">

### `WrapPanel()` {: #wrappanel data-toc-label="WrapPanel()" }

**Constructor**

Creates a WrapPanel with HORIZONTAL orientation.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WrapPanel::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Set Orientation**

Sets flow direction (HORIZONTAL rows or VERTICAL columns).

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | target orientation |

Returns `WrapPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WrapPanel::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

**Set Item Spacing**

Sets gap in pixels between items on the same line.

| Parameter | Type | Description |
| --- | --- | --- |
| `spacing` | `float` | spacing in UI pixels |

Returns `WrapPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WrapPanel::</span>`lineSpacing(float)` {: #linespacingfloat data-toc-label="lineSpacing(float)" }

**Set Line Spacing**

Sets gap in pixels between consecutive wrapped lines.

| Parameter | Type | Description |
| --- | --- | --- |
| `lineSpacing` | `float` | line gap in UI pixels |

Returns `WrapPanel`: this widget for fluent configuration.

</div>

