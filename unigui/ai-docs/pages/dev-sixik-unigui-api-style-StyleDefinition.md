# `StyleDefinition` (extends `Record`)

Immutable named style rule containing a selector, backend, properties, and event-to-animation bindings.

- **Purpose:** Defines how one reusable rule in a `StylePack` is selected and rendered.

### USE WHEN

- Add a declarative style rule targeted by widget type, class, or explicit style id.
- Attach a registered custom renderer or animation preset to a style rule.

### CRITICAL RULES & GOTCHAS:

- Every modifier returns a new `StyleDefinition`; retain or put the returned instance.
- `target` matches a widget style type, `styleClass` matches `addStyleClass`, and `widgetId` matches the widget's explicit `styleId` (not its general-purpose `id`).
- A custom renderer id must be registered in `WidgetRenderRegistry` before the style is resolved.
- Use stable event ids from `StyleAnimationIds.Event` when binding animation presets.

- **Related:** `StylePack`, `MutableStyle`, `StyleSelector`, `StyleAnimationDefinition`, `WidgetRenderRegistry`

### CREATION

- `of(String, Style)` → StyleDefinition — Creates a declarative rule backed only by typed style properties.
- `custom(String, String, Style)` → StyleDefinition — Creates a rule backed by a registered custom renderer plus optional properties.

### SELECTORS

- `target(String)` → StyleDefinition — Returns a copy restricted to a widget style type.
- `styleClass(String)` → StyleDefinition — Returns a copy restricted to a widget style class.
- `widgetId(String)` → StyleDefinition — Returns a copy restricted to a widget's explicit style id.

### ANIMATIONS

- `eventAnimation(String, String)` → StyleDefinition — Returns a copy that adds, replaces, or removes an event-to-animation binding.

### EXAMPLES

#### Target a primary button rule

```java
StyleDefinition primary = StyleDefinition.of(
        "button.primary",
        new MutableStyle().put(StyleKeys.BACKGROUND_COLOR, accent))
        .target(Button.STYLE_TYPE)
        .styleClass("primary");
```

