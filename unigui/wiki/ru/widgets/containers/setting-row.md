---
title: "SettingRow"
slug: "setting-row"
category: "Widgets/Containers"
locale: "ru"
order: 150
---

# SettingRow

Специализированная строка настроек с текстовой подписью слева и интерактивным элементом управления справа.

## Обзор

SettingRow — специализированный подкласс `PanelRowWidget`, созданный специально для диалогов настроек, панелей параметров и форм опций. В левом слоте находится встроенный `Label`, а в правом — интерактивный элемент управления (например, [Slider](<../interaction/slider.md>), [Checkbox](<../interaction/checkbox.md>), Dropdown или [TextField](<../interaction/text-field.md>)).

## Перенаправление фокуса

Ключевая особенность удобства SettingRow — автоматическое перенаправление фокуса: для левого `Label` устанавливается `label.focusTarget(control)`, благодаря чему клик по подписи автоматически передаёт фокус на связанный элемент управления.

## Строка настройки громкости

```java
SettingRow volumeRow = new SettingRow("Master Volume", volumeSlider)
        .controlWidth(120.0f)
        .rowHeight(24.0f);
```

## Связанные ссылки

- [Box](<box.md>)
- [PanelRowWidget](<panel-row-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`label()` {: #label data-toc-label="label()" }

**Получить [Label](<../display/label.md>)**

Возвращает встроенный экземпляр [Label](<../display/label.md>) для расширенной стилизации текста.

Возвращает `Label`: экземпляр [Label](<../display/label.md>) подписи.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`labelText(String)` {: #labeltextstring data-toc-label="labelText(String)" }

**Задать текст подписи**

Задаёт обычный текст для подписи настройки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `labelText` | `String` | строка текста подписи |

Возвращает `SettingRow`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`control(Widget)` {: #controlwidget data-toc-label="control(Widget)" }

**Задать элемент управления**

Устанавливает интерактивный элемент управления на правой стороне.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `control` | `Widget` | элемент управления (например, [Slider](<../interaction/slider.md>), Toggle, [TextField](<../interaction/text-field.md>)) |

Возвращает `SettingRow`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SettingRow::</span>`controlWidth(float)` {: #controlwidthfloat data-toc-label="controlWidth(float)" }

**Ширина элемента управления**

Задаёт фиксированную ширину для правого слота элемента управления.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `controlWidth` | `float` | ширина в пикселях или AUTO |

Возвращает `SettingRow`: этот виджет для fluent-настройки.

</div>

