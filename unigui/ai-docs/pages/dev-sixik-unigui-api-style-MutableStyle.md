# `MutableStyle` (implements `Style`)

Fluent mutable implementation used to build typed styles and state overrides.

- **Purpose:** Builds or edits a style in Java while validating each value against its `StyleKey` type.

### USE WHEN

- Create a reusable style for a `StyleDefinition`, theme, or widget.
- Override a property for hover, pressed, selected, disabled, or another widget state.

### CRITICAL RULES & GOTCHAS:

- Use `put(key, value)` for `NORMAL`; use the state overload only for an override.
- The value must be null or an instance of `key.type()`, otherwise `put` throws `IllegalArgumentException`.
- Every write increments `version`; use `putAll` instead of mutating maps returned by `values`.
- A renderer set directly on a widget still has priority over a renderer stored in its style.

- **Related:** `Style`, `StyleKeys`, `WidgetState`, `StyleDefinition`, `WidgetRenderRegistry`

### PROPERTIES

- `put(StyleKey<T>, T)` → MutableStyle — Stores a typed property for the `NORMAL` widget state.
- `put(StyleKey<T>, WidgetState, T)` → MutableStyle — Stores a typed property override for one widget state.

### COMPOSITION

- `putAll(Style)` → MutableStyle — Copies all normal and state-specific values from another style.

### RENDERING

- `rendererId(String)` → MutableStyle — Selects a renderer registered by stable string id.

### EXAMPLES

#### Build state-aware colors

```java
Style buttonStyle = new MutableStyle()
        .put(StyleKeys.BACKGROUND_COLOR, normalColor)
        .put(StyleKeys.BACKGROUND_COLOR, WidgetState.HOVERED, hoverColor)
        .put(StyleKeys.TEXT_COLOR, textColor);
```

