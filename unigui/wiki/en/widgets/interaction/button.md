---
title: "Button"
slug: "button"
category: "Widgets/Interaction"
locale: "en"
order: 10
---

# Button

Standard interactive push button with composable surface, icon, text, and rich state transitions.

## Overview

`Button` extends `SurfaceWidget<Button>` to combine a styled background plate (solid color, texture, or GLSL shader), border, corner radius, focus ring, and centered foreground text/icon.

It supports reactive state styling (`WidgetState.NORMAL`, `HOVERED`, `PRESSED`, `DISABLED`), pointer capture, keypress activation (`SPACE`, `ENTER`), and fluent event subscription via `onClick(...)`.

## Hover and Pressed Chrome

Buttons can specify explicit per-state overrides that supersede theme defaults:

- **`hoverBackground(color)`**: Dedicated background color when hovered.
- **`pressedBackground(color)`**: Dedicated background color while pressed.
- **`hoverTexture(handle)` / `pressedTexture(handle)`**: Specialized state textures.
- **`hoverShader(handle)`**: Animated procedural GLSL shader active on hover.
- **`interactionTransitions(true)`**: Enables smooth spring-like scale and opacity feedback on hover and press.

## Primary Action Button

```java
Button saveBtn = new Button("Save Changes")
        .background(ColorView.ofHex("#2C82C9"))
        .hoverBackground(ColorView.ofHex("#3A99D8"))
        .radius(6.0f);
saveBtn.onClick(e -> saveSettings());
```

## Related links

- [HoldButton](<hold-button.md>)
- [ToggleButton](<toggle-button.md>)

## API

<div class="api-member-card" markdown="1">

### `Button()` {: #button data-toc-label="Button()" }

**Constructor**

Creates an empty Button widget.

</div>

<div class="api-member-card" markdown="1">

### `Button(String)` {: #buttonstring data-toc-label="Button(String)" }

**Create with [Text](<../display/text.md>)**

Creates a Button with plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | text label |

</div>

<div class="api-member-card" markdown="1">

### `Button(RichText)` {: #buttonrichtext data-toc-label="Button(RichText)" }

**Create with [RichText](<../../core/text/rich-text.md>)**

Creates a Button with rich formatted text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | rich text object |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

**Set [RichText](<../../core/text/rich-text.md>)**

Sets the formatted [RichText](<../../core/text/rich-text.md>) label of the button.

| Parameter | Type | Description |
| --- | --- | --- |
| `richText` | `RichText` | rich text object |

Returns `Button`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`onClick(EventListener)` {: #onclickeventlistener data-toc-label="onClick(EventListener)" }

**On Click Listener**

Registers a callback triggered when the button is clicked or activated via keyboard.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | click event listener |

Returns `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`hoverBackground(ColorView)` {: #hoverbackgroundcolorview data-toc-label="hoverBackground(ColorView)" }

**Set Hover Background**

Sets custom background color for the hovered state.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | color view |

Returns `Button`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Button::</span>`pressedBackground(ColorView)` {: #pressedbackgroundcolorview data-toc-label="pressedBackground(ColorView)" }

**Set Pressed Background**

Sets custom background color for the pressed state.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | color view |

Returns `Button`: this widget for fluent configuration.

</div>

