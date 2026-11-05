# `StylePropertyTween` (extends `Record`)

Declarative transition of one named widget or style property between string-encoded values.

- **Purpose:** Describes one editable step inside a `StyleAnimationDefinition` without hard-coding a Java value type.

### USE WHEN

- Animate a property supported by the target widget's runtime animation adapter.

### CRITICAL RULES & GOTCHAS:

- `propertyName` must be non-blank and should use a constant from `StyleAnimationIds.Property` or the target widget.
- Values are strings interpreted by the target property's adapter; use the format expected by that property.
- Use `CURRENT` or `currentTo` to start from the current runtime value.
- A null transition is normalized to `TransitionSpec.DEFAULT`; a blank `toValue` remains adapter-dependent.

- **Related:** `StyleAnimationDefinition`, `StyleAnimationIds`, `TransitionSpec`

### CREATION

- `currentTo(String, String, TransitionSpec)` → StylePropertyTween — Creates a tween whose starting value is read from the current runtime property value.

