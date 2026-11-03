---
title: "MenuBar"
slug: "menu-bar"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# MenuBar

Горизонтальная строка меню верхнего уровня, содержащая выпадающие меню с привязкой к командам и координацией всплывающих списков.

## Обзор

Горизонтальная строка меню верхнего уровня, содержащая выпадающие меню с привязкой к командам и координацией всплывающих списков. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
MenuBar menuBar = new MenuBar();
menuBar.menu(fileMenu).menu(editMenu);
```

## Связанные ссылки

- [Menu](<../../core/widgets/navigation/menu.md>): Всплывающее или выпадающее меню, содержащее вертикальный список командных пунктов меню и разделителей.
- [MenuItem](<../../core/widgets/navigation/menu-item.md>): Выбираемый элемент меню, поддерживающий обычные действия, флажки, команды, горячие клавиши или каскадные подменю.

## API

<div class="api-member-card" markdown="1">

### `MenuBar()` {: #menubar data-toc-label="MenuBar()" }

Создает горизонтальную строку меню MenuBar.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`clearMenus()` {: #clearmenus data-toc-label="clearMenus()" }

Очищает все меню из строки меню.

Возвращает `MenuBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`closeOpenMenu()` {: #closeopenmenu data-toc-label="closeOpenMenu()" }

Закрывает текущее открытое выпадающее меню.

Возвращает `MenuBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`commandManager()` {: #commandmanager data-toc-label="commandManager()" }

Возвращает менеджер команд CommandManager строки меню.

Возвращает `CommandManager`: Связанный экземпляр CommandManager..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`commandManager(CommandManager)` {: #commandmanagercommandmanager data-toc-label="commandManager(CommandManager)" }

Устанавливает менеджер команд CommandManager для строки меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandManager` | `CommandManager` | Экземпляр CommandManager. |

Возвращает `MenuBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menu(Menu)` {: #menumenu data-toc-label="menu(Menu)" }

Добавляет выпадающее меню [Menu](<../../core/widgets/navigation/menu.md>) в строку меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `menu` | `Menu` | Экземпляр меню. |

Возвращает `MenuBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menu(String)` {: #menustring data-toc-label="menu(String)" }

Возвращает меню по тексту его заголовка, или null.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `label` | `String` | Текст метки. |

Возвращает `Menu`: Экземпляр меню..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menuCount()` {: #menucount data-toc-label="menuCount()" }

Возвращает количество меню в строке меню.

Возвращает `int`: Общее количество элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menus()` {: #menus data-toc-label="menus()" }

Возвращает неизменяемый список всех выпадающих меню в строке меню.

Возвращает `List`: Неизменяемый список меню..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menus(List)` {: #menuslist data-toc-label="menus(List)" }

Заменяет список меню в строке меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `menus` | `List` | Список меню. |

Возвращает `MenuBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`openMenu(int)` {: #openmenuint data-toc-label="openMenu(int)" }

Открывает выпадающее меню по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `MenuBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`openedMenuIndex()` {: #openedmenuindex data-toc-label="openedMenuIndex()" }

Возвращает индекс текущего открытого меню, или -1.

Возвращает `int`: Результирующее значение..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`openedPopup()` {: #openedpopup data-toc-label="openedPopup()" }

Возвращает текущий активный всплывающий контейнер меню, или null.

Возвращает `ContextMenu`: Всплывающий контейнер всплывающего меню..

</div>

