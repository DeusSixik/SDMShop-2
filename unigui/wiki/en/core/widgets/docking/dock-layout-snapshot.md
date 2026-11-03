---
title: "DockLayoutSnapshot"
slug: "dock-layout-snapshot"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockLayoutSnapshot

Immutable snapshot representing the recursive docking layout tree and active pane selection for serialization and layout persistence.

## Overview

`DockLayoutSnapshot` captures the complete recursive docking tree structure for persistence.

### Persistence Flow

1. Capture: `DockLayoutSnapshot snapshot = dockingManager.snapshot();`
2. Serialize: `String encoded = DockLayoutSnapshotCodec.encode(snapshot);`
3. Save: Persist `encoded` string into application settings or workspace file.
4. Restore: `dockingRoot.restoreLayout(snapshot, paneRegistry);`

## Capturing and Restoring Snapshots

```java
DockLayoutSnapshot snapshot = DockLayoutSnapshot.from(dockingManager.rootNode(), "editor-1");
```

## Related links

- [DockLayoutSnapshotCodec](<dock-layout-snapshot-codec.md>): Compact string serializer and deserializer (DLS1 format) for persisting docking layouts across application restarts.
- [DockingManager](<docking-manager.md>): Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.

## API

<div class="api-member-card" markdown="1">

### `DockLayoutSnapshot(Node)` {: #docklayoutsnapshotnode data-toc-label="DockLayoutSnapshot(Node)" }

Creates a layout snapshot with empty active pane.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `Node` | Root serialized node. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshot::</span>`from(DockNode)` {: #fromdocknode data-toc-label="from(DockNode)" }

Captures an immutable snapshot from a live [DockNode](<dock-node.md>) hierarchy.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `DockNode` | Live root dock node to snapshot. |

Returns `DockLayoutSnapshot`: Immutable snapshot of the docking hierarchy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshot::</span>`from(DockNode, String)` {: #fromdocknode-string data-toc-label="from(DockNode, String)" }

Captures an immutable snapshot from a live [DockNode](<dock-node.md>) hierarchy with active pane identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `root` | `DockNode` | Live root dock node to snapshot. |
| `activePaneId` | `String` | Identifier of the active dock pane. |

Returns `DockLayoutSnapshot`: Immutable snapshot of the docking hierarchy..

</div>

