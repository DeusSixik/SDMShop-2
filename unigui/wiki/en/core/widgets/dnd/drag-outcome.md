---
title: "DragOutcome"
slug: "drag-outcome"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragOutcome

Represents the terminal outcome of a drag-and-drop gesture in UniGUI: successfully dropped, dropped without target, or cancelled.

## Overview

`DragOutcome` represents the terminal resolution of an active drag-and-drop gesture.

### Outcome Values

| Value | Meaning |
| --- | --- |
| `DROPPED` | The payload was successfully dropped onto an accepting drop target zone. |
| `NO_TARGET` | The pointer was released over an area without an accepting drop target. |
| `CANCELLED` | The drag operation was explicitly cancelled or interrupted. |

!!! note "Lifecycle Completion"
    When `DragController` triggers `onDragEnd(Consumer<DragEnd>)`, the `DragEnd` object contains this outcome to let source widgets decide whether to remove items, reset styling, or revert animations.

## Related links

- [DragController](<drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.

