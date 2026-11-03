---
title: "NodeGraph"
slug: "node-graph"
category: "Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraph

Интерактивный холст 2D-графа узлов с перемещением блоков, соединениями портов, панорамированием, масштабированием и рамкой выделения.

## Обзор

Интерактивный холст 2D-графа узлов с перемещением блоков, соединениями портов, панорамированием, масштабированием и рамкой выделения. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
NodeGraph graph = new NodeGraph();
NodeGraphItem itemA = graph.addItem("nodeA", new Label("A"), 50, 50);
itemA.addPort("out", NodeGraphPortKind.OUTPUT, NodeGraphPortSide.RIGHT, 10);
NodeGraphItem itemB = graph.addItem("nodeB", new Label("B"), 200, 50);
itemB.addPort("in", NodeGraphPortKind.INPUT, NodeGraphPortSide.LEFT, 10);
graph.addConnection("c1", "nodeA", "out", "nodeB", "in");
```

## Связанные ссылки

- [GraphView](<graph-view.md>): Виджет статической или интерактивной диаграммы графа с узлами, ребрами, подписями и всплывающими подсказками.
- [NodeGraphConnection](<../../core/widgets/graph/node-graph-connection.md>): Соединение между двумя портами узлов в NodeGraph.
- [NodeGraphItem](<../../core/widgets/graph/node-graph-item.md>): Элемент-узел на холсте NodeGraph, содержащий виджет содержимого и порты подключения.

## API

<div class="api-member-card" markdown="1">

### `NodeGraph()` {: #nodegraph data-toc-label="NodeGraph()" }

Создает новый экземпляр NodeGraph со стандартной областью просмотра, сеткой и стилями.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addConnection(NodeGraphConnection)` {: #addconnectionnodegraphconnection data-toc-label="addConnection(NodeGraphConnection)" }

Добавляет существующий экземпляр соединения в граф.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `connection` | `NodeGraphConnection` | Параметр connection. |

Возвращает `NodeGraphConnection`: Экземпляр созданного соединения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addConnection(String, NodeGraphPortRef, NodeGraphPortRef)` {: #addconnectionstring-nodegraphportref-nodegraphportref data-toc-label="addConnection(String, NodeGraphPortRef, NodeGraphPortRef)" }

Создает и добавляет соединение между ссылками на порты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор соединения. |
| `from` | `NodeGraphPortRef` | Ссылка на исходный порт. |
| `to` | `NodeGraphPortRef` | Ссылка на целевой порт. |

Возвращает `NodeGraphConnection`: Экземпляр созданного соединения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addConnection(String, String, String, String, String)` {: #addconnectionstring-string-string-string-string data-toc-label="addConnection(String, String, String, String, String)" }

Создает и добавляет соединение между указанными элементами и портами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор соединения. |
| `fromItemId` | `String` | Идентификатор исходного элемента. |
| `fromPortId` | `String` | Идентификатор исходного порта. |
| `toItemId` | `String` | Идентификатор целевого элемента. |
| `toPortId` | `String` | Идентификатор целевого порта. |

Возвращает `NodeGraphConnection`: Экземпляр созданного соединения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addItem(NodeGraphItem)` {: #additemnodegraphitem data-toc-label="addItem(NodeGraphItem)" }

Добавляет существующий элемент-узел в граф.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `NodeGraphItem` | Целевой элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addItem(String, Widget, float, float)` {: #additemstring-widget-float-float data-toc-label="addItem(String, Widget, float, float)" }

Создает и размещает новый элемент-узел с указанным ID, содержимым и координатами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор элемента. |
| `content` | `Widget` | Виджет содержимого. |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |

Возвращает `NodeGraphItem`: Экземпляр созданного элемента-узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`backgroundColor()` {: #backgroundcolor data-toc-label="backgroundColor()" }

Возвращает изменяемый цвет фона холста графа.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`bringToFrontOnSelect()` {: #bringtofrontonselect data-toc-label="bringToFrontOnSelect()" }

Возвращает true, если выбранный узел перемещается на передний план.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`bringToFrontOnSelect(boolean)` {: #bringtofrontonselectboolean data-toc-label="bringToFrontOnSelect(boolean)" }

Устанавливает, перемещается ли выбранный узел на передний план.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `bringToFrontOnSelect` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clearConnectionSelection()` {: #clearconnectionselection data-toc-label="clearConnectionSelection()" }

Снимает выбор со всех соединений.

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clearItems()` {: #clearitems data-toc-label="clearItems()" }

Удаляет все элементы-узлы и соединения из графа.

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clearSelection()` {: #clearselection data-toc-label="clearSelection()" }

Снимает выбор со всех элементов-узлов.

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clippingEnabled()` {: #clippingenabled data-toc-label="clippingEnabled()" }

Возвращает true, если отрисовка содержимого холста ограничена границами виджета.

Возвращает `boolean`: true, если ограничение отрисовки включено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clippingEnabled(boolean)` {: #clippingenabledboolean data-toc-label="clippingEnabled(boolean)" }

Устанавливает, ограничена ли отрисовка содержимого холста границами виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `clippingEnabled` | `boolean` | Включено ли ограничение отрисовки границами виджета. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connection(String)` {: #connectionstring data-toc-label="connection(String)" }

Возвращает соединение по указанному ID, или null.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор соединения. |

Возвращает `NodeGraphConnection`: Экземпляр соединения или null, если не найден..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionColor()` {: #connectioncolor data-toc-label="connectionColor()" }

Возвращает цвет линий соединений по умолчанию.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionDragging()` {: #connectiondragging data-toc-label="connectionDragging()" }

Возвращает true, если в данный момент создается соединение перетаскиванием из порта.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionPolicy()` {: #connectionpolicy data-toc-label="connectionPolicy()" }

Возвращает активную политику валидации соединений.

Возвращает `NodeGraphConnectionPolicy`: Активная политика валидации соединений..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionPolicy(NodeGraphConnectionPolicy)` {: #connectionpolicynodegraphconnectionpolicy data-toc-label="connectionPolicy(NodeGraphConnectionPolicy)" }

Устанавливает политику валидации соединений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `connectionPolicy` | `NodeGraphConnectionPolicy` | Политика валидации соединений. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionPreviewColor()` {: #connectionpreviewcolor data-toc-label="connectionPreviewColor()" }

Возвращает цвет линии предварительного просмотра допустимого соединения.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connections()` {: #connections data-toc-label="connections()" }

Возвращает неизменяемый список всех соединений в графе.

Возвращает `List`: Неизменяемый список соединений..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`consumeWheelWhileHovered()` {: #consumewheelwhilehovered data-toc-label="consumeWheelWhileHovered()" }

Возвращает true, если события колеса мыши поглощаются при наведении на холст.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`consumeWheelWhileHovered(boolean)` {: #consumewheelwhilehoveredboolean data-toc-label="consumeWheelWhileHovered(boolean)" }

Устанавливает, поглощаются ли события колеса мыши при наведении на холст.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `consumeWheelWhileHovered` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`draggingItemId()` {: #draggingitemid data-toc-label="draggingItemId()" }

Возвращает ID перемещаемого в данный момент элемента или пустую строку.

Возвращает `String`: Идентификатор перемещаемого элемента или пустая строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`gridColor()` {: #gridcolor data-toc-label="gridColor()" }

Возвращает цвет тонких вспомогательных линий сетки.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`gridSize()` {: #gridsize data-toc-label="gridSize()" }

Возвращает шаг ячеек сетки в пикселях.

Возвращает `float`: Размер ячейки сетки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`gridSize(float)` {: #gridsizefloat data-toc-label="gridSize(float)" }

Устанавливает шаг ячеек сетки в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `gridSize` | `float` | Размер ячейки сетки в пикселях. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hasConnection(NodeGraphPortRef, NodeGraphPortRef)` {: #hasconnectionnodegraphportref-nodegraphportref data-toc-label="hasConnection(NodeGraphPortRef, NodeGraphPortRef)" }

Возвращает true, если соединение между указанными портами уже существует.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `from` | `NodeGraphPortRef` | Ссылка на исходный порт. |
| `to` | `NodeGraphPortRef` | Ссылка на целевой порт. |

Возвращает `boolean`: true, если соединение существует; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredConnectionId()` {: #hoveredconnectionid data-toc-label="hoveredConnectionId()" }

Возвращает ID соединения под курсором или пустую строку.

Возвращает `String`: Идентификатор соединения под курсором или пустая строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredItemBorderColor()` {: #hovereditembordercolor data-toc-label="hoveredItemBorderColor()" }

Возвращает цвет границы элементов-узлов при наведении.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredItemId()` {: #hovereditemid data-toc-label="hoveredItemId()" }

Возвращает ID элемента-узла под курсором или пустую строку.

Возвращает `String`: Идентификатор элемента под курсором или пустая строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredPort()` {: #hoveredport data-toc-label="hoveredPort()" }

Возвращает ссылку на порт под курсором или null.

Возвращает `NodeGraphPortRef`: Ссылка на порт под курсором или null, если отсутствует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredPortColor()` {: #hoveredportcolor data-toc-label="hoveredPortColor()" }

Возвращает цвет заливки портов узлов при наведении.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`invalidConnectionPreviewColor()` {: #invalidconnectionpreviewcolor data-toc-label="invalidConnectionPreviewColor()" }

Возвращает цвет линии предварительного просмотра недопустимого соединения.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`item(String)` {: #itemstring data-toc-label="item(String)" }

Возвращает элемент-узел по указанному идентификатору, или null.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор элемента. |

Возвращает `NodeGraphItem`: Экземпляр элемента-узла или null, если не найден..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemBorderColor()` {: #itembordercolor data-toc-label="itemBorderColor()" }

Возвращает цвет границы элементов-узлов по умолчанию.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemContentPadding()` {: #itemcontentpadding data-toc-label="itemContentPadding()" }

Возвращает внутренний отступ содержимого элементов-узлов.

Возвращает `float`: Внутренний отступ содержимого в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemContentPadding(float)` {: #itemcontentpaddingfloat data-toc-label="itemContentPadding(float)" }

Устанавливает внутренний отступ содержимого элементов-узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `padding` | `float` | Внутренний отступ в пикселях. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemDraggingEnabled()` {: #itemdraggingenabled data-toc-label="itemDraggingEnabled()" }

Возвращает true, если перемещение узлов перетаскиванием включено.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemDraggingEnabled(boolean)` {: #itemdraggingenabledboolean data-toc-label="itemDraggingEnabled(boolean)" }

Устанавливает, разрешено ли перемещение узлов перетаскиванием.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `itemDraggingEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`items()` {: #items data-toc-label="items()" }

Возвращает неизменяемый список всех элементов-узлов графа.

Возвращает `List`: Неизменяемый список элементов-узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`keyboardEditingEnabled()` {: #keyboardeditingenabled data-toc-label="keyboardEditingEnabled()" }

Возвращает true, если горячие клавиши редактирования (Delete, стрелки) включены.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`keyboardEditingEnabled(boolean)` {: #keyboardeditingenabledboolean data-toc-label="keyboardEditingEnabled(boolean)" }

Устанавливает, включены ли горячие клавиши редактирования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `keyboardEditingEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`lassoSelecting()` {: #lassoselecting data-toc-label="lassoSelecting()" }

Возвращает true, если в данный момент растягивается рамка лассо-выделения.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`lassoSelectionEnabled()` {: #lassoselectionenabled data-toc-label="lassoSelectionEnabled()" }

Возвращает true, если выделение прямоугольной рамкой включено.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`lassoSelectionEnabled(boolean)` {: #lassoselectionenabledboolean data-toc-label="lassoSelectionEnabled(boolean)" }

Устанавливает, включено ли выделение прямоугольной рамкой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `lassoSelectionEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`majorGridColor()` {: #majorgridcolor data-toc-label="majorGridColor()" }

Возвращает цвет основных линий сетки.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`maxZoom()` {: #maxzoom data-toc-label="maxZoom()" }

Возвращает максимальный коэффициент масштабирования.

Возвращает `float`: Максимальный коэффициент масштабирования..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`minZoom()` {: #minzoom data-toc-label="minZoom()" }

Возвращает минимальный коэффициент масштабирования.

Возвращает `float`: Минимальный коэффициент масштабирования..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionCreated(EventListener)` {: #onconnectioncreatedeventlistener data-toc-label="onConnectionCreated(EventListener)" }

Регистрирует слушателя создания нового соединения между портами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionDragEnded(EventListener)` {: #onconnectiondragendedeventlistener data-toc-label="onConnectionDragEnded(EventListener)" }

Регистрирует слушателя завершения или отмены перетаскивания линии соединения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionDragStarted(EventListener)` {: #onconnectiondragstartedeventlistener data-toc-label="onConnectionDragStarted(EventListener)" }

Регистрирует слушателя начала перетаскивания линии соединения из порта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionRemoved(EventListener)` {: #onconnectionremovedeventlistener data-toc-label="onConnectionRemoved(EventListener)" }

Регистрирует слушателя удаления соединения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionSelectionChanged(EventListener)` {: #onconnectionselectionchangedeventlistener data-toc-label="onConnectionSelectionChanged(EventListener)" }

Регистрирует слушателя изменения выбора соединений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemMoveEnded(EventListener)` {: #onitemmoveendedeventlistener data-toc-label="onItemMoveEnded(EventListener)" }

Регистрирует слушателя завершения перемещения элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemMoveStarted(EventListener)` {: #onitemmovestartedeventlistener data-toc-label="onItemMoveStarted(EventListener)" }

Регистрирует слушателя начала перемещения элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemMoved(EventListener)` {: #onitemmovedeventlistener data-toc-label="onItemMoved(EventListener)" }

Регистрирует слушателя процесса перемещения элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemRemoved(EventListener)` {: #onitemremovedeventlistener data-toc-label="onItemRemoved(EventListener)" }

Регистрирует слушателя удаления элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemResizeEnded(EventListener)` {: #onitemresizeendedeventlistener data-toc-label="onItemResizeEnded(EventListener)" }

Регистрирует слушателя завершения изменения размера элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemResizeStarted(EventListener)` {: #onitemresizestartedeventlistener data-toc-label="onItemResizeStarted(EventListener)" }

Регистрирует слушателя начала изменения размера элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemResized(EventListener)` {: #onitemresizedeventlistener data-toc-label="onItemResized(EventListener)" }

Регистрирует слушателя процесса изменения размера элемента-узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Регистрирует слушателя изменения состава выбранных элементов-узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onViewportChanged(EventListener)` {: #onviewportchangedeventlistener data-toc-label="onViewportChanged(EventListener)" }

Регистрирует слушателя изменения панорамы или масштаба области просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`panning()` {: #panning data-toc-label="panning()" }

Возвращает true, если холст в данный момент панорамируется перетаскиванием.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`panningEnabled()` {: #panningenabled data-toc-label="panningEnabled()" }

Возвращает true, если панорамирование холста мышью включено.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`panningEnabled(boolean)` {: #panningenabledboolean data-toc-label="panningEnabled(boolean)" }

Устанавливает, разрешено ли панорамирование холста мышью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `panningEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`port(NodeGraphPortRef)` {: #portnodegraphportref data-toc-label="port(NodeGraphPortRef)" }

Разрешает ссылку на порт в соответствующий объект порта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `ref` | `NodeGraphPortRef` | Ссылка на порт. |

Возвращает `NodeGraphPort`: Экземпляр порта или null, если не найден..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`portColor()` {: #portcolor data-toc-label="portColor()" }

Возвращает цвет заливки портов узлов по умолчанию.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Возвращает предпочтительную высоту графа.

Возвращает `float`: Предпочтительная высота в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Устанавливает предпочтительную высоту графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | Предпочтительная высота в пикселях. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Устанавливает предпочтительные ширину и высоту графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Предпочтительная ширина в пикселях. |
| `height` | `float` | Предпочтительная высота в пикселях. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Возвращает предпочтительную ширину графа.

Возвращает `float`: Предпочтительная ширина в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Устанавливает предпочтительную ширину графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | Предпочтительная ширина в пикселях. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeConnection(NodeGraphConnection)` {: #removeconnectionnodegraphconnection data-toc-label="removeConnection(NodeGraphConnection)" }

Удаляет указанный экземпляр соединения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `connection` | `NodeGraphConnection` | Параметр connection. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeConnection(String)` {: #removeconnectionstring data-toc-label="removeConnection(String)" }

Удаляет соединение по его идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор соединения. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeItem(NodeGraphItem)` {: #removeitemnodegraphitem data-toc-label="removeItem(NodeGraphItem)" }

Удаляет указанный элемент-узел и все связанные с ним соединительные линии.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `NodeGraphItem` | Целевой элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeItem(String)` {: #removeitemstring data-toc-label="removeItem(String)" }

Удаляет элемент-узел с указанным идентификатором.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор элемента. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeSelectedConnections()` {: #removeselectedconnections data-toc-label="removeSelectedConnections()" }

Удаляет все выбранные в данный момент соединения.

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`resizeEnabled()` {: #resizeenabled data-toc-label="resizeEnabled()" }

Возвращает true, если интерактивное изменение размеров узлов включено.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`resizeEnabled(boolean)` {: #resizeenabledboolean data-toc-label="resizeEnabled(boolean)" }

Устанавливает, разрешено ли интерактивное изменение размеров узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `resizeEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`resizeHandleColor()` {: #resizehandlecolor data-toc-label="resizeHandleColor()" }

Возвращает цвет маркеров изменения размеров узлов.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`restoreSnapshot(NodeGraphSnapshot, NodeGraphWidgetResolver)` {: #restoresnapshotnodegraphsnapshot-nodegraphwidgetresolver data-toc-label="restoreSnapshot(NodeGraphSnapshot, NodeGraphWidgetResolver)" }

Восстанавливает граф узлов, соединения и область просмотра из снимка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `snapshot` | `NodeGraphSnapshot` | Объект снимка состояния графа. |
| `resolver` | `NodeGraphWidgetResolver` | Колбэк разрешения виджетов. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`scaleContentWithZoom()` {: #scalecontentwithzoom data-toc-label="scaleContentWithZoom()" }

Возвращает true, если виджеты содержимого узлов масштабируются вместе с зумом.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`scaleContentWithZoom(boolean)` {: #scalecontentwithzoomboolean data-toc-label="scaleContentWithZoom(boolean)" }

Устанавливает, масштабируются ли виджеты содержимого узлов вместе с зумом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `scaleContentWithZoom` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectConnection(NodeGraphConnection)` {: #selectconnectionnodegraphconnection data-toc-label="selectConnection(NodeGraphConnection)" }

Выбирает указанное соединение.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `connection` | `NodeGraphConnection` | Параметр connection. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectConnection(String)` {: #selectconnectionstring data-toc-label="selectConnection(String)" }

Выбирает соединение с указанным идентификатором.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор соединения. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectItem(NodeGraphItem)` {: #selectitemnodegraphitem data-toc-label="selectItem(NodeGraphItem)" }

Выбирает указанный элемент-узел.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `NodeGraphItem` | Целевой элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectItem(NodeGraphItem, boolean)` {: #selectitemnodegraphitem-boolean data-toc-label="selectItem(NodeGraphItem, boolean)" }

Выбирает элемент-узел с опциональным аддитивным переключением.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `NodeGraphItem` | Целевой элемент. |
| `additive` | `boolean` | Добавляется ли элемент к текущему выделению. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectItem(String)` {: #selectitemstring data-toc-label="selectItem(String)" }

Выбирает элемент-узел с указанным ID.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор элемента. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedConnectionColor()` {: #selectedconnectioncolor data-toc-label="selectedConnectionColor()" }

Возвращает цвет выделенных линий соединений.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedConnectionIds()` {: #selectedconnectionids data-toc-label="selectedConnectionIds()" }

Возвращает неизменяемый список идентификаторов выбранных соединений.

Возвращает `List`: Список идентификаторов выбранных соединений..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedItemBorderColor()` {: #selecteditembordercolor data-toc-label="selectedItemBorderColor()" }

Возвращает цвет границы выбранных элементов-узлов.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedItemIds()` {: #selecteditemids data-toc-label="selectedItemIds()" }

Возвращает неизменяемый список идентификаторов выбранных элементов-узлов.

Возвращает `List`: Список идентификаторов выбранных элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionBoxBorderColor()` {: #selectionboxbordercolor data-toc-label="selectionBoxBorderColor()" }

Возвращает цвет границы рамки лассо-выделения.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionBoxFillColor()` {: #selectionboxfillcolor data-toc-label="selectionBoxFillColor()" }

Возвращает цвет заливки рамки лассо-выделения.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionMode()` {: #selectionmode data-toc-label="selectionMode()" }

Возвращает текущий режим выделения (NONE, SINGLE, MULTIPLE).

Возвращает `NodeGraphSelectionMode`: Активный режим выделения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionMode(NodeGraphSelectionMode)` {: #selectionmodenodegraphselectionmode data-toc-label="selectionMode(NodeGraphSelectionMode)" }

Устанавливает режим выделения в графе.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectionMode` | `NodeGraphSelectionMode` | Режим выделения. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`snapshot()` {: #snapshot data-toc-label="snapshot()" }

Создает полный сериализуемый снимок состояния графа узлов.

Возвращает `NodeGraphSnapshot`: Сериализованный объект снимка состояния..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`validateConnection(NodeGraphPortRef, NodeGraphPortRef)` {: #validateconnectionnodegraphportref-nodegraphportref data-toc-label="validateConnection(NodeGraphPortRef, NodeGraphPortRef)" }

Проверяет допустимость создания соединения по активной политике валидации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `from` | `NodeGraphPortRef` | Ссылка на исходный порт. |
| `to` | `NodeGraphPortRef` | Ссылка на целевой порт. |

Возвращает `NodeGraphConnectionValidation`: Результат валидации соединения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`viewport()` {: #viewport data-toc-label="viewport()" }

Возвращает текущее состояние области просмотра холста.

Возвращает `NodeGraphViewport`: Текущее состояние области просмотра..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

Устанавливает смещение панорамирования без изменения масштаба.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Смещение области просмотра по X. |
| `y` | `float` | Смещение области просмотра по Y. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`viewport(float, float, float)` {: #viewportfloat-float-float data-toc-label="viewport(float, float, float)" }

Устанавливает смещение панорамирования и масштаб области просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Смещение области просмотра по X. |
| `y` | `float` | Смещение области просмотра по Y. |
| `zoom` | `float` | Коэффициент масштабирования. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanStep()` {: #wheelpanstep data-toc-label="wheelPanStep()" }

Возвращает шаг панорамирования колесом мыши в пикселях.

Возвращает `float`: Дистанция смещения на шаг колеса мыши в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanStep(float)` {: #wheelpanstepfloat data-toc-label="wheelPanStep(float)" }

Устанавливает шаг панорамирования колесом мыши в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wheelPanStep` | `float` | Дистанция смещения на шаг колеса мыши в пикселях. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanningEnabled()` {: #wheelpanningenabled data-toc-label="wheelPanningEnabled()" }

Возвращает true, если колесо мыши панорамирует холст вместо масштабирования.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanningEnabled(boolean)` {: #wheelpanningenabledboolean data-toc-label="wheelPanningEnabled(boolean)" }

Устанавливает, панорамирует ли колесо мыши холст вместо масштабирования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wheelPanningEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`zoomEnabled()` {: #zoomenabled data-toc-label="zoomEnabled()" }

Возвращает true, если масштабирование колесом мыши включено.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`zoomEnabled(boolean)` {: #zoomenabledboolean data-toc-label="zoomEnabled(boolean)" }

Устанавливает, разрешено ли масштабирование колесом мыши.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zoomEnabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`zoomRange(float, float)` {: #zoomrangefloat-float data-toc-label="zoomRange(float, float)" }

Настраивает минимальный и максимальный диапазон масштабирования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minZoom` | `float` | Минимальный масштаб. |
| `maxZoom` | `float` | Максимальный масштаб. |

Возвращает `NodeGraph`: Текущий экземпляр для цепочки вызовов..

</div>

