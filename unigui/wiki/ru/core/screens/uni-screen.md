---
title: "UniScreen"
slug: "uni-screen"
category: "Core/Screens"
locale: "ru"
order: 10
---

# UniScreen

Платформо-независимое описание полноэкранного GUI-окна UniGUI.

## Обзор

`UniScreen` — фундаментальный дескриптор полноэкранных GUI-интерфейсов в UniGUI. Он инкапсулирует дерево виджетов `Widget`, заголовок окна, масштабирование интерфейса (`uiScale`), синхронизацию с масштабом GUI игры, политику рендеринга (`UiRenderPolicy`), шрифт по умолчанию и перехват клавиатурного ввода перед отправкой виджетам.

## Жизненный цикл и открытие

Открытие и закрытие экрана не зависит от платформы. Вызов `openGui()` запрашивает у активного `RenderBackend` открытие экрана (например, создание и отображение Minecraft-обёртки `Screen` или автономного окна). Колбэк `onClose` вызывается при закрытии экрана.

## Масштабирование и перехват ввода

Экран может масштабироваться синхронно с настройками интерфейса игры (`scaleWithGameGui(true)`) либо использовать фиксированный масштаб через `uiScale(float)`.

Интерфейс `KeyboardInputHandler` позволяет глобально перехватывать горячие клавиши (например, Esc или поиск) до их передачи сфокусированным виджетам.

## Создание и открытие экрана

```java
Widget root = new MySettingsWidget();
UniScreen screen = new UniScreen("Settings", root)
    .renderPolicy(UiRenderPolicy.onDirty())
    .scaleWithGameGui(true)
    .onClose(() -> System.out.println("Экран закрыт"))
    .openGui();
```

## Пользовательский масштаб и перехват ввода

```java
UniScreen screen = new UniScreen("Редактор", editorRoot)
    .uiScale(1.5f)
    .keyboardInputHandler((keyCode, scanCode, modifiers) -> {
        if (keyCode == 256) { // ESC
            screen.closeGui();
            return true;
        }
        return false;
    })
    .openGui();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`closeCallback()` {: #closecallback data-toc-label="closeCallback()" }

**closeCallback()**

Возвращает зарегистрированный колбэк закрытия или null.

Возвращает `Runnable`: Колбэк Runnable или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`closeGui()` {: #closegui data-toc-label="closeGui()" }

**closeGui()**

Закрывает этот экран через установленный бэкенд платформы игры.

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`context()` {: #context data-toc-label="context()" }

**context()**

Возвращает опциональный контекст интерфейса, привязанный к экрану.

Возвращает `UIContext`: Привязанный UIContext или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`defaultFont()` {: #defaultfont data-toc-label="defaultFont()" }

**defaultFont()**

Возвращает шрифт по умолчанию на уровне экрана или null, если используется дефолт бэкенда.

Возвращает `FontFace`: Экземпляр FontFace или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`defaultFont(FontFace)` {: #defaultfontfontface data-toc-label="defaultFont(FontFace)" }

**defaultFont(FontFace)**

Устанавливает шрифт по умолчанию для экрана; null восстанавливает значение по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `font` | `FontFace` | Шрифт FontFace или null для сброса на дефолт бэкенда. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`hasCustomUiScale()` {: #hascustomuiscale data-toc-label="hasCustomUiScale()" }

**hasCustomUiScale()**

Возвращает true, если задан пользовательский масштаб интерфейса.

Возвращает `boolean`: True, если задан конечный масштаб..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`keyboardInputHandler()` {: #keyboardinputhandler data-toc-label="keyboardInputHandler()" }

**keyboardInputHandler()**

Возвращает перехватчик клавиатурного ввода уровня экрана.

Возвращает `KeyboardInputHandler`: Экземпляр KeyboardInputHandler или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`keyboardInputHandler(KeyboardInputHandler)` {: #keyboardinputhandlerkeyboardinputhandler data-toc-label="keyboardInputHandler(KeyboardInputHandler)" }

**keyboardInputHandler(KeyboardInputHandler)**

Устанавливает перехватчик ввода с клавиатуры, вызываемый до передачи событий виджетам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `handler` | `KeyboardInputHandler` | Обработчик клавиатурного ввода. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`onClose(Runnable)` {: #oncloserunnable data-toc-label="onClose(Runnable)" }

**onClose(Runnable)**

Регистрирует колбэк, вызываемый при закрытии экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `callback` | `Runnable` | Действие Runnable. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`openGui()` {: #opengui data-toc-label="openGui()" }

**openGui()**

Открывает этот экран через установленный бэкенд платформы игры.

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`pauseGame()` {: #pausegame data-toc-label="pauseGame()" }

**pauseGame()**

Ставится ли игра на паузу, пока экран открыт (только одиночная игра); по умолчанию true.

Возвращает `boolean`: true, если игра ставится на паузу, пока экран открыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`pauseGame(boolean)` {: #pausegameboolean data-toc-label="pauseGame(boolean)" }

**pauseGame(boolean)**

Задаёт, ставится ли игра на паузу, пока экран открыт. В сетевой игре не действует.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `boolean` | Логическое значение. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`renderPolicy()` {: #renderpolicy data-toc-label="renderPolicy()" }

**renderPolicy()**

Возвращает активную политику рендеринга и инвалидации.

Возвращает `UiRenderPolicy`: Текущая политика [UiRenderPolicy](<ui-render-policy.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`renderPolicy(UiRenderPolicy)` {: #renderpolicyuirenderpolicy data-toc-label="renderPolicy(UiRenderPolicy)" }

**renderPolicy([UiRenderPolicy](<ui-render-policy.md>))**

Настраивает политику рендеринга и инвалидации для экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `policy` | `UiRenderPolicy` | Устанавливаемая политика [UiRenderPolicy](<ui-render-policy.md>); при null используется continuous. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`root()` {: #root data-toc-label="root()" }

**root()**

Возвращает корневой виджет экрана.

Возвращает `Widget`: Экземпляр корневого виджета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`scaleWithGameGui()` {: #scalewithgamegui data-toc-label="scaleWithGameGui()" }

**scaleWithGameGui()**

Возвращает true, если масштаб экрана синхронизируется с настройками GUI игры.

Возвращает `boolean`: True при синхронизации масштаба..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`scaleWithGameGui(boolean)` {: #scalewithgameguiboolean data-toc-label="scaleWithGameGui(boolean)" }

**scaleWithGameGui(boolean)**

Определяет, должен ли экран синхронизировать масштаб с настройками GUI игры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `boolean` | True для включения синхронизации. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`title()` {: #title data-toc-label="title()" }

**title()**

Возвращает заголовок окна экрана.

Возвращает `String`: Строка заголовка экрана..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

**title(String)**

Устанавливает заголовок окна экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Новый заголовок экрана. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`uiScale()` {: #uiscale data-toc-label="uiScale()" }

**uiScale()**

Возвращает пользовательский масштаб интерфейса или NaN, если не задан.

Возвращает `float`: Множитель масштабирования..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`uiScale(float)` {: #uiscalefloat data-toc-label="uiScale(float)" }

**uiScale(float)**

Устанавливает фиксированный масштаб интерфейса для экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `scale` | `float` | Положительный конечный масштабный коэффициент. |

Возвращает `UniScreen`: Текущий экземпляр экрана для цепочки вызовов..

</div>

