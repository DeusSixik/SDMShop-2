---
title: "UiLayerBounds"
slug: "ui-layer-bounds"
category: "Core/PostEffects"
locale: "ru"
order: 80
---

# UiLayerBounds

Размеры, положение и масштаб разрешения для закадрового буфера слоя пост-эффектов.

## Обзор

`UiLayerBounds` хранит прямоугольник координат и масштаб разрешения UI-слоя, отрисовываемого во временную текстуру. Методы `targetWidth()` и `targetHeight()` безопасно округляют и ограничивают размеры, предотвращая создание некорректных фреймбуферов.

## Создание границ вьюпорта

```java
UiLayerBounds bounds = UiLayerBounds.viewport(800.0f, 600.0f);
int texWidth = bounds.targetWidth();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`of(float, float, float, float)` {: #offloat-float-float-float data-toc-label="of(float, float, float, float)" }

**of(float, float, float, float)**

Создает границы слоя со стандартным масштабом 1.0.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Левая координата. |
| `y` | `float` | Верхняя координата. |
| `width` | `float` | Ширина слоя. |
| `height` | `float` | Высота слоя. |

Возвращает `UiLayerBounds`: Экземпляр UiLayerBounds..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`safeHeight()` {: #safeheight data-toc-label="safeHeight()" }

**safeHeight()**

Возвращает безопасную высоту не меньше 1.0.

Возвращает `float`: Экземпляр float..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`safeScale()` {: #safescale data-toc-label="safeScale()" }

**safeScale()**

Возвращает безопасный масштаб не меньше 0.01.

Возвращает `float`: Экземпляр float..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`safeWidth()` {: #safewidth data-toc-label="safeWidth()" }

**safeWidth()**

Возвращает безопасную ширину не меньше 1.0.

Возвращает `float`: Экземпляр float..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`targetHeight()` {: #targetheight data-toc-label="targetHeight()" }

**targetHeight()**

Возвращает высоту целевой текстуры в пикселях.

Возвращает `int`: Экземпляр int..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`targetWidth()` {: #targetwidth data-toc-label="targetWidth()" }

**targetWidth()**

Возвращает ширину целевой текстуры в пикселях.

Возвращает `int`: Экземпляр int..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

**viewport(float, float)**

Создает границы слоя для полного вьюпорта с (0, 0).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина вьюпорта. |
| `height` | `float` | Высота вьюпорта. |

Возвращает `UiLayerBounds`: Экземпляр UiLayerBounds..

</div>

