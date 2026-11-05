---
title: "DockLayoutSnapshotCodec"
slug: "dock-layout-snapshot-codec"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockLayoutSnapshotCodec

Compact string serializer and deserializer (DLS1 format) for persisting docking layouts across application restarts.

## Overview

`DockLayoutSnapshotCodec` serializes and deserializes `DockLayoutSnapshot` instances using the compact `DLS1` format.

Format handles recursive split nodes, split ratios, active tabs, and pane ID lists in a single string suitable for storage.

## Serializing and Deserializing Layout Snapshot

```java
String encoded = DockLayoutSnapshotCodec.encode(snapshot);
DockLayoutSnapshot restored = DockLayoutSnapshotCodec.decode(encoded);
```

## Related links

- [DockLayoutSnapshot](<dock-layout-snapshot.md>): Immutable snapshot representing the recursive docking layout tree and active pane selection for serialization and layout persistence.
- [DockingManager](<docking-manager.md>): Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshotCodec::</span>`decode(String)` {: #decodestring data-toc-label="decode(String)" }

Deserializes a DLS1-encoded string back into a [DockLayoutSnapshot](<dock-layout-snapshot.md>), falling back cleanly to an empty snapshot on errors.

| Parameter | Type | Description |
| --- | --- | --- |
| `encoded` | `String` | Encoded DLS1 snapshot string. |

Returns `DockLayoutSnapshot`: Decoded [DockLayoutSnapshot](<dock-layout-snapshot.md>) instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshotCodec::</span>`encode(DockLayoutSnapshot)` {: #encodedocklayoutsnapshot data-toc-label="encode(DockLayoutSnapshot)" }

Serializes a [DockLayoutSnapshot](<dock-layout-snapshot.md>) into a compact DLS1-encoded string.

| Parameter | Type | Description |
| --- | --- | --- |
| `snapshot` | `DockLayoutSnapshot` | Layout snapshot to encode. |

Returns `String`: Compact DLS1 encoded string representation..

</div>

