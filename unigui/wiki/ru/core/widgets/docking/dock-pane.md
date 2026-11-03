---
title: "DockPane"
slug: "dock-pane"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockPane

Дескриптор отдельной панели докинга, содержащей виджет содержимого, заголовок вкладки, иконку, признак несохраненных правок и стражи закрытия.

## Обзор

`DockPane` представляет отдельную панель докинга с произвольным содержимым.

### Возможности

- **Настройка заголовка**: Обычный текст, форматированный заголовок [RichText](<../../text/rich-text.md>) и иконка (`icon()`).
- **Признак изменений**: `dirty(true)` отображает индикатор несохраненных правок (точку или маркер).
- **Защита от закрытия**: `onCloseRequested(guard)` предотвращает случайное закрытие до подтверждения пользователем.
- **Классификация**: Настраивается как `DOCUMENT` или `TOOL`.

## Создание панелей документов и инструментов

```java
DockPane doc = DockPane.document("main-editor", "Main.java", editorWidget)
        .dirty(true)
        .closable(true);
```

## Связанные ссылки

- [DockPaneKind](<dock-pane-kind.md>): Классификация панели докинга: DOCUMENT (основное содержимое рабочей области) или TOOL (вспомогательное инструментальное окно).
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### `DockPane(String, RichText, Widget)` {: #dockpanestring-richtext-widget data-toc-label="DockPane(String, RichText, Widget)" }

Создает панель докинга с явным идентификатором, форматированным заголовком [RichText](<../../text/rich-text.md>) и содержимым.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `RichText` | Форматированный заголовок панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

</div>

<div class="api-member-card" markdown="1">

### `DockPane(String, String, Widget)` {: #dockpanestring-string-widget data-toc-label="DockPane(String, String, Widget)" }

Создает панель с идентификатором, заголовком и виджетом содержимого.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `String` | Текст заголовка панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

</div>

<div class="api-member-card" markdown="1">

### `DockPane(String, Widget)` {: #dockpanestring-widget data-toc-label="DockPane(String, Widget)" }

Создает панель докинга со сгенерированным уникальным идентификатором.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`autoHide()` {: #autohide data-toc-label="autoHide()" }

Проверяет, включено ли автоматическое сворачивание панели при потере фокуса.

Возвращает `boolean`: True, если автоскрытие включено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`autoHide(boolean)` {: #autohideboolean data-toc-label="autoHide(boolean)" }

Настраивает автоматическое сворачивание панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `autoHide` | `boolean` | Сворачивается ли панель автоматически при потере фокуса. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`canClose()` {: #canclose data-toc-label="canClose()" }

Вызывает зарегистрированный страж закрытия для подтверждения или отмены закрытия панели.

Возвращает `boolean`: True, если закрытие одобрено стражем; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`closable()` {: #closable data-toc-label="closable()" }

Проверяет, отображает ли панель кнопку закрытия и разрешено ли ее закрытие.

Возвращает `boolean`: True, если панель имеет кнопку закрытия и может быть закрыта..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`closable(boolean)` {: #closableboolean data-toc-label="closable(boolean)" }

Настраивает возможность закрытия панели пользователем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closable` | `boolean` | Можно ли закрыть панель. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`content()` {: #content data-toc-label="content()" }

Возвращает виджет содержимого, размещенный внутри панели.

Возвращает `Widget`: Виджет, размещенный внутри данной панели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`dirty()` {: #dirty data-toc-label="dirty()" }

Проверяет, содержит ли панель несохраненные изменения данных.

Возвращает `boolean`: True, если панель содержит несохраненные изменения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`dirty(boolean)` {: #dirtyboolean data-toc-label="dirty(boolean)" }

Задает признак наличия несохраненных изменений в панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dirty` | `boolean` | Имеет ли панель несохраненные изменения. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`document()` {: #document data-toc-label="document()" }

Проверяет, классифицирована ли данная панель как документ рабочей области.

Возвращает `boolean`: True, если тип панели — DOCUMENT..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`document(String, RichText, Widget)` {: #documentstring-richtext-widget data-toc-label="document(String, RichText, Widget)" }

Создает документную панель с форматированным заголовком [RichText](<../../text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `RichText` | Форматированный заголовок панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

Возвращает `DockPane`: Созданный экземпляр панели DockPane..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`document(String, String, Widget)` {: #documentstring-string-widget data-toc-label="document(String, String, Widget)" }

Создает документную панель со строковым заголовком.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `String` | Текст заголовка панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

Возвращает `DockPane`: Созданный экземпляр панели DockPane..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`icon()` {: #icon data-toc-label="icon()" }

Возвращает иконку вкладки панели или null.

Возвращает `UiIcon`: Иконка в заголовке вкладки или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`icon(UiIcon)` {: #iconuiicon data-toc-label="icon(UiIcon)" }

Задает иконку вкладки панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `icon` | `UiIcon` | Иконка, отображаемая в заголовке вкладки. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`id()` {: #id data-toc-label="id()" }

Возвращает уникальный строковый идентификатор панели.

Возвращает `String`: Уникальная строка-идентификатор панели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`kind()` {: #kind data-toc-label="kind()" }

Возвращает классификацию типа панели (DOCUMENT или TOOL).

Возвращает `DockPaneKind`: Тип панели (DOCUMENT или TOOL)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`kind(DockPaneKind)` {: #kinddockpanekind data-toc-label="kind(DockPaneKind)" }

Задает классификацию типа панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `kind` | `DockPaneKind` | Классификация типа панели (DOCUMENT или TOOL). |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`onCloseRequested(Predicate)` {: #oncloserequestedpredicate data-toc-label="onCloseRequested(Predicate)" }

Регистрирует предикат-страж, подтверждающий закрытие панели (например, для проверки несохраненных правок).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `guard` | `Predicate` | Предикат-страж, подтверждающий или отклоняющий закрытие панели. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`pinned()` {: #pinned data-toc-label="pinned()" }

Проверяет, закреплена ли вкладка панели в начале полосы вкладок.

Возвращает `boolean`: True, если панель закреплена во вкладках..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`pinned(boolean)` {: #pinnedboolean data-toc-label="pinned(boolean)" }

Настраивает закрепление вкладки панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pinned` | `boolean` | Закреплена ли панель во вкладках. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`richTitle()` {: #richtitle data-toc-label="richTitle()" }

Возвращает форматированный заголовок [RichText](<../../text/rich-text.md>) или null.

Возвращает `RichText`: Форматированный заголовок панели или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`richTitle(RichText)` {: #richtitlerichtext data-toc-label="richTitle(RichText)" }

Задает форматированный заголовок [RichText](<../../text/rich-text.md>) для панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `RichText` | Форматированный заголовок панели. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`title()` {: #title data-toc-label="title()" }

Возвращает текстовый заголовок вкладки панели.

Возвращает `String`: Строка текстового заголовка панели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

Задает обычный строковый заголовок для панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка панели. |

Возвращает `DockPane`: Данный экземпляр DockPane для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`tool()` {: #tool data-toc-label="tool()" }

Проверяет, классифицирована ли данная панель как вспомогательный инструмент.

Возвращает `boolean`: True, если тип панели — TOOL..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`tool(String, RichText, Widget)` {: #toolstring-richtext-widget data-toc-label="tool(String, RichText, Widget)" }

Создает инструментальную панель с форматированным заголовком [RichText](<../../text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `RichText` | Форматированный заголовок панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

Возвращает `DockPane`: Созданный экземпляр панели DockPane..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`tool(String, String, Widget)` {: #toolstring-string-widget data-toc-label="tool(String, String, Widget)" }

Создает инструментальную панель со строковым заголовком.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `String` | Текст заголовка панели. |
| `content` | `Widget` | Виджет содержимого, отображаемый внутри панели. |

Возвращает `DockPane`: Созданный экземпляр панели DockPane..

</div>

