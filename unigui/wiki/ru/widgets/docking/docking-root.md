---
title: "DockingRoot"
slug: "docking-root"
category: "Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockingRoot

Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## Обзор

`DockingRoot` — корневой визуальный виджет, объединяющий всю систему докинга.

### Основные функции

- Отображает иерархическое дерево узлов с интерактивными разделителями.
- Отрисовывает панели вкладок с кнопками закрытия, иконками и индикаторами активной вкладки.
- Интегрируется с оконными хостами `WindowWidget` для плавающих окон.
- Показывает визуальный предпросмотр сброса и крестообразный индикатор докинга.
- Генерирует события жизненного цикла (`onLayoutChanged`, `onDropPreviewChanged`).

## Настройка DockingRoot и добавление панелей

```java
DockingRoot root = new DockingRoot();
root.addDocument("code-1", "CodeEditor.java", editorWidget);
root.addToolPane("proj-1", "Project", projectTreeWidget, DockArea.LEFT);
```

## Связанные ссылки

- [DockPane](<../../core/widgets/docking/dock-pane.md>): Дескриптор отдельной панели докинга, содержащей виджет содержимого, заголовок вкладки, иконку, признак несохраненных правок и стражи закрытия.
- [DockTheme](<../../core/widgets/docking/dock-theme.md>): Тема оформления системы докинга, настраивающая палитру цветов ([DockColor](<../../core/widgets/docking/dock-color.md>)), геометрию вкладок, разделители, тени и ручки изменения размера окон.
- [DockingManager](<../../core/widgets/docking/docking-manager.md>): Невизуальный контроллер раскладки, управляющий иерархическим деревом докинга, добавлением панелей, разделениями, порядком вкладок и снимками раскладки.

## API

<div class="api-member-card" markdown="1">

### `DockingRoot()` {: #dockingroot data-toc-label="DockingRoot()" }

Конструирует новый виджет DockingRoot со стандартной темой и стилями.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`acceptsDockDrops()` {: #acceptsdockdrops data-toc-label="acceptsDockDrops()" }

Проверяет, разрешен ли прием перетаскиваемых панелей в данный DockingRoot.

Возвращает `boolean`: True, если корень принимает операции сброса при докинге..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`addDocument(String, String, Widget)` {: #adddocumentstring-string-widget data-toc-label="addDocument(String, String, Widget)" }

Создает и добавляет документную панель с указанным содержимым.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `String` | Текст заголовка панели. |
| `content` | `Widget` | Виджет содержимого панели. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`addPane(DockPane)` {: #addpanedockpane data-toc-label="addPane(DockPane)" }

Добавляет панель в раскладку докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pane` | `DockPane` | Панель [DockPane](<../../core/widgets/docking/dock-pane.md>) для добавления. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`addToolPane(String, String, Widget, DockArea)` {: #addtoolpanestring-string-widget-dockarea data-toc-label="addToolPane(String, String, Widget, DockArea)" }

Создает и добавляет инструментальную панель в указанную область докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор панели. |
| `title` | `String` | Текст заголовка панели. |
| `content` | `Widget` | Виджет содержимого панели. |
| `area` | `DockArea` | Область докинга, куда помещается панель. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`allowFloatingOutsideHost()` {: #allowfloatingoutsidehost data-toc-label="allowFloatingOutsideHost()" }

Проверяет, разрешено ли плавающим окнам выходить за границы хоста.

Возвращает `boolean`: True, если плавающие окна могут выходить за границы хоста..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`allowFloatingOutsideHost(boolean)` {: #allowfloatingoutsidehostboolean data-toc-label="allowFloatingOutsideHost(boolean)" }

Задает, могут ли плавающие окна выходить за границы хоста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `allowFloatingOutsideHost` | `boolean` | Разрешено ли плавающим окнам выходить за границы хоста. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`clearForeignWindowDropPreview(WindowWidget)` {: #clearforeignwindowdroppreviewwindowwidget data-toc-label="clearForeignWindowDropPreview(WindowWidget)" }

Очищает активный предпросмотр сброса внешнего плавающего окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Перетаскиваемое плавающее окно. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`closeActivePane()` {: #closeactivepane data-toc-label="closeActivePane()" }

Закрывает текущую выбранную активную вкладку.

Возвращает `boolean`: True, если панель была закрыта; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`crossCellRect(RectView, DockArea)` {: #crosscellrectrectview-dockarea data-toc-label="crossCellRect(RectView, DockArea)" }

Вычисляет экранные границы одной из ячеек крестообразного индикатора сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `leafBounds` | `RectView` | Экранные границы листового узла докинга. |
| `area` | `DockArea` | Область докинга, куда помещается панель. |

Возвращает `MutableRect`: Прямоугольник границ целевой ячейки докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockDragging()` {: #dockdragging data-toc-label="dockDragging()" }

Проверяет, активен ли в данный момент жест перетаскивания вкладки докинга.

Возвращает `boolean`: True, если операция перетаскивания докинга активна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockDropPreview()` {: #dockdroppreview data-toc-label="dockDropPreview()" }

Возвращает текущее намерение предпросмотра сброса при перетаскивании.

Возвращает `DockDropIntent`: Текущее намерение предпросмотра сброса или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockForeignWindow(WindowWidget, float, float)` {: #dockforeignwindowwindowwidget-float-float data-toc-label="dockForeignWindow(WindowWidget, float, float)" }

Интегрирует плавающее окно в дерево докинга в указанную область.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Перетаскиваемое плавающее окно. |
| `rootX` | `float` | Корневая координата X указателя. |
| `rootY` | `float` | Корневая координата Y указателя. |

Возвращает `boolean`: True, если панель была успешно пристыкована; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockingManager()` {: #dockingmanager data-toc-label="dockingManager()" }

Возвращает связанный экземпляр менеджера раскладки [DockingManager](<../../core/widgets/docking/docking-manager.md>).

Возвращает `DockingManager`: Лежащий в основе менеджер [DockingManager](<../../core/widgets/docking/docking-manager.md>), управляющий раскладкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dragController()` {: #dragcontroller data-toc-label="dragController()" }

Возвращает контроллер жестов перетаскивания вкладок.

Возвращает `DockDragController`: Контроллер [DockDragController](<../../core/widgets/docking/dock-drag-controller.md>), управляющий жестами вкладок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dropPreviewRenderer()` {: #droppreviewrenderer data-toc-label="dropPreviewRenderer()" }

Возвращает делегат отрисовщика предпросмотра сброса или null.

Возвращает `WidgetRender`: Запрошенный отрисовщик..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dropPreviewRenderer(WidgetRender)` {: #droppreviewrendererwidgetrender data-toc-label="dropPreviewRenderer(WidgetRender)" }

Задает пользовательский делегат отрисовщика предпросмотра сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dropPreviewRenderer` | `WidgetRender` | Пользовательский отрисовщик предпросмотра сброса. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`findPane(String)` {: #findpanestring data-toc-label="findPane(String)" }

Находит панель по уникальному идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели для поиска или выбора. |

Возвращает `DockPane`: Найденная панель [DockPane](<../../core/widgets/docking/dock-pane.md>) или null, если не найдена..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`finishForeignWindowDrop(WindowWidget, float, float)` {: #finishforeignwindowdropwindowwidget-float-float data-toc-label="finishForeignWindowDrop(WindowWidget, float, float)" }

Завершает операцию сброса внешнего окна с интеграцией в дерево докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Перетаскиваемое плавающее окно. |
| `rootX` | `float` | Корневая координата X указателя. |
| `rootY` | `float` | Корневая координата Y указателя. |

Возвращает `boolean`: True, если панель была успешно пристыкована; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`floatingWindowsRedockLocked()` {: #floatingwindowsredocklocked data-toc-label="floatingWindowsRedockLocked()" }

Проверяет, заблокирован ли повторный докинг для плавающих окон этого корня.

Возвращает `boolean`: True, если плавающие окна заблокированы от повторного докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`floatingWindowsRedockLocked(boolean)` {: #floatingwindowsredocklockedboolean data-toc-label="floatingWindowsRedockLocked(boolean)" }

Блокирует или разблокирует возможность повторного докинга для плавающих окон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `floatingWindowsRedockLocked` | `boolean` | Заблокирован ли повторный докинг плавающих окон. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`lastFloatingWindow()` {: #lastfloatingwindow data-toc-label="lastFloatingWindow()" }

Возвращает последнее оторванное плавающее окно или null.

Возвращает `WindowWidget`: Последнее оторванное плавающее окно или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`manager()` {: #manager data-toc-label="manager()" }

Синоним для получения экземпляра менеджера [DockingManager](<../../core/widgets/docking/docking-manager.md>).

Возвращает `DockingManager`: Лежащий в основе менеджер [DockingManager](<../../core/widgets/docking/docking-manager.md>), управляющий раскладкой..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDragEnded(EventListener)` {: #ondragendedeventlistener data-toc-label="onDragEnded(EventListener)" }

Регистрирует слушатель событий завершения перетаскивания вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения предпросмотра сброса. |

Возвращает `EventSubscription`: Токен подписки для отмены регистрации слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDragMoved(EventListener)` {: #ondragmovedeventlistener data-toc-label="onDragMoved(EventListener)" }

Регистрирует слушатель событий перемещения перетаскиваемой вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения предпросмотра сброса. |

Возвращает `EventSubscription`: Токен подписки для отмены регистрации слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDragStarted(EventListener)` {: #ondragstartedeventlistener data-toc-label="onDragStarted(EventListener)" }

Регистрирует слушатель событий начала перетаскивания вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения предпросмотра сброса. |

Возвращает `EventSubscription`: Токен подписки для отмены регистрации слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDropPreviewChanged(EventListener)` {: #ondroppreviewchangedeventlistener data-toc-label="onDropPreviewChanged(EventListener)" }

Регистрирует слушатель событий изменения предпросмотра сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения предпросмотра сброса. |

Возвращает `EventSubscription`: Токен подписки для отмены регистрации слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onLayoutChanged(EventListener)` {: #onlayoutchangedeventlistener data-toc-label="onLayoutChanged(EventListener)" }

Регистрирует слушатель событий изменения раскладки дерева докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения предпросмотра сброса. |

Возвращает `EventSubscription`: Токен подписки для отмены регистрации слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onLayoutRestored(EventListener)` {: #onlayoutrestoredeventlistener data-toc-label="onLayoutRestored(EventListener)" }

Регистрирует слушатель событий успешного восстановления раскладки из снимка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Слушатель событий изменения предпросмотра сброса. |

Возвращает `EventSubscription`: Токен подписки для отмены регистрации слушателя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`paneRenderer()` {: #panerenderer data-toc-label="paneRenderer()" }

Возвращает пользовательский делегат отрисовщика панелей или null.

Возвращает `WidgetRender`: Запрошенный отрисовщик..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`paneRenderer(WidgetRender)` {: #panerendererwidgetrender data-toc-label="paneRenderer(WidgetRender)" }

Задает пользовательский делегат отрисовщика панелей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneRenderer` | `WidgetRender` | Пользовательский отрисовщик предпросмотра сброса. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`previewForeignWindowDrop(WindowWidget, float, float)` {: #previewforeignwindowdropwindowwidget-float-float data-toc-label="previewForeignWindowDrop(WindowWidget, float, float)" }

Вычисляет и активирует предпросмотр сброса для перетаскиваемого внешнего плавающего окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `window` | `WindowWidget` | Перетаскиваемое плавающее окно. |
| `rootX` | `float` | Корневая координата X указателя. |
| `rootY` | `float` | Корневая координата Y указателя. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`restoreLayout(DockLayoutSnapshot, Map)` {: #restorelayoutdocklayoutsnapshot-map data-toc-label="restoreLayout(DockLayoutSnapshot, Map)" }

Восстанавливает раскладку докинга из снимка с использованием реестра панелей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `snapshot` | `DockLayoutSnapshot` | Снимок раскладки для восстановления. |
| `paneRegistry` | `Map` | Реестр соответствия ID панелей экземплярам [DockPane](<../../core/widgets/docking/dock-pane.md>). |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`rootNode()` {: #rootnode data-toc-label="rootNode()" }

Возвращает корневой узел текущей иерархии дерева докинга.

Возвращает `DockNode`: Корневой узел дерева докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`selectNextTab()` {: #selectnexttab data-toc-label="selectNextTab()" }

Переключает выбор на следующую вкладку в текущей группе.

Возвращает `boolean`: True, если активная вкладка изменилась; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`selectPane(String)` {: #selectpanestring data-toc-label="selectPane(String)" }

Делает указанную панель активной выбранной вкладкой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели для поиска или выбора. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`selectPreviousTab()` {: #selectprevioustab data-toc-label="selectPreviousTab()" }

Переключает выбор на предыдущую вкладку в текущей группе.

Возвращает `boolean`: True, если активная вкладка изменилась; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleRenderer()` {: #splithandlerenderer data-toc-label="splitHandleRenderer()" }

Возвращает пользовательский делегат отрисовщика разделителей или null.

Возвращает `WidgetRender`: Запрошенный отрисовщик..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleRenderer(WidgetRender)` {: #splithandlerendererwidgetrender data-toc-label="splitHandleRenderer(WidgetRender)" }

Задает пользовательский делегат отрисовщика разделителей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `splitHandleRenderer` | `WidgetRender` | Пользовательский отрисовщик предпросмотра сброса. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleStyle()` {: #splithandlestyle data-toc-label="splitHandleStyle()" }

Возвращает визуальный стиль отрисовки разделителей.

Возвращает `SplitHandleStyle`: Текущий визуальный стиль разделителей..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleStyle(SplitHandleStyle)` {: #splithandlestylesplithandlestyle data-toc-label="splitHandleStyle(SplitHandleStyle)" }

Задает визуальный стиль отрисовки разделителей (BLOCK или SEAM).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `splitHandleStyle` | `SplitHandleStyle` | Визуальный стиль разделителей. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleThickness()` {: #splithandlethickness data-toc-label="splitHandleThickness()" }

Возвращает толщину разделителей в пикселях.

Возвращает `float`: Толщина разделителя в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleThickness(float)` {: #splithandlethicknessfloat data-toc-label="splitHandleThickness(float)" }

Задает толщину разделителей в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `splitHandleThickness` | `float` | Толщина разделителей в пикселях. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitPane(String, DockArea, DockPane)` {: #splitpanestring-dockarea-dockpane data-toc-label="splitPane(String, DockArea, DockPane)" }

Разделяет листовой узел с targetPaneId вдоль указанной области.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetPaneId` | `String` | ID опорной панели для разделения или объединения во вкладки. |
| `area` | `DockArea` | Область докинга, куда помещается панель. |
| `pane` | `DockPane` | Панель [DockPane](<../../core/widgets/docking/dock-pane.md>) для добавления. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitPane(String, DockArea, DockPane, float)` {: #splitpanestring-dockarea-dockpane-float data-toc-label="splitPane(String, DockArea, DockPane, float)" }

Разделяет листовой узел с targetPaneId вдоль указанной области с заданным коэффициентом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetPaneId` | `String` | ID опорной панели для разделения или объединения во вкладки. |
| `area` | `DockArea` | Область докинга, куда помещается панель. |
| `pane` | `DockPane` | Панель [DockPane](<../../core/widgets/docking/dock-pane.md>) для добавления. |
| `splitRatio` | `float` | Коэффициент разделения области. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabBounds(String)` {: #tabboundsstring data-toc-label="tabBounds(String)" }

Вычисляет экранные прямоугольные границы заголовка указанной вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели для поиска или выбора. |

Возвращает `RectView`: Экранные границы заголовка запрошенной вкладки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabCloseBounds(String)` {: #tabcloseboundsstring data-toc-label="tabCloseBounds(String)" }

Вычисляет экранные прямоугольные границы кнопки закрытия указанной вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели для поиска или выбора. |

Возвращает `RectView`: Экранные границы кнопки закрытия вкладки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabHeight()` {: #tabheight data-toc-label="tabHeight()" }

Возвращает высоту полосы вкладок в пикселях.

Возвращает `float`: Высота панели вкладок в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabHeight(float)` {: #tabheightfloat data-toc-label="tabHeight(float)" }

Задает высоту полосы вкладок в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tabHeight` | `float` | Высота заголовков вкладок в пикселях. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabPane(String, DockPane)` {: #tabpanestring-dockpane data-toc-label="tabPane(String, DockPane)" }

Группирует новую панель во вкладки с целевой панелью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetPaneId` | `String` | ID опорной панели для разделения или объединения во вкладки. |
| `pane` | `DockPane` | Панель [DockPane](<../../core/widgets/docking/dock-pane.md>) для добавления. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tearOffOnDragOut()` {: #tearoffondragout data-toc-label="tearOffOnDragOut()" }

Проверяет, приводит ли вытягивание вкладки за пределы области к отрыву в плавающее окно.

Возвращает `boolean`: True, если перетаскивание вкладки наружу создает плавающее окно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tearOffOnDragOut(boolean)` {: #tearoffondragoutboolean data-toc-label="tearOffOnDragOut(boolean)" }

Включает или выключает автоматический отрыв вкладок в плавающие окна при сбросе вне док-зон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tearOffOnDragOut` | `boolean` | Отрывать ли вкладку в плавающее окно при вытягивании наружу. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`theme()` {: #theme data-toc-label="theme()" }

Возвращает активную тему [DockTheme](<../../core/widgets/docking/dock-theme.md>) данного корня.

Возвращает `DockTheme`: Текущая тема оформления докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`theme(DockTheme)` {: #themedocktheme data-toc-label="theme(DockTheme)" }

Применяет пользовательскую тему [DockTheme](<../../core/widgets/docking/dock-theme.md>) к данному корню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `theme` | `DockTheme` | Конфигурация темы докинга. |

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`useDefaultDockingRenderers()` {: #usedefaultdockingrenderers data-toc-label="useDefaultDockingRenderers()" }

Сбрасывает все отрисовщики на стандартные встроенные реализации.

Возвращает `DockingRoot`: Данный DockingRoot для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`windowDragZoneAt(float, float)` {: #windowdragzoneatfloat-float data-toc-label="windowDragZoneAt(float, float)" }

Проверяет, попадает ли указатель в зону перетаскивания заголовка окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rootX` | `float` | Корневая координата X указателя. |
| `rootY` | `float` | Корневая координата Y указателя. |

Возвращает `boolean`: True, если координаты указателя попали в зону перетаскивания окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`windowHost()` {: #windowhost data-toc-label="windowHost()" }

Возвращает родительский хост [WindowWidget](<../feedback/window-widget.md>) или null, если корень верхнего уровня.

Возвращает `WindowWidget`: Виджет-хост, содержащий плавающие окна..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`windowHost(WindowWidget)` {: #windowhostwindowwidget data-toc-label="windowHost(WindowWidget)" }

Привязывает данный корень докинга к виджету-хосту [WindowWidget](<../feedback/window-widget.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `windowHost` | `WindowWidget` | Виджет-хост для плавающих окон. |

</div>

