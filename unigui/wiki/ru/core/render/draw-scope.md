---
title: "DrawScope"
slug: "draw-scope"
category: "Core/Render"
locale: "ru"
order: 10
---

# DrawScope

Безопасный draw-list фасад для рендереров виджетов: транслирует высокоуровневые вызовы отрисовки в backend-нейтральные команды, автоматически корректирует pivot трансформаций и предоставляет garbage-free примитивы.

## Архитектура и запись команд

`DrawScope` — это основной фасад отрисовки, передаваемый в кастомные рендереры виджетов (`WidgetRender#render(Widget, DrawScope)`). Он соединяет процедурное рисование в immediate-стиле с буферизованной draw-list архитектурой UniGUI.

Вместо прямых вызовов OpenGL, Vulkan или Direct3D, `DrawScope` превращает каждый вызов метода в неизменяемую команду `DrawCommand` и добавляет её в `DrawList` текущего кадра. Это обеспечивает автоматический батчинг вызовов отрисовки, многопоточный рендеринг, retained-кэширование и полную переносимость между всеми бэкендами.

| Подсистема | Основные методы | Возможности и архитектура |
| --- | --- | --- |
| **Базовые фигуры** | `rect`, `roundedRect`, `circle`, `line`, `path` | Высокоуровневые 2D-фигуры, настраиваемые через переиспользуемые объекты `Paint`. |
| **Garbage-Free примитивы** | `rectFilled`, `rectStroke`, `circleFilled` и др. | Отрисовка без аллокаций памяти в куче, принимающая `ColorView` напрямую без временного `Paint`. |
| **Текстуры и Nine-Slice** | `texture`, `textureTinted`, `nineSlice` | Отрисовка атласов с UV-координатами, скруглённых текстур и масштабируемых 9-slice панелей. |
| **Отрисовка текста** | `text`, `addText` | Отрисовка строк и форматированного `RichText` с субпиксельным позиционированием. |
| **Прямая геометрия и меши** | `addTriangle`, `addQuad`, `addMesh`, `ribbon` | Произвольные многоугольники, текстурированные quad'ы, индексированные меши и процедурные ленты. |
| **Векторные пути и каналы** | `pathLineTo`, `pathArcTo`, `pathBezier*`, `channelsSplit` | Построение интерактивных векторных контуров и многослойная сортировка вывода. |
| **Отсечение (Clipping)** | `pushClip`, `pushTextClip`, `popClip` | Аппаратное scissor-отсечение с защитными полями (bleed) для глифов текста. |

!!! note "Immediate API, отложенное исполнение"
    Вызов методов `DrawScope` не приводит к мгновенной отрисовке пикселей на экране. Все команды сохраняются в `RenderContext` и затем воспроизводятся, объединяются в батчи и исполняются графическим бэкендом.

## Трансформации и привязка Pivot

При отрисовке кастомной графики виджета все элементы должны подчиняться его глобальной трансформации (`Transform`: масштаб, поворот, смещение, скос и pivot). `DrawScope` согласует это автоматически.

### Автоматическая корректировка Pivot (`transformFor`)

Если `DrawScope` создан с границами макета виджета (`transformBounds`), он динамически вычисляет смещение между началом координат виджета и локальными координатами каждой команды:

```java
// Внутренняя логика корректировки pivot:
Transform adjusted = transform.copy();
adjusted.pivot().set(
        transform.pivot().x() + transformBoundsX - commandX,
        transform.pivot().y() + transformBoundsY - commandY);
```

Это гарантирует, что все нарисованные части (фон, иконки, текст) вращаются и масштабируются как единое целое вокруг общего pivot виджета, а не вокруг своих собственных верхних левых углов.

### Дочерние Scope (`withTransform`)

Метод `withTransform(Transform)` создаёт легковесный дочерний scope, который пишет в тот же render context, но применяет локальную трансформацию (например, для вращающихся спиннеров или пульсирующих значков):

```java
DrawScope iconScope = scope.withTransform(spinningTransform);
iconScope.texture(iconTexture, 0, 0, 16, 16, paint);
```

## Garbage-Free примитивы отрисовки

В высоконагруженном рендеринге Minecraft (60–144+ FPS) создание временных объектов на каждый кадр вызывает просадки производительности из-за работы сборщика мусора (GC).

`DrawScope` предоставляет две параллельные группы методов:

1. **Стандартные перегрузки с `Paint` (`rect`, `circle` и др.)**: Удобны, когда параметры заливки, обводки, шейдера и фильтра инкапсулированы в кэшируемый объект `Paint`.
2. **Garbage-Free перегрузки (`rectFilled`, `rectStroke`, `circleFilled` и др.)**: Принимают примитивные `float` координаты и экземпляры `ColorView` напрямую. При вызове они производят **ноль аллокаций в куче**.

| Фигура | Метод с [Paint](<paint.md>) | Garbage-Free метод (ColorView) | Влияние на GC |
| --- | --- | --- | --- |
| **Сплошной прямоугольник** | `scope.rect(x, y, w, h, paint)` | `scope.rectFilled(x, y, w, h, color)` | Ноль аллокаций |
| **Контур прямоугольника** | `scope.rect(x, y, w, h, paint)` | `scope.rectStroke(x, y, w, h, color, width)` | Ноль аллокаций |
| **Скруглённый прямоугольник** | `scope.roundedRect(..., paint)`| `scope.roundedRectFilled(..., color)` | Ноль аллокаций |
| **Круг / Эллипс** | `scope.circle(x, y, w, h, paint)`| `scope.circleFilled(x, y, w, h, color)` | Ноль аллокаций |
| **Текстурированный Quad** | `scope.texture(tex, placement, ...)` | `scope.addTexturedQuad(tex, x1, y1, ...)` | Ноль аллокаций |

> [!TIP]
> В методах `render` виджетов всегда отдавайте предпочтение перегрузкам `*Filled` и `*Stroke` с константами `ColorView` для максимальной стабильности FPS.

## Текстуры и система Nine-Slice

UniGUI включает гибкую систему работы с 2D-текстурами через `DrawScope`.

### Масштабируемые панели Nine-Slice

Текстуры Nine-Slice (9-patch) позволяют отображать прямоугольные панели произвольного размера без искажения углов и кромок:

- **4 фиксированных угла**: Верхний левый, верхний правый, нижний левый и нижний правый углы рисуются в масштабе 1:1 без растяжения.
- **4 растягиваемых края**: Верхний и нижний края растягиваются по горизонтали, боковые — по вертикали.
- **Центральная область**: Масштабируется по обеим осям либо остаётся полностью прозрачной при `fillCenter = false`.

```java
NineSlice borderSlice = NineSlice.of(windowTexture, 8, 8, 8, 8);
// Отрисовка окна с фоном:
scope.nineSlice(borderSlice, 0, 0, width, height, Paint.NONE, true);
// Отрисовка полой рамки выделения:
scope.nineSlice(borderSlice, 0, 0, width, height, Paint.NONE, false);
```

### Пропорциональная защита от переполнения

Если размеры виджета меньше суммы угловых рамок текстуры, `DrawScope` автоматически пропорционально ужимает углы (`Math.min(srcLeft, width * 0.5f)`). Это предотвращает наложение и искажение углов на маленьких кнопках.

## Векторные пути, меши и ленты (Ribbons)

Для процедурной графики, графов связей, интерактивных карт и схем `DrawScope` предоставляет богатый набор векторных операций.

### Процедурные ленты (Ribbons)

Метод `ribbon(...)` строит гладкую текстурированную ленту вдоль произвольной ломаной линии со стыками miter и прямыми торцами:

- **Связи в графах**: Идеально для проводов, трубопроводов и соединительных линий в редакторах схем.
- **Повторение текстуры**: При использовании текстуры с режимом `REPEAT` и параметром `textureLength`, равным длине тайла, текстура повторяется вдоль пути без искажений.

### Прямая геометрия и меши

- **Многоугольники**: `addPolyline`, `addConvexPolyFilled` и `addConcavePolyFilled` (с триангуляцией через ear-clipping).
- **Меши**: `addMesh(DrawMesh, TextureHandle)` позволяет передавать готовые триангулированные буферы вершин одной командой.
- **Градиенты**: `addRectFilledMultiColor` для 4-точечных плавных градиентов по углам.

## Отсечение и иерархия Scissor

Отсечение (clipping) ограничивает рисование прямоугольной областью экрана, что критично для списков с прокруткой, выпадающих меню и текстовых полей.

### Обычный Scissor против [Text](<../../widgets/display/text.md>)-Safe Clip

```java
// Базовое прямоугольное отсечение:
scope.pushClip(x, y, width, height);
// Отрисовка контента...
scope.popClip();
```

При выводе шрифтов стандартное аппаратное scissor-отсечение может обрезать антиалиасинг или выступающие части курсивных глифов по границам строки.

Метод `DrawScope#pushTextClip` автоматически добавляет защитные поля (bleed: слева/справа 1px, сверху 1px, снизу 2px), сохраняя чистые края шрифта и чёткое сглаживание.

!!! important "Обязательный баланс Push и Pop"
    Каждый вызов `pushClip(...)` или `pushTextClip(...)` обязан завершаться парным вызовом `popClip()`, как правило внутри блока `try / finally`:
    ```java
    scope.pushClip(bounds.x(), bounds.y(), bounds.width(), bounds.height());
    try {
        renderContent(scope);
    } finally {
        scope.popClip();
    }
    ```

## Отрисовка карточки с Nine-Slice шапкой и фоном

```java
@Override
public void render(Widget widget, DrawScope scope) {
    RectView b = widget.layoutBounds();
    
    // 1. Рисуем тень и скруглённый фон
    scope.roundedRectFilled(b.x() + 2, b.y() + 2, b.width(), b.height(), 6.0f, ColorView.rgba(0, 0, 0, 80));
    scope.roundedRectFilled(b.x(), b.y(), b.width(), b.height(), 6.0f, ColorView.rgb(32, 34, 37));
    scope.roundedRectStroke(b.x(), b.y(), b.width(), b.height(), 6.0f, ColorView.rgb(55, 58, 64), 1.0f);
    
    // 2. Рисуем 9-slice плашку заголовка
    NineSlice headerSlice = NineSlice.of(cardHeaderTex, 4, 4, 4, 4);
    scope.nineSlice(headerSlice, b.x() + 1, b.y() + 1, b.width() - 2, 28.0f, Paint.NONE, true);
    
    // 3. Выводим текст с безопасным отсечением
    scope.pushTextClip(b.x() + 10, b.y() + 6, b.width() - 20, 18.0f);
    try {
        scope.addText("Карточка настроек", b.x() + 10, b.y() + 6, b.width() - 20, 18.0f, ColorView.WHITE);
    } finally {
        scope.popClip();
    }
}
```

## Отрисовка процедурной текстурированной ленты между точками

```java
List<DrawPoint> path = List.of(
        new DrawPoint(20.0f, 40.0f),
        new DrawPoint(80.0f, 40.0f),
        new DrawPoint(140.0f, 90.0f),
        new DrawPoint(200.0f, 90.0f)
);

// Рисуем непрерывный кабель или трубку со стыками
RibbonOptions options = RibbonOptions.defaults()
        .closed(false)
        .miterLimit(3.0f);

scope.ribbon(cableTexture, path, 6.0f, 32.0f, ColorView.rgb(0, 200, 255), options);
```

## API

### Scope и трансформации

| Метод | Описание |
| --- | --- |
| [`DrawScope(RenderContext, Transform)`](#drawscoperendercontext-transform) | Создаёт scope отрисовки без привязки anchor bounds для transform pivot. |
| [`DrawScope(RenderContext, Transform, RectView)`](#drawscoperendercontext-transform-rectview) | Создаёт scope отрисовки с границами макета виджета для удержания относительного transform pivot. |
| [`context()`](#context) | Возвращает базовый контекст рендеринга, в который записываются команды отрисовки. |
| [`transform()`](#transform) | Возвращает текущую трансформацию, применяемую этим scope ко всем создаваемым командам. |
| [`withTransform(Transform)`](#withtransformtransform) | Создаёт дочерний scope для того же render context с новой трансформацией. |

<div class="api-member-card" markdown="1">

#### `DrawScope(RenderContext, Transform)` {: #drawscoperendercontext-transform data-toc-label="DrawScope(RenderContext, Transform)" }

**Создать Scope**

Создаёт scope отрисовки без привязки anchor bounds для transform pivot.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext, куда записываются команды отрисовки. |
| `transform` | `Transform` | Transform виджета, применяемый к командам в этом scope. |

</div>

<div class="api-member-card" markdown="1">

#### `DrawScope(RenderContext, Transform, RectView)` {: #drawscoperendercontext-transform-rectview data-toc-label="DrawScope(RenderContext, Transform, RectView)" }

**Создать Scope с привязкой Bounds**

Создаёт scope отрисовки с границами макета виджета для удержания относительного transform pivot.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext, куда записываются команды отрисовки. |
| `transform` | `Transform` | Transform виджета, применяемый к командам в этом scope. |
| `transformBounds` | `RectView` | Исходные границы макета виджета для привязки transform pivot. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`context()` {: #context data-toc-label="context()" }

**Получить RenderContext**

Возвращает базовый контекст рендеринга, в который записываются команды отрисовки.

Возвращает `RenderContext`: Экземпляр активного RenderContext..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`transform()` {: #transform data-toc-label="transform()" }

**Получить трансформацию Scope**

Возвращает текущую трансформацию, применяемую этим scope ко всем создаваемым командам.

Возвращает `Transform`: Экземпляр активного Transform или null, если трансформация не задана..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`withTransform(Transform)` {: #withtransformtransform data-toc-label="withTransform(Transform)" }

**Создать дочерний Scope с трансформацией**

Создаёт дочерний scope для того же render context с новой трансформацией.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `transform` | `Transform` | Transform виджета, применяемый к командам в этом scope. |

Возвращает `DrawScope`: Новый экземпляр DrawScope с тем же контекстом и указанной трансформацией..

</div>

### Базовые фигуры и примитивы

| Метод | Описание |
| --- | --- |
| [`rect(float, float, float, float, Paint)`](#rectfloat-float-float-float-paint) | Рисует прямоугольник с параметрами заливки и обводки из [Paint](<paint.md>). |
| [`roundedRect(float, float, float, float, float, Paint)`](#roundedrectfloat-float-float-float-float-paint) | Рисует прямоугольник со скруглёнными углами заданного радиуса с использованием [Paint](<paint.md>). |
| [`circle(float, float, float, float, Paint)`](#circlefloat-float-float-float-paint) | Рисует эллипс или окружность, вписанную в указанную прямоугольную область. |
| [`line(float, float, float, float, Paint)`](#linefloat-float-float-float-paint) | Рисует прямую линию между двумя точками с использованием [Paint](<paint.md>). |
| [`path(VectorPath, float, float, float, float, Paint)`](#pathvectorpath-float-float-float-float-paint) | Рисует переиспользуемый векторный путь VectorPath, масштабированный в целевые границы. |
| [`rectFilled(float, float, float, float, ColorView)`](#rectfilledfloat-float-float-float-colorview) | Заливает прямоугольник сплошным цветом без аллокаций памяти под [Paint](<paint.md>). |
| [`rectStroke(float, float, float, float, ColorView, float)`](#rectstrokefloat-float-float-float-colorview-float) | Рисует контурную рамку прямоугольника без аллокаций памяти под [Paint](<paint.md>). |
| [`roundedRectFilled(float, float, float, float, float, ColorView)`](#roundedrectfilledfloat-float-float-float-float-colorview) | Заливает прямоугольник со скруглёнными углами без аллокаций памяти под [Paint](<paint.md>). |
| [`roundedRectStroke(float, float, float, float, float, ColorView, float)`](#roundedrectstrokefloat-float-float-float-float-colorview-float) | Рисует контур скруглённого прямоугольника без аллокаций памяти под [Paint](<paint.md>). |
| [`circleFilled(float, float, float, float, ColorView)`](#circlefilledfloat-float-float-float-colorview) | Заливает круг или эллипс сплошным цветом без аллокаций памяти под [Paint](<paint.md>). |
| [`circleStroke(float, float, float, float, ColorView, float)`](#circlestrokefloat-float-float-float-colorview-float) | Рисует контур окружности или эллипса без аллокаций памяти под [Paint](<paint.md>). |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`rect(float, float, float, float, Paint)` {: #rectfloat-float-float-float-paint data-toc-label="rect(float, float, float, float, Paint)" }

**Нарисовать прямоугольник**

Рисует прямоугольник с параметрами заливки и обводки из [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`roundedRect(float, float, float, float, float, Paint)` {: #roundedrectfloat-float-float-float-float-paint data-toc-label="roundedRect(float, float, float, float, float, Paint)" }

**Нарисовать скруглённый прямоугольник**

Рисует прямоугольник со скруглёнными углами заданного радиуса с использованием [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`circle(float, float, float, float, Paint)` {: #circlefloat-float-float-float-paint data-toc-label="circle(float, float, float, float, Paint)" }

**Нарисовать круг / эллипс**

Рисует эллипс или окружность, вписанную в указанную прямоугольную область.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`line(float, float, float, float, Paint)` {: #linefloat-float-float-float-paint data-toc-label="line(float, float, float, float, Paint)" }

**Нарисовать линию**

Рисует прямую линию между двумя точками с использованием [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x1` | `float` | X-координата первой точки. |
| `y1` | `float` | Y-координата первой точки. |
| `x2` | `float` | X-координата второй точки. |
| `y2` | `float` | Y-координата второй точки. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`path(VectorPath, float, float, float, float, Paint)` {: #pathvectorpath-float-float-float-float-paint data-toc-label="path(VectorPath, float, float, float, float, Paint)" }

**Нарисовать векторный путь**

Рисует переиспользуемый векторный путь VectorPath, масштабированный в целевые границы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `path` | `VectorPath` | Экземпляр векторного пути (VectorPath), содержащий контур фигуры. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`rectFilled(float, float, float, float, ColorView)` {: #rectfilledfloat-float-float-float-colorview data-toc-label="rectFilled(float, float, float, float, ColorView)" }

**Залить прямоугольник (Garbage-Free)**

Заливает прямоугольник сплошным цветом без аллокаций памяти под [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`rectStroke(float, float, float, float, ColorView, float)` {: #rectstrokefloat-float-float-float-colorview-float data-toc-label="rectStroke(float, float, float, float, ColorView, float)" }

**Обвести прямоугольник (Garbage-Free)**

Рисует контурную рамку прямоугольника без аллокаций памяти под [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `strokeWidth` | `float` | Толщина контурной линии обводки в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`roundedRectFilled(float, float, float, float, float, ColorView)` {: #roundedrectfilledfloat-float-float-float-float-colorview data-toc-label="roundedRectFilled(float, float, float, float, float, ColorView)" }

**Залить скруглённый прямоугольник (Garbage-Free)**

Заливает прямоугольник со скруглёнными углами без аллокаций памяти под [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`roundedRectStroke(float, float, float, float, float, ColorView, float)` {: #roundedrectstrokefloat-float-float-float-float-colorview-float data-toc-label="roundedRectStroke(float, float, float, float, float, ColorView, float)" }

**Обвести скруглённый прямоугольник (Garbage-Free)**

Рисует контур скруглённого прямоугольника без аллокаций памяти под [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `strokeWidth` | `float` | Толщина контурной линии обводки в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`circleFilled(float, float, float, float, ColorView)` {: #circlefilledfloat-float-float-float-colorview data-toc-label="circleFilled(float, float, float, float, ColorView)" }

**Залить круг (Garbage-Free)**

Заливает круг или эллипс сплошным цветом без аллокаций памяти под [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`circleStroke(float, float, float, float, ColorView, float)` {: #circlestrokefloat-float-float-float-colorview-float data-toc-label="circleStroke(float, float, float, float, ColorView, float)" }

**Обвести круг (Garbage-Free)**

Рисует контур окружности или эллипса без аллокаций памяти под [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `strokeWidth` | `float` | Толщина контурной линии обводки в локальных пикселях. |

</div>

### Текстуры, Nine-Slice и шейдеры

| Метод | Описание |
| --- | --- |
| [`texture(TextureHandle, float, float, float, float, Paint)`](#texturetexturehandle-float-float-float-float-paint) | Рисует текстуру, растянутую по указанным границам с тинтом из [Paint](<paint.md>). |
| [`texture(TextureHandle, TexturePlacement, float, Paint)`](#texturetexturehandle-textureplacement-float-paint) | Рисует текстуру с точными UV-координатами placement и радиусом скругления. |
| [`nineSlice(NineSlice, float, float, float, float, Paint)`](#nineslicenineslice-float-float-float-float-paint) | Рисует масштабируемую 9-slice панель с сохранением пропорций углов и растяжением краёв. |
| [`nineSlice(NineSlice, float, float, float, float, Paint, boolean)`](#nineslicenineslice-float-float-float-float-paint-boolean) | Рисует 9-slice панель с возможностью отключения отрисовки центра для получения полой рамки. |
| [`textureTinted(TextureHandle, float, float, float, float, ColorView)`](#texturetintedtexturehandle-float-float-float-float-colorview) | Рисует текстурированный прямоугольник с цветом тинта напрямую из ColorView. |
| [`textureTinted(TextureHandle, TexturePlacement, float, ColorView)`](#texturetintedtexturehandle-textureplacement-float-colorview) | Рисует текстуру с placement, скруглением углов и прямым тинтом ColorView. |
| [`shader(ShaderHandle, float, float, float, float, ShaderUniforms, ShaderDrawOptions)`](#shadershaderhandle-float-float-float-float-shaderuniforms-shaderdrawoptions) | Выполняет проход кастомного шейдера в прямоугольной области с передачей uniforms. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`texture(TextureHandle, float, float, float, float, Paint)` {: #texturetexturehandle-float-float-float-float-paint data-toc-label="texture(TextureHandle, float, float, float, float, Paint)" }

**Нарисовать текстуру**

Рисует текстуру, растянутую по указанным границам с тинтом из [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`texture(TextureHandle, TexturePlacement, float, Paint)` {: #texturetexturehandle-textureplacement-float-paint data-toc-label="texture(TextureHandle, TexturePlacement, float, Paint)" }

**Нарисовать текстуру с placement и скруглением**

Рисует текстуру с точными UV-координатами placement и радиусом скругления.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `placement` | `TexturePlacement` | Параметры TexturePlacement с целевым прямоугольником и исходными UV-границами. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`nineSlice(NineSlice, float, float, float, float, Paint)` {: #nineslicenineslice-float-float-float-float-paint data-toc-label="nineSlice(NineSlice, float, float, float, float, Paint)" }

**Нарисовать Nine-Slice панель**

Рисует масштабируемую 9-slice панель с сохранением пропорций углов и растяжением краёв.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `slice` | `NineSlice` | Параметры [NineSlice](<../textures/nine-slice.md>) с отступами и текстурой. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`nineSlice(NineSlice, float, float, float, float, Paint, boolean)` {: #nineslicenineslice-float-float-float-float-paint-boolean data-toc-label="nineSlice(NineSlice, float, float, float, float, Paint, boolean)" }

**Нарисовать Nine-Slice с выбором заливки центра**

Рисует 9-slice панель с возможностью отключения отрисовки центра для получения полой рамки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `slice` | `NineSlice` | Параметры [NineSlice](<../textures/nine-slice.md>) с отступами и текстурой. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |
| `fillCenter` | `boolean` | Флаг отрисовки центральной области (false оставляет полую рамку). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`textureTinted(TextureHandle, float, float, float, float, ColorView)` {: #texturetintedtexturehandle-float-float-float-float-colorview data-toc-label="textureTinted(TextureHandle, float, float, float, float, ColorView)" }

**Нарисовать текстуру с тинтом (Garbage-Free)**

Рисует текстурированный прямоугольник с цветом тинта напрямую из ColorView.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`textureTinted(TextureHandle, TexturePlacement, float, ColorView)` {: #texturetintedtexturehandle-textureplacement-float-colorview data-toc-label="textureTinted(TextureHandle, TexturePlacement, float, ColorView)" }

**Нарисовать текстуру с placement и тинтом (Garbage-Free)**

Рисует текстуру с placement, скруглением углов и прямым тинтом ColorView.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `placement` | `TexturePlacement` | Параметры TexturePlacement с целевым прямоугольником и исходными UV-границами. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`shader(ShaderHandle, float, float, float, float, ShaderUniforms, ShaderDrawOptions)` {: #shadershaderhandle-float-float-float-float-shaderuniforms-shaderdrawoptions data-toc-label="shader(ShaderHandle, float, float, float, float, ShaderUniforms, ShaderDrawOptions)" }

**Отрисовать с кастомным шейдером**

Выполняет проход кастомного шейдера в прямоугольной области с передачей uniforms.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `shader` | `ShaderHandle` | Дескриптор скомпилированного кастомного шейдера. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `uniforms` | `ShaderUniforms` | Параметры [ShaderUniforms](<../shaders/shader-uniforms.md>), передаваемые в программу шейдера. |
| `options` | `ShaderDrawOptions` | Опции исполнения шейдера или настройки построения ленты [RibbonOptions](<ribbon-options.md>). |

</div>

### Отрисовка текста

| Метод | Описание |
| --- | --- |
| [`text(String, float, float, float, float, Paint)`](#textstring-float-float-float-float-paint) | Отрисовывает строку текста в заданных границах с использованием параметров [Paint](<paint.md>). |
| [`text(RichText, float, float, float, float, Paint)`](#textrichtext-float-float-float-float-paint) | Отрисовывает форматированный [RichText](<../text/rich-text.md>) с включённым выравниванием по пикселям. |
| [`text(RichText, float, float, float, float, Paint, boolean)`](#textrichtext-float-float-float-float-paint-boolean) | Отрисовывает [RichText](<../text/rich-text.md>) с настраиваемым выравниванием глифов по сетке пикселей. |
| [`addText(String, float, float, float, float, ColorView)`](#addtextstring-float-float-float-float-colorview) | Добавляет команду отрисовки текста с прямым цветом ColorView без аллокаций [Paint](<paint.md>). |
| [`addText(RichText, float, float, float, float, ColorView)`](#addtextrichtext-float-float-float-float-colorview) | Добавляет команду отрисовки [RichText](<../text/rich-text.md>) с прямым цветом ColorView без аллокаций [Paint](<paint.md>). |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`text(String, float, float, float, float, Paint)` {: #textstring-float-float-float-float-paint data-toc-label="text(String, float, float, float, float, Paint)" }

**Нарисовать текст**

Отрисовывает строку текста в заданных границах с использованием параметров [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая строка или объект [RichText](<../text/rich-text.md>) для отрисовки. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`text(RichText, float, float, float, float, Paint)` {: #textrichtext-float-float-float-float-paint data-toc-label="text(RichText, float, float, float, float, Paint)" }

**Нарисовать форматированный текст**

Отрисовывает форматированный [RichText](<../text/rich-text.md>) с включённым выравниванием по пикселям.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текстовая строка или объект [RichText](<../text/rich-text.md>) для отрисовки. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`text(RichText, float, float, float, float, Paint, boolean)` {: #textrichtext-float-float-float-float-paint-boolean data-toc-label="text(RichText, float, float, float, float, Paint, boolean)" }

**Нарисовать [RichText](<../text/rich-text.md>) с настройкой snapping**

Отрисовывает [RichText](<../text/rich-text.md>) с настраиваемым выравниванием глифов по сетке пикселей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текстовая строка или объект [RichText](<../text/rich-text.md>) для отрисовки. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `paint` | `Paint` | Параметры [Paint](<paint.md>), управляющие заливкой, обводкой, тинтом и шейдером. |
| `textPixelSnap` | `boolean` | Флаг привязки позиций глифов к целым пикселям экрана. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addText(String, float, float, float, float, ColorView)` {: #addtextstring-float-float-float-float-colorview data-toc-label="addText(String, float, float, float, float, ColorView)" }

**Добавить команду текста (Garbage-Free)**

Добавляет команду отрисовки текста с прямым цветом ColorView без аллокаций [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая строка или объект [RichText](<../text/rich-text.md>) для отрисовки. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addText(RichText, float, float, float, float, ColorView)` {: #addtextrichtext-float-float-float-float-colorview data-toc-label="addText(RichText, float, float, float, float, ColorView)" }

**Добавить команду [RichText](<../text/rich-text.md>) (Garbage-Free)**

Добавляет команду отрисовки [RichText](<../text/rich-text.md>) с прямым цветом ColorView без аллокаций [Paint](<paint.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текстовая строка или объект [RichText](<../text/rich-text.md>) для отрисовки. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

### Прямая геометрия и меши

| Метод | Описание |
| --- | --- |
| [`addDrawCmd(DrawCommand)`](#adddrawcmddrawcommand) | Добавляет готовую команду DrawCommand с автоматической корректировкой pivot трансформации. |
| [`addCallback(CustomDraw)`](#addcallbackcustomdraw) | Вставляет callback кастомной отрисовки, выполняемый во время воспроизведения команд бэкендом. |
| [`addLine(float, float, float, float, ColorView, float)`](#addlinefloat-float-float-float-colorview-float) | Добавляет команду отрисовки линии между двумя точками с указанной толщиной. |
| [`addRect(float, float, float, float, ColorView, float)`](#addrectfloat-float-float-float-colorview-float) | Добавляет команду обводки прямоугольника с заданной толщиной линии. |
| [`addRect(float, float, float, float, float, ColorView, float)`](#addrectfloat-float-float-float-float-colorview-float) | Добавляет команду обводки скруглённого прямоугольника с заданным радиусом и толщиной. |
| [`addRectFilled(float, float, float, float, ColorView)`](#addrectfilledfloat-float-float-float-colorview) | Добавляет команду сплошной заливки прямоугольника цветом ColorView. |
| [`addRectFilled(float, float, float, float, float, ColorView)`](#addrectfilledfloat-float-float-float-float-colorview) | Добавляет команду сплошной заливки скруглённого прямоугольника. |
| [`addRectFilledMultiColor(float, float, float, float, ColorView, ColorView, ColorView, ColorView)`](#addrectfilledmulticolorfloat-float-float-float-colorview-colorview-colorview-colorview) | Отрисовывает прямоугольник с плавной интерполяцией цветов между четырьмя вершинами. |
| [`addQuad(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView, float)`](#addquaddrawpoint-drawpoint-drawpoint-drawpoint-colorview-float) | Добавляет контур произвольного четырехугольника по четырём вершинам. |
| [`addQuadFilled(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)`](#addquadfilleddrawpoint-drawpoint-drawpoint-drawpoint-colorview) | Добавляет залитый произвольный четырехугольник по четырём вершинам. |
| [`addTriangle(DrawPoint, DrawPoint, DrawPoint, ColorView, float)`](#addtriangledrawpoint-drawpoint-drawpoint-colorview-float) | Добавляет контур треугольника по трём точкам. |
| [`addTriangleFilled(DrawPoint, DrawPoint, DrawPoint, ColorView)`](#addtrianglefilleddrawpoint-drawpoint-drawpoint-colorview) | Добавляет залитый треугольник по трём точкам. |
| [`addCircle(float, float, float, ColorView, int, float)`](#addcirclefloat-float-float-colorview-int-float) | Добавляет контур окружности, аппроксимированный заданным числом сегментов. |
| [`addCircleFilled(float, float, float, ColorView, int)`](#addcirclefilledfloat-float-float-colorview-int) | Добавляет залитый круг, аппроксимированный заданным числом сегментов. |
| [`addNgon(float, float, float, ColorView, int, float)`](#addngonfloat-float-float-colorview-int-float) | Добавляет контур правильного многоугольника с N вершинами (пятиугольник, шестиугольник и т.д.). |
| [`addNgonFilled(float, float, float, ColorView, int)`](#addngonfilledfloat-float-float-colorview-int) | Добавляет залитый правильный многоугольник с N вершинами. |
| [`addEllipse(float, float, float, float, ColorView, int, float)`](#addellipsefloat-float-float-float-colorview-int-float) | Добавляет контур эллипса с раздельными радиусами по осям X и Y. |
| [`addEllipseFilled(float, float, float, float, ColorView, int)`](#addellipsefilledfloat-float-float-float-colorview-int) | Добавляет залитый эллипс с раздельными радиусами по осям X и Y. |
| [`addPolyline(List, ColorView, boolean, float)`](#addpolylinelist-colorview-boolean-float) | Добавляет ломаную линию, соединяющую последовательность точек. |
| [`addConvexPolyFilled(List, ColorView)`](#addconvexpolyfilledlist-colorview) | Добавляет команду сплошной заливки выпуклого многоугольника произвольной формы. |
| [`addConcavePolyFilled(List, ColorView)`](#addconcavepolyfilledlist-colorview) | Добавляет команду заливки сложного невыпуклого многоугольника с триангуляцией. |
| [`addImage(TextureHandle, float, float, float, float, ColorView)`](#addimagetexturehandle-float-float-float-float-colorview) | Добавляет команду отрисовки текстуры на всю прямоугольную область с тинтом. |
| [`addImage(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView)`](#addimagetexturehandle-float-float-float-float-drawpoint-drawpoint-colorview) | Добавляет команду отрисовки части текстуры/атласа по координатам uvMin и uvMax. |
| [`addImageQuad(TextureHandle, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)`](#addimagequadtexturehandle-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-colorview) | Добавляет произвольный четырехугольник с индивидуальными UV-координатами для каждой вершины. |
| [`addTexturedQuad(TextureHandle, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, ColorView)`](#addtexturedquadtexturehandle-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-colorview) | Добавляет текстурированный четырехугольник через примитивные float координаты без создания объектов DrawPoint. |
| [`addImageRounded(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView, float)`](#addimageroundedtexturehandle-float-float-float-float-drawpoint-drawpoint-colorview-float) | Добавляет команду отрисовки текстуры со скруглением углов заданного радиуса. |
| [`addMesh(DrawMesh, TextureHandle)`](#addmeshdrawmesh-texturehandle) | Отправляет низкоуровневый полигональный меш DrawMesh с текстурой. |
| [`ribbon(TextureHandle, List, float, float, ColorView, RibbonOptions)`](#ribbontexturehandle-list-float-float-colorview-ribbonoptions) | Отрисовывает текстурированную ленту вдоль полилинии с miter-стыками и опциями [RibbonOptions](<ribbon-options.md>). |
| [`ribbon(TextureHandle, List, float, float, ColorView)`](#ribbontexturehandle-list-float-float-colorview) | Отрисовывает процедурную ленту со стандартными настройками. |
| [`ribbon(TextureHandle, List, float, float, ColorView, boolean)`](#ribbontexturehandle-list-float-float-colorview-boolean) | Отрисовывает замкнутый контур процедурной ленты. |
| [`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean)`](#ribbontexturehandle-list-float-float-colorview-boolean-boolean) | Отрисовывает ленту с разворотом осей UV на 90 градусов для вертикальных текстур. |
| [`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean, boolean, boolean)`](#ribbontexturehandle-list-float-float-colorview-boolean-boolean-boolean-boolean) | Отрисовывает ленту с разворотом и отражением UV координат. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addDrawCmd(DrawCommand)` {: #adddrawcmddrawcommand data-toc-label="addDrawCmd(DrawCommand)" }

**Добавить низкоуровневую команду отрисовки**

Добавляет готовую команду DrawCommand с автоматической корректировкой pivot трансформации.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `command` | `DrawCommand` | Готовая команда DrawCommand для прямого добавления. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addCallback(CustomDraw)` {: #addcallbackcustomdraw data-toc-label="addCallback(CustomDraw)" }

**Добавить callback пользовательской отрисовки**

Вставляет callback кастомной отрисовки, выполняемый во время воспроизведения команд бэкендом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `callback` | `CustomDraw` | Callback кастомной отрисовки, вызываемый бэкендом при воспроизведении. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addLine(float, float, float, float, ColorView, float)` {: #addlinefloat-float-float-float-colorview-float data-toc-label="addLine(float, float, float, float, ColorView, float)" }

**Добавить линию с толщиной**

Добавляет команду отрисовки линии между двумя точками с указанной толщиной.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x1` | `float` | X-координата первой точки. |
| `y1` | `float` | Y-координата первой точки. |
| `x2` | `float` | X-координата второй точки. |
| `y2` | `float` | Y-координата второй точки. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRect(float, float, float, float, ColorView, float)` {: #addrectfloat-float-float-float-colorview-float data-toc-label="addRect(float, float, float, float, ColorView, float)" }

**Добавить контур прямоугольника**

Добавляет команду обводки прямоугольника с заданной толщиной линии.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRect(float, float, float, float, float, ColorView, float)` {: #addrectfloat-float-float-float-float-colorview-float data-toc-label="addRect(float, float, float, float, float, ColorView, float)" }

**Добавить контур скруглённого прямоугольника**

Добавляет команду обводки скруглённого прямоугольника с заданным радиусом и толщиной.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRectFilled(float, float, float, float, ColorView)` {: #addrectfilledfloat-float-float-float-colorview data-toc-label="addRectFilled(float, float, float, float, ColorView)" }

**Добавить сплошную заливку прямоугольника**

Добавляет команду сплошной заливки прямоугольника цветом ColorView.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRectFilled(float, float, float, float, float, ColorView)` {: #addrectfilledfloat-float-float-float-float-colorview data-toc-label="addRectFilled(float, float, float, float, float, ColorView)" }

**Добавить сплошную заливку скруглённого прямоугольника**

Добавляет команду сплошной заливки скруглённого прямоугольника.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRectFilledMultiColor(float, float, float, float, ColorView, ColorView, ColorView, ColorView)` {: #addrectfilledmulticolorfloat-float-float-float-colorview-colorview-colorview-colorview data-toc-label="addRectFilledMultiColor(float, float, float, float, ColorView, ColorView, ColorView, ColorView)" }

**Добавить 4-цветный градиентный прямоугольник**

Отрисовывает прямоугольник с плавной интерполяцией цветов между четырьмя вершинами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `topLeft` | `ColorView` | Цвет верхней левой вершины. |
| `topRight` | `ColorView` | Цвет верхней правой вершины. |
| `bottomRight` | `ColorView` | Цвет нижней правой вершины. |
| `bottomLeft` | `ColorView` | Цвет нижней левой вершины. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addQuad(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView, float)` {: #addquaddrawpoint-drawpoint-drawpoint-drawpoint-colorview-float data-toc-label="addQuad(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView, float)" }

**Добавить контур четырехугольника**

Добавляет контур произвольного четырехугольника по четырём вершинам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `p1` | `DrawPoint` | Первая точка четырехугольника или треугольника. |
| `p2` | `DrawPoint` | Вторая точка четырехугольника или треугольника. |
| `p3` | `DrawPoint` | Третья точка четырехугольника или треугольника. |
| `p4` | `DrawPoint` | Четвёртая точка четырехугольника. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addQuadFilled(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)` {: #addquadfilleddrawpoint-drawpoint-drawpoint-drawpoint-colorview data-toc-label="addQuadFilled(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)" }

**Добавить залитый четырехугольник**

Добавляет залитый произвольный четырехугольник по четырём вершинам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `p1` | `DrawPoint` | Первая точка четырехугольника или треугольника. |
| `p2` | `DrawPoint` | Вторая точка четырехугольника или треугольника. |
| `p3` | `DrawPoint` | Третья точка четырехугольника или треугольника. |
| `p4` | `DrawPoint` | Четвёртая точка четырехугольника. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addTriangle(DrawPoint, DrawPoint, DrawPoint, ColorView, float)` {: #addtriangledrawpoint-drawpoint-drawpoint-colorview-float data-toc-label="addTriangle(DrawPoint, DrawPoint, DrawPoint, ColorView, float)" }

**Добавить контур треугольника**

Добавляет контур треугольника по трём точкам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `p1` | `DrawPoint` | Первая точка четырехугольника или треугольника. |
| `p2` | `DrawPoint` | Вторая точка четырехугольника или треугольника. |
| `p3` | `DrawPoint` | Третья точка четырехугольника или треугольника. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addTriangleFilled(DrawPoint, DrawPoint, DrawPoint, ColorView)` {: #addtrianglefilleddrawpoint-drawpoint-drawpoint-colorview data-toc-label="addTriangleFilled(DrawPoint, DrawPoint, DrawPoint, ColorView)" }

**Добавить залитый треугольник**

Добавляет залитый треугольник по трём точкам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `p1` | `DrawPoint` | Первая точка четырехугольника или треугольника. |
| `p2` | `DrawPoint` | Вторая точка четырехугольника или треугольника. |
| `p3` | `DrawPoint` | Третья точка четырехугольника или треугольника. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addCircle(float, float, float, ColorView, int, float)` {: #addcirclefloat-float-float-colorview-int-float data-toc-label="addCircle(float, float, float, ColorView, int, float)" }

**Добавить контур окружности с сегментами**

Добавляет контур окружности, аппроксимированный заданным числом сегментов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addCircleFilled(float, float, float, ColorView, int)` {: #addcirclefilledfloat-float-float-colorview-int data-toc-label="addCircleFilled(float, float, float, ColorView, int)" }

**Добавить залитый круг с сегментами**

Добавляет залитый круг, аппроксимированный заданным числом сегментов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addNgon(float, float, float, ColorView, int, float)` {: #addngonfloat-float-float-colorview-int-float data-toc-label="addNgon(float, float, float, ColorView, int, float)" }

**Добавить контур правильного многоугольника**

Добавляет контур правильного многоугольника с N вершинами (пятиугольник, шестиугольник и т.д.).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addNgonFilled(float, float, float, ColorView, int)` {: #addngonfilledfloat-float-float-colorview-int data-toc-label="addNgonFilled(float, float, float, ColorView, int)" }

**Добавить залитый правильный многоугольник**

Добавляет залитый правильный многоугольник с N вершинами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addEllipse(float, float, float, float, ColorView, int, float)` {: #addellipsefloat-float-float-float-colorview-int-float data-toc-label="addEllipse(float, float, float, float, ColorView, int, float)" }

**Добавить контур эллипса**

Добавляет контур эллипса с раздельными радиусами по осям X и Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radiusX` | `float` | Горизонтальный радиус по оси X. |
| `radiusY` | `float` | Вертикальный радиус по оси Y. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addEllipseFilled(float, float, float, float, ColorView, int)` {: #addellipsefilledfloat-float-float-float-colorview-int data-toc-label="addEllipseFilled(float, float, float, float, ColorView, int)" }

**Добавить залитый эллипс**

Добавляет залитый эллипс с раздельными радиусами по осям X и Y.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radiusX` | `float` | Горизонтальный радиус по оси X. |
| `radiusY` | `float` | Вертикальный радиус по оси Y. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addPolyline(List, ColorView, boolean, float)` {: #addpolylinelist-colorview-boolean-float data-toc-label="addPolyline(List, ColorView, boolean, float)" }

**Добавить полилинию**

Добавляет ломаную линию, соединяющую последовательность точек.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `closed` | `boolean` | Флаг замыкания контура ломаной или векторного пути. |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addConvexPolyFilled(List, ColorView)` {: #addconvexpolyfilledlist-colorview data-toc-label="addConvexPolyFilled(List, ColorView)" }

**Добавить залитый выпуклый многоугольник**

Добавляет команду сплошной заливки выпуклого многоугольника произвольной формы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addConcavePolyFilled(List, ColorView)` {: #addconcavepolyfilledlist-colorview data-toc-label="addConcavePolyFilled(List, ColorView)" }

**Добавить залитый невыпуклый многоугольник**

Добавляет команду заливки сложного невыпуклого многоугольника с триангуляцией.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImage(TextureHandle, float, float, float, float, ColorView)` {: #addimagetexturehandle-float-float-float-float-colorview data-toc-label="addImage(TextureHandle, float, float, float, float, ColorView)" }

**Добавить команду изображения**

Добавляет команду отрисовки текстуры на всю прямоугольную область с тинтом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImage(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView)` {: #addimagetexturehandle-float-float-float-float-drawpoint-drawpoint-colorview data-toc-label="addImage(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView)" }

**Добавить изображение с UV-диапазоном**

Добавляет команду отрисовки части текстуры/атласа по координатам uvMin и uvMax.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `uvMin` | `DrawPoint` | Верхняя левая UV-координата в пространстве текстуры. |
| `uvMax` | `DrawPoint` | Нижняя правая UV-координата в пространстве текстуры. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImageQuad(TextureHandle, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)` {: #addimagequadtexturehandle-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-colorview data-toc-label="addImageQuad(TextureHandle, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)" }

**Добавить текстурированный четырехугольник**

Добавляет произвольный четырехугольник с индивидуальными UV-координатами для каждой вершины.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `p1` | `DrawPoint` | Первая точка четырехугольника или треугольника. |
| `p2` | `DrawPoint` | Вторая точка четырехугольника или треугольника. |
| `p3` | `DrawPoint` | Третья точка четырехугольника или треугольника. |
| `p4` | `DrawPoint` | Четвёртая точка четырехугольника. |
| `uv1` | `DrawPoint` | Текстурная координата UV в вершине 1. |
| `uv2` | `DrawPoint` | Текстурная координата UV в вершине 2. |
| `uv3` | `DrawPoint` | Текстурная координата UV в вершине 3. |
| `uv4` | `DrawPoint` | Текстурная координата UV в вершине 4. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addTexturedQuad(TextureHandle, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, ColorView)` {: #addtexturedquadtexturehandle-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-colorview data-toc-label="addTexturedQuad(TextureHandle, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, ColorView)" }

**Добавить текстурированный quad (float примитивы)**

Добавляет текстурированный четырехугольник через примитивные float координаты без создания объектов DrawPoint.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `x1` | `float` | X-координата первой точки. |
| `y1` | `float` | Y-координата первой точки. |
| `x2` | `float` | X-координата второй точки. |
| `y2` | `float` | Y-координата второй точки. |
| `x3` | `float` | X-координата третьей вершины. |
| `y3` | `float` | Y-координата третьей вершины. |
| `x4` | `float` | X-координата четвёртой вершины. |
| `y4` | `float` | Y-координата четвёртой вершины. |
| `u1` | `float` | Горизонтальная UV-координата U для вершины 1. |
| `v1` | `float` | Вертикальная UV-координата V для вершины 1. |
| `u2` | `float` | Горизонтальная UV-координата U для вершины 2. |
| `v2` | `float` | Вертикальная UV-координата V для вершины 2. |
| `u3` | `float` | Горизонтальная UV-координата U для вершины 3. |
| `v3` | `float` | Вертикальная UV-координата V для вершины 3. |
| `u4` | `float` | Горизонтальная UV-координата U для вершины 4. |
| `v4` | `float` | Вертикальная UV-координата V для вершины 4. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImageRounded(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView, float)` {: #addimageroundedtexturehandle-float-float-float-float-drawpoint-drawpoint-colorview-float data-toc-label="addImageRounded(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView, float)" }

**Добавить изображение со скруглёнными углами**

Добавляет команду отрисовки текстуры со скруглением углов заданного радиуса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `uvMin` | `DrawPoint` | Верхняя левая UV-координата в пространстве текстуры. |
| `uvMax` | `DrawPoint` | Нижняя правая UV-координата в пространстве текстуры. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |
| `radius` | `float` | Радиус скругления углов в пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addMesh(DrawMesh, TextureHandle)` {: #addmeshdrawmesh-texturehandle data-toc-label="addMesh(DrawMesh, TextureHandle)" }

**Добавить кастомный меш геометрии**

Отправляет низкоуровневый полигональный меш DrawMesh с текстурой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mesh` | `DrawMesh` | Предрассчитанный буфер вершин и индексов DrawMesh. |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, RibbonOptions)` {: #ribbontexturehandle-list-float-float-colorview-ribbonoptions data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, RibbonOptions)" }

**Нарисовать процедурную ленту с опциями**

Отрисовывает текстурированную ленту вдоль полилинии с miter-стыками и опциями [RibbonOptions](<ribbon-options.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `textureLength` | `float` | Длина одного повторяющегося тайла текстуры вдоль пути в локальных единицах. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |
| `options` | `RibbonOptions` | Опции исполнения шейдера или настройки построения ленты [RibbonOptions](<ribbon-options.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView)` {: #ribbontexturehandle-list-float-float-colorview data-toc-label="ribbon(TextureHandle, List, float, float, ColorView)" }

**Нарисовать процедурную ленту (Устарело)**

Отрисовывает процедурную ленту со стандартными настройками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `textureLength` | `float` | Длина одного повторяющегося тайла текстуры вдоль пути в локальных единицах. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, boolean)` {: #ribbontexturehandle-list-float-float-colorview-boolean data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, boolean)" }

**Нарисовать замкнутую процедурную ленту (Устарело)**

Отрисовывает замкнутый контур процедурной ленты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `textureLength` | `float` | Длина одного повторяющегося тайла текстуры вдоль пути в локальных единицах. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |
| `closed` | `boolean` | Флаг замыкания контура ломаной или векторного пути. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean)` {: #ribbontexturehandle-list-float-float-colorview-boolean-boolean data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean)" }

**Нарисовать ленту с ориентацией UV (Устарело)**

Отрисовывает ленту с разворотом осей UV на 90 градусов для вертикальных текстур.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `textureLength` | `float` | Длина одного повторяющегося тайла текстуры вдоль пути в локальных единицах. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |
| `closed` | `boolean` | Флаг замыкания контура ломаной или векторного пути. |
| `swapUv` | `boolean` | Флаг смены осей U и V для вертикальных текстур-лент. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean, boolean, boolean)` {: #ribbontexturehandle-list-float-float-colorview-boolean-boolean-boolean-boolean data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean, boolean, boolean)" }

**Нарисовать ленту с флагами трансформации (Устарело)**

Отрисовывает ленту с разворотом и отражением UV координат.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Дескриптор GPU-ресурса текстуры. |
| `points` | `List` | Список 2D-точек вершин многоугольника или полилинии. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `textureLength` | `float` | Длина одного повторяющегося тайла текстуры вдоль пути в локальных единицах. |
| `tint` | `ColorView` | Цвет тинта ColorView, умножаемый на пиксели текстуры. |
| `closed` | `boolean` | Флаг замыкания контура ломаной или векторного пути. |
| `swapUv` | `boolean` | Флаг смены осей U и V для вертикальных текстур-лент. |
| `flipU` | `boolean` | Флаг горизонтального отражения текстуры внутри тайла. |
| `flipV` | `boolean` | Флаг вертикального отражения текстуры внутри тайла. |

</div>

### Векторные пути и каналы

| Метод | Описание |
| --- | --- |
| [`channelsSplit(int)`](#channelssplitint) | Разделяет поток команд отрисовки на несколько независимых слоёв/каналов. |
| [`channelsMerge()`](#channelsmerge) | Объединяет ранее разделённые каналы обратно в последовательном порядке слоёв. |
| [`channelsSetCurrent(int)`](#channelssetcurrentint) | Переключает индекс активного канала, в который направляются последующие команды. |
| [`path()`](#path) | Возвращает встроенный объект построения векторных путей VectorPath. |
| [`pathClear()`](#pathclear) | Очищает активный векторный путь, удаляя все накопленные вершины и сегменты. |
| [`pathLineTo(DrawPoint)`](#pathlinetodrawpoint) | Добавляет точку-вершину в активный векторный путь. |
| [`pathLineTo(float, float)`](#pathlinetofloat-float) | Добавляет координаты (x, y) в активный векторный путь. |
| [`pathLineToMergeDuplicate(DrawPoint)`](#pathlinetomergeduplicatedrawpoint) | Добавляет точку в путь, игнорируя её, если она совпадает с предыдущей. |
| [`pathLineToMergeDuplicate(float, float)`](#pathlinetomergeduplicatefloat-float) | Добавляет координаты (x, y) в путь, отсекая дубликаты предыдущей вершины. |
| [`pathFillConvex(ColorView)`](#pathfillconvexcolorview) | Заливает активный выпуклый векторный путь указанным цветом ColorView. |
| [`pathStroke(ColorView, boolean, float)`](#pathstrokecolorview-boolean-float) | Обводит активный векторный путь линией заданной толщины и цвета. |
| [`pathArcTo(float, float, float, float, float, int)`](#patharctofloat-float-float-float-float-int) | Добавляет сегмент круговой дуги между начальным и конечным углами. |
| [`pathArcToFast(float, float, float, int, int)`](#patharctofastfloat-float-float-int-int) | Добавляет дугу с использованием предрассчитанной 12-точечной тригонометрической таблицы. |
| [`pathBezierCubicCurveTo(float, float, float, float, float, float, int)`](#pathbeziercubiccurvetofloat-float-float-float-float-float-int) | Добавляет сегмент кубической кривой Безье с двумя контрольными точками и концом. |
| [`pathBezierQuadraticCurveTo(float, float, float, float, int)`](#pathbezierquadraticcurvetofloat-float-float-float-int) | Добавляет сегмент квадратичной кривой Безье с одной контрольной точкой и концом. |
| [`pathRect(float, float, float, float)`](#pathrectfloat-float-float-float) | Добавляет вершины прямоугольника в активный векторный путь. |
| [`pathRect(float, float, float, float, float)`](#pathrectfloat-float-float-float-float) | Добавляет вершины скруглённого прямоугольника с радиусом углов в активный путь. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`channelsSplit(int)` {: #channelssplitint data-toc-label="channelsSplit(int)" }

**Разделить каналы отрисовки**

Разделяет поток команд отрисовки на несколько независимых слоёв/каналов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `count` | `int` | Количество выделяемых независимых каналов отрисовки. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`channelsMerge()` {: #channelsmerge data-toc-label="channelsMerge()" }

**Объединить каналы отрисовки**

Объединяет ранее разделённые каналы обратно в последовательном порядке слоёв.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`channelsSetCurrent(int)` {: #channelssetcurrentint data-toc-label="channelsSetCurrent(int)" }

**Установить активный канал**

Переключает индекс активного канала, в который направляются последующие команды.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `channel` | `int` | Целевой индекс канала для последующих команд отрисовки. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`path()` {: #path data-toc-label="path()" }

**Получить активный VectorPath**

Возвращает встроенный объект построения векторных путей VectorPath.

Возвращает `VectorPath`: Экземпляр активного VectorPath..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathClear()` {: #pathclear data-toc-label="pathClear()" }

**Очистить точки пути**

Очищает активный векторный путь, удаляя все накопленные вершины и сегменты.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineTo(DrawPoint)` {: #pathlinetodrawpoint data-toc-label="pathLineTo(DrawPoint)" }

**Добавить точку в путь**

Добавляет точку-вершину в активный векторный путь.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `point` | `DrawPoint` | Координаты точки вершины для добавления в путь. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineTo(float, float)` {: #pathlinetofloat-float data-toc-label="pathLineTo(float, float)" }

**Добавить координаты в путь**

Добавляет координаты (x, y) в активный векторный путь.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineToMergeDuplicate(DrawPoint)` {: #pathlinetomergeduplicatedrawpoint data-toc-label="pathLineToMergeDuplicate(DrawPoint)" }

**Добавить точку с объединением дубликатов**

Добавляет точку в путь, игнорируя её, если она совпадает с предыдущей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `point` | `DrawPoint` | Координаты точки вершины для добавления в путь. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineToMergeDuplicate(float, float)` {: #pathlinetomergeduplicatefloat-float data-toc-label="pathLineToMergeDuplicate(float, float)" }

**Добавить координаты с объединением дубликатов**

Добавляет координаты (x, y) в путь, отсекая дубликаты предыдущей вершины.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathFillConvex(ColorView)` {: #pathfillconvexcolorview data-toc-label="pathFillConvex(ColorView)" }

**Залить выпуклый путь**

Заливает активный выпуклый векторный путь указанным цветом ColorView.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathStroke(ColorView, boolean, float)` {: #pathstrokecolorview-boolean-float data-toc-label="pathStroke(ColorView, boolean, float)" }

**Обвести контур пути**

Обводит активный векторный путь линией заданной толщины и цвета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Цвет ColorView для сплошной заливки или обводки без аллокаций [Paint](<paint.md>). |
| `closed` | `boolean` | Флаг замыкания контура ломаной или векторного пути. |
| `thickness` | `float` | Толщина линии или контура в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathArcTo(float, float, float, float, float, int)` {: #patharctofloat-float-float-float-float-int data-toc-label="pathArcTo(float, float, float, float, float, int)" }

**Добавить дугу в путь**

Добавляет сегмент круговой дуги между начальным и конечным углами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `minAngle` | `float` | Начальный угол дуги в радианах. |
| `maxAngle` | `float` | Конечный угол дуги в радианах. |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathArcToFast(float, float, float, int, int)` {: #patharctofastfloat-float-float-int-int data-toc-label="pathArcToFast(float, float, float, int, int)" }

**Добавить быструю табличную дугу в путь**

Добавляет дугу с использованием предрассчитанной 12-точечной тригонометрической таблицы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `centerX` | `float` | X-координата центра круга или многоугольника. |
| `centerY` | `float` | Y-координата центра круга или многоугольника. |
| `radius` | `float` | Радиус скругления углов в пикселях. |
| `minSampleOf12` | `int` | Начальный индекс в предрассчитанной 12-точечной таблице круга. |
| `maxSampleOf12` | `int` | Конечный индекс в предрассчитанной 12-точечной таблице круга. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathBezierCubicCurveTo(float, float, float, float, float, float, int)` {: #pathbeziercubiccurvetofloat-float-float-float-float-float-int data-toc-label="pathBezierCubicCurveTo(float, float, float, float, float, float, int)" }

**Добавить кубическую кривую Безье в путь**

Добавляет сегмент кубической кривой Безье с двумя контрольными точками и концом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `controlX1` | `float` | X-координата первой контрольной точки кубической кривой Безье. |
| `controlY1` | `float` | Y-координата первой контрольной точки кубической кривой Безье. |
| `controlX2` | `float` | X-координата второй контрольной точки кубической кривой Безье. |
| `controlY2` | `float` | Y-координата второй контрольной точки кубической кривой Безье. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathBezierQuadraticCurveTo(float, float, float, float, int)` {: #pathbezierquadraticcurvetofloat-float-float-float-int data-toc-label="pathBezierQuadraticCurveTo(float, float, float, float, int)" }

**Добавить квадратичную кривую Безье в путь**

Добавляет сегмент квадратичной кривой Безье с одной контрольной точкой и концом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `controlX` | `float` | X-координата контрольной точки квадратичной кривой Безье. |
| `controlY` | `float` | Y-координата контрольной точки квадратичной кривой Безье. |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `segments` | `int` | Количество сегментов для аппроксимации кривой/многоугольника. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathRect(float, float, float, float)` {: #pathrectfloat-float-float-float data-toc-label="pathRect(float, float, float, float)" }

**Добавить прямоугольник в путь**

Добавляет вершины прямоугольника в активный векторный путь.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathRect(float, float, float, float, float)` {: #pathrectfloat-float-float-float-float data-toc-label="pathRect(float, float, float, float, float)" }

**Добавить скруглённый прямоугольник в путь**

Добавляет вершины скруглённого прямоугольника с радиусом углов в активный путь.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |
| `radius` | `float` | Радиус скругления углов в пикселях. |

</div>

### Отсечение и Scissor

| Метод | Описание |
| --- | --- |
| [`pushClip(float, float, float, float)`](#pushclipfloat-float-float-float) | Устанавливает прямоугольную область отсечения, ограничивающую все последующие вызовы отрисовки. |
| [`pushTextClip(float, float, float, float)`](#pushtextclipfloat-float-float-float) | Устанавливает область отсечения с защитными отступами (bleed) для предотвращения обрезки сглаживания текста. |
| [`popClip()`](#popclip) | Восстанавливает предыдущие границы отсечения, снимая верхний кадр со стека clip. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pushClip(float, float, float, float)` {: #pushclipfloat-float-float-float data-toc-label="pushClip(float, float, float, float)" }

**Установить область отсечения (Scissor Clip)**

Устанавливает прямоугольную область отсечения, ограничивающую все последующие вызовы отрисовки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pushTextClip(float, float, float, float)` {: #pushtextclipfloat-float-float-float data-toc-label="pushTextClip(float, float, float, float)" }

**Установить безопасную для текста область отсечения**

Устанавливает область отсечения с защитными отступами (bleed) для предотвращения обрезки сглаживания текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | X-координата целевой прямоугольной области в локальном пространстве scope. |
| `y` | `float` | Y-координата целевой прямоугольной области в локальном пространстве scope. |
| `width` | `float` | Ширина целевой области в локальных пикселях. |
| `height` | `float` | Высота целевой области в локальных пикселях. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`popClip()` {: #popclip data-toc-label="popClip()" }

**Восстановить область отсечения**

Восстанавливает предыдущие границы отсечения, снимая верхний кадр со стека clip.

</div>

