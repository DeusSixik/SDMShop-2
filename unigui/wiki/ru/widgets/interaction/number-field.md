---
title: "NumberField"
slug: "number-field"
category: "Widgets/Interaction"
locale: "ru"
order: 130
---

# NumberField

Поле ввода чисел с поддержкой ограничений диапазона, шага изменения и настройки перетаскиванием.

## Обзор

`NumberField` расширяет `TextInput`, форматируя и валидируя ввод чисел. Пользователи могут вводить значения вручную, использовать стрелки клавиатуры для шага `step()` или перетаскивать значения мышью.

## Ввод числового диапазона

```java
NumberField volume = new NumberField()
        .range(0.0, 100.0)
        .step(1.0)
        .value(50.0);
volume.onValueChanged(e -> audio.setVolume(e.newValue()));
```

## Связанные ссылки

- [Slider](<slider.md>)
- [TextInput](<text-input.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NumberField::</span>`range(double, double)` {: #rangedouble-double data-toc-label="range(double, double)" }

**Задать диапазон**

Задаёт минимальную и максимальную границы диапазона.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `min` | `double` | минимальная граница |
| `max` | `double` | максимальная граница |

Возвращает `NumberField`: этот виджет для fluent-настройки.

</div>

