---
title: "RadioGroup"
slug: "radio-group"
category: "Core/Widgets/Interaction"
locale: "en"
order: 100
---

# RadioGroup

Controller managing mutual exclusivity and selection state across multiple [RadioButton](<../../../widgets/interaction/radio-button.md>) instances.

## Overview

`RadioGroup` is a logical manager that coordinates a collection of `RadioButton` widgets. It ensures only one button is selected at any time and allows querying the selected button or value directly.

## RadioGroup Controller

```java
RadioGroup group = new RadioGroup()
        .add(optionA)
        .add(optionB);
String selected = group.selectedValue();
```

## Related links

- [RadioButton](<../../../widgets/interaction/radio-button.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RadioGroup::</span>`add(RadioButton)` {: #addradiobutton data-toc-label="add(RadioButton)" }

**Add [RadioButton](<../../../widgets/interaction/radio-button.md>)**

Adds a [RadioButton](<../../../widgets/interaction/radio-button.md>) to the managed exclusive group.

| Parameter | Type | Description |
| --- | --- | --- |
| `button` | `RadioButton` | — |

Returns `RadioGroup`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RadioGroup::</span>`selectedValue()` {: #selectedvalue data-toc-label="selectedValue()" }

**Get Selected Value**

Returns the value string of currently selected [RadioButton](<../../../widgets/interaction/radio-button.md>), or null.

Returns `String`.

</div>

