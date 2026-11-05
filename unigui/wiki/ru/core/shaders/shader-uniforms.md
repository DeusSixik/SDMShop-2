---
title: "ShaderUniforms"
slug: "shader-uniforms"
category: "Core/Shaders"
locale: "ru"
order: 30
---

# ShaderUniforms

Изменяемая коллекция именованных параметров шейдера (float, int, векторы, цвета), передаваемых в GPU-программы.

## Обзор и порядок добавления

`ShaderUniforms` хранит именованные параметры (uniforms) для шейдерных команд отрисовки.

- **Сохранение порядка**: Бэкенд обходит uniforms в порядке добавления, что упрощает диагностику и ускоряет загрузку на GPU.
- **Защитное копирование**: Значения копируются при записи, позволяя переиспользовать временные объекты цвета или векторов.
- **Удобный fluent API**: Прямые методы для `float`, `int`, `vec2`, `vec3`, `vec4`, `ColorView` и ARGB-чисел.

## Поддерживаемые типы Uniforms

| Тип | Метод записи | GLSL эквивалент |
| --- | --- | --- |
| loat | setFloat(name, val) | uniform float name; |
| int | setInt(name, val) | uniform int name; |
| ec2 | setVec2(name, x, y) | uniform vec2 name; |
| ec3 | setVec3(name, x, y, z) | uniform vec3 name; |
| ec4 | setVec4(name, x, y, z, w) | uniform vec4 name; |
| ColorView | setColor(name, color) | uniform vec4 name; (RGBA нормализованный 0..1) |

## Заполнение параметров шейдера

```java
ShaderUniforms uniforms = ShaderUniforms.create()
        .setVec2("u_resolution", width, height)
        .setFloat("u_time", (System.currentTimeMillis() % 10000) / 1000.0f)
        .setColor("u_tint", ColorView.rgb(0, 180, 255));

scope.shader(waterShader, x, y, width, height, uniforms, ShaderDrawOptions.defaults());
```

## API

### Фабричные методы

| Метод | Описание |
| --- | --- |
| [`empty()`](#empty) | Возвращает новый пустой набор ShaderUniforms. |
| [`create()`](#create) | Возвращает новый изменяемый набор ShaderUniforms. |
| [`clear()`](#clear) | Очищает все записанные параметры без создания новой коллекции. |
| [`copy()`](#copy) | Создаёт точную независимую копию этой коллекции параметров. |
| [`copyFrom(ShaderUniforms)`](#copyfromshaderuniforms) | Копирует параметры из другой коллекции без создания новых объектов. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`empty()` {: #empty data-toc-label="empty()" }

**Создать пустой набор**

Возвращает новый пустой набор ShaderUniforms.

Возвращает `ShaderUniforms`: Пустой экземпляр ShaderUniforms..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`create()` {: #create data-toc-label="create()" }

**Создать набор**

Возвращает новый изменяемый набор ShaderUniforms.

Возвращает `ShaderUniforms`: Новый экземпляр ShaderUniforms..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`clear()` {: #clear data-toc-label="clear()" }

**Очистить параметры**

Очищает все записанные параметры без создания новой коллекции.

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`copy()` {: #copy data-toc-label="copy()" }

**Копировать коллекцию**

Создаёт точную независимую копию этой коллекции параметров.

Возвращает `ShaderUniforms`: Новая копия ShaderUniforms..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`copyFrom(ShaderUniforms)` {: #copyfromshaderuniforms data-toc-label="copyFrom(ShaderUniforms)" }

**Скопировать из другой коллекции**

Копирует параметры из другой коллекции без создания новых объектов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `source` | `ShaderUniforms` | Исходная коллекция параметров или null для очистки. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

### Запись параметров (Setters)

| Метод | Описание |
| --- | --- |
| [`set(String, ShaderUniform)`](#setstring-shaderuniform) | Сохраняет значение ShaderUniform под указанным именем. |
| [`setFloat(String, float)`](#setfloatstring-float) | Сохраняет одиночный float параметр под указанным именем. |
| [`setInt(String, int)`](#setintstring-int) | Сохраняет целочисленный int параметр под указанным именем. |
| [`setVec2(String, float, float)`](#setvec2string-float-float) | Сохраняет двухкомпонентный вектор (x, y) под указанным именем. |
| [`setVec3(String, float, float, float)`](#setvec3string-float-float-float) | Сохраняет трёхкомпонентный вектор (x, y, z) под указанным именем. |
| [`setVec4(String, float, float, float, float)`](#setvec4string-float-float-float-float) | Сохраняет четырёхкомпонентный вектор (x, y, z, w) под указанным именем. |
| [`setColor(String, ColorView)`](#setcolorstring-colorview) | Преобразует ColorView в нормализованный vec4 (r, g, b, a) параметр. |
| [`setColorArgb(String, int)`](#setcolorargbstring-int) | Преобразует упакованное ARGB-число в нормализованный vec4 параметр. |
| [`setMat4(String, float[])`](#setmat4string-float) | Сохраняет матрицу 4x4 из массива 16 float-значений. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`set(String, ShaderUniform)` {: #setstring-shaderuniform data-toc-label="set(String, ShaderUniform)" }

**Записать ShaderUniform**

Сохраняет значение ShaderUniform под указанным именем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `uniform` | `ShaderUniform` | Объект ShaderUniform с данными параметра. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setFloat(String, float)` {: #setfloatstring-float data-toc-label="setFloat(String, float)" }

**Записать float**

Сохраняет одиночный float параметр под указанным именем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `value` | `float` | Примитивное значение типа float или int. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setInt(String, int)` {: #setintstring-int data-toc-label="setInt(String, int)" }

**Записать int**

Сохраняет целочисленный int параметр под указанным именем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `value` | `int` | Примитивное значение типа float или int. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setVec2(String, float, float)` {: #setvec2string-float-float data-toc-label="setVec2(String, float, float)" }

**Записать vec2**

Сохраняет двухкомпонентный вектор (x, y) под указанным именем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `x` | `float` | Первая координата вектора (X). |
| `y` | `float` | Вторая координата вектора (Y). |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setVec3(String, float, float, float)` {: #setvec3string-float-float-float data-toc-label="setVec3(String, float, float, float)" }

**Записать vec3**

Сохраняет трёхкомпонентный вектор (x, y, z) под указанным именем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `x` | `float` | Первая координата вектора (X). |
| `y` | `float` | Вторая координата вектора (Y). |
| `z` | `float` | Третья координата вектора (Z). |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setVec4(String, float, float, float, float)` {: #setvec4string-float-float-float-float data-toc-label="setVec4(String, float, float, float, float)" }

**Записать vec4**

Сохраняет четырёхкомпонентный вектор (x, y, z, w) под указанным именем.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `x` | `float` | Первая координата вектора (X). |
| `y` | `float` | Вторая координата вектора (Y). |
| `z` | `float` | Третья координата вектора (Z). |
| `w` | `float` | Четвёртая координата вектора (W). |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setColor(String, ColorView)` {: #setcolorstring-colorview data-toc-label="setColor(String, ColorView)" }

**Записать цвет ColorView**

Преобразует ColorView в нормализованный vec4 (r, g, b, a) параметр.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `color` | `ColorView` | Источник ColorView для RGBA-параметра. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setColorArgb(String, int)` {: #setcolorargbstring-int data-toc-label="setColorArgb(String, int)" }

**Записать ARGB цвет**

Преобразует упакованное ARGB-число в нормализованный vec4 параметр.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `argb` | `int` | Упакованное 32-битное число в формате ARGB. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setMat4(String, float[])` {: #setmat4string-float data-toc-label="setMat4(String, float[])" }

**Записать матрицу mat4**

Сохраняет матрицу 4x4 из массива 16 float-значений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | Имя параметра uniform в коде шейдера. |
| `values` | `float[]` | Массив ровно из 16 значений float для матрицы. |

Возвращает `ShaderUniforms`: Текущая коллекция для цепочечного вызова..

</div>

### Чтение и проверка параметров

| Метод | Описание |
| --- | --- |
| [`values()`](#values) | Возвращает неизменяемую карту всех зарегистрированных параметров. |
| [`isEmpty()`](#isempty) | Возвращает true, если в наборе нет параметров. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`values()` {: #values data-toc-label="values()" }

**Получить карту параметров**

Возвращает неизменяемую карту всех зарегистрированных параметров.

Возвращает `Map`: Неизменяемая карта Map параметров..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`isEmpty()` {: #isempty data-toc-label="isEmpty()" }

**Проверить пустоту**

Возвращает true, если в наборе нет параметров.

Возвращает `boolean`: True, если коллекция пуста..

</div>

