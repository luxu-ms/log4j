# Spring Application with Log4j 2.x

This project demonstrates a Spring application that has been migrated from **Log4j 1.x to Log4j 2.x**.

## Dependency Chain

The application code uses:
- **SLF4J API** - The logging facade used in application code
- **Spring Framework** - Business logic and dependency injection
- **Log4j 2** - Modern logging backend through SLF4J binding

Log4j 2.x is configured through:
```
log4j-slf4j-impl (SLF4J binding for Log4j 2)
  ├─> log4j-api:2.24.3
  └─> log4j-core:2.24.3
```

## Key Points

1. **No Direct Log4j Usage**: The application code never imports `org.apache.logging.log4j.*` classes
2. **SLF4J Abstraction**: All logging is done through `org.slf4j.Logger` and `org.slf4j.LoggerFactory`
3. **Log4j 2 Backend**: Log4j 2.x provides the actual logging implementation through SLF4J binding
4. **Modern Pattern**: This represents a clean migration approach that preserves the SLF4J abstraction

## Running the Application

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Migration Completed

This application has been successfully migrated from Log4j 1.x to Log4j 2.x:
- ✅ Replaced `slf4j-log4j12` with `log4j-slf4j-impl`
- ✅ Removed `log4j:log4j` dependency
- ✅ Added `log4j-api:2.24.3` and `log4j-core:2.24.3` dependencies
- ✅ Converted `log4j.properties` to `log4j2.xml`
- ✅ Upgraded Spring Framework to 5.3.39 for security fixes
- ✅ **No application code changes needed** (thanks to SLF4J abstraction)

## Dependency Tree

To view the complete dependency tree:
```bash
mvn dependency:tree
```

You'll see that `log4j-api:2.24.3` and `log4j-core:2.24.3` appear as dependencies under `log4j-slf4j-impl`.
