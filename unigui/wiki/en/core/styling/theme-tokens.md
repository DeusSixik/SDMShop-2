---
title: "ThemeTokens"
slug: "theme-tokens"
category: "Core/Styling"
locale: "en"
order: 50
---

# ThemeTokens

Immutable design-token snapshot for palette, shapes, typography, and spacing.

## Overview

`ThemeTokens` groups reusable colors, shapes, typography, and spacing into one immutable snapshot. Use the nested builder to customize one or more groups, then attach the snapshot to `MutableTheme` or `StylePack`. A built snapshot can be shared safely across UI trees.

## Cascade priority

Tokens are the lowest-priority active style layer. Pack fallback, matching definitions, and local widget styles override them. `ThemeTokens.EMPTY` is inactive and contributes nothing, while `defaults()` creates an active neutral dark set. `asStyle()` exposes only the keys mapped by the token system.

## Create custom tokens

```java
ThemeTokens tokens = ThemeTokens.builder()
        .colors(colors -> colors.accent(accentColor))
        .shapes(shapes -> shapes.radiusMd(6.0f))
        .spacing(spacing -> spacing.controlGap(6.0f))
        .build();
StylePack pack = StylePack.create("app").tokens(tokens);
```

## API

### Creation

| Method | Description |
| --- | --- |
| [`builder()`](#builder) | Starts a builder for an active immutable token snapshot. |
| [`defaults()`](#defaults) | Creates the active neutral dark default token set. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ThemeTokens::</span>`builder()` {: #builder data-toc-label="builder()" }

**builder()**

Starts a builder for an active immutable token snapshot.

Returns `Builder`: Token builder with default color, shape, typography, and spacing groups.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ThemeTokens::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**defaults()**

Creates the active neutral dark default token set.

Returns `ThemeTokens`: New active default token snapshot.

</div>

### [Style](<style.md>) Integration

| Method | Description |
| --- | --- |
| [`asStyle()`](#asstyle) | Exposes mapped tokens as a read-only style layer. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ThemeTokens::</span>`asStyle()` {: #asstyle data-toc-label="asStyle()" }

**asStyle()**

Exposes mapped tokens as a read-only style layer.

Returns `Style`: Token-backed [Style](<style.md>), or [Style](<style.md>).EMPTY for the inactive EMPTY snapshot.

</div>

