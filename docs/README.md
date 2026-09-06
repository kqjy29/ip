# Sylveon User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Sorting tasks

Sort all tasks alphabetically:

`sort`

Sort only deadlines by date, with the earliest deadline first:

`sort deadline`

Sort only events by their starting date, with the earliest event first:

`sort event`

Sort only todos alphabetically by description:

`sort todo`

The sorted command displays only the selected task type for `sort deadline`,
`sort event`, and `sort todo`. The displayed task numbers can be used with
`mark`, `unmark`, or `delete`. Sorting is saved automatically.

Dates for deadlines and events must use the `yyyy-mm-dd` format.

Example:

```text
event project meeting /from 2026-09-15 /to 2026-09-15
sort event
```

Expected output:

```text
Sorted events by date (earliest first):
   1. [E][ ] project meeting (from: Sept 15 2026 to: Sept 15 2026)
```
