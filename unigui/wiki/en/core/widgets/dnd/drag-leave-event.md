---
title: "DragLeaveEvent"
slug: "drag-leave-event"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragLeaveEvent

Routed event notified when the drag pointer exits a drop zone or when the gesture is aborted.

## Overview

`DragLeaveEvent` is dispatched to a drop zone when the pointer moves outside its bounding box or when the drag session is aborted.

Use this event to clear hover state highlights, remove placeholder items, or hide drop indicator lines.

## Related links

- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

