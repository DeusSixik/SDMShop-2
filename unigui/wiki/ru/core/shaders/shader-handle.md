---
title: "ShaderHandle"
slug: "shader-handle"
category: "Core/Shaders"
locale: "ru"
order: 10
---

# ShaderHandle

Идентификатор и контейнер UI-шейдеров, ссылающийся на путь к ресурсу или содержащий встроенный GLSL-код.

## Архитектура и разрешение GLSL

`ShaderHandle` представляет шейдерный пайплайн видеокарты для команд `DrawCommandType.SHADER` и `DrawScope#shader`.

### Два режима работы

1. **Идентификатор ресурса (`ShaderHandle.resource(id)`)**: Ссылается на именованный ресурс шейдера (`minecraft:rendertype_gui` или шейдер мода). Бэкенд запрашивает зарегистрированные `ShaderProvider` для загрузки исходного кода.
2. **Встроенный GLSL-код (`ShaderHandle.source(...)` или `fragmentSource(...)`)**: Хранит строки vertex/fragment GLSL-шейдеров прямо в объекте для процедурных эффектов без необходимости создания отдельных файлов ресурсов.

## Пайплайн исполнения шейдера

При вызове `DrawScope#shader`:

1. `ShaderHandle` указывает исполняемую программу.
2. `ShaderUniforms` передаёт пользовательские uniforms (разрешение, время, цвета, координаты).
3. `ShaderDrawOptions` настраивает блендинг и автоматические стандартные uniforms (`SquareVertex`).

Бэкенд компилирует и кэширует GLSL-программы по ключу `id()`, предотвращая повторную компиляцию между кадрами.

## Процедурный радиальный градиент

```java
// Встроенный fragment shader для пульсирующего портала
String glsl = """
#version 150
uniform vec2 u_resolution;
uniform float u_time;
out vec4 fragColor;

void main() {
    vec2 st = gl_FragCoord.xy / u_resolution;
    float dist = distance(st, vec2(0.5));
    float pulse = 0.5 + 0.5 * sin(u_time * 3.0);
    fragColor = vec4(0.2, 0.6, 1.0, (1.0 - smoothstep(0.1, 0.5, dist)) * pulse);
}
""";

ShaderHandle portalShader = ShaderHandle.fragmentSource("mod:portal_effect", glsl);
```

## API

### Фабричные методы

| Метод | Описание |
| --- | --- |
| [`resource(String)`](#resourcestring) | Создаёт ShaderHandle, ссылающийся на внешний ресурс шейдера, разрешаемый через ShaderProviders. |
| [`fragmentSource(String, String)`](#fragmentsourcestring-string) | Создаёт ShaderHandle со встроенным GLSL-кодом fragment shader. |
| [`source(String, String, String)`](#sourcestring-string-string) | Создаёт ShaderHandle со встроенным GLSL-кодом для vertex и fragment шейдеров. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`resource(String)` {: #resourcestring data-toc-label="resource(String)" }

**Создать дескриптор ресурса**

Создаёт ShaderHandle, ссылающийся на внешний ресурс шейдера, разрешаемый через ShaderProviders.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор ресурса шейдера или встроенной программы. |

Возвращает `ShaderHandle`: Новый дескриптор ShaderHandle на ресурс..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`fragmentSource(String, String)` {: #fragmentsourcestring-string data-toc-label="fragmentSource(String, String)" }

**Создать со встроенным fragment кодом**

Создаёт ShaderHandle со встроенным GLSL-кодом fragment shader.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор ресурса шейдера или встроенной программы. |
| `fragmentSource` | `String` | GLSL-код фрагментного шейдера. |

Возвращает `ShaderHandle`: Новый ShaderHandle со встроенным fragment кодом..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`source(String, String, String)` {: #sourcestring-string-string data-toc-label="source(String, String, String)" }

**Создать с vertex и fragment кодом**

Создаёт ShaderHandle со встроенным GLSL-кодом для vertex и fragment шейдеров.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор ресурса шейдера или встроенной программы. |
| `vertexSource` | `String` | GLSL-код вершинного шейдера. |
| `fragmentSource` | `String` | GLSL-код фрагментного шейдера. |

Возвращает `ShaderHandle`: Новый ShaderHandle с полным встроенным кодом..

</div>

### Свойства и исходный код

| Метод | Описание |
| --- | --- |
| [`id()`](#id) | Возвращает стабильный строковый идентификатор шейдера. |
| [`vertexSource()`](#vertexsource) | Возвращает встроенный GLSL-код vertex shader или null. |
| [`fragmentSource()`](#fragmentsource) | Возвращает встроенный GLSL-код fragment shader или null. |
| [`hasEmbeddedVertexSource()`](#hasembeddedvertexsource) | Возвращает true, если хэндл содержит встроенный vertex source. |
| [`hasEmbeddedFragmentSource()`](#hasembeddedfragmentsource) | Возвращает true, если хэндл содержит встроенный fragment source. |
| [`copy()`](#copy) | Создаёт независимую копию этого ShaderHandle. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`id()` {: #id data-toc-label="id()" }

**Получить ID шейдера**

Возвращает стабильный строковый идентификатор шейдера.

Возвращает `String`: Нормализованный ID шейдера..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`vertexSource()` {: #vertexsource data-toc-label="vertexSource()" }

**Получить vertex код**

Возвращает встроенный GLSL-код vertex shader или null.

Возвращает `String`: Строка с GLSL vertex source либо null..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`fragmentSource()` {: #fragmentsource data-toc-label="fragmentSource()" }

**Получить fragment код**

Возвращает встроенный GLSL-код fragment shader или null.

Возвращает `String`: Строка с GLSL fragment source либо null..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`hasEmbeddedVertexSource()` {: #hasembeddedvertexsource data-toc-label="hasEmbeddedVertexSource()" }

**Проверить наличие vertex кода**

Возвращает true, если хэндл содержит встроенный vertex source.

Возвращает `boolean`: True, если vertex source присутствует..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`hasEmbeddedFragmentSource()` {: #hasembeddedfragmentsource data-toc-label="hasEmbeddedFragmentSource()" }

**Проверить наличие fragment кода**

Возвращает true, если хэндл содержит встроенный fragment source.

Возвращает `boolean`: True, если fragment source присутствует..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`copy()` {: #copy data-toc-label="copy()" }

**Копировать ShaderHandle**

Создаёт независимую копию этого ShaderHandle.

Возвращает `ShaderHandle`: Новый ShaderHandle с идентичными id и кодом..

</div>

