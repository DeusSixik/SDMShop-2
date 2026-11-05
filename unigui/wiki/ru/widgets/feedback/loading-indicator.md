---
title: "LoadingIndicator"
slug: "loading-indicator"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# LoadingIndicator

Виджет индикатора загрузки с поддержкой режимов кольца, полосы, пульсации и точек.

## Обзор

`LoadingIndicator` предоставляет динамическую визуальную индикацию выполнения фоновых операций и асинхронных задач.

Поддерживаемые стили и режимы:
- Разнообразные режимы анимации: `RING` (кольцо), `BAR` (полоса), `PULSE` (пульсация), `DOTS` (точки).
- Точная настройка числа дуг, скорости, фазы, толщины и геометрических размеров.
- Автоматическая интеграция в цикл обновления кадров при активном состоянии.

## Настройка и отображение индикатора загрузки

```java
LoadingIndicator indicator = new LoadingIndicator();
indicator.mode(LoadingIndicator.Mode.SPINNER).start();
```

## Связанные ссылки

- [ProgressBar](<progress-bar.md>): Линейный индикатор прогресса, отображающий детерминированное или неопределенное состояние выполнения.
- [Spinner](<spinner.md>): Вращающийся индикатор прогресса (спиннер) для отображения фоновых операций.

## API

<div class="api-member-card" markdown="1">

### `LoadingIndicator()` {: #loadingindicator data-toc-label="LoadingIndicator()" }

Создает LoadingIndicator с режимом SPINNER и стилями по умолчанию.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`accentColor()` {: #accentcolor data-toc-label="accentColor()" }

Возвращает основной акцентный цвет индикатора.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`activeDots()` {: #activedots data-toc-label="activeDots()" }

Возвращает количество подсвеченных активных точек.

Возвращает `int`: Количество точек..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`activeDots(int)` {: #activedotsint data-toc-label="activeDots(int)" }

Устанавливает количество подсвеченных активных точек.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `activeDots` | `int` | Количество подсвеченных активных точек. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`angle()` {: #angle data-toc-label="angle()" }

Возвращает угол охвата дуги в радианах.

Возвращает `float`: Угол фазы вращения в радианах..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`angle(float)` {: #anglefloat data-toc-label="angle(float)" }

Устанавливает угол охвата дуги в радианах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `radians` | `float` | Угол фазы вращения в радианах. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`arcs()` {: #arcs data-toc-label="arcs()" }

Возвращает количество дуг для многодуговых стилей.

Возвращает `int`: Количество сегментов или дуг..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`arcs(int)` {: #arcsint data-toc-label="arcs(int)" }

Устанавливает количество дуг для многодуговых стилей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `arcs` | `int` | Количество сегментов или дуг. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`dots()` {: #dots data-toc-label="dots()" }

Возвращает общее количество точек для точечных стилей.

Возвращает `int`: Количество точек..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`dots(int)` {: #dotsint data-toc-label="dots(int)" }

Устанавливает общее количество точек для точечных стилей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dots` | `int` | Общее количество точек. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`indicatorSize(float)` {: #indicatorsizefloat data-toc-label="indicatorSize(float)" }

Устанавливает одновременно одинаковую предпочитаемую ширину и высоту в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `size` | `float` | Размер стороны или диаметр индикатора. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`mode()` {: #mode data-toc-label="mode()" }

Возвращает активный режим отображения (SPINNER, BAR, PULSE, DOTS и др.).

Возвращает `Mode`: Режим отображения индикатора загрузки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`mode(Mode)` {: #modemode data-toc-label="mode(Mode)" }

Устанавливает режим отображения индикатора загрузки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mode` | `Mode` | Режим отображения индикатора загрузки. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`phase()` {: #phase data-toc-label="phase()" }

Возвращает текущую нормализованную фазу анимации от 0.0 до 1.0.

Возвращает `float`: Угол фазы вращения в радианах..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`phase(float)` {: #phasefloat data-toc-label="phase(float)" }

Устанавливает значение фазы анимации вручную.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `phase` | `float` | Угол фазы вращения в радианах. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Возвращает предпочитаемую высоту индикатора.

Возвращает `float`: Числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Устанавливает предпочитаемую высоту индикатора в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | Предпочитаемая высота. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Устанавливает независимые предпочитаемые ширину и высоту.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина в пикселях. |
| `height` | `float` | Высота в пикселях. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Возвращает предпочитаемую ширину индикатора.

Возвращает `float`: Числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Устанавливает предпочитаемую ширину индикатора в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | Предпочитаемая ширина. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`radius()` {: #radius data-toc-label="radius()" }

Возвращает явный радиус индикатора в пикселях или 0 при автоматическом расчете.

Возвращает `float`: Числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

Устанавливает явный радиус индикатора в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `radius` | `float` | Радиус окружности или дуги в пикселях. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`running()` {: #running data-toc-label="running()" }

Возвращает true, если анимация запущена в данный момент.

Возвращает `boolean`: True, если анимация запущена..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`running(boolean)` {: #runningboolean data-toc-label="running(boolean)" }

Запускает или останавливает анимацию индикатора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `running` | `boolean` | Активна ли анимация в текущий момент. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`secondaryColor()` {: #secondarycolor data-toc-label="secondaryColor()" }

Возвращает вторичный цвет подсветки.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`segments()` {: #segments data-toc-label="segments()" }

Возвращает количество сегментов для сегментированных стилей.

Возвращает `int`: Количество сегментов или дуг..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`segments(int)` {: #segmentsint data-toc-label="segments(int)" }

Устанавливает количество сегментов для сегментированных стилей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `segments` | `int` | Количество сегментов или дуг. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`speed()` {: #speed data-toc-label="speed()" }

Возвращает множитель скорости анимации.

Возвращает `float`: Множитель скорости анимации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`speed(float)` {: #speedfloat data-toc-label="speed(float)" }

Устанавливает множитель скорости анимации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `speed` | `float` | Множитель скорости анимации. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`spinnerStyle()` {: #spinnerstyle data-toc-label="spinnerStyle()" }

Возвращает активный пресет стиля спиннера.

Возвращает `Style`: Значение [Spinner](<spinner.md>).[Style](<../../core/styling/style.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`spinnerStyle(Style)` {: #spinnerstylestyle data-toc-label="spinnerStyle(Style)" }

Устанавливает пресет стиля спиннера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spinnerStyle` | `Style` | Визуальный стиль анимации. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`start()` {: #start data-toc-label="start()" }

Запускает анимацию индикатора.

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`stop()` {: #stop data-toc-label="stop()" }

Останавливает анимацию индикатора.

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`thickness()` {: #thickness data-toc-label="thickness()" }

Возвращает толщину линии обводки в пикселях интерфейса.

Возвращает `float`: Числовое значение с плавающей точкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

Устанавливает толщину линии обводки в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `thickness` | `float` | Толщина линии в пикселях. |

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`trackColor()` {: #trackcolor data-toc-label="trackColor()" }

Возвращает фоновый цвет дорожки индикатора.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`useDefaultPreferredSize()` {: #usedefaultpreferredsize data-toc-label="useDefaultPreferredSize()" }

Сбрасывает размеры к значениям по умолчанию для текущего режима.

Возвращает `LoadingIndicator`: Текущий экземпляр виджета для цепочки вызовов..

</div>

