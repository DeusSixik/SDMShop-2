---
title: "UniRenderLayerRegistration"
slug: "uni-render-layer-registration"
category: "Core/Screens"
locale: "en"
order: 60
---

# UniRenderLayerRegistration

Backend-neutral handle for a registered render layer.

## Overview

`UniRenderLayerRegistration` encapsulates a registered layer in the active backend. It allows dynamically enabling or disabling the layer, changing its z-priority, updating its visibility predicate, and cleanly unregistering it via `close()`.

## Lifecycle Management

Since `UniRenderLayerRegistration` implements `AutoCloseable`, it can be managed with try-with-resources or closed when a feature is toggled off, instantly unhooking both the backend registration and the layer itself.

## Toggle and Unregister Layer

```java
UniRenderLayerRegistration reg = UniHudRender.register(myWidget);
// Temporarily disable
reg.enabled(false);
// Later, permanently remove
reg.close();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`close()` {: #close data-toc-label="close()" }

**close()**

Unregisters and closes this layer from the backend.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`closed()` {: #closed data-toc-label="closed()" }

**closed()**

Returns true if this registration has been closed.

Returns `boolean`: True if closed..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

**enabled()**

Returns true if this layer is currently enabled for rendering.

Returns `boolean`: True if enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

**enabled(boolean)**

Enables or disables rendering for this layer without unregistering.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | True to enable, false to disable. |

Returns `UniRenderLayerRegistration`: This registration handle for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`layer()` {: #layer data-toc-label="layer()" }

**layer()**

Returns the underlying [UniRenderLayer](<uni-render-layer.md>) instance.

Returns `UniRenderLayer`: Associated [UniRenderLayer](<uni-render-layer.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`priority()` {: #priority data-toc-label="priority()" }

**priority()**

Returns the rendering order priority.

Returns `int`: Current priority integer..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`priority(int)` {: #priorityint data-toc-label="priority(int)" }

**priority(int)**

Updates the rendering order priority.

| Parameter | Type | Description |
| --- | --- | --- |
| `priority` | `int` | New priority integer. |

Returns `UniRenderLayerRegistration`: This registration handle for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`visibleWhen(Predicate)` {: #visiblewhenpredicate data-toc-label="visibleWhen(Predicate)" }

**visibleWhen(Predicate<Object>)**

Updates the dynamic visibility predicate.

| Parameter | Type | Description |
| --- | --- | --- |
| `visibility` | `Predicate` | Predicate testing context object. |

Returns `UniRenderLayerRegistration`: This registration handle for chaining..

</div>

