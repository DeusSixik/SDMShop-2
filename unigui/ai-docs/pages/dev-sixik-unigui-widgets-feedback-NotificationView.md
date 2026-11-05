# `NotificationView` (extends `Box`, implements `OverlayHostAware`)

Floating notification stack host displaying timed toast messages aligned to configurable screen edges or corners.

### API

- `NotificationView()` — Constructs an empty NotificationView.
- `NotificationView(String)` — Constructs a NotificationView with an initial toast message.
- `activeCount()` → int — Returns the count of active notifications.
- `addToast(Toast)` → NotificationView — Adds a custom Toast instance into the stack.
- `arrangeInHost(RectView)` → void — Arranges the notification cards within host boundaries according to placement.
- `clear()` → NotificationView — Clears all active and pending notifications.
- `duration()` → float — Returns the display duration in seconds for new notifications.
- `duration(float)` → NotificationView (default: `3.5`) — Sets the display duration in seconds for new notifications.
- `hide()` → NotificationView — Hides the notification view.
- `margin()` → float — Returns the margin distance from screen edges in UI pixels.
- `margin(float)` → NotificationView (default: `10`) — Sets the margin distance from screen edges.
- `maxVisible()` → int — Returns the maximum number of simultaneously visible notification cards.
- `maxVisible(int)` → NotificationView (default: `3`) — Sets the maximum number of simultaneously visible notification cards.
- `notifications()` → List<Toast> — Returns an unmodifiable list of active Toast instances.
- `notify(String)` → NotificationView — Synonym pushing a new text toast into the notification stack.
- `opened()` → boolean — Returns true if any notifications are currently active.
- `placement()` → Placement — Returns the screen corner/edge placement.
- `placement(Placement)` → NotificationView (default: `top_right`) — Sets the screen corner/edge placement.
- `show()` → NotificationView — Shows the notification view.
- `spacing()` → float — Returns the vertical spacing between stacked notification cards in UI pixels.
- `spacing(float)` → NotificationView (default: `8`) — Sets the vertical spacing between stacked notification cards.
- `text()` → String — Returns the text of the latest notification.
- `text(String)` → NotificationView — Queues a new notification with the given text.
- `toast(String)` → NotificationView — Pushes a new text toast into the notification stack.

