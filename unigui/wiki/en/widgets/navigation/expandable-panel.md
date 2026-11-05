---
title: "ExpandablePanel"
slug: "expandable-panel"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# ExpandablePanel

Collapsible panel container with header bar, toggle chevron, and smooth expansion/collapse height animation.

## Overview

Collapsible panel container with header bar, toggle chevron, and smooth expansion/collapse height animation. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
ExpandablePanel panel = new ExpandablePanel("Advanced Settings");
panel.addContent(settingsBox);
```

## Related links

- [Accordion](<accordion.md>): Vertical accordion container managing a stack of collapsible ExpandablePanel sections with optional single-panel open constraint.

## API

<div class="api-member-card" markdown="1">

### `ExpandablePanel()` {: #expandablepanel data-toc-label="ExpandablePanel()" }

Constructs an empty ExpandablePanel.

</div>

<div class="api-member-card" markdown="1">

### `ExpandablePanel(RichText)` {: #expandablepanelrichtext data-toc-label="ExpandablePanel(RichText)" }

Constructs an ExpandablePanel with a [RichText](<../../core/text/rich-text.md>) formatted title.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `RichText` | The title text. |

</div>

<div class="api-member-card" markdown="1">

### `ExpandablePanel(String)` {: #expandablepanelstring data-toc-label="ExpandablePanel(String)" }

Constructs an ExpandablePanel with a plain text title.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | The title text. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`addContent(Widget)` {: #addcontentwidget data-toc-label="addContent(Widget)" }

Adds a content widget into the expandable body.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | The child node instance. |

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`clearContent()` {: #clearcontent data-toc-label="clearContent()" }

Clears all content widgets from the expandable body.

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`contentHost()` {: #contenthost data-toc-label="contentHost()" }

Returns the vertical container hosting expandable content widgets.

Returns `VBox`: The container hosting content views..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`expanded()` {: #expanded data-toc-label="expanded()" }

Returns true if the panel content is currently expanded.

Returns `boolean`: True if expanded; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`expanded(boolean)` {: #expandedboolean data-toc-label="expanded(boolean)" }

Sets the expanded state and dispatches an expansion event.

| Parameter | Type | Description |
| --- | --- | --- |
| `expanded` | `boolean` | Whether the node or panel is expanded. |

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`headerButton()` {: #headerbutton data-toc-label="headerButton()" }

Returns the header [ToggleButton](<../interaction/toggle-button.md>) controlling panel expansion.

Returns `ToggleButton`: The header toggle button..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`onExpandedChanged(EventListener)` {: #onexpandedchangedeventlistener data-toc-label="onExpandedChanged(EventListener)" }

Registers a listener invoked when the panel expands or collapses.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`removeContent(Widget)` {: #removecontentwidget data-toc-label="removeContent(Widget)" }

Removes a content widget from the expandable body.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | The child node instance. |

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`richTitle()` {: #richtitle data-toc-label="richTitle()" }

Returns the [RichText](<../../core/text/rich-text.md>) formatted title.

Returns `RichText`: The rich text title..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`richTitle(RichText)` {: #richtitlerichtext data-toc-label="richTitle(RichText)" }

Sets the [RichText](<../../core/text/rich-text.md>) formatted title.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `RichText` | The title text. |

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`silentExpanded(boolean)` {: #silentexpandedboolean data-toc-label="silentExpanded(boolean)" }

Sets the expanded state without triggering event callbacks.

| Parameter | Type | Description |
| --- | --- | --- |
| `expanded` | `boolean` | Whether the node or panel is expanded. |

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`title()` {: #title data-toc-label="title()" }

Returns the plain text title string.

Returns `String`: The plain text title..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

Sets the plain text title.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | The title text. |

Returns `ExpandablePanel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ExpandablePanel::</span>`toggleExpanded()` {: #toggleexpanded data-toc-label="toggleExpanded()" }

Toggles between expanded and collapsed states.

Returns `ExpandablePanel`: This instance for chaining..

</div>

