---
title: "UiPostEffectUniforms"
slug: "ui-post-effect-uniforms"
category: "Core/PostEffects"
locale: "ru"
order: 50
---

# UiPostEffectUniforms

Контейнер параметров uniforms пост-эффекта со статическими и динамическими поставщиками значений.

## Обзор

`UiPostEffectUniforms` предоставляет fluent-интерфейс для привязки uniforms к проходу пост-эффекта. Поддерживает как статические константы, так и динамические поставщики (`Supplier<Float>`, `Supplier<ShaderUniform>`), что позволяет реализовать непрерывную анимацию (например, фазу волн по таймеру) без пересоздания объекта прохода.

## Привязка динамических uniforms

```java
UiPostEffectUniforms uniforms = UiPostEffectUniforms.create()
    .floatValue("intensity", 0.85f)
    .vec2("center", 0.5f, 0.5f)
    .floatSupplier("time", () -> (System.currentTimeMillis() % 10000) / 1000.0f);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`boolValue(String, boolean)` {: #boolvaluestring-boolean data-toc-label="boolValue(String, boolean)" }

**boolValue(String, boolean)**

Задает логический uniform в виде 0 или 1.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `value` | `boolean` | Логическое значение. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`color(String, ColorView)` {: #colorstring-colorview data-toc-label="color(String, ColorView)" }

**color(String, ColorView)**

Задает цвет в виде 4-компонентного вектора uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `color` | `ColorView` | Экземпляр ColorView. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Возвращает независимую копию контейнера.

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`create()` {: #create data-toc-label="create()" }

**create()**

Создает новый пустой контейнер uniforms.

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`empty()` {: #empty data-toc-label="empty()" }

**empty()**

Возвращает пустой контейнер uniforms.

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`floatSupplier(String, Supplier)` {: #floatsupplierstring-supplier data-toc-label="floatSupplier(String, Supplier)" }

**floatSupplier(String, Supplier<Float>)**

Задает динамический float поставщик uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `supplier` | `Supplier` | Поставщик float. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`floatValue(String, float)` {: #floatvaluestring-float data-toc-label="floatValue(String, float)" }

**floatValue(String, float)**

Задает статический float uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `value` | `float` | Значение float. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`intValue(String, int)` {: #intvaluestring-int data-toc-label="intValue(String, int)" }

**intValue(String, int)**

Задает статический целочисленный uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `value` | `int` | Значение int. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`isEmpty()` {: #isempty data-toc-label="isEmpty()" }

**isEmpty()**

Возвращает true, если контейнер не содержит uniforms.

Возвращает `boolean`: Экземпляр boolean..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`resolve()` {: #resolve data-toc-label="resolve()" }

**resolve()**

Разрешает статические и динамические значения в снимок [ShaderUniforms](<../shaders/shader-uniforms.md>).

Возвращает `ShaderUniforms`: Экземпляр [ShaderUniforms](<../shaders/shader-uniforms.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`set(String, ShaderUniform)` {: #setstring-shaderuniform data-toc-label="set(String, ShaderUniform)" }

**set(String, ShaderUniform)**

Задает статическое значение uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `uniform` | `ShaderUniform` | Значение ShaderUniform. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`setDynamic(String, Supplier)` {: #setdynamicstring-supplier data-toc-label="setDynamic(String, Supplier)" }

**setDynamic(String, Supplier<ShaderUniform>)**

Задает динамический поставщик значения uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `supplier` | `Supplier` | Функция-поставщик. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`vec2(String, float, float)` {: #vec2string-float-float data-toc-label="vec2(String, float, float)" }

**vec2(String, float, float)**

Задает 2-компонентный векторный uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `x` | `float` | Компонент X. |
| `y` | `float` | Компонент Y. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`vec3(String, float, float, float)` {: #vec3string-float-float-float data-toc-label="vec3(String, float, float, float)" }

**vec3(String, float, float, float)**

Задает 3-компонентный векторный uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `x` | `float` | Компонент X. |
| `y` | `float` | Компонент Y. |
| `z` | `float` | Компонент Z. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`vec4(String, float, float, float, float)` {: #vec4string-float-float-float-float data-toc-label="vec4(String, float, float, float, float)" }

**vec4(String, float, float, float, float)**

Задает 4-компонентный векторный uniform.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя uniform. |
| `x` | `float` | Компонент X. |
| `y` | `float` | Компонент Y. |
| `z` | `float` | Компонент Z. |
| `w` | `float` | Компонент W. |

Возвращает `UiPostEffectUniforms`: Экземпляр UiPostEffectUniforms..

</div>

