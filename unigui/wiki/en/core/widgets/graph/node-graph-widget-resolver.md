---
title: "NodeGraphWidgetResolver"
slug: "node-graph-widget-resolver"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphWidgetResolver

Factory callback to restore item content widgets when deserializing a node graph snapshot.

## Overview

Factory callback to restore item content widgets when deserializing a node graph snapshot. Provides extensive layout configuration and interaction capabilities.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphWidgetResolver::</span>`resolve(String, String)` {: #resolvestring-string data-toc-label="resolve(String, String)" }

Resolves or instantiates the content widget for a deserialized node item.

| Parameter | Type | Description |
| --- | --- | --- |
| `itemId` | `String` | The item identifier. |
| `contentType` | `String` | The contentType parameter. |

Returns `Widget`: The resolved content widget..

</div>

