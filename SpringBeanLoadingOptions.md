# Spring Bean Loading Options

## Overview
Different ways to load all Spring beans without passing each configuration class individually.

---

## Option 1: Multiple Classes in Context

Pass multiple configuration classes to the same ApplicationContext.

```java
ApplicationContext context = new AnnotationConfigApplicationContext(
    MySimConfig.class, 
    WithoutConfigurationExample.class,
    WithConfigurationExample.class
);
```

**Pros:**
- Simple and explicit
- No additional annotations needed

**Cons:**
- Must list all classes manually
- Not scalable for many configuration classes

---

## Option 2: @Import Annotation

Use `@Import` to include other configuration classes.

```java
@Configuration
@Import({WithoutConfigurationExample.class, WithConfigurationExample.class})
public class MySimConfig {
    @Bean
    public ISim sim(){
        return new BoostMobile();
    }
}

// Usage
ApplicationContext context = new AnnotationConfigApplicationContext(MySimConfig.class);
```

**Pros:**
- Centralized configuration
- Only need to pass one class to context

**Cons:**
- Still need to list all classes in @Import
- Tight coupling between configurations

---

## Option 3: @ComponentScan

Automatically scan and load all `@Configuration` classes in a package.

```java
@Configuration
@ComponentScan("com.example.demo.configs")
public class MySimConfig {
    @Bean
    public ISim sim(){
        return new BoostMobile();
    }
}

// Usage
ApplicationContext context = new AnnotationConfigApplicationContext(MySimConfig.class);
```

**Pros:**
- Automatic discovery of all configuration classes
- No need to list classes individually
- Scalable for large projects

**Cons:**
- Less explicit - harder to know what's loaded
- May load unwanted classes if not careful with package structure

---

## Option 4: Spring Boot Application Context

Use the existing Spring Boot application context.

```java
@SpringBootApplication
@Import({MySimConfig.class, WithoutConfigurationExample.class})
public class DemoApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        
        // All beans are now available
        ISim sim = context.getBean(ISim.class);
    }
}
```

**Pros:**
- Leverages existing Spring Boot setup
- Component scanning enabled by default
- Production-ready approach

**Cons:**
- Requires Spring Boot
- More overhead for simple examples

---

## Option 5: Package Scanning with Base Packages

Scan specific packages without a configuration class.

```java
ApplicationContext context = new AnnotationConfigApplicationContext();
((AnnotationConfigApplicationContext) context).scan("com.example.demo.configs");
((AnnotationConfigApplicationContext) context).refresh();
```

**Pros:**
- No need for a main configuration class
- Flexible package selection

**Cons:**
- More verbose syntax
- Less common pattern

---

## Recommendation

**For small projects:** Use Option 1 or Option 2
**For medium/large projects:** Use Option 3 (@ComponentScan)
**For Spring Boot applications:** Use Option 4

## Example Comparison

### Without Auto-Loading
```java
// Must specify each class
ApplicationContext context = new AnnotationConfigApplicationContext(
    ConfigA.class, ConfigB.class, ConfigC.class, ConfigD.class
);
```

### With @ComponentScan
```java
// Automatically loads all @Configuration classes in package
@Configuration
@ComponentScan("com.example.demo.configs")
public class AppConfig {}

ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
```
