---
title: "UiPostEffectChain"
slug: "ui-post-effect-chain"
category: "Core/PostEffects"
locale: "ru"
order: 20
---

# UiPostEffectChain

Неизменяемая ссылка на конвейер пост-эффектов для UI-слоя.

## Обзор

`UiPostEffectChain` инкапсулирует последовательность шейдерных проходов (pass), применяемых к закадровому буферу слоя. Цепочка может быть пустой (`none()`), ссылаться на ID зарегистрированного эффекта (`of("unigui:tint")`) или содержать встроенные проходы.

Бэкенд разрешает цепочки лениво в момент отрисовки. Если указанный ID не найден или бэкенд не поддерживает пост-эффекты, конвейер прозрачно откатывается к стандартному рендерингу без сбоев.

## Создание цепочек эффектов

```java
// Ссылка на зарегистрированный эффект
UiPostEffectChain vignetteChain = UiPostEffectChain.of("unigui:vignette");

// Пустая цепочка без эффектов
UiPostEffectChain cleanChain = UiPostEffectChain.none();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Возвращает независимую копию этой цепочки.

Возвращает `UiPostEffectChain`: Экземпляр UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`effectId()` {: #effectid data-toc-label="effectId()" }

**effectId()**

Возвращает идентификатор эффекта или null.

Возвращает `String`: Экземпляр String..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`inlinePasses()` {: #inlinepasses data-toc-label="inlinePasses()" }

**inlinePasses()**

Возвращает список встроенных проходов, если они заданы напрямую.

Возвращает `List`: Экземпляр List<UiPostEffectPass>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`isNone()` {: #isnone data-toc-label="isNone()" }

**isNone()**

Возвращает true, если цепочка пуста и не выполняет пост-обработку.

Возвращает `boolean`: Экземпляр boolean..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`none()` {: #none data-toc-label="none()" }

**none()**

Возвращает пустую цепочку без пост-обработки.

Возвращает `UiPostEffectChain`: Экземпляр UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`of(List)` {: #oflist data-toc-label="of(List)" }

**of(List<UiPostEffectPass>)**

Создает встроенную цепочку из списка шейдерных проходов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `passes` | `List` | Список проходов [UiPostEffectPass](<ui-post-effect-pass.md>). |

Возвращает `UiPostEffectChain`: Экземпляр UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`of(String)` {: #ofstring data-toc-label="of(String)" }

**of(String)**

Создает цепочку, ссылающуюся на ID зарегистрированного эффекта.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `effectId` | `String` | Строковый идентификатор эффекта. |

Возвращает `UiPostEffectChain`: Экземпляр UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`of(UiPostEffectDefinition)` {: #ofuiposteffectdefinition data-toc-label="of(UiPostEffectDefinition)" }

**of([UiPostEffectDefinition](<ui-post-effect-definition.md>))**

Создает цепочку с проходами из указанной дефиниции.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `definition` | `UiPostEffectDefinition` | Дефиниция пост-эффекта. |

Возвращает `UiPostEffectChain`: Экземпляр UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`resolve(UiPostEffectRegistry)` {: #resolveuiposteffectregistry data-toc-label="resolve(UiPostEffectRegistry)" }

**resolve([UiPostEffectRegistry](<ui-post-effect-registry.md>))**

Разрешает цепочку в упорядоченный список проходов через реестр.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `registry` | `UiPostEffectRegistry` | Реестр [UiPostEffectRegistry](<ui-post-effect-registry.md>). |

Возвращает `List`: Экземпляр List<UiPostEffectPass>..

</div>

