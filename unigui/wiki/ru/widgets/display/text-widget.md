---
title: "TextWidget"
slug: "text-widget"
category: "Widgets/Display"
locale: "ru"
order: 10
---

# TextWidget

Базовый абстрактный виджет отображения текста, поддерживающий строки, [RichText](<../../core/text/rich-text.md>), шрифты, градиенты и кисти.

## Обзор

`TextWidget` служит базовым классом для `Label`, `Text`, `TextBlock` и `RichTextView`. Он инкапсулирует выбор шрифта, кегль, строковые и `RichText` модели текста, цветовые градиенты и измерение размеров текстового блока.

## Настройка текстового виджета

```java
Label label = new Label("UniGUI")
        .font(Fonts.TITLE, 18.0f)
        .textGradient(Color.WHITE, Color.CYAN, 90.0f);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`font(FontFace, float)` {: #fontfontface-float data-toc-label="font(FontFace, float)" }

**Задать шрифт и размер**

Устанавливает гарнитуру шрифта и его размер в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `font` | `FontFace` | — |
| `pixelSize` | `float` | — |

Возвращает `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

**Содержимое [RichText](<../../core/text/rich-text.md>)**

Возвращает или устанавливает стилизованную модель [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `richText` | `RichText` | — |

Возвращает `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

**Текстовое содержимое**

Возвращает или устанавливает строковое содержимое текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | — |

Возвращает `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`textBrush(TextBrush)` {: #textbrushtextbrush data-toc-label="textBrush(TextBrush)" }

**Задать текстовую кисть**

Применяет пользовательскую кисть TextBrush для эффектов отрисовки текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `brush` | `TextBrush` | — |

Возвращает `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`textGradient(ColorView, ColorView, float)` {: #textgradientcolorview-colorview-float data-toc-label="textGradient(ColorView, ColorView, float)" }

**Задать градиент текста**

Применяет двухцветный линейный градиент к отрисовке символов текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `startColor` | `ColorView` | — |
| `endColor` | `ColorView` | — |
| `angleDegrees` | `float` | — |

Возвращает `TextWidget`.

</div>

