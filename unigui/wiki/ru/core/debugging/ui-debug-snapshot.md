---
title: "UiDebugSnapshot"
slug: "ui-debug-snapshot"
category: "Core/Debugging"
locale: "ru"
order: 20
---

# UiDebugSnapshot

Неизменяемый снимок времени кадра, объёма рендера и работы кэша текстур.

## Обзор

Получите `UiDebugSnapshot` через `UIContext#debugCounters().snapshot()`. Снимок относится к одному кадру и содержит нормализованные времена кадра, число команд и batch-ей, а также активность кэша текстур. GPU-время может быть равно `-1`, если активный backend не умеет его измерять; остальные времена и счётчики неотрицательны.

## Чтение метрик кадра

```java
UiDebugSnapshot snapshot = context.debugCounters().snapshot();
if (snapshot.frameGpuMillis() >= 0.0f) {
    logGpuTime(snapshot.frameGpuMillis());
}
logRenderLoad(snapshot.drawCommandCount(), snapshot.batchCount());
```

## API

### Кадр

| Метод | Описание |
| --- | --- |
| [`frameIndex()`](#frameindex) | Возвращает индекс кадра, представленного снимком. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`frameIndex()` {: #frameindex data-toc-label="frameIndex()" }

**frameIndex()**

Возвращает индекс кадра, представленного снимком.

Возвращает `long`: Индекс снятого кадра.

</div>

### Время кадра

| Метод | Описание |
| --- | --- |
| [`framesPerSecond()`](#framespersecond) | Возвращает измеренное число кадров в секунду или ноль при отсутствии данных. |
| [`frameTotalMillis()`](#frametotalmillis) | Возвращает полное время кадра по настенным часам. |
| [`frameGpuMillis()`](#framegpumillis) | Возвращает GPU-время кадра, если backend его предоставляет. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`framesPerSecond()` {: #framespersecond data-toc-label="framesPerSecond()" }

**framesPerSecond()**

Возвращает измеренное число кадров в секунду или ноль при отсутствии данных.

Возвращает `float`: Неотрицательное число кадров в секунду.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`frameTotalMillis()` {: #frametotalmillis data-toc-label="frameTotalMillis()" }

**frameTotalMillis()**

Возвращает полное время кадра по настенным часам.

Возвращает `float`: Неотрицательная длительность в миллисекундах.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`frameGpuMillis()` {: #framegpumillis data-toc-label="frameGpuMillis()" }

**frameGpuMillis()**

Возвращает GPU-время кадра, если backend его предоставляет.

Возвращает `float`: GPU-время в миллисекундах или -1 при отсутствии данных.

</div>

### Работа рендера

| Метод | Описание |
| --- | --- |
| [`drawCommandCount()`](#drawcommandcount) | Возвращает число команд draw list, записанных за кадр. |
| [`batchCount()`](#batchcount) | Возвращает число backend batch-ей, записанных за кадр. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`drawCommandCount()` {: #drawcommandcount data-toc-label="drawCommandCount()" }

**drawCommandCount()**

Возвращает число команд draw list, записанных за кадр.

Возвращает `int`: Неотрицательное число draw-команд.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`batchCount()` {: #batchcount data-toc-label="batchCount()" }

**batchCount()**

Возвращает число backend batch-ей, записанных за кадр.

Возвращает `int`: Неотрицательное число batch-ей.

</div>

### Кэш текстур

| Метод | Описание |
| --- | --- |
| [`lastTextureCacheMissReason()`](#lasttexturecachemissreason) | Возвращает последнюю нормализованную причину промаха кэша текстур. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`lastTextureCacheMissReason()` {: #lasttexturecachemissreason data-toc-label="lastTextureCacheMissReason()" }

**lastTextureCacheMissReason()**

Возвращает последнюю нормализованную причину промаха кэша текстур.

Возвращает `String`: Причина промаха или NONE, если причина отсутствует.

</div>

