---
title: "HBox"
slug: "hbox"
category: "Widgets/Containers"
locale: "ru"
order: 60
---

# HBox

Горизонтальный линейный контейнер, размещающий детей слева направо с единым шагом отступа.

## Обзор

`HBox` — готовое сокращение для горизонтального `LinearBox`. Дети раскладываются слева направо через flex-адаптер (`FlexDirection.ROW`) без переноса строк.

Расстояние между соседними элементами задаётся через `spacing(pixels)` и сохраняется в `columnGap`. Идеален для панелей инструментов, строк кнопок и навигационных блоков.

## Панель кнопок

```java
HBox toolbar = new HBox().spacing(6.0f);
toolbar.addChild(new Button("Создать"));
toolbar.addChild(new Button("Открыть"));
toolbar.addChild(new Button("Сохранить"));
```

## Связанные ссылки

- [LinearBox](<linear-box.md>)
- [PanelRowWidget](<panel-row-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### `HBox()` {: #hbox data-toc-label="HBox()" }

**Конструктор**

Создаёт пустой горизонтальный линейный контейнер.

</div>

