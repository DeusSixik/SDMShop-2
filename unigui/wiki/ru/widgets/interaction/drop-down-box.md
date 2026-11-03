---
title: "DropDownBox"
slug: "drop-down-box"
category: "Widgets/Interaction"
locale: "ru"
order: 200
---

# DropDownBox

Раскрывающийся контейнер с кнопкой-заголовком и выпадающим списком в режиме popup или inline.

## Обзор

`DropDownBox` управляет раскрывающейся областью контента по клику на кнопку заголовка. Поддерживает как встроенный (inline) режим расширения разметки, так и всплывающий слой (overlay popup), с автоматическими индикаторами стрелок и прокруткой.

## Пример DropDownBox

```java
DropDownBox dropdown = new DropDownBox()
        .headerText("Advanced Settings")
        .content(new VBox().children(setting1, setting2))
        .useOverlay();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`close()` {: #close data-toc-label="close()" }

**Закрыть выпадающий контейнер**

Сворачивает выпадающий контейнер.

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`content()` {: #content data-toc-label="content()" }

**Получить виджет содержимого**

Возвращает текущий размещенный виджет содержимого.

Возвращает `Widget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

**Задать виджет содержимого**

Устанавливает дочерний виджет, отображаемый внутри раскрывающегося контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | — |

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`contentHost()` {: #contenthost data-toc-label="contentHost()" }

**Контейнер содержимого**

Возвращает контейнер, вмещающий содержимое.

Возвращает `Box`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`contentScroll()` {: #contentscroll data-toc-label="contentScroll()" }

**Прокрутка содержимого**

Возвращает [ScrollView](<../containers/scroll-view.md>), оборачивающий содержимое выпадающего меню.

Возвращает `ScrollView`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`dropDownWidth()` {: #dropdownwidth data-toc-label="dropDownWidth()" }

**Ширина выпадающего меню**

Возвращает заданную ширину выпадающего меню или 0 для автоматической подгонки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`dropDownWidth(float)` {: #dropdownwidthfloat data-toc-label="dropDownWidth(float)" }

**Задать ширину выпадающего меню**

Устанавливает фиксированную ширину выпадающего меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | — |

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`headerButton()` {: #headerbutton data-toc-label="headerButton()" }

**Кнопка заголовка**

Возвращает внутренний виджет кнопки заголовка.

Возвращает `Button`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`headerText()` {: #headertext data-toc-label="headerText()" }

**Получить текст заголовка**

Возвращает текстовую метку кнопки заголовка.

Возвращает `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`headerText(String)` {: #headertextstring data-toc-label="headerText(String)" }

**Задать текст заголовка**

Устанавливает текстовую метку заголовка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `headerText` | `String` | — |

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`maxContentHeight()` {: #maxcontentheight data-toc-label="maxContentHeight()" }

**Максимальная высота содержимого**

Возвращает максимальную высоту выпадающего блока со скроллом.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`maxContentHeight(float)` {: #maxcontentheightfloat data-toc-label="maxContentHeight(float)" }

**Задать макс. высоту содержимого**

Ограничивает максимальную высоту выпадающего блока до активации полосы прокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `height` | `float` | — |

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`open()` {: #open data-toc-label="open()" }

**Открыть выпадающий контейнер**

Раскрывает содержимое выпадающего контейнера.

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`opened()` {: #opened data-toc-label="opened()" }

**Состояние раскрытия**

Возвращает true, если выпадающий контейнер открыт.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`richHeaderText()` {: #richheadertext data-toc-label="richHeaderText()" }

**Получить форматированный заголовок**

Возвращает стилизованный [RichText](<../../core/text/rich-text.md>) заголовок.

Возвращает `RichText`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`richHeaderText(RichText)` {: #richheadertextrichtext data-toc-label="richHeaderText(RichText)" }

**Задать форматированный заголовок**

Устанавливает стилизованный [RichText](<../../core/text/rich-text.md>) заголовок.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `headerText` | `RichText` | — |

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

**Переключить раскрытие**

Инвертирует открытое/закрытое состояние контейнера.

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`useInline()` {: #useinline data-toc-label="useInline()" }

**Встроенный режим разметки**

Переводит выпадающий блок во встроенный режим расширения в родительском макете.

Возвращает `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`useOverlay()` {: #useoverlay data-toc-label="useOverlay()" }

**Режим всплывающего слоя**

Переводит выпадающий список в режим отдельного слоя поверх остального интерфейса.

Возвращает `DropDownBox`.

</div>

