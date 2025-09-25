# Controller Tests

This directory contains controller layer tests for the ERP system.

## Overview

Controller tests focus on testing the web layer (REST controllers) using Spring Boot testing annotations like `@WebMvcTest`.

## Testing Approach

- **@WebMvcTest**: For focused controller testing
- **MockMvc**: For HTTP request/response testing
- **@MockBean**: For mocking service layer dependencies
- **@JsonTest**: For JSON serialization/deserialization testing

## Test Examples to Implement

### UserController Tests
- User registration endpoint testing
- User authentication endpoint testing
- User profile management endpoints
- Input validation testing
- Error handling scenarios

### InventoryController Tests
- Product CRUD operations
- Stock management endpoints
- Search and filtering endpoints
- Pagination testing
- Security authorization testing

### FinancialController Tests
- Financial report generation
- Invoice management
- Payment processing endpoints
- Data export functionality

## Best Practices

1. **Test all HTTP methods** (GET, POST, PUT, DELETE)
2. **Validate request/response payloads**
3. **Test error scenarios** (400, 401, 403, 404, 500)
4. **Mock external dependencies**
5. **Test security constraints**
6. **Validate content types and headers**

## Running Controller Tests

```bash
# Run all controller tests
mvn test -Dtest="*ControllerTest"

# Run specific controller test
mvn test -Dtest="UserControllerTest"

# Run with coverage
mvn test jacoco:report -Dtest="*ControllerTest"
```

## Integration with CI/CD

Controller tests are automatically executed as part of the build pipeline and must pass before deployment.
