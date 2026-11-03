---
title: "AnimatedTexture"
slug: "animated-texture"
category: "Core/Textures"
locale: "ru"
order: 55
---

# AnimatedTexture

Изменяемая текстура sprite sheet, кадры которой сменяются по игровым тикам из равномерной сетки ячеек.

## Обзор

AnimatedTexture разбивает текстуру sprite sheet на равномерную сетку ячеек и последовательно сменяет кадры в заданном диапазоне по игровым тикам без необходимости обновления со стороны виджетов.

## Сетка и размеры

Размер ячейки задается через `setCellSize(width, height)`. Кадры нумеруются слева направо и сверху вниз по колонкам сетки.

## Воспроизведение и тайминг

Используйте `setAnimation(from, to)` для выбора диапазона кадров включительно (при `from > to` воспроизведение идет в обратную сторону) и `setAnimation(ticksPerFrame)` для управления длительностью кадра.

## Базовая анимация частиц из спрайт-листа

```java
AnimatedTexture particles = new AnimatedTexture(sheet)
        .setCellSize(8)
        .setAnimation(32, 44)
        .setAnimation(1);
scope.texture(particles, x, y, 32.0f, 32.0f, Paint.NONE);
```

## Связанные ссылки

- [TextureHandle](<texture-handle.md>): Backend-нейтральный интерфейс текстурного ресурса, предоставляющий размеры, параметры сэмплирования и доступ к нативному объекту.
- [TextureRegion](<texture-region.md>): Неизменяемая прямоугольная UV-область внутри родительской текстуры для отрисовки спрайтов из атласов.

## API

<div class="api-member-card" markdown="1">

### `AnimatedTexture(String, int, int)` {: #animatedtexturestring-int-int data-toc-label="AnimatedTexture(String, int, int)" }

Создает анимированную текстуру по идентификатору ресурса и известным размерам листа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Строковый идентификатор ресурса. |
| `sheetWidth` | `int` | Ширина sprite sheet в пикселях. |
| `sheetHeight` | `int` | Высота sprite sheet в пикселях. |

</div>

<div class="api-member-card" markdown="1">

### `AnimatedTexture(TextureHandle)` {: #animatedtexturetexturehandle data-toc-label="AnimatedTexture(TextureHandle)" }

Создает анимированную текстуру поверх готового дескриптора sprite sheet.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `sheet` | `TextureHandle` | Дескриптор текстуры sprite sheet. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`base()` {: #base data-toc-label="base()" }

Возвращает базовый физический дескриптор текстуры sprite sheet.

Возвращает `TextureHandle`: Базовый дескриптор текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`cellHeight()` {: #cellheight data-toc-label="cellHeight()" }

Возвращает настроенную высоту ячейки в пикселях.

Возвращает `int`: Высота ячейки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`cellWidth()` {: #cellwidth data-toc-label="cellWidth()" }

Возвращает настроенную ширину ячейки в пикселях.

Возвращает `int`: Ширина ячейки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`currentFrame()` {: #currentframe data-toc-label="currentFrame()" }

Возвращает номер кадра сетки для текущего тика TextureClock.

Возвращает `int`: Вычисленный номер кадра в сетке..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`firstFrame()` {: #firstframe data-toc-label="firstFrame()" }

Возвращает индекс начального кадра последовательности анимации.

Возвращает `int`: Индекс начального кадра..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`frameAt(long)` {: #frameatlong data-toc-label="frameAt(long)" }

Вычисляет номер кадра сетки для указанного абсолютного игрового тика.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `ticks` | `long` | Абсолютное количество тиков. |

Возвращает `int`: Вычисленный номер кадра в сетке..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`frameCount()` {: #framecount data-toc-label="frameCount()" }

Возвращает общее количество кадров в активном диапазоне анимации.

Возвращает `int`: Общее количество кадров в диапазоне..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`frameUv(int, MutableRect)` {: #frameuvint-mutablerect data-toc-label="frameUv(int, MutableRect)" }

Вычисляет координаты UV-прямоугольника внутри sprite sheet для указанного номера кадра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `frame` | `int` | Номер кадра в сетке. |
| `out` | `MutableRect` | Изменяемый прямоугольник для записи UV-координат. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`height()` {: #height data-toc-label="height()" }

Возвращает видимую высоту ячейки в пикселях.

Возвращает `int`: Высота ячейки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`id()` {: #id data-toc-label="id()" }

Возвращает идентификатор ресурса текстуры базового sprite sheet.

Возвращает `String`: Строковый идентификатор текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`lastFrame()` {: #lastframe data-toc-label="lastFrame()" }

Возвращает индекс конечного кадра последовательности анимации.

Возвращает `int`: Индекс конечного кадра..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`nativeHandle()` {: #nativehandle data-toc-label="nativeHandle()" }

Возвращает низкоуровневый дескриптор платформы базового sprite sheet.

Возвращает `Object`: Низкоуровневый дескриптор текстуры платформы..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`options()` {: #options data-toc-label="options()" }

Возвращает параметры выборки текстуры базового sprite sheet.

Возвращает `TextureOptions`: Параметры выборки текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`resolveUv(MutableRect)` {: #resolveuvmutablerect data-toc-label="resolveUv(MutableRect)" }

Разрешает нормализованные UV-координаты для текущего кадра анимации с учетом базовой текстуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `out` | `MutableRect` | Изменяемый прямоугольник для записи UV-координат. |

Возвращает `boolean`: true, если вычисление UV выполнено успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setAnimation(int)` {: #setanimationint data-toc-label="setAnimation(int)" }

Задает длительность одного кадра анимации в игровых тиках.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `ticksPerFrame` | `int` | Длительность кадра в тиках. |

Возвращает `AnimatedTexture`: Текущий экземпляр анимированной текстуры для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setAnimation(int, int)` {: #setanimationint-int data-toc-label="setAnimation(int, int)" }

Задает диапазон номеров кадров включительно; при from > to анимация проигрывается в обратную сторону.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `from` | `int` | Индекс начального кадра. |
| `to` | `int` | Индекс конечного кадра. |

Возвращает `AnimatedTexture`: Текущий экземпляр анимированной текстуры для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setCellSize(int)` {: #setcellsizeint data-toc-label="setCellSize(int)" }

Задает размер квадратной ячейки сетки в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `size` | `int` | Размер стороны ячейки в пикселях. |

Возвращает `AnimatedTexture`: Текущий экземпляр анимированной текстуры для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setCellSize(int, int)` {: #setcellsizeint-int data-toc-label="setCellSize(int, int)" }

Задает ширину и высоту ячейки сетки анимации в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `int` | Ширина ячейки в пикселях. |
| `height` | `int` | Высота ячейки в пикселях. |

Возвращает `AnimatedTexture`: Текущий экземпляр анимированной текстуры для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`sheet()` {: #sheet data-toc-label="sheet()" }

Возвращает дескриптор базовой текстуры sprite sheet.

Возвращает `TextureHandle`: Базовая текстура sprite sheet..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`ticksPerFrame()` {: #ticksperframe data-toc-label="ticksPerFrame()" }

Возвращает количество игровых тиков на один кадр анимации.

Возвращает `int`: Количество тиков на кадр..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`width()` {: #width data-toc-label="width()" }

Возвращает видимую ширину ячейки в пикселях.

Возвращает `int`: Ширина ячейки в пикселях..

</div>

