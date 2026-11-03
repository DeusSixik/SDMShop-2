---
title: "TreeViewNode"
slug: "tree-view-node"
category: "Core/Виджеты/Навигация"
locale: "ru"
order: 0
---

# TreeViewNode

Узел иерархической структуры [TreeView](<../../../widgets/navigation/tree-view.md>) с текстом, иконкой, состоянием раскрытия и дочерними узлами.

## Обзор

Узел иерархической структуры [TreeView](<../../../widgets/navigation/tree-view.md>) с текстом, иконкой, состоянием раскрытия и дочерними узлами. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
TreeViewNode node = new TreeViewNode("Folder");
node.folder(true).expanded(true);
node.addChild("file.txt");
```

## Связанные ссылки

- [TreeView](<../../../widgets/navigation/tree-view.md>): Иерархический древовидный контейнер со сворачиваемыми узлами, кастомными иконками, инлайн-редактированием и выбором.

## API

<div class="api-member-card" markdown="1">

### `TreeViewNode()` {: #treeviewnode data-toc-label="TreeViewNode()" }

Создает пустой узел TreeViewNode с пустой текстовой меткой.

</div>

<div class="api-member-card" markdown="1">

### `TreeViewNode(RichText)` {: #treeviewnoderichtext data-toc-label="TreeViewNode(RichText)" }

Создает узел TreeViewNode с форматированной текстовой меткой [RichText](<../../text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Форматированный текст метки. |

</div>

<div class="api-member-card" markdown="1">

### `TreeViewNode(String)` {: #treeviewnodestring data-toc-label="TreeViewNode(String)" }

Создает узел TreeViewNode с текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`addChild(RichText)` {: #addchildrichtext data-toc-label="addChild(RichText)" }

Создает и добавляет дочерний узел с форматированной меткой [RichText](<../../text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Форматированный текст метки. |

Возвращает `TreeViewNode`: Экземпляр дочернего узла дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`addChild(String)` {: #addchildstring data-toc-label="addChild(String)" }

Создает и добавляет дочерний узел с текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

Возвращает `TreeViewNode`: Экземпляр дочернего узла дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`addChild(TreeViewNode)` {: #addchildtreeviewnode data-toc-label="addChild(TreeViewNode)" }

Добавляет существующий узел в качестве дочернего.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `TreeViewNode` | Экземпляр дочернего узла. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`child(int)` {: #childint data-toc-label="child(int)" }

Возвращает дочерний узел по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `TreeViewNode`: Экземпляр дочернего узла дерева..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`childCount()` {: #childcount data-toc-label="childCount()" }

Возвращает количество дочерних узлов.

Возвращает `int`: Количество дочерних узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`children()` {: #children data-toc-label="children()" }

Возвращает неизменяемый список дочерних узлов.

Возвращает `List`: Неизменяемый список дочерних узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`clearChildren()` {: #clearchildren data-toc-label="clearChildren()" }

Удаляет все дочерние узлы.

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`depth()` {: #depth data-toc-label="depth()" }

Возвращает уровень вложенности узла относительно корня.

Возвращает `int`: Уровень вложенности узла..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`emptyFolder()` {: #emptyfolder data-toc-label="emptyFolder()" }

Возвращает true, если узел настроен как пустая папка.

Возвращает `boolean`: true, если настроено как пустая папка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`emptyFolder(boolean)` {: #emptyfolderboolean data-toc-label="emptyFolder(boolean)" }

Устанавливает, отображается ли узел как папка даже при отсутствии детей.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `emptyFolder` | `boolean` | Отображается ли узел как пустая папка. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`expanded()` {: #expanded data-toc-label="expanded()" }

Возвращает true, если узел в данный момент раскрыт.

Возвращает `boolean`: true, если раскрыто; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`expanded(boolean)` {: #expandedboolean data-toc-label="expanded(boolean)" }

Раскрывает или сворачивает узел, перестраивая строки дерева.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `expanded` | `boolean` | Раскрыт ли узел или панель. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`folder()` {: #folder data-toc-label="folder()" }

Возвращает true, если узел явно обозначен как папка.

Возвращает `boolean`: true, если узел обозначен как папка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`folder(boolean)` {: #folderboolean data-toc-label="folder(boolean)" }

Определяет, отображается ли узел как папка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `isFolder` | `boolean` | Обозначен ли узел как папка. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`hasChildren()` {: #haschildren data-toc-label="hasChildren()" }

Возвращает true, если у узла есть один или несколько дочерних узлов.

Возвращает `boolean`: true, если есть дочерние узлы..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`icon()` {: #icon data-toc-label="icon()" }

Возвращает дескриптор текстурной иконки узла.

Возвращает `TextureHandle`: Дескриптор текстурной иконки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`icon(TextureHandle)` {: #icontexturehandle data-toc-label="icon(TextureHandle)" }

Устанавливает текстурную иконку узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `icon` | `TextureHandle` | Идентификатор или дескриптор иконки. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`iconDraw()` {: #icondraw data-toc-label="iconDraw()" }

Возвращает колбэк пользовательской отрисовки иконки узла.

Возвращает `IconDraw`: Колбэк пользовательской отрисовки иконки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`iconDraw(IconDraw)` {: #icondrawicondraw data-toc-label="iconDraw(IconDraw)" }

Устанавливает колбэк пользовательской отрисовки иконки узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `iconDraw` | `IconDraw` | Колбэк пользовательской отрисовки иконки. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`iconSuppressed()` {: #iconsuppressed data-toc-label="iconSuppressed()" }

Возвращает true, если отрисовка иконки у узла явно отключена.

Возвращает `boolean`: true, если отображение иконки подавлено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`insertChild(int, TreeViewNode)` {: #insertchildint-treeviewnode data-toc-label="insertChild(int, TreeViewNode)" }

Вставляет дочерний узел по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |
| `child` | `TreeViewNode` | Экземпляр дочернего узла. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`isAncestor(TreeViewNode)` {: #isancestortreeviewnode data-toc-label="isAncestor(TreeViewNode)" }

Возвращает true, если данный узел является предком проверяемого узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `candidate` | `TreeViewNode` | Проверяемый узел дерева. |

Возвращает `boolean`: true, если данный узел является предком кандидата..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`noIcon()` {: #noicon data-toc-label="noIcon()" }

Явно отключает отображение иконки у данного узла.

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`owner()` {: #owner data-toc-label="owner()" }

Возвращает дерево [TreeView](<../../../widgets/navigation/tree-view.md>), владеющее данным узлом.

Возвращает `TreeView`: Владеющий контейнер [TreeView](<../../../widgets/navigation/tree-view.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`parent()` {: #parent data-toc-label="parent()" }

Возвращает родительский узел в иерархии, или null.

Возвращает `TreeViewNode`: Родительский узел или null для корня..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`removeChild(TreeViewNode)` {: #removechildtreeviewnode data-toc-label="removeChild(TreeViewNode)" }

Удаляет указанный дочерний узел.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `TreeViewNode` | Экземпляр дочернего узла. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`richText()` {: #richtext data-toc-label="richText()" }

Возвращает форматированную текстовую метку [RichText](<../../text/rich-text.md>) узла.

Возвращает `RichText`: Форматированное текстовое представление..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

Устанавливает форматированную текстовую метку [RichText](<../../text/rich-text.md>) узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Форматированный текст метки. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`rowButton()` {: #rowbutton data-toc-label="rowButton()" }

Возвращает кнопку строки узла, или null, если узел не отображается.

Возвращает `Button`: Виджет кнопки строки или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`selectable()` {: #selectable data-toc-label="selectable()" }

Возвращает true, если узел доступен для выбора пользователем.

Возвращает `boolean`: true, если выбор разрешен..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`selectable(boolean)` {: #selectableboolean data-toc-label="selectable(boolean)" }

Устанавливает, может ли узел быть выбран пользователем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectable` | `boolean` | Разрешен ли выбор. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`silentExpanded(boolean)` {: #silentexpandedboolean data-toc-label="silentExpanded(boolean)" }

Устанавливает состояние раскрытия узла без перестроения строк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `expanded` | `boolean` | Раскрыт ли узел или панель. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`text()` {: #text data-toc-label="text()" }

Возвращает текстовое представление метки узла.

Возвращает `String`: Текстовая строка метки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Устанавливает текстовую метку узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`toggleExpanded()` {: #toggleexpanded data-toc-label="toggleExpanded()" }

Переключает состояние раскрытия узла.

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`userData()` {: #userdata data-toc-label="userData()" }

Возвращает произвольные пользовательские данные узла.

Возвращает `Object`: Прикрепленный объект пользовательских данных..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`userData(Class)` {: #userdataclass data-toc-label="userData(Class)" }

Возвращает типизированные пользовательские данные узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `Class` | Строка типа или категории. |

Возвращает `Object`: Типизированные пользовательские данные или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`userData(Object)` {: #userdataobject data-toc-label="userData(Object)" }

Прикрепляет произвольные пользовательские данные к узлу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `userData` | `Object` | Пользовательские данные. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`value()` {: #value data-toc-label="value()" }

Возвращает семантический строковый идентификатор узла.

Возвращает `String`: Семантическое значение идентификатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`value(String)` {: #valuestring data-toc-label="value(String)" }

Устанавливает семантический строковый идентификатор узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `String` | Семантическое значение. |

Возвращает `TreeViewNode`: Текущий экземпляр для цепочки вызовов..

</div>

