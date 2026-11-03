---
title: "PostProcessingLayer"
slug: "post-processing-layer"
category: "Виджеты/Эффекты"
locale: "ru"
order: 0
---

# PostProcessingLayer

Обертка виджета, применяющая шейдерные эффекты постобработки и масштаб рендеринга к дочернему контенту.

## Обзор

`PostProcessingLayer` — визуальная обертка, перехватывающая отрисовку своего дочернего контента и применяющая шейдерные эффекты постобработки и масштабирование.

Ключевые возможности:
- Цепочки шейдеров и эффектов постобработки (`postEffect`, `effect`).
- Настройка масштаба рендеринга эффекта (`effectScale`).
- Неинвазивное оборачивание сложных деревьев виджетов с сохранением событий пользовательского ввода.

## Применение эффекта постобработки к дочернему виджету

```java
PostProcessingLayer layer = new PostProcessingLayer();
layer.effect("unigui:bloom");
layer.addChild(contentWidget);
```

## Связанные ссылки

- [PanelWidget](<../containers/panel-widget.md>): Базовый контейнер для виджетов с несколькими детьми: управление иерархией, отложенные мутации и порядок отрисовки.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effect()` {: #effect data-toc-label="effect()" }

Возвращает идентификатор текущего эффекта постобработки или пустую строку, если эффект не задан.

Возвращает `String`: Идентификатор активного эффекта постобработки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effect(String)` {: #effectstring data-toc-label="effect(String)" }

Настраивает зарегистрированный эффект постобработки по его строковому идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `effectId` | `String` | Идентификатор шейдерного эффекта постобработки. |

Возвращает `PostProcessingLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effectScale()` {: #effectscale data-toc-label="effectScale()" }

Возвращает масштабный коэффициент рендеринга закадровой цели.

Возвращает `float`: Масштабный коэффициент рендеринга эффекта постобработки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effectScale(float)` {: #effectscalefloat data-toc-label="effectScale(float)" }

Устанавливает масштабный коэффициент рендеринга закадровой цели относительно границ слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `effectScale` | `float` | Масштабный коэффициент рендеринга эффекта постобработки. |

Возвращает `PostProcessingLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`postEffect()` {: #posteffect data-toc-label="postEffect()" }

Возвращает копию активной цепочки эффектов постобработки.

Возвращает `UiPostEffectChain`: Текущая цепочка шейдерных эффектов постобработки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`postEffect(UiPostEffectChain)` {: #posteffectuiposteffectchain data-toc-label="postEffect(UiPostEffectChain)" }

Устанавливает цепочку эффектов постобработки для поддерева этого слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `postEffect` | `UiPostEffectChain` | Цепочка шейдерных эффектов постобработки. |

Возвращает `PostProcessingLayer`: Текущий экземпляр виджета для цепочки вызовов..

</div>

