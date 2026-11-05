# `ClasspathShaderProvider` (implements `ShaderProvider`)

Standard ShaderProvider resolving shader source files from the Java application classpath.

- **Purpose:** Serves as the default fallback provider searching JAR asset directories for vertex (.vsh) and fragment (.fsh) shaders.

### USE WHEN

- Load shaders packaged directly into mod JAR files in assets directories.
- Instantiate with a custom ClassLoader to isolate resource lookups.

### CRITICAL RULES & GOTCHAS:

- Resolves candidate paths following standard resource conventions (e.g. `assets/<namespace>/shaders/core/<name>.fsh`).

- **Related:** `ShaderProvider`, `ShaderProviders`, `ShaderHandle`

