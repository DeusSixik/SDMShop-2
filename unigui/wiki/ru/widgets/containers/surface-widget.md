---
title: "SurfaceWidget"
slug: "surface-widget"
category: "Widgets/Containers"
locale: "ru"
order: 20
---

# SurfaceWidget

Контейнер с композируемой поверхностью: фон, многослойные текстуры, шейдеры, рамка и радиус скругления.

## Обзор

`SurfaceWidget` — основа визуальной системы оформления UniGUI. Вместо привязки к конкретным классам отрисовщиков каждый `SurfaceWidget` включает компонуемую поверхность `Surface`.

Источником фона может выступать сплошной цвет, текстура или процедурный GLSL-шейдер, переключаемый динамически через код, XML или правила темы [StylePack](<../../core/styling/style-pack.md>) без замены экземпляра виджета.

## Текстуры и слои оверлеев

- **Базовый фон**: Настраивается методами `background(color)`, `backgroundTexture(handle)` или `backgroundShader(handle)`.
- **Nine-Slice слайсинг**: Включается через `backgroundTextureFit(ImageFit.NINE_SLICE)` и `sliceBorders(n)` для масштабируемых панелей без искажения углов.
- **Оверлейные текстуры**: Добавление дополнительных текстурных слоёв поверх фона через `overlayTexture(handle)` с индивидуальным тинтом и UV.
- **Анимации и переходы**: Цвета фона, рамки, толщина и радиус скругления поддерживают плавные интерполяции (`animateBackgroundColor`, `animateRadius` и др.).

## Настройка карточки с поверхностью

```java
Box card = new Box()
        .background(ColorView.ofHex("#1A1D24"))
        .border(ColorView.ofHex("#3A3F4D"))
        .borderWidth(1.5f)
        .radius(8.0f);
card.addChild(content);
```

## Связанные ссылки

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`backgroundVisible(boolean)` {: #backgroundvisibleboolean data-toc-label="backgroundVisible(boolean)" }

**Видимость фона**

Включает или выключает отрисовку заливки и текстур фона.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `backgroundVisible` | `boolean` | true для отрисовки фона |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`backgroundVisible()` {: #backgroundvisible data-toc-label="backgroundVisible()" }

**Проверка видимости фона**

Возвращает, включена ли отрисовка фона.

Возвращает `boolean`: true, если фон видим.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`background(ColorView)` {: #backgroundcolorview data-toc-label="background(ColorView)" }

**Установить фон**

Задаёт цвет заливки фона и автоматически включает видимость фона.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | цвет заливки фона или null для прозрачного |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`borderColor(ColorView)` {: #bordercolorcolorview data-toc-label="borderColor(ColorView)" }

**Цвет рамки**

Задаёт цвет рамки и автоматически включает видимость рамки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | цвет рамки |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`border(ColorView)` {: #bordercolorview data-toc-label="border(ColorView)" }

**Установить рамку**

Задаёт цвет обводки рамки и автоматически включает видимость рамки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | цвет обводки рамки |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`borderVisible(boolean)` {: #bordervisibleboolean data-toc-label="borderVisible(boolean)" }

**Видимость рамки**

Включает или выключает отрисовку контура рамки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `borderVisible` | `boolean` | true для отрисовки рамки |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`borderWidth(float)` {: #borderwidthfloat data-toc-label="borderWidth(float)" }

**Толщина рамки**

Задаёт толщину линии рамки в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `borderWidth` | `float` | Толщина в UI-пикселях |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

**Радиус скругления**

Задаёт радиус скругления углов в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `radius` | `float` | Радиус в UI-пикселях |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`boxVisualEnabled(boolean)` {: #boxvisualenabledboolean data-toc-label="boxVisualEnabled(boolean)" }

**Включение поверхности**

Включает или выключает всю визуальную отрисовку поверхности (фон, рамка, текстуры).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `boxVisualEnabled` | `boolean` | true для включения поверхности |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`themeEnabled(boolean)` {: #themeenabledboolean data-toc-label="themeEnabled(boolean)" }

**Поддержка темы**

Управляет тем, могут ли стили глобальной Theme переопределять свойства поверхности.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `themeEnabled` | `boolean` | true для поддержки темы |

Возвращает `SurfaceWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`surface()` {: #surface data-toc-label="surface()" }

**Получить Surface**

Возвращает модель поверхности Surface.

Возвращает `Surface`: экземпляр модели Surface.

</div>

