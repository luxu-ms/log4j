# Log4j v1 to v2 Upgrade Guide

This guide covers three common scenarios for upgrading from Log4j v1 (1.2.17) to Log4j v2 (2.23.1).

## Table of Contents
1. [Scenario 1: Simple Project with Log4j v1](#scenario-1-simple-project-with-log4j-v1)
2. [Scenario 2: Project Using SLF4J with Log4j v1 Backend](#scenario-2-project-using-slf4j-with-log4j-v1-backend)
3. [Scenario 3: Spring Project with Log4j v1](#scenario-3-spring-project-with-log4j-v1)

---

## Scenario 1: Simple Project with Log4j v1

This scenario covers projects that directly use Log4j v1 API without any abstraction layer.

### File Changes Overview
- `pom.xml` - Update dependencies
- `log4j.properties` - Delete (replaced by log4j2.xml)
- `src/main/resources/log4j2.xml` - Create new configuration
- `src/main/java/com/example/App.java` - Update imports and API calls

### 1.1 pom.xml

#### Before (Log4j v1)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>log4j-v1-project</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
    </dependencies>
</project>
```

#### After (Log4j v2)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>log4j-v1-project</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.23.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.23.1</version>
        </dependency>
    </dependencies>
</project>
```

**Key Changes:**
- Replace `log4j:log4j:1.2.17` with two dependencies:
  - `org.apache.logging.log4j:log4j-api:2.23.1` (API)
  - `org.apache.logging.log4j:log4j-core:2.23.1` (Implementation)

### 1.2 Configuration Files

#### Before: log4j.properties
```properties
# Root logger option
log4j.rootLogger=DEBUG, stdout, file

# Direct log messages to stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

# Direct log messages to a log file
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=logs/application.log
log4j.appender.file.MaxFileSize=10MB
log4j.appender.file.MaxBackupIndex=10
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```

**Action:** Delete this file

#### After: src/main/resources/log4j2.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
        </Console>
        
        <!-- File Appender -->
        <RollingFile name="File" fileName="logs/application.log"
                     filePattern="logs/application-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <Root level="debug">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>
</Configuration>
```

**Key Changes:**
- Delete the old file log4j.properties
- Add the new file log4j2.xml

### 1.3 Java Source Code

#### Before: src/main/java/com/example/App.java
```java
package com.example;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        // Configure Log4j
        PropertyConfigurator.configure("log4j.properties");

        logger.debug("Debug message");
        logger.info("Info message - Application started");
        logger.warn("Warning message");
        logger.error("Error message");
        logger.fatal("Fatal message");

        performSomeOperation();

        logger.info("Application finished");
    }

    private static void performSomeOperation() {
        logger.info("Performing some operation...");
        try {
            int result = divide(10, 2);
            logger.info("Division result: " + result);
        } catch (Exception e) {
            logger.error("Error during operation", e);
        }
    }

    private static int divide(int a, int b) {
        return a / b;
    }
}
```

#### After: src/main/java/com/example/App.java
```java
package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("Debug message");
        logger.info("Info message - Application started");
        logger.warn("Warning message");
        logger.error("Error message");
        logger.fatal("Fatal message");

        performSomeOperation();

        logger.info("Application finished");
    }

    private static void performSomeOperation() {
        logger.info("Performing some operation...");
        try {
            int result = divide(10, 2);
            logger.info("Division result: " + result);
        } catch (Exception e) {
            logger.error("Error during operation", e);
        }
    }

    private static int divide(int a, int b) {
        return a / b;
    }
}
```

**Key Changes:**
- Import `org.apache.log4j.Logger` → `org.apache.logging.log4j.Logger`
- Import `org.apache.log4j.PropertyConfigurator` → Remove (no longer needed)
- Add import `org.apache.logging.log4j.LogManager`
- `Logger.getLogger(App.class)` → `LogManager.getLogger(App.class)`
- Remove `PropertyConfigurator.configure()` call (Log4j 2.x auto-configures)
- Logging method calls remain unchanged (debug, info, warn, error, fatal)

---

## Scenario 2: Project Using SLF4J with Log4j v1 Backend

This scenario covers projects that use SLF4J as a logging facade with Log4j v1 as the backend implementation.

### File Changes Overview
- `pom.xml` - Update Log4j binding and add Log4j v2 dependencies
- `log4j.properties` - Delete (replaced by log4j2.xml)
- `src/main/resources/log4j2.xml` - Create new configuration
- `src/main/java/com/example/App.java` - No changes required (SLF4J abstraction)

### 2.1 pom.xml

#### Before (SLF4J with Log4j v1)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>slf4j-log4j-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <slf4j.version>1.7.36</slf4j.version>
    </properties>

    <dependencies>
        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        
        <!-- SLF4J binding for Log4j 1.2 -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        
        <!-- Log4j 1.2 -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
    </dependencies>
</project>
```

#### After (SLF4J with Log4j v2)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>slf4j-log4j-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <slf4j.version>1.7.36</slf4j.version>
    </properties>

    <dependencies>
        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        
        <!-- SLF4J binding for Log4j 2.x -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.23.1</version>
        </dependency>
        
        <!-- Log4j 2.x Core Dependencies -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.23.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.23.1</version>
        </dependency>
    </dependencies>
</project>
```

**Key Changes:**
- Keep `org.slf4j:slf4j-api` unchanged
- Replace `org.slf4j:slf4j-log4j12` with `org.apache.logging.log4j:log4j-slf4j-impl:2.23.1`
- Remove `log4j:log4j:1.2.17`
- Add `org.apache.logging.log4j:log4j-api:2.23.1`
- Add `org.apache.logging.log4j:log4j-core:2.23.1`

### 2.2 Configuration Files

#### Before: log4j.properties
```properties
# Root logger option
log4j.rootLogger=DEBUG, stdout, file

# Direct log messages to stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

# Direct log messages to a log file
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=logs/application.log
log4j.appender.file.MaxFileSize=10MB
log4j.appender.file.MaxBackupIndex=10
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```

**Action:** Delete this file

#### After: src/main/resources/log4j2.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
        </Console>
        
        <!-- File Appender -->
        <RollingFile name="File" fileName="logs/application.log"
                     filePattern="logs/application-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <Root level="debug">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>
</Configuration>
```
**Key Changes:**
- Delete the old file log4j.properties
- Add the new file log4j2.xml

### 2.3 Java Source Code

#### Before and After: src/main/java/com/example/App.java
```java
package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("Debug message");
        logger.info("Info message - Application started");
        logger.warn("Warning message");
        logger.error("Error message");

        performSomeOperation();

        logger.info("Application finished");
    }

    private static void performSomeOperation() {
        logger.info("Performing some operation...");
        try {
            int result = divide(10, 2);
            logger.info("Division result: {}", result);
            
            result = divide(10, 0);
        } catch (Exception e) {
            logger.error("Error during operation", e);
        }
    }

    private static int divide(int a, int b) {
        return a / b;
    }
}
```

**Key Changes:**
- **NO CODE CHANGES REQUIRED!**
- SLF4J abstraction shields application code from underlying logging implementation changes
- All imports remain the same (`org.slf4j.Logger`, `org.slf4j.LoggerFactory`)
- All logging method calls remain unchanged

---

## Scenario 3: Spring Project with Log4j v1

This scenario covers Spring Framework applications that use SLF4J with Log4j v1, including Spring-specific logging configurations.

### File Changes Overview
- `pom.xml` - Update Log4j binding and add Log4j v2 dependencies
- `src/main/resources/log4j.properties` - Delete (replaced by log4j2.xml)
- `src/main/resources/log4j2.xml` - Create new configuration with Spring loggers
- `src/main/java/**/*.java` - No changes required (SLF4J abstraction)

### 3.1 pom.xml

#### Before (Spring with Log4j v1)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>spring-log4j-transitive</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring.version>4.3.30.RELEASE</spring.version>
    </properties>

    <dependencies>
        <!-- Spring Framework -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>
        
        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        
        <!-- Bridge commons-logging to SLF4J -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>1.7.36</version>
        </dependency>
        
        <!-- SLF4J binding to Log4j 1.x -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.36</version>
        </dependency>
    </dependencies>
</project>
```

#### After (Spring with Log4j v2)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>spring-log4j-transitive</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring.version>4.3.30.RELEASE</spring.version>
    </properties>

    <dependencies>
        <!-- Spring Framework -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>
        
        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        
        <!-- Bridge commons-logging to SLF4J -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>1.7.36</version>
        </dependency>
        
        <!-- SLF4J binding to Log4j 2.x -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.23.1</version>
        </dependency>
        
        <!-- Log4j 2.x Core Dependencies -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.23.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.23.1</version>
        </dependency>
    </dependencies>
</project>
```

**Key Changes:**
- Keep Spring Framework dependencies unchanged
- Keep `org.slf4j:slf4j-api` and `org.slf4j:jcl-over-slf4j` unchanged
- Replace `org.slf4j:slf4j-log4j12` with `org.apache.logging.log4j:log4j-slf4j-impl:2.23.1`
- Add `org.apache.logging.log4j:log4j-api:2.23.1`
- Add `org.apache.logging.log4j:log4j-core:2.23.1`

### 3.2 Configuration Files

#### Before: src/main/resources/log4j.properties
```properties
# Root logger option
log4j.rootLogger=INFO, stdout

# Direct log messages to stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

# Spring Framework logging
log4j.logger.org.springframework=WARN
```

**Action:** Delete this file

#### After: src/main/resources/log4j2.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
        </Console>
        
        <!-- File Appender -->
        <RollingFile name="File" fileName="logs/application.log"
                     filePattern="logs/application-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
        
        <!-- Spring Framework logging -->
        <Logger name="org.springframework" level="warn" additivity="false">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Logger>
    </Loggers>
</Configuration>
```

**Key Changes:**
- Delete the old file log4j.properties
- Add the new file log4j2.xml

### 3.3 Java Source Code

#### Before and After: src/main/java/com/example/App.java
```java
package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.example.config.AppConfig;
import com.example.service.UserService;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        logger.info("Starting Spring application");
        
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        logger.info("Spring context initialized successfully");
        
        UserService userService = context.getBean(UserService.class);
        
        try {
            logger.info("Demonstrating user service operations...");
            userService.createUser("john.doe", "john.doe@example.com");
            userService.createUser("jane.smith", "jane.smith@example.com");
            
            String user = userService.getUserById(123L);
            logger.info("Retrieved user: {}", user);
            
            userService.deleteUser(123L);
            logger.info("Application completed successfully");
        } catch (Exception e) {
            logger.error("Error during application execution", e);
        }
        
        logger.info("Application finished");
    }
}
```

#### Before and After: src/main/java/com/example/service/UserService.java
```java
package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public void createUser(String username, String email) {
        logger.info("Creating user: {} with email: {}", username, email);
        // Business logic here
        logger.debug("User created successfully: {}", username);
    }
    
    public String getUserById(Long id) {
        logger.debug("Fetching user by id: {}", id);
        // Business logic here
        return "User-" + id;
    }
    
    public void deleteUser(Long id) {
        logger.info("Deleting user: {}", id);
        // Business logic here
        logger.debug("User deleted successfully: {}", id);
    }
}
```

**Key Changes:**
- **NO CODE CHANGES REQUIRED!**
- All application code uses SLF4J API
- Spring Framework internally handles the logging backend change
- All imports and method calls remain unchanged

---

## Migration Checklist

### For All Scenarios:

1. **Update pom.xml dependencies:**
   - [ ] Remove old Log4j v1 dependency (`log4j:log4j:1.2.17`)
   - [ ] Remove SLF4J Log4j v1 binding if used (`org.slf4j:slf4j-log4j12`)
   - [ ] Add Log4j 2 API (`org.apache.logging.log4j:log4j-api:2.23.1`)
   - [ ] Add Log4j 2 Core (`org.apache.logging.log4j:log4j-core:2.23.1`)
   - [ ] Add SLF4J binding for Log4j 2 if needed (`org.apache.logging.log4j:log4j-slf4j-impl:2.23.1`)

2. **Update configuration:**
   - [ ] Delete `log4j.properties` file
   - [ ] Create `src/main/resources/log4j2.xml` file
   - [ ] Convert logging levels and appenders to XML format
   - [ ] Preserve Spring-specific logger configurations if applicable

3. **Update code (Scenario 1 only):**
   - [ ] Replace `org.apache.log4j.*` imports with `org.apache.logging.log4j.*`
   - [ ] Replace `Logger.getLogger()` with `LogManager.getLogger()`
   - [ ] Remove `PropertyConfigurator.configure()` calls

4. **Validate changes:**
   - [ ] Build project successfully
   - [ ] Run all tests
   - [ ] Verify logging output format
   - [ ] Check log file rotation

---

## Additional Notes

### Benefits of Log4j v2
- **Performance:** Asynchronous logging with better throughput
- **Security:** Fixed critical vulnerabilities from Log4j v1
- **Features:** Lambda support, garbage-free logging, better configuration
- **Modern API:** More flexible and extensible

### SLF4J Advantage
Projects using SLF4J (Scenarios 2 and 3) benefit from:
- No code changes required during migration
- Easy switching between logging implementations
- Consistent API across different backends

### Spring Framework Considerations
- Spring uses commons-logging by default
- `jcl-over-slf4j` bridges commons-logging to SLF4J
- Spring-specific loggers help reduce verbose framework logs
- Consider upgrading Spring Framework for better Log4j 2 support

### Configuration Format Options
Log4j 2 supports multiple configuration formats:
- **XML** (recommended, shown in examples)
- **JSON**
- **YAML** (requires additional dependencies)
- **Properties** (limited features)

---

## Troubleshooting

### Common Issues

1. **ClassNotFoundException: org.apache.log4j.Logger**
   - Ensure old Log4j v1 dependency is completely removed
   - Check for transitive dependencies bringing in Log4j v1

2. **No logging output after migration**
   - Verify `log4j2.xml` is in `src/main/resources/`
   - Check XML syntax and configuration status attribute
   - Ensure log4j-core dependency is included

3. **SLF4J multiple bindings warning**
   - Remove conflicting SLF4J bindings (e.g., logback-classic)
   - Keep only `log4j-slf4j-impl` as SLF4J implementation

4. **Spring logs too verbose**
   - Add Spring-specific logger with higher level (WARN or ERROR)
   - Use `additivity="false"` to prevent duplicate messages

---

## References

- [Apache Log4j 2 Official Documentation](https://logging.apache.org/log4j/2.x/)
- [Log4j 2 Migration Guide](https://logging.apache.org/log4j/2.x/manual/migration.html)
- [SLF4J User Manual](https://www.slf4j.org/manual.html)
- [Spring Boot Logging Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)
