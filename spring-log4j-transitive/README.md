# Spring Application with Transitive Log4j 1.x Dependency

This project demonstrates a typical scenario in older Spring applications where **Log4j 1.x comes as a transitive dependency** rather than being directly used in the application code.

## Dependency Chain

The application code uses:
- **SLF4J API** - The logging facade used in application code
- **Spring Framework** - Business logic and dependency injection

Log4j 1.x comes transitively through:
```
slf4j-log4j12 (SLF4J binding)
  └─> log4j:log4j:1.2.17 (transitive dependency)
```

## Key Points

1. **No Direct Log4j Usage**: The application code never imports `org.apache.log4j.*` classes
2. **SLF4J Abstraction**: All logging is done through `org.slf4j.Logger` and `org.slf4j.LoggerFactory`
3. **Transitive Dependency**: Log4j 1.x is pulled in automatically by the SLF4J Log4j binding
4. **Common Pattern**: This represents how many Spring 4.x applications were structured

## Running the Application

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Migration Considerations

When migrating to Log4j 2.x:
- Replace `slf4j-log4j12` with `log4j-slf4j-impl`
- Remove explicit `log4j:log4j` dependency
- Add `log4j-api` and `log4j-core` dependencies
- Convert `log4j.properties` to `log4j2.xml`
- **No application code changes needed** (thanks to SLF4J abstraction)

## Dependency Tree

To view the complete dependency tree:
```bash
mvn dependency:tree
```

You'll see that `log4j:log4j:1.2.17` appears as a transitive dependency under `slf4j-log4j12`.
