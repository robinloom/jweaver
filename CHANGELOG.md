# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

---

## [4.0] - 2026-04-21

### Added
- Dictionary feature – providing curated `toString()` implementations for common JDK types

### Changed
- Modularized the project structure
  - `jweaver-api` serves as the main API
  - `jweaver-core` contains the implementation of the core API
  - `jweaver-dictionary` contains the dictionary feature
  - `jweaver-reflections` contains the AST construction and rendering
- Reworked construction of the traversable abstract syntax tree (AST)
- All rendering modes now operate on the AST instead of direct reflection

### Removed
- `CARD` and `BULLET` modes

## [3.0] - 2026-01-27

### Added
- Redesign of the API
  - introduced `Mode` - a simple abstraction for each output format
- Support for Java 25
- Automatic redaction of sensitive fields

### Changed
- Renamed `FlatWeaver` to `LinearWeaver`

### Removed
- Configuration options per output format in favor of a zero-decision approach

## [2.0] - 2026-01-10

### Added
- Config to order printed fields alphabetically
- Annotation-based configuration
  - `@WeaveRedact` for redacting fields
  - `@WeaveIgnore` for ignoring fields
  - `@WeaveName` for renaming fields
- New `CardWeaver`

### Changed
- Renamed `DynamicWeaver` to `FlatWeaver`
- Simplified API
  - main entry point with default `Weaver` behind it
  - other weavers are hidden behind a `Internal` static class

## [1.1.0] - 2025-03-01

### Added
- Logging integration with SLF4J via `jweaver-logging`

## [1.0.0] - 2025-02-18

### Added
-  Basic functionality with several output formats
   - `DynamicWeaver` for record-like output
   - `TreeWeaver` and `BulletWeaver` for structured multi-line output
- Several configuration options for each Weaver