---
title: "DockSide"
slug: "dock-side"
category: "Core/Виджеты/Контейнеры"
locale: "ru"
order: 125
---

# DockSide

Определяет внешнюю сторону (LEFT, RIGHT, TOP, BOTTOM), к которой прикрепляется дочерний виджет в [DockPanel](<../../../widgets/containers/dock-panel.md>).

## Обзор

`DockSide` определяет четыре внешние стороны, к которым `DockPanel` прикрепляет дочерние виджеты:

- **`LEFT`**: Прижимает виджет к левой границе оставшейся области.
- **`RIGHT`**: Прижимает виджет к правой границе.
- **`TOP`**: Прижимает виджет к верхней границе.
- **`BOTTOM`**: Прижимает виджет к нижней границе.

Примечание: `DockSide` используется исключительно легковесным layout-контейнером `DockPanel`. Для полноценной IDE-системы докинга (плавающие окна, вкладки, зоны сброса) используйте `DockArea` и `DockingManager`.

## Прикрепление к сторонам

```java
dockPanel.addChild(toolbar, DockSide.TOP);
dockPanel.addChild(sidebar, DockSide.LEFT);
dockPanel.addChild(mainView);
dockPanel.lastChildFill(true);
```

## Связанные ссылки

- [DockPanel](<../../../widgets/containers/dock-panel.md>)

