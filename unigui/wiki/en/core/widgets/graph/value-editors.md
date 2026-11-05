---
title: "ValueEditors"
slug: "value-editors"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# ValueEditors

Factory of ready-made value editors for common node input types.

## Overview

Use `ValueEditors` to create text, number, integer, boolean, color, choice, and vector defaults for `NodeBuilder` input rows. Each editor exposes a typed runtime value and a stable serialized representation; vectors use `double[]` and colors use ARGB integers.

