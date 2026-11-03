---
title: "UiPostEffectPass"
slug: "ui-post-effect-pass"
category: "Core/PostEffects"
locale: "ru"
order: 40
---

# UiPostEffectPass

Конфигурация одного шейдерного прохода внутри конвейера пост-эффектов.

## Обзор

`UiPostEffectPass` задает состояние рендера для одного вызова шейдера в цепочке. Объединяет `ShaderHandle`, параметры uniforms (`UiPostEffectUniforms`), дополнительные сэмплеры текстур (`TextureHandle`) и режим смешивания (`UiPostEffectBlendMode`).

Стандартные входные данные (`SourceTexture`, `SourceSize`, `SourceFlipY`) автоматически передаются графическим бэкендом.

## Настройка прохода с параметрами и текстурой

```java
UiPostEffectPass pass = UiPostEffectPass.shader("mymod:overlay")
    .blendMode(UiPostEffectBlendMode.ALPHA_BLEND)
    .texture("NoiseTexture", noiseTexHandle)
    .uniforms(u -> u.floatValue("intensity", 0.75f));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`blendMode()` {: #blendmode data-toc-label="blendMode()" }

**blendMode()**

Возвращает режим смешивания прохода.

Возвращает `UiPostEffectBlendMode`: Экземпляр [UiPostEffectBlendMode](<ui-post-effect-blend-mode.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`blendMode(UiPostEffectBlendMode)` {: #blendmodeuiposteffectblendmode data-toc-label="blendMode(UiPostEffectBlendMode)" }

**blendMode([UiPostEffectBlendMode](<ui-post-effect-blend-mode.md>))**

Возвращает копию прохода с новым режимом смешивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `blendMode` | `UiPostEffectBlendMode` | Режим [UiPostEffectBlendMode](<ui-post-effect-blend-mode.md>). |

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Возвращает независимую копию прохода.

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shader()` {: #shader data-toc-label="shader()" }

**shader()**

Возвращает копию дескриптора шейдера.

Возвращает `ShaderHandle`: Экземпляр [ShaderHandle](<../shaders/shader-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shader(ShaderHandle)` {: #shadershaderhandle data-toc-label="shader(ShaderHandle)" }

**shader([ShaderHandle](<../shaders/shader-handle.md>))**

Создает проход из экземпляра [ShaderHandle](<../shaders/shader-handle.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `shader` | `ShaderHandle` | Экземпляр [ShaderHandle](<../shaders/shader-handle.md>). |

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shader(String)` {: #shaderstring data-toc-label="shader(String)" }

**shader(String)**

Создает проход по идентификатору ресурса шейдера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `shaderId` | `String` | ID ресурса шейдера. |

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shaderId()` {: #shaderid data-toc-label="shaderId()" }

**shaderId()**

Возвращает стабильный идентификатор шейдера.

Возвращает `String`: Экземпляр String..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`texture(String, TextureHandle)` {: #texturestring-texturehandle data-toc-label="texture(String, TextureHandle)" }

**texture(String, [TextureHandle](<../textures/texture-handle.md>))**

Возвращает копию прохода с добавленной текстурой сэмплера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `uniformName` | `String` | Имя uniform-сэмплера. |
| `texture` | `TextureHandle` | Экземпляр [TextureHandle](<../textures/texture-handle.md>). |

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`textures()` {: #textures data-toc-label="textures()" }

**textures()**

Возвращает карту дополнительных текстур для сэмплеров.

Возвращает `Map`: Экземпляр Map<String, TextureHandle>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`uniforms()` {: #uniforms data-toc-label="uniforms()" }

**uniforms()**

Возвращает копию параметров uniforms прохода.

Возвращает `UiPostEffectUniforms`: Экземпляр [UiPostEffectUniforms](<ui-post-effect-uniforms.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`uniforms(Consumer)` {: #uniformsconsumer data-toc-label="uniforms(Consumer)" }

**uniforms(Consumer<UiPostEffectUniforms>)**

Возвращает копию прохода с настройкой uniforms через колбэк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `config` | `Consumer` | Конфигуратор uniforms. |

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`uniforms(UiPostEffectUniforms)` {: #uniformsuiposteffectuniforms data-toc-label="uniforms(UiPostEffectUniforms)" }

**uniforms([UiPostEffectUniforms](<ui-post-effect-uniforms.md>))**

Возвращает копию прохода с новым набором uniforms.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `uniforms` | `UiPostEffectUniforms` | Новый набор uniforms. |

Возвращает `UiPostEffectPass`: Экземпляр UiPostEffectPass..

</div>

