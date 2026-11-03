---
title: "UiPostEffectRegistry"
slug: "ui-post-effect-registry"
category: "Core/PostEffects"
locale: "ru"
order: 60
---

# UiPostEffectRegistry

Реестр для регистрации и поиска именованных дефиниций пост-эффектов.

## Обзор

`UiPostEffectRegistry` хранит объекты `UiPostEffectDefinition` по строковым ключам. Синглтон `UiPostEffectRegistry.global()` используется кодом, XML-разметкой и [StylePack](<../styling/style-pack.md>) для поиска конвейеров пост-эффектов по имени.

## Регистрация кастомного пост-эффекта

```java
UiPostEffectDefinition crtDef = UiPostEffectDefinition.create("mymod:crt")
    .pass("mymod:scanlines")
    .pass("mymod:curvature")
    .build();
UiPostEffectRegistry.global().register(crtDef);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`definitions()` {: #definitions data-toc-label="definitions()" }

**definitions()**

Возвращает снимок всех зарегистрированных дефиниций.

Возвращает `List`: Экземпляр List<UiPostEffectDefinition>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`find(String)` {: #findstring data-toc-label="find(String)" }

**find(String)**

Ищет дефиницию эффекта по идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор эффекта. |

Возвращает `Optional`: Экземпляр Optional<UiPostEffectDefinition>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`global()` {: #global data-toc-label="global()" }

**global()**

Возвращает глобальный синглтон реестра пост-эффектов.

Возвращает `UiPostEffectRegistry`: Экземпляр UiPostEffectRegistry..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`ids()` {: #ids data-toc-label="ids()" }

**ids()**

Возвращает неизменяемый список всех зарегистрированных ID эффектов.

Возвращает `List`: Экземпляр List<String>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`register(UiPostEffectDefinition)` {: #registeruiposteffectdefinition data-toc-label="register(UiPostEffectDefinition)" }

**register([UiPostEffectDefinition](<ui-post-effect-definition.md>))**

Регистрирует дефиницию эффекта, заменяя существующую с тем же ID.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `definition` | `UiPostEffectDefinition` | Регистрируемая дефиниция. |

Возвращает `UiPostEffectRegistry`: Экземпляр UiPostEffectRegistry..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`unregister(String)` {: #unregisterstring data-toc-label="unregister(String)" }

**unregister(String)**

Удаляет дефиницию эффекта по идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор эффекта. |

Возвращает `boolean`: Экземпляр boolean..

</div>

