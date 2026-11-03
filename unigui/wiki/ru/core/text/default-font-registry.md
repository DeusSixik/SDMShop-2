---
title: "DefaultFontRegistry"
slug: "default-font-registry"
category: "Core/Text"
locale: "ru"
order: 40
---

# DefaultFontRegistry

Стандартный реестр шрифтов, загружающий TrueType и Type1 шрифты из файлов, потоков или системных шрифтов ОС без зависимостей от Minecraft.

## Обзор

`DefaultFontRegistry` предоставляет стандартную реализацию управления шрифтами. Позволяет регистрировать шрифты из файлов на диске, потоков ввода и байтовых массивов, а также подключать системные шрифты операционной системы без обращения к ресурсам Minecraft.

## Загрузка и регистрация шрифтов

Шрифты регистрируются под уникальными строковыми идентификаторами через перегрузки метода `register()`. Поддерживаются форматы TrueType (`.ttf`) и Type1, автоматически конвертируемые в объекты `FontFace`.

## Дефолтный и системные шрифты

Реестр поддерживает резервный шрифт по умолчанию, возвращаемый методом `defaultFace()`. Если кастомный шрифт не был назначен, автоматически загружается системный шрифт `SansSerif`. Дополнительные шрифты ОС регистрируются через `registerSystem()`.

## Управление жизненным циклом

Реестр реализует интерфейс `AutoCloseable`. Вызов метода `close()` очищает все зарегистрированные гарнитуры и сбрасывает дефолтный шрифт, предотвращая утечки ресурсов.

## Регистрация TrueType-шрифта из ресурса

```java
DefaultFontRegistry registry = DefaultFontRegistry.global();
try (InputStream stream = getClass().getResourceAsStream("/fonts/custom.ttf")) {
    registry.register("custom-font", stream);
}
```

## Настройка системного шрифта по умолчанию

```java
DefaultFontRegistry registry = DefaultFontRegistry.global();
FontFace roboto = registry.registerSystem("roboto", "Roboto");
registry.defaultFace(roboto);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`global()` {: #global data-toc-label="global()" }

**global()**

Возвращает глобальный синглтон-реестр шрифтов по умолчанию.

Возвращает `DefaultFontRegistry`: Глобальный экземпляр DefaultFontRegistry..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`register(String, Path)` {: #registerstring-path data-toc-label="register(String, Path)" }

**register(String, [Path](<../../widgets/display/path.md>))**

Загружает и регистрирует файл шрифта из файловой системы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор шрифта. |
| `path` | `Path` | Путь к файлу шрифта. |

Возвращает `FontFace`: Зарегистрированный экземпляр FontFace..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`register(String, InputStream)` {: #registerstring-inputstream data-toc-label="register(String, InputStream)" }

**register(String, InputStream)**

Читает и регистрирует шрифт из входного потока.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор шрифта. |
| `input` | `InputStream` | Входной поток данных. |

Возвращает `FontFace`: Зарегистрированный экземпляр FontFace..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`register(String, byte[])` {: #registerstring-byte data-toc-label="register(String, byte[])" }

**register(String, byte[])**

Регистрирует шрифт из массива байт.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор шрифта. |
| `data` | `byte[]` | Байты файла шрифта. |

Возвращает `FontFace`: Зарегистрированный экземпляр FontFace..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`registerSystem(String, String)` {: #registersystemstring-string data-toc-label="registerSystem(String, String)" }

**registerSystem(String, String)**

Регистрирует семейство шрифтов операционной системы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор шрифта. |
| `family` | `String` | Название семейства шрифта в ОС. |

Возвращает `FontFace`: Зарегистрированный экземпляр FontFace..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`find(String)` {: #findstring data-toc-label="find(String)" }

**find(String)**

Ищет зарегистрированную гарнитуру шрифта по идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Идентификатор шрифта. |

Возвращает `FontFace`: Экземпляр FontFace или null, если не найден..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`defaultFace()` {: #defaultface data-toc-label="defaultFace()" }

**defaultFace()**

Возвращает резервный шрифт по умолчанию.

Возвращает `FontFace`: Дефолтный экземпляр FontFace..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`defaultFace(FontFace)` {: #defaultfacefontface data-toc-label="defaultFace(FontFace)" }

**defaultFace(FontFace)**

Устанавливает резервный шрифт по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `face` | `FontFace` | FontFace для назначения дефолтным. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`faces()` {: #faces data-toc-label="faces()" }

**faces()**

Возвращает неизменяемую карту всех зарегистрированных шрифтов.

Возвращает `Map`: Карта ID -> FontFace..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`close()` {: #close data-toc-label="close()" }

**close()**

Очищает все зарегистрированные шрифты и сбрасывает дефолтный шрифт.

</div>

