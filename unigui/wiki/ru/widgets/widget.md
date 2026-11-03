---
title: "Widget"
slug: "widget"
category: "Widgets"
locale: "ru"
order: 10
---

# Widget

Базовый контракт любого элемента UI-дерева: жизненный цикл, компоновка, отрисовка, ввод и стили.

## Обзор

`Widget` — фундаментальный интерфейс любого элемента иерархического дерева UniGUI. Он объединяет измерение размеров, размещение, визуальные стили, проверку попадания (hit testing), диспетчеризацию событий, кадровые обновления (tick) и процедурную отрисовку в единый согласованный контракт.

Интерфейс расширяет `EventEmitter`, благодаря чему каждый виджет дерева может подписываться на типизированные события и рассылать их.

| Подсистема | Ключевой контракт | Назначение и выполнение |
| --- | --- | --- |
| **Иерархия** | `parent()`, `children()`, `uiContext()` | Связи предков и потомков, координаты и контекст окна. |
| **Компоновка (Layout)** | `measure()`, `arrange()`, `desiredSize()`, `layoutBounds()` | Двухпроходный layout: расчёт желаемого размера, затем фиксация границ. |
| **Визуальные стили** | `style()`, `styleId()`, `styleClasses()` | Инлайновые CSS-декларации, токены классов и темы из [StylePack](<../core/styling/style-pack.md>). |
| **Ввод и фокус** | `handle()`, `mouseCursorAt()`, `focusable()`, `focusScope()` | Обработка событий мыши, курсора и клавиатуры; скоупы фокуса. |
| **Инвалидация** | `invalidate()`, `clearInvalidation()`, `invalidationFlags()` | Отслеживание dirty-флагов геометрии и отрисовки для исключения лишних пересчётов. |
| **Рендер и тик** | `render()`, `tick()`, `dispose()` | Покадровые обновления, запись команд рисования и освобождение ресурсов. |

!!! tip "Наследование [WidgetBase](<../core/widgets/widget-base.md>) вместо реализации Widget"
    В подавляющем большинстве случаев создавайте компоненты расширением `WidgetBase`, а не прямой реализацией `Widget`.     `WidgetBase` содержит готовую инфраструктуру: retained-кэш команд отрисовки, FLIP-анимации, вычисление стилей и всплытие инвалидации.

## Жизненный цикл и конвейер

Каждый кадр среда выполнения UniGUI выполняет строгий конвейер по всей иерархии виджетов:

1. **`measure(LayoutContext)`**: Вычисляет желаемые размеры (`desiredSize()`) с учётом содержимого и ограничений контейнера.
2. **`arrange(RectView)`**: Сохраняет финальные экранные координаты и позиционирует дочерние виджеты.
3. **`handle(Event)`**: Принимает маршрутизируемые события ввода, мыши и клавиатуры.
4. **`tick(FrameContext)`**: Обновляет интерполяции, пружинные анимации и логику времени.
5. **`render(RenderContext)`**: Добавляет команды отрисовки в буфер текущего кадра.
6. **`dispose()`**: Освобождает внутренние ресурсы виджета перед его удалением из дерева.

## Иерархия и структура дерева

Виджеты образуют направленное дерево под управлением методов `parent()` и `children()`:

- **`parent()`**: Возвращает родительский контейнер или `null`, если виджет является корнем или ещё не прикреплён к дереву.
- **`children()`**: Возвращает список детей в порядке обхода.
- **Семантика порядка**: Порядок детей определяет очерёдность отрисовки (от первого к последнему: первый ребёнок снизу, последний сверху) и порядок проверки попадания hit-testing (от последнего к первому: верхний ребёнок первым принимает события мыши).
- **`uiContext()`**: Предоставляет доступ к окну, диспетчеру, реестру шрифтов и сервисам ввода.

## Стили и компоновка

UniGUI строго разделяет геометрию и визуальное оформление:

- **Геометрия**: Управляется через `layoutBounds()` и `desiredSize()`, настраивается в `LayoutStyle`.
- **Визуальные стили**: Задаются через `styleId(...)` (правило темы [StylePack](<../core/styling/style-pack.md>)), `styleClasses(...)` (CSS-классы) или инлайновые объявления через `style("background: #333; radius: 8;")`.
- **Область видимости стилей (Scope)**: Виджет может объявить `styleScope(true)`, изолируя локальные переопределения стилей от внешних деревьев.

## Ввод, фокус и взаимодействие

Состояние взаимодействия управляется явными флагами:

| Флаг состояния | Назначение и эффект |
| --- | --- |
| **`visibility()`** | `VISIBLE` участвует в layout и рендере; `HIDDEN` сохраняет место, но скрывает визуал; `COLLAPSED` полностью исключается из layout. |
| **`enabled()`** | Управляет доступностью для пользователя. Отключённые виджеты игнорируют ввод и получают псевдокласс `:disabled`. |
| **`hovered()`** | Показывает, находится ли курсор мыши над границами виджета. |
| **`mouseCursorAt()`** | Возвращает форму курсора (например, `HAND`, `TEXT`) при наведении в локальных координатах. |
| **`focusable()`** | Определяет, может ли виджет получать фокус клавиатуры по Tab или клику. |
| **`focusScope()`** | Замыкает цикл переключения фокуса внутри поддерева (необходимо для диалогов и модальных окон). |
| **`focusOrder()`** | Явный приоритетный индекс последовательности Tab. |

## Dirty-состояние и инвалидация

Для достижения максимальной частоты кадров UniGUI пропускает пересчёт компоновки и генерацию команд рендера для чистых поддеревьев:

- **`invalidate(flags)`**: Помечает флаги из `InvalidationFlags` (`LAYOUT`, `VISUAL`, `RETAINED_RENDER`). Флаги геометрии всплывают вверх к корню.
- **`clearInvalidation(flags)`**: Снимает обработанные флаги после завершения прохода конвейера.
- **`subtreeInvalidationFlags()`**: Возвращает совокупную битовую маску dirty-флагов для виджета и всех его потомков.

## Подписка на события виджета

```java
Widget button = new Button("Отправить");
button.on(ButtonClickEvent.TYPE, event -> {
    System.out.println("Нажат виджет: " + event.target().id());
});
```

## Создание пользовательского виджета

```java
public class CustomCard extends WidgetBase {
    @Override
    public void measure(LayoutContext context) {
        setDesiredSize(200.0f, 100.0f);
    }

    @Override
    public void render(RenderContext context) {
        context.fillRect(layoutBounds(), 0xFF2A2A2A);
    }
}
```

## Связанные ссылки

- [EventEmitter](<../core/events/event-emitter.md>)
- [LayoutStyle](<../core/layout/layout-style.md>)
- [WidgetBase](<../core/widgets/widget-base.md>)

## API

### Идентификация и стили

| Метод | Описание |
| --- | --- |
| [`id(String)`](#idstring) | Назначает строковый id для поиска в дереве и отладки. |
| [`id()`](#id) | Возвращает идентификатор виджета или пустую строку. |
| [`styleId(String)`](#styleidstring) | Задаёт идентификатор стиля из [StylePack](<../core/styling/style-pack.md>) для резолва темы. |
| [`styleId()`](#styleid) | Возвращает токен стиля темы или пустую строку. |
| [`styleClass(String)`](#styleclassstring) | Задаёт классы стиля строкой через пробелы или запятые. |
| [`styleClasses()`](#styleclasses) | Возвращает список назначенных классов стиля. |
| [`addStyleClass(String)`](#addstyleclassstring) | Добавляет один класс стиля к виджету. |
| [`removeStyleClass(String)`](#removestyleclassstring) | Удаляет один класс стиля у виджета. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`id(String)` {: #idstring data-toc-label="id(String)" }

**Задать идентификатор**

Назначает строковый id для поиска в дереве и отладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | строковый идентификатор виджета |

Возвращает `Widget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`id()` {: #id data-toc-label="id()" }

**Получить идентификатор**

Возвращает идентификатор виджета или пустую строку.

Возвращает `String`: строка идентификатора.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleId(String)` {: #styleidstring data-toc-label="styleId(String)" }

**Задать [Style](<../core/styling/style.md>) ID**

Задаёт идентификатор стиля из [StylePack](<../core/styling/style-pack.md>) для резолва темы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleId` | `String` | токен стиля темы |

Возвращает `Widget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleId()` {: #styleid data-toc-label="styleId()" }

**Получить [Style](<../core/styling/style.md>) ID**

Возвращает токен стиля темы или пустую строку.

Возвращает `String`: строка id стиля.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleClass(String)` {: #styleclassstring data-toc-label="styleClass(String)" }

**Задать классы стиля**

Задаёт классы стиля строкой через пробелы или запятые.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClasses` | `String` | строка классов стиля |

Возвращает `Widget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleClasses()` {: #styleclasses data-toc-label="styleClasses()" }

**Получить классы стиля**

Возвращает список назначенных классов стиля.

Возвращает `List`: список токенов классов стиля.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`addStyleClass(String)` {: #addstyleclassstring data-toc-label="addStyleClass(String)" }

**Добавить класс стиля**

Добавляет один класс стиля к виджету.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClass` | `String` | добавляемый класс стиля |

Возвращает `Widget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`removeStyleClass(String)` {: #removestyleclassstring data-toc-label="removeStyleClass(String)" }

**Удалить класс стиля**

Удаляет один класс стиля у виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClass` | `String` | удаляемый класс стиля |

Возвращает `Widget`: этот виджет для fluent-настройки.

</div>

### Макет и геометрия

| Метод | Описание |
| --- | --- |
| [`layoutBounds()`](#layoutbounds) | Возвращает финальный прямоугольник виджета после раскладки. |
| [`desiredSize()`](#desiredsize) | Возвращает размер, рассчитанный на последнем проходе measure. |
| [`measure(LayoutContext)`](#measurelayoutcontext) | Измеряет виджет и рассчитывает желаемый размер для контекста. |
| [`arrange(RectView)`](#arrangerectview) | Размещает виджет в границах итогового прямоугольника. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`layoutBounds()` {: #layoutbounds data-toc-label="layoutBounds()" }

**Границы макета**

Возвращает финальный прямоугольник виджета после раскладки.

Возвращает `RectView`: текущие границы размещения виджета.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`desiredSize()` {: #desiredsize data-toc-label="desiredSize()" }

**Желаемый размер**

Возвращает размер, рассчитанный на последнем проходе measure.

Возвращает `LayoutSize`: последний измеренный желаемый размер.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`measure(LayoutContext)` {: #measurelayoutcontext data-toc-label="measure(LayoutContext)" }

**Измерение**

Измеряет виджет и рассчитывает желаемый размер для контекста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `LayoutContext` | ограничения и контекст измерения |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`arrange(RectView)` {: #arrangerectview data-toc-label="arrange(RectView)" }

**Размещение**

Размещает виджет в границах итогового прямоугольника.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `bounds` | `RectView` | назначенный прямоугольник макета |

</div>

### Состояние и интерактивность

| Метод | Описание |
| --- | --- |
| [`visibility()`](#visibility) | Возвращает текущий режим видимости (VISIBLE, HIDDEN, COLLAPSED). |
| [`visible()`](#visible) | Возвращает true только при режиме VISIBLE. |
| [`enabled()`](#enabled) | Возвращает true, если виджет принимает взаимодействие. |
| [`hovered()`](#hovered) | Возвращает true, если указатель мыши находится над виджетом. |
| [`focusable()`](#focusable) | Возвращает true, если виджет может получать клавиатурный фокус. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`visibility()` {: #visibility data-toc-label="visibility()" }

**Режим видимости**

Возвращает текущий режим видимости (VISIBLE, HIDDEN, COLLAPSED).

Возвращает `Visibility`: текущий режим видимости.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`visible()` {: #visible data-toc-label="visible()" }

**Видим ли виджет**

Возвращает true только при режиме VISIBLE.

Возвращает `boolean`: true, если виджет в режиме VISIBLE.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

**Доступен ли виджет**

Возвращает true, если виджет принимает взаимодействие.

Возвращает `boolean`: true, если виджет включён.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`hovered()` {: #hovered data-toc-label="hovered()" }

**Наведён ли курсор**

Возвращает true, если указатель мыши находится над виджетом.

Возвращает `boolean`: true, если курсор над виджетом.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`focusable()` {: #focusable data-toc-label="focusable()" }

**Фокусируемость**

Возвращает true, если виджет может получать клавиатурный фокус.

Возвращает `boolean`: true, если виджет фокусируемый.

</div>

### Отрисовка и кэширование

| Метод | Описание |
| --- | --- |
| [`render(RenderContext)`](#renderrendercontext) | Отрисовывает виджет в текущем контексте кадра. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`render(RenderContext)` {: #renderrendercontext data-toc-label="render(RenderContext)" }

**Отрисовка**

Отрисовывает виджет в текущем контексте кадра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | контекст отрисовки кадра |

</div>

### События и жизненный цикл

| Метод | Описание |
| --- | --- |
| [`uiContext()`](#uicontext) | Возвращает runtime-контекст с сервисами dispatcher, theme и input. |
| [`parent()`](#parent) | Возвращает родительский виджет в дереве UI. |
| [`children()`](#children) | Возвращает дочерние элементы в порядке обхода/раскладки. |
| [`invalidationFlags()`](#invalidationflags) | Возвращает битовую маску текущих флагов инвалидации. |
| [`invalidate(int)`](#invalidateint) | Выставляет флаги инвалидации (VISUAL, LAYOUT, HIERARCHY и др.). |
| [`clearInvalidation(int)`](#clearinvalidationint) | Снимает обработанные флаги инвалидации. |
| [`handle(Event)`](#handleevent) | Обрабатывает маршрутизированное или прямое событие UI. |
| [`tick(FrameContext)`](#tickframecontext) | Обновляет покадровое состояние, таймеры и анимации. |
| [`dispose()`](#dispose) | Освобождает собственные ресурсы виджета перед удалением из дерева. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`uiContext()` {: #uicontext data-toc-label="uiContext()" }

**UI Context**

Возвращает runtime-контекст с сервисами dispatcher, theme и input.

Возвращает `UIContext`: активный UIContext или null, если виджет отсоединён.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`parent()` {: #parent data-toc-label="parent()" }

**Родительский виджет**

Возвращает родительский виджет в дереве UI.

Возвращает `Widget`: родительский виджет или null для корня.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`children()` {: #children data-toc-label="children()" }

**Дочерние виджеты**

Возвращает дочерние элементы в порядке обхода/раскладки.

Возвращает `List`: список дочерних элементов.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`invalidationFlags()` {: #invalidationflags data-toc-label="invalidationFlags()" }

**Флаги инвалидации**

Возвращает битовую маску текущих флагов инвалидации.

Возвращает `int`: битовая маска dirty-флагов.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`invalidate(int)` {: #invalidateint data-toc-label="invalidate(int)" }

**Инвалидировать**

Выставляет флаги инвалидации (VISUAL, LAYOUT, HIERARCHY и др.).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flags` | `int` | битовая маска из InvalidationFlags |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`clearInvalidation(int)` {: #clearinvalidationint data-toc-label="clearInvalidation(int)" }

**Снять флаги инвалидации**

Снимает обработанные флаги инвалидации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flags` | `int` | битовая маска обработанных флагов |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`handle(Event)` {: #handleevent data-toc-label="handle(Event)" }

**Обработка события**

Обрабатывает маршрутизированное или прямое событие UI.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `event` | `Event` | объект события |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`tick(FrameContext)` {: #tickframecontext data-toc-label="tick(FrameContext)" }

**Тик кадра**

Обновляет покадровое состояние, таймеры и анимации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `frame` | `FrameContext` | контекст текущего кадра |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`dispose()` {: #dispose data-toc-label="dispose()" }

**Освобождение ресурсов**

Освобождает собственные ресурсы виджета перед удалением из дерева.

</div>

