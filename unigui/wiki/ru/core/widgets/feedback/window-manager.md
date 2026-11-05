---
title: "WindowManager"
slug: "window-manager"
category: "Core/Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# WindowManager

Менеджер окон, управляющий z-порядком, стеком фокуса, активными состояниями и расположением окон.

## Обзор

`WindowManager` управляет жизненным циклом, z-порядком, стеком фокуса и пространственным положением плавающих окон рабочего стола внутри `OverlayLayer`.

Основные задачи:
- Регистрация, активация и передача фокуса между окнами.
- Управление z-порядком отображения с помощью `bringToFront` и `sendToBack`.
- Стек модальных диалогов, блокирующий взаимодействие с нижележащими окнами до закрытия модального окна.
- Запросы текущего активного окна и перечисление зарегистрированных окон.

## Управление плавающими окнами

```java
WindowManager manager = overlayLayer.windows();
manager.activate(window);
```

## Связанные ссылки

- [OverlayLayer](<../../../widgets/feedback/overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [WindowWidget](<../../../widgets/feedback/window-widget.md>): Оконный виджет в стиле рабочего стола с поддержкой заголовка, перетаскивания, изменения размера, сворачивания и модальности.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`activate(WindowWidget)` {: #activatewindowwidget data-toc-label="activate(WindowWidget)" }

Активирует указанное окно, перемещает его на передний план и передает фокус ввода.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Экземпляр оконного виджета. |

Возвращает `WindowManager`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`activeWindow()` {: #activewindow data-toc-label="activeWindow()" }

Возвращает текущий активный [WindowWidget](<../../../widgets/feedback/window-widget.md>) в фокусе или null, если активных окон нет.

Возвращает `WindowWidget`: Текущее активное окно в фокусе или null, если активных окон нет..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`bringToFront(WindowWidget)` {: #bringtofrontwindowwidget data-toc-label="bringToFront(WindowWidget)" }

Перемещает указанное окно на передний план стека окон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Экземпляр оконного виджета. |

Возвращает `WindowManager`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`host()` {: #host data-toc-label="host()" }

Возвращает родительский [OverlayLayer](<../../../widgets/feedback/overlay-layer.md>), содержащий управляемые плавающие окна.

Возвращает `OverlayLayer`: Родительский слой [OverlayLayer](<../../../widgets/feedback/overlay-layer.md>), содержащий управляемые окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`modalStackDepth()` {: #modalstackdepth data-toc-label="modalStackDepth()" }

Возвращает глубину стека активных модальных окон.

Возвращает `int`: Количество активных модальных окон в стеке модальности..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`register(WindowWidget)` {: #registerwindowwidget data-toc-label="register(WindowWidget)" }

Регистрирует оконный виджет в этом менеджере.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Экземпляр оконного виджета. |

Возвращает `WindowManager`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`registered(WindowWidget)` {: #registeredwindowwidget data-toc-label="registered(WindowWidget)" }

Возвращает true, если окно зарегистрировано в этом менеджере.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Экземпляр оконного виджета. |

Возвращает `boolean`: True, если окно в данный момент зарегистрировано в менеджере..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`topModalWindow()` {: #topmodalwindow data-toc-label="topModalWindow()" }

Возвращает верхнее модальное окно или null, если модальных окон нет.

Возвращает `WindowWidget`: Верхнее активное модальное окно или null, если модальных окон нет..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`unregister(WindowWidget)` {: #unregisterwindowwidget data-toc-label="unregister(WindowWidget)" }

Удаляет регистрацию оконного виджета из этого менеджера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Экземпляр оконного виджета. |

Возвращает `WindowManager`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`windows()` {: #windows data-toc-label="windows()" }

Возвращает неизменяемый список всех зарегистрированных окон.

Возвращает `List`: Неизменяемый список всех зарегистрированных оконных виджетов..

</div>

