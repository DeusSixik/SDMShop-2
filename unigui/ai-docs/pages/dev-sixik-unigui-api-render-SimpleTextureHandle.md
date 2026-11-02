# `SimpleTextureHandle` (implements `TextureHandle`)

Standard immutable implementation of TextureHandle for tests, dynamic texture providers, and backend adapters.

- **Purpose:** Provides a concrete, unmodifiable TextureHandle instance encapsulating id, dimensions, optional native handle, and TextureOptions.

### USE WHEN

- Instantiate TextureHandle instances in platform backend adapters or test harnesses.
- Wrap custom dynamic procedural textures with known pixel dimensions.
- Derive texture variations with altered sampling using `withOptions(TextureOptions)`.

### CRITICAL RULES & GOTCHAS:

- Dimensions must be non-negative integers.
- `withOptions(TextureOptions)` returns a new instance preserving id and nativeHandle.

- **Related:** `TextureHandle`, `TextureOptions`, `RenderBackend`

### EXAMPLES

#### Create simple texture handle

```java
TextureHandle handle = new SimpleTextureHandle("mod:icon", 16, 16, nativeTex, TextureOptions.defaults());
```

