---
title: "NotificationView"
slug: "notification-view"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# NotificationView

Rich notification card widget rendering severity icons, titles, messages, action buttons, and auto-dismiss progress.

## Overview

`NotificationView` manages a visual stack of transient notification banners and alert cards on top of application content.

Key features:
- Automatic positioning and stacking according to `Placement` (top-right, bottom-right, etc.).
- Animated entry, timed auto-dismissal, and exit transitions.
- Severity indicators, customizable icons, titles, and action buttons.
- Global and container-level hosting via `OverlayLayer`.

## Displaying Rich Notification Card

```java
NotificationView notifications = new NotificationView();
notifications.toast("Build completed!");
```

## Related links

- [OverlayLayer](<overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [Toast](<toast.md>): Non-intrusive brief notification banner supporting status icons, formatted text, and timed auto-dismiss.

## API

<div class="api-member-card" markdown="1">

### `NotificationView()` {: #notificationview data-toc-label="NotificationView()" }

Constructs an empty NotificationView.

</div>

<div class="api-member-card" markdown="1">

### `NotificationView(String)` {: #notificationviewstring data-toc-label="NotificationView(String)" }

Constructs a NotificationView with an initial toast message.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`activeCount()` {: #activecount data-toc-label="activeCount()" }

Returns the count of active notifications.

Returns `int`: Total count of items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`addToast(Toast)` {: #addtoasttoast data-toc-label="addToast(Toast)" }

Adds a custom [Toast](<toast.md>) instance into the stack.

| Parameter | Type | Description |
| --- | --- | --- |
| `toast` | `Toast` | [Toast](<toast.md>) notification card instance. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Arranges the notification cards within host boundaries according to placement.

| Parameter | Type | Description |
| --- | --- | --- |
| `hostBounds` | `RectView` | Available host rectangular boundary. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`clear()` {: #clear data-toc-label="clear()" }

Clears all active and pending notifications.

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`duration()` {: #duration data-toc-label="duration()" }

Returns the display duration in seconds for new notifications.

Returns `float`: Display duration in seconds..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`duration(float)` {: #durationfloat data-toc-label="duration(float)" }

Sets the display duration in seconds for new notifications.

| Parameter | Type | Description |
| --- | --- | --- |
| `seconds` | `float` | Display duration in seconds before auto-dismissal. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`hide()` {: #hide data-toc-label="hide()" }

Hides the notification view.

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`margin()` {: #margin data-toc-label="margin()" }

Returns the margin distance from screen edges in UI pixels.

Returns `float`: Margin distance in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

Sets the margin distance from screen edges.

| Parameter | Type | Description |
| --- | --- | --- |
| `margin` | `float` | Margin distance from screen edges. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`maxVisible()` {: #maxvisible data-toc-label="maxVisible()" }

Returns the maximum number of simultaneously visible notification cards.

Returns `int`: Maximum visible count..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`maxVisible(int)` {: #maxvisibleint data-toc-label="maxVisible(int)" }

Sets the maximum number of simultaneously visible notification cards.

| Parameter | Type | Description |
| --- | --- | --- |
| `maxVisible` | `int` | Maximum number of simultaneously visible notifications. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`notifications()` {: #notifications data-toc-label="notifications()" }

Returns an unmodifiable list of active [Toast](<toast.md>) instances.

Returns `List`: List of hosted elements..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`notify(String)` {: #notifystring data-toc-label="notify(String)" }

Synonym pushing a new text toast into the notification stack.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`opened()` {: #opened data-toc-label="opened()" }

Returns true if any notifications are currently active.

Returns `boolean`: True if currently open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`placement()` {: #placement data-toc-label="placement()" }

Returns the screen corner/edge placement.

Returns `Placement`: The active placement position..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`placement(Placement)` {: #placementplacement data-toc-label="placement(Placement)" }

Sets the screen corner/edge placement.

| Parameter | Type | Description |
| --- | --- | --- |
| `placement` | `Placement` | Screen corner placement for stacking notifications. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`show()` {: #show data-toc-label="show()" }

Shows the notification view.

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`spacing()` {: #spacing data-toc-label="spacing()" }

Returns the vertical spacing between stacked notification cards in UI pixels.

Returns `float`: Spacing distance in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

Sets the vertical spacing between stacked notification cards.

| Parameter | Type | Description |
| --- | --- | --- |
| `spacing` | `float` | Vertical spacing distance between stacked notifications. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`text()` {: #text data-toc-label="text()" }

Returns the text of the latest notification.

Returns `String`: The displayed text string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Queues a new notification with the given text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `NotificationView`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`toast(String)` {: #toaststring data-toc-label="toast(String)" }

Pushes a new text toast into the notification stack.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `NotificationView`: This widget instance for chaining..

</div>

