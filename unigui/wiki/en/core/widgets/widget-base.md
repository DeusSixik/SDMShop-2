---
title: "WidgetBase"
slug: "widget-base"
category: "Core/Widgets"
locale: "en"
order: 30
---

# WidgetBase

Shared widget foundation for layout, [StylePack](<../styling/style-pack.md>) lookup, state, rendering, events, and animation.

## Overview

`WidgetBase` is the central abstract foundation for all UniGUI widgets. It unifies layout bounds, visual styles, input handling, invalidation tracking, focus traversal, transforms, animations, and render dispatch into a coherent fluent API.

| Core Subsystem | Primary Methods | Role & Architecture |
| --- | --- | --- |
| **Hierarchy** | `parent()`, `children()`, `uiContext()` | Manages parent/child widget tree relations, layout measurement propagation, and contextual host access. |
| **Geometry & Layout** | `layout()`, `layoutStyle()`, `desiredSize()` | Measures and positions the widget via `LayoutStyle` (flexbox, CSS grid, margins, padding, size constraints). |
| **Visual Styling** | `style()`, `styleId()`, `styleClass()` | Handles inline CSS declarations, [StylePack](<../styling/style-pack.md>) theme lookups, class selectors, and scoped style isolation. |
| **State & Interaction** | `visibility()`, `enabled()`, `mouseCursor()` | Governs layout flow participation, hover detection, custom mouse cursor, and tab focus management. |
| **Transforms & Animation** | `animate()`, `opacity()`, `rotationDegrees()` | Interpolates properties, executes FLIP layout transitions, spring physics, and multi-track storyboards. |
| **Rendering & Caching** | `renderer()`, `renderCached()`, `invalidate()` | Dispatches draw commands, supports retained render cache for leaf widgets, and manages dirty flags. |
| **Events** | `on()`, `emit()`, `handle()` | Strongly typed event listening and bubbling throughout the widget hierarchy. |

!!! note "Abstract Base Implementation"
    `WidgetBase` provides core infrastructure and is abstract. Build user interfaces using concrete built-in widgets (such as `Label`, `Button`, `VBox`, `ScrollView`) or extend it to implement custom controls.

## [Style](<../styling/style.md>) and layout

UniGUI cleanly separates **Layout** (geometry, positioning, and sizing) from **[Style](<../styling/style.md>)** (visual colors, borders, shadows, and textures).

| Styling Mechanism | Method / Syntax | Description & Behavior |
| --- | --- | --- |
| **Inline CSS Declarations** | `style("color: #fff; padding: 8px;")` | Parses property-value pairs into the widget's inline style. Parser warnings remain in `styleDiagnostics()`. |
| **[StylePack](<../styling/style-pack.md>) Reference** | `styleId("card-primary")` | References a pre-defined theme style rule declared in an active `StylePack`. |
| **[Style](<../styling/style.md>) Classes** | `styleClass("btn primary")` / `addStyleClass("active")` | Assigns CSS classes for conditional or state-based styling rules (`:hover`, `:focused`, `:disabled`). |
| **Subtree [Style](<../styling/style.md>) Scope** | `styleScope(true)` | Isolates the widget subtree, preventing inherited styles from propagating beyond this boundary. |
| **Local Type Overrides** | `localStyle("Button", buttonStyle)` | Replaces the default style for all descendant widgets of the given type within this subtree. |
| **Layout Configuration** | `layout(style -> style.width(100).fill())` | Configures geometry via batched `LayoutStyle` callback, avoiding redundant layout invalidations. |

!!! tip "Performance Best Practice: Layout vs. [Style](<../styling/style.md>)"
    Mutating visual styles (e.g. changing `color` or `opacity`) only triggers a **VISUAL** invalidation (repaint). Changing geometry via `layout(...)` triggers a **LAYOUT** invalidation (remeasurement). Separating them ensures smooth 60+ FPS UI performance.

## Visibility, focus, and interaction

[Widget](<../../widgets/widget.md>) visibility, user interactivity, and focus navigation are managed via explicit state flags.

| Visibility Mode | Layout Calculation | Visual [Paint](<../render/paint.md>) | Mouse / Input Events | Keyboard Focusable |
| --- | --- | --- | --- | --- |
| `Visibility.VISIBLE` | Participates normally | Rendered | Receives events | Yes |
| `Visibility.HIDDEN` | Participates (preserves space) | Suppressed | Ignored | No |
| `Visibility.COLLAPSED` | Completely removed (zero size) | Suppressed | Ignored | No |

| Interaction Feature | Method | Purpose & Effect |
| --- | --- | --- |
| **Enablement** | `enabled(boolean)` | Enables or disables interaction. Inactive widgets receive the `:disabled` style pseudo-class and ignore events. |
| **Hover Query** | `hovered()` | Returns whether the cursor is currently within the widget's bounds. |
| **Custom Cursor** | `mouseCursor(MouseCursor)` | Sets active cursor shape (`HAND`, `TEXT`, `CROSSHAIR`, etc.) while enabled and visible. |
| **Focus Navigation** | `focusable(boolean)` | Enables tab navigation and keyboard focus for this widget. |
| **Focus Boundary** | `focusScope(true)` | Traps tab cycling within this widget's subtree (crucial for modal dialogs and dropdown menus). |
| **Tab Ordering** | `focusOrder(int)` | Explicit relative index for tab-key sequence ordering (default: document flow). |

!!! warning "Choosing `HIDDEN` vs. `COLLAPSED`"
    Use `Visibility.HIDDEN` when an element should temporarily vanish without causing sibling widgets to shift or recalculate positions. Use `Visibility.COLLAPSED` when the element should be removed from flow and yield its space.

## Transforms and animation

UniGUI provides a unified animation engine supporting instant transforms, interpolated transitions, spring physics, layout movement, and choreographies.

| Animation Technique | Method | Description |
| --- | --- | --- |
| **Direct Transform** | `opacity(val)`, `rotationDegrees(deg)`, `transformPivot(x, y)` | Instantly updates visual transform properties without animation overhead. |
| **Interpolated Transition** | `animate(property, target, spec)`, `animateOpacity(...)`, `animatePosition(...)`, `animateScale(...)` | Smoothly transitions a property using easing curves (`TransitionSpec`). In-flight transitions are smoothly retargeted. |
| **Spring Physics** | `animateSpring(property, target, stiffness, damping)` | Physically modeled damped harmonic oscillator motion with realistic bounce and overshoot. |
| **Layout Movement (FLIP)** | `layoutTransition(duration, easing)` | Animates positional changes caused by layout reflows using First-Last-Invert-Play (FLIP). |
| **Attention Feedback** | `shake(amplitude, duration)` | Oscillates the widget horizontally/vertically to indicate errors or invalid actions. |
| **Choreographed Storyboards** | `playStoryboard(storyboard)` | Complex, multi-target, multi-keyframe timelines managed by `StoryboardPlayer`. |

!!! note "Layout Transitions vs. Geometry Animation"
    `layoutTransition(...)` animates position displacement (X and Y coordinates) resulting from layout passes. It does not stretch or interpolate width/height dimensions.

## Rendering, invalidation, and events

The render pipeline separates layout measurement, visual draw command recording, and subtree invalidation.

| Invalidation Flag | Target Subsystem | When Triggered |
| --- | --- | --- |
| `InvalidationFlags.LAYOUT` | Layout Engine | Size, padding, margin, or flex rules changed. Propagates dirty flags upward to root. |
| `InvalidationFlags.VISUAL` | Render Pipeline | Colors, text, borders, or opacity changed. Triggers repaint without remeasuring geometry. |
| `InvalidationFlags.RETAINED_RENDER` | Draw Cache | Internal retained draw command cache invalidated. |

| Pipeline Component | Method | Purpose & Usage |
| --- | --- | --- |
| **Custom Renderer** | `renderer(WidgetRender)` | Replaces the widget's default drawing commands with a custom procedural renderer. |
| **Retained Draw Cache** | `renderCachingEnabled(boolean)` | Records draw calls for static leaf widgets into a reusable buffer, drastically reducing GPU overhead. |
| **Event Subscription** | `on(EventType, listener)` | Registers a typed listener for input, focus, or lifecycle events. Returns an `EventSubscription` for unregistering. |
| **Event Dispatch** | `emit(Event)` | Dispatches a typed event directly to the widget and triggers listener callbacks. |

!!! important "Child Traversal with Custom Renderers"
    Assigning a custom `renderer(...)` replaces background and content rendering of the target widget. Child widgets belonging to the container are still traversed and drawn automatically by the framework.

## Configure a widget

```java
Label greeting = new Label("Hello")
        .id("greeting")
        .styleId("card")
        .addStyleClass("interactive");

greeting.layout(style -> style
        .padding(12.0f)
        .width(240.0f)
        .flexNone());
```

## Animate a visual property

```java
greeting
        .opacity(0.0f)
        .animateOpacity(1.0f, 0.20f)
        .animateScale(1.0f, 1.0f, 0.20f);
```

## API

### Identity & Styling

| Method | Description |
| --- | --- |
| [`id(String)`](#idstring) | Assigns the unique runtime/editor identifier used for lookup and code-behind. |
| [`id()`](#id) | Returns the identifier of this widget, or an empty string if none was assigned. |
| [`styleId(String)`](#styleidstring) | Selects an explicit style rule from an active [StylePack](<../styling/style-pack.md>) for this widget. |
| [`styleId()`](#styleid) | Returns the explicit [StylePack](<../styling/style-pack.md>) style id assigned to this widget. |
| [`style(String)`](#stylestring) | Applies inline declarations, or treats a bare token as a [StylePack](<../styling/style-pack.md>) style id. |
| [`style(Consumer)`](#styleconsumer) | Applies typed inline style declarations through a mutable style builder callback. |
| [`styleClear()`](#styleclear) | Removes all inline style declarations and clears parser diagnostics. |
| [`styleDiagnostics()`](#stylediagnostics) | Returns diagnostics and warnings collected while parsing inline style strings. |
| [`styleClass(String)`](#styleclassstring) | Replaces the widget's style classes with a space-separated class list. |
| [`addStyleClass(String)`](#addstyleclassstring) | Adds a normalized style class selector without removing existing classes. |
| [`removeStyleClass(String)`](#removestyleclassstring) | Removes a style class selector if it is currently assigned. |
| [`hasStyleClass(String)`](#hasstyleclassstring) | Checks whether the specified style class is assigned to this widget. |
| [`styleClasses()`](#styleclasses) | Returns an unmodifiable view of all normalized style classes assigned to this widget. |
| [`styleScope(boolean)`](#stylescopeboolean) | Limits local style inheritance to this widget subtree when enabled. |
| [`styleScope()`](#stylescope) | Checks whether this widget forms a local style scope boundary. |
| [`localStyle(String, Style)`](#localstylestring-style) | Sets or clears a local style override for a specific widget type within this subtree. |
| [`localStyle(String)`](#localstylestring) | Retrieves the local style override defined on this widget for a given widget type. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`id(String)` {: #idstring data-toc-label="id(String)" }

**Set widget ID**

Assigns the unique runtime/editor identifier used for lookup and code-behind.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique identifier string. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`id()` {: #id data-toc-label="id()" }

**Get widget ID**

Returns the identifier of this widget, or an empty string if none was assigned.

Returns `String`: the widget identifier string.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleId(String)` {: #styleidstring data-toc-label="styleId(String)" }

**Set style ID**

Selects an explicit style rule from an active [StylePack](<../styling/style-pack.md>) for this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleId` | `String` | [StylePack](<../styling/style-pack.md>) style id token. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleId()` {: #styleid data-toc-label="styleId()" }

**Get style ID**

Returns the explicit [StylePack](<../styling/style-pack.md>) style id assigned to this widget.

Returns `String`: the active [StylePack](<../styling/style-pack.md>) style id, or empty string.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`style(String)` {: #stylestring data-toc-label="style(String)" }

**Inline style**

Applies inline declarations, or treats a bare token as a [StylePack](<../styling/style-pack.md>) style id.

A string without `:` or `;` is passed to `styleId`. Otherwise it is parsed as `property: value;` declarations and merged into the widget inline style. Parser diagnostics stay in `styleDiagnostics()`.

| Parameter | Type | Description |
| --- | --- | --- |
| `declarations` | `String` | Inline declarations, or a [StylePack](<../styling/style-pack.md>) id when the string has no `:` or `;`. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`style(Consumer)` {: #styleconsumer data-toc-label="style(Consumer)" }

**Configure inline style**

Applies typed inline style declarations through a mutable style builder callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `update` | `Consumer` | Callback mutating properties on [MutableStyle](<../styling/mutable-style.md>). |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleClear()` {: #styleclear data-toc-label="styleClear()" }

**Clear inline style**

Removes all inline style declarations and clears parser diagnostics.

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleDiagnostics()` {: #stylediagnostics data-toc-label="styleDiagnostics()" }

**[Style](<../styling/style.md>) diagnostics**

Returns diagnostics and warnings collected while parsing inline style strings.

Returns `List`: list of style parser diagnostics.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleClass(String)` {: #styleclassstring data-toc-label="styleClass(String)" }

**Set style classes**

Replaces the widget's style classes with a space-separated class list.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClasses` | `String` | Space-separated style class tokens. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`addStyleClass(String)` {: #addstyleclassstring data-toc-label="addStyleClass(String)" }

**Add style class**

Adds a normalized style class selector without removing existing classes.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClass` | `String` | Class token to add. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`removeStyleClass(String)` {: #removestyleclassstring data-toc-label="removeStyleClass(String)" }

**Remove style class**

Removes a style class selector if it is currently assigned.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClass` | `String` | Class token to remove. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`hasStyleClass(String)` {: #hasstyleclassstring data-toc-label="hasStyleClass(String)" }

**Has style class**

Checks whether the specified style class is assigned to this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClass` | `String` | Class token to check. |

Returns `boolean`: true if the class is present, false otherwise.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleClasses()` {: #styleclasses data-toc-label="styleClasses()" }

**Get style classes**

Returns an unmodifiable view of all normalized style classes assigned to this widget.

Returns `List`: list of style classes.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleScope(boolean)` {: #stylescopeboolean data-toc-label="styleScope(boolean)" }

**Set style scope**

Limits local style inheritance to this widget subtree when enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleScope` | `boolean` | True to isolate local style lookups to this subtree. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`styleScope()` {: #stylescope data-toc-label="styleScope()" }

**Get style scope**

Checks whether this widget forms a local style scope boundary.

Returns `boolean`: true if style scoping is active.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`localStyle(String, Style)` {: #localstylestring-style data-toc-label="localStyle(String, Style)" }

**Define local style**

Sets or clears a local style override for a specific widget type within this subtree.

| Parameter | Type | Description |
| --- | --- | --- |
| `widgetType` | `String` | Target widget type name. |
| `style` | `Style` | [Style](<../styling/style.md>) override to apply, or null to remove. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`localStyle(String)` {: #localstylestring data-toc-label="localStyle(String)" }

**Get local style**

Retrieves the local style override defined on this widget for a given widget type.

| Parameter | Type | Description |
| --- | --- | --- |
| `widgetType` | `String` | Target widget type name. |

Returns `Style`: the local style override, or null if none.

</div>

### Layout & Geometry

| Method | Description |
| --- | --- |
| [`layout(Consumer)`](#layoutconsumer) | Updates the live `LayoutStyle` and batches the resulting layout invalidation. |
| [`layoutStyle()`](#layoutstyle) | Returns the live [LayoutStyle](<../layout/layout-style.md>) instance governing this widget's geometry. |
| [`layoutBounds()`](#layoutbounds) | Returns the current arranged bounds of the widget in parent coordinates. |
| [`desiredSize()`](#desiredsize) | Returns the dimensions requested during the most recent measurement pass. |
| [`layoutTransition(float)`](#layouttransitionfloat) | Enables FLIP animation for subsequent layout-driven repositioning. |
| [`layoutTransition(float, Easing)`](#layouttransitionfloat-easing) | Enables FLIP movement animation with custom duration and easing curve. |
| [`layoutTransitionsEnabled(boolean)`](#layouttransitionsenabledboolean) | Enables or disables automatic FLIP transitions when layout coordinates change. |
| [`layoutTransitionsEnabled()`](#layouttransitionsenabled) | Checks whether FLIP layout transitions are currently enabled. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layout(Consumer)` {: #layoutconsumer data-toc-label="layout(Consumer)" }

**Apply layout**

Updates the live `LayoutStyle` and batches the resulting layout invalidation.

| Parameter | Type | Description |
| --- | --- | --- |
| `update` | `Consumer` | Callback that mutates layout size, spacing, flex, grid, or position. Null is a no-op. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutStyle()` {: #layoutstyle data-toc-label="layoutStyle()" }

**Get layout style**

Returns the live [LayoutStyle](<../layout/layout-style.md>) instance governing this widget's geometry.

Returns `LayoutStyle`: the live [LayoutStyle](<../layout/layout-style.md>).

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutBounds()` {: #layoutbounds data-toc-label="layoutBounds()" }

**Get layout bounds**

Returns the current arranged bounds of the widget in parent coordinates.

Returns `RectView`: current arranged bounds.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`desiredSize()` {: #desiredsize data-toc-label="desiredSize()" }

**Get desired size**

Returns the dimensions requested during the most recent measurement pass.

Returns `LayoutSize`: most recently measured desired size.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransition(float)` {: #layouttransitionfloat data-toc-label="layoutTransition(float)" }

**Enable layout transition**

Enables FLIP animation for subsequent layout-driven repositioning.

| Parameter | Type | Description |
| --- | --- | --- |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransition(float, Easing)` {: #layouttransitionfloat-easing data-toc-label="layoutTransition(float, Easing)" }

**Enable layout transition with easing**

Enables FLIP movement animation with custom duration and easing curve.

| Parameter | Type | Description |
| --- | --- | --- |
| `durationSeconds` | `float` | Transition duration in seconds. |
| `easing` | `Easing` | Easing curve for smooth movement. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransitionsEnabled(boolean)` {: #layouttransitionsenabledboolean data-toc-label="layoutTransitionsEnabled(boolean)" }

**Toggle layout transitions**

Enables or disables automatic FLIP transitions when layout coordinates change.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | True to animate layout changes. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`layoutTransitionsEnabled()` {: #layouttransitionsenabled data-toc-label="layoutTransitionsEnabled()" }

**Layout transitions enabled**

Checks whether FLIP layout transitions are currently enabled.

Returns `boolean`: true if layout transitions are active.

</div>

### State & Interaction

| Method | Description |
| --- | --- |
| [`visibility(Visibility)`](#visibilityvisibility) | Sets whether the widget is shown, hidden in place, or removed from layout. |
| [`visibility()`](#visibility) | Returns the current Visibility mode (VISIBLE, HIDDEN, or COLLAPSED). |
| [`visible(boolean)`](#visibleboolean) | Convenience toggle setting VISIBLE when true, or HIDDEN when false. |
| [`visible()`](#visible) | Checks whether the widget's visibility mode is strictly VISIBLE. |
| [`enabled(boolean)`](#enabledboolean) | Enables or disables user interaction and applies the :disabled pseudo-class. |
| [`enabled()`](#enabled) | Checks whether the widget accepts user interaction. |
| [`hovered()`](#hovered) | Checks whether the mouse cursor is currently positioned over this widget. |
| [`mouseCursor(MouseCursor)`](#mousecursormousecursor) | Sets the cursor icon displayed when hovering over this enabled, visible widget. |
| [`mouseCursor()`](#mousecursor) | Returns the cursor shape configured on this widget. |
| [`mouseCursorAt(float, float)`](#mousecursoratfloat-float) | Resolves the appropriate cursor shape for a specific local coordinate offset. |
| [`focusable(boolean)`](#focusableboolean) | Enables or disables keyboard tab navigation and focus for this widget. |
| [`focusable()`](#focusable) | Checks whether the widget can receive keyboard focus. |
| [`focusScope(boolean)`](#focusscopeboolean) | Makes this widget a boundary that traps tab-key navigation within its children. |
| [`focusScope()`](#focusscope) | Checks whether this widget acts as an isolated focus scope boundary. |
| [`focusOrder(int)`](#focusorderint) | Sets the explicit relative tab sequence index for keyboard navigation. |
| [`focusOrder()`](#focusorder) | Returns the relative tab index assigned to this widget. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visibility(Visibility)` {: #visibilityvisibility data-toc-label="visibility(Visibility)" }

**Visibility**

Sets whether the widget is shown, hidden in place, or removed from layout.

`VISIBLE` participates in layout, rendering, input, and focus. `HIDDEN` keeps its layout space but skips rendering and input. `COLLAPSED` leaves the layout and invalidates both layout and visual state. Null becomes `VISIBLE`.

| Parameter | Type | Description |
| --- | --- | --- |
| `visibility` | `Visibility` | `VISIBLE`, `HIDDEN`, or `COLLAPSED`. Null means `VISIBLE`. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visibility()` {: #visibility data-toc-label="visibility()" }

**Get visibility**

Returns the current Visibility mode (VISIBLE, HIDDEN, or COLLAPSED).

Returns `Visibility`: current visibility mode.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

**Set visible**

Convenience toggle setting VISIBLE when true, or HIDDEN when false.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | True for VISIBLE; false for HIDDEN. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`visible()` {: #visible data-toc-label="visible()" }

**Is visible**

Checks whether the widget's visibility mode is strictly VISIBLE.

Returns `boolean`: true if VISIBLE.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

**Set enabled**

Enables or disables user interaction and applies the :disabled pseudo-class.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | True to allow user interaction, false to disable. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

**Is enabled**

Checks whether the widget accepts user interaction.

Returns `boolean`: true if enabled.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`hovered()` {: #hovered data-toc-label="hovered()" }

**Is hovered**

Checks whether the mouse cursor is currently positioned over this widget.

Returns `boolean`: true if mouse is over the widget.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`mouseCursor(MouseCursor)` {: #mousecursormousecursor data-toc-label="mouseCursor(MouseCursor)" }

**Set mouse cursor**

Sets the cursor icon displayed when hovering over this enabled, visible widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `mouseCursor` | `MouseCursor` | Desired mouse cursor shape. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`mouseCursor()` {: #mousecursor data-toc-label="mouseCursor()" }

**Get mouse cursor**

Returns the cursor shape configured on this widget.

Returns `MouseCursor`: the configured mouse cursor.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`mouseCursorAt(float, float)` {: #mousecursoratfloat-float data-toc-label="mouseCursorAt(float, float)" }

**Get mouse cursor at coordinates**

Resolves the appropriate cursor shape for a specific local coordinate offset.

| Parameter | Type | Description |
| --- | --- | --- |
| `localX` | `float` | Local X coordinate. |
| `localY` | `float` | Local Y coordinate. |

Returns `MouseCursor`: cursor shape at coordinates.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusable(boolean)` {: #focusableboolean data-toc-label="focusable(boolean)" }

**Set focusable**

Enables or disables keyboard tab navigation and focus for this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `focusable` | `boolean` | True to participate in keyboard focus. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusable()` {: #focusable data-toc-label="focusable()" }

**Is focusable**

Checks whether the widget can receive keyboard focus.

Returns `boolean`: true if focusable.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusScope(boolean)` {: #focusscopeboolean data-toc-label="focusScope(boolean)" }

**Set focus scope**

Makes this widget a boundary that traps tab-key navigation within its children.

| Parameter | Type | Description |
| --- | --- | --- |
| `focusScope` | `boolean` | True to trap keyboard focus within this subtree. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusScope()` {: #focusscope data-toc-label="focusScope()" }

**Is focus scope**

Checks whether this widget acts as an isolated focus scope boundary.

Returns `boolean`: true if focus scope is active.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusOrder(int)` {: #focusorderint data-toc-label="focusOrder(int)" }

**Set focus order**

Sets the explicit relative tab sequence index for keyboard navigation.

| Parameter | Type | Description |
| --- | --- | --- |
| `focusOrder` | `int` | Relative index for tab ordering. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`focusOrder()` {: #focusorder data-toc-label="focusOrder()" }

**Get focus order**

Returns the relative tab index assigned to this widget.

Returns `int`: relative tab index.

</div>

### Transforms & Animation

| Method | Description |
| --- | --- |
| [`opacity(float)`](#opacityfloat) | Sets the widget opacity immediately (clamped to 0.0 - 1.0) and repaints. |
| [`opacity()`](#opacity) | Returns the current visual opacity of the widget. |
| [`rotationDegrees(float)`](#rotationdegreesfloat) | Sets the visual rotation angle immediately around the active pivot. |
| [`transformOrigin(TransformOrigin)`](#transformorigintransformorigin) | Selects a named preset pivot (e.g. CENTER, TOP_LEFT) for rotation and scaling. |
| [`transformPivot(float, float)`](#transformpivotfloat-float) | Sets a custom transform pivot point in local pixel coordinates. |
| [`transform()`](#transform) | Returns the live Transform model governing offsets, rotation, scale, and pivot. |
| [`animate(AnimatedProperty, float, float)`](#animateanimatedproperty-float-float) | Smoothly transitions a built-in property to a target value over a duration in seconds. |
| [`animate(AnimatedProperty, float, TransitionSpec)`](#animateanimatedproperty-float-transitionspec) | Transitions a built-in property to a target, or applies it immediately when duration is not positive. |
| [`animateOpacity(float, float)`](#animateopacityfloat-float) | Smoothly transitions opacity to a target value using standard easing. |
| [`animatePosition(float, float, float)`](#animatepositionfloat-float-float) | Animates visual transform displacement offsets along the X and Y axes. |
| [`animatePositionFrom(float, float, float, float, float)`](#animatepositionfromfloat-float-float-float-float) | Animates transform position starting explicitly from (startX, startY) to (endX, endY). |
| [`animateScale(float, float, float)`](#animatescalefloat-float-float) | Smoothly transitions visual scale multipliers on both axes. |
| [`shake(float, float)`](#shakefloat-float) | Oscillates the widget back and forth to provide visual error or rejection feedback. |
| [`stopShakeAnimations()`](#stopshakeanimations) | Cancels any active shake animations and restores the widget's resting position. |
| [`animateSpring(AnimatedProperty, float, float, float)`](#animatespringanimatedproperty-float-float-float) | Runs a physically simulated harmonic oscillator animation with natural bounce and overshoot. |
| [`animateColor(MutableColor, ColorView, float)`](#animatecolormutablecolor-colorview-float) | Smoothly interpolates an RGBA color property to a target color value. |
| [`stopColorAnimations()`](#stopcoloranimations) | Stops all active color interpolation animations on this widget. |
| [`playStoryboard(Storyboard)`](#playstoryboardstoryboard) | Executes a multi-keyframe timeline storyboard across this widget and its descendants. |
| [`playStoryboard(Object, Storyboard)`](#playstoryboardobject-storyboard) | Plays a storyboard under an explicit key, canceling any previous storyboard with that key. |
| [`stopStoryboard(Object)`](#stopstoryboardobject) | Stops and removes the active storyboard registered under the specified key. |
| [`stopAnimation(AnimatedProperty)`](#stopanimationanimatedproperty) | Stops running transitions specifically targeted at the given property. |
| [`stopAnimations()`](#stopanimations) | Stops every running transition, spring, shake, and storyboard on this widget. |
| [`animationRunning(AnimatedProperty)`](#animationrunninganimatedproperty) | Checks whether an animation is currently running for a specific property. |
| [`animationsRunning()`](#animationsrunning) | Checks whether any animations are currently playing on this widget. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`opacity(float)` {: #opacityfloat data-toc-label="opacity(float)" }

**Set opacity**

Sets the widget opacity immediately (clamped to 0.0 - 1.0) and repaints.

| Parameter | Type | Description |
| --- | --- | --- |
| `opacity` | `float` | Opacity value from 0.0 (transparent) to 1.0 (opaque). |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`opacity()` {: #opacity data-toc-label="opacity()" }

**Get opacity**

Returns the current visual opacity of the widget.

Returns `float`: opacity value between 0.0 and 1.0.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`rotationDegrees(float)` {: #rotationdegreesfloat data-toc-label="rotationDegrees(float)" }

**Set rotation degrees**

Sets the visual rotation angle immediately around the active pivot.

| Parameter | Type | Description |
| --- | --- | --- |
| `degrees` | `float` | Rotation angle in degrees. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`transformOrigin(TransformOrigin)` {: #transformorigintransformorigin data-toc-label="transformOrigin(TransformOrigin)" }

**Set transform origin**

Selects a named preset pivot (e.g. CENTER, TOP_LEFT) for rotation and scaling.

| Parameter | Type | Description |
| --- | --- | --- |
| `origin` | `TransformOrigin` | Named transform origin preset. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`transformPivot(float, float)` {: #transformpivotfloat-float data-toc-label="transformPivot(float, float)" }

**Set transform pivot**

Sets a custom transform pivot point in local pixel coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Pivot X in local pixels. |
| `y` | `float` | Pivot Y in local pixels. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`transform()` {: #transform data-toc-label="transform()" }

**Get transform**

Returns the live Transform model governing offsets, rotation, scale, and pivot.

Returns `Transform`: the active Transform instance.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animate(AnimatedProperty, float, float)` {: #animateanimatedproperty-float-float data-toc-label="animate(AnimatedProperty, float, float)" }

**Animate property with duration**

Smoothly transitions a built-in property to a target value over a duration in seconds.

| Parameter | Type | Description |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Built-in property such as opacity, position, scale, rotation, or pivot. |
| `targetValue` | `float` | Target value. Opacity is clamped to 0..1; non-finite values are sanitized. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animate(AnimatedProperty, float, TransitionSpec)` {: #animateanimatedproperty-float-transitionspec data-toc-label="animate(AnimatedProperty, float, TransitionSpec)" }

**Animate a property**

Transitions a built-in property to a target, or applies it immediately when duration is not positive.

A matching in-flight transition is retargeted instead of restarted. Rotation uses the shortest angle path. A null property is ignored; a null spec uses `TransitionSpec.DEFAULT`.

| Parameter | Type | Description |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Built-in property such as opacity, position, scale, rotation, or pivot. |
| `targetValue` | `float` | Target value. Opacity is clamped to 0..1; non-finite values are sanitized. |
| `spec` | `TransitionSpec` | Duration and easing. Null uses the default spec; non-positive duration snaps to the target. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateOpacity(float, float)` {: #animateopacityfloat-float data-toc-label="animateOpacity(float, float)" }

**Animate opacity**

Smoothly transitions opacity to a target value using standard easing.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetOpacity` | `float` | Target opacity between 0.0 and 1.0. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animatePosition(float, float, float)` {: #animatepositionfloat-float-float data-toc-label="animatePosition(float, float, float)" }

**Animate position**

Animates visual transform displacement offsets along the X and Y axes.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Target X offset in pixels. |
| `y` | `float` | Target Y offset in pixels. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animatePositionFrom(float, float, float, float, float)` {: #animatepositionfromfloat-float-float-float-float data-toc-label="animatePositionFrom(float, float, float, float, float)" }

**Animate position from start**

Animates transform position starting explicitly from (startX, startY) to (endX, endY).

| Parameter | Type | Description |
| --- | --- | --- |
| `startX` | `float` | Starting X offset. |
| `startY` | `float` | Starting Y offset. |
| `endX` | `float` | Target ending X offset. |
| `endY` | `float` | Target ending Y offset. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateScale(float, float, float)` {: #animatescalefloat-float-float data-toc-label="animateScale(float, float, float)" }

**Animate scale**

Smoothly transitions visual scale multipliers on both axes.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Target scale multiplier on X axis. |
| `y` | `float` | Target scale multiplier on Y axis. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`shake(float, float)` {: #shakefloat-float data-toc-label="shake(float, float)" }

**Trigger shake animation**

Oscillates the widget back and forth to provide visual error or rejection feedback.

| Parameter | Type | Description |
| --- | --- | --- |
| `amplitude` | `float` | Shake displacement amplitude in pixels. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopShakeAnimations()` {: #stopshakeanimations data-toc-label="stopShakeAnimations()" }

**Stop shake animations**

Cancels any active shake animations and restores the widget's resting position.

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateSpring(AnimatedProperty, float, float, float)` {: #animatespringanimatedproperty-float-float-float data-toc-label="animateSpring(AnimatedProperty, float, float, float)" }

**Animate spring physics**

Runs a physically simulated harmonic oscillator animation with natural bounce and overshoot.

| Parameter | Type | Description |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Built-in property such as opacity, position, scale, rotation, or pivot. |
| `targetValue` | `float` | Target value. Opacity is clamped to 0..1; non-finite values are sanitized. |
| `stiffness` | `float` | Spring stiffness coefficient (higher = faster pull). |
| `damping` | `float` | Damping coefficient (higher = less oscillation). |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animateColor(MutableColor, ColorView, float)` {: #animatecolormutablecolor-colorview-float data-toc-label="animateColor(MutableColor, ColorView, float)" }

**Animate color**

Smoothly interpolates an RGBA color property to a target color value.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `MutableColor` | MutableColor target to mutate. |
| `targetColor` | `ColorView` | Destination color value. |
| `durationSeconds` | `float` | Transition duration in seconds. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopColorAnimations()` {: #stopcoloranimations data-toc-label="stopColorAnimations()" }

**Stop color animations**

Stops all active color interpolation animations on this widget.

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`playStoryboard(Storyboard)` {: #playstoryboardstoryboard data-toc-label="playStoryboard(Storyboard)" }

**Play storyboard**

Executes a multi-keyframe timeline storyboard across this widget and its descendants.

| Parameter | Type | Description |
| --- | --- | --- |
| `storyboard` | `Storyboard` | Storyboard animation timeline to play. |

Returns `StoryboardPlayer`: StoryboardPlayer controlling playback.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`playStoryboard(Object, Storyboard)` {: #playstoryboardobject-storyboard data-toc-label="playStoryboard(Object, Storyboard)" }

**Play keyed storyboard**

Plays a storyboard under an explicit key, canceling any previous storyboard with that key.

| Parameter | Type | Description |
| --- | --- | --- |
| `key` | `Object` | Unique tracking key. |
| `storyboard` | `Storyboard` | Storyboard animation timeline to play. |

Returns `StoryboardPlayer`: StoryboardPlayer controlling playback.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopStoryboard(Object)` {: #stopstoryboardobject data-toc-label="stopStoryboard(Object)" }

**Stop storyboard**

Stops and removes the active storyboard registered under the specified key.

| Parameter | Type | Description |
| --- | --- | --- |
| `key` | `Object` | Unique tracking key. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopAnimation(AnimatedProperty)` {: #stopanimationanimatedproperty data-toc-label="stopAnimation(AnimatedProperty)" }

**Stop animation for property**

Stops running transitions specifically targeted at the given property.

| Parameter | Type | Description |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Built-in property such as opacity, position, scale, rotation, or pivot. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`stopAnimations()` {: #stopanimations data-toc-label="stopAnimations()" }

**Stop all animations**

Stops every running transition, spring, shake, and storyboard on this widget.

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animationRunning(AnimatedProperty)` {: #animationrunninganimatedproperty data-toc-label="animationRunning(AnimatedProperty)" }

**Is property animating**

Checks whether an animation is currently running for a specific property.

| Parameter | Type | Description |
| --- | --- | --- |
| `property` | `AnimatedProperty` | Built-in property such as opacity, position, scale, rotation, or pivot. |

Returns `boolean`: true if property has an active animation.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`animationsRunning()` {: #animationsrunning data-toc-label="animationsRunning()" }

**Are animations running**

Checks whether any animations are currently playing on this widget.

Returns `boolean`: true if any animation is active.

</div>

### Bindings

| Method | Description |
| --- | --- |
| [`bind(BindingData, Object)`](#bindbindingdata-object) | Connects a supported property key to a signal and returns this widget. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`bind(BindingData, Object)` {: #bindbindingdata-object data-toc-label="bind(BindingData, Object)" }

**Bind property to signal**

Connects a supported property key to a signal and returns this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `key` | `BindingData` | Identity key naming the widget property and expected signal type. |
| `signal` | `Object` | Signal that supplies the property's value and receives widget changes. |

Returns `WidgetBase`: This widget for fluent configuration.

</div>

### Rendering & Caching

| Method | Description |
| --- | --- |
| [`renderer(WidgetRender)`](#rendererwidgetrender) | Replaces this widget's standard visual with one renderer instance. |
| [`renderer()`](#renderer) | Returns the custom procedural renderer assigned to this widget, or null if using default drawing. |
| [`renderCachingEnabled(boolean)`](#rendercachingenabledboolean) | Enables or disables retained draw command caching for this widget. |
| [`renderCachingEnabled()`](#rendercachingenabled) | Checks whether retained draw command caching is active. |
| [`invalidateRenderCache()`](#invalidaterendercache) | Marks the retained draw command buffer stale, forcing a record on the next paint pass. |
| [`render(RenderContext)`](#renderrendercontext) | Executes drawing for this widget: evaluates custom renderer or standard content, then draws children. |
| [`renderCached(RenderContext)`](#rendercachedrendercontext) | Replays cached draw commands if valid; otherwise records new commands and caches them. |
| [`measure(LayoutContext)`](#measurelayoutcontext) | Computes desiredSize based on widget content and LayoutContext constraints. |
| [`measureCached(LayoutContext)`](#measurecachedlayoutcontext) | Reuses cached desired dimensions if layout constraints match and subtree is not dirty. |
| [`arrange(RectView)`](#arrangerectview) | Assigns final positioned bounds to this widget and positions child elements. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderer(WidgetRender)` {: #rendererwidgetrender data-toc-label="renderer(WidgetRender)" }

**Custom renderer**

Replaces this widget's standard visual with one renderer instance.

The renderer draws the widget background and content. Framework traversal still paints children. Null restores the standard visual. [Style](<../styling/style.md>)-declared renderers apply only when this slot is empty.

| Parameter | Type | Description |
| --- | --- | --- |
| `renderer` | `WidgetRender` | Per-instance `WidgetRender`, or null for the standard visual. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderer()` {: #renderer data-toc-label="renderer()" }

**Get renderer**

Returns the custom procedural renderer assigned to this widget, or null if using default drawing.

Returns `WidgetRender`: the active WidgetRender, or null.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderCachingEnabled(boolean)` {: #rendercachingenabledboolean data-toc-label="renderCachingEnabled(boolean)" }

**Toggle render caching**

Enables or disables retained draw command caching for this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | True to enable retained caching. |

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderCachingEnabled()` {: #rendercachingenabled data-toc-label="renderCachingEnabled()" }

**Is render caching enabled**

Checks whether retained draw command caching is active.

Returns `boolean`: true if caching is enabled.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`invalidateRenderCache()` {: #invalidaterendercache data-toc-label="invalidateRenderCache()" }

**Invalidate render cache**

Marks the retained draw command buffer stale, forcing a record on the next paint pass.

Returns `WidgetBase`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`render(RenderContext)` {: #renderrendercontext data-toc-label="render(RenderContext)" }

**Render widget**

Executes drawing for this widget: evaluates custom renderer or standard content, then draws children.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext providing draw canvas and state. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`renderCached(RenderContext)` {: #rendercachedrendercontext data-toc-label="renderCached(RenderContext)" }

**Render with cache**

Replays cached draw commands if valid; otherwise records new commands and caches them.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext providing draw canvas and state. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`measure(LayoutContext)` {: #measurelayoutcontext data-toc-label="measure(LayoutContext)" }

**Measure dimensions**

Computes desiredSize based on widget content and LayoutContext constraints.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `LayoutContext` | LayoutContext providing parent bounds and available space. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`measureCached(LayoutContext)` {: #measurecachedlayoutcontext data-toc-label="measureCached(LayoutContext)" }

**Measure with cache**

Reuses cached desired dimensions if layout constraints match and subtree is not dirty.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `LayoutContext` | LayoutContext providing parent bounds and available space. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`arrange(RectView)` {: #arrangerectview data-toc-label="arrange(RectView)" }

**Arrange layout bounds**

Assigns final positioned bounds to this widget and positions child elements.

| Parameter | Type | Description |
| --- | --- | --- |
| `bounds` | `RectView` | Final positioned bounds rectangle. |

</div>

### Events & Lifecycle

| Method | Description |
| --- | --- |
| [`on(EventType, EventListener)`](#oneventtype-eventlistener) | Registers a typed event listener and returns a subscription handle to unregister. |
| [`on(EventType, boolean, EventListener)`](#oneventtype-boolean-eventlistener) | Registers a typed listener with explicit control over whether cancelled events are received. |
| [`emit(Event)`](#emitevent) | Dispatches an event to listeners registered directly on this widget. |
| [`handle(Event)`](#handleevent) | Dispatches an event and handles standard input routing logic. |
| [`invalidate(int)`](#invalidateint) | Marks layout or visual state dirty and bubbles the flags to ancestors. |
| [`clearInvalidation(int)`](#clearinvalidationint) | Clears dirty invalidation flags after layout or paint processing completes. |
| [`invalidationFlags()`](#invalidationflags) | Returns bitmask of pending invalidation flags on this specific widget. |
| [`subtreeInvalidationFlags()`](#subtreeinvalidationflags) | Returns combined bitmask of pending invalidation flags in this widget's entire subtree. |
| [`tick(FrameContext)`](#tickframecontext) | Advances animations, transitions, and time-based state for this frame. |
| [`dispose()`](#dispose) | Cleans up resources, terminates running animations, and unregisters event listeners. |
| [`parent()`](#parent) | Returns the parent container widget, or null if this is a root or unattached widget. |
| [`children()`](#children) | Returns an unmodifiable list of child widgets contained within this widget. |
| [`uiContext()`](#uicontext) | Returns the active UIContext providing window access, scale factor, and input focus. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`on(EventType, EventListener)` {: #oneventtype-eventlistener data-toc-label="on(EventType, EventListener)" }

**Subscribe to event**

Registers a typed event listener and returns a subscription handle to unregister.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | EventType token identifying event class. |
| `listener` | `EventListener` | Callback receiving event instances. |

Returns `EventSubscription`: EventSubscription for cancellation.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`on(EventType, boolean, EventListener)` {: #oneventtype-boolean-eventlistener data-toc-label="on(EventType, boolean, EventListener)" }

**Subscribe with cancelled events**

Registers a typed listener with explicit control over whether cancelled events are received.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | EventType token identifying event class. |
| `receiveCancelled` | `boolean` | True to receive cancelled events. |
| `listener` | `EventListener` | Callback receiving event instances. |

Returns `EventSubscription`: EventSubscription for cancellation.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`emit(Event)` {: #emitevent data-toc-label="emit(Event)" }

**Emit event**

Dispatches an event to listeners registered directly on this widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `event` | `Event` | Event instance to dispatch. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`handle(Event)` {: #handleevent data-toc-label="handle(Event)" }

**Handle event**

Dispatches an event and handles standard input routing logic.

| Parameter | Type | Description |
| --- | --- | --- |
| `event` | `Event` | Event instance to dispatch. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`invalidate(int)` {: #invalidateint data-toc-label="invalidate(int)" }

**Invalidate**

Marks layout or visual state dirty and bubbles the flags to ancestors.

Use `InvalidationFlags.LAYOUT` when measurement or placement changes, and `VISUAL` for a paint-only change. The call also dirties the retained render cache. `NONE` does nothing.

| Parameter | Type | Description |
| --- | --- | --- |
| `flags` | `int` | Bit flags from `InvalidationFlags`, usually `LAYOUT`, `VISUAL`, or both. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`clearInvalidation(int)` {: #clearinvalidationint data-toc-label="clearInvalidation(int)" }

**Clear invalidation flags**

Clears dirty invalidation flags after layout or paint processing completes.

| Parameter | Type | Description |
| --- | --- | --- |
| `flags` | `int` | Bit flags from `InvalidationFlags`, usually `LAYOUT`, `VISUAL`, or both. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`invalidationFlags()` {: #invalidationflags data-toc-label="invalidationFlags()" }

**Get invalidation flags**

Returns bitmask of pending invalidation flags on this specific widget.

Returns `int`: bitmask of dirty flags.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`subtreeInvalidationFlags()` {: #subtreeinvalidationflags data-toc-label="subtreeInvalidationFlags()" }

**Get subtree invalidation flags**

Returns combined bitmask of pending invalidation flags in this widget's entire subtree.

Returns `int`: combined bitmask of subtree dirty flags.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`tick(FrameContext)` {: #tickframecontext data-toc-label="tick(FrameContext)" }

**Tick frame update**

Advances animations, transitions, and time-based state for this frame.

| Parameter | Type | Description |
| --- | --- | --- |
| `frame` | `FrameContext` | FrameContext providing delta time and clock. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`dispose()` {: #dispose data-toc-label="dispose()" }

**Dispose widget**

Cleans up resources, terminates running animations, and unregisters event listeners.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`parent()` {: #parent data-toc-label="parent()" }

**Get parent widget**

Returns the parent container widget, or null if this is a root or unattached widget.

Returns `Widget`: the parent widget, or null.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`children()` {: #children data-toc-label="children()" }

**Get child widgets**

Returns an unmodifiable list of child widgets contained within this widget.

Returns `List`: list of child widgets.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">WidgetBase::</span>`uiContext()` {: #uicontext data-toc-label="uiContext()" }

**Get UI context**

Returns the active UIContext providing window access, scale factor, and input focus.

Returns `UIContext`: active UIContext.

</div>

