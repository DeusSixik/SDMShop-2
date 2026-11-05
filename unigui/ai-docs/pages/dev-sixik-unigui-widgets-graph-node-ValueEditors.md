# `ValueEditors`

Factory of ready-made ValueEditors: text, number, integer, bool, color, choice and vector.

- **Purpose:** Covers the common default-value editors of Blueprint and Shader Graph inputs.

### USE WHEN

- Attach a typed default value to a node input: `NodeBuilder.input(id, label, type, ValueEditors.number(5))`.
- Edit a vec3 in one row via `ValueEditors.vector(3, 0, 64, 0)`.

### CRITICAL RULES & GOTCHAS:

- Value types: text String, number Double, integer Integer, bool Boolean, color Integer (ARGB), choice String, vector double[].
- Serialized forms are plain text: numbers as decimal, color as #AARRGGBB, vector as space-separated numbers.

- **Related:** `ValueEditor`, `PortRowModule`, `NodeBuilder`

