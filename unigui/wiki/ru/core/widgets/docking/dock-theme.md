---
title: "DockTheme"
slug: "dock-theme"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockTheme

Тема оформления системы докинга, настраивающая палитру цветов ([DockColor](<dock-color.md>)), геометрию вкладок, разделители, тени и ручки изменения размера окон.

## Обзор

`DockTheme` управляет всеми визуальными свойствами подсистемы докинга.

### Готовые темы

| Фабрика | Палитра и стиль |
| --- | --- |
| `DockTheme.defaultTheme()` | Стандартная темная тема IDE. |
| `DockTheme.editor()` | Контрастная тема для редакторов кода. |
| `DockTheme.classic()` | Светлая классическая тема. |

### Настройки метрик

Позволяет изменять высоту вкладок (`tabHeight`), скругление углов (`tabRadius`), толщину разделителей (`splitThickness`), тени окон и стиль ручек изменения размера.

## Настройка цветов и метрик темы докинга

```java
DockTheme theme = DockTheme.editor().copy()
        .tabHeight(24.0f)
        .splitThickness(6.0f);
```

## Связанные ссылки

- [DockColor](<dock-color.md>): Семантические роли цветов темы DockTheme для стилизации вкладок, разделителей, обрамления плавающих окон и зон предпросмотра сброса.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`accentThickness()` {: #accentthickness data-toc-label="accentThickness()" }

Возвращает толщину индикатора активной вкладки в пикселях.

Возвращает `float`: Толщина индикатора активной вкладки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`accentThickness(float)` {: #accentthicknessfloat data-toc-label="accentThickness(float)" }

Задает толщину индикатора активной вкладки в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `accentThickness` | `float` | Толщина акцентной полосы активной вкладки. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`classic()` {: #classic data-toc-label="classic()" }

Создает светлую классическую тему оформления докинга.

Возвращает `DockTheme`: Новый или измененный экземпляр DockTheme..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`closeGap()` {: #closegap data-toc-label="closeGap()" }

Возвращает зазор перед кнопкой закрытия вкладки.

Возвращает `float`: Зазор между заголовком и кнопкой закрытия в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`closeSize()` {: #closesize data-toc-label="closeSize()" }

Возвращает размер кнопки закрытия вкладки в пикселях.

Возвращает `float`: Размер кнопки закрытия в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`closeSize(float)` {: #closesizefloat data-toc-label="closeSize(float)" }

Задает размер кнопки закрытия вкладки в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closeSize` | `float` | Размер кнопки закрытия в заголовках вкладок. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`color(DockColor)` {: #colordockcolor data-toc-label="color(DockColor)" }

Возвращает цвет ColorView, назначенный для семантической роли [DockColor](<dock-color.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `role` | `DockColor` | Семантическая роль цвета. |

Возвращает `MutableColor`: Экземпляр MutableColorView для модификации..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`color(DockColor, ColorView)` {: #colordockcolor-colorview data-toc-label="color(DockColor, ColorView)" }

Назначает цвет для семантической роли [DockColor](<dock-color.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `role` | `DockColor` | Семантическая роль цвета. |
| `color` | `ColorView` | Назначаемый цвет ColorView. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`copy()` {: #copy data-toc-label="copy()" }

Создает независимую копию данной темы оформления для модификации.

Возвращает `DockTheme`: Новый или измененный экземпляр DockTheme..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`defaultTheme()` {: #defaulttheme data-toc-label="defaultTheme()" }

Возвращает глобальную тему оформления докинга по умолчанию.

Возвращает `DockTheme`: Новый или измененный экземпляр DockTheme..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`defaultTheme(DockTheme)` {: #defaultthemedocktheme data-toc-label="defaultTheme(DockTheme)" }

Заменяет глобальную тему оформления докинга по умолчанию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `theme` | `DockTheme` | Исходная тема для копирования. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`editor()` {: #editor data-toc-label="editor()" }

Создает контрастную темную тему для редакторов кода и инструментов.

Возвращает `DockTheme`: Новый или измененный экземпляр DockTheme..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`gripSize()` {: #gripsize data-toc-label="gripSize()" }

Возвращает размер ручки изменения размера окна в пикселях.

Возвращает `float`: Размер ручки изменения размера в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`gripSize(float)` {: #gripsizefloat data-toc-label="gripSize(float)" }

Задает размер ручки изменения размера окна в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `gripSize` | `float` | Размер ручки изменения размера окна. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`iconGap()` {: #icongap data-toc-label="iconGap()" }

Возвращает зазор между иконкой и текстом заголовка во вкладке.

Возвращает `float`: Зазор между иконкой и текстом в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`iconSize()` {: #iconsize data-toc-label="iconSize()" }

Возвращает размер иконок во вкладках в пикселях.

Возвращает `float`: Размер иконки в заголовке вкладки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`iconSize(float)` {: #iconsizefloat data-toc-label="iconSize(float)" }

Задает размер иконок во вкладках в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `iconSize` | `float` | Размер иконок в заголовках вкладок. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`resizeGrip()` {: #resizegrip data-toc-label="resizeGrip()" }

Возвращает используемый стиль отрисовки ручки изменения размера.

Возвращает `ResizeGripStyle`: Настроенный стиль ручки изменения размера..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`resizeGrip(ResizeGripStyle)` {: #resizegripresizegripstyle data-toc-label="resizeGrip(ResizeGripStyle)" }

Задает стиль отрисовки ручки изменения размера окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `resizeGrip` | `ResizeGripStyle` | Стиль отрисовки ручки изменения размера. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitStyle()` {: #splitstyle data-toc-label="splitStyle()" }

Возвращает визуальный стиль разделителей узлов.

Возвращает `SplitHandleStyle`: Визуальный стиль разделителей..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitStyle(SplitHandleStyle)` {: #splitstylesplithandlestyle data-toc-label="splitStyle(SplitHandleStyle)" }

Задает визуальный стиль разделителей узлов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `splitStyle` | `SplitHandleStyle` | Визуальный стиль разделителей. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitThickness()` {: #splitthickness data-toc-label="splitThickness()" }

Возвращает толщину разделителей узлов докинга в пикселях.

Возвращает `float`: Толщина разделителя в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`splitThickness(float)` {: #splitthicknessfloat data-toc-label="splitThickness(float)" }

Задает толщину разделителей узлов докинга в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `splitThickness` | `float` | Толщина разделителей между узлами докинга. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabHeight()` {: #tabheight data-toc-label="tabHeight()" }

Возвращает высоту полосы вкладок в пикселях.

Возвращает `float`: Высота панели вкладок в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabHeight(float)` {: #tabheightfloat data-toc-label="tabHeight(float)" }

Задает высоту полосы вкладок в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tabHeight` | `float` | Высота заголовков вкладок докинга в пикселях. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMaxWidth()` {: #tabmaxwidth data-toc-label="tabMaxWidth()" }

Возвращает максимальную ширину вкладки в пикселях.

Возвращает `float`: Максимальная ширина вкладки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMaxWidth(float)` {: #tabmaxwidthfloat data-toc-label="tabMaxWidth(float)" }

Задает максимальную ширину вкладки в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tabMaxWidth` | `float` | Максимальная ширина вкладки в пикселях. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMinWidth()` {: #tabminwidth data-toc-label="tabMinWidth()" }

Возвращает минимальную ширину вкладки в пикселях.

Возвращает `float`: Минимальная ширина вкладки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabMinWidth(float)` {: #tabminwidthfloat data-toc-label="tabMinWidth(float)" }

Задает минимальную ширину вкладки в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tabMinWidth` | `float` | Минимальная ширина вкладки в пикселях. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabPaddingX()` {: #tabpaddingx data-toc-label="tabPaddingX()" }

Возвращает горизонтальный отступ содержимого вкладок в пикселях.

Возвращает `float`: Горизонтальный отступ внутри вкладок в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabPaddingX(float)` {: #tabpaddingxfloat data-toc-label="tabPaddingX(float)" }

Задает горизонтальный отступ содержимого вкладок в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tabPaddingX` | `float` | Горизонтальный отступ внутри вкладок докинга. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabRadius()` {: #tabradius data-toc-label="tabRadius()" }

Возвращает радиус скругления углов вкладок в пикселях.

Возвращает `float`: Радиус скругления вкладок в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tabRadius(float)` {: #tabradiusfloat data-toc-label="tabRadius(float)" }

Задает радиус скругления углов вкладок в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tabRadius` | `float` | Радиус скругления вкладок докинга в пикселях. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffHeight()` {: #tearoffheight data-toc-label="tearOffHeight()" }

Возвращает начальную высоту оторванного плавающего окна.

Возвращает `float`: Высота оторванного окна по умолчанию в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffPreviewEnabled()` {: #tearoffpreviewenabled data-toc-label="tearOffPreviewEnabled()" }

Проверяет, включен ли компактный предпросмотр при отрыве вкладки.

Возвращает `boolean`: True, если предпросмотр отрыва включен..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffPreviewSize(float, float)` {: #tearoffpreviewsizefloat-float data-toc-label="tearOffPreviewSize(float, float)" }

Настраивает размеры компактного предпросмотра при отрыве вкладки из докинга.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина по умолчанию для оторванных плавающих окон. |
| `height` | `float` | Высота по умолчанию для оторванных плавающих окон. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`tearOffWidth()` {: #tearoffwidth data-toc-label="tearOffWidth()" }

Возвращает начальную ширину оторванного плавающего окна.

Возвращает `float`: Ширина оторванного окна по умолчанию в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowRadius()` {: #windowradius data-toc-label="windowRadius()" }

Возвращает радиус скругления углов плавающих окон в пикселях.

Возвращает `float`: Радиус скругления углов плавающих окон в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowRadius(float)` {: #windowradiusfloat data-toc-label="windowRadius(float)" }

Задает радиус скругления углов плавающих окон в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `windowRadius` | `float` | Радиус скругления углов плавающих окон. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowShadowSize()` {: #windowshadowsize data-toc-label="windowShadowSize()" }

Возвращает размер тени плавающих окон в пикселях.

Возвращает `float`: Размер тени плавающих окон в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockTheme::</span>`windowShadowSize(float)` {: #windowshadowsizefloat data-toc-label="windowShadowSize(float)" }

Задает размер тени плавающих окон в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `windowShadowSize` | `float` | Размер тени для плавающих окон. |

Возвращает `DockTheme`: Данный экземпляр темы для цепочки вызовов..

</div>

