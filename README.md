# JWeaver
![Build And Test](https://github.com/robinloom/jweaver/actions/workflows/build.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.robinloom/jweaver)

JWeaver makes it easy to generate well-structured, human-readable representations of Java objects. With a simple API, you can format objects dynamically, as a tree, or as a bullet list - each with customizable options to match your needs.
## Features

**JWeaver** offers three primary modes for presenting object data in a clear and appealing way:

### Dynamic
A flat, concise representation of an object's properties.

```
Person[name=John Doe, birthday=1990-01-01]
```

The configuration of the output is highly dynamic:

```
== Person ==
name: John Doe
birthday: 1990-01-01
```

### Tree
A hierarchical representation that visually outlines nested object structures.

```
Person
|-- name=John Doe
`-- birthday=1990-01-01
```

### Bullet
A list-style representation where each property appears as its own bullet point.

```
- Person
  - name: John Doe
  - birthday: 1990-01-01
```

## Installation

Add JWeaver to your Maven project:

<details>
  <summary><strong>Maven</strong></summary>

```xml
<dependency>
    <groupId>com.robinloom</groupId>
    <artifactId>jweaver</artifactId>
    <version>1.0.0</version>
</dependency>
```
</details> <details> <summary><strong>Gradle</strong></summary>

```
implementation 'com.robinloom:jweaver:1.0.0'
```
</details>

## Usage
### Basic Usage

An easy way to use JWeaver is to simply override the `toString()` method in your class and delegate to JWeaver:

```java
public class Person {
    private final String name;
    private final String birthday;

    public Person(String name, String birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return JWeaver.getDynamic().weave(this);
    }
```

Now, printing an instance of Person will return a formatted string:

```java
Person person = new Person("John Doe", "1990-01-01");
System.out.println(person);
```

Output:
```yaml
Person[name=John Doe, birthday=1990-01-01]
```

### Choosing a Weaver

JWeaver supports multiple output formats. You can select the desired weaver as follows:

```java
Person person = new Person("John Doe", "1990-01-01");

// Dynamic format
System.out.println(JWeaver.getDynamic().weave(person));

// Tree format
System.out.println(JWeaver.getTree().weave(person));

// Bullet format
System.out.println(JWeaver.getBullet().weave(person));
```

### Customizing the output

JWeaver allows customization of formatting options such as separators, indentation, and more for all weavers.

#### Customizing the weavers

```java
DynamicWeaver weaver = JWeaver.getDynamic()
                              .classNameFieldsSeparator(" -> ") // String that separates class name and fields
                              .fieldValueSeparator(": ") // String that separates field name and value
                              .capitalizeFields(); // Capitalizes field names

System.out.println(weaver.weave(person));
```

Example output:

```
Person -> Name: John Doe, Birthday: 1990-01-01
```

### Global configuration with System Properties
JWeaver supports system-wide configuration using Java system properties:

```properties
com.robinloom.jweaver.excludedFields=password
com.robinloom.jweaver.capitalizeFields=true
com.robinloom.jweaver.showDataTypes=true
com.robinloom.jweaver.dynamic.classNamePrefix=[*]
# [...]
```

JWeaver will automatically apply these settings to all instances.

---
This is just the beginning! Explore the API for more customization options and advanced features.