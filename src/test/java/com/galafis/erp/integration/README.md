# Integration Tests

This directory contains integration tests for the ERP system that test multiple components working together.

## Overview

Integration tests validate the interaction between different layers of the application (controller, service, repository) and external systems (database, external APIs).

## Testing Approach

- **@SpringBootTest**: For full application context loading
- **@TestContainers**: For database integration testing
- **@AutoConfigureTestDatabase**: For database configuration in tests
- **@Transactional**: For test data isolation
- **@Sql**: For test data setup

## Test Categories

### End-to-End API Tests
- Complete user workflows
- Full request-response cycles
- Cross-module interactions
- Authentication and authorization flows

### Database Integration Tests
- Complex queries and transactions
- Data consistency verification
- Constraint validation
- Performance testing

### External System Integration
- Payment gateway integration
- Email service integration
- File storage system integration
- Third-party API interactions

## Test Examples to Implement

### UserIntegrationTest
- User registration complete flow
- Login and session management
- Profile updates with validations
- Password reset functionality

### InventoryIntegrationTest
- Product lifecycle management
- Stock transactions and balancing
- Low stock alert workflows
- Multi-user concurrent operations

### FinancialIntegrationTest
- Invoice generation and processing
- Payment processing workflows
- Financial report generation
- Tax calculation integration

## Configuration

### Test Properties
```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
  profiles:
    active: test
```

### TestContainers Configuration
```java
@Testcontainers
class DatabaseIntegrationTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("erp_test")
        .withUsername("test")
        .withPassword("test");
}
```

## Best Practices

1. **Use Test Containers** for real database testing
2. **Keep tests independent** - each test should clean up after itself
3. **Test realistic scenarios** - avoid testing trivial cases
4. **Use proper test data** - create representative datasets
5. **Performance considerations** - monitor test execution times
6. **Test rollback scenarios** - verify transaction boundaries
7. **Security testing** - validate authorization at integration level

## Running Integration Tests

```bash
# Run all integration tests
mvn test -Dtest="*IntegrationTest"

# Run specific integration test
mvn test -Dtest="UserIntegrationTest"

# Run integration tests with specific profile
mvn test -Dspring.profiles.active=integration

# Run with TestContainers
mvn test -Dtest="*IntegrationTest" -DforkCount=1
```

## Test Data Management

- Use `@Sql` annotations for test data setup
- Create reusable test data builders
- Implement test data cleanup strategies
- Use database migrations for schema setup

## Monitoring and Reporting

- Integration test execution time tracking
- Test coverage for integration scenarios
- Performance benchmarking
- Database query analysis

## CI/CD Integration

Integration tests run in dedicated pipeline stages with:
- Isolated test databases
- Extended timeout configurations
- Comprehensive logging
- Artifact collection for debugging
