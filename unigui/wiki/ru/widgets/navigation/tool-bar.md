---
title: "ToolBar"
slug: "tool-bar"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# ToolBar

Компактная панель инструментов с кнопками команд, разделителями и интеграцией с CommandManager.

## Обзор

Компактная панель инструментов с кнопками команд, разделителями и интеграцией с CommandManager. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
ToolBar toolbar = new ToolBar();
toolbar.command("edit.undo", "icon:undo", ToolButton.DisplayMode.ICON_ONLY);
toolbar.separator();
toolbar.command("edit.redo", "icon:redo", ToolButton.DisplayMode.ICON_ONLY);
```

## Связанные ссылки

- [MenuBar](<menu-bar.md>): Горизонтальная строка меню верхнего уровня, содержащая выпадающие меню с привязкой к командам и координацией всплывающих списков.

## API

<div class="api-member-card" markdown="1">

### `ToolBar()` {: #toolbar data-toc-label="ToolBar()" }

Создает горизонтальную панель инструментов ToolBar с высотой 26px по умолчанию.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`button(ToolButton)` {: #buttontoolbutton data-toc-label="button(ToolButton)" }

Добавляет кнопку [ToolButton](<../interaction/tool-button.md>) на панель инструментов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `button` | `ToolButton` | Экземпляр кнопки. |

Возвращает `ToolBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`command(String)` {: #commandstring data-toc-label="command(String)" }

Добавляет кнопку команды по ее идентификатору в текстовом режиме.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |

Возвращает `ToolButton`: Созданная или добавленная кнопка [ToolButton](<../interaction/tool-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`command(String, String, DisplayMode)` {: #commandstring-string-displaymode data-toc-label="command(String, String, DisplayMode)" }

Добавляет кнопку команды с идентификатором, иконкой и режимом отображения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |
| `icon` | `String` | Идентификатор или дескриптор иконки. |
| `displayMode` | `DisplayMode` | Режим отображения кнопки. |

Возвращает `ToolButton`: Созданная или добавленная кнопка [ToolButton](<../interaction/tool-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`commandManager()` {: #commandmanager data-toc-label="commandManager()" }

Возвращает связанный с панелью менеджер команд CommandManager.

Возвращает `CommandManager`: Связанный экземпляр CommandManager..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`commandManager(CommandManager)` {: #commandmanagercommandmanager data-toc-label="commandManager(CommandManager)" }

Устанавливает менеджер команд CommandManager для кнопок панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandManager` | `CommandManager` | Экземпляр CommandManager. |

Возвращает `ToolBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`iconCommand(String, String)` {: #iconcommandstring-string data-toc-label="iconCommand(String, String)" }

Добавляет компактную иконочную кнопку команды.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |
| `icon` | `String` | Идентификатор или дескриптор иконки. |

Возвращает `IconButton`: Созданная или добавленная кнопка [IconButton](<../interaction/icon-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`refreshCommandButtons()` {: #refreshcommandbuttons data-toc-label="refreshCommandButtons()" }

Обновляет состояние, метки и активность всех командных кнопок.

Возвращает `ToolBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`separator()` {: #separator data-toc-label="separator()" }

Добавляет вертикальный разделитель между группами кнопок.

Возвращает `ToolBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`spacer()` {: #spacer data-toc-label="spacer()" }

Добавляет расширяющийся пружинный распорный элемент.

Возвращает `ToolBar`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`toggleCommand(String, String, DisplayMode)` {: #togglecommandstring-string-displaymode data-toc-label="toggleCommand(String, String, DisplayMode)" }

Добавляет кнопку-переключатель команды с иконкой и режимом отображения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `commandId` | `String` | Идентификатор команды. |
| `icon` | `String` | Идентификатор или дескриптор иконки. |
| `displayMode` | `DisplayMode` | Режим отображения кнопки. |

Возвращает `ToggleToolButton`: Созданная или добавленная кнопка [ToggleToolButton](<../interaction/toggle-tool-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`widget(Widget)` {: #widgetwidget data-toc-label="widget(Widget)" }

Добавляет кастомный виджет на панель инструментов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | Компонент виджета. |

Возвращает `ToolBar`: Текущий экземпляр для цепочки вызовов..

</div>

