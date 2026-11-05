---
title: "ThemeTokens"
slug: "theme-tokens"
category: "Core/Styling"
locale: "ru"
order: 50
---

# ThemeTokens

Неизменяемый снимок design-токенов палитры, форм, типографики и отступов.

## Обзор

`ThemeTokens` объединяет переиспользуемые цвета, формы, типографику и отступы в один неизменяемый снимок. Настройте одну или несколько групп через вложенный builder, затем подключите снимок к `MutableTheme` или `StylePack`. Готовый снимок можно безопасно разделять между UI-деревьями.

## Приоритет каскада

Токены являются активным слоем с самым низким приоритетом. Fallback pack-а, подходящие определения и локальные стили виджетов перекрывают их. `ThemeTokens.EMPTY` неактивен и ничего не добавляет, а `defaults()` создаёт активный нейтральный тёмный набор. `asStyle()` предоставляет только ключи, сопоставленные системой токенов.

## Создание пользовательских токенов

```java
ThemeTokens tokens = ThemeTokens.builder()
        .colors(colors -> colors.accent(accentColor))
        .shapes(shapes -> shapes.radiusMd(6.0f))
        .spacing(spacing -> spacing.controlGap(6.0f))
        .build();
StylePack pack = StylePack.create("app").tokens(tokens);
```

## API

### Создание

| Метод | Описание |
| --- | --- |
| [`builder()`](#builder) | Создаёт builder активного неизменяемого снимка токенов. |
| [`defaults()`](#defaults) | Создаёт активный стандартный нейтральный тёмный набор токенов. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ThemeTokens::</span>`builder()` {: #builder data-toc-label="builder()" }

**builder()**

Создаёт builder активного неизменяемого снимка токенов.

Возвращает `Builder`: Builder токенов со стандартными группами цветов, форм, типографики и отступов.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ThemeTokens::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**defaults()**

Создаёт активный стандартный нейтральный тёмный набор токенов.

Возвращает `ThemeTokens`: Новый активный снимок стандартных токенов.

</div>

### Интеграция со стилями

| Метод | Описание |
| --- | --- |
| [`asStyle()`](#asstyle) | Предоставляет сопоставленные токены как неизменяемый слой стиля. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ThemeTokens::</span>`asStyle()` {: #asstyle data-toc-label="asStyle()" }

**asStyle()**

Предоставляет сопоставленные токены как неизменяемый слой стиля.

Возвращает `Style`: [Style](<style.md>) на основе токенов или [Style](<style.md>).EMPTY для неактивного снимка EMPTY.

</div>

