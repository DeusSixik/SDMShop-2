---
title: "DockSplitOrientation"
slug: "dock-split-orientation"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockSplitOrientation

Ориентация разделения между соседними узлами докинга: HORIZONTAL (бок о бок) или VERTICAL (друг над другом).

## Обзор

`DockSplitOrientation` определяет направление разделения пространства между двумя дочерними узлами докинга.

| Ориентация | Расположение | Первый узел | Второй узел |
| --- | --- | --- | --- |
| `HORIZONTAL` | Бок о бок | Слева | Справа |
| `VERTICAL` | Друг над другом | Сверху | Снизу |

## Связанные ссылки

- [DockNode](<dock-node.md>): Иерархический узел дерева докинга: листовой узел (LEAF) со вкладками [DockPane](<dock-pane.md>) или узел разделения (SPLIT), делящий детей горизонтально или вертикально.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockSplitOrientation::</span>`toWidgetOrientation()` {: #towidgetorientation data-toc-label="toWidgetOrientation()" }

Конвертирует ориентацию докинга в стандартный enum Orientation библиотеки UniGUI.

Возвращает `Orientation`: Эквивалентное значение Orientation из UniGUI..

</div>

