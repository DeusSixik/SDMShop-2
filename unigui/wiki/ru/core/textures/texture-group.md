---
title: "TextureGroup"
slug: "texture-group"
category: "Core/Textures"
locale: "ru"
order: 60
---

# TextureGroup

Упорядоченная композитная коллекция слоёв TextureLayer, отрисовываемых последовательно в общих границах.

## Обзор и композиция слоёв

`TextureGroup` объединяет несколько дескрипторов `TextureLayer` в единый визуальный ресурс.

### Многослойная архитектура поверхностей

Вместо ручного управления разрозненными текстурами в коде виджета, `TextureGroup` объединяет слои в одну структуру:
- Каждый слой хранит свой `TextureHandle`, UV-координаты, tint-цвет, `ImageFit` и 9-slice рамки.
- Отрисовка происходит строго по порядку от индекса 0 до N-1 (фон, рамка, декоративные узоры, блики).
- Слои с `null`-текстурой автоматически пропускаются без лишних накладных расходов.

## Построение через Builder

Создать `TextureGroup` можно через фабричный метод или построитель `TextureGroup#builder()`:

```java
TextureGroup group = TextureGroup.builder()
        .add(backgroundTexture) // стандартные настройки слоя
        .add(new TextureLayer(frameTexture).tint(ColorView.rgb(200, 200, 200)))
        .build();
```

## Создание многослойной поверхности

```java
// Создание группы с фоновой подложкой и золотой рамкой
TextureGroup cardSurface = TextureGroup.builder()
        .add(new TextureLayer(basePlate).fit(ImageFit.NINE_SLICE).sliceBorders(SliceBorders.all(8.0f)))
        .add(new TextureLayer(goldTrim).tint(ColorView.rgb(255, 215, 0)))
        .build();
```

## API

### Фабричные методы и построитель

| Метод | Описание |
| --- | --- |
| [`create(TextureLayer[])`](#createtexturelayer) | Создаёт TextureGroup из переданного массива слоёв в порядке отрисовки. |
| [`TextureGroup(List)`](#texturegrouplist) | Создаёт TextureGroup, копируя переданный список слоёв. |
| [`builder()`](#builder) | Возвращает новый объект Builder для построения группы. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureGroup::</span>`create(TextureLayer[])` {: #createtexturelayer data-toc-label="create(TextureLayer[])" }

**Создать из массива слоёв**

Создаёт TextureGroup из переданного массива слоёв в порядке отрисовки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layers` | `TextureLayer[]` | Последовательность текстурных слоёв для композиции. |

Возвращает `TextureGroup`: Новый неизменяемый экземпляр TextureGroup..

</div>

<div class="api-member-card" markdown="1">

#### `TextureGroup(List)` {: #texturegrouplist data-toc-label="TextureGroup(List)" }

**Создать со списком слоёв**

Создаёт TextureGroup, копируя переданный список слоёв.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layers` | `List` | Последовательность текстурных слоёв для композиции. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureGroup::</span>`builder()` {: #builder data-toc-label="builder()" }

**Создать Builder**

Возвращает новый объект Builder для построения группы.

Возвращает `Builder`: Новый экземпляр TextureGroup.Builder..

</div>

### Доступ к слоям

| Метод | Описание |
| --- | --- |
| [`layers()`](#layers) | Возвращает неизменяемый список слоёв в порядке отрисовки. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureGroup::</span>`layers()` {: #layers data-toc-label="layers()" }

**Получить список слоёв**

Возвращает неизменяемый список слоёв в порядке отрисовки.

Возвращает `List`: Неизменяемый список слоёв TextureLayer..

</div>

