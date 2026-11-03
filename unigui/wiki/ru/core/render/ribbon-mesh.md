---
title: "RibbonMesh"
slug: "ribbon-mesh"
category: "Core/Render"
locale: "ru"
order: 40
---

# RibbonMesh

Генератор процедурной текстурированной ленты вдоль полилиний для отрисовки дорог, кабелей, трасс, трубопроводов и связей графов.

## Обзор

`RibbonMesh` разворачивает двумерную полилинию (список центральных точек) в триангулированную ленту заданной ширины. Каждая точка формирует пару вершин слева и справа от оси пути. Соседние пары соединяются двумя треугольниками с прямыми торцами (butt caps) и сглаженными miter-стыками.

## Геометрия и miter-стыки

Внутренние стыки вычисляют усреднённую нормаль соседних сегментов (miter). Чтобы острые углы и резкие развороты не порождали бесконечно длинные шипы, длина miter ограничивается лимитом `miterLimit` (по умолчанию 4.0 половины ширины).

## UV-координаты и ориентация

Текстурные координаты распределяются так: `u` идёт поперёк ленты (`0..1`), а `v` продвигается вдоль пути как `distance / textureLength`. При режиме наложения `REPEAT` текстура бесшовно тайлится без растяжения. Для вертикальных текстур-полосок (например, проводов или кабелей) флаг `swapUv(true)` поворачивает текстуру на 90°, направляя `u` вдоль пути, а `v` поперёк ширины.

## Круглые стыки (Joint Dots)

На предельно острых углах, где miter вырождается, включение `jointDots(true)` добавляет круглые диски (радиусом в половину ширины ленты) на стыках, полностью устраняя зазоры и швы.

## Отсечение и видимость

Построитель ленты поддерживает отсечение по прямоугольнику видимости (`clip(x, y, w, h)`). Сегменты вне области отсечения (с учётом запаса под miter) пропускаются без сброса накопленной дистанции пути, сохраняя непрерывность текстуры при скроллинге.

## Builder API

Создание ленты выполняется через fluent-строитель `RibbonMesh.builder(points)` для тонкой настройки (ширина, длина тайла, лимит miter, замкнутый контур, отсечение, отражение UV) либо через быстрый метод `RibbonMesh.ribbon(...)` со стандартными параметрами.

## Построение текстурированного силового кабеля

```java
List<DrawPoint> path = List.of(
    new DrawPoint(20.0f, 40.0f),
    new DrawPoint(80.0f, 90.0f),
    new DrawPoint(160.0f, 60.0f)
);
DrawMesh cable = RibbonMesh.builder(path)
    .width(8.0f)
    .textureLength(32.0f)
    .swapUv(true)
    .build(ColorView.WHITE);
scope.addMesh(cable, cableTexture);
```

## Замкнутый контур с круглыми стыками

```java
List<DrawPoint> corners = List.of(
    new DrawPoint(50.0f, 50.0f),
    new DrawPoint(250.0f, 50.0f),
    new DrawPoint(200.0f, 180.0f),
    new DrawPoint(80.0f, 150.0f)
);
DrawMesh track = RibbonMesh.builder(corners)
    .width(16.0f)
    .textureLength(48.0f)
    .closed(true)
    .jointDots(true)
    .jointSegments(16)
    .build(ColorView.rgb(200, 200, 200));
scope.addMesh(track, roadTexture);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonMesh::</span>`builder(List)` {: #builderlist data-toc-label="builder(List)" }

**builder(List)**

Создаёт новый fluent builder ленты для указанного пути.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `points` | `List` | Центральная полилиния в координатах назначения; null-точки игнорируются. |

Возвращает `Builder`: Новый экземпляр Builder..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonMesh::</span>`ribbon(List, float, float, ColorView)` {: #ribbonlist-float-float-colorview data-toc-label="ribbon(List, float, float, ColorView)" }

**ribbon(List, float, float, ColorView)**

Строит текстурированную ленту одной операцией с настройками по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `points` | `List` | Центральная полилиния пути. |
| `width` | `float` | Ширина ленты в пикселях назначения. |
| `textureLength` | `float` | Длина одного текстурного тайла вдоль пути. |
| `tint` | `ColorView` | Цвет вершин или null для белого непрозрачного. |

Возвращает `DrawMesh`: Сгенерированный DrawMesh или пустой mesh, если путь содержит менее двух валидных точек..

</div>

