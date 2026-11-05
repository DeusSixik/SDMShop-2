---
title: "DefaultFontRegistry"
slug: "default-font-registry"
category: "Core/Text"
locale: "en"
order: 40
---

# DefaultFontRegistry

Standard font registry loading TrueType and Type1 fonts from streams, files, or platform system fonts without Minecraft dependencies.

## Overview

`DefaultFontRegistry` provides the default standalone font management implementation. It allows registering fonts from filesystem paths, resource input streams, or byte arrays, and supports system font fallbacks without depending on Minecraft client asset loaders.

## Font Loading and Registration

Fonts are registered under unique identifier strings via `register()`. Both TrueType (`.ttf`) and Type1 font formats are automatically parsed and mapped into `FontFace` objects.

## Default and System Fonts

The registry maintains a default font face accessible via `defaultFace()`. If no custom font has been set, it automatically loads a system fallback (e.g. `SansSerif`). Custom system families can be registered via `registerSystem()`.

## Lifecycle Management

Implements `AutoCloseable`. Calling `close()` clears all registered font faces and resets the default font reference, preventing memory leaks during application reload.

## Registering TrueType Font from Resource

```java
DefaultFontRegistry registry = DefaultFontRegistry.global();
try (InputStream stream = getClass().getResourceAsStream("/fonts/custom.ttf")) {
    registry.register("custom-font", stream);
}
```

## Configuring System Fallback Font

```java
DefaultFontRegistry registry = DefaultFontRegistry.global();
FontFace roboto = registry.registerSystem("roboto", "Roboto");
registry.defaultFace(roboto);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`global()` {: #global data-toc-label="global()" }

**global()**

Returns the singleton global default font registry instance.

Returns `DefaultFontRegistry`: Global DefaultFontRegistry instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`register(String, Path)` {: #registerstring-path data-toc-label="register(String, Path)" }

**register(String, [Path](<../../widgets/display/path.md>))**

Loads and registers a font file from the filesystem.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique font identifier. |
| `path` | `Path` | Filesystem path to font file. |

Returns `FontFace`: Registered FontFace instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`register(String, InputStream)` {: #registerstring-inputstream data-toc-label="register(String, InputStream)" }

**register(String, InputStream)**

Reads and registers a font from an input stream.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique font identifier. |
| `input` | `InputStream` | Source stream. |

Returns `FontFace`: Registered FontFace instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`register(String, byte[])` {: #registerstring-byte data-toc-label="register(String, byte[])" }

**register(String, byte[])**

Registers a font from raw byte data.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique font identifier. |
| `data` | `byte[]` | Raw font file bytes. |

Returns `FontFace`: Registered FontFace instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`registerSystem(String, String)` {: #registersystemstring-string data-toc-label="registerSystem(String, String)" }

**registerSystem(String, String)**

Registers an operating system font family.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique font identifier. |
| `family` | `String` | System font family name. |

Returns `FontFace`: Registered FontFace instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`find(String)` {: #findstring data-toc-label="find(String)" }

**find(String)**

Looks up a registered font face by identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Font identifier. |

Returns `FontFace`: FontFace instance or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`defaultFace()` {: #defaultface data-toc-label="defaultFace()" }

**defaultFace()**

Returns the default fallback font face.

Returns `FontFace`: Default FontFace instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`defaultFace(FontFace)` {: #defaultfacefontface data-toc-label="defaultFace(FontFace)" }

**defaultFace(FontFace)**

Sets the default fallback font face for the registry.

| Parameter | Type | Description |
| --- | --- | --- |
| `face` | `FontFace` | FontFace to designate as default. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`faces()` {: #faces data-toc-label="faces()" }

**faces()**

Returns an unmodifiable map of all currently registered font faces.

Returns `Map`: Map of font IDs to FontFace objects..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DefaultFontRegistry::</span>`close()` {: #close data-toc-label="close()" }

**close()**

Clears all registered fonts and resets the default font.

</div>

