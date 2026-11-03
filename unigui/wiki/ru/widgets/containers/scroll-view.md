---
title: "ScrollView"
slug: "scroll-view"
category: "Widgets/Containers"
locale: "ru"
order: 110
---

# ScrollView

Контейнер области прокрутки (viewport), вмещающий один дочерний виджет и управляющий горизонтальным и вертикальным скроллингом.

## Обзор

ScrollView предоставляет прокручиваемый viewport для содержимого, превышающего видимые границы. Прокрутка настраивается по осям X и Y через параметры overflow (`Overflow.AUTO`, `Overflow.SCROLL`, `Overflow.HIDDEN`). Поддерживается скролл колесом мыши с переключением на горизонтальную ось при зажатом Shift, ограничение диапазона и точная настройка шага скролла.

## Режимы и стилизация полос прокрутки

Полосы прокрутки могут работать в режиме наложения overlay (плавают поверх содержимого с эффектом наведения) или в режиме резервирования (выделяют отдельное пространство под дорожки скроллбара). Можно настраивать цвет дорожки, цвет ползунка, подсветку при наведении, толщину полос и отступы.

## Базовый ScrollView с переполнением

```java
ScrollView scroll = new ScrollView(largeWidget).scrollStep(18.0f);
scroll.layout(style -> style.overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO));
```

## Связанные ссылки

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

**Задать контент**

Устанавливает дочерний виджет содержимого внутри viewport.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | виджет для отображения внутри viewport |

Возвращает `ScrollView`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`scrollTo(float, float)` {: #scrolltofloat-float data-toc-label="scrollTo(float, float)" }

**Прокрутить к координатам**

Прокручивает viewport к абсолютным координатам (x, y), с ограничением допустимыми границами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | целевая горизонтальная позиция скролла |
| `y` | `float` | целевая вертикальная позиция скролла |

Возвращает `ScrollView`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`scrollBy(float, float)` {: #scrollbyfloat-float data-toc-label="scrollBy(float, float)" }

**Прокрутить относительно**

Прокручивает viewport относительно текущей позиции на dx и dy.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dx` | `float` | горизонтальное смещение |
| `dy` | `float` | вертикальное смещение |

Возвращает `ScrollView`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`scrollStep(float)` {: #scrollstepfloat data-toc-label="scrollStep(float)" }

**Задать шаг скролла**

Задаёт множитель шага скролла в пикселях UI на единицу дельты колеса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `scrollStep` | `float` | множитель шага скролла; минимум 1.0 |

Возвращает `ScrollView`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`overlayScrollBars(boolean)` {: #overlayscrollbarsboolean data-toc-label="overlayScrollBars(boolean)" }

**Режим Overlay скроллбаров**

Определяет, накладываются ли скроллбары поверх контента или резервируют место в разметке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overlayScrollBars` | `boolean` | true для режима overlay, false для резервирования места |

Возвращает `ScrollView`: этот виджет для fluent-настройки.

</div>

