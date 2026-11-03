---
title: "UiRenderPolicy"
slug: "ui-render-policy"
category: "Core/Screens"
locale: "ru"
order: 15
---

# UiRenderPolicy

Политика частоты обновления и рендера retained UI, управляющая пересборкой кадра по таймингу или сигналам инвалидации.

## Обзор

`UiRenderPolicy` определяет, перерисовывает ли runtime интерфейс каждый игровой кадр, только при изменении состояния виджетов (dirty signal), с фиксированным FPS или с привязкой к VSYNC дисплея. Это позволяет существенно снизить нагрузку на CPU и GPU на статичных экранах.

## Режимы рендера

- `CONTINUOUS`: Полная перестройка и рендер каждый кадр. Идеально для плавных анимаций (60+ FPS), шейдерных эффектов и вьюпортов редактора.
- `ON_DIRTY`: Рендер выполняется только при явном сигнале инвалидации (dirty). Сводит к нулю расход ресурсов в состоянии покоя.
- `FIXED_FPS`: Ограничивает частоту рендера заданным значением FPS (например, 30 FPS).
- `VSYNC`: Синхронизирует интервалы обновления с частотой развёртки экрана.

## Производительность и энергоэффективность

Статичные экраны с глубоким деревом виджетов создают избыточную нагрузку при ежекадровом рендере. Переключение на `onDirty()` полностью исключает лишнюю генерацию draw-команд, запуская перерисовку только в ответ на ввод пользователя или события данных.

## Настройка рендера по событию для статичных меню

```java
UiRenderPolicy policy = UiRenderPolicy.onDirty();
window.renderPolicy(policy);
```

## Включение VSYNC для плавной анимации

```java
UiRenderPolicy policy = UiRenderPolicy.vsync();
window.renderPolicy(policy);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`continuous()` {: #continuous data-toc-label="continuous()" }

**continuous()**

Возвращает политику непрерывного ежекадрового рендера.

Возвращает `UiRenderPolicy`: Экземпляр непрерывной политики рендера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`mode()` {: #mode data-toc-label="mode()" }

**mode()**

Возвращает текущий режим политики рендера.

Возвращает `Mode`: Режим политики..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`onDirty()` {: #ondirty data-toc-label="onDirty()" }

**onDirty()**

Возвращает политику рендера только по сигналу инвалидации (dirty).

Возвращает `UiRenderPolicy`: Экземпляр on-dirty политики рендера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`fixedFps(float)` {: #fixedfpsfloat data-toc-label="fixedFps(float)" }

**fixedFps(float)**

Создаёт политику с фиксированной целевой частотой кадров.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `fps` | `float` | Целевая частота кадров; должна быть конечным положительным числом. |

Возвращает `UiRenderPolicy`: Экземпляр политики фиксированного FPS..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`vsync()` {: #vsync data-toc-label="vsync()" }

**vsync()**

Возвращает политику, синхронизированную с частотой обновления экрана (VSYNC).

Возвращает `UiRenderPolicy`: Экземпляр VSYNC-политики..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`fps()` {: #fps data-toc-label="fps()" }

**fps()**

Возвращает целевой FPS для режима FIXED_FPS или 0.0 для остальных режимов.

Возвращает `float`: Значение целевого FPS..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiRenderPolicy::</span>`intervalNanos()` {: #intervalnanos data-toc-label="intervalNanos()" }

**intervalNanos()**

Возвращает минимальный интервал между кадрами в наносекундах для режима FIXED_FPS.

Возвращает `long`: Интервал в наносекундах или 0, если режим не FIXED_FPS..

</div>

