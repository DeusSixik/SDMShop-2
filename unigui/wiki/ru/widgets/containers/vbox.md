---
title: "VBox"
slug: "vbox"
category: "Widgets/Containers"
locale: "ru"
order: 50
---

# VBox

Вертикальный линейный контейнер, размещающий детей сверху вниз с единым шагом отступа.

## Обзор

`VBox` — готовое сокращение для вертикального `LinearBox`. Дети раскладываются сверху вниз через flex-адаптер (`FlexDirection.COLUMN`) без переноса строк.

Расстояние между соседними элементами задаётся через `spacing(pixels)` и сохраняется в `rowGap`. Отдельные дочерние элементы могут забирать свободное место через `child.layout(s -> s.expand())`.

## Вертикальная форма ввода

```java
VBox form = new VBox().spacing(10.0f);
form.addChild(new Label("Логин"));
form.addChild(new TextField());
form.addChild(new Label("Пароль"));
form.addChild(new PasswordField());
```

## Связанные ссылки

- [HBox](<hbox.md>)
- [LinearBox](<linear-box.md>)
- [ScrollView](<scroll-view.md>)

## API

<div class="api-member-card" markdown="1">

### `VBox()` {: #vbox data-toc-label="VBox()" }

**Конструктор**

Создаёт пустой вертикальный линейный контейнер.

</div>

