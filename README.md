# JWeaver

![Build And Test](https://github.com/robinloom/jweaver/actions/workflows/build.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.robinloom/jweaver)
---

Stop writing Java `toString()` methods.

JWeaver is a zero-configuration object renderer for debugging, logging and testing.

Instead of manually maintaining `toString()`, configuring serializers, or fighting unreadable logs:

```java
System.out.println(JWeaver.weave(order));
```

Get readable output immediately.

## Example

Given this object:

```java
record Order(Customer customer, 
             List<Item> items, 
             Payment payment) {}

record Customer(String name, 
                Address address) {}
```

JWeaver renders:

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
    <artifactId>jweaver</artifactId>
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

```java
LOGGER.info("Authenticated user:\n{}", JWeaver.weave(user));
```

Readable logs without custom formatting.

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