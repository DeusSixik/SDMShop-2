---
title: "MapMarkerHandle"
slug: "map-marker-handle"
category: "Core/Виджеты/Карта"
locale: "ru"
order: 0
---

# MapMarkerHandle

Дескриптор для удобной настройки положения, экранного размера, масштабирования от зума, опорной точки (pivot) и видимости маркера на [MapCanvas](<../../../widgets/map/map-canvas.md>).

## Обзор

`MapMarkerHandle` предоставляет удобный дескриптор для настройки виджетов, добавленных в качестве маркеров на `MapCanvas`.

Ключевые возможности:
- Перемещение в координатах карты (`position`) или во внешних мировых координатах (`projectedPosition`).
- Фиксированные экранные размеры с ограничением минимального и максимального зума (`screenSize`, `minScreenSize`, `maxScreenSize`).
- Динамическое масштабирование размера от зума относительно опорного зума (`zoomScaling`).
- Настройка опорной точки привязки (`pivot`) и отсечение за пределами экрана (`cullOutsideViewport`).
- Настройка диапазонов видимости по уровню зума для LOD (`visibleZoom`).

## Настройка маркера через дескриптор

```java
MapMarkerHandle<MapMarker> handle = map.addMarker("home", 128.0f, 256.0f, "Base")
    .screenSize(32.0f, 32.0f)
    .pivot(0.5f, 1.0f);
```

## Связанные ссылки

- [AnchorWidget](<../world/anchor-widget.md>): Пространственный якорь, привязывающий экранный UI-виджет к мировым координатам с масштабированием от зума, настройкой pivot и отсечением вне экрана.
- [MapCanvas](<../../../widgets/map/map-canvas.md>): Интерактивный виджет 2D-карты, расширяющий [WorldCanvas](<../../../widgets/world/world-canvas.md>) поддержкой размеров карты, слоями текстур и сетки, проекцией координат и маркерами.
- [MapMarker](<../../../widgets/map/map-marker.md>): Базовая оболочка маркера для [MapCanvas](<../../../widgets/map/map-canvas.md>) с поддержкой произвольного контента, текстовых меток, состояний выбора и подсветки.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Возвращает низкоуровневый [AnchorWidget](<../world/anchor-widget.md>), управляющий маркером.

Возвращает `AnchorWidget`: Управляющий [AnchorWidget](<../world/anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`cullOutsideViewport(boolean)` {: #culloutsideviewportboolean data-toc-label="cullOutsideViewport(boolean)" }

Включает или отключает автоматическое отсечение маркера вне видимой области экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `cullOutsideViewport` | `boolean` | Скрывать ли маркер автоматически при выходе за пределы области просмотра. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`id()` {: #id data-toc-label="id()" }

Возвращает уникальный строковый идентификатор этого маркера на карте.

Возвращает `String`: Строковый идентификатор или текст..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`mapX()` {: #mapx data-toc-label="mapX()" }

Возвращает координату X маркера в системе координат карты.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`mapY()` {: #mapy data-toc-label="mapY()" }

Возвращает координату Y маркера в системе координат карты.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`maxScreenSize(float, float)` {: #maxscreensizefloat-float data-toc-label="maxScreenSize(float, float)" }

Устанавливает максимальные экранные размеры маркера для предотвращения чрезмерного увеличения при приближении.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`minScreenSize(float, float)` {: #minscreensizefloat-float data-toc-label="minScreenSize(float, float)" }

Устанавливает минимальные экранные размеры маркера для предотвращения сильного сжатия при отдалении.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`pivot(float, float)` {: #pivotfloat-float data-toc-label="pivot(float, float)" }

Задает нормализованную опорную точку (pivot от 0.0 до 1.0) внутри границ маркера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`position(float, float)` {: #positionfloat-float data-toc-label="position(float, float)" }

Позиционирует маркер в координатах карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mapX` | `float` | Целевая координата X в единицах карты. |
| `mapY` | `float` | Целевая координата Y в единицах карты. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`priority()` {: #priority data-toc-label="priority()" }

Возвращает пользовательский приоритет сортировки маркера.

Возвращает `int`: Числовое количество..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`priority(int)` {: #priorityint data-toc-label="priority(int)" }

Устанавливает пользовательский приоритет сортировки маркера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `priority` | `int` | Значение приоритета для сортировки и кластеризации маркера. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`projectedPosition(float, float)` {: #projectedpositionfloat-float data-toc-label="projectedPosition(float, float)" }

Позиционирует маркер во внешних мировых координатах через проекцию [MapCanvas](<../../../widgets/map/map-canvas.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Целевая координата X во внешних единицах мира. |
| `worldY` | `float` | Целевая координата Y во внешних единицах мира. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`screenSize(float, float)` {: #screensizefloat-float data-toc-label="screenSize(float, float)" }

Устанавливает базовые фиксированные экранные размеры маркера в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Устанавливает флаг ручной видимости маркера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | Флаг видимости. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`visibleZoom(float, float)` {: #visiblezoomfloat-float data-toc-label="visibleZoom(float, float)" }

Ограничивает видимость маркера указанным диапазоном масштаба зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minZoom` | `float` | Минимально допустимый масштаб зума. |
| `maxZoom` | `float` | Максимально допустимый масштаб зума. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`widget()` {: #widget data-toc-label="widget()" }

Возвращает UI-виджет, отображаемый в качестве маркера.

Возвращает `Widget`: Значение T..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`zoomScaling(boolean, float)` {: #zoomscalingboolean-float data-toc-label="zoomScaling(boolean, float)" }

Настраивает динамическое масштабирование размеров маркера в зависимости от зума карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zoomScaling` | `boolean` | True для динамического масштабирования размера маркера от зума. |
| `referenceZoom` | `float` | Уровень зума, при котором маркер отображается в базовом размере. |

Возвращает `MapMarkerHandle`: Текущий экземпляр для цепочки вызовов..

</div>

