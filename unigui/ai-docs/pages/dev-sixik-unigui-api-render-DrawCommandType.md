# `DrawCommandType` (extends `Enum`)

Enumeration of draw command categories recorded into DrawList.

- **Purpose:** Informs RenderBackend which payload fields in DrawCommand (bounds, paint, texture, uv, shader, path, mesh) must be processed.

### USE WHEN

- Differentiating command processing inside a RenderBackend drawing loop.
- Instantiating or recycling DrawCommand objects via DrawList#obtain(DrawCommandType).

### CRITICAL RULES & GOTCHAS:

- Backends must exhaustively handle or gracefully ignore all command types.
- PUSH_CLIP and POP_CLIP commands must always be processed in matching pairs.

- **Related:** `DrawCommand`, `DrawList`, `RenderBackend`

