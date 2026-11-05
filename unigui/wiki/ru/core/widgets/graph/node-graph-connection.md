---
title: "NodeGraphConnection"
slug: "node-graph-connection"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraphConnection

Соединение между двумя портами узлов в [NodeGraph](<../../../widgets/graph/node-graph.md>).

## Обзор

Соединение между двумя портами узлов в [NodeGraph](<../../../widgets/graph/node-graph.md>). Предоставляет широкие возможности настройки компоновки и взаимодействия.

## API

<div class="api-member-card" markdown="1">

### `NodeGraphConnection(String, NodeGraphPortRef, NodeGraphPortRef)` {: #nodegraphconnectionstring-nodegraphportref-nodegraphportref data-toc-label="NodeGraphConnection(String, NodeGraphPortRef, NodeGraphPortRef)" }

Создает соединение между портами по ссылкам на порты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `from` | `NodeGraphPortRef` | Ссылка на исходный порт. |
| `to` | `NodeGraphPortRef` | Ссылка на целевой порт. |

</div>

<div class="api-member-card" markdown="1">

### `NodeGraphConnection(String, String, String, String, String)` {: #nodegraphconnectionstring-string-string-string-string data-toc-label="NodeGraphConnection(String, String, String, String, String)" }

Создает соединение между портами по строковым идентификаторам элементов и портов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `fromItemId` | `String` | Идентификатор исходного элемента. |
| `fromPortId` | `String` | Идентификатор исходного порта. |
| `toItemId` | `String` | Идентификатор целевого элемента. |
| `toPortId` | `String` | Идентификатор целевого порта. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Возвращает true, если соединение активно и включено.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Устанавливает, активно ли соединение.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraphConnection`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`from()` {: #from data-toc-label="from()" }

Возвращает ссылку на исходный порт соединения.

Возвращает `NodeGraphPortRef`: Ссылка на исходный порт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`fromItemId()` {: #fromitemid data-toc-label="fromItemId()" }

Возвращает идентификатор исходного элемента-узла.

Возвращает `String`: Идентификатор элемента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`fromPortId()` {: #fromportid data-toc-label="fromPortId()" }

Возвращает идентификатор исходного порта.

Возвращает `String`: Идентификатор порта..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`id()` {: #id data-toc-label="id()" }

Возвращает уникальный идентификатор соединения.

Возвращает `String`: Уникальный строковый идентификатор..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`selected()` {: #selected data-toc-label="selected()" }

Возвращает true, если данное соединение в настоящий момент выбрано.

Возвращает `boolean`: true, если объект выбран; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`to()` {: #to data-toc-label="to()" }

Возвращает ссылку на целевой порт соединения.

Возвращает `NodeGraphPortRef`: Ссылка на целевой порт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`toItemId()` {: #toitemid data-toc-label="toItemId()" }

Возвращает идентификатор целевого элемента-узла.

Возвращает `String`: Идентификатор элемента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`toPortId()` {: #toportid data-toc-label="toPortId()" }

Возвращает идентификатор целевого порта.

Возвращает `String`: Идентификатор порта..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`type()` {: #type data-toc-label="type()" }

Возвращает строку типа данных или категории совместимости соединения.

Возвращает `String`: Строка типа или категории совместимости..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`type(String)` {: #typestring data-toc-label="type(String)" }

Устанавливает строку типа данных или категории совместимости соединения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `String` | Строка типа или категории. |

Возвращает `NodeGraphConnection`: Текущий экземпляр для цепочки вызовов..

</div>

