---
title: "OverlayHostAware"
slug: "overlay-host-aware"
category: "Core/Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# OverlayHostAware

Интерфейс для виджетов, динамически рассчитывающих свое положение и компоновку в границах оверлея.

## Обзор

`OverlayHostAware` — интерфейс для плавающих виджетов (подсказок, контекстных меню, всплывающих окон), позволяющий динамически адаптировать свое положение под границы оверлея.

Основной метод:
- `arrangeInHost(RectView hostBounds)`: Пересчитывает координаты и размеры для предотвращения выхода виджета за пределы экрана.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayHostAware::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Позиционирует и компонует этот виджет внутри границ родительского оверлея.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `hostBounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

