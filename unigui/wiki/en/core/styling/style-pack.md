---
title: "StylePack"
slug: "style-pack"
category: "Core/Styling"
locale: "en"
order: 40
---

# StylePack

Editable theme containing reusable style rules, widget bindings, tokens, and animation presets.

## Overview

`StylePack` is both a `Theme` and an editable style-sheet model. Build it in Java or parse a complete XML `<StylePack>`, then install it on a UI context. Named definitions can be selected by widget type, style class, explicit style id, or a direct widget-type binding.

## Resolution order

Resolution starts with theme tokens, then the pack fallback, a bound base definition, and matching selector or explicit-style rules. Matching definitions are applied in insertion order; a later rule overrides an earlier one regardless of selector specificity. Returned maps and lists are snapshots.

## Build a style pack in Java

```java
StylePack pack = StylePack.create("app")
        .tokens(ThemeTokens.defaults())
        .put(StyleDefinition.of("button.primary", primaryStyle)
                .target(Button.STYLE_TYPE)
                .styleClass("primary"));
context.theme(pack);
```

## Load a style pack from XML

```xml
<StylePack id="app">
  <Style id="button.primary" target="button" class="primary">
    <Setter property="background" value="#365df5"/>
    <State name="hovered">
      <Setter property="background" value="#4b70ff"/>
    </State>
  </Style>
</StylePack>
```

## API

### Creation and Parsing

| Method | Description |
| --- | --- |
| [`create(String)`](#createstring) | Creates an empty style pack with a normalized id. |
| [`from(String)`](#fromstring) | Parses a complete XML `<StylePack>` with built-in properties. |
| [`copy()`](#copy) | Creates an independent editable copy, preserving all style rules and state overrides. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`create(String)` {: #createstring data-toc-label="create(String)" }

**create(String)**

Creates an empty style pack with a normalized id.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Pack id; null or blank becomes style-pack. |

Returns `StylePack`: New mutable style pack.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`from(String)` {: #fromstring data-toc-label="from(String)" }

**from(String)**

Parses a complete XML `<StylePack>` with built-in properties.

| Parameter | Type | Description |
| --- | --- | --- |
| `xml` | `String` | Complete XML StylePack document. |

Returns `StylePack`: Parsed mutable style pack.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`copy()` {: #copy data-toc-label="copy()" }

**Copy style pack**

Creates an independent editable copy, preserving all style rules and state overrides.

Returns `StylePack`: An independent mutable copy of this style pack.

</div>

### [Style](<style.md>) Rules

| Method | Description |
| --- | --- |
| [`put(StyleDefinition)`](#putstyledefinition) | Adds or replaces one named style definition. |
| [`bind(String, String)`](#bindstring-string) | Binds a widget style type to a base definition id. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`put(StyleDefinition)` {: #putstyledefinition data-toc-label="put(StyleDefinition)" }

**put([StyleDefinition](<style-definition.md>))**

Adds or replaces one named style definition.

| Parameter | Type | Description |
| --- | --- | --- |
| `definition` | `StyleDefinition` | Immutable definition to store by id. |

Returns `StylePack`: The same style pack for fluent configuration.

Throws:

- `NullPointerException`: The definition is null.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`bind(String, String)` {: #bindstring-string data-toc-label="bind(String, String)" }

**bind(String, String)**

Binds a widget style type to a base definition id.

| Parameter | Type | Description |
| --- | --- | --- |
| `widgetType` | `String` | [Widget](<../../widgets/widget.md>) style type; the wildcard updates the fallback style id. |
| `styleId` | `String` | Definition id; blank removes the binding. |

Returns `StylePack`: The same style pack for fluent configuration.

</div>

### Animation Presets

| Method | Description |
| --- | --- |
| [`putAnimation(StyleAnimationDefinition)`](#putanimationstyleanimationdefinition) | Adds or replaces one named animation preset. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`putAnimation(StyleAnimationDefinition)` {: #putanimationstyleanimationdefinition data-toc-label="putAnimation(StyleAnimationDefinition)" }

**putAnimation(StyleAnimationDefinition)**

Adds or replaces one named animation preset.

| Parameter | Type | Description |
| --- | --- | --- |
| `animation` | `StyleAnimationDefinition` | Animation definition to store by id. |

Returns `StylePack`: The same style pack for fluent configuration.

Throws:

- `NullPointerException`: The animation definition is null.

</div>

### Resolution

| Method | Description |
| --- | --- |
| [`resolveStyleFor(String, String, Collection)`](#resolvestyleforstring-string-collection) | Resolves all active style layers for one widget. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">StylePack::</span>`resolveStyleFor(String, String, Collection)` {: #resolvestyleforstring-string-collection data-toc-label="resolveStyleFor(String, String, Collection)" }

**resolveStyleFor(String, String, Collection)**

Resolves all active style layers for one widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `widgetType` | `String` | [Widget](<../../widgets/widget.md>) style type. |
| `widgetStyleId` | `String` | Explicit style id assigned to the widget, or blank. |
| `styleClasses` | `Collection` | [Widget](<../../widgets/widget.md>) style classes; null is treated as empty. |

Returns `Style`: Read-only resolved style ordered from lowest to highest priority.

</div>

