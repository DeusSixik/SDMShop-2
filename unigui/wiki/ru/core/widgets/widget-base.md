---
title: "WidgetBase"
slug: "widget-base"
category: "Core/Виджеты"
locale: "ru"
order: 30
---

# WidgetBase

Общая основа виджетов для layout, [StylePack](<../styling/style-pack.md>), состояния, рендера, событий и анимаций.

## Обзор

`WidgetBase` — базовый абстрактный класс для всех виджетов UniGUI. Он объединяет границы макета, визуальные стили, обработку ввода, отслеживание инвалидации, навигацию фокуса, трансформации, анимации и отрисовку в единый fluent-API.

| Основная подсистема | Ключевые методы | Назначение и архитектура |
| --- | --- | --- |
| **Иерархия** | `parent()`, `children()`, `uiContext()` | Управляет отношениями дерева предков и потомков, передачей инвалидации и доступом к контексту окна. |
| **Геометрия и макет** | `layout()`, `layoutStyle()`, `desiredSize()` | Измеряет и размещает виджет через `LayoutStyle` (flexbox, CSS Grid, отступы margin/padding, ограничения размеров). |
| **Визуальные стили** | `style()`, `styleId()`, `styleClass()` | Обрабатывает инлайновые CSS-декларации, поиск правил в [StylePack](<../styling/style-pack.md>), классы-селекторы и изоляцию скоупов. |
| **Состояние и ввод** | `visibility()`, `enabled()`, `mouseCursor()` | Управляет участием в макете, проверкой наведения hover, курсором мыши и клавиатурным фокусом. |
| **Трансформации и анимации**| `animate()`, `opacity()`, `rotationDegrees()` | Интерполирует свойства, выполняет FLIP-переходы макета, пружинную физику и раскадровки Storyboard. |
| **Рендеринг и кэш** | `renderer()`, `renderCached()`, `invalidate()` | Формирует команды отрисовки, поддерживает retained-кэш команд для листовых виджетов и управляет dirty-флагами. |
| **События** | `on()`, `emit()`, `handle()` | Строго типизированная подписка на события и всплытие по иерархии виджетов. |

!!! note "Абстрактный базовый класс"
    `WidgetBase` предоставляет инфраструктуру и является абстрактным. Используйте готовые виджеты (`Label`, `Button`, `VBox`, `ScrollView` и др.) либо наследуйте его для создания собственных компонентов.

## Стили и layout

UniGUI строго разделяет **Layout** (геометрию, позиционирование и размеры) и **[Style](<../styling/style.md>)** (цвета, рамки, тени, текстуры).

| Механизм стилизации | Метод / Синтаксис | Описание и поведение |
| --- | --- | --- |
| **Инлайновые CSS-декларации**| `style("color: #fff; padding: 8px;")` | Парсит пары свойство-значение в инлайновый стиль. Предупреждения парсера доступны в `styleDiagnostics()`. |
| **Ссылка на [StylePack](<../styling/style-pack.md>)** | `styleId("card-primary")` | Применяет готовое правило темы из активного набора `StylePack`. |
| **Классы стилей** | `styleClass("btn primary")` / `addStyleClass("active")` | Назначает классы для условных селекторов (`:hover`, `:focused`, `:disabled`). |
| **Изоляция стилей (Scope)** | `styleScope(true)` | Изолирует поддерево виджета, предотвращая наследование локальных стилей за пределы этой границы. |
| **Локальные переопределения**| `localStyle("Button", buttonStyle)` | Переопределяет стиль по умолчанию для всех дочерних виджетов указанного типа в данном поддереве. |
| **Конфигурация макета** | `layout(style -> style.width(100).fill())` | Настраивает геометрию через батчинг `LayoutStyle`, исключая лишние пересчёты. |

!!! tip "Разделение геометрии и визуала"
    Изменение визуальных стилей (например, `color` или `opacity`) вызывает только перерисовку (**VISUAL** invalidation). Изменение геометрии через `layout(...)` запускает пересчёт макета (**LAYOUT** invalidation). Их разделение обеспечивает стабильные 60+ FPS.

## Видимость, фокус и взаимодействие

Видимость виджета, доступность взаимодействия и навигация фокуса управляются явными флагами состояния.

| Режим видимости | Участие в Layout | Отрисовка | События мыши и ввода | Фокус с клавиатуры |
| --- | --- | --- | --- | --- |
| `Visibility.VISIBLE` | Измеряется и размещается | Рисуется | Принимает события | Да |
| `Visibility.HIDDEN` | Измеряется (сохраняет место) | Скрыт | Игнорирует | Нет |
| `Visibility.COLLAPSED` | Полностью исключён (размер 0) | Скрыт | Игнорирует | Нет |

| Возможность взаимодействия | Метод | Назначение и эффект |
| --- | --- | --- |
| **Доступность (Enabled)** | `enabled(boolean)` | Включает/выключает интерактивность; неактивный виджет получает псевдокласс `:disabled` и игнорирует события. |
| **Проверка наведения** | `hovered()` | Возвращает true, если курсор мыши в данный момент находится над виджетом. |
| **Курсор мыши** | `mouseCursor(MouseCursor)` | Задаёт форму курсора (`HAND`, `TEXT`, `CROSSHAIR` и др.) при наведении на включённый видимый виджет. |
| **Фокусируемость** | `focusable(boolean)` | Разрешает виджету участвовать в таб-навигации и получать клавиатурный фокус. |
| **Граница фокуса (Scope)** | `focusScope(true)` | Замыкает переключение Tab внутри поддерева виджета (необходимо для модальных окон и диалогов). |
| **Порядок табуляции** | `focusOrder(int)` | Явный относительный индекс последовательности клавиши Tab. |

!!! warning "Выбор между `HIDDEN` и `COLLAPSED`"
    Используйте `Visibility.HIDDEN`, когда элемент должен временно исчезнуть без смещения соседних виджетов. Используйте `Visibility.COLLAPSED`, когда элемент должен полностью освободить своё пространство.

## Трансформации и анимации

UniGUI включает единый движок анимаций, поддерживающий прямые трансформации, интерполяцию, физику пружин, перемещение макета и сложные раскадровки.

| Техника анимации | Метод | Описание |
| --- | --- | --- |
| **Прямая трансформация** | `opacity(val)`, `rotationDegrees(deg)`, `transformPivot(x, y)` | Мгновенно обновляет визуальные параметры без затрат на анимацию. |
| **Интерполированный переход**| `animate(property, target, spec)`, `animateOpacity(...)`, `animatePosition(...)`, `animateScale(...)` | Плавно анимирует свойство по кривой плавности (`TransitionSpec`). Активные анимации плавно перенаправляются к новой цели. |
| **Физика пружины (Spring)** | `animateSpring(property, target, stiffness, damping)` | Физически смоделированное затухающее гармоническое колебание с естественным отскоком. |
| **Движение макета (FLIP)** | `layoutTransition(duration, easing)` | Плавно анимирует смещение координат при пересчёте макета по технологии First-Last-Invert-Play (FLIP). |
| **Обратная связь (Shake)** | `shake(amplitude, duration)` | Покачивает виджет горизонтально/вертикально для индикации ошибок или недопустимых действий. |
| **Сложные раскадровки** | `playStoryboard(storyboard)` | Многодорожечные таймлайны анимации множества параметров под управлением `StoryboardPlayer`. |

!!! note "Переходы макета vs. Анимация размеров"
    `layoutTransition(...)` анимирует позиционное смещение (координаты X и Y), возникающее при пересчёте макета. Он не растягивает и не анимирует ширину/высоту.

## Рендер, invalidation и события

Конвейер рендеринга разделяет расчёт геометрии, запись команд отрисовки и инвалидацию поддеревьев.

| Флаг инвалидации | Подсистема | Когда вызывается |
| --- | --- | --- |
| `InvalidationFlags.LAYOUT` | Движок макета | Изменились размеры, отступы или правила flex. Флаг всплывает к корню дерева. |
| `InvalidationFlags.VISUAL` | Рендеринг | Изменились цвета, текст, границы или прозрачность без изменения размеров. Запускает перерисовку. |
| `InvalidationFlags.RETAINED_RENDER` | Кэш отрисовки | Помечает буфер записанных команд устаревшим. |

| Компонент конвейера | Метод | Назначение и использование |
| --- | --- | --- |
| **Пользовательский рендерер**| `renderer(WidgetRender)` | Заменяет стандартное рисование виджета на процедурный рендерер. |
| **Кэш команд (Retained Cache)**| `renderCachingEnabled(boolean)` | Кэширует команды отрисовки статичных листовых виджетов в буфер, снижая нагрузку на GPU. |
| **Подписка на события** | `on(EventType, listener)` | Регистрирует строго типизированный слушатель событий ввода или жизненного цикла. Возвращает `EventSubscription`. |
| **Отправка событий** | `emit(Event)` | Отправляет событие непосредственно виджету и вызывает зарегистрированных слушателей. |

!!! important "Обход дочерних виджетов при кастомном рендерере"
    Назначение пользовательского `renderer(...)` заменяет отрисовку фона и содержимого самого виджета. Дочерние виджеты контейнера продолжают автоматически обходиться и отрисовываться фреймворком.

## Настройка виджета

```java
Label greeting = new Label("Hello")
        .id("greeting")
        .styleId("card")
        .addStyleClass("interactive");

greeting.layout(style -> style
        .padding(12.0f)
        .width(240.0f)
        .flexNone());
```

## Анимация визуального свойства

```java
greeting
        .opacity(0.0f)
        .animateOpacity(1.0f, 0.20f)
        .animateScale(1.0f, 1.0f, 0.20f);
```

## API

### Идентификация и стили

| Метод | Описание |
| --- | --- |
| [`id(String)`](#idstring) | Назначает уникальный строковый идентификатор рантайма/редактора для поиска и code-behind. |
| [`id()`](#id) | Возвращает строковый идентификатор этого виджета или пустую строку, если он не задан. |
| [`styleId(String)`](#styleidstring) | Выбирает явное именованное правило оформления из активного [StylePack](<../styling/style-pack.md>). |
| [`styleId()`](#styleid) | Возвращает идентификатор стиля [StylePack](<../styling/style-pack.md>), назначенный этому виджету. |
| [`style(String)`](#stylestring) | Применяет inline-объявления или считает голый токен id стиля [StylePack](<../styling/style-pack.md>). |
| [`style(Consumer)`](#styleconsumer) | Применяет типизированные декларации стилей через callback-билдер [MutableStyle](<../styling/mutable-style.md>). |
| [`styleClear()`](#styleclear) | Удаляет все инлайновые декларации стилей и очищает диагностику парсера. |
| [`styleDiagnostics()`](#stylediagnostics) | Возвращает список предупреждений и ошибок, собранных при парсинге инлайновых строк стилей. |
| [`styleClass(String)`](#styleclassstring) | Заменяет классы стилей виджета списком классов, разделённых пробелами. |
| [`addStyleClass(String)`](#addstyleclassstring) | Добавляет один нормализованный класс стиля без удаления уже существующих. |
| [`removeStyleClass(String)`](#removestyleclassstring) | Удаляет указанный класс стиля, если он был назначен виджету. |
| [`hasStyleClass(String)`](#hasstyleclassstring) | Проверяет, назначен ли указанный класс стиля данному виджету. |
| [`styleClasses()`](#styleclasses) | Возвращает неизменяемый список всех классов стилей, назначенных этому виджету. |
| [`styleScope(boolean)`](#stylescopeboolean) | Ограничивает наследование локальных стилей поддеревом этого виджета. |
| [`styleScope()`](#stylescope) | Проверяет, образует ли этот виджет границу локальной области стилей. |
| [`localStyle(String, Style)`](#localstylestring-style) | Назначает или сбрасывает локальное переопределение стиля для указанного типа виджетов в этом поддереве. |
| [`localStyle(String)`](#localstylestring) | Возвращает локальное переопределение стиля для указанного типа виджетов. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`id(String)` {: #idstring data-toc-label="id(String)" }

**Задать ID виджета**

Назначает уникальный строковый идентификатор рантайма/редактора для поиска и code-behind.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`id()` {: #id data-toc-label="id()" }

**Получить ID виджета**

Возвращает строковый идентификатор этого виджета или пустую строку, если он не задан.

Возвращает `String`: строковый идентификатор виджета.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleId(String)` {: #styleidstring data-toc-label="styleId(String)" }

**Задать ID стиля**

Выбирает явное именованное правило оформления из активного [StylePack](<../styling/style-pack.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleId` | `String` | Идентификатор стиля [StylePack](<../styling/style-pack.md>). |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleId()` {: #styleid data-toc-label="styleId()" }

**Получить ID стиля**

Возвращает идентификатор стиля [StylePack](<../styling/style-pack.md>), назначенный этому виджету.

Возвращает `String`: активный идентификатор стиля [StylePack](<../styling/style-pack.md>) или пустая строка.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`style(String)` {: #stylestring data-toc-label="style(String)" }

**Встроенный стиль**

Применяет inline-объявления или считает голый токен id стиля [StylePack](<../styling/style-pack.md>).

Строка без `:` и `;` уходит в `styleId`. Иначе она разбирается как объявления `property: value;` и сливается с inline-стилем. Диагностики парсера остаются в `styleDiagnostics()`.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `declarations` | `String` | Inline-объявления или id [StylePack](<../styling/style-pack.md>), если в строке нет `:` и `;`. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`style(Consumer)` {: #styleconsumer data-toc-label="style(Consumer)" }

**Настроить инлайновый стиль**

Применяет типизированные декларации стилей через callback-билдер [MutableStyle](<../styling/mutable-style.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `update` | `Consumer` | Callback, изменяющий свойства в [MutableStyle](<../styling/mutable-style.md>). |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleClear()` {: #styleclear data-toc-label="styleClear()" }

**Очистить инлайновый стиль**

Удаляет все инлайновые декларации стилей и очищает диагностику парсера.

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleDiagnostics()` {: #stylediagnostics data-toc-label="styleDiagnostics()" }

**Диагностика стилей**

Возвращает список предупреждений и ошибок, собранных при парсинге инлайновых строк стилей.

Возвращает `List`: список диагностических сообщений парсера.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleClass(String)` {: #styleclassstring data-toc-label="styleClass(String)" }

**Задать классы стилей**

Заменяет классы стилей виджета списком классов, разделённых пробелами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClasses` | `String` | Строка с именами классов, разделёнными пробелами. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`addStyleClass(String)` {: #addstyleclassstring data-toc-label="addStyleClass(String)" }

**Добавить класс стиля**

Добавляет один нормализованный класс стиля без удаления уже существующих.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClass` | `String` | Имя добавляемого класса стиля. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`removeStyleClass(String)` {: #removestyleclassstring data-toc-label="removeStyleClass(String)" }

**Удалить класс стиля**

Удаляет указанный класс стиля, если он был назначен виджету.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClass` | `String` | Имя удаляемого класса стиля. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`hasStyleClass(String)` {: #hasstyleclassstring data-toc-label="hasStyleClass(String)" }

**Проверить наличие класса стиля**

Проверяет, назначен ли указанный класс стиля данному виджету.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleClass` | `String` | Имя проверяемого класса стиля. |

Возвращает `boolean`: true, если класс назначен; иначе false.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleClasses()` {: #styleclasses data-toc-label="styleClasses()" }

**Получить классы стилей**

Возвращает неизменяемый список всех классов стилей, назначенных этому виджету.

Возвращает `List`: список классов стилей.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleScope(boolean)` {: #stylescopeboolean data-toc-label="styleScope(boolean)" }

**Задать область стилей (Scope)**

Ограничивает наследование локальных стилей поддеревом этого виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `styleScope` | `boolean` | True для изоляции поиска локальных стилей внутри этого поддерева. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleScope()` {: #stylescope data-toc-label="styleScope()" }

**Проверить область стилей (Scope)**

Проверяет, образует ли этот виджет границу локальной области стилей.

Возвращает `boolean`: true, если изоляция стилей включена.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`localStyle(String, Style)` {: #localstylestring-style data-toc-label="localStyle(String, Style)" }

**Задать локальный стиль**

Назначает или сбрасывает локальное переопределение стиля для указанного типа виджетов в этом поддереве.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widgetType` | `String` | Имя целевого типа виджетов. |
| `style` | `Style` | Переопределение стиля или null для сброса. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`localStyle(String)` {: #localstylestring data-toc-label="localStyle(String)" }

**Получить локальный стиль**

Возвращает локальное переопределение стиля для указанного типа виджетов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widgetType` | `String` | Имя целевого типа виджетов. |

Возвращает `Style`: локальное переопределение стиля или null.

</div>

### Макет и геометрия

| Метод | Описание |
| --- | --- |
| [`layout(Consumer)`](#layoutconsumer) | Обновляет живой `LayoutStyle` и группирует связанную инвалидацию layout. |
| [`layoutStyle()`](#layoutstyle) | Возвращает активный экземпляр [LayoutStyle](<../layout/layout-style.md>), определяющий геометрию виджета. |
| [`layoutBounds()`](#layoutbounds) | Возвращает текущие размещённые координаты и размеры виджета в системе координат родителя. |
| [`desiredSize()`](#desiredsize) | Возвращает размеры, рассчитанные на последнем проходе измерения (measure). |
| [`layoutTransition(float)`](#layouttransitionfloat) | Включает FLIP-анимацию для последующих изменений позиций при пересчёте макета. |
| [`layoutTransition(float, Easing)`](#layouttransitionfloat-easing) | Включает FLIP-анимацию смещения с настраиваемой длительностью и функцией плавности. |
| [`layoutTransitionsEnabled(boolean)`](#layouttransitionsenabledboolean) | Включает или выключает автоматические FLIP-переходы при изменении координат макета. |
| [`layoutTransitionsEnabled()`](#layouttransitionsenabled) | Проверяет, включены ли FLIP-переходы координат макета. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layout(Consumer)` {: #layoutconsumer data-toc-label="layout(Consumer)" }

**Применить layout**

Обновляет живой `LayoutStyle` и группирует связанную инвалидацию layout.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `update` | `Consumer` | Callback, который меняет размер, отступы, flex, grid или позицию. Null ничего не делает. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutStyle()` {: #layoutstyle data-toc-label="layoutStyle()" }

**Получить [LayoutStyle](<../layout/layout-style.md>)**

Возвращает активный экземпляр [LayoutStyle](<../layout/layout-style.md>), определяющий геометрию виджета.

Возвращает `LayoutStyle`: активный экземпляр [LayoutStyle](<../layout/layout-style.md>).

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutBounds()` {: #layoutbounds data-toc-label="layoutBounds()" }

**Получить границы макета**

Возвращает текущие размещённые координаты и размеры виджета в системе координат родителя.

Возвращает `RectView`: текущие границы размещения виджета.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`desiredSize()` {: #desiredsize data-toc-label="desiredSize()" }

**Получить желаемый размер**

Возвращает размеры, рассчитанные на последнем проходе измерения (measure).

Возвращает `LayoutSize`: последний измеренный желаемый размер.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransition(float)` {: #layouttransitionfloat data-toc-label="layoutTransition(float)" }

**Включить переход макета**

Включает FLIP-анимацию для последующих изменений позиций при пересчёте макета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransition(float, Easing)` {: #layouttransitionfloat-easing data-toc-label="layoutTransition(float, Easing)" }

**Включить переход макета с кривой плавности**

Включает FLIP-анимацию смещения с настраиваемой длительностью и функцией плавности.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `durationSeconds` | `float` | Длительность перехода в секундах. |
| `easing` | `Easing` | Кривая плавности перехода. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransitionsEnabled(boolean)` {: #layouttransitionsenabledboolean data-toc-label="layoutTransitionsEnabled(boolean)" }

**Переключить переходы макета**

Включает или выключает автоматические FLIP-переходы при изменении координат макета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | True для включения анимации макета. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransitionsEnabled()` {: #layouttransitionsenabled data-toc-label="layoutTransitionsEnabled()" }

**Переходы макета активны**

Проверяет, включены ли FLIP-переходы координат макета.

Возвращает `boolean`: true, если переходы макета активны.

</div>

### Состояние и интерактивность

| Метод | Описание |
| --- | --- |
| [`visibility(Visibility)`](#visibilityvisibility) | Задаёт, рисуется ли виджет, скрыт на месте или исключён из layout. |
| [`visibility()`](#visibility) | Возвращает текущий режим видимости (VISIBLE, HIDDEN или COLLAPSED). |
| [`visible(boolean)`](#visibleboolean) | Удобный переключатель: устанавливает VISIBLE при true или HIDDEN при false. |
| [`visible()`](#visible) | Проверяет, установлен ли режим видимости строго в VISIBLE. |
| [`enabled(boolean)`](#enabledboolean) | Включает или отключает интерактивность и выставляет CSS-псевдокласс :disabled. |
| [`enabled()`](#enabled) | Проверяет, принимает ли виджет взаимодействие пользователя. |
| [`hovered()`](#hovered) | Проверяет, находится ли курсор мыши в данный момент в границах этого виджета. |
| [`mouseCursor(MouseCursor)`](#mousecursormousecursor) | Устанавливает форму курсора при наведении на включённый видимый виджет. |
| [`mouseCursor()`](#mousecursor) | Возвращает форму курсора, настроенную для этого виджета. |
| [`mouseCursorAt(float, float)`](#mousecursoratfloat-float) | Определяет форму курсора для конкретных локальных координат внутри виджета. |
| [`focusable(boolean)`](#focusableboolean) | Разрешает или запрещает виджету получать клавиатурный фокус и участвовать в Tab-навигации. |
| [`focusable()`](#focusable) | Проверяет, может ли виджет получать клавиатурный фокус. |
| [`focusScope(boolean)`](#focusscopeboolean) | Делает этот виджет границей, замыкающей переключение Tab внутри его детей. |
| [`focusScope()`](#focusscope) | Проверяет, действует ли этот виджет как изолированная область фокуса. |
| [`focusOrder(int)`](#focusorderint) | Устанавливает явный относительный индекс последовательности клавиши Tab. |
| [`focusOrder()`](#focusorder) | Возвращает относительный индекс табуляции, назначенный этому виджету. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visibility(Visibility)` {: #visibilityvisibility data-toc-label="visibility(Visibility)" }

**Видимость**

Задаёт, рисуется ли виджет, скрыт на месте или исключён из layout.

`VISIBLE` участвует в layout, отрисовке, вводе и фокусе. `HIDDEN` сохраняет место, но пропускает отрисовку и ввод. `COLLAPSED` уходит из layout и инвалидирует и layout, и visual. Null становится `VISIBLE`.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visibility` | `Visibility` | `VISIBLE`, `HIDDEN` или `COLLAPSED`. Null означает `VISIBLE`. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visibility()` {: #visibility data-toc-label="visibility()" }

**Получить режим видимости**

Возвращает текущий режим видимости (VISIBLE, HIDDEN или COLLAPSED).

Возвращает `Visibility`: текущий режим видимости.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

**Задать видимость (булево)**

Удобный переключатель: устанавливает VISIBLE при true или HIDDEN при false.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | True для VISIBLE; false для HIDDEN. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visible()` {: #visible data-toc-label="visible()" }

**Видим ли виджет**

Проверяет, установлен ли режим видимости строго в VISIBLE.

Возвращает `boolean`: true, если виджет в режиме VISIBLE.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

**Задать доступность (Enabled)**

Включает или отключает интерактивность и выставляет CSS-псевдокласс :disabled.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | True для включения взаимодействия, false для отключения. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

**Доступен ли виджет**

Проверяет, принимает ли виджет взаимодействие пользователя.

Возвращает `boolean`: true, если виджет включён.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`hovered()` {: #hovered data-toc-label="hovered()" }

**Наведён ли курсор**

Проверяет, находится ли курсор мыши в данный момент в границах этого виджета.

Возвращает `boolean`: true, если курсор над виджетом.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`mouseCursor(MouseCursor)` {: #mousecursormousecursor data-toc-label="mouseCursor(MouseCursor)" }

**Задать курсор мыши**

Устанавливает форму курсора при наведении на включённый видимый виджет.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mouseCursor` | `MouseCursor` | Желаемая форма курсора мыши. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`mouseCursor()` {: #mousecursor data-toc-label="mouseCursor()" }

**Получить курсор мыши**

Возвращает форму курсора, настроенную для этого виджета.

Возвращает `MouseCursor`: настроенный курсор мыши.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`mouseCursorAt(float, float)` {: #mousecursoratfloat-float data-toc-label="mouseCursorAt(float, float)" }

**Получить курсор по координатам**

Определяет форму курсора для конкретных локальных координат внутри виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `localX` | `float` | Локальная координата X. |
| `localY` | `float` | Локальная координата Y. |

Возвращает `MouseCursor`: форма курсора в указанной точке.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusable(boolean)` {: #focusableboolean data-toc-label="focusable(boolean)" }

**Задать фокусируемость**

Разрешает или запрещает виджету получать клавиатурный фокус и участвовать в Tab-навигации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `focusable` | `boolean` | True для участия в фокусе с клавиатуры. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusable()` {: #focusable data-toc-label="focusable()" }

**Фокусируем ли виджет**

Проверяет, может ли виджет получать клавиатурный фокус.

Возвращает `boolean`: true, если виджет фокусируемый.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusScope(boolean)` {: #focusscopeboolean data-toc-label="focusScope(boolean)" }

**Задать границу фокуса (Scope)**

Делает этот виджет границей, замыкающей переключение Tab внутри его детей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `focusScope` | `boolean` | True для замыкания клавиатурного фокуса внутри этого поддерева. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusScope()` {: #focusscope data-toc-label="focusScope()" }

**Является ли границей фокуса**

Проверяет, действует ли этот виджет как изолированная область фокуса.

Возвращает `boolean`: true, если область фокуса активна.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusOrder(int)` {: #focusorderint data-toc-label="focusOrder(int)" }

**Задать порядок фокуса**

Устанавливает явный относительный индекс последовательности клавиши Tab.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `focusOrder` | `int` | Относительный индекс порядка табуляции. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusOrder()` {: #focusorder data-toc-label="focusOrder()" }

**Получить порядок фокуса**

Возвращает относительный индекс табуляции, назначенный этому виджету.

Возвращает `int`: относительный индекс порядка фокуса.

</div>

### Трансформации и анимация

| Метод | Описание |
| --- | --- |
| [`opacity(float)`](#opacityfloat) | Мгновенно устанавливает непрозрачность виджета (от 0.0 до 1.0) и запрашивает перерисовку. |
| [`opacity()`](#opacity) | Возвращает текущую визуальную непрозрачность виджета. |
| [`rotationDegrees(float)`](#rotationdegreesfloat) | Мгновенно задаёт угол поворота в градусах вокруг активной опорной точки (pivot). |
| [`transformOrigin(TransformOrigin)`](#transformorigintransformorigin) | Выбирает именованный пресет опорной точки (CENTER, TOP_LEFT и др.) для вращения и масштабирования. |
| [`transformPivot(float, float)`](#transformpivotfloat-float) | Задаёт пользовательские координаты опорной точки в локальных пикселях виджета. |
| [`transform()`](#transform) | Возвращает активную модель Transform, управляющую смещениями, вращением, масштабом и pivot. |
| [`animate(AnimatedProperty, float, float)`](#animateanimatedproperty-float-float) | Плавно анимирует встроенное свойство к целевому значению за указанное количество секунд. |
| [`animate(AnimatedProperty, float, TransitionSpec)`](#animateanimatedproperty-float-transitionspec) | Плавно меняет встроенное свойство или применяет его сразу, если длительность неположительная. |
| [`animateOpacity(float, float)`](#animateopacityfloat-float) | Плавно переводит прозрачность к целевому значению с использованием кривой плавности. |
| [`animatePosition(float, float, float)`](#animatepositionfloat-float-float) | Анимирует смещение визуальной позиции виджета по осям X и Y. |
| [`animatePositionFrom(float, float, float, float, float)`](#animatepositionfromfloat-float-float-float-float) | Анимирует визуальную позицию строго из точки (startX, startY) в точку (endX, endY). |
| [`animateScale(float, float, float)`](#animatescalefloat-float-float) | Плавно изменяет визуальные коэффициенты масштабирования по обеим осям. |
| [`shake(float, float)`](#shakefloat-float) | Вызывает быстрое покачивание виджета для визуальной обратной связи об ошибке. |
| [`stopShakeAnimations()`](#stopshakeanimations) | Прерывает активные анимации покачивания и возвращает виджет в исходное положение. |
| [`animateSpring(AnimatedProperty, float, float, float)`](#animatespringanimatedproperty-float-float-float) | Запускает симуляцию гармонического осциллятора с реалистичным отскоком и затуханием. |
| [`animateColor(MutableColor, ColorView, float)`](#animatecolormutablecolor-colorview-float) | Плавно интерполирует цветовые компоненты RGBA к новому целевому цвету. |
| [`stopColorAnimations()`](#stopcoloranimations) | Прерывает все активные переходы цвета на этом виджете. |
| [`playStoryboard(Storyboard)`](#playstoryboardstoryboard) | Воспроизводит многодорожечный таймлайн Storyboard по дереву этого виджета. |
| [`playStoryboard(Object, Storyboard)`](#playstoryboardobject-storyboard) | Воспроизводит раскадровку под уникальным ключом, останавливая предыдущую с тем же ключом. |
| [`stopStoryboard(Object)`](#stopstoryboardobject) | Останавливает и удаляет активную раскадровку с указанным ключом. |
| [`stopAnimation(AnimatedProperty)`](#stopanimationanimatedproperty) | Прерывает активные анимации, нацеленные на указанное свойство. |
| [`stopAnimations()`](#stopanimations) | Останавливает все переходы, пружины, shake и раскадровки на этом виджете. |
| [`animationRunning(AnimatedProperty)`](#animationrunninganimatedproperty) | Проверяет, проигрывается ли в данный момент анимация для указанного свойства. |
| [`animationsRunning()`](#animationsrunning) | Проверяет, проигрывается ли какая-либо анимация на этом виджете. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`opacity(float)` {: #opacityfloat data-toc-label="opacity(float)" }

**Задать непрозрачность**

Мгновенно устанавливает непрозрачность виджета (от 0.0 до 1.0) и запрашивает перерисовку.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `opacity` | `float` | Значение непрозрачности от 0.0 (прозрачный) до 1.0 (полная видимость). |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`opacity()` {: #opacity data-toc-label="opacity()" }

**Получить непрозрачность**

Возвращает текущую визуальную непрозрачность виджета.

Возвращает `float`: значение непрозрачности от 0.0 до 1.0.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`rotationDegrees(float)` {: #rotationdegreesfloat data-toc-label="rotationDegrees(float)" }

**Задать угол вращения**

Мгновенно задаёт угол поворота в градусах вокруг активной опорной точки (pivot).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `degrees` | `float` | Угол поворота в градусах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`transformOrigin(TransformOrigin)` {: #transformorigintransformorigin data-toc-label="transformOrigin(TransformOrigin)" }

**Задать опорную точку (Origin)**

Выбирает именованный пресет опорной точки (CENTER, TOP_LEFT и др.) для вращения и масштабирования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `origin` | `TransformOrigin` | Именованный пресет опорной точки. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`transformPivot(float, float)` {: #transformpivotfloat-float data-toc-label="transformPivot(float, float)" }

**Задать координаты точки вращения (Pivot)**

Задаёт пользовательские координаты опорной точки в локальных пикселях виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Координата X опорной точки в пикселях. |
| `y` | `float` | Координата Y опорной точки в пикселях. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`transform()` {: #transform data-toc-label="transform()" }

**Получить модель трансформации**

Возвращает активную модель Transform, управляющую смещениями, вращением, масштабом и pivot.

Возвращает `Transform`: активный экземпляр Transform.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animate(AnimatedProperty, float, float)` {: #animateanimatedproperty-float-float data-toc-label="animate(AnimatedProperty, float, float)" }

**Анимировать свойство по длительности**

Плавно анимирует встроенное свойство к целевому значению за указанное количество секунд.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Встроенное свойство: opacity, position, scale, rotation или pivot. |
| `targetValue` | `float` | Целевое значение. Opacity ограничивается диапазоном 0..1; нечисловые значения нормализуются. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animate(AnimatedProperty, float, TransitionSpec)` {: #animateanimatedproperty-float-transitionspec data-toc-label="animate(AnimatedProperty, float, TransitionSpec)" }

**Анимировать свойство**

Плавно меняет встроенное свойство или применяет его сразу, если длительность неположительная.

Уже идущий переход с теми же параметрами перенацеливается, а не перезапускается. Поворот идёт по кратчайшему углу. Null-свойство игнорируется; null-spec заменяется на `TransitionSpec.DEFAULT`.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Встроенное свойство: opacity, position, scale, rotation или pivot. |
| `targetValue` | `float` | Целевое значение. Opacity ограничивается диапазоном 0..1; нечисловые значения нормализуются. |
| `spec` | `TransitionSpec` | Длительность и easing. Null использует spec по умолчанию; неположительная длительность ставит значение сразу. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateOpacity(float, float)` {: #animateopacityfloat-float data-toc-label="animateOpacity(float, float)" }

**Анимировать непрозрачность**

Плавно переводит прозрачность к целевому значению с использованием кривой плавности.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetOpacity` | `float` | Целевая непрозрачность от 0.0 до 1.0. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animatePosition(float, float, float)` {: #animatepositionfloat-float-float data-toc-label="animatePosition(float, float, float)" }

**Анимировать позицию**

Анимирует смещение визуальной позиции виджета по осям X и Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Целевое смещение X в пикселях. |
| `y` | `float` | Целевое смещение Y в пикселях. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animatePositionFrom(float, float, float, float, float)` {: #animatepositionfromfloat-float-float-float-float data-toc-label="animatePositionFrom(float, float, float, float, float)" }

**Анимировать позицию от начальной точки**

Анимирует визуальную позицию строго из точки (startX, startY) в точку (endX, endY).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `startX` | `float` | Начальное смещение X. |
| `startY` | `float` | Начальное смещение Y. |
| `endX` | `float` | Конечное смещение X. |
| `endY` | `float` | Конечное смещение Y. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateScale(float, float, float)` {: #animatescalefloat-float-float data-toc-label="animateScale(float, float, float)" }

**Анимировать масштаб**

Плавно изменяет визуальные коэффициенты масштабирования по обеим осям.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Целевой масштаб по оси X. |
| `y` | `float` | Целевой масштаб по оси Y. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`shake(float, float)` {: #shakefloat-float data-toc-label="shake(float, float)" }

**Запустить покачивание (Shake)**

Вызывает быстрое покачивание виджета для визуальной обратной связи об ошибке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `amplitude` | `float` | Амплитуда отклонения покачивания в пикселях. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopShakeAnimations()` {: #stopshakeanimations data-toc-label="stopShakeAnimations()" }

**Остановить покачивание**

Прерывает активные анимации покачивания и возвращает виджет в исходное положение.

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateSpring(AnimatedProperty, float, float, float)` {: #animatespringanimatedproperty-float-float-float data-toc-label="animateSpring(AnimatedProperty, float, float, float)" }

**Анимировать с физикой пружины**

Запускает симуляцию гармонического осциллятора с реалистичным отскоком и затуханием.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Встроенное свойство: opacity, position, scale, rotation или pivot. |
| `targetValue` | `float` | Целевое значение. Opacity ограничивается диапазоном 0..1; нечисловые значения нормализуются. |
| `stiffness` | `float` | Коэффициент жёсткости пружины. |
| `damping` | `float` | Коэффициент демпфирования (затухания). |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateColor(MutableColor, ColorView, float)` {: #animatecolormutablecolor-colorview-float data-toc-label="animateColor(MutableColor, ColorView, float)" }

**Анимировать цвет**

Плавно интерполирует цветовые компоненты RGBA к новому целевому цвету.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `MutableColor` | Целевой изменяемый цвет MutableColor. |
| `targetColor` | `ColorView` | Конечное значение цвета. |
| `durationSeconds` | `float` | Длительность перехода в секундах. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopColorAnimations()` {: #stopcoloranimations data-toc-label="stopColorAnimations()" }

**Остановить анимации цвета**

Прерывает все активные переходы цвета на этом виджете.

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`playStoryboard(Storyboard)` {: #playstoryboardstoryboard data-toc-label="playStoryboard(Storyboard)" }

**Запустить раскадровку (Storyboard)**

Воспроизводит многодорожечный таймлайн Storyboard по дереву этого виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `storyboard` | `Storyboard` | Объект раскадровки Storyboard для воспроизведения. |

Возвращает `StoryboardPlayer`: StoryboardPlayer, управляющий воспроизведением.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`playStoryboard(Object, Storyboard)` {: #playstoryboardobject-storyboard data-toc-label="playStoryboard(Object, Storyboard)" }

**Запустить именованную раскадровку**

Воспроизводит раскадровку под уникальным ключом, останавливая предыдущую с тем же ключом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `key` | `Object` | Уникальный ключ раскадровки. |
| `storyboard` | `Storyboard` | Объект раскадровки Storyboard для воспроизведения. |

Возвращает `StoryboardPlayer`: StoryboardPlayer, управляющий воспроизведением.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopStoryboard(Object)` {: #stopstoryboardobject data-toc-label="stopStoryboard(Object)" }

**Остановить раскадровку**

Останавливает и удаляет активную раскадровку с указанным ключом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `key` | `Object` | Уникальный ключ раскадровки. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopAnimation(AnimatedProperty)` {: #stopanimationanimatedproperty data-toc-label="stopAnimation(AnimatedProperty)" }

**Остановить анимацию свойства**

Прерывает активные анимации, нацеленные на указанное свойство.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Встроенное свойство: opacity, position, scale, rotation или pivot. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopAnimations()` {: #stopanimations data-toc-label="stopAnimations()" }

**Остановить все анимации**

Останавливает все переходы, пружины, shake и раскадровки на этом виджете.

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animationRunning(AnimatedProperty)` {: #animationrunninganimatedproperty data-toc-label="animationRunning(AnimatedProperty)" }

**Анимируется ли свойство**

Проверяет, проигрывается ли в данный момент анимация для указанного свойства.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Встроенное свойство: opacity, position, scale, rotation или pivot. |

Возвращает `boolean`: true, если свойство анимируется.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animationsRunning()` {: #animationsrunning data-toc-label="animationsRunning()" }

**Воспроизводятся ли анимации**

Проверяет, проигрывается ли какая-либо анимация на этом виджете.

Возвращает `boolean`: true, если активна хотя бы одна анимация.

</div>

### Привязки

| Метод | Описание |
| --- | --- |
| [`bind(BindingData, Object)`](#bindbindingdata-object) | Связывает поддерживаемый ключ свойства с сигналом и возвращает этот виджет. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`bind(BindingData, Object)` {: #bindbindingdata-object data-toc-label="bind(BindingData, Object)" }

**Привязать свойство к сигналу**

Связывает поддерживаемый ключ свойства с сигналом и возвращает этот виджет.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `key` | `BindingData` | Ключ-идентификатор свойства виджета и ожидаемого типа сигнала. |
| `signal` | `Object` | Сигнал, который задаёт значение свойства и получает изменения виджета. |

Возвращает `WidgetBase`: Этот виджет для дальнейшей fluent-настройки.

</div>

### Отрисовка и кэширование

| Метод | Описание |
| --- | --- |
| [`renderer(WidgetRender)`](#rendererwidgetrender) | Заменяет стандартный visual виджета одним экземпляром renderer. |
| [`renderer()`](#renderer) | Возвращает пользовательский процедурный рендерер или null при стандартной отрисовке. |
| [`renderCachingEnabled(boolean)`](#rendercachingenabledboolean) | Включает или выключает retained-кэширование записанных команд рисования. |
| [`renderCachingEnabled()`](#rendercachingenabled) | Проверяет, активно ли retained-кэширование команд отрисовки. |
| [`invalidateRenderCache()`](#invalidaterendercache) | Помечает буфер команд устаревшим, вызывая повторную запись на следующем кадре. |
| [`render(RenderContext)`](#renderrendercontext) | Выполняет отрисовку виджета: вызывает кастомный рендерер или стандартное содержимое, затем обходит детей. |
| [`renderCached(RenderContext)`](#rendercachedrendercontext) | Воспроизводит сохранённый буфер команд рисования или перезаписывает его при устаревании. |
| [`measure(LayoutContext)`](#measurelayoutcontext) | Вычисляет желаемый размер desiredSize на основе содержимого и ограничений LayoutContext. |
| [`measureCached(LayoutContext)`](#measurecachedlayoutcontext) | Повторно использует измеренный desiredSize, если ограничения совпадают и поддерево не dirty. |
| [`arrange(RectView)`](#arrangerectview) | Устанавливает окончательные границы виджета в координатах экрана и размещает дочерние элементы. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderer(WidgetRender)` {: #rendererwidgetrender data-toc-label="renderer(WidgetRender)" }

**Свой renderer**

Заменяет стандартный visual виджета одним экземпляром renderer.

Renderer рисует фон и содержимое. Обход детей по-прежнему делает фреймворк. Null возвращает стандартный visual. Renderer из стилей применяется только если этот слот пуст.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `renderer` | `WidgetRender` | Экземпляр `WidgetRender` или null для стандартного visual. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderer()` {: #renderer data-toc-label="renderer()" }

**Получить рендерер**

Возвращает пользовательский процедурный рендерер или null при стандартной отрисовке.

Возвращает `WidgetRender`: активный экземпляр WidgetRender или null.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderCachingEnabled(boolean)` {: #rendercachingenabledboolean data-toc-label="renderCachingEnabled(boolean)" }

**Переключить кэш рендеринга**

Включает или выключает retained-кэширование записанных команд рисования.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | True для включения кэширования команд рисования. |

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderCachingEnabled()` {: #rendercachingenabled data-toc-label="renderCachingEnabled()" }

**Включён ли кэш рендеринга**

Проверяет, активно ли retained-кэширование команд отрисовки.

Возвращает `boolean`: true, если кэширование включено.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`invalidateRenderCache()` {: #invalidaterendercache data-toc-label="invalidateRenderCache()" }

**Инвалидировать кэш отрисовки**

Помечает буфер команд устаревшим, вызывая повторную запись на следующем кадре.

Возвращает `WidgetBase`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`render(RenderContext)` {: #renderrendercontext data-toc-label="render(RenderContext)" }

**Отрисовать виджет**

Выполняет отрисовку виджета: вызывает кастомный рендерер или стандартное содержимое, затем обходит детей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext, предоставляющий канвас и состояние графики. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderCached(RenderContext)` {: #rendercachedrendercontext data-toc-label="renderCached(RenderContext)" }

**Отрисовать с кэшированием**

Воспроизводит сохранённый буфер команд рисования или перезаписывает его при устаревании.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext, предоставляющий канвас и состояние графики. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`measure(LayoutContext)` {: #measurelayoutcontext data-toc-label="measure(LayoutContext)" }

**Измерить размеры**

Вычисляет желаемый размер desiredSize на основе содержимого и ограничений LayoutContext.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `LayoutContext` | LayoutContext с доступным пространством и контекстом родителя. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`measureCached(LayoutContext)` {: #measurecachedlayoutcontext data-toc-label="measureCached(LayoutContext)" }

**Измерить с кэшированием**

Повторно использует измеренный desiredSize, если ограничения совпадают и поддерево не dirty.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `LayoutContext` | LayoutContext с доступным пространством и контекстом родителя. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`arrange(RectView)` {: #arrangerectview data-toc-label="arrange(RectView)" }

**Разместить границы (Arrange)**

Устанавливает окончательные границы виджета в координатах экрана и размещает дочерние элементы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `bounds` | `RectView` | Финальный прямоугольник границ размещения. |

</div>

### События и жизненный цикл

| Метод | Описание |
| --- | --- |
| [`on(EventType, EventListener)`](#oneventtype-eventlistener) | Регистрирует строго типизированный слушатель событий и возвращает дескриптор подписки. |
| [`on(EventType, boolean, EventListener)`](#oneventtype-boolean-eventlistener) | Регистрирует типизированный слушатель с контролем получения событий, помеченных как cancelled. |
| [`emit(Event)`](#emitevent) | Отправляет событие слушателям, зарегистрированным непосредственно на этом виджете. |
| [`handle(Event)`](#handleevent) | Диспетчеризирует событие и выполняет стандартную маршрутизацию ввода. |
| [`invalidate(int)`](#invalidateint) | Помечает layout или visual устаревшими и поднимает флаги к родителям. |
| [`clearInvalidation(int)`](#clearinvalidationint) | Очищает обработанные dirty-флаги после завершения прохода layout или отрисовки. |
| [`invalidationFlags()`](#invalidationflags) | Возвращает битовую маску ожидающих флагов инвалидации на этом виджете. |
| [`subtreeInvalidationFlags()`](#subtreeinvalidationflags) | Возвращает объединённую битовую маску флагов инвалидации всего поддерева этого виджета. |
| [`tick(FrameContext)`](#tickframecontext) | Продвигает таймлайны анимаций, переходов и временное состояние виджета на текущем кадре. |
| [`dispose()`](#dispose) | Освобождает занятые ресурсы, останавливает активные анимации и отписывает слушателей. |
| [`parent()`](#parent) | Возвращает родительский контейнер или null, если виджет корневой либо отсоединён. |
| [`children()`](#children) | Возвращает неизменяемый список дочерних виджетов, входящих в состав этого контейнера. |
| [`uiContext()`](#uicontext) | Возвращает активный UIContext, предоставляющий доступ к окну, коэффициенту масштаба и фокусу. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`on(EventType, EventListener)` {: #oneventtype-eventlistener data-toc-label="on(EventType, EventListener)" }

**Подписаться на событие**

Регистрирует строго типизированный слушатель событий и возвращает дескриптор подписки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Маркер EventType, идентифицирующий тип события. |
| `listener` | `EventListener` | Callback-слушатель события. |

Возвращает `EventSubscription`: дескриптор EventSubscription для отписки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`on(EventType, boolean, EventListener)` {: #oneventtype-boolean-eventlistener data-toc-label="on(EventType, boolean, EventListener)" }

**Подписаться с отменёнными событиями**

Регистрирует типизированный слушатель с контролем получения событий, помеченных как cancelled.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Маркер EventType, идентифицирующий тип события. |
| `receiveCancelled` | `boolean` | True для получения отменённых событий. |
| `listener` | `EventListener` | Callback-слушатель события. |

Возвращает `EventSubscription`: дескриптор EventSubscription для отписки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`emit(Event)` {: #emitevent data-toc-label="emit(Event)" }

**Отправить событие (Emit)**

Отправляет событие слушателям, зарегистрированным непосредственно на этом виджете.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `event` | `Event` | Экземпляр отправляемого события. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`handle(Event)` {: #handleevent data-toc-label="handle(Event)" }

**Обработать событие (Handle)**

Диспетчеризирует событие и выполняет стандартную маршрутизацию ввода.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `event` | `Event` | Экземпляр отправляемого события. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`invalidate(int)` {: #invalidateint data-toc-label="invalidate(int)" }

**Инвалидация**

Помечает layout или visual устаревшими и поднимает флаги к родителям.

`InvalidationFlags.LAYOUT` — если меняются измерение или размещение, `VISUAL` — если только отрисовка. Вызов также сбрасывает retained render cache. `NONE` ничего не делает.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flags` | `int` | Битовые флаги `InvalidationFlags`, обычно `LAYOUT`, `VISUAL` или оба. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`clearInvalidation(int)` {: #clearinvalidationint data-toc-label="clearInvalidation(int)" }

**Сбросить флаги инвалидации**

Очищает обработанные dirty-флаги после завершения прохода layout или отрисовки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flags` | `int` | Битовые флаги `InvalidationFlags`, обычно `LAYOUT`, `VISUAL` или оба. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`invalidationFlags()` {: #invalidationflags data-toc-label="invalidationFlags()" }

**Получить флаги инвалидации**

Возвращает битовую маску ожидающих флагов инвалидации на этом виджете.

Возвращает `int`: битовая маска dirty-флагов.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`subtreeInvalidationFlags()` {: #subtreeinvalidationflags data-toc-label="subtreeInvalidationFlags()" }

**Получить флаги инвалидации поддерева**

Возвращает объединённую битовую маску флагов инвалидации всего поддерева этого виджета.

Возвращает `int`: битовая маска флагов инвалидации поддерева.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`tick(FrameContext)` {: #tickframecontext data-toc-label="tick(FrameContext)" }

**Тик кадра (Tick)**

Продвигает таймлайны анимаций, переходов и временное состояние виджета на текущем кадре.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `frame` | `FrameContext` | FrameContext, предоставляющий delta-время и часы кадра. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`dispose()` {: #dispose data-toc-label="dispose()" }

**Освободить ресурсы (Dispose)**

Освобождает занятые ресурсы, останавливает активные анимации и отписывает слушателей.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`parent()` {: #parent data-toc-label="parent()" }

**Получить родительский виджет**

Возвращает родительский контейнер или null, если виджет корневой либо отсоединён.

Возвращает `Widget`: родительский виджет или null.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`children()` {: #children data-toc-label="children()" }

**Получить дочерние виджеты**

Возвращает неизменяемый список дочерних виджетов, входящих в состав этого контейнера.

Возвращает `List`: список дочерних виджетов.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`uiContext()` {: #uicontext data-toc-label="uiContext()" }

**Получить контекст UI**

Возвращает активный UIContext, предоставляющий доступ к окну, коэффициенту масштаба и фокусу.

Возвращает `UIContext`: активный контекст UIContext.

</div>

