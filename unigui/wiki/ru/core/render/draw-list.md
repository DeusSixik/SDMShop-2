---
title: "DrawList"
slug: "draw-list"
category: "Core/Render"
locale: "ru"
order: 20
---

# DrawList

Буфер команд рисования одного кадра, записывающий аппаратно-независимые примитивы, переиспользующий команды из пула и предоставляющий многослойную запись (channels).

## Обзор

Виджеты не рисуют напрямую на GPU. Вместо этого операции рисования записывают объекты `DrawCommand` в `DrawList`. В конце кадра активный `RenderBackend` обходит список, выполняет сортировку состояний, батчинг геометрии, привязку текстур и непосредственную отрисовку.

## Жизненный цикл команд и пул

Чтобы предотвратить нагрузку на сборщик мусора при непрерывном рендеринге интерфейса, `DrawList` содержит пул переиспользуемых объектов `DrawCommand` (до 8192 экземпляров). Внутренние методы запрашивают команды через `obtain(DrawCommandType)` и возвращают их в пул при вызове `clear()`.

## Многослойная запись (Channels)

Механизм каналов (channels) позволяет виджетам записывать команды рисования в раздельные виртуальные слои (фон, содержимое, оверлей, тултип) в рамках одного прохода без усложнения логики. Вызов `channelsSplit(n)` разделяет буфер на слои, `channelsSetCurrent(i)` переключает целевой слой, а `channelsMerge()` склеивает слои обратно в строгом порядке.

## Векторные пути (Vector Paths)

`DrawList` владеет выделенным экземпляром `VectorPath`, доступным через `path()`. Процедурные контуры, кривые и многоугольники могут строиться в этом общем объекте без лишних аллокаций геометрических структур.

## Потребление бэкендом

Бэкенды могут получать команды как через неизменяемый список `List<DrawCommand>` (`commands()`), так и через прямой доступ к элементам массива fastutil (`commandElements()` и `size()`) для максимального быстродействия циклов рендера.

## Разделение фона и переднего плана по каналам

```java
drawList.channelsSplit(2);
drawList.channelsSetCurrent(1);
// Рисуем передний план первым в коде
drawList.add(foregroundCommand);
drawList.channelsSetCurrent(0);
// Рисуем задний план под содержимым
drawList.add(backgroundCommand);
// Склеиваем слои в правильном порядке 0..N
drawList.channelsMerge();
```

## Обход записанных команд в цикле бэкенда

```java
Object[] elements = drawList.commandElements();
int size = drawList.size();
for (int i = 0; i < size; i++) {
    DrawCommand cmd = (DrawCommand) elements[i];
    dispatchCommand(cmd);
}
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`add(DrawCommand)` {: #adddrawcommand data-toc-label="add(DrawCommand)" }

**add(DrawCommand)**

Добавляет копию указанной команды рисования в текущий список или активный канал.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `command` | `DrawCommand` | Команда рисования; null игнорируется. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`obtain(DrawCommandType)` {: #obtaindrawcommandtype data-toc-label="obtain(DrawCommandType)" }

**obtain(DrawCommandType)**

Получает переиспользуемый экземпляр DrawCommand из пула или создаёт новый, если пул пуст.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `DrawCommandType` | Тип запрашиваемой команды. |

Возвращает `DrawCommand`: Сброшенный экземпляр DrawCommand, готовый к заполнению..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`clear()` {: #clear data-toc-label="clear()" }

**clear()**

Очищает все записанные команды, возвращает их в пул переиспользования, сбрасывает каналы и очищает общий векторный путь.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`commands()` {: #commands data-toc-label="commands()" }

**commands()**

Возвращает неизменяемое представление всех записанных команд текущего draw-списка.

Возвращает `List`: Read-only представление записанных команд..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`commandElements()` {: #commandelements data-toc-label="commandElements()" }

**commandElements()**

Возвращает внутренний массив fastutil для максимального быстродействия циклов обработки бэкенда.

Возвращает `Object[]`: Массив объектов; читать только первые size() элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`size()` {: #size data-toc-label="size()" }

**size()**

Возвращает общее количество записанных команд.

Возвращает `int`: Количество команд..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`path()` {: #path data-toc-label="path()" }

**path()**

Возвращает общий экземпляр VectorPath для построения контуров.

Возвращает `VectorPath`: Общий экземпляр VectorPath..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`pathClear()` {: #pathclear data-toc-label="pathClear()" }

**pathClear()**

Очищает общий построитель VectorPath, сбрасывая все подпути и вершины.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsActive()` {: #channelsactive data-toc-label="channelsActive()" }

**channelsActive()**

Проверяет, активна ли многослойная запись в каналы.

Возвращает `boolean`: true, если запись ведётся в раздельные каналы..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`currentChannel()` {: #currentchannel data-toc-label="currentChannel()" }

**currentChannel()**

Возвращает индекс текущего активного канала записи.

Возвращает `int`: Индекс текущего канала (0..N-1)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsSplit(int)` {: #channelssplitint data-toc-label="channelsSplit(int)" }

**channelsSplit(int)**

Разделяет запись команд на указанное число слоёв-каналов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `count` | `int` | Количество каналов; значения меньше 1 нормализуются к 1. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsSetCurrent(int)` {: #channelssetcurrentint data-toc-label="channelsSetCurrent(int)" }

**channelsSetCurrent(int)**

Выбирает активный канал для последующих команд рисования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `channel` | `int` | Индекс целевого канала, ограничивается доступным диапазоном. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsMerge()` {: #channelsmerge data-toc-label="channelsMerge()" }

**channelsMerge()**

Сливает команды всех каналов обратно в основной список в строгом порядке следования каналов.

</div>

