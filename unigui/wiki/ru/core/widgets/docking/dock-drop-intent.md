---
title: "DockDropIntent"
slug: "dock-drop-intent"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockDropIntent

Разрешенное намерение сброса докинга, содержащее целевую область, границы предпросмотра и идентификаторы исходной и целевой панелей.

## Обзор

`DockDropIntent` содержит разрешенное намерение сброса вкладки докинга.

Включает целевую область `DockArea`, ID опорной панели, границы прямоугольника предпросмотра и признак разделения, вкладки или плавающего окна.

## Связанные ссылки

- [DockArea](<dock-area.md>): Целевая зона назначения для операций докинга: краевые разделения (LEFT, RIGHT, TOP, BOTTOM), группировка во вкладки (CENTER, TAB) или плавающие окна (FLOAT).
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`floating()` {: #floating data-toc-label="floating()" }

Проверяет, запрашивает ли данное намерение отрыв в плавающее окно.

Возвращает `boolean`: True, если намерение запрашивает отрыв в плавающее окно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`floating(String, float, float)` {: #floatingstring-float-float data-toc-label="floating(String, float, float)" }

Создает намерение отрыва панели в отдельное плавающее окно.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `sourcePaneId` | `String` | Идентификатор перетаскиваемой исходной панели. |
| `pointerX` | `float` | Координата X указателя. |
| `pointerY` | `float` | Координата Y указателя. |

Возвращает `DockDropIntent`: Экземпляр разрешенного намерения сброса докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`none()` {: #none data-toc-label="none()" }

Создает неактивный пустой экземпляр намерения сброса.

Возвращает `DockDropIntent`: Экземпляр разрешенного намерения сброса докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`of(String, String, DockArea, RectView, float, float)` {: #ofstring-string-dockarea-rectview-float-float data-toc-label="of(String, String, DockArea, RectView, float, float)" }

Создает экземпляр намерения сброса с указанием целевой области, панелей и прямоугольника предпросмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `sourcePaneId` | `String` | Идентификатор перетаскиваемой исходной панели. |
| `targetPaneId` | `String` | Идентификатор целевой панели под курсором. |
| `area` | `DockArea` | Целевая область докинга. |
| `previewBounds` | `RectView` | Прямоугольные границы зоны предпросмотра. |
| `pointerX` | `float` | Координата X указателя. |
| `pointerY` | `float` | Координата Y указателя. |

Возвращает `DockDropIntent`: Экземпляр разрешенного намерения сброса докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`split()` {: #split data-toc-label="split()" }

Проверяет, запрашивает ли данное намерение краевое разделение пространства (LEFT, RIGHT, TOP, BOTTOM).

Возвращает `boolean`: True, если намерение запрашивает докинг с разделением области..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`tabbed()` {: #tabbed data-toc-label="tabbed()" }

Проверяет, запрашивает ли данное намерение объединение во вкладки с целевой панелью.

Возвращает `boolean`: True, если намерение запрашивает группировку в существующие вкладки..

</div>

