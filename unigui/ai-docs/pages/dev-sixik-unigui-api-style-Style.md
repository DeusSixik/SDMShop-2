# `Style`

Read-only, typed style values with optional widget-state overrides.

- **Purpose:** Represents one style layer that widgets, themes, and style packs can query without casting arbitrary maps.

### USE WHEN

- Read a visual property with an explicit fallback.
- Parse a standalone XML `<Style>` element or inline style declaration.

### CRITICAL RULES & GOTCHAS:

- Always query with the matching `StyleKey<T>`; the key defines the Java value type.
- State lookup may fall back to `WidgetState.NORMAL`, then to the caller-supplied fallback.
- `Style.from` parses one `<Style>` only; use `StylePack.from` for definitions, selectors, bindings, and animations.
- Treat maps returned by `values` as read-only snapshots.

- **Related:** `MutableStyle`, `StyleKey`, `StyleKeys`, `WidgetState`, `StylePack`

### PARSING

- `from(String)` → Style — Parses one XML `<Style>` element with the built-in property registry.
- `from(String, StyleKeyRegistry)` → Style — Parses one XML `<Style>` element with a custom property registry.
- `fromText(String)` → Style — Parses semicolon-separated inline declarations and skips invalid entries.

### READING

- `get(StyleKey<T>, WidgetState, T)` → T — Reads a typed property for a widget state, using the supplied fallback when absent.

### EXAMPLES

#### Read a state-aware value

```java
ColorView color = style.get(
        StyleKeys.TEXT_COLOR,
        WidgetState.HOVERED,
        fallbackColor);
```

