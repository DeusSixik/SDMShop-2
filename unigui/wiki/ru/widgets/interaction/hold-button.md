---
title: "HoldButton"
slug: "hold-button"
category: "Widgets/Interaction"
locale: "ru"
order: 20
---

# HoldButton

Кнопка подтверждения, требующая удержания нажатой в течение заданного времени.

## Обзор

`HoldButton` расширяет `Button` для предотвращения случайных нажатий на критические действия (удаление профилей, очистка данных, сброс конфигураций).

Отображает полосу прогресса удержания. Если курсор покидает кнопку или отпускается раньше времени, прогресс сбрасывается.

## Кнопка подтверждения удаления

```java
HoldButton deleteBtn = new HoldButton("Удерживайте для удаления")
        .holdDurationSeconds(1.5f)
        .cancelOnPointerExit(true);
deleteBtn.onHoldCompleted(e -> deleteDatabase());
```

## Связанные ссылки

- [Button](<button.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">HoldButton::</span>`onHoldCompleted(EventListener)` {: #onholdcompletedeventlistener data-toc-label="onHoldCompleted(EventListener)" }

**Слушатель завершения удержания**

Регистрирует коллбэк, вызываемый по истечении требуемого времени удержания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

