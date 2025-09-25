# DTO Tests / Testes de DTO

🇺🇸 **English** | 🇧🇷 **Português**

---

## 🇺🇸 English

### 📋 Overview
This directory contains unit tests for Data Transfer Object (DTO) classes in the ERP system.

### 🧪 Test Coverage
- **Request DTO Tests** - Validation, serialization, and deserialization of input DTOs
- **Response DTO Tests** - Output DTOs structure and data mapping tests
- **Validation Tests** - Bean validation constraints and error handling
- **Mapper Tests** - Entity-to-DTO and DTO-to-Entity conversion tests
- **Serialization Tests** - JSON serialization/deserialization with Jackson

### 📁 Structure
```
dto/
├── request/                     # Request DTO tests
│   ├── UserRequestDtoTest.java
│   ├── ProductRequestDtoTest.java
│   └── OrderRequestDtoTest.java
├── response/                    # Response DTO tests
│   ├── UserResponseDtoTest.java
│   ├── ProductResponseDtoTest.java
│   └── OrderResponseDtoTest.java
├── mapper/                      # DTO mapper tests
│   ├── UserMapperTest.java
│   ├── ProductMapperTest.java
│   └── OrderMapperTest.java
└── validation/                  # Validation tests
    ├── ValidationConstraintTest.java
    └── CustomValidatorTest.java
```

### 🔧 Testing Strategy
- **@JsonTest** - JSON serialization/deserialization tests
- **@WebMvcTest** - DTO validation in web layer
- **@ParameterizedTest** - Multiple validation scenarios
- **@MockBean** - Mock dependencies in mapper tests
- **Jackson ObjectMapper** - JSON processing validation

### 🎯 Key Test Areas
- **Validation Constraints** - @NotNull, @Valid, @Size, @Email, etc.
- **JSON Mapping** - Correct field mapping and naming conventions
- **Data Binding** - Request parameter binding validation
- **Error Handling** - Validation error messages and codes
- **Edge Cases** - Null values, empty collections, boundary conditions

---

## 🇧🇷 Português

### 📋 Visão Geral
Este diretório contém testes unitários para classes de Data Transfer Object (DTO) no sistema ERP.

### 🧪 Cobertura de Testes
- **Testes de DTO de Requisição** - Validação, serialização e deserialização de DTOs de entrada
- **Testes de DTO de Resposta** - Testes de estrutura e mapeamento de dados de DTOs de saída
- **Testes de Validação** - Restrições de validação de beans e tratamento de erros
- **Testes de Mapeadores** - Testes de conversão Entidade-para-DTO e DTO-para-Entidade
- **Testes de Serialização** - Serialização/deserialização JSON com Jackson

### 📁 Estrutura
```
dto/
├── request/                     # Testes de DTO de requisição
│   ├── UserRequestDtoTest.java
│   ├── ProductRequestDtoTest.java
│   └── OrderRequestDtoTest.java
├── response/                    # Testes de DTO de resposta
│   ├── UserResponseDtoTest.java
│   ├── ProductResponseDtoTest.java
│   └── OrderResponseDtoTest.java
├── mapper/                      # Testes de mapeadores de DTO
│   ├── UserMapperTest.java
│   ├── ProductMapperTest.java
│   └── OrderMapperTest.java
└── validation/                  # Testes de validação
    ├── ValidationConstraintTest.java
    └── CustomValidatorTest.java
```

### 🔧 Estratégia de Testes
- **@JsonTest** - Testes de serialização/deserialização JSON
- **@WebMvcTest** - Validação de DTO na camada web
- **@ParameterizedTest** - Múltiplos cenários de validação
- **@MockBean** - Mock de dependências nos testes de mapeador
- **Jackson ObjectMapper** - Validação de processamento JSON

### 🎯 Áreas Principais de Teste
- **Restrições de Validação** - @NotNull, @Valid, @Size, @Email, etc.
- **Mapeamento JSON** - Mapeamento correto de campos e convenções de nomenclatura
- **Vinculação de Dados** - Validação de vinculação de parâmetros de requisição
- **Tratamento de Erros** - Mensagens e códigos de erro de validação
- **Casos Extremos** - Valores nulos, coleções vazias, condições de fronteira

---

## 🚀 Getting Started / Começando

### Running Tests / Executando Testes
```bash
# Run all DTO tests / Executar todos os testes de DTO
mvn test -Dtest="com.galafis.erp.dto.*"

# Run specific DTO test / Executar teste específico de DTO
mvn test -Dtest="UserRequestDtoTest"

# Run validation tests / Executar testes de validação
mvn test -Dtest="com.galafis.erp.dto.validation.*"
```

### Best Practices / Melhores Práticas
- Test all validation constraints and error scenarios
- Verify JSON serialization matches API contracts
- Test mapper bidirectional conversions
- Validate error messages are user-friendly
- Use test data builders for complex DTOs
- Test both positive and negative validation cases

---

*Created for Enterprise Resource Planning System*  
*Criado para o Sistema de Planejamento de Recursos Empresariais*
