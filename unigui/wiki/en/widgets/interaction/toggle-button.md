---
title: "ToggleButton"
slug: "toggle-button"
category: "Widgets/Interaction"
locale: "en"
order: 50
---

# ToggleButton

Two-state button that retains checked state upon clicking.

## Overview

`ToggleButton` extends `Button` with a boolean `checked()` state. When clicked, it flips its state and emits a `CheckedChangedEvent`.

## Toggle Mode

```java
ToggleButton gridToggle = new ToggleButton("Snap to Grid")
        .checked(true);
gridToggle.onCheckedChanged(e -> editor.setSnap(e.newValue()));
```

## Related links

- [Button](<button.md>)
- [ToggleSwitch](<toggle-switch.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToggleButton::</span>`onCheckedChanged(EventListener)` {: #oncheckedchangedeventlistener data-toc-label="onCheckedChanged(EventListener)" }

**On Checked Changed Listener**

Registers callback for toggle state change.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

