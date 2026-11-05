---
title: "DockPane"
slug: "dock-pane"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockPane

Descriptor representing an individual dockable pane hosting arbitrary widget content, tab title, icon, dirty state, and close guards.

## Overview

`DockPane` represents a dockable panel hosting arbitrary widget content.

### Key Capabilities

- **Header Customization**: Title, rich formatted text, and icons (`icon()`).
- **Dirty State**: `dirty(true)` renders an unsaved indicator (e.g. bullet point or accent).
- **Close Protection**: `onCloseRequested(guard)` prevents pane closing until confirmed or saved.
- **Classification**: Configurable as `DOCUMENT` or `TOOL`.

## Creating Document and Tool Panes

```java
DockPane doc = DockPane.document("main-editor", "Main.java", editorWidget)
        .dirty(true)
        .closable(true);
```

## Related links

- [DockPaneKind](<dock-pane-kind.md>): Classification of a docking pane: DOCUMENT (primary central workspace content) or TOOL (dockable side utility tool window).
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### `DockPane(String, RichText, Widget)` {: #dockpanestring-richtext-widget data-toc-label="DockPane(String, RichText, Widget)" }

Creates a dock pane with explicit identifier, [RichText](<../../text/rich-text.md>) title, and content.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `RichText` | Rich text title formatting. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

</div>

<div class="api-member-card" markdown="1">

### `DockPane(String, String, Widget)` {: #dockpanestring-string-widget data-toc-label="DockPane(String, String, Widget)" }

Creates a dock pane with explicit identifier, string title, and widget content.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `String` | Pane title text. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

</div>

<div class="api-member-card" markdown="1">

### `DockPane(String, Widget)` {: #dockpanestring-widget data-toc-label="DockPane(String, Widget)" }

Creates a dock pane with generated unique identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Pane title text. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`autoHide()` {: #autohide data-toc-label="autoHide()" }

Returns true if this pane automatically hides when focus is lost.

Returns `boolean`: True if auto-hide is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`autoHide(boolean)` {: #autohideboolean data-toc-label="autoHide(boolean)" }

Enables or disables auto-hide for unpinned panes.

| Parameter | Type | Description |
| --- | --- | --- |
| `autoHide` | `boolean` | Whether the pane auto-hides when unfocused. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`canClose()` {: #canclose data-toc-label="canClose()" }

Returns true if closable and closeGuard permits closing.

Returns `boolean`: True if closing was approved by the guard; false if cancelled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`closable()` {: #closable data-toc-label="closable()" }

Returns true if this pane can be closed by the user.

Returns `boolean`: True if the pane has a close button and can be closed..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`closable(boolean)` {: #closableboolean data-toc-label="closable(boolean)" }

Sets whether the pane can be closed.

| Parameter | Type | Description |
| --- | --- | --- |
| `closable` | `boolean` | Whether the pane can be closed. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`content()` {: #content data-toc-label="content()" }

Returns the hosted widget content.

Returns `Widget`: The widget hosted inside this pane..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`dirty()` {: #dirty data-toc-label="dirty()" }

Returns true if this pane has unsaved changes (shows dot indicator).

Returns `boolean`: True if the pane contains unsaved modifications..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`dirty(boolean)` {: #dirtyboolean data-toc-label="dirty(boolean)" }

Marks the pane as having unsaved modifications.

| Parameter | Type | Description |
| --- | --- | --- |
| `dirty` | `boolean` | Whether the pane has unsaved changes. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`document()` {: #document data-toc-label="document()" }

Returns true if this pane is classified as a document.

Returns `boolean`: True if kind is DOCUMENT..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`document(String, RichText, Widget)` {: #documentstring-richtext-widget data-toc-label="document(String, RichText, Widget)" }

Creates a document-style dock pane with [RichText](<../../text/rich-text.md>) title.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `RichText` | Rich text title formatting. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

Returns `DockPane`: The constructed DockPane instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`document(String, String, Widget)` {: #documentstring-string-widget data-toc-label="document(String, String, Widget)" }

Creates a document-style dock pane with string title.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `String` | Pane title text. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

Returns `DockPane`: The constructed DockPane instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`icon()` {: #icon data-toc-label="icon()" }

Returns the icon associated with this tab.

Returns `UiIcon`: Icon displayed in the tab header, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`icon(UiIcon)` {: #iconuiicon data-toc-label="icon(UiIcon)" }

Sets the icon displayed in tab strips and floating window headers.

| Parameter | Type | Description |
| --- | --- | --- |
| `icon` | `UiIcon` | Icon displayed in the tab header. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`id()` {: #id data-toc-label="id()" }

Returns the unique pane identifier.

Returns `String`: Unique pane identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`kind()` {: #kind data-toc-label="kind()" }

Returns the pane kind (DOCUMENT or TOOL).

Returns `DockPaneKind`: Pane kind (DOCUMENT or TOOL)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`kind(DockPaneKind)` {: #kinddockpanekind data-toc-label="kind(DockPaneKind)" }

Sets the pane classification (DOCUMENT or TOOL).

| Parameter | Type | Description |
| --- | --- | --- |
| `kind` | `DockPaneKind` | Pane kind classification (DOCUMENT or TOOL). |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`onCloseRequested(Predicate)` {: #oncloserequestedpredicate data-toc-label="onCloseRequested(Predicate)" }

Sets a close request guard predicate that can prompt for saving or prevent closing.

| Parameter | Type | Description |
| --- | --- | --- |
| `guard` | `Predicate` | Predicate guard approving or rejecting pane close. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`pinned()` {: #pinned data-toc-label="pinned()" }

Returns true if this pane is pinned in the dock hierarchy.

Returns `boolean`: True if the pane is pinned in its tab strip..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`pinned(boolean)` {: #pinnedboolean data-toc-label="pinned(boolean)" }

Sets whether the pane is pinned.

| Parameter | Type | Description |
| --- | --- | --- |
| `pinned` | `boolean` | Whether the pane is pinned in the tab strip. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`richTitle()` {: #richtitle data-toc-label="richTitle()" }

Returns the [RichText](<../../text/rich-text.md>) formatted title.

Returns `RichText`: Rich text title formatted component, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`richTitle(RichText)` {: #richtitlerichtext data-toc-label="richTitle(RichText)" }

Sets the [RichText](<../../text/rich-text.md>) title of the pane.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `RichText` | Rich text title formatting. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`title()` {: #title data-toc-label="title()" }

Returns the plain-text title string.

Returns `String`: Plain text title string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

Sets the plain-text title of the pane.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Pane title text. |

Returns `DockPane`: This DockPane for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`tool()` {: #tool data-toc-label="tool()" }

Returns true if this pane is classified as a tool window.

Returns `boolean`: True if kind is TOOL..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`tool(String, RichText, Widget)` {: #toolstring-richtext-widget data-toc-label="tool(String, RichText, Widget)" }

Creates a tool-style dock pane with [RichText](<../../text/rich-text.md>) title.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `RichText` | Rich text title formatting. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

Returns `DockPane`: The constructed DockPane instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPane::</span>`tool(String, String, Widget)` {: #toolstring-string-widget data-toc-label="tool(String, String, Widget)" }

Creates a tool-style dock pane with string title.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `String` | Pane title text. |
| `content` | `Widget` | [Widget](<../../../widgets/widget.md>) content displayed inside the pane. |

Returns `DockPane`: The constructed DockPane instance..

</div>

