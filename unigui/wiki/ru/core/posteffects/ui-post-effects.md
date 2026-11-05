---
title: "UiPostEffects"
slug: "ui-post-effects"
category: "Core/PostEffects"
locale: "ru"
order: 10
---

# UiPostEffects

Каталог стандартных пост-эффектов и встроенных шейдеров для слоев UniGUI.

## Обзор

`UiPostEffects` предоставляет готовые пресеты пост-эффектов и встроенные GLSL-шейдеры для слоев HUD, оверлеев и модальных экранов.

### Встроенные пресеты эффектов

- `unigui:passthrough`: Сквозной проход без визуальных изменений.
- `unigui:tint`: Цветовой тинт с настраиваемым цветом и интенсивностью.
- `unigui:vignette`: Радиальное затемнение по краям с регулировкой радиуса, мягкости и цвета.
- `unigui:barrel_distortion`: Искривление линзы («бочка») для имитации забрала шлема, очков или ЭЛТ-монитора.
- `unigui:chromatic_aberration`: Разделение цветовых каналов RGB вдоль вектора смещения.
- `unigui:scanline`: Анимированные горизонтальные строки развёртки ретро-экранов.

## Регистрация и применение эффектов

Метод `UiPostEffects.ensureRegistered()` регистрирует стандартные дефиниции и встроенный провайдер шейдеров в глобальных реестрах. Для применения к слою достаточно вызвать `layer.postEffect("unigui:vignette")`.

## Применение виньетки к HUD-слою

```java
UniRenderLayer layer = new UniRenderLayer(myHudWidget)
    .postEffect(UiPostEffects.chain(UiPostEffects.VIGNETTE));
UniHudRender.register(layer);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`barrelDistortion()` {: #barreldistortion data-toc-label="barrelDistortion()" }

**barrelDistortion()**

Возвращает описание эффекта дисторсии линзы.

Возвращает `UiPostEffectDefinition`: Экземпляр [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`chain(String)` {: #chainstring data-toc-label="chain(String)" }

**chain(String)**

Создает цепочку эффектов, ссылающуюся на указанный ID эффекта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `effectId` | `String` | Идентификатор зарегистрированного эффекта. |

Возвращает `UiPostEffectChain`: Экземпляр [UiPostEffectChain](<ui-post-effect-chain.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`chromaticAberration()` {: #chromaticaberration data-toc-label="chromaticAberration()" }

**chromaticAberration()**

Возвращает описание эффекта хроматической аберрации.

Возвращает `UiPostEffectDefinition`: Экземпляр [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`ensureRegistered()` {: #ensureregistered data-toc-label="ensureRegistered()" }

**ensureRegistered()**

Регистрирует все стандартные пост-эффекты и шейдеры в глобальном реестре.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`passthrough()` {: #passthrough data-toc-label="passthrough()" }

**passthrough()**

Возвращает описание эффекта сквозного копирования без визуальных изменений.

Возвращает `UiPostEffectDefinition`: Экземпляр [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`scanline()` {: #scanline data-toc-label="scanline()" }

**scanline()**

Возвращает описание эффекта анимированных строк развёртки.

Возвращает `UiPostEffectDefinition`: Экземпляр [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`shader(String)` {: #shaderstring data-toc-label="shader(String)" }

**shader(String)**

Возвращает дескриптор шейдера для встроенного пост-эффекта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `effectId` | `String` | Идентификатор зарегистрированного шейдера. |

Возвращает `ShaderHandle`: Экземпляр [ShaderHandle](<../shaders/shader-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`tint()` {: #tint data-toc-label="tint()" }

**tint()**

Возвращает описание эффекта цветового тинта.

Возвращает `UiPostEffectDefinition`: Экземпляр [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`vignette()` {: #vignette data-toc-label="vignette()" }

**vignette()**

Возвращает описание эффекта радиальной виньетки по краям.

Возвращает `UiPostEffectDefinition`: Экземпляр [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

