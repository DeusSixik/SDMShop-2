---
title: "UniHudRender"
slug: "uni-hud-render"
category: "Core/Screens"
locale: "ru"
order: 30
---

# UniHudRender

Точка входа для регистрации и управления слоями внутриигрового HUD.

## Обзор

`UniHudRender` предоставляет статические методы для привязки деревьев виджетов или слоев `UniRenderLayer` к внутриигровому HUD (`UniRenderTarget.HUD`). Для каждого слоя можно настраивать предикат видимости и приоритет отрисовки.

## Приоритеты и предикаты видимости

Слои с большим числом приоритета рисуются поверх слоев с меньшим приоритетом.

- **Приоритет по умолчанию**: 0
- **Предикат видимости**: Управляет условной видимостью каждый кадр (например, скрытие при активном режиме F1).

## Регистрация HUD-виджета

```java
Widget healthBar = new HealthBarWidget();
UniRenderLayerRegistration reg = UniHudRender.register(
    new UniRenderLayer(healthBar),
    ctx -> true,
    10
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`clear()` {: #clear data-toc-label="clear()" }

**clear()**

Очищает и закрывает все зарегистрированные слои HUD.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(UniRenderLayer)` {: #registerunirenderlayer data-toc-label="register(UniRenderLayer)" }

**register([UniRenderLayer](<uni-render-layer.md>))**

Регистрирует настроенный слой [UniRenderLayer](<uni-render-layer.md>) в HUD со стандартной видимостью и приоритетом 0.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Настроенный слой [UniRenderLayer](<uni-render-layer.md>). |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(UniRenderLayer, Predicate, int)` {: #registerunirenderlayer-predicate-int data-toc-label="register(UniRenderLayer, Predicate, int)" }

**register([UniRenderLayer](<uni-render-layer.md>), Predicate<Object>, int)**

Регистрирует [UniRenderLayer](<uni-render-layer.md>) в HUD с условием видимости и приоритетом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Регистрируемый слой отрисовки. |
| `visibility` | `Predicate` | Предикат видимости, проверяемый перед отрисовкой. |
| `priority` | `int` | Приоритет порядка отрисовки. |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(Widget)` {: #registerwidget data-toc-label="register(Widget)" }

**register([Widget](<../../widgets/widget.md>))**

Регистрирует корневой виджет как слой HUD со стандартными настройками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `Widget` | Корневой виджет для отрисовки. |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniHudRender::</span>`register(Widget, UIContext)` {: #registerwidget-uicontext data-toc-label="register(Widget, UIContext)" }

**register([Widget](<../../widgets/widget.md>), UIContext)**

Регистрирует корневой виджет с явным UIContext как слой HUD.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `Widget` | Корневой виджет для отрисовки. |
| `context` | `UIContext` | Привязываемый контекст UIContext. |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

