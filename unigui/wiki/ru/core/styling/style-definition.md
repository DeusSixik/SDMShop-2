---
title: "StyleDefinition"
slug: "style-definition"
category: "Core/Styling"
locale: "ru"
order: 30
---

# StyleDefinition

Неизменяемое именованное правило с селекторами, renderer-ом, свойствами и анимациями событий.

## Обзор

`StyleDefinition` — одно переиспользуемое правило внутри `StylePack`. У него есть стабильный id, селектор, декларативные свойства или зарегистрированный renderer, а также необязательные связи событий с анимациями. Методы-модификаторы возвращают новые определения — сохраните результат или снова поместите его в pack.

## Селекторы

`target` ограничивает правило типом стиля виджета, `styleClass` сопоставляет классы из `addStyleClass`, а `widgetId` — явный `styleId` виджета. Условия можно комбинировать. Селектор `widgetId` не сопоставляет обычный widget `id`.

## Правило для основной кнопки

```java
StyleDefinition primary = StyleDefinition.of(
        "button.primary",
        new MutableStyle().put(StyleKeys.BACKGROUND_COLOR, accent))
        .target(Button.STYLE_TYPE)
        .styleClass("primary");
```

## API

### Создание

| Метод | Описание |
| --- | --- |
| [`of(String, Style)`](#ofstring-style) | Создаёт декларативное правило стиля. |
| [`custom(String, String, Style)`](#customstring-string-style) | Создаёт правило на основе зарегистрированного пользовательского renderer-а. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`of(String, Style)` {: #ofstring-style data-toc-label="of(String, Style)" }

**of(String, [Style](<style.md>))**

Создаёт декларативное правило стиля.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Стабильный непустой id внутри style pack. |
| `style` | `Style` | Типизированные свойства стиля; null превращается в [Style](<style.md>).EMPTY. |

Возвращает `StyleDefinition`: Новое неизменяемое определение стиля.

Исключения:

- `IllegalArgumentException`: Id определения равен null или пуст.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`custom(String, String, Style)` {: #customstring-string-style data-toc-label="custom(String, String, Style)" }

**custom(String, String, [Style](<style.md>))**

Создаёт правило на основе зарегистрированного пользовательского renderer-а.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Стабильный непустой id внутри style pack. |
| `rendererId` | `String` | Стабильный id renderer-а из WidgetRenderRegistry. |
| `style` | `Style` | Типизированные свойства стиля; null превращается в [Style](<style.md>).EMPTY. |

Возвращает `StyleDefinition`: Новое неизменяемое определение стиля.

Исключения:

- `IllegalArgumentException`: Id определения равен null или пуст.

</div>

### Селекторы

| Метод | Описание |
| --- | --- |
| [`target(String)`](#targetstring) | Ограничивает правило типом стиля виджета. |
| [`styleClass(String)`](#styleclassstring) | Ограничивает правило style-классом виджета. |
| [`widgetId(String)`](#widgetidstring) | Ограничивает правило явным style id виджета. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`target(String)` {: #targetstring data-toc-label="target(String)" }

**target(String)**

Ограничивает правило типом стиля виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `target` | `String` | Тип стиля виджета, обычно константа STYLE_TYPE. |

Возвращает `StyleDefinition`: Новое неизменяемое определение с запрошенным изменением.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`styleClass(String)` {: #styleclassstring data-toc-label="styleClass(String)" }

**styleClass(String)**

Ограничивает правило style-классом виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClass` | `String` | Класс из addStyleClass или XML-атрибута class. |

Возвращает `StyleDefinition`: Новое неизменяемое определение с запрошенным изменением.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`widgetId(String)` {: #widgetidstring data-toc-label="widgetId(String)" }

**widgetId(String)**

Ограничивает правило явным style id виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widgetId` | `String` | Значение из styleId или XML-атрибута style. |

Возвращает `StyleDefinition`: Новое неизменяемое определение с запрошенным изменением.

</div>

### Анимации событий

| Метод | Описание |
| --- | --- |
| [`eventAnimation(String, String)`](#eventanimationstring-string) | Добавляет, заменяет или удаляет связь события с анимацией. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`eventAnimation(String, String)` {: #eventanimationstring-string data-toc-label="eventAnimation(String, String)" }

**eventAnimation(String, String)**

Добавляет, заменяет или удаляет связь события с анимацией.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `eventName` | `String` | Стабильный id события, желательно из StyleAnimationIds.Event. |
| `animationId` | `String` | Id preset-а анимации; пустое значение удаляет связь. |

Возвращает `StyleDefinition`: Новое неизменяемое определение с запрошенным изменением.

</div>

