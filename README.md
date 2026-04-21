# JWeaver

![Build And Test](https://github.com/robinloom/jweaver/actions/workflows/build.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.robinloom/jweaver)
---

**JWeaver** is a zero-decision Java utility for generating  
**human-readable, structured string representations of objects**.

It is designed as a drop-in alternative to handwritten `toString()` methods,
making objects easy to inspect in logs, debugging sessions, and tests.

---

## Quick Example

```java
Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));

System.out.println(JWeaver.weave(person));
```

Output (INLINE mode):

```
Person[name=John Doe, birthday=1990-01-01]
```

---

## Rendering Modes

JWeaver supports multiple rendering styles depending on the context.

### INLINE (default)

Compact, record-style output ideal for logs.

```
Person[name=John Doe, birthday=1990-01-01]
```

### TREE

Structured representation for nested object graphs.

```
Person
├─ name: John Doe
└─ birthday: 1990-01-01
```

---

## Features

* Human-readable object rendering
* Multiple rendering modes (INLINE, TREE)
* Cycle detection (no infinite recursion)
* Depth limits
* Sensitive field redaction
* Annotation-based configuration

---

## Example: Redacting Sensitive Fields

```java
class User {
    String username;

    @WeaveRedact
    String secret;
}
```

Output:

```
User[username=john, secret=***]
```

---

## Typical Use Cases

* Logging processing results
* Debugging complex DTOs
* Inspecting nested object graphs
* CLI debugging tools

---

## Philosophy

JWeaver follows a zero-decision approach:

* No configuration required
* Sensible defaults
* Consistent output

Just call `JWeaver.weave(...)` and get useful results.

## Installation

Basic installation

```xml
<groupId>com.robinloom</groupId>
<artifactId>jweaver</artifactId>
<version>4.0</version>
```

## License

Apache License 2.0