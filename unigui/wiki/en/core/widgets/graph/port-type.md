---
title: "PortType"
slug: "port-type"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# PortType

Immutable definition of a graph port's id, display name, color, marker shape, and flow behavior.

## Overview

Create data or execution-flow types and register them with `PortTypeRegistry`. A port type determines the marker and wire appearance as well as the compatibility system used by the graph. Flow types cannot connect to data types; immutable display and shape variants are returned as copies.

