# `DatePicker` (extends `LinearBox`)

Date selection control featuring an ISO date text field and an interactive calendar popup.

- **Purpose:** Enables visual calendar picking and ISO string parsing for date values.

### USE WHEN

- Log or event filtering by calendar date.
- Birthday, expiration date, or schedule input forms.

### CRITICAL RULES & GOTCHAS:

- Values are stored as java.time.LocalDate instances.
- Typing valid ISO-8601 strings in the field automatically updates the selected date.

- **Related:** `TextField`, `Popup`, `LinearBox`

