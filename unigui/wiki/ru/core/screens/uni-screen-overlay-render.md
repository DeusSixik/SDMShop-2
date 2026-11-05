---
title: "UniScreenOverlayRender"
slug: "uni-screen-overlay-render"
category: "Core/Screens"
locale: "ru"
order: 40
---

# UniScreenOverlayRender

Точка входа для оверлеев, отрисовываемых поверх открытых игровых экранов.

## Обзор

`UniScreenOverlayRender` регистрирует слои для цели `UniRenderTarget.SCREEN_OVERLAY`. Эти слои отрисовываются поверх любого активного экрана (инвентарей, контейнеров или кастомных GUI), не нарушая их работу.

## Сценарии использования оверлеев экранов

Оверлеи экранов идеально подходят для компонентов, остающихся видимыми между экранами:

- **Браузеры рецептов и предметов** (боковые панели стиля JEI/REI/EMI).
- **Уведомления и всплывающие окна**, отображаемые поверх меню.
- **Инспекторы отладки** и визуализаторы дерева виджетов.
- **Закреплённые подсказки** или окна помощи.

## Регистрация оверлея экрана

```java
Widget sidebar = new RecipeSidebarWidget();
UniRenderLayerRegistration reg = UniScreenOverlayRender.register(
    new UniRenderLayer(sidebar),
    screen -> true,
    100
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`clear()` {: #clear data-toc-label="clear()" }

**clear()**

Очищает и закрывает все зарегистрированные оверлеи экранов.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(UniRenderLayer)` {: #registerunirenderlayer data-toc-label="register(UniRenderLayer)" }

**register([UniRenderLayer](<uni-render-layer.md>))**

Регистрирует настроенный слой [UniRenderLayer](<uni-render-layer.md>) как оверлей экрана со стандартной видимостью и приоритетом 0.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Настроенный слой [UniRenderLayer](<uni-render-layer.md>). |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(UniRenderLayer, Predicate, int)` {: #registerunirenderlayer-predicate-int data-toc-label="register(UniRenderLayer, Predicate, int)" }

**register([UniRenderLayer](<uni-render-layer.md>), Predicate<Object>, int)**

Регистрирует [UniRenderLayer](<uni-render-layer.md>) как оверлей экрана с условием видимости и приоритетом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `layer` | `UniRenderLayer` | Регистрируемый слой отрисовки. |
| `visibility` | `Predicate` | Предикат видимости, проверяемый перед отрисовкой. |
| `priority` | `int` | Приоритет порядка отрисовки. |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(Widget)` {: #registerwidget data-toc-label="register(Widget)" }

**register([Widget](<../../widgets/widget.md>))**

Регистрирует корневой виджет как оверлей экрана со стандартными настройками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `Widget` | Корневой виджет для отрисовки. |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreenOverlayRender::</span>`register(Widget, UIContext)` {: #registerwidget-uicontext data-toc-label="register(Widget, UIContext)" }

**register([Widget](<../../widgets/widget.md>), UIContext)**

Регистрирует корневой виджет с явным UIContext как оверлей экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `Widget` | Корневой виджет для отрисовки. |
| `context` | `UIContext` | Привязываемый контекст UIContext. |

Возвращает `UniRenderLayerRegistration`: Дескриптор регистрации..

</div>

