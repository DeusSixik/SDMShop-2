---
title: "DockingManager"
slug: "docking-manager"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockingManager

Невизуальный контроллер раскладки, управляющий иерархическим деревом докинга, добавлением панелей, разделениями, порядком вкладок и снимками раскладки.

## Обзор

`DockingManager` — невизуальная модель и контроллер дерева раскладки докинга.

Выполняет структурные изменения дерева:
- Добавление панелей со сплитом или объединением во вкладки (`addPane`, `tabPane`, `splitPane`).
- Перемещение вкладок внутри группы (`reorderPane`).
- Открепление панелей в плавающие окна (`detachPaneForFloating`).
- Создание и восстановление снимков раскладки (`snapshot`, `restore`).

## Разделение и группировка панелей во вкладки

```java
dockingRoot.manager().splitPane("editor-1", DockArea.RIGHT, toolPane, 0.3f);
```

## Связанные ссылки

- [DockLayoutSnapshot](<dock-layout-snapshot.md>): Неизменяемый снимок, представляющий рекурсивное дерево раскладки докинга и выбранные панели для сериализации и сохранения состояния.
- [DockNode](<dock-node.md>): Иерархический узел дерева докинга: листовой узел (LEAF) со вкладками [DockPane](<dock-pane.md>) или узел разделения (SPLIT), делящий детей горизонтально или вертикально.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`addPane(DockPane)` {: #addpanedockpane data-toc-label="addPane(DockPane)" }

Добавляет панель в указанную область докинга относительно целевой панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pane` | `DockPane` | Панель [DockPane](<dock-pane.md>) для добавления или изменения. |

Возвращает `DockingManager`: Данный DockingManager для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`closeActivePane()` {: #closeactivepane data-toc-label="closeActivePane()" }

Закрывает текущую активную панель с вызовом стража закрытия.

Возвращает `boolean`: True, если панель была закрыта; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`closePane(String)` {: #closepanestring data-toc-label="closePane(String)" }

Закрывает панель по указанному строковому идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |

Возвращает `DockingManager`: Данный DockingManager для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`containsPane(String)` {: #containspanestring data-toc-label="containsPane(String)" }

Проверяет, открыта ли панель с указанным ID в текущем дереве докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |

Возвращает `boolean`: True, если панель с указанным ID существует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`detachPaneForFloating(String)` {: #detachpaneforfloatingstring data-toc-label="detachPaneForFloating(String)" }

Открепляет панель из дерева докинга для ручного управления плавающим окном.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |

Возвращает `DockPane`: Открепленный экземпляр [DockPane](<dock-pane.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`dockPane(String, String, DockArea)` {: #dockpanestring-string-dockarea data-toc-label="dockPane(String, String, DockArea)" }

Пристыковывает панель в указанную позицию или сторону дерева докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |
| `targetPaneId` | `String` | Идентификатор опорной целевой панели. |
| `area` | `DockArea` | Целевая область докинга. |

Возвращает `boolean`: True, если докинг прошел успешно; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`empty()` {: #empty data-toc-label="empty()" }

Проверяет, пуста ли иерархия докинга и не содержит ли зарегистрированных панелей.

Возвращает `boolean`: True, если дерево докинга не содержит панелей..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`findPane(String)` {: #findpanestring data-toc-label="findPane(String)" }

Находит панель по строковому идентификатору или возвращает null.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |

Возвращает `DockPane`: Найденная панель [DockPane](<dock-pane.md>) или null, если не найдена..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`floatPane(String)` {: #floatpanestring data-toc-label="floatPane(String)" }

Открепляет панель и помещает ее в плавающее окно хоста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |

Возвращает `WindowWidget`: Созданный виджет плавающего окна [WindowWidget](<../../../widgets/feedback/window-widget.md>) с открепленной панелью..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`paneCount()` {: #panecount data-toc-label="paneCount()" }

Возвращает общее количество открытых панелей во всем дереве докинга.

Возвращает `int`: Общее количество панелей во всем дереве докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`panes()` {: #panes data-toc-label="panes()" }

Возвращает неизменяемую коллекцию всех зарегистрированных панелей докинга.

Возвращает `List`: Неизменяемая коллекция всех зарегистрированных панелей..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`reorderPane(String, String)` {: #reorderpanestring-string data-toc-label="reorderPane(String, String)" }

Перемещает вкладку панели в пределах ее листового узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |
| `beforePaneId` | `String` | Идентификатор панели, перед которой выполняется вставка. |

Возвращает `boolean`: True, если порядок вкладок изменился; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`restore(DockLayoutSnapshot, Map)` {: #restoredocklayoutsnapshot-map data-toc-label="restore(DockLayoutSnapshot, Map)" }

Восстанавливает иерархию дерева докинга и активные вкладки из сохраненного снимка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `snapshot` | `DockLayoutSnapshot` | Снимок раскладки для восстановления. |
| `paneRegistry` | `Map` | Карта зарегистрированных панелей по ID. |

Возвращает `RestoreResult`: True, если снимок раскладки был успешно восстановлен..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`rootNode()` {: #rootnode data-toc-label="rootNode()" }

Возвращает корневой узел текущей иерархии докинга.

Возвращает `DockNode`: Корневой узел иерархии докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectNextTab()` {: #selectnexttab data-toc-label="selectNextTab()" }

Переключает выбор на следующую вкладку в активном листовом узле.

Возвращает `boolean`: True, если активная вкладка переключена; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectPane(String)` {: #selectpanestring data-toc-label="selectPane(String)" }

Делает указанную панель активной выбранной вкладкой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `paneId` | `String` | Идентификатор панели. |

Возвращает `DockingManager`: Данный DockingManager для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectPreviousTab()` {: #selectprevioustab data-toc-label="selectPreviousTab()" }

Переключает выбор на предыдущую вкладку в активном листовом узле.

Возвращает `boolean`: True, если активная вкладка переключена; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectedLeaf()` {: #selectedleaf data-toc-label="selectedLeaf()" }

Возвращает текущий активный листовой узел дерева докинга.

Возвращает `DockNode`: Текущий выбранный листовой узел..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectedPane()` {: #selectedpane data-toc-label="selectedPane()" }

Возвращает активную выбранную панель в текущем активном листовом узле.

Возвращает `DockPane`: Текущая выбранная активная панель [DockPane](<dock-pane.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`snapshot()` {: #snapshot data-toc-label="snapshot()" }

Создает неизменяемый снимок текущего состояния раскладки докинга.

Возвращает `DockLayoutSnapshot`: Снятый снимок текущей раскладки докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`splitPane(String, DockArea, DockPane)` {: #splitpanestring-dockarea-dockpane data-toc-label="splitPane(String, DockArea, DockPane)" }

Разделяет листовой узел с targetPaneId вдоль указанной области с коэффициентом 0.5.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetPaneId` | `String` | Идентификатор опорной целевой панели. |
| `area` | `DockArea` | Целевая область докинга. |
| `pane` | `DockPane` | Панель [DockPane](<dock-pane.md>) для добавления или изменения. |

Возвращает `DockingManager`: Данный DockingManager для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`splitPane(String, DockArea, DockPane, float)` {: #splitpanestring-dockarea-dockpane-float data-toc-label="splitPane(String, DockArea, DockPane, float)" }

Разделяет листовой узел с targetPaneId вдоль указанной области с пользовательским коэффициентом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetPaneId` | `String` | Идентификатор опорной целевой панели. |
| `area` | `DockArea` | Целевая область докинга. |
| `pane` | `DockPane` | Панель [DockPane](<dock-pane.md>) для добавления или изменения. |
| `splitRatio` | `float` | Коэффициент разделения пространства. |

Возвращает `DockingManager`: Данный DockingManager для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`tabPane(String, DockPane)` {: #tabpanestring-dockpane data-toc-label="tabPane(String, DockPane)" }

Группирует панель во вкладки с целевой панелью.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `targetPaneId` | `String` | Идентификатор опорной целевой панели. |
| `pane` | `DockPane` | Панель [DockPane](<dock-pane.md>) для добавления или изменения. |

Возвращает `DockingManager`: Данный DockingManager для цепочки вызовов..

</div>

