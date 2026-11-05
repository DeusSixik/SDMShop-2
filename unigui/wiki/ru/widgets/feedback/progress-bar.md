---
title: "ProgressBar"
slug: "progress-bar"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# ProgressBar

Линейный индикатор прогресса, отображающий детерминированное или неопределенное состояние выполнения.

## Обзор

`ProgressBar` обеспечивает визуальную обратную связь о ходе выполнения задачи с помощью детерминированного заполнения или анимированного индикатора активности.

Ключевые возможности:
- Настраиваемый диапазон значений с помощью `min`, `max` и текущего `value`.
- Расчет относительного прогресса от 0.0 до 1.0 (`progress()`).
- Неопределенный (indeterminate) режим для операций неизвестной длительности.
- Настраиваемый визуальный стиль дорожки и индикатора.

## Настройка детерминированного и неопределенного прогресс-бара

```java
ProgressBar bar = new ProgressBar();
bar.range(0.0f, 100.0f).value(45.0f);
```

## Связанные ссылки

- [LoadingIndicator](<loading-indicator.md>): Виджет индикатора загрузки с поддержкой режимов кольца, полосы, пульсации и точек.

## API

<div class="api-member-card" markdown="1">

### `ProgressBar()` {: #progressbar data-toc-label="ProgressBar()" }

Создает ProgressBar с диапазоном по умолчанию от 0 до 100.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`indeterminate()` {: #indeterminate data-toc-label="indeterminate()" }

Возвращает true, если индикатор работает в неопределенном режиме анимации.

Возвращает `boolean`: True, если индикатор работает в неопределенном режиме..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`indeterminate(boolean)` {: #indeterminateboolean data-toc-label="indeterminate(boolean)" }

Устанавливает флаг неопределенного режима анимации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `indeterminate` | `boolean` | Работает ли индикатор прогресса в неопределенном режиме. |

Возвращает `ProgressBar`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`max()` {: #max data-toc-label="max()" }

Возвращает максимальное значение прогресса.

Возвращает `float`: Текущий нормализованный прогресс от 0.0 до 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`max(float)` {: #maxfloat data-toc-label="max(float)" }

Устанавливает максимальное значение прогресса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `max` | `float` | Верхняя граница диапазона. |

Возвращает `ProgressBar`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`min()` {: #min data-toc-label="min()" }

Возвращает минимальное значение прогресса.

Возвращает `float`: Текущий нормализованный прогресс от 0.0 до 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`min(float)` {: #minfloat data-toc-label="min(float)" }

Устанавливает минимальное значение прогресса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `min` | `float` | Нижняя граница диапазона. |

Возвращает `ProgressBar`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`progress()` {: #progress data-toc-label="progress()" }

Возвращает нормализованный прогресс от 0.0 до 1.0.

Возвращает `float`: Текущий нормализованный прогресс от 0.0 до 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`range(float, float)` {: #rangefloat-float data-toc-label="range(float, float)" }

Устанавливает одновременно минимальную и максимальную границы диапазона.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `min` | `float` | Нижняя граница диапазона. |
| `max` | `float` | Верхняя граница диапазона. |

Возвращает `ProgressBar`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`value()` {: #value data-toc-label="value()" }

Возвращает текущее значение прогресса.

Возвращает `float`: Текущий нормализованный прогресс от 0.0 до 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`value(float)` {: #valuefloat data-toc-label="value(float)" }

Устанавливает текущее значение прогресса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `float` | Значение прогресса. |

Возвращает `ProgressBar`: Текущий экземпляр виджета для цепочки вызовов..

</div>

