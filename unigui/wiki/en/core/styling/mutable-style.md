---
title: "MutableStyle"
slug: "mutable-style"
category: "Core/Styling"
locale: "en"
order: 20
---

# MutableStyle

Fluent mutable style builder with typed properties and state-specific overrides.

## Overview

`MutableStyle` validates every non-null value against its `StyleKey` type and tracks a version for cache invalidation. Values written without a state belong to `NORMAL`; the state overload creates hover, pressed, selected, disabled, and other overrides. Maps exposed by `values()` are read-only snapshots.

## Build state-aware colors

```java
Style buttonStyle = new MutableStyle()
        .put(StyleKeys.BACKGROUND_COLOR, normalColor)
        .put(StyleKeys.BACKGROUND_COLOR, WidgetState.HOVERED, hoverColor)
        .put(StyleKeys.TEXT_COLOR, textColor);
```

## API

### Properties

| Method | Description |
| --- | --- |
| [`put(StyleKey, Object)`](#putstylekey-object) | Stores a typed property in the NORMAL state. |
| [`put(StyleKey, WidgetState, Object)`](#putstylekey-widgetstate-object) | Stores a typed property override for one widget state. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`put(StyleKey, Object)` {: #putstylekey-object data-toc-label="put(StyleKey, Object)" }

**put(StyleKey, T)**

Stores a typed property in the NORMAL state.

| Parameter | Type | Description |
| --- | --- | --- |
| `key` | `StyleKey` | Typed style key. |
| `value` | `Object` | Property value or null; a non-null value must match the key type. |

Returns `MutableStyle`: The same mutable style for fluent configuration.

Throws:

- `IllegalArgumentException`: The value does not match the Java type declared by the key.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`put(StyleKey, WidgetState, Object)` {: #putstylekey-widgetstate-object data-toc-label="put(StyleKey, WidgetState, Object)" }

**put(StyleKey, WidgetState, T)**

Stores a typed property override for one widget state.

| Parameter | Type | Description |
| --- | --- | --- |
| `key` | `StyleKey` | Typed style key. |
| `state` | `WidgetState` | State that owns the override; must not be null. |
| `value` | `Object` | Property value or null; a non-null value must match the key type. |

Returns `MutableStyle`: The same mutable style for fluent configuration.

Throws:

- `IllegalArgumentException`: The value does not match the Java type declared by the key.

</div>

### Composition

| Method | Description |
| --- | --- |
| [`putAll(Style)`](#putallstyle) | Copies NORMAL and state-specific values from another style. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`putAll(Style)` {: #putallstyle data-toc-label="putAll(Style)" }

**putAll([Style](<style.md>))**

Copies NORMAL and state-specific values from another style.

| Parameter | Type | Description |
| --- | --- | --- |
| `source` | `Style` | Source style; null and [Style](<style.md>).EMPTY are ignored. |

Returns `MutableStyle`: The same mutable style for fluent configuration.

</div>

### Renderer Override

| Method | Description |
| --- | --- |
| [`rendererId(String)`](#rendereridstring) | Selects a custom widget renderer by its registry id. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">MutableStyle::</span>`rendererId(String)` {: #rendereridstring data-toc-label="rendererId(String)" }

**rendererId(String)**

Selects a custom widget renderer by its registry id.

| Parameter | Type | Description |
| --- | --- | --- |
| `rendererId` | `String` | Stable id registered in WidgetRenderRegistry; null clears it. |

Returns `MutableStyle`: The same mutable style for fluent configuration.

</div>

