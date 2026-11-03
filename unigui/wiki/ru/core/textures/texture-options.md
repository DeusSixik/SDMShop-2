---
title: "TextureOptions"
slug: "texture-options"
category: "Core/Textures"
locale: "ru"
order: 40
---

# TextureOptions

Неизменяемая конфигурация сэмплирования текстур: min/mag фильтрация, wrap-режимы UV, mipmaps и premultiplied alpha.

## Обзор и неизменяемый дизайн

`TextureOptions` определяет, как графический ускоритель сэмплирует текстуру при отрисовке полигонов, мешей и 9-slice панелей.

### Неизменяемая семантика

`TextureOptions` не владеет ресурсами видеокарты, а лишь описывает параметры сэмплера. Все методы-модификаторы возвращают новый экземпляр `TextureOptions` (или текущий объект, если значение не изменилось), что исключает случайные побочные эффекты:

```java
TextureOptions base = TextureOptions.defaults();
TextureOptions linearWithRepeat = base.sampling(TextureFilter.LINEAR).wrap(TextureWrap.REPEAT);
```

## Режимы фильтрации (Nearest против Linear)

Режимы `TextureFilter` управляют интерполяцией пикселей при масштабировании:

| Режим | Поведение | Где использовать |
| --- | --- | --- |
| `NEAREST` | Выбирает ближайший пиксель без интерполяции. | Pixel-art текстуры Minecraft, чёткие иконки предметов, рамки UI. |
| `LINEAR` | Билинейная интерполяция между 4 соседними текселями. | Высокодетализированный арт, векторная графика, плавный зум. |
| `NEAREST_MIPMAP_NEAREST` | Nearest-выборка из ближайшего mipmap-уровня. | Сильно уменьшенные pixel-art поверхности. |
| `LINEAR_MIPMAP_LINEAR` | Трилинейная фильтрация с плавным переходом между уровнями. | 3D-проекции в мире, удалённые плоскости, защита от муара. |

## Режимы повторения UV (`TextureWrap`)

Определяет поведение сэмплера при выходе UV-координат за пределы диапазона `0.0..1.0`:

- `CLAMP_TO_EDGE`: Прижимает координаты к крайним пикселям. Стандартный режим UniGUI; исключает паразитные цветные полосы по краям рамок.
- `REPEAT`: Бесконечно повторяет текстуру плиткой (тайлинг). Идеально для фоновых узоров, бегущих текстур кабелей и полос.
- `MIRRORED_REPEAT`: Повторяет текстуру с зеркальным отражением каждого второго тайла для создания симметричных паттернов.

## Premultiplied Alpha и Mipmaps

### Premultiplied Alpha
При билинейной фильтрации полупрозрачных текстур стандартный straight alpha может давать тёмные грязные ореолы вокруг контуров из-за смешивания RGB с чёрным цветом при alpha=0. Опция `premultipliedAlpha(true)` переключает шейдер бэкенда на математику premultiplied alpha (`src + dst * (1 - srcAlpha)`).

### Mipmaps
Включает использование предварительно отфильтрованных уменьшенных копий текстуры, устраняя мерцание и рябь (алиасинг) при сильном удалении или сжатии.

## 10-битный сжатый ключ

Все параметры сэмплирования и wrap-режимов упакованы в компактный 10-битный целочисленный ключ (`packed()`):

```text
Биты 0-1: MinFilter (NEAREST=0, LINEAR=1, NEAREST_MIP=2, LINEAR_MIP=3)
Биты 2-3: MagFilter (NEAREST=0, LINEAR=1)
Биты 4-5: WrapS (CLAMP=0, REPEAT=1, MIRRORED=2)
Биты 6-7: WrapT (CLAMP=0, REPEAT=1, MIRRORED=2)
Бит 8:    Флаг Mipmaps (0/1)
Бит 9:    Флаг Premultiplied Alpha (0/1)
```

Благодаря тому, что ключ занимает всего 10 бит (значения от 0 до 1023), бэкенды используют прямой массив (`SamplerState[1024]`) или быстрый switch, обеспечивая мгновенный поиск O(1) **без выделения памяти**.

## Бесшовный тайлинг фона

```java
// Настройка повторяющегося фона
TextureOptions tiledOptions = TextureOptions.defaults()
        .wrap(TextureWrap.REPEAT);

TextureHandle tiledTexture = new SimpleTextureHandle("custom:wallpaper", 32, 32, null, tiledOptions);
// Отрисовка 5x3 плиток по ширине и высоте
scope.texture(tiledTexture, x, y, width, height, 0.0f, 0.0f, 5.0f, 3.0f, Paint.NONE);
```

## Сглаженная иконка с mipmaps

```java
// Сглаженные трилинейные опции для иконок интерфейса
TextureOptions smoothOptions = TextureOptions.linear()
        .mipmaps(true)
        .premultipliedAlpha(true);
```

## API

### Предустановки и стандартные фабрики

| Метод | Описание |
| --- | --- |
| [`defaults()`](#defaults) | Возвращает стандартные настройки: nearest-фильтрация, clamp-to-edge, без mipmaps. |
| [`nearest()`](#nearest) | Возвращает настройки для pixel-perfect выборки без сглаживания. |
| [`linear()`](#linear) | Возвращает настройки для сглаженной линейной фильтрации. |
| [`isDefault()`](#isdefault) | Возвращает true, если опции совпадают со стандартными. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**Получить опции по умолчанию**

Возвращает стандартные настройки: nearest-фильтрация, clamp-to-edge, без mipmaps.

Возвращает `TextureOptions`: Кэшированный стандартный экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`nearest()` {: #nearest data-toc-label="nearest()" }

**Получить Nearest опции**

Возвращает настройки для pixel-perfect выборки без сглаживания.

Возвращает `TextureOptions`: Экземпляр TextureOptions с nearest-фильтрацией..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`linear()` {: #linear data-toc-label="linear()" }

**Получить Linear опции**

Возвращает настройки для сглаженной линейной фильтрации.

Возвращает `TextureOptions`: Экземпляр TextureOptions с linear-фильтрацией..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`isDefault()` {: #isdefault data-toc-label="isDefault()" }

**Проверить настройки по умолчанию**

Возвращает true, если опции совпадают со стандартными.

Возвращает `boolean`: True, если идентично TextureOptions.defaults()..

</div>

### Свойства сэмплера

| Метод | Описание |
| --- | --- |
| [`minFilter()`](#minfilter) | Возвращает фильтр при отрисовке текстуры меньше её размера. |
| [`magFilter()`](#magfilter) | Возвращает фильтр при отрисовке текстуры больше её размера. |
| [`wrapS()`](#wraps) | Возвращает режим повторения по горизонтали (ось U). |
| [`wrapT()`](#wrapt) | Возвращает режим повторения по вертикали (ось V). |
| [`mipmaps()`](#mipmaps) | Возвращает true, если включены mipmaps. |
| [`premultipliedAlpha()`](#premultipliedalpha) | Возвращает true, если текстура использует premultiplied alpha. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`minFilter()` {: #minfilter data-toc-label="minFilter()" }

**Получить фильтр уменьшения**

Возвращает фильтр при отрисовке текстуры меньше её размера.

Возвращает `TextureFilter`: Установленный minification TextureFilter..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`magFilter()` {: #magfilter data-toc-label="magFilter()" }

**Получить фильтр увеличения**

Возвращает фильтр при отрисовке текстуры больше её размера.

Возвращает `TextureFilter`: Установленный magnification TextureFilter..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrapS()` {: #wraps data-toc-label="wrapS()" }

**Получить Wrap S**

Возвращает режим повторения по горизонтали (ось U).

Возвращает `TextureWrap`: Режим повторения TextureWrap по горизонтали..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrapT()` {: #wrapt data-toc-label="wrapT()" }

**Получить Wrap T**

Возвращает режим повторения по вертикали (ось V).

Возвращает `TextureWrap`: Режим повторения TextureWrap по вертикали..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`mipmaps()` {: #mipmaps data-toc-label="mipmaps()" }

**Проверить включение Mipmaps**

Возвращает true, если включены mipmaps.

Возвращает `boolean`: True, если mipmaps активны..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`premultipliedAlpha()` {: #premultipliedalpha data-toc-label="premultipliedAlpha()" }

**Проверить Premultiplied Alpha**

Возвращает true, если текстура использует premultiplied alpha.

Возвращает `boolean`: True, если используется premultiplied alpha..

</div>

### Методы конфигурации

| Метод | Описание |
| --- | --- |
| [`minFilter(TextureFilter)`](#minfiltertexturefilter) | Возвращает копию TextureOptions с заданным фильтром уменьшения. |
| [`magFilter(TextureFilter)`](#magfiltertexturefilter) | Возвращает копию TextureOptions с заданным фильтром увеличения. |
| [`sampling(TextureFilter)`](#samplingtexturefilter) | Устанавливает один фильтр для уменьшения и увеличения одновременно. |
| [`wrap(TextureWrap)`](#wraptexturewrap) | Применяет wrap-режим одновременно к обеим осям (U и V). |
| [`wrap(TextureWrap, TextureWrap)`](#wraptexturewrap-texturewrap) | Применяет независимые wrap-режимы для осей U (S) и V (T). |
| [`mipmaps(boolean)`](#mipmapsboolean) | Включает или выключает использование mipmaps. |
| [`premultipliedAlpha(boolean)`](#premultipliedalphaboolean) | Указывает, содержат ли данные текстуры premultiplied alpha. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`minFilter(TextureFilter)` {: #minfiltertexturefilter data-toc-label="minFilter(TextureFilter)" }

**С фильтром уменьшения**

Возвращает копию TextureOptions с заданным фильтром уменьшения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `filter` | `TextureFilter` | Режим интерполяции TextureFilter. |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`magFilter(TextureFilter)` {: #magfiltertexturefilter data-toc-label="magFilter(TextureFilter)" }

**С фильтром увеличения**

Возвращает копию TextureOptions с заданным фильтром увеличения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `filter` | `TextureFilter` | Режим интерполяции TextureFilter. |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`sampling(TextureFilter)` {: #samplingtexturefilter data-toc-label="sampling(TextureFilter)" }

**С единым фильтром сэмплирования**

Устанавливает один фильтр для уменьшения и увеличения одновременно.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `filter` | `TextureFilter` | Режим интерполяции TextureFilter. |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrap(TextureWrap)` {: #wraptexturewrap data-toc-label="wrap(TextureWrap)" }

**С единым режимом Wrap**

Применяет wrap-режим одновременно к обеим осям (U и V).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wrap` | `TextureWrap` | Режим повторения TextureWrap для UV-координат. |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrap(TextureWrap, TextureWrap)` {: #wraptexturewrap-texturewrap data-toc-label="wrap(TextureWrap, TextureWrap)" }

**С независимыми режимами Wrap**

Применяет независимые wrap-режимы для осей U (S) и V (T).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `s` | `TextureWrap` | Режим TextureWrap по горизонтали (ось U). |
| `t` | `TextureWrap` | Режим TextureWrap по вертикали (ось V). |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`mipmaps(boolean)` {: #mipmapsboolean data-toc-label="mipmaps(boolean)" }

**С настройкой Mipmaps**

Включает или выключает использование mipmaps.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mipmaps` | `boolean` | True для включения mipmaps, false для отключения. |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`premultipliedAlpha(boolean)` {: #premultipliedalphaboolean data-toc-label="premultipliedAlpha(boolean)" }

**С настройкой Premultiplied Alpha**

Указывает, содержат ли данные текстуры premultiplied alpha.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `premultipliedAlpha` | `boolean` | True, если RGB-каналы текстуры пред-умножены на альфу. |

Возвращает `TextureOptions`: Обновлённый экземпляр TextureOptions..

</div>

### Сжатый ключ и кэширование

| Метод | Описание |
| --- | --- |
| [`packed()`](#packed) | Возвращает компактный 10-битный целочисленный ключ всех параметров сэмплера. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`packed()` {: #packed data-toc-label="packed()" }

**Получить сжатый ключ**

Возвращает компактный 10-битный целочисленный ключ всех параметров сэмплера.

Возвращает `int`: 10-битный ключ для кэширования сэмплеров в бэкенде..

</div>

