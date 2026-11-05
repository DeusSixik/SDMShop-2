---
title: "ToggleSwitch"
slug: "toggle-switch"
category: "Widgets/Interaction"
locale: "ru"
order: 70
---

# ToggleSwitch

Скользящий переключатель с плавной анимацией бегунка и опциональной подписью.

## Обзор

`ToggleSwitch` представляет современный переключатель-тумблер. При нажатии круглый бегунок плавно перемещается по закруглённому треку.

## Тумблер в настройках

```java
ToggleSwitch darkTheme = new ToggleSwitch("Тёмная тема")
        .checked(true);
darkTheme.onCheckedChanged(e -> applyTheme(e.newValue()));
```

## Связанные ссылки

- [Checkbox](<checkbox.md>)
- [ToggleButton](<toggle-button.md>)

