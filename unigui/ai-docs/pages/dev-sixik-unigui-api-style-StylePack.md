# `StylePack` (implements `Theme`)

Named theme containing reusable style rules, widget bindings, design tokens, and animation presets.

- **Purpose:** Acts as both a `Theme` and an editable style-sheet model for Java or XML-defined UI styling.

### USE WHEN

- Install a set of reusable styles on `UIContext.theme(...)`.
- Resolve selectors and explicit style ids for a concrete widget.
- Load or edit a complete XML `<StylePack>` including animations and renderer bindings.

### CRITICAL RULES & GOTCHAS:

- Use `Style.from` for a single `<Style>`; use `StylePack.from` for a full `<StylePack>` document.
- Later matching definitions override earlier ones regardless of selector specificity.
- `bind(widgetType, styleId)` supplies a base definition; matching selector rules can still layer over it.
- `resolveStyleFor` layers tokens, fallback, and matching definitions in that order.
- Adding or removing rules increments the pack version; returned maps and definition lists are snapshots.

- **Related:** `Theme`, `Style`, `MutableStyle`, `StyleDefinition`, `StyleAnimationDefinition`, `ThemeTokens`

### CREATION

- `create(String)` → StylePack — Creates an empty mutable style pack with a normalized id.
- `from(String)` → StylePack — Parses a complete XML style pack with the built-in property registry.
- `copy()` → StylePack — Creates an independent mutable copy of this pack, including state-aware style values.

### STYLE RULES

- `put(StyleDefinition)` → StylePack — Adds or replaces one named immutable style definition.
- `bind(String, String)` → StylePack — Binds a widget style type to a base definition id; an empty id removes the binding.

### ANIMATIONS

- `putAnimation(StyleAnimationDefinition)` → StylePack — Adds or replaces one named animation preset.

### RESOLUTION

- `resolveStyleFor(String, String, Collection<String>)` → Style — Resolves tokens, fallback, bindings, explicit style id, and selector rules into one style.

### EXAMPLES

#### Build and install a style pack

```java
StylePack pack = StylePack.create("app")
        .put(StyleDefinition.of("button.primary", primaryStyle)
                .target(Button.STYLE_TYPE)
                .styleClass("primary"));
context.theme(pack);
```

