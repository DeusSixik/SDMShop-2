---
title: "DockNode"
slug: "dock-node"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockNode

Иерархический узел дерева докинга: листовой узел (LEAF) со вкладками [DockPane](<dock-pane.md>) или узел разделения (SPLIT), делящий детей горизонтально или вертикально.

## Обзор

`DockNode` — древовидный узел структуры докинга.

- **Листовой узел (`LEAF`)**: Содержит список сгруппированных вкладок `DockPane` и индекс выбранной вкладки `selectedIndex`.
- **Разделительный узел (`SPLIT`)**: Разделяет два дочерних поддерева (`first`, `second`) по горизонтали или вертикали с пропорцией `splitRatio`.

## Связанные ссылки

- [DockPane](<dock-pane.md>): Дескриптор отдельной панели докинга, содержащей виджет содержимого, заголовок вкладки, иконку, признак несохраненных правок и стражи закрытия.
- [DockingManager](<docking-manager.md>): Невизуальный контроллер раскладки, управляющий иерархическим деревом докинга, добавлением панелей, разделениями, порядком вкладок и снимками раскладки.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`containsPane(String)` {: #containspanestring data-toc-label="containsPane(String)" }

Рекурсивно проверяет, содержится ли панель с указанным ID в данном поддереве.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор искомой панели. |

Возвращает `boolean`: True, если поддерево содержит панель с указанным ID..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`empty()` {: #empty data-toc-label="empty()" }

Проверяет, пуст ли узел и не содержит ли дочерних панелей.

Возвращает `boolean`: True, если узел не содержит панелей..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`findPane(String)` {: #findpanestring data-toc-label="findPane(String)" }

Рекурсивно выполняет поиск панели по строковому идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор искомой панели. |

Возвращает `DockPane`: Найденная панель [DockPane](<dock-pane.md>) или null, если не найдена..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`first()` {: #first data-toc-label="first()" }

Возвращает первый дочерний узел (слева или сверху).

Возвращает `DockNode`: Запрошенный дочерний узел..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`id()` {: #id data-toc-label="id()" }

Возвращает уникальный строковый идентификатор узла дерева.

Возвращает `String`: Уникальный идентификатор узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`isLeaf()` {: #isleaf data-toc-label="isLeaf()" }

Проверяет, является ли узел листовым и содержит ли вкладки.

Возвращает `boolean`: True, если это листовой узел, содержащий вкладки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`isSplit()` {: #issplit data-toc-label="isSplit()" }

Проверяет, является ли узел разделительным и содержит ли два дочерних поддерева.

Возвращает `boolean`: True, если это разделительный узел, содержащий два дочерних узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`kind()` {: #kind data-toc-label="kind()" }

Возвращает тип узла дерева (LEAF или SPLIT).

Возвращает `Kind`: Тип классификации узла (LEAF или SPLIT)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`leaf()` {: #leaf data-toc-label="leaf()" }

Создает пустой листовой узел дерева докинга.

Возвращает `DockNode`: Созданный экземпляр узла DockNode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`leaf(DockPane)` {: #leafdockpane data-toc-label="leaf(DockPane)" }

Создает листовой узел дерева докинга с начальной панелью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pane` | `DockPane` | Панель [DockPane](<dock-pane.md>) для размещения в листовом узле. |

Возвращает `DockNode`: Созданный экземпляр узла DockNode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`orientation()` {: #orientation data-toc-label="orientation()" }

Возвращает ориентацию разделения дочерних узлов (HORIZONTAL или VERTICAL).

Возвращает `DockSplitOrientation`: Ориентация разделения или null для листовых узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`panes()` {: #panes data-toc-label="panes()" }

Возвращает неизменяемый список панелей докинга, размещенных в листовом узле.

Возвращает `List`: Неизменяемый список вкладок [DockPane](<dock-pane.md>) в данном листовом узле..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`second()` {: #second data-toc-label="second()" }

Возвращает второй дочерний узел (справа или снизу).

Возвращает `DockNode`: Запрошенный дочерний узел..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Возвращает индекс текущей выбранной активной вкладки в листовом узле.

Возвращает `int`: Индекс выбранной вкладки (начиная с нуля)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`selectedPane()` {: #selectedpane data-toc-label="selectedPane()" }

Возвращает активную выбранную панель [DockPane](<dock-pane.md>) в листовом узле.

Возвращает `DockPane`: Активная выбранная панель [DockPane](<dock-pane.md>) или null, если пусто..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`split(DockSplitOrientation, DockNode, DockNode, float)` {: #splitdocksplitorientation-docknode-docknode-float data-toc-label="split(DockSplitOrientation, DockNode, DockNode, float)" }

Создает разделительный узел, объединяющий два дочерних узла с заданной ориентацией и коэффициентом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `DockSplitOrientation` | Ориентация разделения между двумя дочерними узлами. |
| `first` | `DockNode` | Первый дочерний узел. |
| `second` | `DockNode` | Второй дочерний узел. |
| `splitRatio` | `float` | Коэффициент разделения пространства (от 0.0 до 1.0). |

Возвращает `DockNode`: Созданный экземпляр узла DockNode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`splitRatio()` {: #splitratio data-toc-label="splitRatio()" }

Возвращает коэффициент разделения пространства между дочерними узлами.

Возвращает `float`: Коэффициент разделения между двумя дочерними узлами..

</div>

