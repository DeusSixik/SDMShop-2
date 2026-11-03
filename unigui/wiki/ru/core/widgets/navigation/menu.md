---
title: "Menu"
slug: "menu"
category: "Core/Виджеты/Навигация"
locale: "ru"
order: 0
---

# Menu

Всплывающее или выпадающее меню, содержащее вертикальный список командных пунктов меню и разделителей.

## Обзор

Всплывающее или выпадающее меню, содержащее вертикальный список командных пунктов меню и разделителей. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
Menu fileMenu = new Menu("File")
    .action("New", this::onNew)
    .separator()
    .action("Exit", this::onExit);
```

## Связанные ссылки

- [MenuBar](<../../../widgets/navigation/menu-bar.md>): Горизонтальная строка меню верхнего уровня, содержащая выпадающие меню с привязкой к командам и координацией всплывающих списков.
- [MenuItem](<menu-item.md>): Выбираемый элемент меню, поддерживающий обычные действия, флажки, команды, горячие клавиши или каскадные подменю.

## API

<div class="api-member-card" markdown="1">

### `Menu(String)` {: #menustring data-toc-label="Menu(String)" }

Создает меню с указанной текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `label` | `String` | Текст метки. |

</div>

<div class="api-member-card" markdown="1">

### `Menu(String, String)` {: #menustring-string data-toc-label="Menu(String, String)" }

Создает меню с указанными строковым ID и текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |
| `label` | `String` | Текст метки. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`action(String, Runnable)` {: #actionstring-runnable data-toc-label="action(String, Runnable)" }

Добавляет в меню пункт с текстовой меткой и действием по клику.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `label` | `String` | Текст метки. |
| `action` | `Runnable` | Колбэк действия. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`clear()` {: #clear data-toc-label="clear()" }

Удаляет все элементы из меню.

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`command(String)` {: #commandstring data-toc-label="command(String)" }

Добавляет в меню пункт, привязанный к идентификатору команды.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`command(String, String)` {: #commandstring-string data-toc-label="command(String, String)" }

Добавляет в меню пункт с текстовой меткой, привязанный к команде.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |
| `label` | `String` | Текст метки. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`id()` {: #id data-toc-label="id()" }

Возвращает идентификатор меню.

Возвращает `String`: Уникальный строковый идентификатор..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`id(String)` {: #idstring data-toc-label="id(String)" }

Устанавливает идентификатор меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`item(MenuItem)` {: #itemmenuitem data-toc-label="item(MenuItem)" }

Добавляет элемент [MenuItem](<menu-item.md>) в меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `MenuItem` | Целевой элемент. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`items()` {: #items data-toc-label="items()" }

Возвращает неизменяемый список элементов меню.

Возвращает `List`: Неизменяемый список элементов-узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`label()` {: #label data-toc-label="label()" }

Возвращает текстовую метку заголовка меню.

Возвращает `String`: Текстовая строка метки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`label(String)` {: #labelstring data-toc-label="label(String)" }

Устанавливает текстовую метку заголовка меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `label` | `String` | Текст метки. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`separator()` {: #separator data-toc-label="separator()" }

Добавляет разделитель между пунктами меню.

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`submenu(Menu)` {: #submenumenu data-toc-label="submenu(Menu)" }

Добавляет дочернее подменю в данное меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `submenu` | `Menu` | Дочернее подменю. |

Возвращает `Menu`: Текущий экземпляр для цепочки вызовов..

</div>

