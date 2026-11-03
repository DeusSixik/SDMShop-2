---
title: "MapCanvas"
slug: "map-canvas"
category: "Виджеты/Карта"
locale: "ru"
order: 0
---

# MapCanvas

Интерактивный виджет 2D-карты, расширяющий [WorldCanvas](<../world/world-canvas.md>) поддержкой размеров карты, слоями текстур и сетки, проекцией координат и маркерами.

## Обзор

`MapCanvas` — высокоуровневый картографический виджет, построенный поверх `WorldCanvas`.

Ключевые возможности:
- Задание размеров карты в специальных координатах карты (`mapSize`, `mapWidth`, `mapHeight`).
- Отрисовка фоновой текстуры или сплошного цвета с выбором UV-подобласти (`texture`, `source`).
- Координатная сетка и контурные границы карты (`gridVisible`, `gridSize`, `mapBorderVisible`).
- Преобразование между мировыми координатами игры и координатами карты с помощью `MapProjection`.
- Удобное управление интерактивными маркерами через `MapMarkerHandle` и `MapMarker`.

## Настройка холста карты и добавление маркеров

```java
MapCanvas map = new MapCanvas()
    .mapSize(2048, 1024)
    .viewport(0, 0, 1.0f);
map.addMarker("spawn", 512, 256, "Spawn");
```

## Связанные ссылки

- [MapMarker](<map-marker.md>): Базовая оболочка маркера для MapCanvas с поддержкой произвольного контента, текстовых меток, состояний выбора и подсветки.
- [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>): Дескриптор для удобной настройки положения, экранного размера, масштабирования от зума, опорной точки (pivot) и видимости маркера на MapCanvas.
- [MapProjection](<../../core/widgets/map/map-projection.md>): Движок линейной проекции для преобразования между внешними мировыми координатами и внутренними координатами карты MapCanvas.
- [WorldCanvas](<../world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.

## API

<div class="api-member-card" markdown="1">

### `MapCanvas()` {: #mapcanvas data-toc-label="MapCanvas()" }

Создает пустой MapCanvas со стандартными размерами карты 1024x512.

</div>

<div class="api-member-card" markdown="1">

### `MapCanvas(TextureHandle)` {: #mapcanvastexturehandle data-toc-label="MapCanvas(TextureHandle)" }

Создает MapCanvas с заданной текстурой и размерами, полученными из текстуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор текстуры. |

</div>

<div class="api-member-card" markdown="1">

### `MapCanvas(TextureHandle, float, float)` {: #mapcanvastexturehandle-float-float data-toc-label="MapCanvas(TextureHandle, float, float)" }

Создает MapCanvas с текстурой и явно указанными размерами ширины и высоты карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор текстуры. |
| `mapWidth` | `float` | Ширина. |
| `mapHeight` | `float` | Высота. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addMarker(String, float, float, MapMarker)` {: #addmarkerstring-float-float-mapmarker data-toc-label="addMarker(String, float, float, MapMarker)" }

Добавляет оболочку [MapMarker](<map-marker.md>) по координатам карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `mapX` | `float` | Координата X карты. |
| `mapY` | `float` | Координата Y карты. |
| `marker` | `MapMarker` | Экземпляр [MapMarker](<map-marker.md>). |

Возвращает `MapMarkerHandle`: Созданный или найденный дескриптор [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addMarkerWidget(String, float, float, Widget)` {: #addmarkerwidgetstring-float-float-widget data-toc-label="addMarkerWidget(String, float, float, Widget)" }

Добавляет произвольный UI-виджет в качестве маркера по координатам карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `mapX` | `float` | Координата X карты. |
| `mapY` | `float` | Координата Y карты. |
| `widget` | `Widget` | Вложенный виджет контента. |

Возвращает `MapMarkerHandle`: Созданный или найденный дескриптор [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addProjectedMarker(String, float, float, MapMarker)` {: #addprojectedmarkerstring-float-float-mapmarker data-toc-label="addProjectedMarker(String, float, float, MapMarker)" }

Добавляет оболочку [MapMarker](<map-marker.md>) по внешним мировым координатам с проекцией в пространство карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `worldX` | `float` | Мировая координата X. |
| `worldY` | `float` | Мировая координата Y. |
| `marker` | `MapMarker` | Экземпляр [MapMarker](<map-marker.md>). |

Возвращает `MapMarkerHandle`: Созданный или найденный дескриптор [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addProjectedMarkerWidget(String, float, float, Widget)` {: #addprojectedmarkerwidgetstring-float-float-widget data-toc-label="addProjectedMarkerWidget(String, float, float, Widget)" }

Добавляет произвольный UI-виджет как маркер по внешним мировым координатам с проекцией в карту.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `worldX` | `float` | Мировая координата X. |
| `worldY` | `float` | Мировая координата Y. |
| `widget` | `Widget` | Вложенный виджет контента. |

Возвращает `MapMarkerHandle`: Созданный или найденный дескриптор [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`backgroundColor()` {: #backgroundcolor data-toc-label="backgroundColor()" }

Возвращает изменяемый фоновый цвет заполнения за пределами границ карты.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`backgroundVisible()` {: #backgroundvisible data-toc-label="backgroundVisible()" }

Возвращает true, если внешний фон холста карты отображается.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`backgroundVisible(boolean)` {: #backgroundvisibleboolean data-toc-label="backgroundVisible(boolean)" }

Включает или отключает отрисовку внешнего фона холста карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `backgroundVisible` | `boolean` | Значение параметра для gridVisible. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`borderColor()` {: #bordercolor data-toc-label="borderColor()" }

Возвращает изменяемый цвет линии границы вокруг карты.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`clampToMapBounds(boolean)` {: #clamptomapboundsboolean data-toc-label="clampToMapBounds(boolean)" }

Ограничивает панорамирование пределами области карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `clamp` | `boolean` | Флаг ограничения. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`externalToMapX(float)` {: #externaltomapxfloat data-toc-label="externalToMapX(float)" }

Преобразует внешнюю мировую координату X в координату карты с помощью текущей проекции.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Мировая координата X. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`externalToMapY(float)` {: #externaltomapyfloat data-toc-label="externalToMapY(float)" }

Преобразует внешнюю мировую координату Y в координату карты с помощью текущей проекции.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridColor()` {: #gridcolor data-toc-label="gridColor()" }

Возвращает изменяемый цвет линий координатной сетки поверх карты.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridSize()` {: #gridsize data-toc-label="gridSize()" }

Возвращает шаг между линиями сетки в единицах карты.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridSize(float)` {: #gridsizefloat data-toc-label="gridSize(float)" }

Задает шаг ячеек координатной сетки поверх карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `gridSize` | `float` | Шаг ячеек координатной сетки. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridVisible()` {: #gridvisible data-toc-label="gridVisible()" }

Возвращает true, если координатная сетка поверх карты отображается.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridVisible(boolean)` {: #gridvisibleboolean data-toc-label="gridVisible(boolean)" }

Включает или отключает линии координатной сетки на карте.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `gridVisible` | `boolean` | Значение параметра для gridVisible. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapBorderVisible()` {: #mapbordervisible data-toc-label="mapBorderVisible()" }

Возвращает true, если контурная рамка вокруг границы карты отображается.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapBorderVisible(boolean)` {: #mapbordervisibleboolean data-toc-label="mapBorderVisible(boolean)" }

Включает или отключает контурную рамку по периметру карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mapBorderVisible` | `boolean` | Значение параметра для gridVisible. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapColor()` {: #mapcolor data-toc-label="mapColor()" }

Возвращает изменяемый резервный цвет заполнения карты, если текстура не загружена.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapHeight()` {: #mapheight data-toc-label="mapHeight()" }

Возвращает высоту карты в единицах координат карты.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapSize(float, float)` {: #mapsizefloat-float data-toc-label="mapSize(float, float)" }

Задает размеры карты в единицах координат карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapWidth()` {: #mapwidth data-toc-label="mapWidth()" }

Возвращает ширину карты в единицах координат карты.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`marker(String)` {: #markerstring data-toc-label="marker(String)" }

Находит дескриптор маркера по его строковому идентификатору или возвращает null, если не найден.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |

Возвращает `MapMarkerHandle`: Созданный или найденный дескриптор [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`moveMarker(String, float, float)` {: #movemarkerstring-float-float data-toc-label="moveMarker(String, float, float)" }

Перемещает существующий маркер в новые координаты карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `mapX` | `float` | Координата X карты. |
| `mapY` | `float` | Координата Y карты. |

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`moveProjectedMarker(String, float, float)` {: #moveprojectedmarkerstring-float-float data-toc-label="moveProjectedMarker(String, float, float)" }

Перемещает существующий маркер в новые внешние мировые координаты с проекцией в пространство карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `worldX` | `float` | Мировая координата X. |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`projection()` {: #projection data-toc-label="projection()" }

Возвращает активную проекцию [MapProjection](<../../core/widgets/map/map-projection.md>), используемую для перевода внешних координат.

Возвращает `MapProjection`: Активная проекция [MapProjection](<../../core/widgets/map/map-projection.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`projection(MapProjection)` {: #projectionmapprojection data-toc-label="projection(MapProjection)" }

Устанавливает проекцию координат для перевода точек мира в пространство карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `projection` | `MapProjection` | Движок проекции координат. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`removeMarker(String)` {: #removemarkerstring data-toc-label="removeMarker(String)" }

Удаляет маркер по его строковому идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`source()` {: #source data-toc-label="source()" }

Возвращает нормализованный UV-прямоугольник исходной текстуры карты.

Возвращает `MutableRect`: Изменяемый UV-прямоугольник исходной текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`source(float, float, float, float)` {: #sourcefloat-float-float-float data-toc-label="source(float, float, float, float)" }

Задает нормализованную UV-подобласть текстуры для отображения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `u` | `float` | Нормализованная координата текстуры U. |
| `v` | `float` | Нормализованная координата текстуры V. |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`texture()` {: #texture data-toc-label="texture()" }

Возвращает дескриптор текстуры фона карты.

Возвращает `TextureHandle`: Дескриптор текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`texture(TextureHandle)` {: #texturetexturehandle data-toc-label="texture(TextureHandle)" }

Устанавливает дескриптор текстуры фона карты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор текстуры. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`tint()` {: #tint data-toc-label="tint()" }

Возвращает изменяемый цвет тонирования текстуры карты.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`viewport(Viewport2D)` {: #viewportviewport2d data-toc-label="viewport(Viewport2D)" }

Устанавливает модель 2D-области просмотра для панорамирования и зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `viewport` | `Viewport2D` | Значение параметра для viewport. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

Устанавливает координаты центра области просмотра с сохранением текущего зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`viewport(float, float, float)` {: #viewportfloat-float-float data-toc-label="viewport(float, float, float)" }

Устанавливает одновременно координаты центра области просмотра и уровень зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |
| `zoom` | `float` | Масштаб зума. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`wheelPanStep(float)` {: #wheelpanstepfloat data-toc-label="wheelPanStep(float)" }

Задает расстояние перемещения при панорамировании колесом мыши.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wheelPanStep` | `float` | Значение параметра для wheelPanStep. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`zoomRange(float, float)` {: #zoomrangefloat-float data-toc-label="zoomRange(float, float)" }

Задает допустимые границы минимального и максимального зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minZoom` | `float` | Минимально допустимый масштаб зума. |
| `maxZoom` | `float` | Максимально допустимый масштаб зума. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`zoomStep(float)` {: #zoomstepfloat data-toc-label="zoomStep(float)" }

Задает множитель масштабирования зума при прокрутке колеса мыши.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zoomStep` | `float` | Значение параметра для zoomStep. |

Возвращает `MapCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

