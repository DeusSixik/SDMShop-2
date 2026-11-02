# `PanelWidget` (extends `WidgetBase`)

Base container for multi-child widgets managing hierarchy, deferred mutations, and rendering order.

- **Purpose:** Manages child collections with a thread-safe mutation queue, cached snapshot arrays for hot loops, and coordinate/UIContext propagation. Provides default stack/overlay layout.

### USE WHEN

- Extend to implement custom multi-child containers.
- Safely add, remove, or clear children during event handling or rendering without concurrent modification.

### CRITICAL RULES & GOTCHAS:

- Mutations (addChild, removeChild, clearChildren) are deferred and applied via applyQueuedMutations().
- Subclasses overriding measure/arrange/renderChildren should call applyQueuedMutations() first.
- childSnapshot() provides an allocation-free array view of children for hot tick/render loops.

- **Related:** `StackPanel`, `Box`, `SurfaceWidget`, `WidgetBase`

### CHILDREN

- `addChild(Widget)` → void — Enqueues child widget addition to hierarchy.
- `insertChild(int, Widget)` → void — Enqueues child widget insertion at specific index.
- `removeChild(Widget)` → void — Enqueues child removal and detachment.
- `clearChildren()` → void — Enqueues removal and disposal of all child widgets.
- `children()` → List<Widget> — Returns unmodifiable view of active child widgets.

### EXAMPLES

#### Basic container usage

```java
PanelWidget panel = new PanelWidget();
panel.addChild(new Label("Item"));
```

