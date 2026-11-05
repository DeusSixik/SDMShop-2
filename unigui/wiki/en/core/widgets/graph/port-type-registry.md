---
title: "PortTypeRegistry"
slug: "port-type-registry"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# PortTypeRegistry

Port type catalog with colors, shapes, and directed data-conversion compatibility rules.

## Overview

Configure a `PortTypeRegistry` before constructing nodes. Register the data and flow types used by ports, then declare allowed directed conversions such as `int` to `float`. Unknown ids resolve to the neutral `any` type; flow connections remain separate from data conversions.

