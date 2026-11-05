---
title: "TextureHandle"
slug: "texture-handle"
category: "Core/Textures"
locale: "ru"
order: 20
---

# TextureHandle

Backend-нейтральный интерфейс текстурного ресурса, предоставляющий размеры, параметры сэмплирования и доступ к нативному объекту.

## Архитектура и независимость от бэкенда

`TextureHandle` — это фундаментальная абстракция UniGUI, представляющая 2D-текстурный ресурс.

### Backend-нейтральный дизайн

Виджеты и кастомные рендереры в UniGUI никогда не работают напрямую с низкоуровневыми графическими дескрипторами (OpenGL texture ID, DirectX дескрипторы или платформенные классы вроде `ResourceLocation` в Minecraft). Все команды отрисовки (`DrawScope#texture`, `DrawScope#nineSlice`) сохраняют ссылку на `TextureHandle`.

| Характеристика | Подход UniGUI | Прямой графический API |
| --- | --- | --- |
| **Переносимость** | Работает без изменений в Vanilla, Canvas, Iris/Oculus или headless-серверах | Жёсткая привязка к конкретному стейту OpenGL или модлоадеру |
| **Жизненный цикл** | Управляется менеджерами ресурсов UniGUI | Ручные вызовы `glGenTextures` и `glDeleteTextures` |
| **Сэмплирование** | Неизменяемые `TextureOptions`, прикреплённые к хэндлу | Глобальные переключения сэмплеров в цикле отрисовки |
| **Батчинг** | O(1) сравнение по стабильному `id()` и сжатым опциям | Сравнение указателей с постоянными сбросами пайплайна |

!!! tip "Стабильные ID для эффективного батчинга"
    Графический бэкенд UniGUI объединяет последовательные вызовы отрисовки текстур в единые батчи, если они используют один и тот же `id()` и одинаковые параметры сэмплирования.

## Размеры и UV-разметка

Методы `TextureHandle#width()` и `TextureHandle#height()` возвращают физическое разрешение текстуры в пикселях (текселях).

### Нормализация UV-координат

При отрисовке части текстуры через `DrawScope#texture` координаты задаются в нормализованном пространстве UV от `0.0f` (верхний левый угол) до `1.0f` (нижний правый):

```java
// Перевод пиксельного прямоугольника в нормализованные UV-координаты:
float u0 = srcPixelX / (float) texture.width();
float v0 = srcPixelY / (float) texture.height();
float uWidth = srcPixelWidth / (float) texture.width();
float vHeight = srcPixelHeight / (float) texture.height();

scope.texture(texture, screenX, screenY, screenW, screenH, u0, v0, uWidth, vHeight, paint);
```

Для автоматического масштабирования с сохранением пропорций без ручных вычислений используйте `TexturePlacement#fit(TextureHandle, RectView, ImageFit)`.

## Параметры сэмплирования и кэширование

Каждый `TextureHandle` предоставляет метод `options()`, возвращающий неизменяемый объект `TextureOptions`.

- **Фильтрация уменьшения и увеличения**: `TextureFilter.NEAREST` для чёткого pixel-art или `TextureFilter.LINEAR` для сглаженного масштабирования.
- **Wrap-режимы**: `TextureWrap.CLAMP_TO_EDGE` для предотвращения наплыва противоположных краёв или `TextureWrap.REPEAT` для бесконечного тайлинга.
- **Компактный ключ стейта**: Метод `TextureOptions#packed()` возвращает 10-битный целочисленный ключ, позволяющий бэкенду мгновенно находить готовые сэмплеры в кэше без выделения памяти.

## Доступ к нативному ресурсу

Метод `TextureHandle#nativeHandle()` возвращает низкоуровневый графический объект текущей платформы:

- В среде Minecraft: обычно `ResourceLocation` или `AbstractTexture`.
- В среде чистого OpenGL / GLFW: `Integer` с именем текстуры OpenGL.
- В тестах и mock-окружении: возвращает `null` или тестовый идентификатор.

> [!WARNING]
> Не выполняйте приведение типов `nativeHandle()` в коде кастомных виджетов. Используйте исключительно методы `DrawScope`, чтобы виджет работал на любых платформах и в headless-тестах.

## Отрисовка текстуры в виджете

```java
public class IconBadgeWidget extends WidgetBase {
    private final TextureHandle icon;

    public IconBadgeWidget(TextureHandle icon) {
        this.icon = Objects.requireNonNull(icon, "icon");
    }

    @Override
    public void render(DrawScope scope) {
        RectView b = layoutBounds();
        // 1. Рисуем подложку
        scope.roundedRectFilled(b.x(), b.y(), b.width(), b.height(), 4.0f, ColorView.rgb(40, 44, 52));
        
        // 2. Отрисовываем иконку по центру с сохранением пропорций
        TexturePlacement placement = TexturePlacement.fit(icon, b, ImageFit.CONTAIN);
        scope.texture(icon, placement.x(), placement.y(), placement.width(), placement.height(), Paint.NONE);
    }
}
```

## API

### Свойства и доступ к текстуре

| Метод | Описание |
| --- | --- |
| [`id()`](#id) | Возвращает стабильный уникальный идентификатор текстурного ресурса. |
| [`width()`](#width) | Возвращает физическую ширину текстуры в пикселях. |
| [`height()`](#height) | Возвращает физическую высоту текстуры в пикселях. |
| [`options()`](#options) | Возвращает параметры фильтрации и wrap-режимов для этой текстуры. |
| [`nativeHandle()`](#nativehandle) | Возвращает низкоуровневый объект текстуры графического бэкенда или null. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`id()` {: #id data-toc-label="id()" }

**Получить ID текстуры**

Возвращает стабильный уникальный идентификатор текстурного ресурса.

Возвращает `String`: Строковый идентификатор для кэширования и батчинга в бэкенде..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`width()` {: #width data-toc-label="width()" }

**Получить ширину**

Возвращает физическую ширину текстуры в пикселях.

Возвращает `int`: Ширина в физических пикселях (текселях)..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`height()` {: #height data-toc-label="height()" }

**Получить высоту**

Возвращает физическую высоту текстуры в пикселях.

Возвращает `int`: Высота в физических пикселях (текселях)..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`options()` {: #options data-toc-label="options()" }

**Получить параметры сэмплирования**

Возвращает параметры фильтрации и wrap-режимов для этой текстуры.

Возвращает `TextureOptions`: Неизменяемый экземпляр [TextureOptions](<texture-options.md>)..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`nativeHandle()` {: #nativehandle data-toc-label="nativeHandle()" }

**Получить нативный дескриптор**

Возвращает низкоуровневый объект текстуры графического бэкенда или null.

Возвращает `Object`: Платформенный объект текстуры (например, GL ID или ResourceLocation) либо null..

</div>

