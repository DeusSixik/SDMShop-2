---
title: "UniScreen"
slug: "uni-screen"
category: "Core/Screens"
locale: "en"
order: 10
---

# UniScreen

Minecraft-independent description of a standalone screen/GUI window in UniGUI.

## Overview

`UniScreen` is the primary descriptor for standalone GUI screens in UniGUI. It wraps a root `Widget` tree, provides window title configuration, custom UI scaling (`uiScale`), game GUI scaling synchronization, render invalidation policies (`UiRenderPolicy`), default font assignment, and keyboard input interception before passing events to focused widgets.

## Lifecycle and Opening

Opening and closing screens is completely backend-independent. Invoking `openGui()` requests the active `RenderBackend` to open the screen (e.g. creating and setting a Minecraft `Screen` wrapper or standalone window). The `onClose` callback triggers when the screen is dismissed.

## Scaling and Input Interception

Screens can inherit Minecraft's GUI scale (`scaleWithGameGui(true)`) or enforce a fixed scaling factor via `uiScale(float)`.

The `KeyboardInputHandler` allows global shortcut handling (e.g. Esc, search hotkeys) before focused widgets receive the event.

## Creating and Opening a GUI Screen

```java
Widget root = new MySettingsWidget();
UniScreen screen = new UniScreen("Settings", root)
    .renderPolicy(UiRenderPolicy.onDirty())
    .scaleWithGameGui(true)
    .onClose(() -> System.out.println("Screen closed"))
    .openGui();
```

## Fixed Scale and Keyboard Interception

```java
UniScreen screen = new UniScreen("Editor", editorRoot)
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

Returns the registered close callback or null.

Returns `Runnable`: Runnable callback or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`closeGui()` {: #closegui data-toc-label="closeGui()" }

**closeGui()**

Closes this screen through the installed game-version backend.

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`context()` {: #context data-toc-label="context()" }

**context()**

Returns the optional UI context bound to this screen.

Returns `UIContext`: Bound UIContext or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`defaultFont()` {: #defaultfont data-toc-label="defaultFont()" }

**defaultFont()**

Returns the screen-level default font face or null if using backend default.

Returns `FontFace`: FontFace instance or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`defaultFont(FontFace)` {: #defaultfontfontface data-toc-label="defaultFont(FontFace)" }

**defaultFont(FontFace)**

Sets the screen-level default font face; null restores the backend default.

| Parameter | Type | Description |
| --- | --- | --- |
| `font` | `FontFace` | FontFace to use or null to restore default. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`hasCustomUiScale()` {: #hascustomuiscale data-toc-label="hasCustomUiScale()" }

**hasCustomUiScale()**

Returns true if a custom UI scale factor is configured.

Returns `boolean`: True if uiScale is finite..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`keyboardInputHandler()` {: #keyboardinputhandler data-toc-label="keyboardInputHandler()" }

**keyboardInputHandler()**

Returns the optional screen-level keyboard interceptor.

Returns `KeyboardInputHandler`: KeyboardInputHandler instance or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`keyboardInputHandler(KeyboardInputHandler)` {: #keyboardinputhandlerkeyboardinputhandler data-toc-label="keyboardInputHandler(KeyboardInputHandler)" }

**keyboardInputHandler(KeyboardInputHandler)**

Sets an optional screen-level keyboard interceptor invoked before focused widgets.

| Parameter | Type | Description |
| --- | --- | --- |
| `handler` | `KeyboardInputHandler` | KeyboardInputHandler callback. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`onClose(Runnable)` {: #oncloserunnable data-toc-label="onClose(Runnable)" }

**onClose(Runnable)**

Registers a callback to execute when the screen is closed.

| Parameter | Type | Description |
| --- | --- | --- |
| `callback` | `Runnable` | Runnable action. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`openGui()` {: #opengui data-toc-label="openGui()" }

**openGui()**

Opens this screen through the installed game-version backend.

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`pauseGame()` {: #pausegame data-toc-label="pauseGame()" }

**pauseGame()**

Whether the game is paused while this screen is open (single player only); true by default.

Returns `boolean`: True if the game pauses while the screen is open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`pauseGame(boolean)` {: #pausegameboolean data-toc-label="pauseGame(boolean)" }

**pauseGame(boolean)**

Sets whether the game is paused while this screen is open. Has no effect in multiplayer.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `boolean` | Boolean value. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`renderPolicy()` {: #renderpolicy data-toc-label="renderPolicy()" }

**renderPolicy()**

Returns the active rendering and invalidation policy.

Returns `UiRenderPolicy`: Current [UiRenderPolicy](<ui-render-policy.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`renderPolicy(UiRenderPolicy)` {: #renderpolicyuirenderpolicy data-toc-label="renderPolicy(UiRenderPolicy)" }

**renderPolicy([UiRenderPolicy](<ui-render-policy.md>))**

Configures the rendering and invalidation policy for this screen.

| Parameter | Type | Description |
| --- | --- | --- |
| `policy` | `UiRenderPolicy` | [UiRenderPolicy](<ui-render-policy.md>) to set; null defaults to continuous. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`root()` {: #root data-toc-label="root()" }

**root()**

Returns the root widget of this screen.

Returns `Widget`: Root widget instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`scaleWithGameGui()` {: #scalewithgamegui data-toc-label="scaleWithGameGui()" }

**scaleWithGameGui()**

Returns true if the screen scales automatically with the game's GUI scale.

Returns `boolean`: True if scaling with game GUI..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`scaleWithGameGui(boolean)` {: #scalewithgameguiboolean data-toc-label="scaleWithGameGui(boolean)" }

**scaleWithGameGui(boolean)**

Configures whether this screen scales automatically with the game's GUI scale.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `boolean` | True to enable auto-scaling with game GUI. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`title()` {: #title data-toc-label="title()" }

**title()**

Returns the window title of this screen.

Returns `String`: Screen title string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

**title(String)**

Sets the window title of this screen.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | New screen title. |

Returns `UniScreen`: This screen instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`uiScale()` {: #uiscale data-toc-label="uiScale()" }

**uiScale()**

Returns the custom UI scaling factor or NaN if not set.

Returns `float`: Scale multiplier..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniScreen::</span>`uiScale(float)` {: #uiscalefloat data-toc-label="uiScale(float)" }

**uiScale(float)**

Enforces a custom UI scale multiplier for this screen.

| Parameter | Type | Description |
| --- | --- | --- |
| `scale` | `float` | Positive finite scale factor. |

Returns `UniScreen`: This screen instance for chaining..

</div>

