# JWeaver

![Build And Test](https://github.com/robinloom/jweaver/actions/workflows/build.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.robinloom/jweaver)
[![codecov](https://codecov.io/gh/robinloom/jweaver/graph/badge.svg)](https://codecov.io/gh/robinloom/jweaver)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

---

Stop writing Java `toString()` methods.

JWeaver is a zero-configuration object renderer for debugging, logging and testing.

Instead of manually maintaining `toString()`, configuring serializers, or fighting unreadable logs:

```java
System.out.println(JWeaver.weave(order));
```

Get readable output immediately.

## Example

Given this objects:

```java
class Order {
    Customer customer;
    List<Item> items;
    Payment payment;
}

class Customer {
    String name;
    Address address;
}
```

No `toString()` anywhere. `System.out.println(order)` gives you:

```text
Order@6b95977
```

`JWeaver.weave(order)` renders a compact, single-line view with quoted strings and element counts:

```text
Order[customer=Customer[name="John", address=Address[street="123 Main St", city="Anytown"]], items=List[1][Item[name="Widget"]], payment=Payment.CASH]
```

For a hierarchical view, use `Mode.TREE`:

```java
System.out.println(JWeaver.weave(order, Mode.TREE));
```

```text
Order
|-- customer=Customer
|   |-- name="John"
|   `-- address=Address
|       |-- street="123 Main St"
|       `-- city="Anytown"
|-- items=List[1]
|   `-- [0] Item
|       `-- name="Widget"
`-- payment=Payment.CASH
```

No annotations.

No configuration.

No handwritten `toString()`.

## Why JWeaver?

✓ Zero configuration

✓ Cycle-safe object traversal

✓ Human-readable object trees

✓ Logging friendly

✓ Works with existing objects immediately

✓ Replace boilerplate `toString()` methods

## Installation

Maven:

```xml
<dependency>
    <groupId>com.robinloom</groupId>
    <artifactId>jweaver-api</artifactId>
    <version>4.1</version>
</dependency>
```

For the [SLF4J logging integration](#logging), add:

```xml
<dependency>
    <groupId>com.robinloom</groupId>
    <artifactId>jweaver-logging</artifactId>
    <version>4.1</version>
</dependency>
```

## Quick Start

```java
User user = createUser();

String output = JWeaver.weave(user);

System.out.println(output);
```

## Logging

You can call `JWeaver.weave(...)` yourself:

```java
LOGGER.info("Authenticated user: {}", JWeaver.weave(user));
```

Or use the SLF4J integration from `jweaver-logging`. Its `LoggerFactory` returns a regular SLF4J `Logger` that renders object arguments with JWeaver automatically:

```java
import com.robinloom.jweaver.logging.LoggerFactory;
import org.slf4j.Logger;

private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

LOGGER.info("Authenticated user: {}", user);
```

- Strings and exceptions are passed through unchanged, everything else is woven.
- Arguments are only rendered if the log level is enabled.
- The default is `Mode.INLINE`. Pass a mode for other output: `LoggerFactory.getLogger(UserService.class, Mode.TREE)`.
- It wraps whatever SLF4J backend you already use (Logback, Log4j 2, ...).

## Sensitive data

Fields that look sensitive (such as `password` or `token`) are masked automatically. For guaranteed masking, annotate a field with `@WeaveRedact`:

```java
class Credentials {
    String username = "robin";
    @WeaveRedact String note = "hello";
    String password = "hunter2";
}
```

```text
Credentials[username="robin", note=***, password=***]
```

Name-based detection is a best-effort heuristic. Use `@WeaveRedact` for anything that must never appear in output.

## How is this different?

| | Handwritten / IDE-generated `toString()` | Lombok `@ToString` | JWeaver |
| --- | --- | --- | --- |
| Maintenance | Update by hand when fields change | Annotation on every class | None |
| Works on objects you don't own | No | No | Yes |
| Cycle-safe | Only if you handle it | Needs `@ToString.Exclude` | Built in |
| Sensitive data masking | Manual | Manual (`@ToString.Exclude`) | Automatic + `@WeaveRedact` |

## Features

- Object tree rendering
- Reflection-based traversal
- Cycle detection
- Collection rendering
- Configurable rendering strategies
- Sensitive data redaction
- Custom renderers
- Logging integration

## When should I use JWeaver?

Use JWeaver when:

- debugging complex objects
- replacing handwritten `toString()`
- improving logs
- inspecting DTOs
- writing diagnostics
- creating readable test output

Do not use JWeaver when:

- you need serialization
- you need machine-readable output
- you need persistence formats

## Architecture

JWeaver is modular internally to separate rendering, reflection, logging and extension APIs while keeping usage simple.