---
title: "RibbonLayer"
slug: "ribbon-layer"
category: "Core/Виджеты/Мир"
locale: "ru"
order: 0
---

# RibbonLayer

Слой [WorldCanvas](<../../../widgets/world/world-canvas.md>) для отрисовки текстурированных или сплошных лент, маршрутов и полилиний в мировых координатах.

## Обзор

`RibbonLayer` отрисовывает непрерывные текстурированные или сплошные ленты, маршруты и геометрические пути внутри `WorldCanvas`.

Ключевые возможности:
- Текстурированные или однотонные ленты с настраиваемой шириной и шагом повтора текстуры (`width`, `textureLength`).
- Кэширование меша: геометрия переиспользуется до изменения точек пути, камеры или параметров оформления.
- Расширенная настройка через `RibbonOptions`: замкнутые контуры, miter limits, инверсия/обмен UV и скругленные стыки.
- Управление точками пути: `path`, `addPoint`, `clear`.

## Отрисовка текстурированного пути ленты

```java
RibbonLayer ribbon = new RibbonLayer()
    .width(16.0f)
    .addPoint(0, 0)
    .addPoint(100, 150);
canvas.addWorldLayer(ribbon);
```

## Связанные ссылки

- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.
- [WorldLayer](<world-layer.md>): Функциональный контракт для фоновых или декоративных слоев отрисовки внутри [WorldCanvas](<../../../widgets/world/world-canvas.md>).

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`addPoint(float, float)` {: #addpointfloat-float data-toc-label="addPoint(float, float)" }

Добавляет новую точку в мировых координатах в конец пути ленты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Мировая координата X. |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`cacheHits()` {: #cachehits data-toc-label="cacheHits()" }

Возвращает количество успешных обращений к кэшу меша ленты.

Возвращает `long`: Числовое количество..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`cacheRebuilds()` {: #cacherebuilds data-toc-label="cacheRebuilds()" }

Возвращает количество перестроений кэша меша ленты.

Возвращает `long`: Числовое количество..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`clear()` {: #clear data-toc-label="clear()" }

Удаляет все точки из пути ленты.

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`closed()` {: #closed data-toc-label="closed()" }

Возвращает true, если путь ленты образует замкнутый контур.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`closed(boolean)` {: #closedboolean data-toc-label="closed(boolean)" }

Настраивает замыкание конца ленты обратно в ее начало.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closed` | `boolean` | Замкнут ли путь ленты в кольцо. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipU()` {: #flipu data-toc-label="flipU()" }

Возвращает true, если горизонтальная координата текстуры U инвертирована.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipU(boolean)` {: #flipuboolean data-toc-label="flipU(boolean)" }

Настраивает инверсию горизонтальной координаты текстуры U.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flipU` | `boolean` | Инвертировать ли горизонтальную координату текстуры U. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipV()` {: #flipv data-toc-label="flipV()" }

Возвращает true, если вертикальная координата текстуры V инвертирована.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipV(boolean)` {: #flipvboolean data-toc-label="flipV(boolean)" }

Настраивает инверсию вертикальной координаты текстуры V.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flipV` | `boolean` | Инвертировать ли вертикальную координату текстуры V. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`invalidateCache()` {: #invalidatecache data-toc-label="invalidateCache()" }

Принудительно инвалидирует закэшированную геометрию меша ленты.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointDots()` {: #jointdots data-toc-label="jointDots()" }

Возвращает true, если на стыках вершин пути отрисовываются скругленные точки.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointDots(boolean)` {: #jointdotsboolean data-toc-label="jointDots(boolean)" }

Настраивает отрисовку скругленных точек на стыках вершин пути.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `jointDots` | `boolean` | Отрисовывать ли скругленные точки на стыках вершин. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointSegments()` {: #jointsegments data-toc-label="jointSegments()" }

Возвращает количество сегментов для скругления угловых стыков.

Возвращает `int`: Числовое количество..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointSegments(int)` {: #jointsegmentsint data-toc-label="jointSegments(int)" }

Задает количество сегментов для скругления угловых стыков.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `jointSegments` | `int` | Количество сегментов для скругления угловых стыков. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`miterLimit()` {: #miterlimit data-toc-label="miterLimit()" }

Возвращает порог скоса угловых стыков (miter limit).

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`miterLimit(float)` {: #miterlimitfloat data-toc-label="miterLimit(float)" }

Устанавливает порог скоса угловых стыков (miter limit).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `miterLimit` | `float` | Порог ограничения скоса угловых стыков (miter limit). |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`options()` {: #options data-toc-label="options()" }

Возвращает активные параметры генерации ленты.

Возвращает `RibbonOptions`: Активные параметры ленты [RibbonOptions](<../../render/ribbon-options.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`options(RibbonOptions)` {: #optionsribbonoptions data-toc-label="options(RibbonOptions)" }

Устанавливает параметры генерации ленты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `options` | `RibbonOptions` | Параметры ленты, управляющие отрисовкой и триангуляцией. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`path()` {: #path data-toc-label="path()" }

Возвращает неизменяемый список всех мировых точек вдоль пути ленты.

Возвращает `List`: Неизменяемый список точек пути в мировых координатах..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`path(List)` {: #pathlist data-toc-label="path(List)" }

Заменяет весь путь ленты переданным списком точек в мировых координатах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `points` | `List` | Список точек в мировых координатах, задающих путь ленты. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`render(WorldCanvas, DrawScope)` {: #renderworldcanvas-drawscope data-toc-label="render(WorldCanvas, DrawScope)" }

Отрисовывает меш ленты в область рисования холста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `canvas` | `WorldCanvas` | [WorldCanvas](<../../../widgets/world/world-canvas.md>), содержащий этот слой ленты. |
| `draw` | `DrawScope` | [DrawScope](<../../render/draw-scope.md>) для записи команд отрисовки меша ленты. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`resetCacheStats()` {: #resetcachestats data-toc-label="resetCacheStats()" }

Сбрасывает диагностическую статистику кэширования меша.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`swapUv()` {: #swapuv data-toc-label="swapUv()" }

Возвращает true, если координаты текстуры U и V поменяны местами.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`swapUv(boolean)` {: #swapuvboolean data-toc-label="swapUv(boolean)" }

Настраивает обмен осей текстуры U и V.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `swapUv` | `boolean` | Менять ли местами оси текстуры U и V. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`texture()` {: #texture data-toc-label="texture()" }

Возвращает дескриптор текстуры ленты или null при сплошной цветной заливке.

Возвращает `TextureHandle`: Дескриптор текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`texture(TextureHandle)` {: #texturetexturehandle data-toc-label="texture(TextureHandle)" }

Устанавливает текстуру, применяемую к ленте.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор текстуры. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`textureLength()` {: #texturelength data-toc-label="textureLength()" }

Возвращает расстояние в мировых единицах для одного полного повтора тайла текстуры.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`textureLength(float)` {: #texturelengthfloat data-toc-label="textureLength(float)" }

Устанавливает расстояние в мировых единицах для одного полного повтора тайла текстуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `textureLength` | `float` | Расстояние в мировых единицах для одного полного повтора тайла текстуры. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`tint()` {: #tint data-toc-label="tint()" }

Возвращает изменяемый цвет тонирования вершин ленты.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`tint(ColorView)` {: #tintcolorview data-toc-label="tint(ColorView)" }

Устанавливает цвет тонирования вершин ленты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tint` | `ColorView` | Цвет тонирования. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`width()` {: #width data-toc-label="width()" }

Возвращает ширину ленты в мировых единицах.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`width(float)` {: #widthfloat data-toc-label="width(float)" }

Устанавливает ширину ленты в мировых единицах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |

Возвращает `RibbonLayer`: Текущий экземпляр для цепочки вызовов..

</div>

