---
title: "GraphView"
slug: "graph-view"
category: "Виджеты/Графы"
locale: "ru"
order: 0
---

# GraphView

Виджет статической или интерактивной диаграммы графа с узлами, ребрами, подписями и всплывающими подсказками.

## Обзор

Виджет статической или интерактивной диаграммы графа с узлами, ребрами, подписями и всплывающими подсказками. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
GraphView graph = new GraphView();
graph.addNode("a", 0.2f, 0.5f);
graph.addNode("b", 0.8f, 0.5f);
graph.addEdge("a", "b");
```

## Связанные ссылки

- [NodeGraph](<node-graph.md>): Интерактивный холст 2D-графа узлов с перемещением блоков, соединениями портов, панорамированием, масштабированием и рамкой выделения.

## API

### API

| Метод | Описание |
| --- | --- |
| [`GraphView()`](#graphview) | Создает виджет диаграммы GraphView со стилями по умолчанию. |
| [`addEdge(String, String)`](#addedgestring-string) | Добавляет направленное ребро между двумя идентификаторами узлов. |
| [`addNode(String, float, float)`](#addnodestring-float-float) | Добавляет узел с указанным идентификатором и координатами. |
| [`clearGraph()`](#cleargraph) | Очищает все узлы и ребра из диаграммы графа. |
| [`edgeColor()`](#edgecolor) | Возвращает цвет линий ребер соединений. |
| [`hoveredNodeColor()`](#hoverednodecolor) | Возвращает цвет заливки узлов при наведении курсора. |
| [`labelColor()`](#labelcolor) | Возвращает цвет текста подписей узлов. |
| [`nodeColor()`](#nodecolor) | Возвращает цвет заливки кругов узлов по умолчанию. |
| [`nodeLabelPlacement(NodeLabelPlacement)`](#nodelabelplacementnodelabelplacement) | Устанавливает относительное положение подписи вокруг точки узла. |
| [`nodeLabelProvider(Function)`](#nodelabelproviderfunction) | Настраивает функцию, предоставляющую текст подписи для точки узла. |
| [`nodeLabelRenderer(GraphNodeLabelRenderer)`](#nodelabelrenderergraphnodelabelrenderer) | Устанавливает пользовательский отрисовщик текстовых подписей узлов. |
| [`nodeLabelsVisible(boolean)`](#nodelabelsvisibleboolean) | Устанавливает, отображаются ли текстовые подписи рядом с точками узлов. |
| [`nodeRenderer(GraphNodeRenderer)`](#noderenderergraphnoderenderer) | Устанавливает пользовательский отрисовщик точек узлов. |
| [`nodeTooltipProvider(Function)`](#nodetooltipproviderfunction) | Настраивает функцию, предоставляющую текст подсказки при наведении на узел. |
| [`nodeTooltipRenderer(GraphNodeTooltipRenderer)`](#nodetooltiprenderergraphnodetooltiprenderer) | Устанавливает пользовательский отрисовщик всплывающих подсказок узлов. |
| [`onNodeClick(EventListener)`](#onnodeclickeventlistener) | Регистрирует слушателя события клика по узлу графа. |
| [`preferredHeight()`](#preferredheight) | Возвращает предпочтительную высоту виджета графа. |
| [`preferredHeight(float)`](#preferredheightfloat) | Устанавливает предпочтительную высоту виджета графа. |
| [`preferredSize(float, float)`](#preferredsizefloat-float) | Устанавливает предпочтительные размеры ширины и высоты. |
| [`preferredWidth()`](#preferredwidth) | Возвращает предпочтительную ширину виджета графа. |
| [`preferredWidth(float)`](#preferredwidthfloat) | Устанавливает предпочтительную ширину виджета графа. |
| [`tooltipBackground()`](#tooltipbackground) | Возвращает цвет фона всплывающих подсказок узлов. |
| [`tooltipBorder()`](#tooltipborder) | Возвращает цвет рамки всплывающих подсказок узлов. |

<div class="api-member-card" markdown="1">

#### `GraphView()` {: #graphview data-toc-label="GraphView()" }

Создает виджет диаграммы GraphView со стилями по умолчанию.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`addEdge(String, String)` {: #addedgestring-string data-toc-label="addEdge(String, String)" }

Добавляет направленное ребро между двумя идентификаторами узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `from` | `String` | Идентификатор исходного узла. |
| `to` | `String` | Идентификатор целевого узла. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`addNode(String, float, float)` {: #addnodestring-float-float data-toc-label="addNode(String, float, float)" }

Добавляет узел с указанным идентификатором и координатами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор узла. |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`clearGraph()` {: #cleargraph data-toc-label="clearGraph()" }

Очищает все узлы и ребра из диаграммы графа.

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`edgeColor()` {: #edgecolor data-toc-label="edgeColor()" }

Возвращает цвет линий ребер соединений.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`hoveredNodeColor()` {: #hoverednodecolor data-toc-label="hoveredNodeColor()" }

Возвращает цвет заливки узлов при наведении курсора.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`labelColor()` {: #labelcolor data-toc-label="labelColor()" }

Возвращает цвет текста подписей узлов.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeColor()` {: #nodecolor data-toc-label="nodeColor()" }

Возвращает цвет заливки кругов узлов по умолчанию.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelPlacement(NodeLabelPlacement)` {: #nodelabelplacementnodelabelplacement data-toc-label="nodeLabelPlacement(NodeLabelPlacement)" }

Устанавливает относительное положение подписи вокруг точки узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeLabelPlacement` | `NodeLabelPlacement` | Позиция размещения подписи относительно узла. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelProvider(Function)` {: #nodelabelproviderfunction data-toc-label="nodeLabelProvider(Function)" }

Настраивает функцию, предоставляющую текст подписи для точки узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeLabelProvider` | `Function` | Функция предоставления подписей узлов. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelRenderer(GraphNodeLabelRenderer)` {: #nodelabelrenderergraphnodelabelrenderer data-toc-label="nodeLabelRenderer(GraphNodeLabelRenderer)" }

Устанавливает пользовательский отрисовщик текстовых подписей узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeLabelRenderer` | `GraphNodeLabelRenderer` | Колбэк пользовательской отрисовки подписей узлов. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelsVisible(boolean)` {: #nodelabelsvisibleboolean data-toc-label="nodeLabelsVisible(boolean)" }

Устанавливает, отображаются ли текстовые подписи рядом с точками узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeLabelsVisible` | `boolean` | Отображаются ли подписи узлов. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeRenderer(GraphNodeRenderer)` {: #noderenderergraphnoderenderer data-toc-label="nodeRenderer(GraphNodeRenderer)" }

Устанавливает пользовательский отрисовщик точек узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeRenderer` | `GraphNodeRenderer` | Колбэк пользовательской отрисовки узлов. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeTooltipProvider(Function)` {: #nodetooltipproviderfunction data-toc-label="nodeTooltipProvider(Function)" }

Настраивает функцию, предоставляющую текст подсказки при наведении на узел.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeTooltipProvider` | `Function` | Функция предоставления всплывающих подсказок узлов. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeTooltipRenderer(GraphNodeTooltipRenderer)` {: #nodetooltiprenderergraphnodetooltiprenderer data-toc-label="nodeTooltipRenderer(GraphNodeTooltipRenderer)" }

Устанавливает пользовательский отрисовщик всплывающих подсказок узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `nodeTooltipRenderer` | `GraphNodeTooltipRenderer` | Колбэк пользовательской отрисовки всплывающих подсказок узлов. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`onNodeClick(EventListener)` {: #onnodeclickeventlistener data-toc-label="onNodeClick(EventListener)" }

Регистрирует слушателя события клика по узлу графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Возвращает предпочтительную высоту виджета графа.

Возвращает `float`: Предпочтительная высота в пикселях..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Устанавливает предпочтительную высоту виджета графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | Предпочтительная высота в пикселях. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Устанавливает предпочтительные размеры ширины и высоты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Предпочтительная ширина в пикселях. |
| `height` | `float` | Предпочтительная высота в пикселях. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Возвращает предпочтительную ширину виджета графа.

Возвращает `float`: Предпочтительная ширина в пикселях..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Устанавливает предпочтительную ширину виджета графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | Предпочтительная ширина в пикселях. |

Возвращает `GraphView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`tooltipBackground()` {: #tooltipbackground data-toc-label="tooltipBackground()" }

Возвращает цвет фона всплывающих подсказок узлов.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`tooltipBorder()` {: #tooltipborder data-toc-label="tooltipBorder()" }

Возвращает цвет рамки всплывающих подсказок узлов.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

### Graph Data

| Метод | Описание |
| --- | --- |
| [`removeNode(String)`](#removenodestring) | Removes a node and all incident edges. |
| [`nodeCount()`](#nodecount) | Returns the number of nodes currently in the graph. |
| [`edgeCount()`](#edgecount) | Returns the number of edges, including edges waiting for missing endpoints. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`removeNode(String)` {: #removenodestring data-toc-label="removeNode(String)" }

Removes a node and all incident edges.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Node identifier to remove; null or unknown identifiers are ignored. |

Возвращает `GraphView`: This graph view for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeCount()` {: #nodecount data-toc-label="nodeCount()" }

Returns the number of nodes currently in the graph.

Возвращает `int`: The current number of nodes..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`edgeCount()` {: #edgecount data-toc-label="edgeCount()" }

Returns the number of edges, including edges waiting for missing endpoints.

Возвращает `int`: The current number of stored edges..

</div>

### Appearance

| Метод | Описание |
| --- | --- |
| [`nodeBorderColor()`](#nodebordercolor) | Returns the mutable color used to outline graph nodes. |
| [`hoveredEdgeColor()`](#hoverededgecolor) | Returns the mutable color used for edges connected to the hovered node. |
| [`directed(boolean)`](#directedboolean) | Enables or disables arrowheads at edge targets. |
| [`directed()`](#directed) | Returns whether edges are rendered as directed connections. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeBorderColor()` {: #nodebordercolor data-toc-label="nodeBorderColor()" }

Returns the mutable color used to outline graph nodes.

Возвращает `MutableColor`: The mutable RGBA node-border color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`hoveredEdgeColor()` {: #hoverededgecolor data-toc-label="hoveredEdgeColor()" }

Returns the mutable color used for edges connected to the hovered node.

Возвращает `MutableColor`: The mutable RGBA hovered-edge color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`directed(boolean)` {: #directedboolean data-toc-label="directed(boolean)" }

Enables or disables arrowheads at edge targets.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `directed` | `boolean` | True to draw an arrowhead at each target node. |

Возвращает `GraphView`: This graph view for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`directed()` {: #directed data-toc-label="directed()" }

Returns whether edges are rendered as directed connections.

Возвращает `boolean`: True when edge target arrowheads are enabled..

</div>

