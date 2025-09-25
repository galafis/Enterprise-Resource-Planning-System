# DTO Package / Pacote de DTOs

English | Português

## English

### Purpose

The DTO (Data Transfer Object) package centralizes the definition of objects used for data transfer between different layers of the ERP application, particularly between the API layer and business logic. DTOs serve as a crucial abstraction that separates external API payloads from internal domain entities, providing better maintainability, security, and flexibility.

Key benefits:
• **Layer separation**: Decouples API contracts from domain models
• **Data validation**: Centralizes input validation logic
• **Security**: Prevents over-posting and controls data exposure
• **Versioning**: Enables API evolution without breaking internal structures
• **Testing**: Simplifies unit testing with predictable data structures

### DTO Types and Examples

#### Request DTOs
Used for incoming data from API endpoints:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
```

#### Response DTOs
Used for outgoing data to API clients:

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Sensitive fields like cost price are excluded
}
```

#### Update DTOs
For partial updates with optional validation:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequest {
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @Email(message = "Valid email format required")
    private String email;
    
    @Pattern(regexp = "\\d{10,15}", message = "Invalid phone format")
    private String phone;
    
    private String address;
}
```

### Validation Best Practices

• **Use Bean Validation annotations**: `@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Pattern`
• **Custom validators**: Create `@ValidCustomerType` for business-specific rules
• **Nested validation**: Use `@Valid` for complex objects
• **Group validation**: Define validation groups for different scenarios
• **Localized messages**: Externalize validation messages to properties files

Example of custom validation:
```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomerTypeValidator.class)
public @interface ValidCustomerType {
    String message() default "Invalid customer type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### Mapping Strategies

#### Manual Mapping
```java
public static UserResponse fromEntity(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .role(user.getRole())
        .createdAt(user.getCreatedAt())
        .build();
}
```

#### MapStruct Integration
```java
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
    Customer toEntity(CustomerCreateRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Customer customer, CustomerUpdateRequest request);
}
```

### Onboarding Guide

1. **Understand the structure**: Review existing DTOs in this package
2. **Follow naming conventions**: `*Request` for inputs, `*Response` for outputs
3. **Add validation**: Use appropriate Bean Validation annotations
4. **Create mappers**: Implement conversion methods between DTOs and entities
5. **Write tests**: Test validation rules and mapping logic
6. **Document APIs**: Use Swagger annotations for API documentation

### Customization and Extensibility

• **Inheritance**: Use abstract base DTOs for common fields
• **Composition**: Embed common DTOs within larger structures
• **Conditional fields**: Use `@JsonInclude` for optional response fields
• **Custom serialization**: Implement custom serializers for complex types
• **API versioning**: Create versioned DTO packages (v1, v2) when needed

Author: Gabriel Demetrios Lafis

---

## Português

### Propósito

O pacote DTO (Data Transfer Object) centraliza a definição de objetos usados para transferência de dados entre diferentes camadas da aplicação ERP, particularmente entre a camada de API e a lógica de negócio. Os DTOs servem como uma abstração crucial que separa payloads externos da API das entidades internas do domínio, proporcionando melhor manutenibilidade, segurança e flexibilidade.

Principais benefícios:
• **Separação de camadas**: Desacopla contratos de API dos modelos de domínio
• **Validação de dados**: Centraliza a lógica de validação de entrada
• **Segurança**: Previne over-posting e controla exposição de dados
• **Versionamento**: Permite evolução da API sem quebrar estruturas internas
• **Testes**: Simplifica testes unitários com estruturas de dados previsíveis

### Tipos de DTOs e Exemplos

#### DTOs de Request
Usados para dados de entrada dos endpoints da API:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome não deve exceder 100 caracteres")
    private String name;
    
    @Email(message = "Email válido é obrigatório")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    private String password;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
```

#### DTOs de Response
Usados para dados de saída para clientes da API:

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos sensíveis como preço de custo são excluídos
}
```

### Boas Práticas de Validação

• **Use anotações Bean Validation**: `@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Pattern`
• **Validadores customizados**: Crie `@ValidCustomerType` para regras específicas
• **Validação aninhada**: Use `@Valid` para objetos complexos
• **Grupos de validação**: Defina grupos para diferentes cenários
• **Mensagens localizadas**: Externalize mensagens para arquivos de propriedades

### Estratégias de Mapeamento

#### Mapeamento Manual
```java
public static UserResponse fromEntity(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .role(user.getRole())
        .createdAt(user.getCreatedAt())
        .build();
}
```

#### Integração com MapStruct
```java
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
    Customer toEntity(CustomerCreateRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Customer customer, CustomerUpdateRequest request);
}
```

### Guia de Onboarding

1. **Entenda a estrutura**: Revise DTOs existentes neste pacote
2. **Siga convenções de nomenclatura**: `*Request` para entradas, `*Response` para saídas
3. **Adicione validação**: Use anotações Bean Validation apropriadas
4. **Crie mappers**: Implemente métodos de conversão entre DTOs e entidades
5. **Escreva testes**: Teste regras de validação e lógica de mapeamento
6. **Documente APIs**: Use anotações Swagger para documentação

### Customização e Extensibilidade

• **Herança**: Use DTOs base abstratos para campos comuns
• **Composição**: Incorpore DTOs comuns em estruturas maiores
• **Campos condicionais**: Use `@JsonInclude` para campos opcionais
• **Serialização customizada**: Implemente serializadores para tipos complexos
• **Versionamento de API**: Crie pacotes DTO versionados (v1, v2) quando necessário

Autor: Gabriel Demetrios Lafis
