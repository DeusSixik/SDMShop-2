---
title: "AnchorWidget"
slug: "anchor-widget"
category: "Core/Виджеты/Мир"
locale: "ru"
order: 0
---

# AnchorWidget

Пространственный якорь, привязывающий экранный UI-виджет к мировым координатам с масштабированием от зума, настройкой pivot и отсечением вне экрана.

## Обзор

`AnchorWidget` — пространственный контроллер для UI-виджета, закрепленного за точкой в мировых координатах внутри `WorldCanvas`.

Возможности:
- Позиционирование в мировых координатах (`worldPosition`, `worldX`, `worldY`).
- Экранные размеры независимо от зума (`screenSize`) или пропорциональное масштабирование (`zoomScaling`).
- Ограничения минимального и максимального размера для сохранения читаемости при экстремальном зуме.
- Настройка опорной точки (`pivot`), диапазона зума видимости (`visibleZoomRange`) и отсечения вне экрана (`cullOutsideViewport`).

## Настройка свойств пространственного якоря

```java
AnchorWidget anchor = canvas.anchorLayer().add("pin", 100, 200, new Button("Marker"))
    .screenSize(64, 24)
    .pivot(0.5f, 1.0f);
```

## Связанные ссылки

- [AnchorLayer](<anchor-layer.md>): Слой управления экранными UI-виджетами, привязанными к мировым координатам внутри [WorldCanvas](<../../../widgets/world/world-canvas.md>).
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`arrangedVisible()` {: #arrangedvisible data-toc-label="arrangedVisible()" }

Возвращает true, если якорь видим после проверок зума, отсечения и компоновки.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`cullOutsideViewport()` {: #culloutsideviewport data-toc-label="cullOutsideViewport()" }

Возвращает true, если якорь автоматически отсекается вне области просмотра холста.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`cullOutsideViewport(boolean)` {: #culloutsideviewportboolean data-toc-label="cullOutsideViewport(boolean)" }

Включает или отключает автоматическое отсечение якоря вне видимой области экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `cullOutsideViewport` | `boolean` | Отсекать ли этот якорь автоматически при выходе за пределы области просмотра. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`id()` {: #id data-toc-label="id()" }

Возвращает уникальный идентификатор этого якоря в слое.

Возвращает `String`: Строковый идентификатор или текст..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxScreenHeight()` {: #maxscreenheight data-toc-label="maxScreenHeight()" }

Возвращает ограничение максимальной экранной высоты после зум-масштабирования.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxScreenSize(float, float)` {: #maxscreensizefloat-float data-toc-label="maxScreenSize(float, float)" }

Устанавливает максимальные экранные размеры для предотвращения чрезмерного увеличения при приближении.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxScreenWidth()` {: #maxscreenwidth data-toc-label="maxScreenWidth()" }

Возвращает ограничение максимальной экранной ширины после зум-масштабирования.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxVisibleZoom()` {: #maxvisiblezoom data-toc-label="maxVisibleZoom()" }

Возвращает верхний порог зума для видимости якоря.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minScreenHeight()` {: #minscreenheight data-toc-label="minScreenHeight()" }

Возвращает ограничение минимальной экранной высоты после зум-масштабирования.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minScreenSize(float, float)` {: #minscreensizefloat-float data-toc-label="minScreenSize(float, float)" }

Устанавливает минимальные экранные размеры для предотвращения чрезмерного сжатия при отдалении.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minScreenWidth()` {: #minscreenwidth data-toc-label="minScreenWidth()" }

Возвращает ограничение минимальной экранной ширины после зум-масштабирования.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minVisibleZoom()` {: #minvisiblezoom data-toc-label="minVisibleZoom()" }

Возвращает нижний порог зума для видимости якоря.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`pivot(float, float)` {: #pivotfloat-float data-toc-label="pivot(float, float)" }

Настраивает нормализованную точку привязки (pivot), совмещаемую с мировой позицией якоря.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`pivotX()` {: #pivotx data-toc-label="pivotX()" }

Возвращает нормализованную горизонтальную координату pivot (0.0=слева, 0.5=центр, 1.0=справа).

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`pivotY()` {: #pivoty data-toc-label="pivotY()" }

Возвращает нормализованную вертикальную координату pivot (0.0=сверху, 0.5=центр, 1.0=снизу).

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`projectedRootX()` {: #projectedrootx data-toc-label="projectedRootX()" }

Возвращает последнюю рассчитанную экранную координату X точки привязки якоря.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`projectedRootY()` {: #projectedrooty data-toc-label="projectedRootY()" }

Возвращает последнюю рассчитанную экранную координату Y точки привязки якоря.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`referenceZoom()` {: #referencezoom data-toc-label="referenceZoom()" }

Возвращает опорный коэффициент зума, при котором виджет отображается в базовом размере.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`screenHeight()` {: #screenheight data-toc-label="screenHeight()" }

Возвращает фиксированную экранную высоту в пикселях или -1 при использовании желаемого размера виджета.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`screenSize(float, float)` {: #screensizefloat-float data-toc-label="screenSize(float, float)" }

Устанавливает фиксированные экранные размеры в пикселях интерфейса для этого якоря.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина. |
| `height` | `float` | Высота. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`screenWidth()` {: #screenwidth data-toc-label="screenWidth()" }

Возвращает фиксированную экранную ширину в пикселях или -1 при использовании желаемого размера виджета.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`visible()` {: #visible data-toc-label="visible()" }

Возвращает флаг ручной видимости этого якоря.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Вручную устанавливает состояние видимости этого якоря.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | Флаг видимости. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`visibleZoomRange(float, float)` {: #visiblezoomrangefloat-float data-toc-label="visibleZoomRange(float, float)" }

Ограничивает видимость якоря заданным диапазоном зума области просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minZoom` | `float` | Минимально допустимый масштаб зума. |
| `maxZoom` | `float` | Максимально допустимый масштаб зума. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`widget()` {: #widget data-toc-label="widget()" }

Возвращает вложенный UI-виджет, позиционируемый этим якорем.

Возвращает `Widget`: Вложенный UI-виджет..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`worldPosition(float, float)` {: #worldpositionfloat-float data-toc-label="worldPosition(float, float)" }

Обновляет положение якоря в мировых координатах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`worldX()` {: #worldx data-toc-label="worldX()" }

Возвращает мировую координату X якоря.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`worldY()` {: #worldy data-toc-label="worldY()" }

Возвращает мировую координату Y якоря.

Возвращает `float`: Координата или числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`zoomScaling()` {: #zoomscaling data-toc-label="zoomScaling()" }

Возвращает true, если размеры виджета масштабируются пропорционально зуму области просмотра.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`zoomScaling(boolean, float)` {: #zoomscalingboolean-float data-toc-label="zoomScaling(boolean, float)" }

Настраивает динамическое масштабирование размеров виджета в зависимости от зума области просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zoomScaling` | `boolean` | True для динамического масштабирования размера виджета от зума. |
| `referenceZoom` | `float` | Опорный коэффициент зума, при котором виджет имеет базовый экранный размер. |

Возвращает `AnchorWidget`: Текущий экземпляр для цепочки вызовов..

</div>

