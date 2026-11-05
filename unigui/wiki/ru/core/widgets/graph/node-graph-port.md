---
title: "NodeGraphPort"
slug: "node-graph-port"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraphPort

Порт подключения на элементе [NodeGraphItem](<node-graph-item.md>) для связывания линиями с другими узлами.

## Обзор

Порт подключения на элементе [NodeGraphItem](<node-graph-item.md>) для связывания линиями с другими узлами. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## API

<div class="api-member-card" markdown="1">

### `NodeGraphPort(String, NodeGraphPortKind, NodeGraphPortSide, float)` {: #nodegraphportstring-nodegraphportkind-nodegraphportside-float data-toc-label="NodeGraphPort(String, NodeGraphPortKind, NodeGraphPortSide, float)" }

Создает порт NodeGraphPort с идентификатором, направлением, стороной размещения и смещением.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `kind` | `NodeGraphPortKind` | Направление соединения порта. |
| `side` | `NodeGraphPortSide` | Сторона размещения порта на границе узла. |
| `offset` | `float` | Смещение порта вдоль границы узла в пикселях. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Возвращает true, если порт интерактивен и может начинать или принимать соединения.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Устанавливает, интерактивен ли порт.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraphPort`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`id()` {: #id data-toc-label="id()" }

Возвращает идентификатор порта, уникальный в пределах узла.

Возвращает `String`: Уникальный строковый идентификатор..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`itemId()` {: #itemid data-toc-label="itemId()" }

Возвращает идентификатор элемента-узла, владеющего данным портом.

Возвращает `String`: Идентификатор элемента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`kind()` {: #kind data-toc-label="kind()" }

Возвращает направление соединения порта (INPUT, OUTPUT, BIDIRECTIONAL).

Возвращает `NodeGraphPortKind`: Тип элемента меню..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`kind(NodeGraphPortKind)` {: #kindnodegraphportkind data-toc-label="kind(NodeGraphPortKind)" }

Устанавливает направление соединения порта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `kind` | `NodeGraphPortKind` | Направление соединения порта. |

Возвращает `NodeGraphPort`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`offset()` {: #offset data-toc-label="offset()" }

Возвращает смещение порта в пикселях вдоль стороны границы узла.

Возвращает `float`: Смещение порта в пикселях вдоль границы узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`offset(float)` {: #offsetfloat data-toc-label="offset(float)" }

Устанавливает смещение порта в пикселях вдоль стороны границы узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `offset` | `float` | Смещение порта вдоль границы узла в пикселях. |

Возвращает `NodeGraphPort`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`ref()` {: #ref data-toc-label="ref()" }

Возвращает составную ссылку [NodeGraphPortRef](<node-graph-port-ref.md>) на данный порт.

Возвращает `NodeGraphPortRef`: Составная ссылка на порт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`side()` {: #side data-toc-label="side()" }

Возвращает сторону размещения порта на границе узла (LEFT, RIGHT, TOP, BOTTOM).

Возвращает `NodeGraphPortSide`: Сторона размещения порта на границе узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`side(NodeGraphPortSide)` {: #sidenodegraphportside data-toc-label="side(NodeGraphPortSide)" }

Устанавливает сторону размещения порта на границе узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `side` | `NodeGraphPortSide` | Сторона размещения порта на границе узла. |

Возвращает `NodeGraphPort`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`type()` {: #type data-toc-label="type()" }

Возвращает строку типа данных или категории совместимости порта.

Возвращает `String`: Строка типа или категории совместимости..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`type(String)` {: #typestring data-toc-label="type(String)" }

Устанавливает строку типа данных или категории совместимости порта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `String` | Строка типа или категории. |

Возвращает `NodeGraphPort`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`visible()` {: #visible data-toc-label="visible()" }

Возвращает true, если порт отображается на узле.

Возвращает `boolean`: true, если объект видим; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Устанавливает видимость порта на узле.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | Видим ли элемент. |

Возвращает `NodeGraphPort`: Текущий экземпляр для цепочки вызовов..

</div>

