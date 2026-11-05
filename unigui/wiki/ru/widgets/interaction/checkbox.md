---
title: "Checkbox"
slug: "checkbox"
category: "Widgets/Interaction"
locale: "ru"
order: 80
---

# Checkbox

Квадратный флажок с поддержкой двухпозиционного и трёхпозиционного (неопределённого) режимов.

## Обзор

`Checkbox` отображает квадратный флажок с анимированной галочкой/прочерком и сопровождающей текстовой подписью. В трёхпозиционном режиме переключается между CHECKED, UNCHECKED и INDETERMINATE.

## Флажок согласия

```java
Checkbox agreeBox = new Checkbox("Я принимаю условия соглашения")
        .checked(false);
agreeBox.onStateChanged(e -> submitBtn.enabled(e.newState() == CheckboxState.CHECKED));
```

## Связанные ссылки

- [RadioButton](<radio-button.md>)
- [ToggleSwitch](<toggle-switch.md>)

