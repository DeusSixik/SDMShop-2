---
title: "Box"
slug: "box"
category: "Widgets/Containers"
locale: "ru"
order: 30
---

# Box

Стандартный визуальный контейнер: цвет фона, текстура, шейдер, рамка, радиус и дочерние виджеты.

## Обзор

`Box` — основной готовый визуальный контейнер в UniGUI. Он наследует `SurfaceWidget<Box>` с включённой по умолчанию отрисовкой поверхности.

Используйте `Box` для создания карточек, панелей, плашек с фоном и модальных окон.

## Карточка с рамкой и скруглением

```java
Box card = new Box()
        .background(ColorView.ofHex("#2C2F33"))
        .border(ColorView.ofHex("#7289DA"))
        .borderWidth(2.0f)
        .radius(8.0f);
card.addChild(titleLabel);
```

## Связанные ссылки

- [Border](<border.md>)
- [SurfaceWidget](<surface-widget.md>)

## API

<div class="api-member-card" markdown="1">

### `Box()` {: #box data-toc-label="Box()" }

**Конструктор**

Создаёт пустой контейнер Box со включённой поверхностью.

</div>

