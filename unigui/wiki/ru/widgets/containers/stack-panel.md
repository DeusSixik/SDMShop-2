---
title: "StackPanel"
slug: "stack-panel"
category: "Widgets/Containers"
locale: "ru"
order: 90
---

# StackPanel

Контейнер наложения (overlay), размещающий всех детей в одном общем слоте.

## Обзор

`StackPanel` — контейнер наложения, в котором все дочерние элементы помещаются в один общий прямоугольный слот. Каждый ребёнок выравнивается в соответствии со своими отступами margin, желаемыми размерами и правилами выравнивания (`Alignment.STRETCH`, `CENTER` и др.).

Это делает `StackPanel` идеальным для многослойных композиций: наложения декоративных фонов, бейджей уведомлений, индикаторов статуса или водяных знаков поверх контента.

## Бейдж поверх аватара

```java
StackPanel stack = new StackPanel();
stack.addChild(avatarImage);
badge.layout(style -> style
        .position(PositionType.ABSOLUTE)
        .inset(0.0f, Float.NaN, Float.NaN, 0.0f));
stack.addChild(badge);
```

## Связанные ссылки

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">StackPanel::</span>`arrangeChild(Widget, float, float, float, float)` {: #arrangechildwidget-float-float-float-float data-toc-label="arrangeChild(Widget, float, float, float, float)" }

**Размещение дочернего виджета**

Размещает дочерний элемент внутри слота с учётом margin и alignment.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | Добавляемый дочерний виджет |
| `slotX` | `float` | координата X слота |
| `slotY` | `float` | координата Y слота |
| `slotWidth` | `float` | ширина слота |
| `slotHeight` | `float` | высота слота |

</div>

