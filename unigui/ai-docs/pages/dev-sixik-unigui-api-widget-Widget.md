# `Widget` (extends `EventEmitter`)

Root interface for all UI tree elements: defines the lifecycle, layout, input, style, and render contracts.

- **Purpose:** Defines the pipeline entry points (measure -> arrange -> handle -> tick -> render) that the runtime calls each frame for every widget.

### USE WHEN

- Implement a new visual element by extending WidgetBase and overriding pipeline hooks.
- Traverse or inspect the UI tree via parent() and children().
- Subscribe to routed events through the inherited EventEmitter API.
- Programmatically invalidate layout or render state after a property change.

### CRITICAL RULES & GOTCHAS:

- State changes must mark InvalidationFlags; the runtime skips recalculation of clean subtrees.
- measure() sets desiredSize(); arrange() applies final bounds; calling order matters.
- parent() returns null for root or not-yet-attached widgets.
- children() order defines layout/render order and hit-testing sequence.
- dispose() must release only resources the widget itself owns.

- **Related:** `WidgetBase`, `EventEmitter`, `LayoutContext`, `RenderContext`, `FrameContext`, `InvalidationFlags`

### HIERARCHY

- `uiContext()` → UIContext — Returns runtime UIContext with dispatcher, theme, and input subsystems.
- `parent()` → Widget — Returns parent widget in UI tree, or null if root/detached.
- `children()` → List<Widget> — Returns child widgets in layout/traversal order.

### IDENTITY

- `id(String)` → Widget — Sets runtime identifier for hierarchy lookups and debugging.
- `id()` → String — Returns widget identifier string.

### STYLING

- `styleId(String)` → Widget — Sets style pack id for Theme resolution.
- `styleId()` → String — Returns style pack id assigned to this widget.
- `styleClass(String)` → Widget — Sets space/comma separated style classes.
- `styleClasses()` → List<String> — Returns list of assigned style classes.
- `addStyleClass(String)` → Widget — Adds a style class.
- `removeStyleClass(String)` → Widget — Removes a style class.

### LAYOUT

- `layoutBounds()` → RectView — Returns arranged layout bounds rectangle.
- `desiredSize()` → LayoutSize — Returns desired size from most recent measure pass.
- `measure(LayoutContext)` → void — Measures widget desired size under given layout constraints.
- `arrange(RectView)` → void — Positions and dimensions widget in assigned layout rectangle.

### STATE

- `visibility()` → Visibility — Returns current Visibility mode (VISIBLE, HIDDEN, COLLAPSED).
- `visible()` → boolean — Checks if widget is VISIBLE.
- `enabled()` → boolean — Checks if widget is enabled for interaction.
- `hovered()` → boolean — Checks if pointer is hovering over this widget.
- `focusable()` → boolean — Checks if widget can receive keyboard focus.

### LIFECYCLE

- `invalidationFlags()` → int — Returns active bitmask of dirty invalidation flags.
- `invalidate(int)` → void — Marks invalidation flags (VISUAL, LAYOUT, HIERARCHY, etc.).
- `clearInvalidation(int)` → void — Clears processed invalidation flags.
- `tick(FrameContext)` → void — Updates per-frame state, timers, and animations.
- `dispose()` → void — Releases widget-owned resources prior to removal.

### RENDER

- `render(RenderContext)` → void — Draws widget into active render context.

### EVENTS

- `handle(Event)` → void — Dispatches or consumes incoming UI event.

### EXAMPLES

#### Subscribe to a click event

```java
Widget button = ...;
button.on(ButtonClickEvent.TYPE, e -> {
    System.out.println("Clicked: " + e.target().id());
});
```

#### Custom widget skeleton

```java
public class MyWidget extends WidgetBase {
    @Override
    public void measure(LayoutContext ctx) {
        setDesiredSize(100, 50);
    }

    @Override
    public void render(RenderContext ctx) {
        ctx.fillRect(layoutBounds(), 0xFFAABBCC);
    }
}
```

