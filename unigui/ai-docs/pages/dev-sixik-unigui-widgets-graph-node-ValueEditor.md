# `ValueEditor`

Typed default-value editor of a node input: owns a widget, exposes the value and a stable text form.

- **Purpose:** Separates the value of a node input from its widget so graphs can be executed, saved, copied and restored.

### USE WHEN

- Give an input a default value editor via `PortRowModule.editor(...)` or `NodeBuilder.input(..., editor)`.
- Read or write an input value with `NodeGraphItem.value(portId)` and `value(portId, value)`.
- Implement a custom editor (curve, gradient) by implementing this interface.

### CRITICAL RULES & GOTCHAS:

- `silentValue` and `deserialize` never notify listeners; `value(Object)` notifies once if the value changed.
- `deserialize` ignores malformed text and keeps the current value.
- `serialize` must round-trip: `deserialize(serialize())` restores an equal value.

- **Related:** `ValueEditors`, `PortRowModule`, `NodeGraphItem`

