---
title: "DockPanel"
slug: "dock-panel"
category: "Widgets/Containers"
locale: "ru"
order: 120
---

# DockPanel

Контейнер разметки, прикрепляющий дочерние виджеты к внешним границам (слева, справа, сверху, снизу).

## Обзор

DockPanel последовательно размещает детей вдоль краёв, заданных enum `DockSide`. Каждый прикреплённый виджет занимает полосу у соответствующей стороны, уменьшая оставшуюся область для последующих элементов.

## Заполнение центра последним элементом

Если включён `lastChildFill` (по умолчанию), последний ребёнок заполняет всё оставшееся внутреннее пространство. Для сложного IDE-стиля с открепляемыми окнами, зонами сплита и вкладками используйте `DockingRoot` и `DockingManager` вместо `DockPanel`.

## Оболочка окна приложения

```java
DockPanel shell = new DockPanel();
shell.addChild(toolbar, DockSide.TOP);
shell.addChild(sidebar, DockSide.LEFT);
shell.addChild(mainView);
shell.lastChildFill(true);
```

## Связанные ссылки

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)
- [SplitPanel](<split-panel.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPanel::</span>`addChild(Widget, DockSide)` {: #addchildwidget-dockside data-toc-label="addChild(Widget, DockSide)" }

**Добавить прикреплённый виджет**

Добавляет дочерний виджет, прикреплённый к указанной стороне.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | дочерний виджет для добавления |
| `dockSide` | `DockSide` | сторона для прикрепления виджета |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPanel::</span>`dockSide(Widget, DockSide)` {: #docksidewidget-dockside data-toc-label="dockSide(Widget, DockSide)" }

**Сторона прикрепления**

Возвращает или устанавливает сторону прикрепления для дочернего виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | дочерний виджет для добавления |
| `dockSide` | `DockSide` | сторона для прикрепления виджета |

Возвращает `DockPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPanel::</span>`lastChildFill(boolean)` {: #lastchildfillboolean data-toc-label="lastChildFill(boolean)" }

**Заполнение остатка последним элементом**

Управляет тем, заполняет ли последний дочерний элемент всё оставшееся внутреннее пространство.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `lastChildFill` | `boolean` | true, чтобы последний виджет заполнил остаток области |

Возвращает `DockPanel`: этот виджет для fluent-настройки.

</div>

