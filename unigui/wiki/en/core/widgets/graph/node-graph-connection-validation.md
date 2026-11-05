---
title: "NodeGraphConnectionValidation"
slug: "node-graph-connection-validation"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphConnectionValidation

Validation result for proposed connections between node ports.

## Overview

Validation result for proposed connections between node ports. Provides extensive layout configuration and interaction capabilities.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnectionValidation::</span>`accepted()` {: #accepted data-toc-label="accepted()" }

Creates an accepted connection validation result.

Returns `NodeGraphConnectionValidation`: An accepted connection validation result..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnectionValidation::</span>`invalid(String)` {: #invalidstring data-toc-label="invalid(String)" }

Creates a rejected connection validation result with failure explanation.

| Parameter | Type | Description |
| --- | --- | --- |
| `reason` | `String` | The rejection explanation message. |

Returns `NodeGraphConnectionValidation`: A rejected connection validation result..

</div>

