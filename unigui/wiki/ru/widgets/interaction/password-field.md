---
title: "PasswordField"
slug: "password-field"
category: "Widgets/Interaction"
locale: "ru"
order: 140
---

# PasswordField

Защищённое поле ввода пароля, маскирующее символы точками или указанным глифом.

## Обзор

`PasswordField` расширяет `TextInput`, отображая символ маски (например, `*` или `•`) вместо исходного текста. Реальный пароль доступен через метод `text()`.

## Ввод пароля

```java
PasswordField password = new PasswordField()
        .placeholder("Пароль")
        .mask('*');
```

## Связанные ссылки

- [TextInput](<text-input.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PasswordField::</span>`mask(char)` {: #maskchar data-toc-label="mask(char)" }

**Задать символ маски**

Задаёт символ, используемый для сокрытия вводимых знаков.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `mask` | `char` | символ маски |

Возвращает `PasswordField`: этот виджет для fluent-настройки.

</div>

