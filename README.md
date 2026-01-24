# JWeaver
![Build And Test](https://github.com/robinloom/jweaver/actions/workflows/build.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.robinloom/jweaver)

JWeaver is a simple, zero-decision Java utility for generating human-readable,
structured string representations of objects. It eliminates the need to write
custom `toString()` methods while producing consistent, safe, and readable output.

---

## Minimal Usage – Zero Decisions

For most users, weaving an object is as easy as:

```java
Person person = new Person("John Doe", "1990-01-01");
System.out.println(JWeaver.weave(person));
```

What happens automatically
- compact one-line output
- Collections → limited to a safe number of elements
- Circular or reciprocal references are detected
- Sensitive fields (password, token, etc.) are automatically ignored

No builder, no configuration, no thinking required.

Example output:

```yaml
Person[name=John Doe, birthday=1990-01-01]
```

## Installation

Add JWeaver to your project:

<details>
  <summary><strong>Maven</strong></summary>

```xml
<dependency>
    <groupId>com.robinloom</groupId>
    <artifactId>jweaver</artifactId>
    <version>2.0</version>
</dependency>
```
</details> <details> <summary><strong>Gradle</strong></summary>

```
implementation 'com.robinloom:jweaver:2.0'
```
</details>

## Why JWeaver?
- Removes boilerplate toString() implementations
- Ensures consistent, readable, and safe output
- Works out-of-the-box for simple usage