---
title: "WindowWidget"
slug: "window-widget"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# WindowWidget

Оконный виджет в стиле рабочего стола с поддержкой заголовка, перетаскивания, изменения размера, сворачивания и модальности.

## Обзор

`WindowWidget` — полнофункциональный контейнер плавающего окна в стиле рабочего стола с заголовком, кнопками управления, перетаскиванием, изменением размера, сворачиванием, поддержкой модальности и интеграцией с системой докинга.

Ключевые возможности:
- Заголовок с поддержкой обычного и форматированного текста, иконки и кнопок управления (свернуть, закрыть).
- Изменение размера по 8 направлениям и углам с настраиваемыми ограничениями минимального размера и стилями углового маркера.
- Ограничение перемещения в пределах родительского контейнера (`constrainToHost`).
- Поддержка модального режима с блокирующим затемнением фона.
- Бесшовная интеграция с деревьями докинга `DockingRoot` и панелями `DockPane`.

## Создание и позиционирование оконного виджета

```java
WindowWidget window = new WindowWidget("Settings", settingsForm);
window.position(100, 100).open();
```

## Связанные ссылки

- [DockingRoot](<../docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.
- [OverlayLayer](<overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [WindowManager](<../../core/widgets/feedback/window-manager.md>): Менеджер окон, управляющий z-порядком, стеком фокуса, активными состояниями и расположением окон.

## API

<div class="api-member-card" markdown="1">

### `WindowWidget()` {: #windowwidget data-toc-label="WindowWidget()" }

Создает пустой WindowWidget с заголовком и размерами по умолчанию.

</div>

<div class="api-member-card" markdown="1">

### `WindowWidget(RichText, Widget)` {: #windowwidgetrichtext-widget data-toc-label="WindowWidget(RichText, Widget)" }

Создает WindowWidget с заголовком [RichText](<../../core/text/rich-text.md>) и вложенным виджетом контента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `RichText` | Компонент заголовка с форматированным текстом [RichText](<../../core/text/rich-text.md>). |
| `content` | `Widget` | Вложенный виджет контента. |

</div>

<div class="api-member-card" markdown="1">

### `WindowWidget(String, Widget)` {: #windowwidgetstring-widget data-toc-label="WindowWidget(String, Widget)" }

Создает WindowWidget с простым текстовым заголовком и вложенным виджетом контента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текстовая строка заголовка. |
| `content` | `Widget` | Вложенный виджет контента. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`acceptsDocking()` {: #acceptsdocking data-toc-label="acceptsDocking()" }

Возвращает true, если это окно может принимать перетаскиваемые в него панели.

Возвращает `boolean`: True, если это окно может принимать перетаскиваемые панели докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`active()` {: #active data-toc-label="active()" }

Возвращает true, если это окно удерживает фокус в своем [WindowManager](<../../core/widgets/feedback/window-manager.md>).

Возвращает `boolean`: True, если окно активно и находится в фокусе..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Позиционирует окно внутри границ родительского контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `bounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`captionless()` {: #captionless data-toc-label="captionless()" }

Возвращает true, если окно отображается без панели заголовка.

Возвращает `boolean`: True, если у окна отсутствует панель заголовка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`close()` {: #close data-toc-label="close()" }

Закрывает окно.

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeButton()` {: #closebutton data-toc-label="closeButton()" }

Возвращает виджет кнопки закрытия в панели заголовка.

Возвращает `Button`: Экземпляр кнопки в панели заголовка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeButtonVisible()` {: #closebuttonvisible data-toc-label="closeButtonVisible()" }

Возвращает true, если кнопка закрытия окна отображается.

Возвращает `boolean`: True, если кнопка закрытия отображается..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeButtonVisible(boolean)` {: #closebuttonvisibleboolean data-toc-label="closeButtonVisible(boolean)" }

Устанавливает видимость кнопки закрытия окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closeButtonVisible` | `boolean` | Видимость кнопки закрытия в заголовке окна. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeOnOutsideClick()` {: #closeonoutsideclick data-toc-label="closeOnOutsideClick()" }

Возвращает true, если клик снаружи приводит к закрытию окна.

Возвращает `boolean`: True, если закрытие по клику вне виджета включено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeOnOutsideClick(boolean)` {: #closeonoutsideclickboolean data-toc-label="closeOnOutsideClick(boolean)" }

Настраивает, закрывается ли окно при клике снаружи.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closeOnOutsideClick` | `boolean` | Закрывать ли виджет автоматически при клике вне его области. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapseButtonVisible()` {: #collapsebuttonvisible data-toc-label="collapseButtonVisible()" }

Возвращает true, если кнопка сворачивания отображается в заголовке.

Возвращает `boolean`: True, если кнопка сворачивания отображается..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapseButtonVisible(boolean)` {: #collapsebuttonvisibleboolean data-toc-label="collapseButtonVisible(boolean)" }

Устанавливает видимость кнопки сворачивания в заголовке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | Флаг целевой видимости. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapsed()` {: #collapsed data-toc-label="collapsed()" }

Возвращает true, если окно в данный момент свернуто.

Возвращает `boolean`: True, если окно в данный момент свернуто..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapsed(boolean)` {: #collapsedboolean data-toc-label="collapsed(boolean)" }

Устанавливает свернутое состояние окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `collapsed` | `boolean` | Свернуто ли окно до панели заголовка. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`commitDockHost()` {: #commitdockhost data-toc-label="commitDockHost()" }

Закрепляет автоматически созданный хост докинга, делая его постоянным.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`constrainToHost()` {: #constraintohost data-toc-label="constrainToHost()" }

Возвращает true, если перемещение окна ограничено границами контейнера.

Возвращает `boolean`: True, если перемещение окна ограничено границами родительского контейнера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`constrainToHost(boolean)` {: #constraintohostboolean data-toc-label="constrainToHost(boolean)" }

Настраивает ограничение перемещения окна границами контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `constrainToHost` | `boolean` | Ограничено ли перемещение окна строго рамками родительского контейнера. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`content()` {: #content data-toc-label="content()" }

Возвращает вложенный виджет контента окна.

Возвращает `Widget`: Вложенный виджет контента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Устанавливает вложенный виджет контента окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | Вложенный виджет контента. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockDocument(String, String, Widget)` {: #dockdocumentstring-string-widget data-toc-label="dockDocument(String, String, Widget)" }

Прикрепляет панель документов во встроенный контейнер докинга этого окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `title` | `String` | Текстовая строка заголовка. |
| `content` | `Widget` | Вложенный виджет контента. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockKind()` {: #dockkind data-toc-label="dockKind()" }

Возвращает тип панели (DOCUMENT или TOOL) при докинге.

Возвращает `DockPaneKind`: Тип панели докинга, настроенный для этого окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockKind(DockPaneKind)` {: #dockkinddockpanekind data-toc-label="dockKind(DockPaneKind)" }

Устанавливает тип классификации панели при докинге.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dockKind` | `DockPaneKind` | Тип исходной панели докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPane(DockPane)` {: #dockpanedockpane data-toc-label="dockPane(DockPane)" }

Прикрепляет панель во встроенный контейнер докинга этого окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pane` | `DockPane` | Экземпляр панели докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPaneId()` {: #dockpaneid data-toc-label="dockPaneId()" }

Возвращает строковый идентификатор панели, представляющей это окно при докинге.

Возвращает `String`: Строковое значение..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPaneId(String)` {: #dockpaneidstring data-toc-label="dockPaneId(String)" }

Устанавливает строковый идентификатор панели при докинге.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dockPaneId` | `String` | Идентификатор исходной панели докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPolicy()` {: #dockpolicy data-toc-label="dockPolicy()" }

Возвращает политику докинга (FULL, GUEST_ONLY, DISABLED).

Возвращает `DockPolicy`: Политика докинга, настроенная для этого окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPolicy(DockPolicy)` {: #dockpolicydockpolicy data-toc-label="dockPolicy(DockPolicy)" }

Устанавливает политику интеграции с системой докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dockPolicy` | `DockPolicy` | Политика размещения докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockRedockLocked()` {: #dockredocklocked data-toc-label="dockRedockLocked()" }

Возвращает true, если повторное прикрепление окна в докинг заблокировано.

Возвращает `boolean`: True, если повторное прикрепление в докинг заблокировано..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockRedockLocked(boolean)` {: #dockredocklockedboolean data-toc-label="dockRedockLocked(boolean)" }

Блокирует или разблокирует возможность повторного прикрепления окна в докинг.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dockRedockLocked` | `boolean` | Заблокировано ли повторное прикрепление окна обратно в докинг. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockSourceId()` {: #docksourceid data-toc-label="dockSourceId()" }

Возвращает идентификатор исходного узла докинга, от которого было отсоединено окно.

Возвращает `String`: Идентификатор исходной панели докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockSourcePane()` {: #docksourcepane data-toc-label="dockSourcePane()" }

Возвращает исходное представление [DockPane](<../../core/widgets/docking/dock-pane.md>) при плавающем состоянии.

Возвращает `DockPane`: Исходная панель докинга, связанная с этим окном..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockSourcePane(DockPane)` {: #docksourcepanedockpane data-toc-label="dockSourcePane(DockPane)" }

Устанавливает исходное представление [DockPane](<../../core/widgets/docking/dock-pane.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pane` | `DockPane` | Экземпляр панели докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockTool(String, String, Widget)` {: #docktoolstring-string-widget data-toc-label="dockTool(String, String, Widget)" }

Прикрепляет панель инструментов во встроенный контейнер докинга этого окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `title` | `String` | Текстовая строка заголовка. |
| `content` | `Widget` | Вложенный виджет контента. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockable()` {: #dockable data-toc-label="dockable()" }

Возвращает true, если это окно может быть прикреплено во внешний контейнер докинга.

Возвращает `boolean`: True, если окно может быть прикреплено к панели докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockable(boolean)` {: #dockableboolean data-toc-label="dockable(boolean)" }

Включает или отключает возможность прикрепления окна в докинг.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dockable` | `boolean` | Возможность прикрепления окна в контейнер докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockingEnabled()` {: #dockingenabled data-toc-label="dockingEnabled()" }

Возвращает true, если функции докинга включены для этого окна.

Возвращает `boolean`: True, если функции докинга включены для этого окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockingEnabled(boolean)` {: #dockingenabledboolean data-toc-label="dockingEnabled(boolean)" }

Включает или отключает интеграцию с системой докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dockingEnabled` | `boolean` | Включена ли поддержка докинга для этого окна. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockingRoot()` {: #dockingroot data-toc-label="dockingRoot()" }

Возвращает внутренний [DockingRoot](<../docking/docking-root.md>), размещенный внутри окна, или null.

Возвращает `DockingRoot`: Внутренний экземпляр [DockingRoot](<../docking/docking-root.md>), управляющий панелями докинга окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`draggable()` {: #draggable data-toc-label="draggable()" }

Возвращает true, если перетаскивание окна за заголовок разрешено.

Возвращает `boolean`: True, если окно можно перетаскивать за заголовок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`draggable(boolean)` {: #draggableboolean data-toc-label="draggable(boolean)" }

Включает или отключает возможность перетаскивания окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `draggable` | `boolean` | Возможность перемещения окна перетаскиванием за заголовок. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

Возвращает true, если окно в данный момент перетаскивается.

Возвращает `boolean`: True, если окно в данный момент перетаскивается пользователем..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`ensureDockHost()` {: #ensuredockhost data-toc-label="ensureDockHost()" }

Инициализирует и возвращает встроенный экземпляр [DockingRoot](<../docking/docking-root.md>) в качестве контента.

Возвращает `DockingRoot`: Внутренний экземпляр [DockingRoot](<../docking/docking-root.md>), управляющий панелями докинга окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`fixedModal()` {: #fixedmodal data-toc-label="fixedModal()" }

Возвращает true, если позиция модального окна зафиксирована.

Возвращает `boolean`: True, если модальное окно зафиксировано на месте..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`fixedModal(boolean)` {: #fixedmodalboolean data-toc-label="fixedModal(boolean)" }

Настраивает, зафиксировано ли положение модального окна без возможности перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `fixedModal` | `boolean` | Заблокировано ли перемещение модального диалога. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerColor()` {: #headercolor data-toc-label="headerColor()" }

Возвращает изменяемый цвет фона панели заголовка окна.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerHeight()` {: #headerheight data-toc-label="headerHeight()" }

Возвращает высоту панели заголовка в пикселях интерфейса.

Возвращает `float`: Высота панели заголовка окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerHeight(float)` {: #headerheightfloat data-toc-label="headerHeight(float)" }

Устанавливает высоту панели заголовка в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `headerHeight` | `float` | Высота панели заголовка окна в пикселях. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerSeparatorColor()` {: #headerseparatorcolor data-toc-label="headerSeparatorColor()" }

Возвращает изменяемый цвет линии-разделителя между заголовком и контентом.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`icon()` {: #icon data-toc-label="icon()" }

Возвращает иконку панели заголовка окна.

Возвращает `UiIcon`: Иконка в заголовке окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`icon(UiIcon)` {: #iconuiicon data-toc-label="icon(UiIcon)" }

Устанавливает иконку панели заголовка окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `icon` | `UiIcon` | Иконка, отображаемая в заголовке окна. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`lastDragRootX()` {: #lastdragrootx data-toc-label="lastDragRootX()" }

Возвращает последнюю экранную координату X курсора во время перетаскивания.

Возвращает `float`: Координата положения окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`lastDragRootY()` {: #lastdragrooty data-toc-label="lastDragRootY()" }

Возвращает последнюю экранную координату Y курсора во время перетаскивания.

Возвращает `float`: Координата положения окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`mergeDockingFrom(WindowWidget)` {: #mergedockingfromwindowwidget data-toc-label="mergeDockingFrom(WindowWidget)" }

Объединяет панели докинга из другого окна во встроенный хост докинга этого окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `source` | `WindowWidget` | Значение параметра для source. |

Возвращает `boolean`: True, если состояние докинга было успешно объединено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`minWindowSize(float, float)` {: #minwindowsizefloat-float data-toc-label="minWindowSize(float, float)" }

Устанавливает минимальные ограничения ширины и высоты при изменении размера окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина в пикселях. |
| `height` | `float` | Высота в пикселях. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`modal()` {: #modal data-toc-label="modal()" }

Возвращает true, если окно открыто в модальном режиме, блокируя нижние слои.

Возвращает `boolean`: True, если окно открыто в модальном режиме..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`modal(boolean)` {: #modalboolean data-toc-label="modal(boolean)" }

Настраивает модальный режим для этого окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `modal` | `boolean` | Работает ли окно как модальный диалог, блокирующий нижние слои. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`open()` {: #open data-toc-label="open()" }

Открывает окно.

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`open(boolean)` {: #openboolean data-toc-label="open(boolean)" }

Устанавливает состояние открытия окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `open` | `boolean` | Флаг целевого состояния открытия. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`openModal()` {: #openmodal data-toc-label="openModal()" }

Открывает это окно в качестве модального диалога.

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`opened()` {: #opened data-toc-label="opened()" }

Возвращает true, если окно в данный момент открыто на слое оверлея.

Возвращает `boolean`: True, если виджет в данный момент открыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`padding()` {: #padding data-toc-label="padding()" }

Возвращает внутренние отступы содержимого окна.

Возвращает `EdgeInsets`: Внутренние отступы контента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

Устанавливает внутренние отступы содержимого окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `padding` | `EdgeInsets` | Внутренние отступы контента. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`position(float, float)` {: #positionfloat-float data-toc-label="position(float, float)" }

Устанавливает координаты окна в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`releaseAutoDockHost()` {: #releaseautodockhost data-toc-label="releaseAutoDockHost()" }

Освобождает автоматически созданный хост докинга, если он пуст.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizable()` {: #resizable data-toc-label="resizable()" }

Возвращает true, если изменение размеров окна пользователем разрешено.

Возвращает `boolean`: True, если размеры окна могут изменяться пользователем..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizable(boolean)` {: #resizableboolean data-toc-label="resizable(boolean)" }

Включает или отключает возможность изменения размеров окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `resizable` | `boolean` | Возможность изменения размеров окна пользователем. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGrip()` {: #resizegrip data-toc-label="resizeGrip()" }

Возвращает активный визуальный стиль углового маркера изменения размера.

Возвращает `ResizeGripStyle`: Визуальный стиль углового маркера изменения размера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGrip(ResizeGripStyle)` {: #resizegripresizegripstyle data-toc-label="resizeGrip(ResizeGripStyle)" }

Устанавливает визуальный стиль углового маркера изменения размера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `resizeGrip` | `ResizeGripStyle` | Визуальный стиль углового маркера изменения размера. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGripVisible()` {: #resizegripvisible data-toc-label="resizeGripVisible()" }

Возвращает true, если угловой маркер изменения размера отображается.

Возвращает `boolean`: True, если угловой маркер изменения размера отображается..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGripVisible(boolean)` {: #resizegripvisibleboolean data-toc-label="resizeGripVisible(boolean)" }

Устанавливает, отображается ли угловой маркер изменения размера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visible` | `boolean` | Флаг целевой видимости. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeHandle()` {: #resizehandle data-toc-label="resizeHandle()" }

Возвращает идентификатор активного маркера изменения размера (например, BOTTOM_RIGHT).

Возвращает `String`: Идентификатор границы или угла, за который выполняется изменение размера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizing()` {: #resizing data-toc-label="resizing()" }

Возвращает true, если в данный момент изменяются размеры окна.

Возвращает `boolean`: True, если окно в данный момент изменяет свой размер..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`richTitle()` {: #richtitle data-toc-label="richTitle()" }

Возвращает компонент заголовка окна [RichText](<../../core/text/rich-text.md>).

Возвращает `RichText`: Компонент заголовка с форматированным текстом [RichText](<../../core/text/rich-text.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`richTitle(RichText)` {: #richtitlerichtext data-toc-label="richTitle(RichText)" }

Устанавливает форматированный заголовок окна [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `RichText` | Компонент заголовка с форматированным текстом [RichText](<../../core/text/rich-text.md>). |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`theme()` {: #theme data-toc-label="theme()" }

Возвращает активную тему [DockTheme](<../../core/widgets/docking/dock-theme.md>), примененную к обрамлению окна.

Возвращает `DockTheme`: Значение [DockTheme](<../../core/widgets/docking/dock-theme.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`theme(DockTheme)` {: #themedocktheme data-toc-label="theme(DockTheme)" }

Применяет пользовательскую тему [DockTheme](<../../core/widgets/docking/dock-theme.md>) к элементам обрамления и управления окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `theme` | `DockTheme` | Конфигурация темы докинга. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`title()` {: #title data-toc-label="title()" }

Возвращает строку заголовка окна.

Возвращает `String`: Текстовая строка заголовка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

Устанавливает простой текстовый заголовок окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текстовая строка заголовка. |

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`titleColor()` {: #titlecolor data-toc-label="titleColor()" }

Возвращает изменяемый цвет текста заголовка окна.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

Переключает открытое и закрытое состояние окна.

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`toggleCollapsed()` {: #togglecollapsed data-toc-label="toggleCollapsed()" }

Переключает свернутое и развернутое состояние окна.

Возвращает `WindowWidget`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`windowX()` {: #windowx data-toc-label="windowX()" }

Возвращает горизонтальную координату X окна.

Возвращает `float`: Координата положения окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`windowY()` {: #windowy data-toc-label="windowY()" }

Возвращает вертикальную координату Y окна.

Возвращает `float`: Координата положения окна..

</div>

