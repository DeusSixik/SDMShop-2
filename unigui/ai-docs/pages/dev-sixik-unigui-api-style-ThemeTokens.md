# `ThemeTokens`

Immutable design-token snapshot for colors, shapes, typography, and spacing.

- **Purpose:** Supplies consistent theme defaults as the lowest-priority active layer of a `Theme` or `StylePack`.

### USE WHEN

- Create a coherent palette and geometry baseline shared by many widget styles.
- Read token groups directly for custom widget or renderer behavior.

### CRITICAL RULES & GOTCHAS:

- Tokens are fallback values: StylePack rules and local widget styles take priority.
- `EMPTY` is inactive and contributes no values; `defaults()` creates an active neutral dark token set.
- Built snapshots are immutable; derive a new group with `toBuilder` and build a new `ThemeTokens` instance.
- Only keys mapped by `ThemeTokens` are supplied through `asStyle`; unknown keys preserve the caller fallback.

- **Related:** `Theme`, `StylePack`, `ThemeColors`, `ThemeShapes`, `ThemeTypography`, `ThemeSpacing`

### CREATION

- `builder()` → Builder — Starts a builder for an active immutable token snapshot.
- `defaults()` → ThemeTokens — Creates the active neutral dark default token set.

### INTEGRATION

- `asStyle()` → Style — Exposes mapped tokens as a read-only style layer for cascade composition.

### EXAMPLES

#### Create custom theme tokens

```java
ThemeTokens tokens = ThemeTokens.builder()
        .colors(colors -> colors.accent(accentColor))
        .shapes(shapes -> shapes.radiusMd(6.0f))
        .spacing(spacing -> spacing.controlGap(6.0f))
        .build();
```

