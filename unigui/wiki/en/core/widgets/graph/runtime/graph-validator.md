---
title: "GraphValidator"
slug: "graph-validator"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# GraphValidator

Static checks for missing required inputs, invalid pure-data cycles, and unreachable execution nodes.

## Overview

Use `GraphValidator` to find structural issues before executing a graph. It checks required inputs, data cycles between pure nodes, and unreachable exec nodes, returning `GraphIssue` entries with severity and node/port references. `GraphExecutor.validate` adds checks for registered behaviors.

