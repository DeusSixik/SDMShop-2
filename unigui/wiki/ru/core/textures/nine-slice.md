---
title: "NineSlice"
slug: "nine-slice"
category: "Core/Textures"
locale: "ru"
order: 50
---

# NineSlice

Определение масштабируемой 9-patch текстуры: 4 неизменяемых угла, 4 растягиваемых края и 1 центральная область.

## Обзор и 9-patch сетка

`NineSlice` — это модель масштабируемых прямоугольных элементов интерфейса (оконные рамки, кнопки, диалоговые панели и тултипы).

### Сетка нарезки 9-Slice

Текстура делится на 9 прямоугольных квадов на основе четырёх пиксельных отступов:

```text
+------------+--------------------+------------+
| Верх-Лево  | Верхний край       | Верх-Право |
| (фикс)     | (растяг по гориз)  | (фикс)     |
+------------+--------------------+------------+
| Левый край | Центр              | Правый край|
| (растяг V) | (растяг по обеим)  | (растяг V) |
+------------+--------------------+------------+
| Низ-Лево   | Нижний край        | Низ-Право  |
| (фикс)     | (растяг по гориз)  | (фикс)     |
+------------+--------------------+------------+
```

Так как 4 угла всегда отрисовываются в масштабе 1:1, графика углов никогда не размывается и не искажается при любых размерах виджета.

## Пиксели текстуры и координаты UI

Все отступы рамок в `NineSlice` (`left`, `top`, `right`, `bottom`) задаются в **физических пикселях исходной текстуры**, а не в экранных координатах UI.

```java
// Нарезка текстуры 32x32 с углами 6x6 пикселей:
NineSlice panel = NineSlice.of(windowTexture, 6.0f);
```

При вызове `DrawScope#nineSlice(panel, x, y, width, height, paint, fillCenter)` движок проецирует эти пиксельные отступы на экранные границы виджета.

## Защита от переполнения (`scaleToFit`)

Если размеры виджета меньше суммы угловых рамок (`width < horizontal()` или `height < vertical()`), стандартная отрисовка привела бы к наложению или инверсии углов.

Метод `NineSlice#scaleToFit(targetWidth, targetHeight)` вычисляет коэффициент масштабирования от `0.0f` до `1.0f`:

```java
float scale = slice.scaleToFit(destWidth, destHeight);
// Автоматически ужимает углы при нехватке места:
float effectiveLeft = slice.left() * scale;
```

Это гарантирует аккуратный вид даже очень маленьких кнопок или компактных вкладок.

## Сплошные панели и полые рамки

Метод `DrawScope#nineSlice` принимает логический флаг `fillCenter`:

- `fillCenter = true`: Отрисовывает все 9 квадов (углы, края и центр). Стандарт для сплошных окон и панелей инвентаря.
- `fillCenter = false`: Пропускает центральный квад (рисуются только 8 пограничных квадов). Идеально для полых рамок выделения и фокус-колец.

## Масштабируемая панель окна

```java
NineSlice dialogSlice = NineSlice.of(dialogTexture, 8.0f);

// Отрисовка сплошного окна с рамкой
scope.nineSlice(dialogSlice, b.x(), b.y(), b.width(), b.height(), Paint.NONE, true);
```

## Полая рамка выделения слота

```java
NineSlice selectionSlice = NineSlice.of(selectionTexture, 3.0f);

// Отрисовка полой рамки без заливки центра
scope.nineSlice(selectionSlice, slot.x() - 1, slot.y() - 1, 20.0f, 20.0f, Paint.NONE, false);
```

## API

### Фабричные методы

| Метод | Описание |
| --- | --- |
| [`of(TextureHandle, float)`](#oftexturehandle-float) | Создаёт NineSlice с одинаковыми отступами рамок со всех четырёх сторон. |
| [`of(TextureHandle, float, float, float, float)`](#oftexturehandle-float-float-float-float) | Создаёт NineSlice с независимыми отступами слева, сверху, справа и снизу. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`of(TextureHandle, float)` {: #oftexturehandle-float data-toc-label="of(TextureHandle, float)" }

**Создать NineSlice с одинаковой рамкой**

Создаёт NineSlice с одинаковыми отступами рамок со всех четырёх сторон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Исходный дескриптор текстуры. |
| `border` | `float` | Одинаковая ширина рамки в пикселях текстуры. |

Возвращает `NineSlice`: Новый экземпляр NineSlice с одинаковой рамкой..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`of(TextureHandle, float, float, float, float)` {: #oftexturehandle-float-float-float-float data-toc-label="of(TextureHandle, float, float, float, float)" }

**Создать NineSlice с произвольными рамками**

Создаёт NineSlice с независимыми отступами слева, сверху, справа и снизу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | Исходный дескриптор текстуры. |
| `left` | `float` | Левый отступ рамки в пикселях текстуры. |
| `top` | `float` | Верхний отступ рамки в пикселях текстуры. |
| `right` | `float` | Правый отступ рамки в пикселях текстуры. |
| `bottom` | `float` | Нижний отступ рамки в пикселях текстуры. |

Возвращает `NineSlice`: Новый экземпляр NineSlice с заданными рамками..

</div>

### Отступы рамок

| Метод | Описание |
| --- | --- |
| [`texture()`](#texture) | Возвращает исходный дескриптор текстуры [TextureHandle](<texture-handle.md>). |
| [`left()`](#left) | Возвращает ширину левой рамки в пикселях текстуры. |
| [`top()`](#top) | Возвращает высоту верхней рамки в пикселях текстуры. |
| [`right()`](#right) | Возвращает ширину правой рамки в пикселях текстуры. |
| [`bottom()`](#bottom) | Возвращает высоту нижней рамки в пикселях текстуры. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`texture()` {: #texture data-toc-label="texture()" }

**Получить исходную текстуру**

Возвращает исходный дескриптор текстуры [TextureHandle](<texture-handle.md>).

Возвращает `TextureHandle`: Экземпляр исходной текстуры [TextureHandle](<texture-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`left()` {: #left data-toc-label="left()" }

**Получить левый отступ**

Возвращает ширину левой рамки в пикселях текстуры.

Возвращает `float`: Левая рамка в пикселях текстуры..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`top()` {: #top data-toc-label="top()" }

**Получить верхний отступ**

Возвращает высоту верхней рамки в пикселях текстуры.

Возвращает `float`: Верхняя рамка в пикселях текстуры..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`right()` {: #right data-toc-label="right()" }

**Получить правый отступ**

Возвращает ширину правой рамки в пикселях текстуры.

Возвращает `float`: Правая рамка в пикселях текстуры..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`bottom()` {: #bottom data-toc-label="bottom()" }

**Получить нижний отступ**

Возвращает высоту нижней рамки в пикселях текстуры.

Возвращает `float`: Нижняя рамка в пикселях текстуры..

</div>

