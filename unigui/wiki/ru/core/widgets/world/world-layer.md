---
title: "WorldLayer"
slug: "world-layer"
category: "Core/Виджеты/Мир"
locale: "ru"
order: 0
---

# WorldLayer

Функциональный контракт для фоновых или декоративных слоев отрисовки внутри [WorldCanvas](<../../../widgets/world/world-canvas.md>).

## Обзор

`WorldLayer` — функциональный интерфейс отрисовки для декоративных, фоновых или визуализирующих данные слоев внутри `WorldCanvas`.

Применение:
- Идеально подходит для координатных сеток, текстур ландшафта, лент маршрутов, тепловых карт и отладочной геометрии.
- Вызывается во время отрисовки холста с доступом к родительскому `WorldCanvas` и объекту `DrawScope`.
- Преобразует мировые координаты в экранные координаты с помощью `canvas.worldToRootX()` и `canvas.worldToRootY()`.

## Реализация пользовательского слоя отрисовки мира

```java
canvas.addWorldLayer((world, draw) -> {
    float x = world.worldToRootX(100.0f);
    float y = world.worldToRootY(200.0f);
    draw.addCircleFilled(x, y, 6.0f, MutableColor.ofRgb(0xFF0000));
});
```

## Связанные ссылки

- [RibbonLayer](<ribbon-layer.md>): Слой [WorldCanvas](<../../../widgets/world/world-canvas.md>) для отрисовки текстурированных или сплошных лент, маршрутов и полилиний в мировых координатах.
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldLayer::</span>`render(WorldCanvas, DrawScope)` {: #renderworldcanvas-drawscope data-toc-label="render(WorldCanvas, DrawScope)" }

Отрисовывает этот слой мира в текущую область рисования кадра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `canvas` | `WorldCanvas` | [WorldCanvas](<../../../widgets/world/world-canvas.md>), вызывающий отрисовку этого слоя мира. |
| `draw` | `DrawScope` | [DrawScope](<../../render/draw-scope.md>) для записи команд отрисовки текущего кадра. |

</div>

