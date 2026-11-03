---
title: "ValueEditor"
slug: "value-editor"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# ValueEditor

Typed default-value editor contract for a node input, including UI, value access, and serialization.

## Overview

Implement `ValueEditor` when a node input needs a custom editor or value type. The editor owns its widget and exposes the runtime value independently of that widget. Its serialized form must round-trip through `serialize()` and `deserialize()` so snapshots, copy/paste, and restoration preserve input defaults.

