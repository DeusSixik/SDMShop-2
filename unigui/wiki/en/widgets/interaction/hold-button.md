---
title: "HoldButton"
slug: "hold-button"
category: "Widgets/Interaction"
locale: "en"
order: 20
---

# HoldButton

Confirmation button that requires holding down for a specified duration before triggering.

## Overview

`HoldButton` extends `Button` to prevent accidental clicks on dangerous or irreversible actions (deleting profiles, wiping caches, resetting configs).

It renders a progressive fill bar representing elapsed hold time. If the pointer leaves the button or is released prematurely, the hold resets.

## Delete Confirmation [Button](<button.md>)

```java
HoldButton deleteBtn = new HoldButton("Hold to Delete")
        .holdDurationSeconds(1.5f)
        .cancelOnPointerExit(true);
deleteBtn.onHoldCompleted(e -> deleteDatabase());
```

## Related links

- [Button](<button.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">HoldButton::</span>`onHoldCompleted(EventListener)` {: #onholdcompletedeventlistener data-toc-label="onHoldCompleted(EventListener)" }

**On Hold Completed Listener**

Registers a callback invoked when the hold duration is reached.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

