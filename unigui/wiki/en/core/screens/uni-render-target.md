---
title: "UniRenderTarget"
slug: "uni-render-target"
category: "Core/Screens"
locale: "en"
order: 50
---

# UniRenderTarget

Host rendering hook target for a neutral UniGUI render layer.

## Overview

`UniRenderTarget` defines the target rendering stage where a `UniRenderLayer` is hooked by the underlying platform runtime:

- `HUD`: Rendered during the in-game HUD rendering phase (crosshair, status bars, hotbar).
- `SCREEN_OVERLAY`: Rendered over open GUI screens and menus.
- `LOADING_SCREEN`: Rendered during game loading, world join, or dimension change phases.

## Target Inspection

```java
UniRenderTarget target = UniRenderTarget.HUD;
System.out.println("Rendering target: " + target);
```

