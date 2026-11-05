---
title: "Button"
slug: "button"
category: "Widgets/Interaction"
locale: "ru"
order: 10
---

# Button

Стандартная интерактивная кнопка с композируемой поверхностью, иконкой, текстом и переходами состояний.

## Обзор

`Button` расширяет `SurfaceWidget<Button>`, объединяя стилизованную поверхность (сплошной цвет, текстура или GLSL-шейдер), рамку, скругление углов, кольцо фокуса и центрированный текст/иконку.

Поддерживает реактивные стили состояний (`WidgetState.NORMAL`, `HOVERED`, `PRESSED`, `DISABLED`), захват курсора, активацию клавишами (`SPACE`, `ENTER`) и подписку на клики через `onClick(...)`.

## Настройка состояний

Кнопки поддерживают явные переопределения состояний, имеющие приоритет над темой оформления:

- **`hoverBackground(color)`**: Цвет фона при наведении курсора.
- **`pressedBackground(color)`**: Цвет фона в момент нажатия.
- **`hoverTexture(handle)` / `pressedTexture(handle)`**: Отдельные текстуры для состояний.
- **`hoverShader(handle)`**: Процедурный GLSL-шейдер, активный при наведении.
- **`interactionTransitions(true)`**: Включает плавную анимацию масштаба и прозрачности при взаимодействии.

## Основная кнопка действия

```java
Button saveBtn = new Button("Сохранить")
        .background(ColorView.ofHex("#2C82C9"))
        .hoverBackground(ColorView.ofHex("#3A99D8"))
        .radius(6.0f);
saveBtn.onClick(e -> saveSettings());
```

## Связанные ссылки

- [HoldButton](<hold-button.md>)
- [ToggleButton](<toggle-button.md>)

## API

<div class="api-member-card" markdown="1">

### `Button()` {: #button data-toc-label="Button()" }

**Конструктор**

Создаёт пустой виджет кнопки.

</div>

<div class="api-member-card" markdown="1">

### `Button(String)` {: #buttonstring data-toc-label="Button(String)" }

**Создать с текстом**

Создаёт кнопку с текстовой подписью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | текст подписи |

</div>

<div class="api-member-card" markdown="1">

### `Button(RichText)` {: #buttonrichtext data-toc-label="Button(RichText)" }

**Создать с [RichText](<../../core/text/rich-text.md>)**

Создаёт кнопку с форматированным текстом [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | объект форматированного текста |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

**Задать [RichText](<../../core/text/rich-text.md>)**

Задаёт форматированный текст [RichText](<../../core/text/rich-text.md>) кнопки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `richText` | `RichText` | объект форматированного текста |

Возвращает `Button`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`onClick(EventListener)` {: #onclickeventlistener data-toc-label="onClick(EventListener)" }

**Слушатель клика**

Регистрирует коллбэк, вызываемый при клике мыши или нажатии клавиши активации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | слушатель события клика |

Возвращает `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`hoverBackground(ColorView)` {: #hoverbackgroundcolorview data-toc-label="hoverBackground(ColorView)" }

**Задать фон при наведении**

Задаёт пользовательский цвет фона при наведении курсора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | значение цвета |

Возвращает `Button`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`pressedBackground(ColorView)` {: #pressedbackgroundcolorview data-toc-label="pressedBackground(ColorView)" }

**Задать фон при нажатии**

Задаёт пользовательский цвет фона в момент нажатия.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | значение цвета |

Возвращает `Button`: этот виджет для fluent-настройки.

</div>

