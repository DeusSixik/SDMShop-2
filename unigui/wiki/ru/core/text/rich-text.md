---
title: "RichText"
slug: "rich-text"
category: "Core/Text"
locale: "ru"
order: 10
---

# RichText

Неизменяемая модель форматированного текста, объединяющая стилизованные текстовые фрагменты, шрифты, градиенты и inline-элементы.

## Обзор

`RichText` представляет стилизованный текст, составленный из множества текстовых фрагментов (`TextRun`) и опционального inline-содержимого (иконки, текстуры, произвольная графика). Предоставляет две проекции: `spans()` для полного потока атомов разметки и `runs()` для совместимости со старыми чисто текстовыми потребителями.

## Span'ы и проекции данных

Текст хранится как неизменяемый список `RichTextSpan`. Текстовые фрагменты (`TextRun`) содержат последовательности символов с индивидуальными шрифтами, размерами в пикселях, цветами, кернингом (tracking) и трансформациями регистра. Элементы inline-контента (`InlineContentSpan`) описывают атомарные блоки компоновки с резервным текстом (fallback text), отрисовываемые через процедурные колбэки.

## Inline-контент и иконки

Встроенные inline-элементы бесшовно участвуют в текстовом потоке: переносятся по строкам, учитываются при измерении и выравнивании, но не создают отдельных виджетов в иерархии UI. Иконки и кастомные рендереры автоматически наследуют родительские clip-области и трансформации.

## Кисти (Brushes) и градиенты

RichText поддерживает процедурные кисти `TextBrush`. Методы `withBrush()` и `withLinearGradient()` позволяют залить все текстовые фрагменты сплошным цветом или плавным линейным градиентом, сохраняя при этом базовый цветовой тинт.

## Трансформация и срезы текста

Метод `slice(start, end)` извлекает подстроку по символьным индексам plain-текста. Полностью входящие в диапазон inline-span'ы сохраняются как графические элементы, а частично пересекающиеся безопасно деградируют до plain-текста. Метод `withTextStyle()` позволяет пакетно менять размер, межбуквенный интервал и регистр символов.

## Builder API

Сборка смешанного текста с иконками выполняется через fluent-строитель `RichText.builder()`. Параметры стиля (`font`, `size`, `color`, `brush`, `tracking`, `transform`) применяются ко всем последующим вызовам `append()`, а графика добавляется методами `icon()` и `inline()`.

## Сборка текста с встроенными иконками

```java
RichText text = RichText.builder()
    .append("Статус: ")
    .icon("icon:ok", checkTexture, 12.0f)
    .color(ColorView.rgb(80, 220, 100))
    .append(" Все системы в норме")
    .build();
```

## Применение линейного градиента к тексту

```java
RichText gradientTitle = RichText.gradient(
    "UniGUI Engine",
    Fonts.TITLE,
    18.0f,
    ColorView.rgb(255, 100, 100),
    ColorView.rgb(100, 150, 255),
    45.0f
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`plain(String)` {: #plainstring data-toc-label="plain(String)" }

**plain(String)**

Создаёт простой rich-text из одного фрагмента без явного шрифта и стилей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Исходная текстовая строка. |

Возвращает `RichText`: Экземпляр RichText из одного run'а..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`resolve(String)` {: #resolvestring data-toc-label="resolve(String)" }

**resolve(String)**

Разбирает строку через активный InlineContentResolver, преобразуя текстовые маркеры в inline-контент.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Исходный текст, возможно содержащий маркеры. |

Возвращает `RichText`: Экземпляр RichText с разрешёнными inline-элементами..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`of(String, FontFace, float)` {: #ofstring-fontface-float data-toc-label="of(String, FontFace, float)" }

**of(String, FontFace, float)**

Создаёт rich-text с указанным шрифтом и размером в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Исходный текст. |
| `font` | `FontFace` | Гарнитура шрифта или null для стандартного. |
| `pixelSize` | `float` | Размер шрифта в UI-пикселях. |

Возвращает `RichText`: Экземпляр RichText из одного run'а..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`of(String, FontFace, float, ColorView)` {: #ofstring-fontface-float-colorview data-toc-label="of(String, FontFace, float, ColorView)" }

**of(String, FontFace, float, ColorView)**

Создаёт rich-text с указанным шрифтом, размером и цветом вершин.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Исходный текст. |
| `font` | `FontFace` | Гарнитура шрифта или null для дефолтного. |
| `pixelSize` | `float` | Размер шрифта в UI-пикселях. |
| `color` | `ColorView` | Цвет текста или null для использования цвета виджета. |

Возвращает `RichText`: Экземпляр RichText из одного run'а..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`brushed(String, FontFace, float, TextBrush)` {: #brushedstring-fontface-float-textbrush data-toc-label="brushed(String, FontFace, float, TextBrush)" }

**brushed(String, FontFace, float, TextBrush)**

Создаёт rich-text с указанным шрифтом, размером и процедурной кистью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Исходный текст. |
| `font` | `FontFace` | Гарнитура шрифта или null для дефолтного. |
| `pixelSize` | `float` | Размер шрифта в UI-пикселях. |
| `brush` | `TextBrush` | Кисть текста или null для сплошной заливки. |

Возвращает `RichText`: Экземпляр RichText из одного run'а..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`gradient(String, FontFace, float, ColorView, ColorView, float)` {: #gradientstring-fontface-float-colorview-colorview-float data-toc-label="gradient(String, FontFace, float, ColorView, ColorView, float)" }

**gradient(String, FontFace, float, ColorView, ColorView, float)**

Создаёт rich-text с линейным градиентным заполнением символов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Исходный текст. |
| `font` | `FontFace` | Гарнитура шрифта или null для дефолтного. |
| `pixelSize` | `float` | Размер шрифта в UI-пикселях. |
| `startColor` | `ColorView` | Начальный цвет градиента. |
| `endColor` | `ColorView` | Конечный цвет градиента. |
| `angleDegrees` | `float` | Угол направления градиента в градусах. |

Возвращает `RichText`: Экземпляр градиентного RichText..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`ofSpans(List)` {: #ofspanslist data-toc-label="ofSpans(List)" }

**ofSpans(List)**

Создаёт неизменяемый rich-text из полного списка layout-span'ов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spans` | `List` | Список текстовых run'ов и inline-элементов. |

Возвращает `RichText`: Нормализованный экземпляр RichText..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`builder()` {: #builder data-toc-label="builder()" }

**builder()**

Создаёт новый fluent builder для пошаговой сборки текста со стилями и иконками.

Возвращает `Builder`: Новый экземпляр RichText.Builder..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`runs()` {: #runs data-toc-label="runs()" }

**runs()**

Возвращает неизменяемый список только текстовых фрагментов без inline-элементов.

Возвращает `List`: Список TextRun..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`spans()` {: #spans data-toc-label="spans()" }

**spans()**

Возвращает неизменяемый список всех layout-span'ов (текст + inline).

Возвращает `List`: Полный список RichTextSpan..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`plainText()` {: #plaintext data-toc-label="plainText()" }

**plainText()**

Возвращает плоское строковое представление всего rich-текста.

Возвращает `String`: Обычная строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`hasInlineContent()` {: #hasinlinecontent data-toc-label="hasInlineContent()" }

**hasInlineContent()**

Проверяет, содержит ли текст хотя бы один встроенный inline-элемент.

Возвращает `boolean`: true, если есть inline-содержимое..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`isEmpty()` {: #isempty data-toc-label="isEmpty()" }

**isEmpty()**

Проверяет, пуст ли текст после нормализации span'ов.

Возвращает `boolean`: true, если текст пуст..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`withBrush(TextBrush)` {: #withbrushtextbrush data-toc-label="withBrush(TextBrush)" }

**withBrush(TextBrush)**

Возвращает копию текста, в которой ко всем текстовым фрагментам применена указанная кисть.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `brush` | `TextBrush` | Новая кисть или null для сплошной заливки. |

Возвращает `RichText`: Обновлённая копия RichText..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`withTextStyle(Float, Float, TextTransform)` {: #withtextstylefloat-float-texttransform data-toc-label="withTextStyle(Float, Float, TextTransform)" }

**withTextStyle(Float, Float, TextTransform)**

Возвращает копию текста с переопределёнными размером, трекингом и регистром символов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pixelSize` | `Float` | Новый размер шрифта или null для сохранения текущего. |
| `tracking` | `Float` | Новый межбуквенный интервал или null для сохранения текущего. |
| `transform` | `TextTransform` | Новая трансформация текста или null для сохранения текущей. |

Возвращает `RichText`: Обновлённая копия RichText..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`withLinearGradient(ColorView, ColorView, float)` {: #withlineargradientcolorview-colorview-float data-toc-label="withLinearGradient(ColorView, ColorView, float)" }

**withLinearGradient(ColorView, ColorView, float)**

Возвращает копию текста с линейным градиентом на всех фрагментах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `startColor` | `ColorView` | Начальный цвет. |
| `endColor` | `ColorView` | Конечный цвет. |
| `angleDegrees` | `float` | Угол в градусах. |

Возвращает `RichText`: Обновлённая копия RichText..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`append(RichText)` {: #appendrichtext data-toc-label="append(RichText)" }

**append(RichText)**

Присоединяет другой rich-text в конец текущего.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `other` | `RichText` | Добавляемый rich-text или null. |

Возвращает `RichText`: Объединённый экземпляр RichText..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`slice(int, int)` {: #sliceint-int data-toc-label="slice(int, int)" }

**slice(int, int)**

Возвращает срез текста по символьным смещениям plain-текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `startInclusive` | `int` | Индекс начала включительно. |
| `endExclusive` | `int` | Индекс конца исключительно. |

Возвращает `RichText`: Новый срез RichText..

</div>

