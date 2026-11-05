---
title: "Carousel"
slug: "carousel"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# Carousel

Контейнер карусели со страницами, поддерживающий горизонтальное или вертикальное перелистывание, анимацию переходов, кнопки навигации и индикаторы.

## Обзор

Контейнер карусели со страницами, поддерживающий горизонтальное или вертикальное перелистывание, анимацию переходов, кнопки навигации и индикаторы. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
Carousel carousel = new Carousel();
carousel.addPage(slide1).addPage(slide2);
```

## Связанные ссылки

- [PageView](<page-view.md>): Контейнер страниц с сохранением состояния, отображающий ровно одну страницу за раз.

## API

<div class="api-member-card" markdown="1">

### `Carousel()` {: #carousel data-toc-label="Carousel()" }

Создает горизонтальный контейнер карусели Carousel.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`addPage(Widget)` {: #addpagewidget data-toc-label="addPage(Widget)" }

Добавляет страницу в карусель.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `page` | `Widget` | Виджет страницы. |

Возвращает `Carousel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`dots()` {: #dots data-toc-label="dots()" }

Возвращает контейнер точек-индикаторов страниц карусели.

Возвращает `DotIndicator`: Контейнер точек-индикаторов страницы..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`header()` {: #header data-toc-label="header()" }

Возвращает контейнер заголовка карусели.

Возвращает `HBox`: Строка заголовка карусели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`indicator()` {: #indicator data-toc-label="indicator()" }

Возвращает виджет текстового индикатора номера страницы карусели.

Возвращает `Label`: Виджет текстового индикатора..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`nextButton()` {: #nextbutton data-toc-label="nextButton()" }

Возвращает кнопку перехода к следующей странице карусели.

Возвращает `Button`: Связанный компонент кнопки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Регистрирует слушателя события изменения выбранной страницы карусели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`pageView()` {: #pageview data-toc-label="pageView()" }

Возвращает базовый контейнер страниц [PageView](<page-view.md>) карусели.

Возвращает `PageView`: Базовый контейнер [PageView](<page-view.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`pages()` {: #pages data-toc-label="pages()" }

Возвращает неизменяемый список страниц карусели.

Возвращает `List`: Неизменяемый список виджетов страниц..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`previousButton()` {: #previousbutton data-toc-label="previousButton()" }

Возвращает кнопку перехода к предыдущей странице карусели.

Возвращает `Button`: Связанный компонент кнопки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`selectRelative(int)` {: #selectrelativeint data-toc-label="selectRelative(int)" }

Смещает текущую выбранную страницу карусели на относительную дельту.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `delta` | `int` | Относительное смещение. |

Возвращает `Carousel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Возвращает индекс текущей выбранной страницы карусели.

Возвращает `int`: Результирующее значение..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

Выбирает страницу карусели по индексу с генерацией события смены страницы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectedIndex` | `int` | Позиция индекса. |

Возвращает `Carousel`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

Выбирает страницу карусели по индексу без генерации событий.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectedIndex` | `int` | Позиция индекса. |

Возвращает `Carousel`: Текущий экземпляр для цепочки вызовов..

</div>

