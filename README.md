# Spring IoC and Bean Loading - Learning Journey

## Project Overview
This project demonstrates different approaches to object creation in Java and Spring Framework, focusing on Inversion of Control (IoC) and Dependency Injection patterns.

---

## Table of Contents
1. [Basic Object Creation](#1-basic-object-creation)
2. [Spring IoC with @Bean](#2-spring-ioc-with-bean)
3. [@Configuration vs Without @Configuration](#3-configuration-vs-without-configuration)
4. [Bean Loading Strategies](#4-bean-loading-strategies)
5. [Key Learnings](#5-key-learnings)

---

## 1. Basic Object Creation

### Traditional Java Way (Without IoC)

```java
// Direct instantiation - Manual object creation
ISim sim = new BoostMobile();
sim.calling();
sim.data();
```

**Characteristics:**
- Constructor is called immediately
- `hashCode()` is NOT automatically called
- Developer has full control over object lifecycle
- Creates new instance every time
- No Spring container involvement

**Output:**
```
BoostMobile Constructor....
Boost Mobile Calling Enabled
Boost Mobile Data Enabled
```

**Key Observation:** `hashCode()` only gets called when explicitly invoked or when object is used in hash-based collections.

---

## 2. Spring IoC with @Bean

### Configuration Class

```java
@ComponentScan("com.example.demo.configs")
public class MySimConfig {
    @Bean
    public ISim sim(){
        System.out.println("MySimConfig.sim() Constructor");
        return new BoostMobile();
    }
}
```

### Usage

```java
ApplicationContext context = new AnnotationConfigApplicationContext(MySimConfig.class);
ISim simIOC = context.getBean(ISim.class);
simIOC.calling();
simIOC.data();

// Getting bean again - returns SAME instance (singleton)
ISim simIOC1 = context.getBean(ISim.class);
```

**Characteristics:**
- Spring manages object lifecycle
- `hashCode()` IS automatically called by Spring for bean registration
- Singleton by default - same instance returned on multiple getBean() calls
- Spring uses hash-based collections internally for bean caching

**Output:**
```
MySimConfig.sim()
BoostMobile Constructor....
BoostMobile.hashCode()1507554780
Boost Mobile Calling Enabled
Boost Mobile Data Enabled
```

**Key Observation:** Spring internally calls `hashCode()` for bean management, even though we didn't explicitly call it.

---

## 3. @Configuration vs Without @Configuration

### Without @Configuration (Lite Mode)

```java
public class WithoutConfigurationExample {
    @Bean
    public ISim woConfigSim() {
        return new BoostMobile();
    }

    @Bean
    public String serviceAWithout() {
        ISim simInstance = woConfigSim(); // Creates NEW instance
        return "ServiceA-Without uses: " + simInstance.hashCode();
    }

    @Bean
    public String serviceBWithout() {
        ISim simInstance = woConfigSim(); // Creates ANOTHER NEW instance
        return "ServiceB-Without uses: " + simInstance.hashCode();
    }
}
```

**Behavior:**
- No CGLIB proxying
- Direct method calls create new instances
- Each call to `woConfigSim()` creates a new BoostMobile object
- Multiple instances with different hash codes

**Output:**
```
WITHOUT @Config - Creating BoostMobile Constructor..
BoostMobile Constructor....
serviceAWithout..
WITHOUT @Config - Creating BoostMobile Constructor..
BoostMobile Constructor....
ServiceA-Without uses: 123456
serviceBWithout..
WITHOUT @Config - Creating BoostMobile Constructor..
BoostMobile Constructor....
ServiceB-Without uses: 789012  // Different hash code!
```

### With @Configuration (Full Mode)

```java
@Configuration
public class WithConfigurationExample {
    @Bean
    public ISim configSim() {
        return new BoostMobile();
    }

    @Bean
    public String serviceAWith() {
        ISim simInstance = configSim(); // Spring intercepts
        return "ServiceA-With uses: " + simInstance.hashCode();
    }

    @Bean
    public String serviceBWith() {
        ISim simInstance = configSim(); // Returns CACHED instance
        return "ServiceB-With uses: " + simInstance.hashCode();
    }
}
```

**Behavior:**
- CGLIB proxy created by Spring
- Method calls are intercepted
- Returns same cached instance (singleton)
- Single instance with same hash code

**Output:**
```
Creating BoostMobile - 54321
WIT @Config - Creating BoostMobile Constructor..
BoostMobile Constructor....
serviceAWith..
ServiceA-With uses: 987654
serviceBWith..
ServiceB-With uses: 987654  // Same hash code!
```

**Key Difference:**
| Aspect | Without @Configuration | With @Configuration |
|--------|----------------------|-------------------|
| Proxying | No CGLIB proxy | CGLIB proxy created |
| Method calls | Direct invocation | Intercepted by Spring |
| Instance creation | New instance each call | Cached singleton |
| Hash codes | Different | Same |

---

## 4. Bean Loading Strategies

### Strategy 1: Single Configuration Class

```java
ApplicationContext context = new AnnotationConfigApplicationContext(MySimConfig.class);
```

### Strategy 2: Multiple Configuration Classes

```java
ApplicationContext context = new AnnotationConfigApplicationContext(
    MySimConfig.class,
    WithoutConfigurationExample.class,
    WithConfigurationExample.class
);
```

### Strategy 3: @ComponentScan

```java
@ComponentScan("com.example.demo.configs")
public class MySimConfig {
    // Automatically scans and loads all @Configuration classes in package
}
```

### Strategy 4: @Import

```java
@Import({WithoutConfigurationExample.class, WithConfigurationExample.class})
public class MySimConfig {
    // Imports specific configuration classes
}
```

---

## 5. Key Learnings

### Object Creation Lifecycle

1. **Traditional Java:**
   - Constructor called
   - Manual lifecycle management
   - No automatic method invocations

2. **Spring IoC:**
   - Constructor called
   - `hashCode()` called by Spring for bean registration
   - Spring manages lifecycle
   - Singleton by default

### When hashCode() Gets Called

- **NOT called:** During normal object instantiation with `new`
- **Called by Spring:** When bean is registered in ApplicationContext
- **Called explicitly:** When you invoke `object.hashCode()`
- **Called implicitly:** When object is used in HashMap, HashSet, or printed

### Singleton Behavior

**Spring Singleton (Default):**
```java
ISim bean1 = context.getBean(ISim.class);
ISim bean2 = context.getBean(ISim.class);
// bean1 == bean2 (same instance)
```

**Without @Configuration:**
```java
// Inter-bean method calls create new instances
@Bean
public String service() {
    return woConfigSim(); // New instance created
}
```

**With @Configuration:**
```java
// Inter-bean method calls return cached instance
@Bean
public String service() {
    return configSim(); // Cached instance returned
}
```

### Multiple Beans of Same Type

**Problem:**
```java
// Two beans of type ISim
@Bean public ISim sim() { ... }
@Bean public ISim configSim() { ... }

// This throws exception
ISim bean = context.getBean(ISim.class);
// NoUniqueBeanDefinitionException: expected single matching bean but found 2
```

**Solutions:**

1. **Use bean name:**
```java
ISim bean = context.getBean("sim", ISim.class);
```

2. **Use @Primary:**
```java
@Bean
@Primary
public ISim sim() { ... }
```

3. **Use @Qualifier:**
```java
@Autowired
@Qualifier("sim")
private ISim sim;
```

---

## Project Structure

```
demo/
├── src/main/java/com/example/demo/
│   ├── DemoApplication.java              # Main application
│   ├── interfaces/
│   │   └── ISim.java                     # Interface
│   ├── implementations/
│   │   └── BoostMobile.java              # Implementation with hashCode override
│   └── configs/
│       ├── MySimConfig.java              # Basic configuration
│       ├── WithoutConfigurationExample.java  # Lite mode demo
│       └── WithConfigurationExample.java     # Full mode demo
├── SpringBeanLoadingOptions.md           # Bean loading strategies
└── README.md                             # This file
```

---

## Experiments Conducted

### Experiment 1: Direct vs IoC Object Creation
- Compared manual instantiation with Spring IoC
- Observed hashCode() behavior differences

### Experiment 2: Singleton Verification
- Called `getBean()` multiple times
- Verified same instance returned

### Experiment 3: @Configuration Impact
- Created two config classes (with and without @Configuration)
- Observed CGLIB proxy behavior
- Compared instance creation patterns

### Experiment 4: Bean Loading Methods
- Tested multiple ways to load configuration classes
- Evaluated @ComponentScan, @Import, and manual registration

### Experiment 5: Multiple Beans Conflict
- Created multiple beans of same type
- Resolved using bean names and @Primary

---

## Best Practices Learned

1. **Always use @Configuration** for configuration classes with @Bean methods
2. **Use @ComponentScan** for automatic bean discovery in larger projects
3. **Specify bean names** when multiple beans of same type exist
4. **Understand singleton scope** - Spring beans are singleton by default
5. **CGLIB proxying** is essential for proper inter-bean method calls
6. **hashCode() is called by Spring** for internal bean management

---

## Running the Examples

### Run DemoApplication
```bash
mvn spring-boot:run
```

### Run ConfigurationTest
```bash
java com.example.demo.ConfigurationTest
```

---

## Conclusion

This project demonstrates the fundamental differences between traditional Java object creation and Spring's IoC container. The key takeaway is understanding how Spring manages bean lifecycle, enforces singleton patterns through CGLIB proxying, and provides flexible configuration options for enterprise applications.

The @Configuration annotation is crucial for proper Spring behavior, ensuring that inter-bean method calls return cached instances rather than creating new objects, which is essential for maintaining singleton semantics and application performance.
