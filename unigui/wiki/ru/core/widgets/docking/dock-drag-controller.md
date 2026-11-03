---
title: "DockDragController"
slug: "dock-drag-controller"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockDragController

Обрабатывает жесты drag-and-drop для вкладок докинга внутри [DockingRoot](<../../../widgets/docking/docking-root.md>), вычисляя предпросмотр сброса, отрыв в окно и намерения докинга.

## Обзор

`DockDragController` обрабатывает перетаскивание вкладок в системе докинга.

Выполняет поиск узлов под курсором, распознает отрыв вкладки в плавающее окно при вытягивании за пределы докинга и формирует объект намерения сброса `DockDropIntent`.

## Связанные ссылки

- [DockDropIntent](<dock-drop-intent.md>): Разрешенное намерение сброса докинга, содержащее целевую область, границы предпросмотра и идентификаторы исходной и целевой панелей.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`active()` {: #active data-toc-label="active()" }

Проверяет, зарегистрировано ли начальное нажатие для возможного перетаскивания вкладки.

Возвращает `boolean`: True, если в данный момент выполняется жест перетаскивания..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`dragThreshold()` {: #dragthreshold data-toc-label="dragThreshold()" }

Возвращает порог смещения в пикселях до начала перетаскивания вкладки.

Возвращает `float`: Порог смещения в пикселях до начала перетаскивания..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`dragThreshold(float)` {: #dragthresholdfloat data-toc-label="dragThreshold(float)" }

Задает порог смещения в пикселях до начала перетаскивания вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dragThreshold` | `float` | Порог в пикселях до начала перетаскивания вкладки. |

Возвращает `DockDragController`: Данный контроллер для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

Проверяет, превысило ли движение мыши порог перетаскивания и активно ли перемещение вкладки.

Возвращает `boolean`: True, если перетаскивание превысило порог смещения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`paneId()` {: #paneid data-toc-label="paneId()" }

Возвращает уникальный идентификатор перемещаемой панели докинга.

Возвращает `String`: Идентификатор перетаскиваемой панели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`pointerId()` {: #pointerid data-toc-label="pointerId()" }

Возвращает идентификатор активного указателя мыши.

Возвращает `int`: Идентификатор активного указателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`previewIntent()` {: #previewintent data-toc-label="previewIntent()" }

Возвращает текущее разрешенное намерение сброса или предпросмотра.

Возвращает `DockDropIntent`: Текущее разрешенное намерение сброса..

</div>

