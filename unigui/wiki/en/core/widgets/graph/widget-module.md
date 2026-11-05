---
title: "WidgetModule"
slug: "widget-module"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# WidgetModule

Node module that embeds one arbitrary widget as a full-width row.

## Overview

Use `WidgetModule` to embed a preview, slider, or editor without implementing a custom `NodeModule`. The widget stretches across the node; its preferred height or configured minimum determines row height. [Text](<../../../widgets/display/text.md>) editors use stable dimensions so typing does not resize the node.

