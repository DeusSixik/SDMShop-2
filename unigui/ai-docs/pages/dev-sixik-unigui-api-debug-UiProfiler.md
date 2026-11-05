# `UiProfiler`

Frame profiler abstraction exposed by `UIContext`.

- **Purpose:** Creates named, closeable timing scopes without coupling UI code to the built-in profiler implementation.

### USE WHEN

- Instrument custom widget, layout, or rendering work that should appear in profiler snapshots.

### CRITICAL RULES & GOTCHAS:

- Always close the `ProfileScope`, preferably with try-with-resources, or the timing sample remains incomplete.
- Call `beginFrame` once at the frame boundary before opening scopes when implementing a profiler.
- `NOOP` is safe and intentionally produces empty snapshots.

- **Related:** `ProfileScope`, `ProfilerSnapshot`, `UIContext`

### INSTRUMENTATION

- `scope(String)` → ProfileScope — Opens a named timing scope that must be closed after the measured work.

### READING

- `snapshot()` → ProfilerSnapshot — Returns the latest immutable profiler snapshot.

### EXAMPLES

#### Time custom work

```java
try (ProfileScope ignored = context.profiler().scope("custom-layout")) {
    performLayout();
}
```

