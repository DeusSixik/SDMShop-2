---
title: "SettingRow"
slug: "setting-row"
category: "Widgets/Containers"
locale: "en"
order: 150
---

# SettingRow

Specialized settings row holding a label on the left and an interactive control on the right.

## Overview

SettingRow is a specialized subclass of `PanelRowWidget` created specifically for settings dialogs, preference panes, and option forms. The left slot hosts an integrated `Label`, while the right slot contains the interactive control (such as a [Slider](<../interaction/slider.md>), [Checkbox](<../interaction/checkbox.md>), Dropdown, or [TextField](<../interaction/text-field.md>)).

## Focus Redirection

A key usability feature of SettingRow is automatic focus redirection: the left `Label` is configured with `label.focusTarget(control)`, so clicking on the label automatically passes keyboard and pointer focus to the associated control.

## Volume Setting Row

```java
SettingRow volumeRow = new SettingRow("Master Volume", volumeSlider)
        .controlWidth(120.0f)
        .rowHeight(24.0f);
```

## Related links

- [Box](<box.md>)
- [PanelRowWidget](<panel-row-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`label()` {: #label data-toc-label="label()" }

**Get [Label](<../display/label.md>)**

Returns the underlying [Label](<../display/label.md>) instance for advanced text styling.

Returns `Label`: the live [Label](<../display/label.md>) instance.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`labelText(String)` {: #labeltextstring data-toc-label="labelText(String)" }

**Set [Label](<../display/label.md>) [Text](<../display/text.md>)**

Sets the plain-text title for the setting label.

| Parameter | Type | Description |
| --- | --- | --- |
| `labelText` | `String` | label text string |

Returns `SettingRow`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`control(Widget)` {: #controlwidget data-toc-label="control(Widget)" }

**Set Control**

Sets the interactive control widget on the right side.

| Parameter | Type | Description |
| --- | --- | --- |
| `control` | `Widget` | the control widget (e.g. [Slider](<../interaction/slider.md>), Toggle, [TextField](<../interaction/text-field.md>)) |

Returns `SettingRow`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`controlWidth(float)` {: #controlwidthfloat data-toc-label="controlWidth(float)" }

**Set Control Width**

Sets a fixed width for the right control slot.

| Parameter | Type | Description |
| --- | --- | --- |
| `controlWidth` | `float` | width in pixels or AUTO |

Returns `SettingRow`: this widget for fluent configuration.

</div>

