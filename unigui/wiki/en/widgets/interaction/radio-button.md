---
title: "RadioButton"
slug: "radio-button"
category: "Widgets/Interaction"
locale: "en"
order: 90
---

# RadioButton

Circular option selector for mutually exclusive choices inside a [RadioGroup](<../../core/widgets/interaction/radio-group.md>).

## Overview

`RadioButton` displays a round selector dot alongside text. When joined with a `RadioGroup`, selecting one button automatically deselects all other buttons in that group.

## Option Selection

```java
RadioGroup difficulty = new RadioGroup();
RadioButton easy = new RadioButton("Easy", "easy").group(difficulty);
RadioButton hard = new RadioButton("Hard", "hard").group(difficulty);
easy.checked(true);
```

## Related links

- [Checkbox](<checkbox.md>)
- [RadioGroup](<../../core/widgets/interaction/radio-group.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RadioButton::</span>`group(RadioGroup)` {: #groupradiogroup data-toc-label="group(RadioGroup)" }

**Assign [RadioGroup](<../../core/widgets/interaction/radio-group.md>)**

Assigns the [RadioGroup](<../../core/widgets/interaction/radio-group.md>) managing mutual exclusivity.

| Parameter | Type | Description |
| --- | --- | --- |
| `group` | `RadioGroup` | — |

Returns `RadioButton`: this widget for fluent configuration.

</div>

