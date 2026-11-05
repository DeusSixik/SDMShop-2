---
title: "OverlayLayer"
slug: "overlay-layer"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# OverlayLayer

Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.

## Обзор

`OverlayLayer` — высокоуровневый контейнер для отрисовки плавающих визуальных элементов поверх основного интерфейса.

Основные задачи:
- Размещение всплывающих окон, выпадающих списков, контекстных меню, подсказок и плавающих окон.
- Управление модальными затемняющими подложками (scrim) с настраиваемым цветом и рендерером.
- Маршрутизация ввода мыши и клавиатуры с учетом слоев и закрытием по клику вне виджета.
- Взаимодействие с `WindowManager` для иерархии окон рабочего стола.

## Размещение оверлеев и всплывающих виджетов

```java
OverlayLayer overlay = new OverlayLayer(mainContent);
overlay.addOverlay(toastWidget);
```

## Связанные ссылки

- [WindowManager](<../../core/widgets/feedback/window-manager.md>): Менеджер окон, управляющий z-порядком, стеком фокуса, активными состояниями и расположением окон.
- [WindowWidget](<window-widget.md>): Оконный виджет в стиле рабочего стола с поддержкой заголовка, перетаскивания, изменения размера, сворачивания и модальности.

## API

<div class="api-member-card" markdown="1">

### `OverlayLayer()` {: #overlaylayer data-toc-label="OverlayLayer()" }

Создает пустой OverlayLayer.

</div>

<div class="api-member-card" markdown="1">

### `OverlayLayer(Widget)` {: #overlaylayerwidget data-toc-label="OverlayLayer(Widget)" }

Создает OverlayLayer, содержащий базовый виджет контента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | Вложенный виджет контента. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`addOverlay(Widget)` {: #addoverlaywidget data-toc-label="addOverlay(Widget)" }

Добавляет плавающий виджет оверлея на слой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overlay` | `Widget` | Плавающий виджет оверлея для добавления или удаления. |

Возвращает `OverlayLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`applyQueuedMutations()` {: #applyqueuedmutations data-toc-label="applyQueuedMutations()" }

Немедленно применяет отложенные добавления и удаления оверлеев.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`clearOverlays()` {: #clearoverlays data-toc-label="clearOverlays()" }

Удаляет все виджеты оверлеев со слоя.

Возвращает `OverlayLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`content()` {: #content data-toc-label="content()" }

Возвращает базовый виджет контента под стеком оверлеев.

Возвращает `Widget`: Вложенный виджет контента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Устанавливает базовый виджет контента под стеком оверлеев.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | Вложенный виджет контента. |

Возвращает `OverlayLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`modalScrimColor()` {: #modalscrimcolor data-toc-label="modalScrimColor()" }

Возвращает изменяемый цвет затемняющей подложки модального режима.

Возвращает `MutableColor`: Изменяемый цвет затемняющей подложки модального режима..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`modalScrimRenderer()` {: #modalscrimrenderer data-toc-label="modalScrimRenderer()" }

Возвращает пользовательский рендерер затемняющей подложки модального режима.

Возвращает `WidgetRender`: Пользовательский рендерер затемняющей подложки модального режима..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`modalScrimRenderer(WidgetRender)` {: #modalscrimrendererwidgetrender data-toc-label="modalScrimRenderer(WidgetRender)" }

Устанавливает пользовательский рендерер затемняющей подложки модального режима.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `modalScrimRenderer` | `WidgetRender` | Пользовательский рендерер затемняющей подложки модального режима. |

Возвращает `OverlayLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`removeOverlay(Widget)` {: #removeoverlaywidget data-toc-label="removeOverlay(Widget)" }

Удаляет плавающий виджет оверлея со слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overlay` | `Widget` | Плавающий виджет оверлея для добавления или удаления. |

Возвращает `OverlayLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`windowManager()` {: #windowmanager data-toc-label="windowManager()" }

Возвращает [WindowManager](<../../core/widgets/feedback/window-manager.md>), управляющий окнами на этом слое оверлея.

Возвращает `WindowManager`: Менеджер окон, управляющий плавающими окнами в этом слое оверлея..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayLayer::</span>`windows()` {: #windows data-toc-label="windows()" }

Возвращает контроллер [WindowManager](<../../core/widgets/feedback/window-manager.md>), управляющий плавающими окнами.

Возвращает `WindowManager`: Менеджер окон, управляющий плавающими окнами в этом слое оверлея..

</div>

