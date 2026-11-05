---
title: "Toast"
slug: "toast"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# Toast

Non-intrusive brief notification banner supporting status icons, formatted text, and timed auto-dismiss.

## Overview

`Toast` provides transient, non-intrusive status messages that automatically disappear after a specified timeout.

Key features:
- Plain and rich-text message formatting.
- Configurable auto-dismiss duration and fade transitions.
- Optional status icon and custom actions.
- Stackable integration within `NotificationView`.

## Showing Auto-Dismissing Toast Notification

```java
Toast toast = new Toast("Settings saved successfully!");
toast.duration(3.0f).show();
```

## Related links

- [NotificationView](<notification-view.md>): Rich notification card widget rendering severity icons, titles, messages, action buttons, and auto-dismiss progress.
- [OverlayLayer](<overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.

## API

<div class="api-member-card" markdown="1">

### `Toast()` {: #toast data-toc-label="Toast()" }

Constructs an empty Toast widget initialized with default styling.

</div>

<div class="api-member-card" markdown="1">

### `Toast(String)` {: #toaststring data-toc-label="Toast(String)" }

Constructs a Toast with the specified message text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Positions the toast anchored to the bottom-right corner of the host container.

| Parameter | Type | Description |
| --- | --- | --- |
| `hostBounds` | `RectView` | Available host rectangular boundary. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`duration()` {: #duration data-toc-label="duration()" }

Returns the toast lifetime in seconds.

Returns `float`: Display duration in seconds..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`duration(float)` {: #durationfloat data-toc-label="duration(float)" }

Sets the toast lifetime in seconds before auto-dismissal (0 keeps it open indefinitely).

| Parameter | Type | Description |
| --- | --- | --- |
| `seconds` | `float` | Display duration in seconds before auto-dismissal. |

Returns `Toast`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`hide()` {: #hide data-toc-label="hide()" }

Hides the toast immediately.

Returns `Toast`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`margin()` {: #margin data-toc-label="margin()" }

Returns the margin distance from host edges in UI pixels.

Returns `float`: Margin distance in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

Sets the margin distance from host edges in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `margin` | `float` | External margin distance. |

Returns `Toast`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`opened()` {: #opened data-toc-label="opened()" }

Returns true if the toast is currently visible and active.

Returns `boolean`: True if currently open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Sets the preferred dimensions of the toast card in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension in pixels. |
| `height` | `float` | Height dimension in pixels. |

Returns `Toast`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`show()` {: #show data-toc-label="show()" }

Shows the toast and resets its elapsed time counter.

Returns `Toast`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`text()` {: #text data-toc-label="text()" }

Returns the toast message text.

Returns `String`: The displayed text string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Sets the toast message text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `Toast`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`toast(String)` {: #toaststring data-toc-label="toast(String)" }

Updates message text and immediately displays the toast with animation.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `Toast`: This widget instance for chaining..

</div>

