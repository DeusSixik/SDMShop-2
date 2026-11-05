---
title: "NodeLayoutContext"
slug: "node-layout-context"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeLayoutContext

Сервисы раскладки модуля для измерения и размещения виджетов, а также привязки портов.

## Обзор

`NodeLayoutContext` передаётся в callbacks измерения и размещения `NodeModule`. Координаты заданы в world-единицах относительно начала узла и не зависят от зума области просмотра. Используйте `preferredWidth` и `preferredHeight` для измерения виджетов, `place` для их раскладки, `anchor` для размещения портов.

