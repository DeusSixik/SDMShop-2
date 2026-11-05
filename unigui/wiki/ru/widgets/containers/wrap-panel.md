---
title: "WrapPanel"
slug: "wrap-panel"
category: "Widgets/Containers"
locale: "ru"
order: 100
---

# WrapPanel

Потоковый flow-контейнер с переносом детей на новую строку или колонку при нехватке места.

## Обзор

`WrapPanel` — многострочный потоковый контейнер (`FlexWrap.WRAP`). При горизонтальной ориентации дети идут слева направо и переносятся на новую строку при достижении края. При вертикальной — сверху вниз и переносятся в новую колонку.

- **`spacing(px)`**: Расстояние между элементами внутри одной линии.
- **`lineSpacing(px)`**: Расстояние между соседними линиями переноса.

## Облако тегов (чипов)

```java
WrapPanel chips = new WrapPanel()
        .spacing(6.0f)
        .lineSpacing(6.0f);
chips.addChild(new Chip("Action"));
chips.addChild(new Chip("Adventure"));
chips.addChild(new Chip("RPG"));
```

## Связанные ссылки

- [FlexBox](<flex-box.md>)
- [LinearBox](<linear-box.md>)

## API

<div class="api-member-card" markdown="1">

### `WrapPanel()` {: #wrappanel data-toc-label="WrapPanel()" }

**Конструктор**

Создаёт WrapPanel с горизонтальной ориентацией.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WrapPanel::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Ориентация потока**

Задаёт направление потока (HORIZONTAL — строки, VERTICAL — колонки).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | целевая ориентация |

Возвращает `WrapPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WrapPanel::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

**Интервал элементов**

Задаёт отступ между элементами в одной строке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spacing` | `float` | отступ в пикселях UI |

Возвращает `WrapPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WrapPanel::</span>`lineSpacing(float)` {: #linespacingfloat data-toc-label="lineSpacing(float)" }

**Интервал линий**

Задаёт отступ между перенесёнными строками/колонками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `lineSpacing` | `float` | отступ линий в пикселях UI |

Возвращает `WrapPanel`: этот виджет для fluent-настройки.

</div>

