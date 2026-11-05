---
title: "NotificationView"
slug: "notification-view"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# NotificationView

Виджет карточки уведомления с поддержкой уровней важности, заголовков, текста, кнопок действий и шкалы таймера.

## Обзор

`NotificationView` управляет визуальным стеком временных карточек уведомлений поверх интерфейса приложения.

Ключевые возможности:
- Автоматическое размещение и стек в соответствии с `Placement` (сверху-справа, снизу-справа и др.).
- Анимации появления, автозакрытие по таймеру и плавное скрытие.
- Уровни важности, настраиваемые иконки, заголовки и кнопки действий.
- Отображение на глобальном уровне через `OverlayLayer`.

## Отображение карточки уведомления

```java
NotificationView notifications = new NotificationView();
notifications.toast("Build completed!");
```

## Связанные ссылки

- [OverlayLayer](<overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [Toast](<toast.md>): Краткое всплывающее уведомление (тост) со значками статуса, форматированным текстом и автоматическим скрытием.

## API

<div class="api-member-card" markdown="1">

### `NotificationView()` {: #notificationview data-toc-label="NotificationView()" }

Создает пустое представление NotificationView.

</div>

<div class="api-member-card" markdown="1">

### `NotificationView(String)` {: #notificationviewstring data-toc-label="NotificationView(String)" }

Создает NotificationView с начальным текстовым сообщением тоста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`activeCount()` {: #activecount data-toc-label="activeCount()" }

Возвращает количество активных уведомлений.

Возвращает `int`: Общее количество элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`addToast(Toast)` {: #addtoasttoast data-toc-label="addToast(Toast)" }

Добавляет пользовательский экземпляр [Toast](<toast.md>) в стек.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `toast` | `Toast` | Экземпляр карточки тост-уведомления. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Рассчитывает положение и компонует стек уведомлений внутри границ экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `hostBounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`clear()` {: #clear data-toc-label="clear()" }

Удаляет все активные уведомления из представления.

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`duration()` {: #duration data-toc-label="duration()" }

Возвращает длительность отображения в секундах для новых уведомлений.

Возвращает `float`: Длительность отображения в секундах..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`duration(float)` {: #durationfloat data-toc-label="duration(float)" }

Устанавливает длительность отображения в секундах для новых уведомлений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `seconds` | `float` | Длительность показа в секундах до автоматического скрытия. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`hide()` {: #hide data-toc-label="hide()" }

Скрывает представление уведомлений.

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`margin()` {: #margin data-toc-label="margin()" }

Возвращает отступ стека уведомлений от границ экрана.

Возвращает `float`: Отступ в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

Устанавливает отступ стека уведомлений от границ экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `margin` | `float` | Отступ от границ экрана. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`maxVisible()` {: #maxvisible data-toc-label="maxVisible()" }

Возвращает максимальное количество одновременно видимых уведомлений.

Возвращает `int`: Максимальное количество видимых элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`maxVisible(int)` {: #maxvisibleint data-toc-label="maxVisible(int)" }

Устанавливает максимальное количество одновременно видимых уведомлений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `maxVisible` | `int` | Максимальное количество одновременно видимых уведомлений. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`notifications()` {: #notifications data-toc-label="notifications()" }

Возвращает список активных тост-уведомлений.

Возвращает `List`: Список вложенных элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`notify(String)` {: #notifystring data-toc-label="notify(String)" }

Синоним для помещения нового текстового тоста в стек уведомлений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`opened()` {: #opened data-toc-label="opened()" }

Возвращает true, если в представлении есть хотя бы одно активное уведомление.

Возвращает `boolean`: True, если виджет в данный момент открыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`placement()` {: #placement data-toc-label="placement()" }

Возвращает угол экрана для размещения стека уведомлений.

Возвращает `Placement`: Текущее положение размещения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`placement(Placement)` {: #placementplacement data-toc-label="placement(Placement)" }

Устанавливает угол экрана для размещения стека уведомлений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `placement` | `Placement` | Угол экрана для размещения стека уведомлений. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`show()` {: #show data-toc-label="show()" }

Показывает представление уведомлений.

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`spacing()` {: #spacing data-toc-label="spacing()" }

Возвращает расстояние по вертикали между соседними уведомлениями.

Возвращает `float`: Расстояние в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

Устанавливает расстояние по вертикали между соседними уведомлениями.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spacing` | `float` | Расстояние между соседними уведомлениями в стеке. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`text()` {: #text data-toc-label="text()" }

Возвращает текст последнего уведомления.

Возвращает `String`: Отображаемая текстовая строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Ставит в очередь новое уведомление с заданным текстом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NotificationView::</span>`toast(String)` {: #toaststring data-toc-label="toast(String)" }

Помещает новый текстовый тост в стек уведомлений.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `NotificationView`: Текущий экземпляр виджета для цепочки вызовов..

</div>

