---
title: "Widget"
slug: "widget"
category: "Widgets"
locale: "en"
order: 10
---

# Widget

Base contract for any UI tree element: lifecycle, layout, rendering, input, and styling.

## Overview

`Widget` is the root interface for every element in the UniGUI tree hierarchy. It unifies layout measurement, positioning, visual styling, hit testing, event dispatching, frame tick updates, and procedural drawing into a single coherent contract.

It extends `EventEmitter`, allowing every widget in the tree to subscribe to and emit strongly typed events.

| Subsystem | Core Contract | Role & Execution |
| --- | --- | --- |
| **Hierarchy** | `parent()`, `children()`, `uiContext()` | Defines parent-child tree relations, coordinate spaces, and window context. |
| **Layout** | `measure()`, `arrange()`, `desiredSize()`, `layoutBounds()` | Two-pass layout: computes size request then commits final geometry bounds. |
| **Visual Styling** | `style()`, `styleId()`, `styleClasses()` | Inline CSS declarations, class tokens, and Theme / [StylePack](<../core/styling/style-pack.md>) rules. |
| **Input & Focus** | `handle()`, `mouseCursorAt()`, `focusable()`, `focusScope()` | Dispatches mouse, pointer, and keyboard events; manages focus scopes. |
| **Invalidation** | `invalidate()`, `clearInvalidation()`, `invalidationFlags()` | Tracks dirty layout and visual bits, avoiding unnecessary reflows. |
| **Render & Tick** | `render()`, `tick()`, `dispose()` | Per-frame state updates, procedural rendering into draw list, and resource disposal. |

!!! tip "Extending [WidgetBase](<../core/widgets/widget-base.md>) vs Implementing Widget"
    In almost all cases, custom controls should extend `WidgetBase` rather than implementing `Widget` from scratch.     `WidgetBase` provides complete implementations for retained render caching, FLIP animations, style resolution, and invalidation bubbling.

## Lifecycle & Pipeline

Each frame, the UniGUI runtime executes a structured pipeline across the widget hierarchy:

1. **`measure(LayoutContext)`**: Calculates requested dimensions (`desiredSize()`) based on content and layout constraints.
2. **`arrange(RectView)`**: Commits final positioned bounds and aligns child widgets.
3. **`handle(Event)`**: Processes routed input, mouse, and keyboard events.
4. **`tick(FrameContext)`**: Updates transitions, spring animations, and time-based state.
5. **`render(RenderContext)`**: Emits drawing commands into the active draw list.
6. **`dispose()`**: Cleans up internal widget resources prior to removal from the tree.

## Tree Hierarchy & Structure

Widgets form a directed tree structure managed by `parent()` and `children()`:

- **`parent()`**: Returns the owning container widget, or `null` if the widget is the root or unattached.
- **`children()`**: Returns child widgets in traversal order.
- **Order semantics**: Children order defines rendering order (back-to-front: first child drawn first, last child on top) and hit-testing order (front-to-back: topmost child receives pointer events first).
- **`uiContext()`**: Provides access to the root window, dispatcher, font registry, and input services.

## [Style](<../core/styling/style.md>) & Layout

UniGUI strictly decouples geometry from visual presentation:

- **Geometry**: Governed by `layoutBounds()` and `desiredSize()`, configured through `LayoutStyle`.
- **Visual [Style](<../core/styling/style.md>)**: Assigned via `styleId(...)` (referencing a [StylePack](<../core/styling/style-pack.md>) rule), `styleClasses(...)` (CSS classes), or inline declarations via `style("background: #333; radius: 8;")`.
- **[Style](<../core/styling/style.md>) Scope**: Widgets can declare `styleScope(true)` to isolate local style overrides from propagating to outer trees.

## Input, Focus & Interaction

Interactive state is controlled through dedicated status flags:

| State Flag | Purpose & Effect |
| --- | --- |
| **`visibility()`** | `VISIBLE` participates in layout and rendering; `HIDDEN` preserves layout space but hides visual; `COLLAPSED` leaves layout entirely. |
| **`enabled()`** | Controls user interactivity. Disabled widgets ignore events and receive the `:disabled` style pseudo-class. |
| **`hovered()`** | Indicates whether the mouse cursor is currently over this widget. |
| **`mouseCursorAt()`** | Returns the cursor shape (e.g. `HAND`, `TEXT`) when hovered at local coordinates. |
| **`focusable()`** | Determines whether the widget can accept keyboard focus via Tab navigation or clicking. |
| **`focusScope()`** | Traps Tab-cycling inside this subtree (crucial for modal dialogs and popup panels). |
| **`focusOrder()`** | Explicit relative priority index for Tab ordering. |

## Dirty State & Invalidation

To achieve maximum frame rates, UniGUI skips layout passes and retained draw command generation for clean subtrees:

- **`invalidate(flags)`**: Marks dirty bits from `InvalidationFlags` (`LAYOUT`, `VISUAL`, `RETAINED_RENDER`). Layout flags bubble upward to root.
- **`clearInvalidation(flags)`**: Clears processed dirty flags after the pipeline pass completes.
- **`subtreeInvalidationFlags()`**: Returns combined bitmask of pending dirty flags for the widget and all descendants.

## Subscribing to widget events

```java
Widget button = new Button("Submit");
button.on(ButtonClickEvent.TYPE, event -> {
    System.out.println("Clicked widget: " + event.target().id());
});
```

## Creating a custom widget

```java
public class CustomCard extends WidgetBase {
    @Override
    public void measure(LayoutContext context) {
        setDesiredSize(200.0f, 100.0f);
    }

    @Override
    public void render(RenderContext context) {
        context.fillRect(layoutBounds(), 0xFF2A2A2A);
    }
}
```

## Related links

- [EventEmitter](<../core/events/event-emitter.md>)
- [LayoutStyle](<../core/layout/layout-style.md>)
- [WidgetBase](<../core/widgets/widget-base.md>)

## API

### Identity & Styling

| Method | Description |
| --- | --- |
| [`id(String)`](#idstring) | Assigns a string identifier for tree lookups and debugging. |
| [`id()`](#id) | Returns the widget identifier, or empty string if unset. |
| [`styleId(String)`](#styleidstring) | Sets explicit [StylePack](<../core/styling/style-pack.md>) style id for Theme resolution. |
| [`styleId()`](#styleid) | Returns active style id token, or empty string. |
| [`styleClass(String)`](#styleclassstring) | Sets space- or comma-separated style class tokens. |
| [`styleClasses()`](#styleclasses) | Returns list of assigned style class identifiers. |
| [`addStyleClass(String)`](#addstyleclassstring) | Adds a single style class to this widget. |
| [`removeStyleClass(String)`](#removestyleclassstring) | Removes a single style class from this widget. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`id(String)` {: #idstring data-toc-label="id(String)" }

**Set Identifier**

Assigns a string identifier for tree lookups and debugging.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | widget identifier string |

Returns `Widget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`id()` {: #id data-toc-label="id()" }

**Get Identifier**

Returns the widget identifier, or empty string if unset.

Returns `String`: identifier string.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleId(String)` {: #styleidstring data-toc-label="styleId(String)" }

**Set [Style](<../core/styling/style.md>) ID**

Sets explicit [StylePack](<../core/styling/style-pack.md>) style id for Theme resolution.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleId` | `String` | style id token from Theme |

Returns `Widget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleId()` {: #styleid data-toc-label="styleId()" }

**Get [Style](<../core/styling/style.md>) ID**

Returns active style id token, or empty string.

Returns `String`: style id string.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleClass(String)` {: #styleclassstring data-toc-label="styleClass(String)" }

**Set [Style](<../core/styling/style.md>) Classes**

Sets space- or comma-separated style class tokens.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClasses` | `String` | style classes string |

Returns `Widget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`styleClasses()` {: #styleclasses data-toc-label="styleClasses()" }

**Get [Style](<../core/styling/style.md>) Classes**

Returns list of assigned style class identifiers.

Returns `List`: list of style class tokens.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`addStyleClass(String)` {: #addstyleclassstring data-toc-label="addStyleClass(String)" }

**Add [Style](<../core/styling/style.md>) Class**

Adds a single style class to this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClass` | `String` | style class to add |

Returns `Widget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`removeStyleClass(String)` {: #removestyleclassstring data-toc-label="removeStyleClass(String)" }

**Remove [Style](<../core/styling/style.md>) Class**

Removes a single style class from this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClass` | `String` | style class to remove |

Returns `Widget`: this widget for fluent configuration.

</div>

### Layout & Geometry

| Method | Description |
| --- | --- |
| [`layoutBounds()`](#layoutbounds) | Returns arranged coordinates and size in parent/root space. |
| [`desiredSize()`](#desiredsize) | Returns size calculated during the most recent measure pass. |
| [`measure(LayoutContext)`](#measurelayoutcontext) | Measures widget and computes desired size for layout context. |
| [`arrange(RectView)`](#arrangerectview) | Positions and dimensions widget in final arranged rectangle. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`layoutBounds()` {: #layoutbounds data-toc-label="layoutBounds()" }

**Layout Bounds**

Returns arranged coordinates and size in parent/root space.

Returns `RectView`: current arranged bounds.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`desiredSize()` {: #desiredsize data-toc-label="desiredSize()" }

**Desired Size**

Returns size calculated during the most recent measure pass.

Returns `LayoutSize`: most recently measured desired size.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`measure(LayoutContext)` {: #measurelayoutcontext data-toc-label="measure(LayoutContext)" }

**Measure**

Measures widget and computes desired size for layout context.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `LayoutContext` | layout constraints and sizing context |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`arrange(RectView)` {: #arrangerectview data-toc-label="arrange(RectView)" }

**Arrange**

Positions and dimensions widget in final arranged rectangle.

| Parameter | Type | Description |
| --- | --- | --- |
| `bounds` | `RectView` | assigned layout rectangle |

</div>

### State & Interaction

| Method | Description |
| --- | --- |
| [`visibility()`](#visibility) | Returns current visibility mode (VISIBLE, HIDDEN, COLLAPSED). |
| [`visible()`](#visible) | Returns true only if visibility is VISIBLE. |
| [`enabled()`](#enabled) | Returns true if widget accepts user interaction. |
| [`hovered()`](#hovered) | Returns true if mouse pointer is hovering over widget bounds. |
| [`focusable()`](#focusable) | Returns true if widget can receive keyboard focus. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`visibility()` {: #visibility data-toc-label="visibility()" }

**Get Visibility**

Returns current visibility mode (VISIBLE, HIDDEN, COLLAPSED).

Returns `Visibility`: current visibility mode.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`visible()` {: #visible data-toc-label="visible()" }

**Is Visible**

Returns true only if visibility is VISIBLE.

Returns `boolean`: true if VISIBLE.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

**Is Enabled**

Returns true if widget accepts user interaction.

Returns `boolean`: true if enabled.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`hovered()` {: #hovered data-toc-label="hovered()" }

**Is Hovered**

Returns true if mouse pointer is hovering over widget bounds.

Returns `boolean`: true if mouse is over the widget.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`focusable()` {: #focusable data-toc-label="focusable()" }

**Is Focusable**

Returns true if widget can receive keyboard focus.

Returns `boolean`: true if focusable.

</div>

### Rendering & Caching

| Method | Description |
| --- | --- |
| [`render(RenderContext)`](#renderrendercontext) | Draws the widget into the current frame render context. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`render(RenderContext)` {: #renderrendercontext data-toc-label="render(RenderContext)" }

**Render**

Draws the widget into the current frame render context.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | frame render context |

</div>

### Events & Lifecycle

| Method | Description |
| --- | --- |
| [`uiContext()`](#uicontext) | Returns the runtime context hosting dispatcher, theme, and input subsystems. |
| [`parent()`](#parent) | Returns the parent widget in the UI hierarchy tree. |
| [`children()`](#children) | Returns the list of child widgets in layout/traversal order. |
| [`invalidationFlags()`](#invalidationflags) | Returns bitmask of pending invalidation flags. |
| [`invalidate(int)`](#invalidateint) | Marks invalidation flags (VISUAL, LAYOUT, HIERARCHY, etc.). |
| [`clearInvalidation(int)`](#clearinvalidationint) | Clears specified flags after processing. |
| [`handle(Event)`](#handleevent) | Processes routed or direct UI events. |
| [`tick(FrameContext)`](#tickframecontext) | Updates per-frame state, timers, and animations. |
| [`dispose()`](#dispose) | Releases widget resources before removal from tree. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`uiContext()` {: #uicontext data-toc-label="uiContext()" }

**UI Context**

Returns the runtime context hosting dispatcher, theme, and input subsystems.

Returns `UIContext`: the active UIContext, or null if detached.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`parent()` {: #parent data-toc-label="parent()" }

**Parent Widget**

Returns the parent widget in the UI hierarchy tree.

Returns `Widget`: the parent widget, or null if root.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`children()` {: #children data-toc-label="children()" }

**Child Widgets**

Returns the list of child widgets in layout/traversal order.

Returns `List`: ordered list of children.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`invalidationFlags()` {: #invalidationflags data-toc-label="invalidationFlags()" }

**Invalidation Flags**

Returns bitmask of pending invalidation flags.

Returns `int`: bitmask of dirty flags.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`invalidate(int)` {: #invalidateint data-toc-label="invalidate(int)" }

**Invalidate**

Marks invalidation flags (VISUAL, LAYOUT, HIERARCHY, etc.).

| Parameter | Type | Description |
| --- | --- | --- |
| `flags` | `int` | bitmask of flags from InvalidationFlags |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`clearInvalidation(int)` {: #clearinvalidationint data-toc-label="clearInvalidation(int)" }

**Clear Invalidation**

Clears specified flags after processing.

| Parameter | Type | Description |
| --- | --- | --- |
| `flags` | `int` | bitmask of handled flags |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`handle(Event)` {: #handleevent data-toc-label="handle(Event)" }

**Handle Event**

Processes routed or direct UI events.

| Parameter | Type | Description |
| --- | --- | --- |
| `event` | `Event` | event object |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`tick(FrameContext)` {: #tickframecontext data-toc-label="tick(FrameContext)" }

**Frame Tick**

Updates per-frame state, timers, and animations.

| Parameter | Type | Description |
| --- | --- | --- |
| `frame` | `FrameContext` | current frame context |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Widget::</span>`dispose()` {: #dispose data-toc-label="dispose()" }

**Dispose**

Releases widget resources before removal from tree.

</div>

