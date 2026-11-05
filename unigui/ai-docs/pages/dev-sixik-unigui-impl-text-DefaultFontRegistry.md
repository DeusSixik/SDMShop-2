# `DefaultFontRegistry` (implements `FontRegistry`, implements `AutoCloseable`)

Standard font registry loading TrueType and Type1 fonts from streams, files, or platform system fonts without Minecraft dependencies.

- **Purpose:** Provides platform-independent font loading, caching, and fallback resolution for desktop UI environments or server-side toolkits.

### USE WHEN

- Loading custom TrueType (.ttf) or Type1 fonts dynamically at runtime.
- Configuring default font faces and system font aliases without accessing Minecraft game assets.

### CRITICAL RULES & GOTCHAS:

- Font identifiers are normalized to 'font' if null or blank.
- Closing the registry via close() purges all loaded font faces and resets default font state.

- **Related:** `FontFace`, `FontRegistry`, `Fonts`

