---
title: "Style"
slug: "style"
category: "Core/Styling"
locale: "en"
order: 10
---

# Style

Read-only typed style values with widget-state overrides and explicit fallback behavior.

## Overview

A `Style` stores values by typed `StyleKey` and `WidgetState`. Querying a state may fall back to `NORMAL`, then to the fallback supplied by the caller. Use `MutableStyle` to build values in Java, `Style#fromText` for inline declarations, or `Style#from` for one XML `<Style>` element.

## Style layers

`Style` is one layer, not the whole theme cascade. `StylePack` combines theme tokens, fallback values, widget bindings, and matching definitions. A local widget style is applied after theme layers. Use `StylePack#from` when XML contains selectors, named definitions, event animations, or renderer bindings.

## Parse and read an inline style

```java
Style style = Style.fromText("background: #20242a; radius: 6;");
ColorView background = style.get(
        StyleKeys.BACKGROUND_COLOR,
        WidgetState.NORMAL,
        fallbackColor);
```

## API

### Creation and Parsing

| Method | Description |
| --- | --- |
| [`from(String)`](#fromstring) | Parses one XML `<Style>` element with built-in properties. |
| [`from(String, StyleKeyRegistry)`](#fromstring-stylekeyregistry) | Parses one XML `<Style>` element with a custom property registry. |
| [`fromText(String)`](#fromtextstring) | Parses semicolon-separated inline declarations and skips invalid entries. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`from(String)` {: #fromstring data-toc-label="from(String)" }

**from(String)**

Parses one XML `<Style>` element with built-in properties.

| Parameter | Type | Description |
| --- | --- | --- |
| `xml` | `String` | XML whose root element is `<Style>`. |

Returns `Style`: Parsed read-only style.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`from(String, StyleKeyRegistry)` {: #fromstring-stylekeyregistry data-toc-label="from(String, StyleKeyRegistry)" }

**from(String, StyleKeyRegistry)**

Parses one XML `<Style>` element with a custom property registry.

| Parameter | Type | Description |
| --- | --- | --- |
| `xml` | `String` | XML whose root element is `<Style>`. |
| `registry` | `StyleKeyRegistry` | Registry containing built-in and application-specific style properties. |

Returns `Style`: Parsed read-only style.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`fromText(String)` {: #fromtextstring data-toc-label="fromText(String)" }

**fromText(String)**

Parses semicolon-separated inline declarations and skips invalid entries.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Inline declarations such as background: #20242a; radius: 6;. |

Returns `Style`: Parsed mutable style; use StyleTextParser.parseEditor when diagnostics are required.

</div>

### Reading Values

| Method | Description |
| --- | --- |
| [`get(StyleKey, WidgetState, Object)`](#getstylekey-widgetstate-object) | Reads a typed value for a widget state with an explicit fallback. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Style::</span>`get(StyleKey, WidgetState, Object)` {: #getstylekey-widgetstate-object data-toc-label="get(StyleKey, WidgetState, Object)" }

**get(StyleKey, WidgetState, T)**

Reads a typed value for a widget state with an explicit fallback.

| Parameter | Type | Description |
| --- | --- | --- |
| `key` | `StyleKey` | Typed property key that defines the expected Java type. |
| `state` | `WidgetState` | [Widget](<../../widgets/widget.md>) state; null is treated as NORMAL. |
| `fallback` | `Object` | Value returned when the property is absent. |

Returns `Object`: State value, NORMAL fallback, or the supplied fallback.

</div>

