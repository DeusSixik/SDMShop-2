# `WidgetBase` (implements `Widget`)

Base implementation for UniGUI widgets and the bridge between widget state, layout, styles, rendering, input, and animation.

- **Purpose:** Provides common fluent configuration and lifecycle behavior so concrete widgets can focus on their own content, measurement, and visual rendering.

### USE WHEN

- Implement a custom widget that should participate in the standard UniGUI layout, style, event, and animation pipelines.
- Configure an existing widget through common identity, visibility, focus, style, transform, or caching APIs.

### CRITICAL RULES & GOTCHAS:

- Subclass `WidgetBase`; do not instantiate it directly because it is abstract and owns framework lifecycle state.
- Use `layout(style -> ...)` for layout properties and `style(...)` for visual/style properties; they update different systems.
- Changing layout-related state must invalidate `LAYOUT`; visual-only changes should invalidate `VISUAL`.
- `Visibility.HIDDEN` keeps layout space but suppresses rendering and input; `Visibility.COLLAPSED` removes the widget from layout as well.
- A per-instance `renderer` replaces the widget's standard visual; children are still handled by the framework.
- Retained render caching is intended for stable leaf visuals; widgets with children fall back to normal child traversal.
- `styleScope(true)` limits inherited local-style lookup at that widget boundary; it does not remove the global theme.
- Call `super` lifecycle behavior when overriding framework methods unless the concrete widget deliberately replaces that contract.

- **Related:** `Widget`, `LayoutStyle`, `StylePack`, `MutableStyle`, `TransitionSpec`, `EventSubscription`

### IDENTITY & STYLE SELECTORS

- `id(String)` → WidgetBase — Assigns the runtime/editor identifier used by code-behind and lookup systems.
- `styleId(String)` → WidgetBase — Selects the explicit `StylePack` style id for this widget.
- `styleClass(String)` → WidgetBase — Replaces the widget's space-separated style-class set.
- `addStyleClass(String)` → WidgetBase — Adds one normalized style class without removing existing classes.
- `removeStyleClass(String)` → WidgetBase — Removes one normalized style class if it is present.
- `hasStyleClass(String)` → boolean — Checks whether the widget has a normalized style class.

### INLINE STYLING & LAYOUT

- `style(String)` → WidgetBase — Parses inline style declarations; a string without `:` or `;` is treated as a style id.
  - Rules:
    - Parsing diagnostics are retained in `styleDiagnostics()` and do not silently disappear.
- `style(Consumer<MutableStyle>)` → WidgetBase — Applies typed inline style values through a `MutableStyle` callback.
- `styleClear()` → WidgetBase — Clears only the widget's inline style and parser diagnostics; external style scopes remain active.
- `layoutStyle()` → LayoutStyle — Returns the live `LayoutStyle` owned by this widget.
  - Rules:
    - Mutating the returned style updates layout constraints and invalidates layout/visual state through its callback.
- `layout(Consumer<LayoutStyle>)` → WidgetBase — Applies one or more layout changes through a batched `LayoutStyle` callback.
- `localStyle(String, Style)` → WidgetBase — Adds or removes a local style override for a widget type.

### STATE & INTERACTION

- `visibility(Visibility)` → WidgetBase (default: `visible`) — Sets `VISIBLE`, `HIDDEN`, or `COLLAPSED` and invalidates layout when the layout footprint changes.
  - Rules:
    - `HIDDEN` preserves layout space; `COLLAPSED` removes the widget from layout.
- `visible(boolean)` → WidgetBase (default: `true`) — Convenience toggle between `VISIBLE` and `HIDDEN`; it never selects `COLLAPSED`.
- `enabled(boolean)` → WidgetBase (default: `true`) — Enables or disables user interaction and exposes the disabled style state.
- `mouseCursor(MouseCursor)` → WidgetBase — Sets the cursor used while the enabled, visible widget is under the pointer.
- `focusable(boolean)` → WidgetBase — Enables keyboard focus for the widget.
- `focusScope(boolean)` → WidgetBase — Makes this widget a boundary for keyboard focus traversal.
- `focusOrder(int)` → WidgetBase — Sets the relative order used by keyboard focus traversal.
- `styleScope(boolean)` → WidgetBase — Limits local style lookup to this widget subtree when enabled; global theme lookup still applies.

### TRANSFORMS & ANIMATION

- `layoutTransition(float, Easing)` → WidgetBase — Enables FLIP animation for subsequent layout-driven position changes.
  - Rules:
    - Only x/y movement is animated; layout size changes are not interpolated.
- `transformOrigin(TransformOrigin)` → WidgetBase — Selects a named transform pivot; `CUSTOM` preserves the raw pixel pivot.
- `transformPivot(float, float)` → WidgetBase — Sets a custom transform pivot in local pixels and cancels active pivot animations.
- `rotationDegrees(float)` → WidgetBase (default: `0`) — Sets rotation in degrees immediately and stops active rotation animations.
- `opacity(float)` → WidgetBase (default: `1`) — Sets opacity immediately after clamping it to the 0..1 range.
- `animateOpacity(float, TransitionSpec)` → WidgetBase — Transitions opacity to a target value using the supplied timing specification.
- `animatePosition(float, float, TransitionSpec)` → WidgetBase — Animates visual transform position without changing layout bounds.
- `animateScale(float, float, TransitionSpec)` → WidgetBase — Animates transform scale on the x and y axes.
- `animateSpring(AnimatedProperty, float, float, float)` → WidgetBase — Runs or retargets a spring animation for a built-in animated property.
- `animate(AnimatedProperty, float, TransitionSpec)` → WidgetBase — Runs or retargets a transition for a built-in animated property.
  - Rules:
    - Rotation uses shortest-path angle interpolation; non-positive duration applies the value immediately.
- `stopAnimations()` → WidgetBase — Stops every animation owned by the widget and removes temporary effect offsets.
- `playStoryboard(Object, Storyboard, PropertyPathResolver)` → StoryboardPlayer — Starts a storyboard against the widget tree; a stable key replaces a previous storyboard with the same key.
  - Rules:
    - Call from the UI thread after constructing the target widget tree.

### BINDINGS

- `bind(BindingData<T>, T)` → WidgetBase — Binds a widget property to a supported signal key and replaces any existing binding for that key.
  - Rules:
    - The signal's current value is applied immediately when the binding is created.
    - A supported binding is bidirectional and is closed automatically when this widget is disposed.
    - Unsupported keys are ignored; a signal whose type does not match the BindingData key is rejected.

### RENDERING & CACHING

- `renderer(WidgetRender)` → WidgetBase — Sets a per-instance renderer that replaces the widget's standard visual.
  - Rules:
    - The custom renderer replaces background/content drawing, but framework child traversal still occurs.
- `measureCached(LayoutContext)` → void — Reuses the measured desired size when constraints match and the subtree has no pending layout invalidation.
- `renderCachingEnabled(boolean)` → WidgetBase — Enables or disables retained draw-command caching.
  - Rules:
    - The current retained cache is effective for leaf widgets; widgets with children use normal rendering.
- `invalidateRenderCache()` → WidgetBase — Marks the retained draw-command fragment stale without modifying public invalidation flags.

### EVENTS

- `on(EventType<T>, EventListener<? super T>)` → EventSubscription — Registers a typed event listener and returns a subscription used to unsubscribe.
- `on(EventType<T>, boolean, EventListener<? super T>)` → EventSubscription — Registers a typed listener with explicit control over receiving cancelled events.
- `emit(Event)` → void — Dispatches an event to listeners registered on this widget.

### INVALIDATION & LIFECYCLE

- `invalidate(int)` → void — Marks widget state dirty, invalidates its retained render cache, and propagates subtree flags to ancestors.
- `clearInvalidationDirect(int)` → void — Clears processed flags without recomputing ancestors; intended only for runtime full-tree passes.
  - Rules:
    - Application and widget code should normally use `clearInvalidation`; incorrect direct clearing can leave ancestor state stale.

### EXAMPLES

#### Configure a widget

```java
WidgetBase widget = new Label("Hello")
        .id("greeting")
        .styleId("card")
        .addStyleClass("interactive")
        .layout(style -> style
                .padding(12.0f)
                .width(240.0f)
                .flexNone());
```

#### Animate a visual property

```java
widget
        .opacity(0.0f)
        .animateOpacity(1.0f, 0.20f)
        .animateScale(1.0f, 1.0f, 0.20f);
```

