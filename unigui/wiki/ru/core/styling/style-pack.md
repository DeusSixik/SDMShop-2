---
title: "StylePack"
slug: "style-pack"
category: "Core/Styling"
locale: "ru"
order: 40
---

# StylePack

Редактируемая тема с переиспользуемыми правилами, привязками виджетов, токенами и анимациями.

## Обзор

`StylePack` одновременно является `Theme` и редактируемой моделью таблицы стилей. Соберите его в Java или загрузите полный XML `<StylePack>`, затем установите в UI-контекст. Именованные определения выбираются по типу виджета, style-классу, явному style id или прямой привязке типа виджета.

## Порядок разрешения

Сначала применяются токены темы, затем fallback pack-а, привязанное базовое определение и подходящие селекторы или явные style-правила. Подходящие определения накладываются в порядке добавления: более позднее правило перекрывает раннее независимо от специфичности селектора. Возвращаемые карты и списки являются снимками.

## Сборка style pack в Java

```java
StylePack pack = StylePack.create("app")
        .tokens(ThemeTokens.defaults())
        .put(StyleDefinition.of("button.primary", primaryStyle)
                .target(Button.STYLE_TYPE)
                .styleClass("primary"));
context.theme(pack);
```

## Загрузка style pack из XML

```xml
<StylePack id="app">
  <Style id="button.primary" target="button" class="primary">
    <Setter property="background" value="#365df5"/>
    <State name="hovered">
      <Setter property="background" value="#4b70ff"/>
    </State>
  </Style>
</StylePack>
```

## API

### Создание и парсинг

| Метод | Описание |
| --- | --- |
| [`create(String)`](#createstring) | Создаёт пустой style pack с нормализованным id. |
| [`from(String)`](#fromstring) | Парсит полный XML `<StylePack>` со встроенными свойствами. |
| [`copy()`](#copy) | Создаёт независимую редактируемую копию со всеми правилами и переопределениями состояний. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`create(String)` {: #createstring data-toc-label="create(String)" }

**create(String)**

Создаёт пустой style pack с нормализованным id.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Id pack-а; null или пустое значение превращается в style-pack. |

Возвращает `StylePack`: Новый изменяемый style pack.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`from(String)` {: #fromstring data-toc-label="from(String)" }

**from(String)**

Парсит полный XML `<StylePack>` со встроенными свойствами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `xml` | `String` | Полный XML-документ StylePack. |

Возвращает `StylePack`: Разобранный изменяемый style pack.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`copy()` {: #copy data-toc-label="copy()" }

**Копировать style pack**

Создаёт независимую редактируемую копию со всеми правилами и переопределениями состояний.

Возвращает `StylePack`: Независимая изменяемая копия этого style pack.

</div>

### Правила стилей

| Метод | Описание |
| --- | --- |
| [`put(StyleDefinition)`](#putstyledefinition) | Добавляет или заменяет одно именованное определение стиля. |
| [`bind(String, String)`](#bindstring-string) | Привязывает тип стиля виджета к id базового определения. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`put(StyleDefinition)` {: #putstyledefinition data-toc-label="put(StyleDefinition)" }

**put([StyleDefinition](<style-definition.md>))**

Добавляет или заменяет одно именованное определение стиля.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `definition` | `StyleDefinition` | Неизменяемое определение, сохраняемое по id. |

Возвращает `StylePack`: Тот же style pack для fluent-конфигурации.

Исключения:

- `NullPointerException`: Определение равно null.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`bind(String, String)` {: #bindstring-string data-toc-label="bind(String, String)" }

**bind(String, String)**

Привязывает тип стиля виджета к id базового определения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widgetType` | `String` | Тип стиля виджета; wildcard обновляет id fallback-стиля. |
| `styleId` | `String` | Id определения; пустое значение удаляет привязку. |

Возвращает `StylePack`: Тот же style pack для fluent-конфигурации.

</div>

### Preset-ы анимаций

| Метод | Описание |
| --- | --- |
| [`putAnimation(StyleAnimationDefinition)`](#putanimationstyleanimationdefinition) | Добавляет или заменяет один именованный preset анимации. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`putAnimation(StyleAnimationDefinition)` {: #putanimationstyleanimationdefinition data-toc-label="putAnimation(StyleAnimationDefinition)" }

**putAnimation(StyleAnimationDefinition)**

Добавляет или заменяет один именованный preset анимации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `animation` | `StyleAnimationDefinition` | Определение анимации, сохраняемое по id. |

Возвращает `StylePack`: Тот же style pack для fluent-конфигурации.

Исключения:

- `NullPointerException`: Определение анимации равно null.

</div>

### Разрешение

| Метод | Описание |
| --- | --- |
| [`resolveStyleFor(String, String, Collection)`](#resolvestyleforstring-string-collection) | Разрешает все активные слои стилей для одного виджета. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`resolveStyleFor(String, String, Collection)` {: #resolvestyleforstring-string-collection data-toc-label="resolveStyleFor(String, String, Collection)" }

**resolveStyleFor(String, String, Collection)**

Разрешает все активные слои стилей для одного виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widgetType` | `String` | Тип стиля виджета. |
| `widgetStyleId` | `String` | Явный style id виджета или пустая строка. |
| `styleClasses` | `Collection` | [Style](<style.md>)-классы виджета; null трактуется как пустая коллекция. |

Возвращает `Style`: Неизменяемый итоговый стиль от низшего к высшему приоритету.

</div>

