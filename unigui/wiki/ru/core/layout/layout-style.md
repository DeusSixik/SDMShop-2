---
title: "LayoutStyle"
slug: "layout-style"
category: "Core/Layout"
locale: "ru"
order: 20
---

# LayoutStyle

Правила, по которым родитель измеряет и размещает виджет: размер, отступы, flex, grid, позиция и переполнение.

## Обзор

Геометрия виджета настраивается через `widget.layout(style -> ...)`. Вручную вычислять экранные координаты не нужно. Любой размер — это **запрос**: родительский контейнер, правила flex/grid и ограничения min/max вычисляют итоговые границы.

| Понятие | Значение |
| --- | --- |
| **Родитель / Ребёнок** | Контейнер управляет компоновкой; дочерний виджет задаёт свои правила поведения. |
| **Желаемый размер** | Запрашиваемые габариты (`width`, `height`, `size`). Могут быть скорректированы контейнером. |
| **`SizeValue.auto()`** | Автоматический подбор размера по содержимому виджета или доступному месту (не равен 100%). |
| **Главная ось** | Направление flex: ширина для `ROW`, высота для `COLUMN`. |
| **Поперечная ось** | Ось, перпендикулярная главной (высота для `ROW`, ширина для `COLUMN`). |
| **Свободное место** | Пространство, оставшееся после базовых размеров и отступов. Делится через `flexGrow` и `flexShrink`. |

!!! warning "Модель компоновки UniGUI и веб-CSS"
    Хотя названия повторяют CSS, UniGUI использует собственную предсказуемую систему компоновки. В частности, `PositionType.FIXED` фиксирует виджет относительно ближайшего хоста компоновки (окна или диалога), а не физического экрана монитора.

## Размер и позиция

Размеры задаются методами `width`, `height`, `size` или `sizePercent`. Значениями могут быть UI-пиксели, проценты родителя или `auto`. Границы `minWidth` / `minHeight` и `maxWidth` / `maxHeight` строго ограничивают диапазон.

| Метод / Помощник | Тип | Описание |
| --- | --- | --- |
| `width(w)` / `height(h)` | Габариты | Задаёт ширину или высоту (`px`, `percent` или `auto`). |
| `size(w, h)` | Сокращение | Задаёт одновременно ширину и высоту в UI-пикселях. |
| `sizePercent(w, h)` | Проценты | Запрашивает процентную долю от доступного размера родителя. |
| `fixed(w, h)` | Фиксация | Задаёт размер в пикселях и отключает flex-рост и сжатие (`0 0 auto`). |
| `fill()` | 100% Родителя | Просит 100% ширины и 100% высоты (`sizePercent(100, 100)`). |
| `expand()` | Flex-остаток | Забирает всё свободное место только по **главной оси** flex-родителя (`1 1 0px`). |
| `position(ABSOLUTE)` | Вне потока | Выводит виджет из потока; координаты через `left`, `top`, `right`, `bottom` или `inset`. |

!!! tip "Разница между `fill()` и `expand()`"
    Используйте `fill()`, если элемент должен занять 100% родителя по обеим осям независимо от соседей.
    Используйте `expand()`, если дочерний flex-виджет (например, область содержимого) должен занять оставшееся место после фиксированных элементов (заголовка, панели кнопок).

## Отступы и переполнение

Отступы управляют внешними зазорами, внутренними полями и поведением при выходе содержимого за границы.

| Свойство отступа | Расположение | Назначение |
| --- | --- | --- |
| `margin(...)` | Снаружи | Отделяет виджет от соседних элементов, не сдвигая собственное содержимое. |
| `marginAutoHorizontal()` / `Vertical()` | Снаружи | Поглощает свободное место оси, центрируя виджет или прижимая к краю. |
| `padding(...)` | Внутри | Отодвигает дочерние элементы внутрь от границ текущего виджета. |
| `gap(row, col)` | Между детьми | Задаёт расстояние между элементами flex-линий или ячейками сетки. |

| Режим переполнения | Поведение |
| --- | --- |
| `Overflow.VISIBLE` | Содержимое отображается за пределами границ виджета без обрезки (по умолчанию). |
| `Overflow.HIDDEN` | Выходящее за границы содержимое аккуратно обрезается. |
| `Overflow.AUTO` | Помечает ось как прокручиваемую. Саму прокрутку и бегунки реализует `ScrollView`. |

!!! note "Организация прокрутки"
    Установка `overflowY(Overflow.AUTO)` сообщает компоновщику о допустимости скролла, однако саму интерактивную прокрутку и скроллбары реализует контейнер `ScrollView`.

## Flex

Flexbox последовательно размещает дочерние элементы вдоль одной главной оси (`ROW` или `COLUMN`).

### Свойства контейнера

| Свойство | Ось / Область | Значения | Назначение |
| --- | --- | --- | --- |
| `flexDirection` | Главная ось | `ROW`, `COLUMN`, `ROW_REVERSE`, `COLUMN_REVERSE` | Направление потока элементов. |
| `flexWrap` | Перенос строк | `NOWRAP`, `WRAP`, `WRAP_REVERSE` | Размещение в одну строку/колонку либо перенос на новые. |
| `justifyContent` | **Главная** ось | `START`, `CENTER`, `END`, `SPACE_BETWEEN`, `SPACE_AROUND`, `SPACE_EVENLY` | Распределение элементов вдоль главной оси. |
| `alignItems` | **Поперечная** ось | `STRETCH`, `START`, `CENTER`, `END`, `BASELINE` | Выравнивание элементов перпендикулярно главной оси. |
| `alignContent` | Многострочность | `START`, `CENTER`, `END`, `STRETCH`, `SPACE_BETWEEN` | Распределение линий переноса (работает только при `WRAP`). |
| `gap(row, col)` | Зазоры | UI-пиксели (например, `gap(8.0f)`) | Расстояние между элементами и между линиями. |

### Поведение дочерних элементов

| Готовый помощник | То же, что | Поведение |
| --- | --- | --- |
| `flexNone()` | `0 0 auto` | Жёсткий: сохраняет заданный размер; не растягивается и не сжимается. |
| `expand()` | `1 1 0px` | Жадный: забирает всё свободное место по главной оси родителя. |
| `flexAuto()` | `1 1 auto` | Адаптивный: берёт размер содержимого, растёт при наличии места, сжимается при тесноте. |
| `flexInitial()` | `0 1 auto` | Сжимаемый: держит базовый размер, сжимается при тесноте, но сам не растёт. |
| `flex(grow, shrink, basis)` | Ручной | Веса для распределения свободного места и стартовый базовый размер. |
| `alignSelf(Align)` | Переопределение | Заменяет родительский `alignItems` для конкретного ребёнка. |

## Сетка и выравнивание

Сетка (CSS Grid) организует виджеты в двухмерную матрицу колонок и строк, задаваемых определениями `GridTrack`.

### Типы треков (`GridTrack`)

| Метод создания | Тип | Описание | Пример |
| --- | --- | --- | --- |
| `GridTrack.px(n)` | Фиксированный | Точный размер трека в UI-пикселях. | `GridTrack.px(96.0f)` |
| `GridTrack.fr(n)` | Дробный (fr) | Доля от оставшегося свободного места. | `GridTrack.fr(1.0f)` |
| `GridTrack.percent(n)` | Процентный | Процент от габаритов контейнера сетки. | `GridTrack.percent(33.3f)` |
| `GridTrack.auto()` | Автоматический | Размер подбирается по самому крупному элементу ячейки. | `GridTrack.auto()` |
| `GridTrack.minmax(min, max)` | Ограниченный | Задаёт допустимый диапазон размера трека от min до max. | `GridTrack.minmax(px(50), fr(1))` |

### Настройка сетки и размещение ячеек

| Метод | Область | Описание |
| --- | --- | --- |
| `gridTemplateColumns(...)` | Контейнер | Явный шаблон колонок сетки. |
| `gridTemplateRows(...)` | Контейнер | Явный шаблон строк сетки. |
| `gridAutoColumns(...)` / `gridAutoRows(...)` | Контейнер | Размеры треков, создаваемых автоматически при выходе за шаблон. |
| `gridAutoFlow(GridAutoFlow)` | Контейнер | Направление авторазмещения: `ROW`, `COLUMN`, `ROW_DENSE`, `COLUMN_DENSE`. |
| `gridColumn(start, span)` | Элемент | Номер стартовой колонки (нумерация с 1) и ширина span (`<= 0` — авторазмещение). |
| `gridRow(start, span)` | Элемент | Номер стартовой строки (нумерация с 1) и высота span (`<= 0` — авторазмещение). |
| `alignSelf` / `centerSelf` | Элемент | Выравнивает конкретный виджет внутри его ячейки. |
| `zIndex(int)` | Элемент | Порядок наложения слоёв (виджеты с большим zIndex рисуются поверх). |

## Пакеты и копии

Стиль виджета автоматически запускает пересчёт компоновки при изменении любого свойства. Чтобы избежать повторных пересчётов при множественных правках, объединяйте вызовы в блок `update(...)` — пересчёт сработает ровно один раз по завершении блока.

```java
style.update(s -> s
        .width(240.0f)
        .padding(12.0f)
        .flexNone());
```

| Метод жизненного цикла | Сохраняет Callback | Когда использовать |
| --- | --- | --- |
| `update(Consumer<LayoutStyle>)` | Да | Пакетное применение нескольких свойств с одним пересчётом в конце. Поддерживает вложенность. |
| `copy()` | Нет | Создаёт отдельный снимок свойств в новом объекте без привязки к виджету. |
| `copyFrom(LayoutStyle other)` | Да | Копирует все значения из другого стиля, сохраняя callback текущего виджета. |

## Фиксированный заголовок и растягиваемое тело

```java
VBox root = new VBox();
Label header = new Label("Files");
VBox content = new VBox();

header.layout(style -> style.height(24.0f).flexNone());
content.layout(style -> style.expand().padding(12.0f));
root.addChild(header);
root.addChild(content);
```

## Фиксированная колонка и гибкая колонка

```java
LayoutStyle grid = new LayoutStyle()
        .gridTemplateColumns(GridTrack.px(96.0f), GridTrack.fr(1.0f))
        .gridAutoRowsOf(GridTrack.auto())
        .gap(8.0f, 12.0f);
```

## API

### Размер и позиция

| Метод | Описание |
| --- | --- |
| [`position(PositionType)`](#positionpositiontype) | Выбирает режим участия в потоке (RELATIVE/STATIC) или внепотоковое позиционирование (ABSOLUTE/FIXED). |
| [`width(SizeValue)`](#widthsizevalue) | Задаёт желаемую ширину через auto, UI-пиксели или процент родителя. |
| [`width(float)`](#widthfloat) | Задаёт желаемую ширину в UI-пикселях. |
| [`height(SizeValue)`](#heightsizevalue) | Задаёт желаемую высоту через auto, UI-пиксели или процент родителя. |
| [`height(float)`](#heightfloat) | Задаёт желаемую высоту в UI-пикселях. |
| [`size(SizeValue, SizeValue)`](#sizesizevalue-sizevalue) | Задаёт одновременно ширину и высоту через объекты SizeValue. |
| [`size(float, float)`](#sizefloat-float) | Запрашивает ширину и высоту в UI-пикселях. Значение `LayoutConstraints.AUTO` превращается в `auto`. |
| [`sizePercent(float, float)`](#sizepercentfloat-float) | Запрашивает ширину и высоту как процент от доступного размера родителя. |
| [`fixed(float, float)`](#fixedfloat-float) | Задаёт точные размеры в пикселях и полностью отключает flex-рост и сжатие (0 0 auto). |
| [`fill()`](#fill) | Запрашивает 100% ширины и 100% высоты родительского контейнера. |
| [`minWidth(SizeValue)`](#minwidthsizevalue) | Задаёт нижнюю границу ширины для предотвращения нежелательного схлопывания. |
| [`maxWidth(SizeValue)`](#maxwidthsizevalue) | Задаёт верхнюю границу ширины виджета. |
| [`minHeight(SizeValue)`](#minheightsizevalue) | Задаёт нижнюю границу высоты виджета. |
| [`maxHeight(SizeValue)`](#maxheightsizevalue) | Задаёт верхнюю границу высоты виджета. |
| [`inset(SizeValue, SizeValue, SizeValue, SizeValue)`](#insetsizevalue-sizevalue-sizevalue-sizevalue) | Задаёт расстояния до краёв родителя, обычно для ABSOLUTE-позиционирования. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`position(PositionType)` {: #positionpositiontype data-toc-label="position(PositionType)" }

**Режим позиционирования**

Выбирает режим участия в потоке (RELATIVE/STATIC) или внепотоковое позиционирование (ABSOLUTE/FIXED).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `position` | `PositionType` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`width(SizeValue)` {: #widthsizevalue data-toc-label="width(SizeValue)" }

**Ширина (SizeValue)**

Задаёт желаемую ширину через auto, UI-пиксели или процент родителя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`width(float)` {: #widthfloat data-toc-label="width(float)" }

**Ширина в пикселях**

Задаёт желаемую ширину в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pixels` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`height(SizeValue)` {: #heightsizevalue data-toc-label="height(SizeValue)" }

**Высота (SizeValue)**

Задаёт желаемую высоту через auto, UI-пиксели или процент родителя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `height` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`height(float)` {: #heightfloat data-toc-label="height(float)" }

**Высота в пикселях**

Задаёт желаемую высоту в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pixels` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`size(SizeValue, SizeValue)` {: #sizesizevalue-sizevalue data-toc-label="size(SizeValue, SizeValue)" }

**Размер (SizeValue)**

Задаёт одновременно ширину и высоту через объекты SizeValue.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `SizeValue` | — |
| `height` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`size(float, float)` {: #sizefloat-float data-toc-label="size(float, float)" }

**Размер в пикселях**

Запрашивает ширину и высоту в UI-пикселях. Значение `LayoutConstraints.AUTO` превращается в `auto`.

Это запрос размера, а не гарантированный итог. Flex, grid, min/max и родитель могут его изменить. Метод не отключает flex-рост и сжатие — для этого нужен `fixed`.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина в UI-пикселях или `LayoutConstraints.AUTO` для размера по содержимому. |
| `height` | `float` | Высота в UI-пикселях или `LayoutConstraints.AUTO` для размера по содержимому. |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`sizePercent(float, float)` {: #sizepercentfloat-float data-toc-label="sizePercent(float, float)" }

**Размер в процентах**

Запрашивает ширину и высоту как процент от доступного размера родителя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widthPercent` | `float` | — |
| `heightPercent` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`fixed(float, float)` {: #fixedfloat-float data-toc-label="fixed(float, float)" }

**Фиксированный размер**

Задаёт точные размеры в пикселях и полностью отключает flex-рост и сжатие (0 0 auto).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | — |
| `height` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`fill()` {: #fill data-toc-label="fill()" }

**Заполнить родителя**

Запрашивает 100% ширины и 100% высоты родительского контейнера.

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`minWidth(SizeValue)` {: #minwidthsizevalue data-toc-label="minWidth(SizeValue)" }

**Минимальная ширина**

Задаёт нижнюю границу ширины для предотвращения нежелательного схлопывания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minWidth` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`maxWidth(SizeValue)` {: #maxwidthsizevalue data-toc-label="maxWidth(SizeValue)" }

**Максимальная ширина**

Задаёт верхнюю границу ширины виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `maxWidth` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`minHeight(SizeValue)` {: #minheightsizevalue data-toc-label="minHeight(SizeValue)" }

**Минимальная высота**

Задаёт нижнюю границу высоты виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minHeight` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`maxHeight(SizeValue)` {: #maxheightsizevalue data-toc-label="maxHeight(SizeValue)" }

**Максимальная высота**

Задаёт верхнюю границу высоты виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `maxHeight` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`inset(SizeValue, SizeValue, SizeValue, SizeValue)` {: #insetsizevalue-sizevalue-sizevalue-sizevalue data-toc-label="inset(SizeValue, SizeValue, SizeValue, SizeValue)" }

**Координаты границ**

Задаёт расстояния до краёв родителя, обычно для ABSOLUTE-позиционирования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `left` | `SizeValue` | — |
| `top` | `SizeValue` | — |
| `right` | `SizeValue` | — |
| `bottom` | `SizeValue` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

### Отступы и переполнение

| Метод | Описание |
| --- | --- |
| [`margin(EdgeInsets)`](#marginedgeinsets) | Задаёт внешние отступы, отделяющие виджет от соседей. |
| [`margin(float)`](#marginfloat) | Задаёт одинаковый внешний отступ со всех четырёх сторон в UI-пикселях. |
| [`margin(float, float)`](#marginfloat-float) | Задаёт горизонтальный и вертикальный внешние отступы в UI-пикселях. |
| [`marginAutoHorizontal()`](#marginautohorizontal) | Разрешает левому и правому отступам поглощать свободное место, центрируя виджет. |
| [`marginAutoVertical()`](#marginautovertical) | Разрешает верхнему и нижнему отступам поглощать свободное место, центрируя виджет. |
| [`padding(EdgeInsets)`](#paddingedgeinsets) | Задаёт внутренние поля между границей виджета и его содержимым. |
| [`padding(float)`](#paddingfloat) | Задаёт одинаковый внутренний отступ со всех четырёх сторон в UI-пикселях. |
| [`padding(float, float)`](#paddingfloat-float) | Задаёт горизонтальный и вертикальный внутренние отступы в UI-пикселях. |
| [`overflowX(Overflow)`](#overflowxoverflow) | Управляет содержимым, выходящим за левую и правую границы (VISIBLE, HIDDEN, AUTO). |
| [`overflowY(Overflow)`](#overflowyoverflow) | Управляет содержимым за верхней и нижней границами; скролл реализует [ScrollView](<../../widgets/containers/scroll-view.md>). |
| [`overflow(Overflow)`](#overflowoverflow) | Задаёт режим переполнения одновременно для горизонтальной и вертикальной осей. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`margin(EdgeInsets)` {: #marginedgeinsets data-toc-label="margin(EdgeInsets)" }

**Внешние отступы (EdgeInsets)**

Задаёт внешние отступы, отделяющие виджет от соседей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `margin` | `EdgeInsets` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

**Единый внешний отступ**

Задаёт одинаковый внешний отступ со всех четырёх сторон в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `all` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`margin(float, float)` {: #marginfloat-float data-toc-label="margin(float, float)" }

**Внешние отступы по осям**

Задаёт горизонтальный и вертикальный внешние отступы в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `horizontal` | `float` | — |
| `vertical` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`marginAutoHorizontal()` {: #marginautohorizontal data-toc-label="marginAutoHorizontal()" }

**Автоматический горизонтальный margin**

Разрешает левому и правому отступам поглощать свободное место, центрируя виджет.

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`marginAutoVertical()` {: #marginautovertical data-toc-label="marginAutoVertical()" }

**Автоматический вертикальный margin**

Разрешает верхнему и нижнему отступам поглощать свободное место, центрируя виджет.

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

**Внутренние отступы (EdgeInsets)**

Задаёт внутренние поля между границей виджета и его содержимым.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `padding` | `EdgeInsets` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`padding(float)` {: #paddingfloat data-toc-label="padding(float)" }

**Одинаковый padding**

Задаёт одинаковый внутренний отступ со всех четырёх сторон в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `all` | `float` | Отступ слева, сверху, справа и снизу. |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`padding(float, float)` {: #paddingfloat-float data-toc-label="padding(float, float)" }

**Внутренние отступы по осям**

Задаёт горизонтальный и вертикальный внутренние отступы в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `horizontal` | `float` | — |
| `vertical` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`overflowX(Overflow)` {: #overflowxoverflow data-toc-label="overflowX(Overflow)" }

**Горизонтальное переполнение**

Управляет содержимым, выходящим за левую и правую границы (VISIBLE, HIDDEN, AUTO).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overflowX` | `Overflow` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`overflowY(Overflow)` {: #overflowyoverflow data-toc-label="overflowY(Overflow)" }

**Вертикальное переполнение**

Управляет содержимым за верхней и нижней границами; скролл реализует [ScrollView](<../../widgets/containers/scroll-view.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overflowY` | `Overflow` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`overflow(Overflow)` {: #overflowoverflow data-toc-label="overflow(Overflow)" }

**Единое переполнение**

Задаёт режим переполнения одновременно для горизонтальной и вертикальной осей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overflow` | `Overflow` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

### Flex-компоновка

| Метод | Описание |
| --- | --- |
| [`flexDirection(FlexDirection)`](#flexdirectionflexdirection) | Задаёт главную ось flex-контейнера (ROW или COLUMN). |
| [`flexWrap(FlexWrap)`](#flexwrapflexwrap) | Управляет переносом дочерних элементов: в одну линию (NOWRAP) или с переносом (WRAP). |
| [`flexFlow(FlexDirection, FlexWrap)`](#flexflowflexdirection-flexwrap) | Задаёт одновременно направление главной оси и режим переноса строк. |
| [`gap(float)`](#gapfloat) | Задаёт одинаковое расстояние между строками и между колонками в UI-пикселях. |
| [`gap(float, float)`](#gapfloat-float) | Задаёт раздельные расстояния между строками и между колонками в UI-пикселях. |
| [`flex(float, float, SizeValue)`](#flexfloat-float-sizevalue) | Задаёт вес роста, вес сжатия и базовый размер до распределения свободного места. |
| [`flexNone()`](#flexnone) | Запрещает растяжение и сжатие, строго сохраняя желаемый размер (0 0 auto). |
| [`expand()`](#expand) | Забирает всё доступное свободное место вдоль главной оси flex-родителя (1 1 0px). |
| [`flexAuto()`](#flexauto) | Размер по содержимому: растёт при наличии места и сжимается при тесноте (1 1 auto). |
| [`flexInitial()`](#flexinitial) | Размер по базовым габаритам: может сжиматься при тесноте, но сам не растёт (0 1 auto). |
| [`justifyContent(Justify)`](#justifycontentjustify) | Распределяет детей вдоль главной оси (START, CENTER, END, SPACE_BETWEEN). |
| [`alignItems(Align)`](#alignitemsalign) | Выравнивает детей вдоль поперечной оси контейнера (STRETCH, START, CENTER, END). |
| [`alignSelf(Align)`](#alignselfalign) | Переопределяет правило alignItems родителя для конкретного дочернего виджета. |
| [`alignContent(AlignContent)`](#aligncontentaligncontent) | Распределяет перенесённые линии вдоль поперечной оси (работает только при WRAP). |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexDirection(FlexDirection)` {: #flexdirectionflexdirection data-toc-label="flexDirection(FlexDirection)" }

**Направление flex**

Задаёт главную ось flex-контейнера (ROW или COLUMN).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flexDirection` | `FlexDirection` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexWrap(FlexWrap)` {: #flexwrapflexwrap data-toc-label="flexWrap(FlexWrap)" }

**Перенос flex-линий**

Управляет переносом дочерних элементов: в одну линию (NOWRAP) или с переносом (WRAP).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flexWrap` | `FlexWrap` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexFlow(FlexDirection, FlexWrap)` {: #flexflowflexdirection-flexwrap data-toc-label="flexFlow(FlexDirection, FlexWrap)" }

**Flex flow**

Задаёт одновременно направление главной оси и режим переноса строк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `direction` | `FlexDirection` | — |
| `wrap` | `FlexWrap` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gap(float)` {: #gapfloat data-toc-label="gap(float)" }

**Единый зазор**

Задаёт одинаковое расстояние между строками и между колонками в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `gap` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gap(float, float)` {: #gapfloat-float data-toc-label="gap(float, float)" }

**Зазоры по осям**

Задаёт раздельные расстояния между строками и между колонками в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rowGap` | `float` | — |
| `columnGap` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flex(float, float, SizeValue)` {: #flexfloat-float-sizevalue data-toc-label="flex(float, float, SizeValue)" }

**Flex shorthand**

Задаёт вес роста, вес сжатия и базовый размер до распределения свободного места.

Grow и shrink — веса, а не проценты. `basis` — стартовый размер на главной оси; `auto` берёт запрос ширины или высоты. Родитель всё равно может изменить итог.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `grow` | `float` | Доля свободного места на главной оси. `0` не растягивает. |
| `shrink` | `float` | Доля нехватки места, когда дети не помещаются. `0` не сжимает. |
| `basis` | `SizeValue` | Базовый размер на главной оси (`auto`, пиксели или проценты). Null становится `auto`. |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexNone()` {: #flexnone data-toc-label="flexNone()" }

**Flex none**

Запрещает растяжение и сжатие, строго сохраняя желаемый размер (0 0 auto).

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`expand()` {: #expand data-toc-label="expand()" }

**Растянуть по главной оси**

Забирает всё доступное свободное место вдоль главной оси flex-родителя (1 1 0px).

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexAuto()` {: #flexauto data-toc-label="flexAuto()" }

**Flex auto**

Размер по содержимому: растёт при наличии места и сжимается при тесноте (1 1 auto).

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexInitial()` {: #flexinitial data-toc-label="flexInitial()" }

**Flex initial**

Размер по базовым габаритам: может сжиматься при тесноте, но сам не растёт (0 1 auto).

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`justifyContent(Justify)` {: #justifycontentjustify data-toc-label="justifyContent(Justify)" }

**Распределение по главной оси**

Распределяет детей вдоль главной оси (START, CENTER, END, SPACE_BETWEEN).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `justifyContent` | `Justify` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`alignItems(Align)` {: #alignitemsalign data-toc-label="alignItems(Align)" }

**Выравнивание по поперечной оси**

Выравнивает детей вдоль поперечной оси контейнера (STRETCH, START, CENTER, END).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `alignItems` | `Align` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`alignSelf(Align)` {: #alignselfalign data-toc-label="alignSelf(Align)" }

**Индивидуальное выравнивание**

Переопределяет правило alignItems родителя для конкретного дочернего виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `alignSelf` | `Align` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`alignContent(AlignContent)` {: #aligncontentaligncontent data-toc-label="alignContent(AlignContent)" }

**Распределение линий**

Распределяет перенесённые линии вдоль поперечной оси (работает только при WRAP).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `alignContent` | `AlignContent` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

### Сетка и выравнивание

| Метод | Описание |
| --- | --- |
| [`gridTemplateColumns(List)`](#gridtemplatecolumnslist) | Задаёт явный шаблон колонок. Пустой список или null оставляет автоматические колонки. |
| [`gridTemplateColumns(GridTrack[])`](#gridtemplatecolumnsgridtrack) | Задаёт явный шаблон колонок сетки через varargs треков. |
| [`gridTemplateRows(List)`](#gridtemplaterowslist) | Задаёт явный шаблон строк сетки. |
| [`gridTemplateRows(GridTrack[])`](#gridtemplaterowsgridtrack) | Задаёт явный шаблон строк сетки через varargs треков. |
| [`gridAutoColumns(List)`](#gridautocolumnslist) | Задаёт размеры колонок, создаваемых автоматически при выходе за шаблон. |
| [`gridAutoRows(List)`](#gridautorowslist) | Задаёт размеры строк, создаваемых автоматически при выходе за шаблон. |
| [`gridAutoFlow(GridAutoFlow)`](#gridautoflowgridautoflow) | Управляет направлением обхода ячеек при авторазмещении (ROW, COLUMN, ROW_DENSE, COLUMN_DENSE). |
| [`gridColumn(int, int)`](#gridcolumnint-int) | Ставит ребёнка в колонку (нумерация с 1) и задаёт ширину span (<= 0 — авторазмещение). |
| [`gridRow(int, int)`](#gridrowint-int) | Ставит ребёнка в строку (нумерация с 1) и задаёт высоту span (<= 0 — авторазмещение). |
| [`aspectRatio(float)`](#aspectratiofloat) | Задаёт пропорцию ширины к высоте (width / height), если одна из осей auto. |
| [`zIndex(int)`](#zindexint) | Задаёт порядок отрисовки среди соседей (виджеты с большим zIndex рисуются поверх). |
| [`center()`](#center) | Центрирует детей контейнера по главной и поперечной осям. |
| [`centerSelf()`](#centerself) | Центрирует этот элемент на поперечной оси родителя (alignSelf(CENTER)). |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateColumns(List)` {: #gridtemplatecolumnslist data-toc-label="gridTemplateColumns(List)" }

**Колонки сетки**

Задаёт явный шаблон колонок. Пустой список или null оставляет автоматические колонки.

Каждый `GridTrack` — одна колонка: `px`, `percent`, `fr`, `auto` или `minmax`. Треки сверх шаблона берутся из `gridAutoColumns`.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tracks` | `List` | Треки колонок по порядку. Null или пустой список очищает шаблон. |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateColumns(GridTrack[])` {: #gridtemplatecolumnsgridtrack data-toc-label="gridTemplateColumns(GridTrack[])" }

**Колонки сетки (varargs)**

Задаёт явный шаблон колонок сетки через varargs треков.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tracks` | `GridTrack[]` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateRows(List)` {: #gridtemplaterowslist data-toc-label="gridTemplateRows(List)" }

**Строки сетки**

Задаёт явный шаблон строк сетки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tracks` | `List` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateRows(GridTrack[])` {: #gridtemplaterowsgridtrack data-toc-label="gridTemplateRows(GridTrack[])" }

**Строки сетки (varargs)**

Задаёт явный шаблон строк сетки через varargs треков.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tracks` | `GridTrack[]` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridAutoColumns(List)` {: #gridautocolumnslist data-toc-label="gridAutoColumns(List)" }

**Авто-колонки**

Задаёт размеры колонок, создаваемых автоматически при выходе за шаблон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tracks` | `List` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridAutoRows(List)` {: #gridautorowslist data-toc-label="gridAutoRows(List)" }

**Авто-строки**

Задаёт размеры строк, создаваемых автоматически при выходе за шаблон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tracks` | `List` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridAutoFlow(GridAutoFlow)` {: #gridautoflowgridautoflow data-toc-label="gridAutoFlow(GridAutoFlow)" }

**Направление авторазмещения**

Управляет направлением обхода ячеек при авторазмещении (ROW, COLUMN, ROW_DENSE, COLUMN_DENSE).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flow` | `GridAutoFlow` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridColumn(int, int)` {: #gridcolumnint-int data-toc-label="gridColumn(int, int)" }

**Размещение в колонках**

Ставит ребёнка в колонку (нумерация с 1) и задаёт ширину span (<= 0 — авторазмещение).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `start` | `int` | — |
| `span` | `int` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridRow(int, int)` {: #gridrowint-int data-toc-label="gridRow(int, int)" }

**Размещение в строках**

Ставит ребёнка в строку (нумерация с 1) и задаёт высоту span (<= 0 — авторазмещение).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `start` | `int` | — |
| `span` | `int` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`aspectRatio(float)` {: #aspectratiofloat data-toc-label="aspectRatio(float)" }

**Соотношение сторон**

Задаёт пропорцию ширины к высоте (width / height), если одна из осей auto.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `ratio` | `float` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`zIndex(int)` {: #zindexint data-toc-label="zIndex(int)" }

**Z-Index**

Задаёт порядок отрисовки среди соседей (виджеты с большим zIndex рисуются поверх).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zIndex` | `int` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`center()` {: #center data-toc-label="center()" }

**Центрировать детей**

Центрирует детей контейнера по главной и поперечной осям.

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`centerSelf()` {: #centerself data-toc-label="centerSelf()" }

**Центрировать себя**

Центрирует этот элемент на поперечной оси родителя (alignSelf(CENTER)).

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

### Пакеты и жизненный цикл

| Метод | Описание |
| --- | --- |
| [`update(Consumer)`](#updateconsumer) | Применяет несколько изменений layout и уведомляет владельца один раз после внешнего блока. |
| [`copy()`](#copy) | Создаёт независимый снимок всех настроек без callback-уведомления родителя. |
| [`copyFrom(LayoutStyle)`](#copyfromlayoutstyle) | Копирует все значения из другого стиля, сохраняя callback текущего владельца. |
| [`onChanged(Runnable)`](#onchangedrunnable) | Регистрирует callback, вызываемый при любом реальном изменении свойств стиля. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`update(Consumer)` {: #updateconsumer data-toc-label="update(Consumer)" }

**Пакетное обновление**

Применяет несколько изменений layout и уведомляет владельца один раз после внешнего блока.

Вложенные вызовы `update` безопасны. Callback срабатывает только после самого внешнего блока и только если свойство реально изменилось. `null` игнорируется.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `update` | `Consumer` | Callback, который меняет этот стиль; null ничего не делает. |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`copy()` {: #copy data-toc-label="copy()" }

**Создать копию**

Создаёт независимый снимок всех настроек без callback-уведомления родителя.

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`copyFrom(LayoutStyle)` {: #copyfromlayoutstyle data-toc-label="copyFrom(LayoutStyle)" }

**Скопировать из другого стиля**

Копирует все значения из другого стиля, сохраняя callback текущего владельца.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `other` | `LayoutStyle` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`onChanged(Runnable)` {: #onchangedrunnable data-toc-label="onChanged(Runnable)" }

**Слушатель изменений**

Регистрирует callback, вызываемый при любом реальном изменении свойств стиля.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `onChanged` | `Runnable` | — |

Возвращает `LayoutStyle`: этот LayoutStyle для fluent-настройки.

</div>

