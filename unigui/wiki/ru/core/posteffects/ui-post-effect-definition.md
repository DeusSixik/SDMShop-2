---
title: "UiPostEffectDefinition"
slug: "ui-post-effect-definition"
category: "Core/PostEffects"
locale: "ru"
order: 30
---

# UiPostEffectDefinition

Неизменяемое описание пост-эффекта с упорядоченными шейдерными проходами.

## Обзор

`UiPostEffectDefinition` описывает именованный эффект, состоящий из одного или нескольких последовательных проходов (`UiPostEffectPass`). Многопроходные эффекты (например, двухпроходное размытие по Гауссу или bloom) переключаются между промежуточными текстурными буферами перед финальным выводом.

## Создание многопроходного эффекта

```java
UiPostEffectDefinition customBloom = UiPostEffectDefinition.create("mymod:bloom")
    .pass("mymod:bright_pass")
    .pass("mymod:blur_h", uniforms -> uniforms.floatValue("radius", 4.0f))
    .pass("mymod:blur_v", uniforms -> uniforms.floatValue("radius", 4.0f))
    .build();
UiPostEffectRegistry.global().register(customBloom);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Возвращает независимую копию дефиниции.

Возвращает `UiPostEffectDefinition`: Экземпляр UiPostEffectDefinition..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`create(String)` {: #createstring data-toc-label="create(String)" }

**create(String)**

Создает новый строитель дефиниции пост-эффекта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор эффекта. |

Возвращает `Builder`: Экземпляр UiPostEffectDefinition.Builder..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`enabledByDefault()` {: #enabledbydefault data-toc-label="enabledByDefault()" }

**enabledByDefault()**

Возвращает true, если эффект активен по умолчанию.

Возвращает `boolean`: Экземпляр boolean..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`id()` {: #id data-toc-label="id()" }

**id()**

Возвращает уникальный идентификатор эффекта.

Возвращает `String`: Экземпляр String..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`passes()` {: #passes data-toc-label="passes()" }

**passes()**

Возвращает неизменяемый список шейдерных проходов.

Возвращает `List`: Экземпляр List<UiPostEffectPass>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`singlePass(String)` {: #singlepassstring data-toc-label="singlePass(String)" }

**singlePass(String)**

Создает однопроходную дефиницию, где ID шейдера совпадает с ID эффекта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор эффекта. |

Возвращает `UiPostEffectDefinition`: Экземпляр UiPostEffectDefinition..

</div>

