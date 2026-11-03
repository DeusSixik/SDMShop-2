---
title: "UniRenderLayer"
slug: "uni-render-layer"
category: "Core/Screens"
locale: "ru"
order: 20
---

# UniRenderLayer

Платформо-независимая конфигурация слоя оверлея или HUD.

## Обзор

`UniRenderLayer` представляет изолированный слой интерфейса, отображаемый в игровом HUD или поверх открытых экранов. Содержит корневой `Widget`, контекст `UIContext`, цепочки пост-эффектов (`UiPostEffectChain`), политики рендеринга и управление жизненным циклом.

## Пост-эффекты и шейдеры

Слои поддерживают пост-обработку через `postEffect(...)`, накладывая размытие (blur), виньетки, эффект ЭЛТ-монитора или цветокоррекцию на буфер слоя перед финальным выводом.

## Инвалидация кэша и закрытие

Слой реализует `AutoCloseable` и может быть отключён вызовом `close()`. При использовании кэширования вызов `invalidateRenderCache()` принудительно запрашивает перерисовку у бэкенда.

## Настройка HUD-слоя с эффектом свечения

```java
Widget hudWidget = new CompassWidget();
UniRenderLayer layer = new UniRenderLayer(hudWidget)
    .renderPolicy(UiRenderPolicy.continuous())
    .postEffect("unigui:bloom");
UniHudRender.register(layer);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`attachRuntimeControl(RuntimeLayerControl)` {: #attachruntimecontrolruntimelayercontrol data-toc-label="attachRuntimeControl(RuntimeLayerControl)" }

**attachRuntimeControl(RuntimeLayerControl)**

Внутренний хук для привязки контроллера управления слоем рантайма.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `control` | `RuntimeLayerControl` | Экземпляр RuntimeLayerControl. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`clearPostEffect()` {: #clearposteffect data-toc-label="clearPostEffect()" }

**clearPostEffect()**

Очищает все пост-эффекты на слое.

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`close()` {: #close data-toc-label="close()" }

**close()**

Закрывает слой и отключает его от конвейера рантайма.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`closed()` {: #closed data-toc-label="closed()" }

**closed()**

Возвращает true, если слой отрисовки был закрыт.

Возвращает `boolean`: True, если закрыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`context()` {: #context data-toc-label="context()" }

**context()**

Возвращает контекст интерфейса, привязанный к слою.

Возвращает `UIContext`: UIContext или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`debugHotkeysEnabled()` {: #debughotkeysenabled data-toc-label="debugHotkeysEnabled()" }

**debugHotkeysEnabled()**

Возвращает true, если для слоя включены отладочные горячие клавиши.

Возвращает `boolean`: True, если отладочные клавиши активны..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`debugHotkeysEnabled(boolean)` {: #debughotkeysenabledboolean data-toc-label="debugHotkeysEnabled(boolean)" }

**debugHotkeysEnabled(boolean)**

Включает или выключает отладочные горячие клавиши для слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `boolean` | True для включения отладки. |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`defaultFont()` {: #defaultfont data-toc-label="defaultFont()" }

**defaultFont()**

Возвращает шрифт по умолчанию для слоя или null.

Возвращает `FontFace`: Шрифт FontFace или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`defaultFont(FontFace)` {: #defaultfontfontface data-toc-label="defaultFont(FontFace)" }

**defaultFont(FontFace)**

Устанавливает шрифт по умолчанию для слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `font` | `FontFace` | Шрифт FontFace. |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`hasCustomUiScale()` {: #hascustomuiscale data-toc-label="hasCustomUiScale()" }

**hasCustomUiScale()**

Возвращает true, если задан пользовательский масштаб интерфейса.

Возвращает `boolean`: True, если задан конечный масштаб..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`invalidateRenderCache()` {: #invalidaterendercache data-toc-label="invalidateRenderCache()" }

**invalidateRenderCache()**

Инвалидирует кэш отрисовки прикреплённого слоя в рантайме.

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`postEffect()` {: #posteffect data-toc-label="postEffect()" }

**postEffect()**

Возвращает копию цепочки пост-эффектов слоя.

Возвращает `UiPostEffectChain`: Копия [UiPostEffectChain](<../posteffects/ui-post-effect-chain.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`postEffect(String)` {: #posteffectstring data-toc-label="postEffect(String)" }

**postEffect(String)**

Устанавливает одиночный пост-эффект по идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `effectId` | `String` | Идентификатор зарегистрированного пост-эффекта. |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`postEffect(UiPostEffectChain)` {: #posteffectuiposteffectchain data-toc-label="postEffect(UiPostEffectChain)" }

**postEffect([UiPostEffectChain](<../posteffects/ui-post-effect-chain.md>))**

Устанавливает цепочку пост-эффектов для слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `chain` | `UiPostEffectChain` | Цепочка пост-эффектов. |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`renderPolicy()` {: #renderpolicy data-toc-label="renderPolicy()" }

**renderPolicy()**

Возвращает политику рендеринга слоя.

Возвращает `UiRenderPolicy`: Активная политика [UiRenderPolicy](<ui-render-policy.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`renderPolicy(UiRenderPolicy)` {: #renderpolicyuirenderpolicy data-toc-label="renderPolicy(UiRenderPolicy)" }

**renderPolicy([UiRenderPolicy](<ui-render-policy.md>))**

Устанавливает политику рендеринга для слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `policy` | `UiRenderPolicy` | Политика [UiRenderPolicy](<ui-render-policy.md>). |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`root()` {: #root data-toc-label="root()" }

**root()**

Возвращает корневой виджет слоя.

Возвращает `Widget`: Экземпляр корневого виджета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`scaleWithGameGui()` {: #scalewithgamegui data-toc-label="scaleWithGameGui()" }

**scaleWithGameGui()**

Возвращает true, если масштаб слоя синхронизируется с настройками GUI игры.

Возвращает `boolean`: True при синхронизации масштаба..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`scaleWithGameGui(boolean)` {: #scalewithgameguiboolean data-toc-label="scaleWithGameGui(boolean)" }

**scaleWithGameGui(boolean)**

Определяет, должен ли слой синхронизировать масштаб с настройками GUI игры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `boolean` | True для включения синхронизации. |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`uiScale()` {: #uiscale data-toc-label="uiScale()" }

**uiScale()**

Возвращает пользовательский масштаб интерфейса или NaN, если не задан.

Возвращает `float`: Множитель масштабирования..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`uiScale(float)` {: #uiscalefloat data-toc-label="uiScale(float)" }

**uiScale(float)**

Устанавливает фиксированный масштаб интерфейса для слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `scale` | `float` | Положительный конечный масштабный коэффициент. |

Возвращает `UniRenderLayer`: Текущий экземпляр слоя для цепочки вызовов..

</div>

