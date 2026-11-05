---
title: "NodeContext"
slug: "node-context"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# NodeContext

UI-free execution context for reading node inputs, publishing outputs, and controlling flow.

## Overview

A `NodeContext` is available only while a `NodeBehavior` runs. Read wired values or editor defaults through its typed input helpers, publish results with `output`, and choose subsequent execution with `continueWith` or `runFlow`. Pure behaviors should not retain state between calls.

