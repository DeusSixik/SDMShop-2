---
title: "Popup"
slug: "popup"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# Popup

Anchored floating popup widget that positions itself relative to target widgets or screen coordinates.

## Overview

`Popup` represents a lightweight floating container anchored to a specific target widget or screen coordinate.

Key features:
- Anchor-based relative positioning (`TOP`, `BOTTOM`, `LEFT`, `RIGHT`).
- Boundary constraint checks against parent host bounds.
- Optional dismissal when clicking outside the popup boundaries (`closeOnOutsideClick`).
- State listeners for open and close events (`onOpenChanged`).

## Opening Anchored Popup

```java
Popup popup = new Popup(button, menuContent);
popup.open();
```

## Related links

- [OverlayLayer](<overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [Tooltip](<tooltip.md>): Hover-activated floating information bubble displaying helper text, rich layout, and delay timers.

## API

<div class="api-member-card" markdown="1">

### `Popup()` {: #popup data-toc-label="Popup()" }

Constructs an empty Popup.

</div>

<div class="api-member-card" markdown="1">

### `Popup(Widget, Widget)` {: #popupwidget-widget data-toc-label="Popup(Widget, Widget)" }

Constructs a Popup bound to an anchor widget and displaying content.

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `Widget` | Anchor widget to position relative to. |
| `content` | `Widget` | Content widget hosted inside. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Returns the anchor widget this popup is positioned relative to.

Returns `Widget`: The target anchor widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`anchor(Widget)` {: #anchorwidget data-toc-label="anchor(Widget)" }

Sets the anchor widget this popup is positioned relative to.

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `Widget` | Anchor widget to position relative to. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Arranges the popup relative to its anchor within host bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `bounds` | `RectView` | Available host rectangular boundary. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`close()` {: #close data-toc-label="close()" }

Closes the popup.

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`closeOnOutsideClick()` {: #closeonoutsideclick data-toc-label="closeOnOutsideClick()" }

Returns true if outside pointer clicks automatically dismiss the popup.

Returns `boolean`: True if closing on outside click is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`closeOnOutsideClick(boolean)` {: #closeonoutsideclickboolean data-toc-label="closeOnOutsideClick(boolean)" }

Sets whether outside clicks automatically close the popup.

| Parameter | Type | Description |
| --- | --- | --- |
| `closeOnOutsideClick` | `boolean` | Whether clicking outside automatically dismisses the widget. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`content()` {: #content data-toc-label="content()" }

Returns the content widget hosted inside this popup.

Returns `Widget`: The hosted content widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Sets the content widget displayed inside this popup.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | Content widget hosted inside. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`offset(float, float)` {: #offsetfloat-float data-toc-label="offset(float, float)" }

Sets additional displacement offsets from the computed placement in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`onOpenChanged(Runnable)` {: #onopenchangedrunnable data-toc-label="onOpenChanged(Runnable)" }

Registers a listener invoked when the open state changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `onOpenChanged` | `Runnable` | Callback invoked when popup open state changes. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`open()` {: #open data-toc-label="open()" }

Opens the popup.

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`open(boolean)` {: #openboolean data-toc-label="open(boolean)" }

Sets the open state of the popup.

| Parameter | Type | Description |
| --- | --- | --- |
| `open` | `boolean` | Target open state flag. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`opened()` {: #opened data-toc-label="opened()" }

Returns true if this popup is currently open.

Returns `boolean`: True if currently open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

Sets the interior padding around popup content.

| Parameter | Type | Description |
| --- | --- | --- |
| `padding` | `EdgeInsets` | Internal content padding. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`placement()` {: #placement data-toc-label="placement()" }

Returns the preferred anchor placement edge.

Returns `Placement`: The active placement position..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`placement(Placement)` {: #placementplacement data-toc-label="placement(Placement)" }

Sets the preferred anchor placement edge.

| Parameter | Type | Description |
| --- | --- | --- |
| `placement` | `Placement` | Popup placement relative to the anchor widget. |

Returns `Popup`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

Toggles between open and closed states.

Returns `Popup`: This widget instance for chaining..

</div>

