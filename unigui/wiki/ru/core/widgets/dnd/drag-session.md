---
title: "DragSession"
slug: "drag-session"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragSession

Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.

## Обзор

`DragSession` представляет активный процесс перетаскивания drag-and-drop.

### Обязанности сессии

- **Отслеживание указателя**: Хранит текущие координаты (`pointerX`, `pointerY`), координаты нажатия и величину смещения (`deltaX`, `deltaY`).
- **Координация цели сброса**: Отслеживает текущий подтвержденный виджет `dropTarget()`.
- **Отрисовка в оверлее**: Управляет положением `DragGhost` и индикатора вставки (`showIndicator`, `clearIndicator`).
- **Автопрокрутка**: Координирует плавную прокрутку при поднесении курсора к краям скроллируемых контейнеров.

## Отображение индикатора сброса

```java
session.showIndicator(x, y, width, 2.0f, MutableColor.rgba(0.2f, 0.6f, 1.0f, 1.0f));
```

## Связанные ссылки

- [DragController](<drag-controller.md>): Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.
- [DropController](<drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`active()` {: #active data-toc-label="active()" }

Проверяет, продолжается ли данная сессия перетаскивания и не была ли отменена.

Возвращает `boolean`: True, если сессия перетаскивания активна и не отменена..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`cancel()` {: #cancel data-toc-label="cancel()" }

Немедленно прерывает сессию перетаскивания с результатом CANCELLED.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`clearIndicator()` {: #clearindicator data-toc-label="clearIndicator()" }

Скрывает индикатор вставки оверлея.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`deltaX()` {: #deltax data-toc-label="deltaX()" }

Возвращает горизонтальное смещение указателя от начальной точки нажатия.

Возвращает `float`: Горизонтальное смещение от начальной точки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`deltaY()` {: #deltay data-toc-label="deltaY()" }

Возвращает вертикальное смещение указателя от начальной точки нажатия.

Возвращает `float`: Вертикальное смещение от начальной точки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`dragged()` {: #dragged data-toc-label="dragged()" }

Возвращает логический виджет, перемещаемый в ходе операции.

Возвращает `Widget`: Логический виджет, перемещаемый операцией перетаскивания..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`dropTarget()` {: #droptarget data-toc-label="dropTarget()" }

Возвращает текущий виджет, подтвердивший прием сброса, или null.

Возвращает `Widget`: Текущий виджет-кандидат на прием сброса или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghost()` {: #ghost data-toc-label="ghost()" }

Возвращает отрисовщик [DragGhost](<drag-ghost.md>), отображаемый в оверлее.

Возвращает `DragGhost`: Отрисовщик [DragGhost](<drag-ghost.md>), используемый для предпросмотра в оверлее..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetX()` {: #ghostoffsetx data-toc-label="ghostOffsetX()" }

Вычисляет горизонтальное экранное смещение для размещения ghost-представления.

Возвращает `float`: Горизонтальное смещение для размещения ghost-отображения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetX(Widget, float)` {: #ghostoffsetxwidget-float data-toc-label="ghostOffsetX(Widget, float)" }

Вычисляет экранное смещение X для конкретного виджета с учетом начальных координат.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | Опорный виджет для расчета координат. |
| `initialX` | `float` | Начальная координата X до перетаскивания. |

Возвращает `float`: Горизонтальное смещение для размещения ghost-отображения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetY()` {: #ghostoffsety data-toc-label="ghostOffsetY()" }

Вычисляет вертикальное экранное смещение для размещения ghost-представления.

Возвращает `float`: Вертикальное смещение для размещения ghost-отображения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetY(Widget, float)` {: #ghostoffsetywidget-float data-toc-label="ghostOffsetY(Widget, float)" }

Вычисляет экранное смещение Y для конкретного виджета с учетом начальных координат.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | Опорный виджет для расчета координат. |
| `initialY` | `float` | Начальная координата Y до перетаскивания. |

Возвращает `float`: Вертикальное смещение для размещения ghost-отображения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`handle()` {: #handle data-toc-label="handle()" }

Возвращает виджет-ручку, инициировавший жест перетаскивания.

Возвращает `Widget`: Виджет-ручка, инициировавший перетаскивание..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorColor()` {: #indicatorcolor data-toc-label="indicatorColor()" }

Возвращает цвет текущего индикатора вставки.

Возвращает `ColorView`: Цвет активного прямоугольника индикатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorHeight()` {: #indicatorheight data-toc-label="indicatorHeight()" }

Возвращает высоту текущего индикатора вставки.

Возвращает `float`: Высота активного прямоугольника индикатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorVisible()` {: #indicatorvisible data-toc-label="indicatorVisible()" }

Проверяет, отображается ли индикатор вставки в данный момент.

Возвращает `boolean`: True, если индикатор сброса отображается в данный момент..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorWidth()` {: #indicatorwidth data-toc-label="indicatorWidth()" }

Возвращает ширину текущего индикатора вставки.

Возвращает `float`: Ширина активного прямоугольника индикатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorX()` {: #indicatorx data-toc-label="indicatorX()" }

Возвращает координату X текущего индикатора вставки.

Возвращает `float`: Координата X активного прямоугольника индикатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorY()` {: #indicatory data-toc-label="indicatorY()" }

Возвращает координату Y текущего индикатора вставки.

Возвращает `float`: Координата Y активного прямоугольника индикатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`payload()` {: #payload data-toc-label="payload()" }

Возвращает неизменяемый объект данных [DragPayload](<drag-payload.md>) текущего жеста.

Возвращает `DragPayload`: Передаваемый объект данных payload..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pointerX()` {: #pointerx data-toc-label="pointerX()" }

Возвращает текущую координату X указателя мыши в координатах оверлея.

Возвращает `float`: Текущая корневая координата X указателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pointerY()` {: #pointery data-toc-label="pointerY()" }

Возвращает текущую координату Y указателя мыши в координатах оверлея.

Возвращает `float`: Текущая корневая координата Y указателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pressX()` {: #pressx data-toc-label="pressX()" }

Возвращает координату X указателя в момент первоначального нажатия.

Возвращает `float`: Начальная корневая координата X в момент нажатия..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pressY()` {: #pressy data-toc-label="pressY()" }

Возвращает координату Y указателя в момент первоначального нажатия.

Возвращает `float`: Начальная корневая координата Y в момент нажатия..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`root()` {: #root data-toc-label="root()" }

Возвращает корневой виджет, содержащий оверлей перетаскивания.

Возвращает `Widget`: Корневой виджет, содержащий слой оверлея перетаскивания..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`showIndicator(float, float, float, float, ColorView)` {: #showindicatorfloat-float-float-float-colorview data-toc-label="showIndicator(float, float, float, float, ColorView)" }

Активирует и позиционирует визуальный прямоугольник индикатора вставки в оверлее.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Координата X прямоугольника индикатора. |
| `y` | `float` | Координата Y прямоугольника индикатора. |
| `width` | `float` | Ширина прямоугольника индикатора. |
| `height` | `float` | Высота прямоугольника индикатора. |
| `color` | `ColorView` | Цвет прямоугольника индикатора. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`uiContext()` {: #uicontext data-toc-label="uiContext()" }

Возвращает связанный контекст пользовательского интерфейса.

Возвращает `UIContext`: Контекст UI, связанный с данной сессией..

</div>

