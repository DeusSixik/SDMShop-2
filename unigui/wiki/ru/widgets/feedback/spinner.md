---
title: "Spinner"
slug: "spinner"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# Spinner

Вращающийся индикатор прогресса (спиннер) для отображения фоновых операций.

## Обзор

`Spinner` — виджет кругового индикатора загрузки, визуализирующий непрерывный фоновый процесс.

Ключевые возможности:
- Различные стили отображения: сегментированные кольца, точки, непрерывные дуги.
- Настройка скорости вращения, радиуса, количества сегментов и толщины линий.
- Кастомизация цветов активных и неактивных элементов индикатора.

## Отображение анимированного спиннера

```java
Spinner spinner = new Spinner(Spinner.Style.ARC_SWEEP);
spinner.speed(1.5f);
```

## Связанные ссылки

- [LoadingIndicator](<loading-indicator.md>): Виджет индикатора загрузки с поддержкой режимов кольца, полосы, пульсации и точек.

## API

<div class="api-member-card" markdown="1">

### `Spinner()` {: #spinner data-toc-label="Spinner()" }

Создает Spinner с размерами по умолчанию.

</div>

<div class="api-member-card" markdown="1">

### `Spinner(Style)` {: #spinnerstyle data-toc-label="Spinner(Style)" }

Создает Spinner с указанным пресетом визуального стиля.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `style` | `Style` | Визуальный стиль анимации. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`activeDots(int)` {: #activedotsint data-toc-label="activeDots(int)" }

Устанавливает количество подсвеченных активных точек в точечных стилях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `activeDots` | `int` | Количество подсвеченных активных точек. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`angle(float)` {: #anglefloat data-toc-label="angle(float)" }

Устанавливает угол охвата дуги в радианах для дуговых стилей спиннера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `radians` | `float` | Угол фазы вращения в радианах. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`arcs(int)` {: #arcsint data-toc-label="arcs(int)" }

Устанавливает количество дуг для многодуговых стилей спиннера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `arcs` | `int` | Количество сегментов или дуг. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`dots(int)` {: #dotsint data-toc-label="dots(int)" }

Устанавливает количество точек для точечных стилей спиннера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dots` | `int` | Общее количество точек. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`indicatorSize(float)` {: #indicatorsizefloat data-toc-label="indicatorSize(float)" }

Устанавливает одновременно одинаковую предпочитаемую ширину и высоту.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `size` | `float` | Размер стороны или диаметр индикатора. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

Устанавливает явный радиус индикатора в пикселях или 0 для расчета из границ.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `radius` | `float` | Радиус окружности или дуги в пикселях. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`segments(int)` {: #segmentsint data-toc-label="segments(int)" }

Устанавливает количество сегментов для сегментированных стилей спиннера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `segments` | `int` | Количество сегментов или дуг. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`speed(float)` {: #speedfloat data-toc-label="speed(float)" }

Устанавливает множитель скорости анимации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `speed` | `float` | Множитель скорости анимации. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`style()` {: #style data-toc-label="style()" }

Возвращает визуальный стиль спиннера.

Возвращает `Style`: Визуальный стиль анимации спиннера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`style(Style)` {: #stylestyle data-toc-label="style(Style)" }

Устанавливает визуальный стиль спиннера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `style` | `Style` | Визуальный стиль анимации. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

Устанавливает толщину линии обводки в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `thickness` | `float` | Толщина линии в пикселях. |

Возвращает `Spinner`: Текущий экземпляр виджета для цепочки вызовов..

</div>

