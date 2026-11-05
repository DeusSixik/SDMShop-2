---
title: "ShaderDrawOptions"
slug: "shader-draw-options"
category: "Core/Shaders"
locale: "ru"
order: 20
---

# ShaderDrawOptions

Параметры команд отрисовки шейдеров: управление блендингом, стандартными uniforms и смещением вершин.

## Обзор и состояние видеокарты

`ShaderDrawOptions` настраивает параметры графического ускорителя при вызове `DrawScope#shader`.

- **Стандартные Uniforms**: Автоматически передаёт разрешение экрана, матрицы проекции и дефолтные параметры при `builtinUniforms = true`.
- **Блендинг**: Указывает бэкенду, требуется ли включение альфа-смешивания для шейдерного прохода.
- **Смещение вершин**: Корректирует выравнивание координат для полноэкранных и локальных quad-шейдеров.

## Стандартные Uniforms и SquareVertex

При включённом `builtinUniforms()` бэкенд автоматически добавляет в контекст шейдера стандартные параметры:

- `SquareVertex`: Нормализованное смещение вершин (по умолчанию `-0.25f` для совместимости со стандартными quad-шейдерами).
- Стандартные матрицы проекции и видового преобразования текущего движка.

## Настройка параметров шейдера

```java
ShaderDrawOptions options = ShaderDrawOptions.defaults()
        .blend(true)
        .builtinUniforms(true);

scope.shader(portalShader, x, y, width, height, uniforms, options);
```

## API

### Фабричные методы

| Метод | Описание |
| --- | --- |
| [`defaults()`](#defaults) | Возвращает новый экземпляр ShaderDrawOptions со стандартными настройками блендинга и uniforms. |
| [`copy()`](#copy) | Создаёт независимую точную копию этого объекта ShaderDrawOptions. |
| [`copyFrom(ShaderDrawOptions)`](#copyfromshaderdrawoptions) | Копирует параметры из другого ShaderDrawOptions без аллокаций памяти. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**Получить стандартные опции**

Возвращает новый экземпляр ShaderDrawOptions со стандартными настройками блендинга и uniforms.

Возвращает `ShaderDrawOptions`: Новый экземпляр ShaderDrawOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`copy()` {: #copy data-toc-label="copy()" }

**Копировать опции**

Создаёт независимую точную копию этого объекта ShaderDrawOptions.

Возвращает `ShaderDrawOptions`: Новая копия ShaderDrawOptions..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`copyFrom(ShaderDrawOptions)` {: #copyfromshaderdrawoptions data-toc-label="copyFrom(ShaderDrawOptions)" }

**Скопировать из источника**

Копирует параметры из другого ShaderDrawOptions без аллокаций памяти.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `source` | `ShaderDrawOptions` | Исходный объект параметров или null для сброса на дефолтные. |

Возвращает `ShaderDrawOptions`: Текущий объект options для цепочечного вызова..

</div>

### Конфигурация стейта рендера

| Метод | Описание |
| --- | --- |
| [`builtinUniforms()`](#builtinuniforms) | Возвращает true, если бэкенд должен автоматически передавать стандартные uniforms. |
| [`builtinUniforms(boolean)`](#builtinuniformsboolean) | Включает или выключает автоматическую передачу стандартных uniforms. |
| [`blend()`](#blend) | Возвращает true, если для команды шейдера включён альфа-блендинг. |
| [`blend(boolean)`](#blendboolean) | Настраивает активность альфа-блендинга для команды шейдера. |
| [`squareVertexOffset()`](#squarevertexoffset) | Возвращает смещение координат вершин для uniform SquareVertex. |
| [`squareVertexOffset(float)`](#squarevertexoffsetfloat) | Устанавливает смещение координат вершин для uniform SquareVertex. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`builtinUniforms()` {: #builtinuniforms data-toc-label="builtinUniforms()" }

**Проверить стандартные Uniforms**

Возвращает true, если бэкенд должен автоматически передавать стандартные uniforms.

Возвращает `boolean`: True, если стандартные uniforms включены..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`builtinUniforms(boolean)` {: #builtinuniformsboolean data-toc-label="builtinUniforms(boolean)" }

**Установить стандартные Uniforms**

Включает или выключает автоматическую передачу стандартных uniforms.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `builtinUniforms` | `boolean` | True для автоматической передачи стандартных uniforms движка. |

Возвращает `ShaderDrawOptions`: Текущий объект options для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`blend()` {: #blend data-toc-label="blend()" }

**Проверить включение блендинга**

Возвращает true, если для команды шейдера включён альфа-блендинг.

Возвращает `boolean`: True, если блендинг активен..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`blend(boolean)` {: #blendboolean data-toc-label="blend(boolean)" }

**Установить блендинг**

Настраивает активность альфа-блендинга для команды шейдера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `blend` | `boolean` | True для включения альфа-блендинга. |

Возвращает `ShaderDrawOptions`: Текущий объект options для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`squareVertexOffset()` {: #squarevertexoffset data-toc-label="squareVertexOffset()" }

**Получить смещение SquareVertex**

Возвращает смещение координат вершин для uniform SquareVertex.

Возвращает `float`: Значение смещения типа float..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`squareVertexOffset(float)` {: #squarevertexoffsetfloat data-toc-label="squareVertexOffset(float)" }

**Установить смещение SquareVertex**

Устанавливает смещение координат вершин для uniform SquareVertex.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `squareVertexOffset` | `float` | Значение смещения координат вершин. |

Возвращает `ShaderDrawOptions`: Текущий объект options для цепочечного вызова..

</div>

