---
title: "VBox"
slug: "vbox"
category: "Widgets/Containers"
locale: "en"
order: 50
---

# VBox

Vertical linear container stacking children top-to-bottom with uniform spacing.

## Overview

`VBox` is a vertical shorthand for `LinearBox`. It arranges child widgets from top to bottom using flex layout (`FlexDirection.COLUMN`) without line wrapping.

Spacing between adjacent elements is configured with `spacing(pixels)` and updates `rowGap`. Individual children can request flex expansion via `child.layout(s -> s.expand())`.

## Vertical Form Layout

```java
VBox form = new VBox().spacing(10.0f);
form.addChild(new Label("Username"));
form.addChild(new TextField());
form.addChild(new Label("Password"));
form.addChild(new PasswordField());
```

## Related links

- [HBox](<hbox.md>)
- [LinearBox](<linear-box.md>)
- [ScrollView](<scroll-view.md>)

## API

<div class="api-member-card" markdown="1">

### `VBox()` {: #vbox data-toc-label="VBox()" }

**Constructor**

Creates an empty vertical linear container.

</div>

