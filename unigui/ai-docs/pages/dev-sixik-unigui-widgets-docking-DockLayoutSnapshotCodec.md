# `DockLayoutSnapshotCodec`

Compact string serializer and deserializer (DLS1 format) for persisting docking layouts across application restarts.

### API

- `decode(String)` → DockLayoutSnapshot — Deserializes a DLS1-encoded string back into a DockLayoutSnapshot, falling back cleanly to an empty snapshot on errors.
- `encode(DockLayoutSnapshot)` → String — Serializes a DockLayoutSnapshot into a compact DLS1-encoded string.

