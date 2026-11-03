---
title: "TreeView"
slug: "tree-view"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# TreeView

Иерархический древовидный контейнер со сворачиваемыми узлами, кастомными иконками, инлайн-редактированием и выбором.

## Обзор

Иерархический древовидный контейнер со сворачиваемыми узлами, кастомными иконками, инлайн-редактированием и выбором. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
TreeView tree = new TreeView();
TreeViewNode root = tree.addRoot("Project");
root.addChild("src");
root.addChild("build.gradle");
```

## Связанные ссылки

- [TreeList](<tree-list.md>): Специализированный древовидный список TreeView для иерархической навигации по путям и файловой структуре.

## API

<div class="api-member-card" markdown="1">

### `TreeView()` {: #treeview data-toc-label="TreeView()" }

Создает пустой древовидный контейнер TreeView.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`addRoot(RichText)` {: #addrootrichtext data-toc-label="addRoot(RichText)" }

Создает и добавляет корневой узел с форматированной меткой [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Форматированный текст метки. |

Возвращает `TreeViewNode`: Экземпляр узла дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`addRoot(String)` {: #addrootstring data-toc-label="addRoot(String)" }

Создает и добавляет корневой узел с текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

Возвращает `TreeViewNode`: Экземпляр узла дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`addRoot(TreeViewNode)` {: #addroottreeviewnode data-toc-label="addRoot(TreeViewNode)" }

Добавляет существующий узел в качестве корневого.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`beginBatch()` {: #beginbatch data-toc-label="beginBatch()" }

Начинает сессию пакетного обновления, откладывая перерасчет компоновки.

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`cancelEditing()` {: #cancelediting data-toc-label="cancelEditing()" }

Отменяет активную сессию инлайн-редактирования без сохранения изменений.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`clearRoots()` {: #clearroots data-toc-label="clearRoots()" }

Удаляет все корневые узлы из дерева.

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`clearSelection()` {: #clearselection data-toc-label="clearSelection()" }

Снимает выбор со всех узлов дерева.

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`editingNode()` {: #editingnode data-toc-label="editingNode()" }

Возвращает редактируемый в данный момент узел, или null.

Возвращает `TreeViewNode`: Редактируемый в данный момент узел или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`endBatch()` {: #endbatch data-toc-label="endBatch()" }

Завершает сессию пакетного обновления, перестраивая строки дерева при наличии изменений.

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`hoverScrollSpeed()` {: #hoverscrollspeed data-toc-label="hoverScrollSpeed()" }

Возвращает скорость прокрутки текста при наведении.

Возвращает `float`: Скорость прокрутки при наведении в пикселях в секунду..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`hoverScrollSpeed(float)` {: #hoverscrollspeedfloat data-toc-label="hoverScrollSpeed(float)" }

Устанавливает скорость прокрутки текста при наведении.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pixelsPerSecond` | `float` | Скорость прокрутки в пикселях в секунду. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`iconMode()` {: #iconmode data-toc-label="iconMode()" }

Возвращает текущий режим отображения иконок дерева.

Возвращает `TreeIconMode`: Режим отображения иконок дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`iconMode(TreeIconMode)` {: #iconmodetreeiconmode data-toc-label="iconMode(TreeIconMode)" }

Устанавливает режим отображения иконок дерева.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `iconMode` | `TreeIconMode` | Режим отображения иконок дерева. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`insertRoot(int, TreeViewNode)` {: #insertrootint-treeviewnode data-toc-label="insertRoot(int, TreeViewNode)" }

Вставляет корневой узел по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`isBatching()` {: #isbatching data-toc-label="isBatching()" }

Возвращает true, если активен режим пакетного обновления.

Возвращает `boolean`: true, если активен пакетный режим обновления..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`isEditing()` {: #isediting data-toc-label="isEditing()" }

Возвращает true, если в данный момент активно инлайн-редактирование метки узла.

Возвращает `boolean`: true, если активно инлайн-редактирование..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`onRowCreated()` {: #onrowcreated data-toc-label="onRowCreated()" }

Возвращает слушателя создания строк дерева.

Возвращает `Consumer`: Результирующее значение..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`onRowCreated(Consumer)` {: #onrowcreatedconsumer data-toc-label="onRowCreated(Consumer)" }

Регистрирует слушателя создания и перепривязки кнопок строк дерева.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Колбэк слушателя события. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Регистрирует слушателя события изменения выбранного узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`removeRoot(TreeViewNode)` {: #removeroottreeviewnode data-toc-label="removeRoot(TreeViewNode)" }

Удаляет указанный корневой узел из дерева.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`root(int)` {: #rootint data-toc-label="root(int)" }

Возвращает корневой узел по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `TreeViewNode`: Корневой узел по указанному индексу..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rootCount()` {: #rootcount data-toc-label="rootCount()" }

Возвращает общее количество корневых узлов дерева.

Возвращает `int`: Количество корневых узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`roots()` {: #roots data-toc-label="roots()" }

Возвращает неизменяемый список корневых узлов верхнего уровня.

Возвращает `List`: Неизменяемый список корневых узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rowTextHoverScrollSpeed()` {: #rowtexthoverscrollspeed data-toc-label="rowTextHoverScrollSpeed()" }

Возвращает скорость прокрутки текста при наведении в пикселях в секунду.

Возвращает `float`: Скорость прокрутки при наведении в пикселях в секунду..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rowTextHoverScrollSpeed(float)` {: #rowtexthoverscrollspeedfloat data-toc-label="rowTextHoverScrollSpeed(float)" }

Устанавливает скорость прокрутки текста при наведении в пикселях в секунду.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pixelsPerSecond` | `float` | Скорость прокрутки в пикселях в секунду. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rowsHost()` {: #rowshost data-toc-label="rowsHost()" }

Возвращает вертикальный контейнер строк кнопок узлов дерева.

Возвращает `VBox`: Контейнер строк кнопок дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`select(TreeViewNode)` {: #selecttreeviewnode data-toc-label="select(TreeViewNode)" }

Выбирает указанный узел и вызывает событие смены выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Возвращает видимый индекс выбранного узла дерева.

Возвращает `int`: Индекс выбранного элемента или -1, если выбор отсутствует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`selectedNode()` {: #selectednode data-toc-label="selectedNode()" }

Возвращает выбранный в данный момент узел дерева, или null.

Возвращает `TreeViewNode`: Выбранный в данный момент узел или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`selectedPath()` {: #selectedpath data-toc-label="selectedPath()" }

Возвращает иерархический индексный путь выбранного узла.

Возвращает `List`: Индексный путь выбранного узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`silentSelect(TreeViewNode)` {: #silentselecttreeviewnode data-toc-label="silentSelect(TreeViewNode)" }

Выбирает указанный узел без генерации события смены выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |

Возвращает `TreeView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`startEditing(TreeViewNode, Consumer)` {: #starteditingtreeviewnode-consumer data-toc-label="startEditing(TreeViewNode, Consumer)" }

Запускает инлайн-редактирование текста узла с настройками по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |
| `onCommit` | `Consumer` | Колбэк подтверждения завершения редактирования. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`startEditing(TreeViewNode, Consumer, Consumer)` {: #starteditingtreeviewnode-consumer-consumer data-toc-label="startEditing(TreeViewNode, Consumer, Consumer)" }

Запускает инлайн-редактирование текста узла с кастомной настройкой поля ввода.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `node` | `TreeViewNode` | Экземпляр узла дерева. |
| `config` | `Consumer` | Колбэк конфигурации текстового поля ввода. |
| `onCommit` | `Consumer` | Колбэк подтверждения завершения редактирования. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`stopEditing()` {: #stopediting data-toc-label="stopEditing()" }

Подтверждает и закрывает активную сессию инлайн-редактирования.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`visibleNodes()` {: #visiblenodes data-toc-label="visibleNodes()" }

Возвращает неизменяемый список всех видимых развернутых узлов дерева.

Возвращает `List`: Неизменяемый список видимых узлов дерева..

</div>

