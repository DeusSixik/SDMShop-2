---
title: "Label"
slug: "label"
category: "Widgets/Display"
locale: "en"
order: 20
---

# Label

[Text](<text.md>) caption widget associated with an interactive focus target control or form input.

## Overview

`Label` displays single-line or wrapped text captions. When clicked, it automatically transfers input focus to its associated `focusTarget` control (such as a `TextField` or `Checkbox`), improving form accessibility.

## Associated Form Label

```java
TextField nameInput = new TextField();
Label nameLabel = new Label("Username:")
        .labeledControl(nameInput);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Set [Text](<text.md>) Color**

Sets foreground text color.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | — |

Returns `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`focusTarget(Widget)` {: #focustargetwidget data-toc-label="focusTarget(Widget)" }

**Focus Target**

Returns or sets widget receiving focus when label is clicked.

| Parameter | Type | Description |
| --- | --- | --- |
| `focusTarget` | `Widget` | — |

Returns `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`labeledControl(Widget)` {: #labeledcontrolwidget data-toc-label="labeledControl(Widget)" }

**Set Labeled Control**

Associates control as focus target and sets label relationship.

| Parameter | Type | Description |
| --- | --- | --- |
| `control` | `Widget` | — |

Returns `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`textAlignment(Alignment)` {: #textalignmentalignment data-toc-label="textAlignment(Alignment)" }

**Set [Text](<text.md>) Alignment**

Sets horizontal alignment of label text within bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `alignment` | `Alignment` | — |

Returns `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`wrap(boolean)` {: #wrapboolean data-toc-label="wrap(boolean)" }

**Set [Text](<text.md>) Wrapping**

Enables or disables automatic multi-line text wrapping.

| Parameter | Type | Description |
| --- | --- | --- |
| `wrap` | `boolean` | — |

Returns `Label`.

</div>

