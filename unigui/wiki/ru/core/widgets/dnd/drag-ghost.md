---
title: "DragGhost"
slug: "drag-ghost"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragGhost

Отрисовщик визуального ghost-представления перетаскиваемых элементов, отображаемый в слое оверлея под курсором.

## Обзор

`DragGhost` выполняет отрисовку визуального отклика под курсором на верхнем слое оверлея во время перетаскивания.

### Встроенные фабрики

| Метод | Поведение |
| --- | --- |
| `DragGhost.widget(widget, opacity)` | Отрисовывает клон исходного виджета с заданной прозрачностью (например, 0.8f). |
| `DragGhost.text()` | Отрисовывает стилизованный бэйдж с текстом `session.payload().preview()`. |

### Пользовательская отрисовка

Реализуйте метод `render(RenderContext context, DragSession session)` для рисования произвольных фигур или иконок:

```java
DragGhost custom = (context, session) -> {
    context.rect(session.pointerX() + 8, session.pointerY() + 8, 32, 32, Paint.fill(ColorView.CYAN));
};
```

## Клонирование виджета в качестве ghost-представления

```java
DragGhost ghost = DragGhost.widget(itemWidget, 0.8f);
```

## Связанные ссылки

- [DragController](<drag-controller.md>): Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.
- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragGhost::</span>`render(RenderContext, DragSession)` {: #renderrendercontext-dragsession data-toc-label="render(RenderContext, DragSession)" }

Отрисовывает визуализацию ghost-элемента в контексте оверлея.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Контекст отрисовки слоя оверлея. |
| `session` | `DragSession` | Текущая сессия перетаскивания. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragGhost::</span>`text()` {: #text data-toc-label="text()" }

Создает легковесный текстовый бэйдж в качестве ghost-представления текста предпросмотра payload.

Возвращает `DragGhost`: Новый DragGhost, отрисовывающий текстовый бэйдж..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragGhost::</span>`widget(Widget, float)` {: #widgetwidget-float data-toc-label="widget(Widget, float)" }

Создает ghost, клонирующий кэшированное отображение живого виджета с заданной непрозрачностью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | Исходный виджет для клонирования визуального представления. |
| `opacity` | `float` | Уровень непрозрачности ghost-клона. |

Возвращает `DragGhost`: Новый DragGhost, клонирующий исходный виджет..

</div>

