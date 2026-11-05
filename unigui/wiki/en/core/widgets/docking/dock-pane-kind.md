---
title: "DockPaneKind"
slug: "dock-pane-kind"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockPaneKind

Classification of a docking pane: DOCUMENT (primary central workspace content) or TOOL (dockable side utility tool window).

## Overview

`DockPaneKind` categorizes panes within an IDE layout.

| Kind | Purpose |
| --- | --- |
| `DOCUMENT` | Main editor documents (code files, canvas, diagrams) occupying central workspace nodes. |
| `TOOL` | Utility side panels (project explorer, console, terminal, inspector) docked around editors. |

## Related links

- [DockPane](<dock-pane.md>): Descriptor representing an individual dockable pane hosting arbitrary widget content, tab title, icon, dirty state, and close guards.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

