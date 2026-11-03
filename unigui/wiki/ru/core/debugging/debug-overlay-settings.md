---
title: "DebugOverlaySettings"
slug: "debug-overlay-settings"
category: "Core/Debugging"
locale: "ru"
order: 10
---

# DebugOverlaySettings

Настройки положения, масштаба и выборки встроенного оверлея производительности.

## Обзор

`DebugOverlaySettings` — изменяемая конфигурация из `UIContext#debugOverlaySettings`. Она задаёт положение встроенного оверлея производительности, его UI-масштаб, число сохранённых точек графиков и расстояние от края viewport. Сеттеры нормализуют небезопасные значения, поэтому настройки можно напрямую менять из редактора или горячими клавишами.

## Отладочные горячие клавиши

В Minecraft-экранах и render layers доступны встроенные сочетания клавиш:

| Сочетание | Действие |
| --- | --- |
| `Ctrl+Shift+P` | Включает или выключает оверлей профайлера. |
| `Ctrl+Shift++`, `Ctrl+Shift+=` или `+` на цифровом блоке | Увеличивает масштаб оверлея на `0.25`. |
| `Ctrl+Shift+-` или `-` на цифровом блоке | Уменьшает масштаб оверлея на `0.25`. |

GLFW сообщает основной `+` как клавишу `=`, поэтому обозначения plus/equal относятся к одному сочетанию. Действие выполняется один раз на физическое нажатие; события auto-repeat поглощаются без повторного изменения настройки. Хоткеи работают со стандартным `DefaultUIContext`. После изменения `DebugOverlaySettings` ограничивает масштаб диапазоном от `0.25` до `4.0` включительно.

## Настройка оверлея

```java
context.debugOverlaySettings()
        .anchor(DebugOverlayAnchor.TOP_RIGHT)
        .scale(1.0f)
        .sampleWindow(240)
        .margin(8.0f);
```

## API

### Настройка

| Метод | Описание |
| --- | --- |
| [`anchor(DebugOverlayAnchor)`](#anchordebugoverlayanchor) | Закрепляет оверлей в углу viewport; null выбирает TOP_LEFT. |
| [`scale(float)`](#scalefloat) | Задаёт UI-масштаб оверлея с ограничением от 0.25 до 4.0 включительно. |
| [`sampleWindow(int)`](#samplewindowint) | Задаёт историю графиков с ограничением от 1 до 10 000 кадров. |
| [`margin(float)`](#marginfloat) | Задаёт неотрицательное расстояние от выбранных краёв viewport в UI-пикселях. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`anchor(DebugOverlayAnchor)` {: #anchordebugoverlayanchor data-toc-label="anchor(DebugOverlayAnchor)" }

**anchor(DebugOverlayAnchor)**

Закрепляет оверлей в углу viewport; null выбирает TOP_LEFT.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `DebugOverlayAnchor` | Угол viewport, служащий началом координат оверлея; может быть null. |

Возвращает `DebugOverlaySettings`: Тот же экземпляр настроек для fluent-конфигурации.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`scale(float)` {: #scalefloat data-toc-label="scale(float)" }

**scale(float)**

Задаёт UI-масштаб оверлея с ограничением от 0.25 до 4.0 включительно.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `scale` | `float` | Запрошенный масштаб; нечисловое или бесконечное значение возвращает стандартные 0.75. |

Возвращает `DebugOverlaySettings`: Тот же экземпляр настроек для fluent-конфигурации.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`sampleWindow(int)` {: #samplewindowint data-toc-label="sampleWindow(int)" }

**sampleWindow(int)**

Задаёт историю графиков с ограничением от 1 до 10 000 кадров.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `sampleWindow` | `int` | Запрошенное число хранимых кадров. |

Возвращает `DebugOverlaySettings`: Тот же экземпляр настроек для fluent-конфигурации.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DebugOverlaySettings::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

**margin(float)**

Задаёт неотрицательное расстояние от выбранных краёв viewport в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `margin` | `float` | Отступ в UI-пикселях; нечисловое или бесконечное значение возвращает 4, отрицательное становится 0. |

Возвращает `DebugOverlaySettings`: Тот же экземпляр настроек для fluent-конфигурации.

</div>

