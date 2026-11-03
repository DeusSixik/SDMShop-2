---
title: "DatePicker"
slug: "date-picker"
category: "Widgets/Interaction"
locale: "en"
order: 230
---

# DatePicker

Date selection control featuring an ISO date text field and an interactive calendar popup.

## Overview

`DatePicker` pairs an editable `TextField` with a popup monthly calendar grid, allowing users to either type dates in ISO-8601 format or select days visually.

## Basic DatePicker

```java
DatePicker datePicker = new DatePicker()
        .value(LocalDate.now());
datePicker.onDateChanged(e -> filterLogsByDate(e.newValue()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`field()` {: #field data-toc-label="field()" }

**[Text](<../display/text.md>) Field**

Returns editable [TextField](<text-field.md>) widget displaying date.

Returns `TextField`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`onDateChanged(EventListener)` {: #ondatechangedeventlistener data-toc-label="onDateChanged(EventListener)" }

**On Date Changed**

Registers event listener triggered when selected date changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`popup()` {: #popup data-toc-label="popup()" }

**Calendar [Popup](<../feedback/popup.md>)**

Returns calendar popup window.

Returns `Popup`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`value()` {: #value data-toc-label="value()" }

**Get Value**

Returns selected LocalDate value.

Returns `LocalDate`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`value(LocalDate)` {: #valuelocaldate data-toc-label="value(LocalDate)" }

**Set Value**

Sets selected date value.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `LocalDate` | — |

Returns `DatePicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`value(String)` {: #valuestring data-toc-label="value(String)" }

**Set Value String**

Parses ISO-8601 date string and sets selected date.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `String` | — |

Returns `DatePicker`.

</div>

