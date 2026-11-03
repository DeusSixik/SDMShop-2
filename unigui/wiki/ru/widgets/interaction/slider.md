---
title: "Slider"
slug: "slider"
category: "Widgets/Interaction"
locale: "ru"
order: 180
---

# Slider

Элемент управления диапазоном значений с перетаскиваемым бегунком и шагом изменения.

## Обзор

`Slider` расширяет `Box`, предоставляя перетаскиваемый ползунок выбора числового значения. Отображает фоновый трек, заполненную активную часть и круглый бегунок.

Поддерживает непрерывные значения или квантование через `step()`, ограничение пределами `min()` и `max()`, генерируя `SliderValueChangedEvent`.

## Ползунок яркости

```java
Slider brightness = new Slider()
        .range(0.0f, 100.0f)
        .value(75.0f)
        .step(5.0f);
brightness.onValueChanged(e -> setBrightness(e.newValue()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

**Состояние перетаскивания**

Возвращает true, если ползунок перетаскивается мышью.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`fillColor()` {: #fillcolor data-toc-label="fillColor()" }

**Цвет заполнения**

Возвращает цвет активной полосы заполнения.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`knobColor()` {: #knobcolor data-toc-label="knobColor()" }

**Цвет ползунка**

Возвращает цвет круглого ползунка.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`max()` {: #max data-toc-label="max()" }

**Получить максимум**

Возвращает верхнюю границу значений слайдера.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`min()` {: #min data-toc-label="min()" }

**Получить минимум**

Возвращает нижнюю границу значений слайдера.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`onValueChanged(EventListener)` {: #onvaluechangedeventlistener data-toc-label="onValueChanged(EventListener)" }

**Слушатель изменения значения**

Регистрирует коллбэк, вызываемый при изменении значения ползунка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

**Получить предпочтительную высоту**

Возвращает предпочтительную высоту для компоновки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

**Задать предпочтительную высоту**

Устанавливает предпочтительную высоту слайдера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | — |

Возвращает `Slider`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

**Получить предпочтительную ширину**

Возвращает предпочтительную ширину для компоновки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

**Задать предпочтительную ширину**

Устанавливает предпочтительную ширину слайдера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | — |

Возвращает `Slider`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`range(float, float)` {: #rangefloat-float data-toc-label="range(float, float)" }

**Задать диапазон**

Задаёт минимальную и максимальную границы значений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `min` | `float` | минимальное значение |
| `max` | `float` | максимальное значение |

Возвращает `Slider`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`step()` {: #step data-toc-label="step()" }

**Получить шаг**

Возвращает шаг дискретизации слайдера.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`trackColor()` {: #trackcolor data-toc-label="trackColor()" }

**Цвет дорожки**

Возвращает цвет фоновой дорожки слайдера.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`value()` {: #value data-toc-label="value()" }

**Получить значение**

Возвращает текущее нормализованное значение слайдера.

Возвращает `float`.

</div>

