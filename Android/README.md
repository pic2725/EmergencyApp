## Code Style

### Logging
Use `logback` instead of `Log` which is Android provided class
```
//Example
class ExampleClass
{
  private val logger by getLogger()
  
  fun hello()
  {
    logger.debug("world")
  }
}
```