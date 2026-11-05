---
title: "UiPostEffectRenderer"
slug: "ui-post-effect-renderer"
category: "Core/PostEffects"
locale: "ru"
order: 90
---

# UiPostEffectRenderer

Диспетчер отрисовки с автоматическим откатом для слоев с пост-эффектами.

## Обзор

`UiPostEffectRenderer` направляет отрисовку слоя в активный бэкенд. Если цепочка пуста или бэкенд не реализует интерфейс `UiPostEffectBackend`, метод автоматически выполняет прямую отрисовку через `RenderBackend#render` без ошибок.

## Выполнение отрисовки с пост-эффектом

```java
UiPostEffectRenderer.render(
    backend,
    drawList,
    UiLayerBounds.viewport(width, height),
    chain
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRenderer::</span>`render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain)` {: #renderrenderbackend-drawlist-uilayerbounds-uiposteffectchain data-toc-label="render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain)" }

**render(RenderBackend, [DrawList](<../render/draw-list.md>), [UiLayerBounds](<ui-layer-bounds.md>), [UiPostEffectChain](<ui-post-effect-chain.md>))**

Рендерит draw list с откатом пост-эффекта в активный экранный target.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `backend` | `RenderBackend` | Активный бэкенд RenderBackend. |
| `drawList` | `DrawList` | Список команд [DrawList](<../render/draw-list.md>). |
| `bounds` | `UiLayerBounds` | Границы слоя [UiLayerBounds](<ui-layer-bounds.md>). |
| `chain` | `UiPostEffectChain` | Цепочка эффектов [UiPostEffectChain](<ui-post-effect-chain.md>). |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRenderer::</span>`render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain, RenderTarget)` {: #renderrenderbackend-drawlist-uilayerbounds-uiposteffectchain-rendertarget data-toc-label="render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain, RenderTarget)" }

**render(RenderBackend, [DrawList](<../render/draw-list.md>), [UiLayerBounds](<ui-layer-bounds.md>), [UiPostEffectChain](<ui-post-effect-chain.md>), RenderTarget)**

Рендерит draw list с откатом пост-эффекта в указанный RenderTarget.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `backend` | `RenderBackend` | Активный бэкенд RenderBackend. |
| `drawList` | `DrawList` | Список команд [DrawList](<../render/draw-list.md>). |
| `bounds` | `UiLayerBounds` | Границы слоя [UiLayerBounds](<ui-layer-bounds.md>). |
| `chain` | `UiPostEffectChain` | Цепочка эффектов [UiPostEffectChain](<ui-post-effect-chain.md>). |
| `target` | `RenderTarget` | Целевой RenderTarget или null. |

</div>

