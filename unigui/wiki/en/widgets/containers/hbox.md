---
title: "HBox"
slug: "hbox"
category: "Widgets/Containers"
locale: "en"
order: 60
---

# HBox

Horizontal linear container placing children left-to-right with uniform spacing.

## Overview

`HBox` is a horizontal shorthand for `LinearBox`. It arranges child widgets from left to right using flex layout (`FlexDirection.ROW`) without line wrapping.

Spacing between adjacent elements is configured with `spacing(pixels)` and updates `columnGap`. Ideal for toolbars, button rows, navigation bars, and inline controls.

## Horizontal Toolbar

```java
HBox toolbar = new HBox().spacing(6.0f);
toolbar.addChild(new Button("New"));
toolbar.addChild(new Button("Open"));
toolbar.addChild(new Button("Save"));
```

## Related links

- [LinearBox](<linear-box.md>)
- [PanelRowWidget](<panel-row-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### `HBox()` {: #hbox data-toc-label="HBox()" }

**Constructor**

Creates an empty horizontal linear container.

</div>

