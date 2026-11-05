---
title: "DatePicker"
slug: "date-picker"
category: "Widgets/Interaction"
locale: "ru"
order: 230
---

# DatePicker

Виджет выбора даты с текстовым полем ввода ISO-формата и интерактивным всплывающим календарем.

## Обзор

`DatePicker` объединяет текстовое поле `TextField` с интерактивной сеткой календаря, позволяя вводить дату вручную в формате ISO-8601 или выбирать дни кликом.

## Пример DatePicker

```java
DatePicker datePicker = new DatePicker()
        .value(LocalDate.now());
datePicker.onDateChanged(e -> filterLogsByDate(e.newValue()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`field()` {: #field data-toc-label="field()" }

**Текстовое поле**

Возвращает текстовое поле отображения и ввода даты.

Возвращает `TextField`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`onDateChanged(EventListener)` {: #ondatechangedeventlistener data-toc-label="onDateChanged(EventListener)" }

**Слушатель изменения даты**

Регистрирует слушатель событий смены выбранной даты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`popup()` {: #popup data-toc-label="popup()" }

**Всплывающий календарь**

Возвращает всплывающее окно календаря.

Возвращает `Popup`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`value()` {: #value data-toc-label="value()" }

**Получить дату**

Возвращает выбранное значение даты LocalDate.

Возвращает `LocalDate`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`value(LocalDate)` {: #valuelocaldate data-toc-label="value(LocalDate)" }

**Задать дату**

Устанавливает выбранную дату.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `LocalDate` | — |

Возвращает `DatePicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DatePicker::</span>`value(String)` {: #valuestring data-toc-label="value(String)" }

**Задать дату строкой**

Парсит строку даты ISO-8601 и устанавливает выбранную дату.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `String` | — |

Возвращает `DatePicker`.

</div>

