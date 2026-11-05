---
title: "BreadcrumbItem"
slug: "breadcrumb-item"
category: "Core/Виджеты/Навигация"
locale: "ru"
order: 0
---

# BreadcrumbItem

Дескриптор навигационного элемента, представляющий шаг или крошку в цепочке навигации.

## Обзор

Обзор и руководство по использованию.

## Базовое использование

```java
BreadcrumbItem item = new BreadcrumbItem("Dashboard");
item.enabled(true);
```

## Связанные ссылки

- [Breadcrumb](<../../../widgets/navigation/breadcrumb.md>): Интерактивная цепочка навигации («хлебные крошки»), отображающая иерархические элементы с настраиваемыми разделителями и обработкой выбора.

## API

<div class="api-member-card" markdown="1">

### `BreadcrumbItem()` {: #breadcrumbitem data-toc-label="BreadcrumbItem()" }

Создает пустой элемент навигационной цепочки с пустой меткой.

</div>

<div class="api-member-card" markdown="1">

### `BreadcrumbItem(RichText)` {: #breadcrumbitemrichtext data-toc-label="BreadcrumbItem(RichText)" }

Создает элемент цепочки с форматированной текстовой меткой [RichText](<../../text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текстовая метка. |

</div>

<div class="api-member-card" markdown="1">

### `BreadcrumbItem(String)` {: #breadcrumbitemstring data-toc-label="BreadcrumbItem(String)" }

Создает элемент цепочки с текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Возвращает true, если элемент интерактивен и доступен для нажатия.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Устанавливает, доступен ли элемент для нажатия.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `BreadcrumbItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`richText()` {: #richtext data-toc-label="richText()" }

Возвращает форматированный текст [RichText](<../../text/rich-text.md>) метки элемента.

Возвращает `RichText`: Форматированное текстовое представление..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

Устанавливает форматированный текст [RichText](<../../text/rich-text.md>) метки элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текстовая метка. |

Возвращает `BreadcrumbItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`text()` {: #text data-toc-label="text()" }

Возвращает обычный текст метки элемента.

Возвращает `String`: Текстовая строка метки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Устанавливает обычный текст метки элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

Возвращает `BreadcrumbItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`value()` {: #value data-toc-label="value()" }

Возвращает семантическое строковое значение элемента.

Возвращает `String`: Семантическое значение идентификатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`value(String)` {: #valuestring data-toc-label="value(String)" }

Устанавливает семантическое строковое значение элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `String` | Семантическое значение. |

Возвращает `BreadcrumbItem`: Текущий экземпляр для цепочки вызовов..

</div>

