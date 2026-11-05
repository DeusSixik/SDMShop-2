---
title: "DockPolicy"
slug: "dock-policy"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockPolicy

Политика интеграции докинга для окон: FULL (полноценный докинг и прием сброса), GUEST_ONLY (может стыковаться, но не принимает сброс) или DISABLED.

## Обзор

`DockPolicy` задает политику интеграции докинга для оконных хостов.

| Политика | Стыковка в другие окна | Прием внешних панелей |
| --- | --- | --- |
| `FULL` | Да | Да |
| `GUEST_ONLY` | Да | Нет |
| `DISABLED` | Нет | Нет |

## Связанные ссылки

- [DockingRoot](<../../../widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPolicy::</span>`acceptsDrops()` {: #acceptsdrops data-toc-label="acceptsDrops()" }

Проверяет, принимает ли окно сброс внешних перемещаемых панелей.

Возвращает `boolean`: True, если это окно принимает сбрасываемые в него панели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPolicy::</span>`canBeDocked()` {: #canbedocked data-toc-label="canBeDocked()" }

Проверяет, может ли окно быть пристыковано в родительскую систему докинга.

Возвращает `boolean`: True, если это окно может быть пристыковано к хосту..

</div>

