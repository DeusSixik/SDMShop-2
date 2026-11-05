---
title: "TreeList"
slug: "tree-list"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# TreeList

Specialized [TreeView](<tree-view.md>) for hierarchical path navigation and directory listing.

## Overview

Specialized [TreeView](<tree-view.md>) for hierarchical path navigation and directory listing. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
TreeList treeList = new TreeList();
treeList.addPath("src", "main", "java");
treeList.addPath("src", "test", "java");
```

## Related links

- [TreeView](<tree-view.md>): Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeList::</span>`addPath(String[])` {: #addpathstring data-toc-label="addPath(String[])" }

Adds a path of node labels to the tree hierarchy, creating intermediate folders if necessary.

| Parameter | Type | Description |
| --- | --- | --- |
| `labels` | `String[]` | The hierarchical path labels. |

Returns `TreeList`: This instance for chaining..

</div>

