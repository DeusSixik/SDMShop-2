# `NodeGraphConnectionPolicy`

Strategy interface defining rules and compatibility validation for forming connections between ports.

### API

- `validate(NodeGraph, NodeGraphPortRef, NodeGraphPortRef)` → NodeGraphConnectionValidation — Validates whether a connection can be established between source and target ports.

