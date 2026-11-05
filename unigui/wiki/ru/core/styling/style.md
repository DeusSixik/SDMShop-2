---
title: "Style"
slug: "style"
category: "Core/Styling"
locale: "ru"
order: 10
---

# Style

Неизменяемые типизированные значения стиля с override-ами состояний и явным fallback.

## Обзор

`Style` хранит значения по типизированным `StyleKey` и `WidgetState`. Запрос состояния может откатиться к `NORMAL`, а затем к fallback, переданному вызывающим кодом. Используйте `MutableStyle` для сборки в Java, `Style#fromText` для inline-объявлений или `Style#from` для одного XML-элемента `<Style>`.

## Слои стилей

`Style` — один слой, а не весь каскад темы. `StylePack` объединяет токены темы, fallback-значения, привязки типов виджетов и подходящие определения. Локальный стиль виджета применяется после слоёв темы. Используйте `StylePack#from`, если XML содержит селекторы, именованные определения, анимации событий или renderer-привязки.

## Парсинг и чтение inline-стиля

```java
Style style = Style.fromText("background: #20242a; radius: 6;");
ColorView background = style.get(
        StyleKeys.BACKGROUND_COLOR,
        WidgetState.NORMAL,
        fallbackColor);
```

## API

### Создание и парсинг

| Метод | Описание |
| --- | --- |
| [`from(String)`](#fromstring) | Парсит один XML-элемент `<Style>` со встроенными свойствами. |
| [`from(String, StyleKeyRegistry)`](#fromstring-stylekeyregistry) | Парсит один XML-элемент `<Style>` с пользовательским реестром свойств. |
| [`fromText(String)`](#fromtextstring) | Парсит разделённые точкой с запятой inline-объявления и пропускает ошибочные записи. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`from(String)` {: #fromstring data-toc-label="from(String)" }

**from(String)**

Парсит один XML-элемент `<Style>` со встроенными свойствами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `xml` | `String` | XML с корневым элементом `<Style>`. |

Возвращает `Style`: Разобранный неизменяемый стиль.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`from(String, StyleKeyRegistry)` {: #fromstring-stylekeyregistry data-toc-label="from(String, StyleKeyRegistry)" }

**from(String, StyleKeyRegistry)**

Парсит один XML-элемент `<Style>` с пользовательским реестром свойств.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `xml` | `String` | XML с корневым элементом `<Style>`. |
| `registry` | `StyleKeyRegistry` | Реестр со встроенными и пользовательскими style-свойствами. |

Возвращает `Style`: Разобранный неизменяемый стиль.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`fromText(String)` {: #fromtextstring data-toc-label="fromText(String)" }

**fromText(String)**

Парсит разделённые точкой с запятой inline-объявления и пропускает ошибочные записи.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Inline-объявления вида background: #20242a; radius: 6;. |

Возвращает `Style`: Разобранный изменяемый стиль; для диагностики используйте StyleTextParser.parseEditor.

</div>

### Чтение значений

| Метод | Описание |
| --- | --- |
| [`get(StyleKey, WidgetState, Object)`](#getstylekey-widgetstate-object) | Читает типизированное значение для состояния виджета с явным fallback. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`get(StyleKey, WidgetState, Object)` {: #getstylekey-widgetstate-object data-toc-label="get(StyleKey, WidgetState, Object)" }

**get(StyleKey, WidgetState, T)**

Читает типизированное значение для состояния виджета с явным fallback.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `key` | `StyleKey` | Типизированный ключ свойства, определяющий ожидаемый Java-тип. |
| `state` | `WidgetState` | Состояние виджета; null трактуется как NORMAL. |
| `fallback` | `Object` | Значение, возвращаемое при отсутствии свойства. |

Возвращает `Object`: Значение состояния, fallback из NORMAL или переданный fallback.

</div>

