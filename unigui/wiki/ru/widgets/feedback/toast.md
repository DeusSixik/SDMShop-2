---
title: "Toast"
slug: "toast"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# Toast

Краткое всплывающее уведомление (тост) со значками статуса, форматированным текстом и автоматическим скрытием.

## Обзор

`Toast` предоставляет кратковременные ненавязчивые всплывающие сообщения, автоматически исчезающие по истечении таймера.

Ключевые возможности:
- Поддержка обычного и форматированного текста сообщений.
- Настраиваемая длительность показа и плавные переходы появления/исчезновения.
- Возможность отображения иконки статуса и кнопок действий.
- Интеграция в стек уведомлений `NotificationView`.

## Показ автоматически исчезающего тост-уведомления

```java
Toast toast = new Toast("Settings saved successfully!");
toast.duration(3.0f).show();
```

## Связанные ссылки

- [NotificationView](<notification-view.md>): Виджет карточки уведомления с поддержкой уровней важности, заголовков, текста, кнопок действий и шкалы таймера.
- [OverlayLayer](<overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.

## API

<div class="api-member-card" markdown="1">

### `Toast()` {: #toast data-toc-label="Toast()" }

Создает пустой экземпляр Toast.

</div>

<div class="api-member-card" markdown="1">

### `Toast(String)` {: #toaststring data-toc-label="Toast(String)" }

Создает Toast с простым текстовым сообщением.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Позиционирует тост-уведомление внутри границ родительского контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `hostBounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`duration()` {: #duration data-toc-label="duration()" }

Возвращает длительность отображения в секундах до автоматического закрытия.

Возвращает `float`: Длительность отображения в секундах..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`duration(float)` {: #durationfloat data-toc-label="duration(float)" }

Устанавливает длительность отображения в секундах до автоматического закрытия.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `seconds` | `float` | Длительность показа в секундах до автоматического скрытия. |

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`hide()` {: #hide data-toc-label="hide()" }

Скрывает тост-уведомление.

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`margin()` {: #margin data-toc-label="margin()" }

Возвращает внешний отступ в пикселях.

Возвращает `float`: Отступ в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

Устанавливает внешний отступ в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `margin` | `float` | Внешний отступ. |

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`opened()` {: #opened data-toc-label="opened()" }

Возвращает true, если тост виден в данный момент.

Возвращает `boolean`: True, если виджет в данный момент открыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Устанавливает предпочитаемые размеры карточки тоста в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Ширина в пикселях. |
| `height` | `float` | Высота в пикселях. |

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`show()` {: #show data-toc-label="show()" }

Показывает тост-уведомление.

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`text()` {: #text data-toc-label="text()" }

Возвращает простое текстовое сообщение.

Возвращает `String`: Отображаемая текстовая строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Устанавливает простое текстовое сообщение.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Toast::</span>`toast(String)` {: #toaststring data-toc-label="toast(String)" }

Обновляет текст сообщения и немедленно отображает тост с анимацией.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `Toast`: Текущий экземпляр виджета для цепочки вызовов..

</div>

