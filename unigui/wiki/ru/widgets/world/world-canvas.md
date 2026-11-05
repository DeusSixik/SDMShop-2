---
title: "WorldCanvas"
slug: "world-canvas"
category: "Виджеты/Мир"
locale: "ru"
order: 0
---

# WorldCanvas

Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.

## Обзор

`WorldCanvas` — холст бесконечного 2D/3D пространства с камерой, проецирующий мировые координаты в экранные пиксели интерфейса.

Ключевые возможности:
- Интерактивное панорамирование и масштабирование перетаскиванием мыши и колесиком (`panningEnabled`, `zoomEnabled`, `wheelPanningEnabled`).
- Модель области просмотра и мировые границы (`viewport`, `worldBounds`, `clampToWorldBounds`).
- Методы преобразования координат: `worldToRoot`, `rootToWorld`, `worldToLocal`, `localToWorld`.
- Многослойная архитектура с фоновыми слоями `WorldLayer` под интерактивными виджетами `AnchorLayer`.

## Настройка холста мира со слоями и областью просмотра

```java
WorldCanvas canvas = new WorldCanvas()
    .viewport(0, 0, 1.0f)
    .zoomRange(0.1f, 5.0f);
canvas.anchorLayer().add("marker", 100, 200, new Button("POI"));
```

## Связанные ссылки

- [AnchorLayer](<../../core/widgets/world/anchor-layer.md>): Слой управления экранными UI-виджетами, привязанными к мировым координатам внутри WorldCanvas.
- [MapCanvas](<../map/map-canvas.md>): Интерактивный виджет 2D-карты, расширяющий WorldCanvas поддержкой размеров карты, слоями текстур и сетки, проекцией координат и маркерами.
- [RibbonLayer](<../../core/widgets/world/ribbon-layer.md>): Слой WorldCanvas для отрисовки текстурированных или сплошных лент, маршрутов и полилиний в мировых координатах.
- [WorldLayer](<../../core/widgets/world/world-layer.md>): Функциональный контракт для фоновых или декоративных слоев отрисовки внутри WorldCanvas.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`addWorldLayer(WorldLayer)` {: #addworldlayerworldlayer data-toc-label="addWorldLayer(WorldLayer)" }

Регистрирует новый слой отрисовки мира.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layer` | `WorldLayer` | Экземпляр [WorldLayer](<../../core/widgets/world/world-layer.md>) для добавления или удаления. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`anchorLayer()` {: #anchorlayer data-toc-label="anchorLayer()" }

Возвращает [AnchorLayer](<../../core/widgets/world/anchor-layer.md>), управляющий экранными виджетами, привязанными к миру.

Возвращает `AnchorLayer`: [AnchorLayer](<../../core/widgets/world/anchor-layer.md>), управляющий привязанными к миру виджетами..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clampToWorldBounds(boolean)` {: #clamptoworldboundsboolean data-toc-label="clampToWorldBounds(boolean)" }

Включает или отключает ограничение панорамирования пределами мировых границ.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `clampToWorldBounds` | `boolean` | Ограничивать ли панорамирование пределами мировых границ. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clearWorldBounds()` {: #clearworldbounds data-toc-label="clearWorldBounds()" }

Очищает мировые границы, разрешая бесконечное панорамирование камеры.

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clearWorldLayers()` {: #clearworldlayers data-toc-label="clearWorldLayers()" }

Удаляет все зарегистрированные слои отрисовки мира.

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clippingEnabled()` {: #clippingenabled data-toc-label="clippingEnabled()" }

Возвращает true, если отрисовка холста обрезается по границам виджета.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clippingEnabled(boolean)` {: #clippingenabledboolean data-toc-label="clippingEnabled(boolean)" }

Включает или отключает отсечение отрисовки по границам компоновки виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `clippingEnabled` | `boolean` | Обрезать ли отрисовку по границам холста. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`consumeWheelWhileHovered()` {: #consumewheelwhilehovered data-toc-label="consumeWheelWhileHovered()" }

Возвращает true, если события колеса мыши перехватываются при наведении на этот холст.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`consumeWheelWhileHovered(boolean)` {: #consumewheelwhilehoveredboolean data-toc-label="consumeWheelWhileHovered(boolean)" }

Устанавливает, должны ли события колеса мыши поглощаться при наведении.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `consumeWheelWhileHovered` | `boolean` | Поглощать ли события колеса мыши при наведении курсора. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`localToWorldX(float)` {: #localtoworldxfloat data-toc-label="localToWorldX(float)" }

Преобразует локальную координату X виджета обратно в мировую координату X.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `localX` | `float` | Локальная координата X виджета. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`localToWorldY(float)` {: #localtoworldyfloat data-toc-label="localToWorldY(float)" }

Преобразует локальную координату Y виджета обратно в мировую координату Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `localY` | `float` | Локальная координата Y виджета. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`onViewportChanged(EventListener)` {: #onviewportchangedeventlistener data-toc-label="onViewportChanged(EventListener)" }

Регистрирует слушатель, вызываемый при изменении положения или зума области просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения положения или зума области просмотра. |

Возвращает `EventSubscription`: EventSubscription для отписки слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panButton()` {: #panbutton data-toc-label="panButton()" }

Возвращает кнопку мыши, используемую для перетаскивания и панорамирования.

Возвращает `PointerButton`: Кнопка мыши, используемая для перетаскивания и панорамирования..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panButton(PointerButton)` {: #panbuttonpointerbutton data-toc-label="panButton(PointerButton)" }

Задает кнопку мыши, используемую для перетаскивания и панорамирования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `panButton` | `PointerButton` | Кнопка мыши, используемая для перетаскивания и панорамирования. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panningEnabled()` {: #panningenabled data-toc-label="panningEnabled()" }

Возвращает true, если перетаскивание мышью для панорамирования включено.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panningEnabled(boolean)` {: #panningenabledboolean data-toc-label="panningEnabled(boolean)" }

Включает или отключает перетаскивание указателем для панорамирования области просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `panningEnabled` | `boolean` | Включено ли панорамирование перетаскиванием указателя. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Возвращает предпочитаемую высоту компоновки холста.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Устанавливает предпочитаемую высоту компоновки холста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | Предпочитаемая высота. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Устанавливает одновременно предпочитаемую ширину и высоту холста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Возвращает предпочитаемую ширину компоновки холста.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Устанавливает предпочитаемую ширину компоновки холста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | Предпочитаемая ширина. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`removeWorldLayer(WorldLayer)` {: #removeworldlayerworldlayer data-toc-label="removeWorldLayer(WorldLayer)" }

Удаляет зарегистрированный слой отрисовки мира.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layer` | `WorldLayer` | Экземпляр [WorldLayer](<../../core/widgets/world/world-layer.md>) для добавления или удаления. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`rootToWorldX(float)` {: #roottoworldxfloat data-toc-label="rootToWorldX(float)" }

Преобразует экранную корневую координату X обратно в мировую координату X.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rootX` | `float` | Экранная координата X. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`rootToWorldY(float)` {: #roottoworldyfloat data-toc-label="rootToWorldY(float)" }

Преобразует экранную корневую координату Y обратно в мировую координату Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rootY` | `float` | Экранная координата Y. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport()` {: #viewport data-toc-label="viewport()" }

Возвращает активную модель 2D-области просмотра.

Возвращает `Viewport2D`: Активная модель области просмотра Viewport2D..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport(Viewport2D)` {: #viewportviewport2d data-toc-label="viewport(Viewport2D)" }

Устанавливает модель 2D-области просмотра для панорамирования и зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `viewport` | `Viewport2D` | Модель 2D-области просмотра для панорамирования и зума. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

Устанавливает координаты центра области просмотра с сохранением текущего зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport(float, float, float)` {: #viewportfloat-float-float data-toc-label="viewport(float, float, float)" }

Устанавливает одновременно координаты центра области просмотра и уровень зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |
| `zoom` | `float` | Масштаб зума. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanStep()` {: #wheelpanstep data-toc-label="wheelPanStep()" }

Возвращает расстояние перемещения камеры за один шаг прокрутки колеса.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanStep(float)` {: #wheelpanstepfloat data-toc-label="wheelPanStep(float)" }

Задает расстояние перемещения камеры за один шаг прокрутки колеса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wheelPanStep` | `float` | Расстояние перемещения за один шаг прокрутки колеса. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanningEnabled()` {: #wheelpanningenabled data-toc-label="wheelPanningEnabled()" }

Возвращает true, если прокрутка колеса мыши выполняет панорамирование камеры.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanningEnabled(boolean)` {: #wheelpanningenabledboolean data-toc-label="wheelPanningEnabled(boolean)" }

Включает или отключает прокрутку колеса мыши для панорамирования камеры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wheelPanningEnabled` | `boolean` | Включено ли панорамирование прокруткой колеса мыши. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldBounds(float, float, float, float)` {: #worldboundsfloat-float-float-float data-toc-label="worldBounds(float, float, float, float)" }

Устанавливает прямоугольные мировые границы, ограничивающие перемещение камеры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldLayers()` {: #worldlayers data-toc-label="worldLayers()" }

Возвращает неизменяемый список всех зарегистрированных слоев отрисовки мира.

Возвращает `List`: Неизменяемый список зарегистрированных слоев мира..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToLocalX(float)` {: #worldtolocalxfloat data-toc-label="worldToLocalX(float)" }

Преобразует мировую координату X в локальную координату X виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Мировая координата X. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToLocalY(float)` {: #worldtolocalyfloat data-toc-label="worldToLocalY(float)" }

Преобразует мировую координату Y в локальную координату Y виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToRootX(float)` {: #worldtorootxfloat data-toc-label="worldToRootX(float)" }

Преобразует мировую координату X в экранную корневую координату X.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldX` | `float` | Мировая координата X. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToRootY(float)` {: #worldtorootyfloat data-toc-label="worldToRootY(float)" }

Преобразует мировую координату Y в экранную корневую координату Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `worldY` | `float` | Мировая координата Y. |

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomEnabled()` {: #zoomenabled data-toc-label="zoomEnabled()" }

Возвращает true, если масштабирование колесом мыши включено.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomEnabled(boolean)` {: #zoomenabledboolean data-toc-label="zoomEnabled(boolean)" }

Включает или отключает масштабирование колесом мыши.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zoomEnabled` | `boolean` | Включено ли масштабирование зума колесом мыши. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomRange(float, float)` {: #zoomrangefloat-float data-toc-label="zoomRange(float, float)" }

Задает допустимые границы минимального и максимального масштаба зума.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minZoom` | `float` | Минимально допустимый масштаб зума. |
| `maxZoom` | `float` | Максимально допустимый масштаб зума. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomStep()` {: #zoomstep data-toc-label="zoomStep()" }

Возвращает множитель зума, применяемый при шаге колеса мыши.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomStep(float)` {: #zoomstepfloat data-toc-label="zoomStep(float)" }

Задает множитель зума, применяемый при шаге колеса мыши.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zoomStep` | `float` | Множитель зума за один шаг колеса мыши. |

Возвращает `WorldCanvas`: Текущий экземпляр для цепочки вызовов..

</div>

