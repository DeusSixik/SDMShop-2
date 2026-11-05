---
title: "DockPolicy"
slug: "dock-policy"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockPolicy

Docking integration policy for windows: FULL (bidirectional docking and hosting), GUEST_ONLY (can dock into hosts but accepts no drops), or DISABLED.

## Overview

`DockPolicy` defines docking capabilities for window hosts.

| Policy | Dock Into Other Windows | Accept Foreign Panes |
| --- | --- | --- |
| `FULL` | Yes | Yes |
| `GUEST_ONLY` | Yes | No |
| `DISABLED` | No | No |

## Related links

- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPolicy::</span>`acceptsDrops()` {: #acceptsdrops data-toc-label="acceptsDrops()" }

Returns true if the window can accept dropped panels or foreign windows.

Returns `boolean`: True if this window accepts panes being dropped into it..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPolicy::</span>`canBeDocked()` {: #canbedocked data-toc-label="canBeDocked()" }

Returns true if the window can be dragged and docked into a host.

Returns `boolean`: True if this window can be docked into a host..

</div>

