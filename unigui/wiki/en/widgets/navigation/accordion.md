---
title: "Accordion"
slug: "accordion"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# Accordion

Vertical accordion container managing a stack of collapsible [ExpandablePanel](<expandable-panel.md>) sections with optional single-panel open constraint.

## Overview

Vertical accordion container managing a stack of collapsible [ExpandablePanel](<expandable-panel.md>) sections with optional single-panel open constraint. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
Accordion accordion = new Accordion();
accordion.singleOpen(true);
accordion.addPanel(new ExpandablePanel("General"));
```

## Related links

- [ExpandablePanel](<expandable-panel.md>): Collapsible panel container with header bar, toggle chevron, and smooth expansion/collapse height animation.

## API

<div class="api-member-card" markdown="1">

### `Accordion()` {: #accordion data-toc-label="Accordion()" }

Constructs an empty Accordion container.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`addPanel(ExpandablePanel)` {: #addpanelexpandablepanel data-toc-label="addPanel(ExpandablePanel)" }

Appends an [ExpandablePanel](<expandable-panel.md>) to this accordion.

| Parameter | Type | Description |
| --- | --- | --- |
| `panel` | `ExpandablePanel` | The expandable panel. |

Returns `Accordion`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`clearPanels()` {: #clearpanels data-toc-label="clearPanels()" }

Removes all panels from this accordion.

Returns `Accordion`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`panels()` {: #panels data-toc-label="panels()" }

Returns an unmodifiable list of expandable panels hosted in this accordion.

Returns `List`: An unmodifiable list of expandable panels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`removePanel(ExpandablePanel)` {: #removepanelexpandablepanel data-toc-label="removePanel(ExpandablePanel)" }

Removes an [ExpandablePanel](<expandable-panel.md>) from this accordion.

| Parameter | Type | Description |
| --- | --- | --- |
| `panel` | `ExpandablePanel` | The expandable panel. |

Returns `Accordion`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`singleOpen()` {: #singleopen data-toc-label="singleOpen()" }

Returns true if opening one panel automatically closes all other panels.

Returns `boolean`: True if single-open mode is active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`singleOpen(boolean)` {: #singleopenboolean data-toc-label="singleOpen(boolean)" }

Sets whether only a single panel may be expanded simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `singleOpen` | `boolean` | Whether only one panel may remain open at a time. |

Returns `Accordion`: This instance for chaining..

</div>

