---
title: "CanvasWidget"
slug: "canvas-widget"
category: "Widgets/Display"
locale: "ru"
order: 110
---

# CanvasWidget

Процедурный холст для рисования произвольной графики через [CanvasDrawCallback](<../../core/widgets/display/canvas-draw-callback.md>).

## Обзор

`CanvasWidget` предоставляет холст для прямого рисования. Разработчики регистрируют коллбэки, получающие `RenderContext` с границами виджета для вывода процедурной графики, анимаций или диаграмм.

## Рисование на холсте

```java
CanvasWidget canvas = new CanvasWidget()
        .onDraw(ctx -> {
            DrawScope scope = new DrawScope(ctx);
            scope.line(0, 0, 100, 100, 2.0f, Color.RED);
        });
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CanvasWidget::</span>`clearDrawCallbacks()` {: #cleardrawcallbacks data-toc-label="clearDrawCallbacks()" }

**Очистить коллбэки**

Удаляет все зарегистрированные коллбэки отрисовки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CanvasWidget::</span>`onDraw(CanvasDrawCallback)` {: #ondrawcanvasdrawcallback data-toc-label="onDraw(CanvasDrawCallback)" }

**Зарегистрировать коллбэк рисования**

Регистрирует коллбэк отрисовки, вызываемый на каждом кадре рендера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `callback` | `CanvasDrawCallback` | — |

Возвращает `CanvasWidget`.

</div>

