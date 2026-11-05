---
title: "NodeBuilder"
slug: "node-builder"
category: "Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeBuilder

Fluent-сборщик модульного элемента-узла из заголовка, портов, редакторов, виджетов и пользовательских модулей.

## Обзор

Используйте `NodeBuilder`, если узлу нужна повторно используемая модульная раскладка. Порядок вызовов сборщика задаёт порядок модулей сверху вниз. Добавляйте потоковые и data-порты, задавайте редакторы значений по умолчанию для входов и завершайте сборку вызовом `build()` перед добавлением элемента в `NodeGraph`.

## Создание модульного узла

```java
NodeGraphItem node = NodeBuilder.node("spawn")
        .header("Create NPC")
        .flowRow("in", "out")
        .input("name", "Name", "string", nameEditor)
        .output("entity", "Entity", "entity")
        .build();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`node(String)` {: #nodestring data-toc-label="node(String)" }

Создаёт сборщик модульного узла с указанным ID элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный ID нового элемента-узла. |

Возвращает `NodeBuilder`: Новый сборщик для указанного ID узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`at(float, float)` {: #atfloat-float data-toc-label="at(float, float)" }

Задаёт начальную позицию узла в world-координатах графа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Начальная горизонтальная позиция в world-координатах графа. |
| `y` | `float` | Начальная вертикальная позиция в world-координатах графа. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`type(String)` {: #typestring data-toc-label="type(String)" }

Задаёт ID зарегистрированного типа узла для восстановления и палитры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `typeId` | `String` | ID зарегистрированного типа, определяющего вид узла. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`style(NodeStyle)` {: #stylenodestyle data-toc-label="style(NodeStyle)" }

Назначает узлу визуальный стиль.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `style` | `NodeStyle` | Визуальный стиль, назначаемый узлу. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`color(int)` {: #colorint data-toc-label="color(int)" }

Задаёт цвет заголовка узла в формате ARGB.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `argb` | `int` | Цвет заголовка в формате 0xAARRGGBB. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`header(String)` {: #headerstring data-toc-label="header(String)" }

Добавляет заголовок узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка узла. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`header(String, Consumer)` {: #headerstring-consumer data-toc-label="header(String, Consumer)" }

Добавляет заголовок и до вставки настраивает иконку или сворачивание.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка узла. |
| `configure` | `Consumer` | Необязательный callback настройки заголовка перед добавлением. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`flowRow(String, String)` {: #flowrowstring-string data-toc-label="flowRow(String, String)" }

Добавляет строку потока выполнения с необязательными ID входного и выходного портов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `inPortId` | `String` | ID входного порта или null, чтобы не создавать его. |
| `outPortId` | `String` | ID выходного порта или null, чтобы не создавать его. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`input(String, String, String)` {: #inputstring-string-string data-toc-label="input(String, String, String)" }

Добавляет строку data-входа без inline-редактора значения по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `portId` | `String` | Уникальный ID порта в пределах узла. |
| `label` | `String` | Отображаемая подпись строки порта. |
| `type` | `String` | ID типа данных порта для совместимости и отображения. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`input(String, String, String, Widget)` {: #inputstring-string-string-widget data-toc-label="input(String, String, String, Widget)" }

Добавляет строку data-входа с виджетом, отображаемым пока вход не подключён.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `portId` | `String` | Уникальный ID порта в пределах узла. |
| `label` | `String` | Отображаемая подпись строки порта. |
| `type` | `String` | ID типа данных порта для совместимости и отображения. |
| `editor` | `Widget` | Необязательный inline-виджет, показываемый пока вход не подключён. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`input(String, String, String, ValueEditor)` {: #inputstring-string-string-valueeditor data-toc-label="input(String, String, String, ValueEditor)" }

Добавляет строку data-входа с типизированным редактором значения по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `portId` | `String` | Уникальный ID порта в пределах узла. |
| `label` | `String` | Отображаемая подпись строки порта. |
| `type` | `String` | ID типа данных порта для совместимости и отображения. |
| `editor` | `ValueEditor` | Типизированный редактор значения входа по умолчанию. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`requiredInput(String, String, String)` {: #requiredinputstring-string-string data-toc-label="requiredInput(String, String, String)" }

Добавляет обязательный data-вход без значения по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `portId` | `String` | Уникальный ID порта в пределах узла. |
| `label` | `String` | Отображаемая подпись строки порта. |
| `type` | `String` | ID типа данных порта для совместимости и отображения. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`output(String, String, String)` {: #outputstring-string-string data-toc-label="output(String, String, String)" }

Добавляет строку data-выхода.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `portId` | `String` | Уникальный ID порта в пределах узла. |
| `label` | `String` | Отображаемая подпись строки порта. |
| `type` | `String` | ID типа данных порта для совместимости и отображения. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`row(String, String, String, String, String, String)` {: #rowstring-string-string-string-string-string data-toc-label="row(String, String, String, String, String, String)" }

Добавляет вход и выход в одной строке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `inPortId` | `String` | ID входного порта или null, чтобы не создавать его. |
| `inLabel` | `String` | Отображаемая подпись входного порта. |
| `inType` | `String` | ID типа данных порта для совместимости и отображения. |
| `outPortId` | `String` | ID выходного порта или null, чтобы не создавать его. |
| `outLabel` | `String` | Отображаемая подпись выходного порта. |
| `outType` | `String` | ID типа данных порта для совместимости и отображения. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`widget(String, Widget)` {: #widgetstring-widget data-toc-label="widget(String, Widget)" }

Добавляет модульную строку на всю ширину с виджетом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | ID, уникальный среди модулей узла. |
| `widget` | `Widget` | Виджет, встраиваемый в строку модуля. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`separator()` {: #separator data-toc-label="separator()" }

Добавляет визуальный разделитель секций узла.

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`module(NodeModule)` {: #modulenodemodule data-toc-label="module(NodeModule)" }

Добавляет пользовательский модуль узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `module` | `NodeModule` | Пользовательский модуль для добавления в узел. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`collapsed(boolean)` {: #collapsedboolean data-toc-label="collapsed(boolean)" }

Задаёт начальное состояние сворачивания узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `collapsed` | `boolean` | Должен ли узел начинать в свёрнутом состоянии. |

Возвращает `NodeBuilder`: Текущий сборщик для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`build()` {: #build data-toc-label="build()" }

Возвращает собранный элемент-узел.

Возвращает `NodeGraphItem`: Собранный `NodeGraphItem`..

</div>

