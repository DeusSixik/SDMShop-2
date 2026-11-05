---
title: "MapProjection"
slug: "map-projection"
category: "Core/Виджеты/Карта"
locale: "ru"
order: 0
---

# MapProjection

Движок линейной проекции для преобразования между внешними мировыми координатами и внутренними координатами карты [MapCanvas](<../../../widgets/map/map-canvas.md>).

## Обзор

`MapProjection` выполняет быстрое выровненное по осям линейное преобразование между внешними координатами игрового мира и внутренними координатами `MapCanvas`.

Формула преобразования:
- `mapX = worldX * scaleX + offsetX`
- `mapY = worldY * scaleY + offsetY`

Калибровка:
- Прямое задание масштаба и смещения (`MapProjection(scaleX, offsetX, scaleY, offsetY)`).
- Калибровка по двум реперным парам точек через `MapProjection.axisAligned(...)` или удобный билдер `MapProjection.affine()`.

## Калибровка проекции мировых координат в координаты карты

```java
MapProjection projection = MapProjection.affine()
    .worldPoint(-1000, -1000).mapPoint(0, 0)
    .worldPoint(1000, 1000).mapPoint(2048, 2048)
    .build();
```

## Связанные ссылки

- [MapCanvas](<../../../widgets/map/map-canvas.md>): Интерактивный виджет 2D-карты, расширяющий [WorldCanvas](<../../../widgets/world/world-canvas.md>) поддержкой размеров карты, слоями текстур и сетки, проекцией координат и маркерами.
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.

## API

<div class="api-member-card" markdown="1">

### `MapProjection(float, float, float, float)` {: #mapprojectionfloat-float-float-float data-toc-label="MapProjection(float, float, float, float)" }

Создает MapProjection напрямую с явными значениями масштаба и смещения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `scaleX` | `float` | Масштабный коэффициент по горизонтали. |
| `offsetX` | `float` | Смещение сдвига по горизонтали. |
| `scaleY` | `float` | Масштабный коэффициент по вертикали. |
| `offsetY` | `float` | Смещение сдвига по вертикали. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`affine()` {: #affine data-toc-label="affine()" }

Запускает билдер для плавной настройки проекции через калибровочные пары точек.

Возвращает `Builder`: Новый экземпляр билдера Builder..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`axisAligned(float, float, float, float, float, float, float, float)` {: #axisalignedfloat-float-float-float-float-float-float-float data-toc-label="axisAligned(float, float, float, float, float, float, float, float)" }

Создает выровненную по осям проекцию, откалиброванную по двум парам точек мир-карта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX1` | `float` | Мировая координата X первой точки калибровки. |
| `worldY1` | `float` | Мировая координата Y первой точки калибровки. |
| `mapX1` | `float` | Координата X карты, соответствующая worldX1. |
| `mapY1` | `float` | Координата Y карты, соответствующая worldY1. |
| `worldX2` | `float` | Мировая координата X второй точки калибровки. |
| `worldY2` | `float` | Мировая координата Y второй точки калибровки. |
| `mapX2` | `float` | Координата X карты, соответствующая worldX2. |
| `mapY2` | `float` | Координата Y карты, соответствующая worldY2. |

Возвращает `MapProjection`: Созданный экземпляр MapProjection..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`identity()` {: #identity data-toc-label="identity()" }

Возвращает единичную проекцию, где мировые координаты соответствуют координатам карты 1:1.

Возвращает `MapProjection`: Созданный экземпляр MapProjection..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`mapX(float)` {: #mapxfloat data-toc-label="mapX(float)" }

Преобразует внешнюю мировую координату X в координату X карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Мировая координата X. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`mapY(float)` {: #mapyfloat data-toc-label="mapY(float)" }

Преобразует внешнюю мировую координату Y в координату Y карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`offsetX()` {: #offsetx data-toc-label="offsetX()" }

Возвращает смещение сдвига по горизонтали X.

Возвращает `float`: Смещение сдвига оси..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`offsetY()` {: #offsety data-toc-label="offsetY()" }

Возвращает смещение сдвига по вертикали Y.

Возвращает `float`: Смещение сдвига оси..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`project(float, float)` {: #projectfloat-float data-toc-label="project(float, float)" }

Преобразует внешние мировые координаты в точку на карте.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Мировая координата X. |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `ViewportPoint`: Преобразованная 2D-координатная точка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`scaleX()` {: #scalex data-toc-label="scaleX()" }

Возвращает масштабный коэффициент по горизонтали X.

Возвращает `float`: Масштабный коэффициент оси..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`scaleY()` {: #scaley data-toc-label="scaleY()" }

Возвращает масштабный коэффициент по вертикали Y.

Возвращает `float`: Масштабный коэффициент оси..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`unproject(float, float)` {: #unprojectfloat-float data-toc-label="unproject(float, float)" }

Выполняет обратное преобразование точки карты во внешние мировые координаты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mapX` | `float` | Координата X карты. |
| `mapY` | `float` | Координата Y карты. |

Возвращает `ViewportPoint`: Преобразованная 2D-координатная точка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`worldX(float)` {: #worldxfloat data-toc-label="worldX(float)" }

Выполняет обратное преобразование координаты X карты во внешнюю мировую координату X.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mapX` | `float` | Координата X карты. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`worldY(float)` {: #worldyfloat data-toc-label="worldY(float)" }

Выполняет обратное преобразование координаты Y карты во внешнюю мировую координату Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mapY` | `float` | Координата Y карты. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

