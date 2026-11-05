---
title: "NodeGraphItem"
slug: "node-graph-item"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraphItem

Элемент-узел на холсте [NodeGraph](<../../../widgets/graph/node-graph.md>), содержащий виджет содержимого и порты подключения.

## Обзор

Элемент-узел на холсте [NodeGraph](<../../../widgets/graph/node-graph.md>), содержащий виджет содержимого и порты подключения. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## API

<div class="api-member-card" markdown="1">

### `NodeGraphItem(String, Widget)` {: #nodegraphitemstring-widget data-toc-label="NodeGraphItem(String, Widget)" }

Создает элемент NodeGraphItem с идентификатором и виджетом содержимого.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `content` | `Widget` | Виджет содержимого. |

</div>

<div class="api-member-card" markdown="1">

### `NodeGraphItem(String, Widget, float, float)` {: #nodegraphitemstring-widget-float-float data-toc-label="NodeGraphItem(String, Widget, float, float)" }

Создает элемент NodeGraphItem с идентификатором, виджетом и начальными координатами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `content` | `Widget` | Виджет содержимого. |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`addPort(NodeGraphPort)` {: #addportnodegraphport data-toc-label="addPort(NodeGraphPort)" }

Прикрепляет существующий экземпляр порта к данному элементу-узлу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `port` | `NodeGraphPort` | Параметр port. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`addPort(String, NodeGraphPortKind, NodeGraphPortSide, float)` {: #addportstring-nodegraphportkind-nodegraphportside-float data-toc-label="addPort(String, NodeGraphPortKind, NodeGraphPortSide, float)" }

Создает и прикрепляет порт с указанным ID, направлением, стороной и смещением.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `kind` | `NodeGraphPortKind` | Направление соединения порта. |
| `side` | `NodeGraphPortSide` | Сторона размещения порта на границе узла. |
| `offset` | `float` | Смещение порта вдоль границы узла в пикселях. |

Возвращает `NodeGraphPort`: Экземпляр созданного порта..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`arrangedHeight()` {: #arrangedheight data-toc-label="arrangedHeight()" }

Возвращает фактическую скомпонованную высоту узла.

Возвращает `float`: Скомпонованная высота в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`arrangedWidth()` {: #arrangedwidth data-toc-label="arrangedWidth()" }

Возвращает фактическую скомпонованную ширину узла.

Возвращает `float`: Скомпонованная ширина в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`autoHeight()` {: #autoheight data-toc-label="autoHeight()" }

Возвращает true, если высота определяется автоматически.

Возвращает `boolean`: true, если высота вычисляется автоматически..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`autoSize()` {: #autosize data-toc-label="autoSize()" }

Сбрасывает ширину и высоту на автоматический расчет по содержимому.

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`autoWidth()` {: #autowidth data-toc-label="autoWidth()" }

Возвращает true, если ширина определяется автоматически.

Возвращает `boolean`: true, если ширина вычисляется автоматически..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`content()` {: #content data-toc-label="content()" }

Возвращает виджет содержимого, размещенный внутри узла.

Возвращает `Widget`: Виджет содержимого..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`height()` {: #height data-toc-label="height()" }

Возвращает явно настроенную высоту узла или значение <= 0 для автоматического размера.

Возвращает `float`: Высота в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`id()` {: #id data-toc-label="id()" }

Возвращает уникальный идентификатор элемента-узла.

Возвращает `String`: Уникальный строковый идентификатор..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`movable()` {: #movable data-toc-label="movable()" }

Возвращает true, если узел может перемещаться перетаскиванием.

Возвращает `boolean`: true, если перемещение разрешено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`movable(boolean)` {: #movableboolean data-toc-label="movable(boolean)" }

Устанавливает, разрешено ли перемещать узел.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `movable` | `boolean` | Разрешено ли перемещение. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`port(String)` {: #portstring data-toc-label="port(String)" }

Возвращает порт с указанным ID, или null.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |

Возвращает `NodeGraphPort`: Экземпляр порта или null, если не найден..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`ports()` {: #ports data-toc-label="ports()" }

Возвращает неизменяемый список всех портов, прикрепленных к узлу.

Возвращает `List`: Неизменяемый список портов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`position(float, float)` {: #positionfloat-float data-toc-label="position(float, float)" }

Устанавливает позицию (X, Y) узла на холсте.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`removePort(String)` {: #removeportstring data-toc-label="removePort(String)" }

Удаляет порт с указанным идентификатором.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`resizable()` {: #resizable data-toc-label="resizable()" }

Возвращает true, если узел поддерживает интерактивное изменение размеров.

Возвращает `boolean`: true, если изменение размера разрешено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`resizable(boolean)` {: #resizableboolean data-toc-label="resizable(boolean)" }

Устанавливает, разрешено ли изменять размеры узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `resizable` | `boolean` | Разрешено ли изменение размера. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`selectable()` {: #selectable data-toc-label="selectable()" }

Возвращает true, если узел может быть выбран кликом или рамкой выделения.

Возвращает `boolean`: true, если выбор разрешен..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`selectable(boolean)` {: #selectableboolean data-toc-label="selectable(boolean)" }

Устанавливает, разрешено ли выбирать узел.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectable` | `boolean` | Разрешен ли выбор. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`selected()` {: #selected data-toc-label="selected()" }

Возвращает true, если узел в данный момент выбран.

Возвращает `boolean`: true, если объект выбран; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`size(float, float)` {: #sizefloat-float data-toc-label="size(float, float)" }

Устанавливает явные размеры ширины и высоты узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина в пикселях. |
| `height` | `float` | Высота в пикселях. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`visible()` {: #visible data-toc-label="visible()" }

Возвращает true, если узел видим на холсте.

Возвращает `boolean`: true, если объект видим; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Устанавливает видимость узла на холсте.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | Видим ли элемент. |

Возвращает `NodeGraphItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`width()` {: #width data-toc-label="width()" }

Возвращает явно настроенную ширину узла или значение <= 0 для автоматического размера.

Возвращает `float`: Ширина в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`x()` {: #x data-toc-label="x()" }

Возвращает координату X позиции узла на холсте.

Возвращает `float`: Координата X..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`y()` {: #y data-toc-label="y()" }

Возвращает координату Y позиции узла на холсте.

Возвращает `float`: Координата Y..

</div>

