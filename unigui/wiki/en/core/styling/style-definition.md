---
title: "StyleDefinition"
slug: "style-definition"
category: "Core/Styling"
locale: "en"
order: 30
---

# StyleDefinition

Immutable named style rule with selectors, renderer configuration, properties, and event animations.

## Overview

A `StyleDefinition` is one reusable rule inside a `StylePack`. It has a stable id, a selector, declarative properties or a registered custom renderer, and optional event-to-animation bindings. Modifier methods return new definitions; store the returned value or put it back into the pack.

## Selectors

`target` restricts a rule to a widget style type, `styleClass` matches classes added with `addStyleClass`, and `widgetId` matches the widget's explicit `styleId`. These conditions can be combined. The `widgetId` selector does not match the general-purpose widget `id`.

## Create a primary button rule

```java
StyleDefinition primary = StyleDefinition.of(
        "button.primary",
        new MutableStyle().put(StyleKeys.BACKGROUND_COLOR, accent))
        .target(Button.STYLE_TYPE)
        .styleClass("primary");
```

## API

### Creation

| Method | Description |
| --- | --- |
| [`of(String, Style)`](#ofstring-style) | Creates a declarative style rule. |
| [`custom(String, String, Style)`](#customstring-string-style) | Creates a rule backed by a registered custom renderer. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`of(String, Style)` {: #ofstring-style data-toc-label="of(String, Style)" }

**of(String, [Style](<style.md>))**

Creates a declarative style rule.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Stable non-empty id within the style pack. |
| `style` | `Style` | Typed style properties; null becomes [Style](<style.md>).EMPTY. |

Returns `StyleDefinition`: New immutable style definition.

Throws:

- `IllegalArgumentException`: The definition id is null or blank.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`custom(String, String, Style)` {: #customstring-string-style data-toc-label="custom(String, String, Style)" }

**custom(String, String, [Style](<style.md>))**

Creates a rule backed by a registered custom renderer.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Stable non-empty id within the style pack. |
| `rendererId` | `String` | Stable renderer id registered in WidgetRenderRegistry. |
| `style` | `Style` | Typed style properties; null becomes [Style](<style.md>).EMPTY. |

Returns `StyleDefinition`: New immutable style definition.

Throws:

- `IllegalArgumentException`: The definition id is null or blank.

</div>

### Selectors

| Method | Description |
| --- | --- |
| [`target(String)`](#targetstring) | Restricts the rule to a widget style type. |
| [`styleClass(String)`](#styleclassstring) | Restricts the rule to a widget style class. |
| [`widgetId(String)`](#widgetidstring) | Restricts the rule to a widget's explicit style id. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`target(String)` {: #targetstring data-toc-label="target(String)" }

**target(String)**

Restricts the rule to a widget style type.

| Parameter | Type | Description |
| --- | --- | --- |
| `target` | `String` | [Widget](<../../widgets/widget.md>) style type, usually a STYLE_TYPE constant. |

Returns `StyleDefinition`: New immutable definition with the requested change.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`styleClass(String)` {: #styleclassstring data-toc-label="styleClass(String)" }

**styleClass(String)**

Restricts the rule to a widget style class.

| Parameter | Type | Description |
| --- | --- | --- |
| `styleClass` | `String` | Class added through addStyleClass or XML class. |

Returns `StyleDefinition`: New immutable definition with the requested change.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`widgetId(String)` {: #widgetidstring data-toc-label="widgetId(String)" }

**widgetId(String)**

Restricts the rule to a widget's explicit style id.

| Parameter | Type | Description |
| --- | --- | --- |
| `widgetId` | `String` | Value assigned through styleId or the XML style attribute. |

Returns `StyleDefinition`: New immutable definition with the requested change.

</div>

### Event Animations

| Method | Description |
| --- | --- |
| [`eventAnimation(String, String)`](#eventanimationstring-string) | Adds, replaces, or removes an event-to-animation binding. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StyleDefinition::</span>`eventAnimation(String, String)` {: #eventanimationstring-string data-toc-label="eventAnimation(String, String)" }

**eventAnimation(String, String)**

Adds, replaces, or removes an event-to-animation binding.

| Parameter | Type | Description |
| --- | --- | --- |
| `eventName` | `String` | Stable event id, preferably from StyleAnimationIds.Event. |
| `animationId` | `String` | Animation preset id; blank removes the binding. |

Returns `StyleDefinition`: New immutable definition with the requested change.

</div>

