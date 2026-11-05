---
title: "MutableStyle"
slug: "mutable-style"
category: "Core/Styling"
locale: "ru"
order: 20
---

# MutableStyle

Fluent-сборщик изменяемого стиля с типизированными свойствами и override-ами состояний.

## Обзор

`MutableStyle` проверяет каждое ненулевое значение по типу его `StyleKey` и ведёт версию для инвалидации кэшей. Значения без состояния относятся к `NORMAL`; перегрузка с состоянием создаёт override для hover, press, selection, disabled и других состояний. Карты из `values()` — неизменяемые снимки.

## Цвета для разных состояний

```java
Style buttonStyle = new MutableStyle()
        .put(StyleKeys.BACKGROUND_COLOR, normalColor)
        .put(StyleKeys.BACKGROUND_COLOR, WidgetState.HOVERED, hoverColor)
        .put(StyleKeys.TEXT_COLOR, textColor);
```

## API

### Свойства

| Метод | Описание |
| --- | --- |
| [`put(StyleKey, Object)`](#putstylekey-object) | Сохраняет типизированное свойство для состояния NORMAL. |
| [`put(StyleKey, WidgetState, Object)`](#putstylekey-widgetstate-object) | Сохраняет типизированный override свойства для одного состояния. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`put(StyleKey, Object)` {: #putstylekey-object data-toc-label="put(StyleKey, Object)" }

**put(StyleKey, T)**

Сохраняет типизированное свойство для состояния NORMAL.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `key` | `StyleKey` | Типизированный ключ стиля. |
| `value` | `Object` | Значение свойства или null; ненулевое значение должно соответствовать типу ключа. |

Возвращает `MutableStyle`: Тот же изменяемый стиль для fluent-конфигурации.

Исключения:

- `IllegalArgumentException`: Значение не соответствует Java-типу, объявленному ключом.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`put(StyleKey, WidgetState, Object)` {: #putstylekey-widgetstate-object data-toc-label="put(StyleKey, WidgetState, Object)" }

**put(StyleKey, WidgetState, T)**

Сохраняет типизированный override свойства для одного состояния.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `key` | `StyleKey` | Типизированный ключ стиля. |
| `state` | `WidgetState` | Состояние, которому принадлежит override; не может быть null. |
| `value` | `Object` | Значение свойства или null; ненулевое значение должно соответствовать типу ключа. |

Возвращает `MutableStyle`: Тот же изменяемый стиль для fluent-конфигурации.

Исключения:

- `IllegalArgumentException`: Значение не соответствует Java-типу, объявленному ключом.

</div>

### Композиция

| Метод | Описание |
| --- | --- |
| [`putAll(Style)`](#putallstyle) | Копирует значения NORMAL и отдельных состояний из другого стиля. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`putAll(Style)` {: #putallstyle data-toc-label="putAll(Style)" }

**putAll([Style](<style.md>))**

Копирует значения NORMAL и отдельных состояний из другого стиля.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `source` | `Style` | Исходный стиль; null и [Style](<style.md>).EMPTY игнорируются. |

Возвращает `MutableStyle`: Тот же изменяемый стиль для fluent-конфигурации.

</div>

### Переопределение renderer-а

| Метод | Описание |
| --- | --- |
| [`rendererId(String)`](#rendereridstring) | Выбирает пользовательский renderer виджета по id из реестра. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`rendererId(String)` {: #rendereridstring data-toc-label="rendererId(String)" }

**rendererId(String)**

Выбирает пользовательский renderer виджета по id из реестра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rendererId` | `String` | Стабильный id из WidgetRenderRegistry; null очищает значение. |

Возвращает `MutableStyle`: Тот же изменяемый стиль для fluent-конфигурации.

</div>

