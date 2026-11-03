---
title: "ExpandablePanel"
slug: "expandable-panel"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# ExpandablePanel

Сворачиваемая панель с заголовком, шевроном переключения и плавной анимацией высоты раскрытия.

## Обзор

Сворачиваемая панель с заголовком, шевроном переключения и плавной анимацией высоты раскрытия. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
ExpandablePanel panel = new ExpandablePanel("Advanced Settings");
panel.addContent(settingsBox);
```

## Связанные ссылки

- [Accordion](<accordion.md>): Вертикальный контейнер аккордеона, управляющий стопкой раскрывающихся панелей ExpandablePanel с поддержкой режима одиночного открытия.

## API

<div class="api-member-card" markdown="1">

### `ExpandablePanel()` {: #expandablepanel data-toc-label="ExpandablePanel()" }

Создает раскрывающуюся панель ExpandablePanel.

</div>

<div class="api-member-card" markdown="1">

### `ExpandablePanel(RichText)` {: #expandablepanelrichtext data-toc-label="ExpandablePanel(RichText)" }

Создает раскрывающуюся панель с форматированным заголовком [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `RichText` | Текст заголовка. |

</div>

<div class="api-member-card" markdown="1">

### `ExpandablePanel(String)` {: #expandablepanelstring data-toc-label="ExpandablePanel(String)" }

Создает раскрывающуюся панель с указанным текстовым заголовком.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`addContent(Widget)` {: #addcontentwidget data-toc-label="addContent(Widget)" }

Добавляет виджет содержимого в раскрывающуюся панель.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | Экземпляр дочернего узла. |

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`clearContent()` {: #clearcontent data-toc-label="clearContent()" }

Очищает содержимое раскрывающейся панели.

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`contentHost()` {: #contenthost data-toc-label="contentHost()" }

Возвращает контейнер, содержащий содержимое панели.

Возвращает `VBox`: Контейнер, содержащий представления содержимого..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`expanded()` {: #expanded data-toc-label="expanded()" }

Возвращает true, если содержимое панели раскрыто.

Возвращает `boolean`: true, если раскрыто; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`expanded(boolean)` {: #expandedboolean data-toc-label="expanded(boolean)" }

Раскрывает или сворачивает панель с анимацией.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `expanded` | `boolean` | Раскрыт ли узел или панель. |

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`headerButton()` {: #headerbutton data-toc-label="headerButton()" }

Возвращает кнопку-заголовок раскрывающейся панели.

Возвращает `ToggleButton`: Кнопка переключения заголовка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`onExpandedChanged(EventListener)` {: #onexpandedchangedeventlistener data-toc-label="onExpandedChanged(EventListener)" }

Регистрирует слушателя события изменения состояния раскрытия панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`removeContent(Widget)` {: #removecontentwidget data-toc-label="removeContent(Widget)" }

Удаляет виджет содержимого из раскрывающейся панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | Экземпляр дочернего узла. |

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`richTitle()` {: #richtitle data-toc-label="richTitle()" }

Возвращает форматированный заголовок [RichText](<../../core/text/rich-text.md>) панели.

Возвращает `RichText`: Форматированный заголовок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`richTitle(RichText)` {: #richtitlerichtext data-toc-label="richTitle(RichText)" }

Устанавливает форматированный заголовок [RichText](<../../core/text/rich-text.md>) панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `RichText` | Текст заголовка. |

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`silentExpanded(boolean)` {: #silentexpandedboolean data-toc-label="silentExpanded(boolean)" }

Устанавливает состояние раскрытия панели без генерации событий.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `expanded` | `boolean` | Раскрыт ли узел или панель. |

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`title()` {: #title data-toc-label="title()" }

Возвращает обычный текст заголовка панели.

Возвращает `String`: Текстовый заголовок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

Устанавливает обычный текст заголовка панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка. |

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`toggleExpanded()` {: #toggleexpanded data-toc-label="toggleExpanded()" }

Переключает состояние раскрытия панели.

Возвращает `ExpandablePanel`: Текущий экземпляр для цепочки вызовов..

</div>

