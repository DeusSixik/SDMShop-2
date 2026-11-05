---
title: "CanvasDrawCallback"
slug: "canvas-draw-callback"
category: "Core/Виджеты/Отображение"
locale: "ru"
order: 120
---

# CanvasDrawCallback

Функциональный интерфейс для пользовательской 2D-отрисовки на [CanvasWidget](<../../../widgets/display/canvas-widget.md>).

## Обзор

`CanvasDrawCallback` вызывается во время прохода рендера `CanvasWidget`, получая объект `RenderContext`.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CanvasDrawCallback::</span>`draw(RenderContext)` {: #drawrendercontext data-toc-label="draw(RenderContext)" }

**Проход отрисовки**

Выполняет пользовательскую логику отрисовки содержимого холста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | — |

</div>

