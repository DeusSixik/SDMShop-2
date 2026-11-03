---
title: "MenuItem"
slug: "menu-item"
category: "Core/Виджеты/Навигация"
locale: "ru"
order: 0
---

# MenuItem

Выбираемый элемент меню, поддерживающий обычные действия, флажки, команды, горячие клавиши или каскадные подменю.

## Обзор

Выбираемый элемент меню, поддерживающий обычные действия, флажки, команды, горячие клавиши или каскадные подменю. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
MenuItem item = MenuItem.action("Save", this::save)
    .shortcutText("Ctrl+S");
```

## Связанные ссылки

- [Menu](<menu.md>): Всплывающее или выпадающее меню, содержащее вертикальный список командных пунктов меню и разделителей.
- [MenuBar](<../../../widgets/navigation/menu-bar.md>): Горизонтальная строка меню верхнего уровня, содержащая выпадающие меню с привязкой к командам и координацией всплывающих списков.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`action()` {: #action data-toc-label="action()" }

Возвращает действие обратного вызова при нажатии элемента.

Возвращает `Runnable`: Колбэк действия элемента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`action(Runnable)` {: #actionrunnable data-toc-label="action(Runnable)" }

Устанавливает действие обратного вызова при нажатии элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `action` | `Runnable` | Колбэк действия. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`action(String, Runnable)` {: #actionstring-runnable data-toc-label="action(String, Runnable)" }

Создает элемент меню с текстовой меткой и действием обратного вызова.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `label` | `String` | Текст метки. |
| `action` | `Runnable` | Колбэк действия. |

Возвращает `MenuItem`: Экземпляр созданного пункта MenuItem..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`checked()` {: #checked data-toc-label="checked()" }

Возвращает true, если у элемента меню установлен флажок.

Возвращает `boolean`: true, если флажок установлен; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`checked(boolean)` {: #checkedboolean data-toc-label="checked(boolean)" }

Устанавливает состояние флажка элемента меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `checked` | `boolean` | Состояние отмеченности. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`command(String)` {: #commandstring data-toc-label="command(String)" }

Создает элемент меню, привязанный к идентификатору команды.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |

Возвращает `MenuItem`: Экземпляр созданного пункта MenuItem..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`command(String, String)` {: #commandstring-string data-toc-label="command(String, String)" }

Создает элемент меню с текстовой меткой, привязанный к команде.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |
| `label` | `String` | Текст метки. |

Возвращает `MenuItem`: Экземпляр созданного пункта MenuItem..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`commandId()` {: #commandid data-toc-label="commandId()" }

Возвращает идентификатор команды, связанной с элементом.

Возвращает `String`: Результирующее значение..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`commandId(String)` {: #commandidstring data-toc-label="commandId(String)" }

Устанавливает идентификатор команды, связанной с элементом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Возвращает true, если элемент меню доступен для выбора.

Возвращает `boolean`: true, если включено; иначе false..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Устанавливает, доступен ли элемент меню для выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включена ли функция или элемент. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`kind()` {: #kind data-toc-label="kind()" }

Возвращает тип элемента меню (ACTION, COMMAND, CHECKBOX, SEPARATOR, SUBMENU).

Возвращает `Kind`: Тип элемента меню..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`label()` {: #label data-toc-label="label()" }

Возвращает текстовую метку элемента меню.

Возвращает `String`: Текстовая строка метки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`label(String)` {: #labelstring data-toc-label="label(String)" }

Устанавливает текстовую метку элемента меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `label` | `String` | Текст метки. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`menu(Menu)` {: #menumenu data-toc-label="menu(Menu)" }

Устанавливает дочернее каскадное подменю для данного элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `submenu` | `Menu` | Дочернее подменю. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`separator()` {: #separator data-toc-label="separator()" }

Создает элемент меню, являющийся разделителем.

Возвращает `MenuItem`: Экземпляр созданного пункта MenuItem..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`shortcutText()` {: #shortcuttext data-toc-label="shortcutText()" }

Возвращает текст сочетания клавиш элемента.

Возвращает `String`: Строка сочетания клавиш..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`shortcutText(String)` {: #shortcuttextstring data-toc-label="shortcutText(String)" }

Устанавливает текст сочетания клавиш элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `shortcutText` | `String` | Текст сочетания клавиш. |

Возвращает `MenuItem`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`submenu()` {: #submenu data-toc-label="submenu()" }

Возвращает дочернее подменю элемента, если оно задано.

Возвращает `Menu`: Дочернее подменю или null, если отсутствует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`submenu(Menu)` {: #submenumenu data-toc-label="submenu(Menu)" }

Создает элемент меню с дочерним подменю.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `submenu` | `Menu` | Дочернее подменю. |

Возвращает `MenuItem`: Экземпляр созданного пункта MenuItem..

</div>

