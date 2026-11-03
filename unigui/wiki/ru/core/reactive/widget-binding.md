---
title: "Привязка сигналов"
slug: "widget-binding"
category: "Core/Reactive"
locale: "ru"
order: 70
---

# Привязка сигналов

Связывает свойства виджетов с типизированными сигналами и синхронизирует изменения в обе стороны.

## Модель привязки

Вызовите `widget.bind(key, signal)`, чтобы связать поддерживаемое свойство виджета с сигналом. Привязка двусторонняя: текущее значение сигнала сразу применяется к виджету, последующие изменения сигнала обновляют виджет, а действия пользователя обновляют сигнал. Встроенный ключ `BindingKeys.TEXT` поддерживается `TextInput` и его наследниками, а `BindingKeys.CHECKED` — `ToggleButton` и его наследниками.

Ключи сравниваются по **идентичности объекта**, поэтому собственный ключ нужно объявить один раз и повторно использовать: `static final BindingData<BooleanSignal> MY_CHECKBOX_VALUE = new BindingData<>(BooleanSignal.class);`. Собственный наследник `WidgetBase` обрабатывает ключи в `createBinding(...)` и возвращает `null` для неизвестных. Неподдерживаемые ключи игнорируются; источник неверного типа считается ошибкой. Повторная привязка того же ключа заменяет прежнюю связь, а `WidgetBase.dispose()` закрывает активные привязки.

Callback-и сигнала вызываются синхронно в потоке, где вызван `set()`. Сигналы, связанные с интерфейсом, изменяйте в UI-потоке.

## Общее состояние для нескольких контролов

```java
StringSignal name = new StringSignal("Ada");
TextField editor = new TextField();
TextField mirror = new TextField();
editor.bind(BindingKeys.TEXT, name);
mirror.bind(BindingKeys.TEXT, name);

BooleanSignal enabled = new BooleanSignal(true);
Checkbox checkbox = new Checkbox("Enabled");
ToggleButton toggle = new ToggleButton("Enabled");
checkbox.bind(BindingKeys.CHECKED, enabled);
toggle.bind(BindingKeys.CHECKED, enabled);
```

