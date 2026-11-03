---
title: "FlexBox"
slug: "flex-box"
category: "Widgets/Containers"
locale: "ru"
order: 70
---

# FlexBox

Flex-контейнер, настраиваемый напрямую через свойства [LayoutStyle](<../../core/layout/layout-style.md>), аналог CSS display: flex.

## Обзор

`FlexBox` — прямой эквивалент CSS `display: flex` в UniGUI. В отличие от `LinearBox` и `WrapPanel`, он не хранит промежуточных состояний ориентации или зазоров.

Все параметры — направление потока, перенос строк, распределение по главной оси (`justifyContent`), выравнивание по поперечной оси (`alignItems`) и отступы (`gap`) — настраиваются напрямую через `layoutStyle()`.

## Выравнивание во FlexBox

```java
FlexBox flex = new FlexBox();
flex.layout(style -> style
        .flexDirection(FlexDirection.ROW)
        .justifyContent(Justify.SPACE_BETWEEN)
        .alignItems(Align.CENTER)
        .gap(8.0f));
```

## Связанные ссылки

- [LayoutStyle](<../../core/layout/layout-style.md>)
- [LinearBox](<linear-box.md>)

## API

<div class="api-member-card" markdown="1">

### `FlexBox()` {: #flexbox data-toc-label="FlexBox()" }

**Конструктор**

Создаёт контейнер FlexBox с направлением ROW по умолчанию.

</div>

