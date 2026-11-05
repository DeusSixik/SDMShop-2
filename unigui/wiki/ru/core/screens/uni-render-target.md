---
title: "UniRenderTarget"
slug: "uni-render-target"
category: "Core/Screens"
locale: "ru"
order: 50
---

# UniRenderTarget

Целевой хост-контекст отрисовки для слоя UniGUI.

## Обзор

`UniRenderTarget` определяет фазу отрисовки в графическом конвейере игры, к которой подключается слой:

- `HUD`: Отрисовывается во время рендеринга игрового HUD (прицел, хотбар, статус-бары).
- `SCREEN_OVERLAY`: Отрисовывается поверх открытых экранов и окон меню.
- `LOADING_SCREEN`: Отрисовывается во время экранов загрузки мира или смены измерений.

## Проверка целевого таргета

```java
UniRenderTarget target = UniRenderTarget.HUD;
System.out.println("Целевой таргет рендеринга: " + target);
```

