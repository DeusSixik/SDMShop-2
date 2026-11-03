---
title: "TextureWidget"
slug: "texture-widget"
category: "Widgets/Display"
locale: "ru"
order: 90
---

# TextureWidget

Базовый виджет для отрисовки текстур, UV-координат, 9-slice рамок и дескрипторов изображений.

## Обзор

`TextureWidget` отображает текстуры, представленные дескриптором `TextureHandle`. Поддерживает режимы фильтрации (linear/nearest), режимы повторения (clamp/repeat), мипмаппинг и UV-координаты источника.

## Отрисовка текстуры

```java
TextureWidget icon = new TextureWidget(TextureHandle.of("mod:textures/gui/icon.png"))
        .textureSampling(TextureFilter.NEAREST);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureWidget::</span>`texture(TextureHandle)` {: #texturetexturehandle data-toc-label="texture(TextureHandle)" }

**Дескриптор текстуры**

Возвращает или устанавливает дескриптор текстуры [TextureHandle](<../../core/textures/texture-handle.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `texture` | `TextureHandle` | — |

Возвращает `TextureWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureWidget::</span>`textureSampling(TextureFilter)` {: #texturesamplingtexturefilter data-toc-label="textureSampling(TextureFilter)" }

**Фильтрация текстуры**

Устанавливает режим сглаживания текстуры (NEAREST или LINEAR).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `filter` | `TextureFilter` | — |

Возвращает `TextureWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureWidget::</span>`textureWrap(TextureWrap)` {: #texturewraptexturewrap data-toc-label="textureWrap(TextureWrap)" }

**Режим повторения**

Устанавливает режим повторения текстуры (CLAMP или REPEAT).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wrap` | `TextureWrap` | — |

Возвращает `TextureWidget`.

</div>

