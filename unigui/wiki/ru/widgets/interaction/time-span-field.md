---
title: "TimeSpanField"
slug: "time-span-field"
category: "Widgets/Interaction"
locale: "ru"
order: 160
---

# TimeSpanField

Поле ввода временных интервалов, форматирующее значения вида 1h 30m или 45s в java.time.Duration.

## Обзор

`TimeSpanField` обеспечивает удобный ввод интервалов времени в понятном формате (`2h 15m`, `30s`, `1d`), парся их напрямую в `java.time.Duration`.

## Ввод длительности кулдауна

```java
TimeSpanField cooldown = new TimeSpanField()
        .value(Duration.ofMinutes(5));
```

## Связанные ссылки

- [TextField](<text-field.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TimeSpanField::</span>`value(Duration)` {: #valueduration data-toc-label="value(Duration)" }

**Задать длительность**

Задаёт длительность в виде объекта java.time.Duration.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `Duration` | экземпляр длительности |

Возвращает `TimeSpanField`: этот виджет для fluent-настройки.

</div>

