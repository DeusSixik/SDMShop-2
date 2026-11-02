# `ReorderController`

Drag reordering controller attachable to container widgets (e.g. VBox, HBox, PanelWidget) supporting insertion lines and automated DOM reordering.

### API

- `accepts(Predicate<DragPayload>)` → ReorderController — Adds custom predicate validation rule for reorder payloads.
- `accepts(String)` → ReorderController — Restricts accepted payload types by comma/pipe/whitespace delimited string (* accepts all).
- `attach(Widget)` → ReorderController — Attaches a ReorderController to the specified container widget.
- `autoApply(boolean)` → ReorderController — Enables or disables automatic repositioning of children when container is a PanelWidget.
- `container()` → Widget — Returns the container widget managed by this controller.
- `detach()` → void — Detaches event listeners from the container.
- `enabled(boolean)` → ReorderController — Enables or disables this reorder controller.
- `indicator(ColorView, float, float)` → ReorderController — Customizes appearance of the insertion line indicator.
- `internalOnly(boolean)` → ReorderController — Restricts reordering to internal children of this container only.
- `itemFilter(Predicate<Widget>)` → ReorderController — Configures a predicate filter selecting which child widgets participate in reordering.
- `onReorder(BiConsumer<Integer, Integer>)` → ReorderController — Registers a listener receiving indices: fromIndex and toIndex.
- `onReorder(Consumer<ReorderEvent>)` → ReorderController — Registers a listener receiving full ReorderEvent details.
- `orientation(Orientation)` → ReorderController — Configures orientation (VERTICAL or HORIZONTAL) for reordering calculations.

