---
title: "ToggleButton"
slug: "toggle-button"
category: "Widgets/Interaction"
locale: "ru"
order: 50
---

# ToggleButton

Двухпозиционная кнопка, сохраняющая состояние выбора при нажатии.

## Обзор

`ToggleButton` расширяет `Button`, добавляя булево состояние `checked()`. При клике состояние переключается и генерируется `CheckedChangedEvent`.

## Переключение режима

```java
ToggleButton gridToggle = new ToggleButton("Сетка")
        .checked(true);
gridToggle.onCheckedChanged(e -> editor.setSnap(e.newValue()));
```

## Связанные ссылки

- [Button](<button.md>)
- [ToggleSwitch](<toggle-switch.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToggleButton::</span>`onCheckedChanged(EventListener)` {: #oncheckedchangedeventlistener data-toc-label="onCheckedChanged(EventListener)" }

**Слушатель смены выбора**

Регистрирует коллбэк на изменение состояния переключателя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

