---
title: "TextureRegion"
slug: "texture-region"
category: "Core/Textures"
locale: "ru"
order: 52
---

# TextureRegion

Неизменяемая прямоугольная UV-область внутри родительской текстуры для отрисовки спрайтов из атласов.

## Обзор

TextureRegion функционирует как стандартный [TextureHandle](<texture-handle.md>), представляя прямоугольную под-область атласа или спрайт-листа, что позволяет виджетам отрисовывать отдельные спрайты с правильными пропорциями.

## Фабричные методы

Регионы можно создавать по нормализованным UV-координатам (`ofUv`), пиксельным координатам (`ofPixels`) или номеру ячейки равномерной сетки (`ofCell`).

## Композиция и вложенность UV

Регионы поддерживают прозрачную вложенность: регион от региона или от [AnimatedTexture](<animated-texture.md>) автоматически комбинирует UV-координаты относительно корневой физической текстуры.

## Извлечение спрайта по пикселям

```java
TextureHandle sword = TextureRegion.ofPixels(atlas, 32, 0, 16, 16);
scope.texture(sword, x, y, 32.0f, 32.0f, Paint.NONE);
```

## Извлечение ячейки по индексу сетки

```java
TextureHandle icon = TextureRegion.ofCell(sheet, 8, 8, 35);
scope.texture(icon, x, y, 16.0f, 16.0f, Paint.NONE);
```

## Связанные ссылки

- [AnimatedTexture](<animated-texture.md>): Изменяемая текстура sprite sheet, кадры которой сменяются по игровым тикам из равномерной сетки ячеек.
- [NineSlice](<nine-slice.md>): Определение масштабируемой 9-patch текстуры: 4 неизменяемых угла, 4 растягиваемых края и 1 центральная область.
- [TextureHandle](<texture-handle.md>): Backend-нейтральный интерфейс текстурного ресурса, предоставляющий размеры, параметры сэмплирования и доступ к нативному объекту.

## API

<div class="api-member-card" markdown="1">

### `TextureRegion(TextureHandle, float, float, float, float)` {: #textureregiontexturehandle-float-float-float-float data-toc-label="TextureRegion(TextureHandle, float, float, float, float)" }

Создает регион текстуры в нормализованных UV-координатах относительно родителя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `parent` | `TextureHandle` | Дескриптор родительской текстуры. |
| `u` | `float` | Начальная координата U (0..1). |
| `v` | `float` | Начальная координата V (0..1). |
| `uWidth` | `float` | Ширина области по U (0..1). |
| `vHeight` | `float` | Высота области по V (0..1). |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`base()` {: #base data-toc-label="base()" }

Возвращает базовый дескриптор физической текстуры.

Возвращает `TextureHandle`: Дескриптор базовой текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`height()` {: #height data-toc-label="height()" }

Возвращает высоту региона в физических пикселях относительно родителя.

Возвращает `int`: Высота региона в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`id()` {: #id data-toc-label="id()" }

Возвращает идентификатор ресурса родительской текстуры.

Возвращает `String`: Строковый идентификатор текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`nativeHandle()` {: #nativehandle data-toc-label="nativeHandle()" }

Возвращает низкоуровневый дескриптор платформы родительской текстуры.

Возвращает `Object`: Дескриптор текстуры платформы..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`ofCell(TextureHandle, int, int, int)` {: #ofcelltexturehandle-int-int-int data-toc-label="ofCell(TextureHandle, int, int, int)" }

Фабричный метод извлечения одной ячейки из равномерной сетки sprite sheet по индексу ячейки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `parent` | `TextureHandle` | Дескриптор родительской текстуры. |
| `cellWidth` | `int` | Ширина ячейки сетки в пикселях. |
| `cellHeight` | `int` | Высота ячейки сетки в пикселях. |
| `index` | `int` | Номер ячейки в сетке. |

Возвращает `TextureRegion`: Созданный экземпляр TextureRegion..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`ofPixels(TextureHandle, int, int, int, int)` {: #ofpixelstexturehandle-int-int-int-int data-toc-label="ofPixels(TextureHandle, int, int, int, int)" }

Фабричный метод создания региона по пиксельным координатам и размерам на родительской текстуре.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `parent` | `TextureHandle` | Дескриптор родительской текстуры. |
| `x` | `int` | Левая координата в пикселях. |
| `y` | `int` | Верхняя координата в пикселях. |
| `width` | `int` | Ширина региона в пикселях. |
| `height` | `int` | Высота региона в пикселях. |

Возвращает `TextureRegion`: Созданный экземпляр TextureRegion..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`ofUv(TextureHandle, float, float, float, float)` {: #ofuvtexturehandle-float-float-float-float data-toc-label="ofUv(TextureHandle, float, float, float, float)" }

Фабричный метод создания региона текстуры в нормализованных UV-координатах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `parent` | `TextureHandle` | Дескриптор родительской текстуры. |
| `u` | `float` | Начальная координата U (0..1). |
| `v` | `float` | Начальная координата V (0..1). |
| `uWidth` | `float` | Ширина области по U (0..1). |
| `vHeight` | `float` | Высота области по V (0..1). |

Возвращает `TextureRegion`: Созданный экземпляр TextureRegion..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`options()` {: #options data-toc-label="options()" }

Возвращает параметры выборки родительской текстуры.

Возвращает `TextureOptions`: Параметры выборки текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`parent()` {: #parent data-toc-label="parent()" }

Возвращает дескриптор родительской текстуры, содержащей данный регион.

Возвращает `TextureHandle`: Дескриптор родительской текстуры..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`resolveUv(MutableRect)` {: #resolveuvmutablerect data-toc-label="resolveUv(MutableRect)" }

Вычисляет составные UV-координаты для данного региона относительно корневой физической текстуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `out` | `MutableRect` | Изменяемый прямоугольник для записи вычисленных UV-координат. |

Возвращает `boolean`: true, если вычисление UV выполнено успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`u()` {: #u data-toc-label="u()" }

Возвращает нормализованную начальную координату U внутри родительской текстуры.

Возвращает `float`: Нормализованная координата начала по U..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`uWidth()` {: #uwidth data-toc-label="uWidth()" }

Возвращает нормализованную ширину области по U внутри родительской текстуры.

Возвращает `float`: Нормализованная ширина по U..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`v()` {: #v data-toc-label="v()" }

Возвращает нормализованную начальную координату V внутри родительской текстуры.

Возвращает `float`: Нормализованная координата начала по V..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`vHeight()` {: #vheight data-toc-label="vHeight()" }

Возвращает нормализованную высоту области по V внутри родительской текстуры.

Возвращает `float`: Нормализованная высота по V..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`width()` {: #width data-toc-label="width()" }

Возвращает ширину региона в физических пикселях относительно родителя.

Возвращает `int`: Ширина региона в пикселях..

</div>

