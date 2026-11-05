# `DragPayload` (extends `Record`)

Immutable transfer payload conveying identifier, type, preview text, and optional user object between drag source and drop target.

### API

- `DragPayload(String, String, String)` — Constructs a drag payload without extra object data.
- `data(Class<T>)` → T — Returns the attached data cast to the specified class, or null if absent or type mismatch.
- `hasData()` → boolean — Checks whether this payload carries attached data.
- `matchesType(String)` → boolean — Checks whether the payload type matches the expected filter.
- `of(String, String, String)` → DragPayload — Factory method to create a payload without extra data.
- `of(String, String, String, Object)` → DragPayload — Factory method to create a payload with arbitrary in-process data.

