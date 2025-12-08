# Spring Application with Log4j 2.x Dependency

This project demonstrates a Spring application using **Log4j 2.x** through SLF4J for logging.

## Dependency Chain

The application code uses:
- **SLF4J API** - The logging facade used in application code
- **Spring Framework** - Business logic and dependency injection

Log4j 2.x is integrated through:
```
log4j-slf4j-impl (SLF4J binding for Log4j 2.x)
  └─> log4j-api:2.23.1
  └─> log4j-core:2.23.1
```

## Key Points

1. **No Direct Log4j Usage**: The application code never imports `org.apache.logging.log4j.*` classes
2. **SLF4J Abstraction**: All logging is done through `org.slf4j.Logger` and `org.slf4j.LoggerFactory`
3. **Log4j 2.x Backend**: Log4j 2.x is used as the logging implementation through the SLF4J binding
4. **Modern Pattern**: This represents the recommended approach for Spring applications using Log4j 2.x

## Running the Application

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Migration Considerations

This project has been migrated from Log4j 1.x to Log4j 2.x:
- Replaced `slf4j-log4j12` with `log4j-slf4j-impl`
- Added `log4j-api` and `log4j-core` dependencies (version 2.23.1)
- Converted `log4j.properties` to `log4j2.xml`
- **No application code changes needed** (thanks to SLF4J abstraction)

## Dependency Tree

To view the complete dependency tree:
```bash
mvn dependency:tree
```

You'll see that `log4j-api:2.23.1` and `log4j-core:2.23.1` are used as dependencies.
