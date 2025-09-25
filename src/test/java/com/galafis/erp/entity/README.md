# Entity Tests / Testes de Entidade

🇺🇸 **English** | 🇧🇷 **Português**

---

## 🇺🇸 English

### 📋 Overview
This directory contains unit tests for JPA Entity classes in the ERP system.

### 🧪 Test Coverage
- **Entity Validation Tests** - Bean validation constraints and JPA annotations
- **Relationship Tests** - JPA associations (@OneToMany, @ManyToOne, @ManyToMany, @OneToOne)
- **Persistence Tests** - Entity lifecycle and database interactions
- **Equals & HashCode Tests** - Contract verification for entity identity
- **Serialization Tests** - JSON serialization compatibility

### 📁 Structure
```
entity/
├── user/                        # User-related entities
│   ├── UserTest.java
│   ├── UserRoleTest.java
│   └── UserProfileTest.java
├── product/                     # Product-related entities
│   ├── ProductTest.java
│   ├── CategoryTest.java
│   └── InventoryTest.java
├── order/                       # Order-related entities
│   ├── OrderTest.java
│   ├── OrderItemTest.java
│   └── OrderStatusTest.java
├── financial/                   # Financial entities
│   ├── InvoiceTest.java
│   ├── PaymentTest.java
│   └── TransactionTest.java
└── common/                      # Common/base entities
    ├── AuditableEntityTest.java
    └── BaseEntityTest.java
```

### 🔧 Testing Strategy
- **@DataJpaTest** - JPA slice testing with in-memory database
- **@Transactional** - Transaction rollback for test isolation
- **TestEntityManager** - JPA entity manager for persistence operations
- **@TestPropertySource** - Custom test database properties
- **@DirtiesContext** - Context reloading for integration tests

### 🎯 Key Test Areas
- **Validation Constraints** - @NotNull, @Size, @Email, @Pattern, custom validators
- **JPA Annotations** - @Entity, @Table, @Column, @Id, @GeneratedValue
- **Relationship Mappings** - Bidirectional associations and cascade operations
- **Database Constraints** - Unique constraints, foreign keys, indexes
- **Entity Lifecycle** - @PrePersist, @PostPersist, @PreUpdate, @PostUpdate

### 🗃️ Database Testing
- **H2 In-Memory Database** - Fast test execution
- **Test Data Builders** - Clean test data creation
- **Database Migrations** - Flyway/Liquibase test scenarios
- **Performance Testing** - N+1 queries and lazy loading validation

---

## 🇧🇷 Português

### 📋 Visão Geral
Este diretório contém testes unitários para classes de Entidade JPA no sistema ERP.

### 🧪 Cobertura de Testes
- **Testes de Validação de Entidade** - Restrições de validação de beans e anotações JPA
- **Testes de Relacionamento** - Associações JPA (@OneToMany, @ManyToOne, @ManyToMany, @OneToOne)
- **Testes de Persistência** - Ciclo de vida de entidades e interações com banco de dados
- **Testes de Equals & HashCode** - Verificação de contrato para identidade de entidade
- **Testes de Serialização** - Compatibilidade de serialização JSON

### 📁 Estrutura
```
entity/
├── user/                        # Entidades relacionadas ao usuário
│   ├── UserTest.java
│   ├── UserRoleTest.java
│   └── UserProfileTest.java
├── product/                     # Entidades relacionadas ao produto
│   ├── ProductTest.java
│   ├── CategoryTest.java
│   └── InventoryTest.java
├── order/                       # Entidades relacionadas ao pedido
│   ├── OrderTest.java
│   ├── OrderItemTest.java
│   └── OrderStatusTest.java
├── financial/                   # Entidades financeiras
│   ├── InvoiceTest.java
│   ├── PaymentTest.java
│   └── TransactionTest.java
└── common/                      # Entidades comuns/base
    ├── AuditableEntityTest.java
    └── BaseEntityTest.java
```

### 🔧 Estratégia de Testes
- **@DataJpaTest** - Teste de fatia JPA com banco de dados em memória
- **@Transactional** - Rollback de transação para isolamento de teste
- **TestEntityManager** - Gerenciador de entidade JPA para operações de persistência
- **@TestPropertySource** - Propriedades customizadas de banco de teste
- **@DirtiesContext** - Recarregamento de contexto para testes de integração

### 🎯 Áreas Principais de Teste
- **Restrições de Validação** - @NotNull, @Size, @Email, @Pattern, validadores customizados
- **Anotações JPA** - @Entity, @Table, @Column, @Id, @GeneratedValue
- **Mapeamentos de Relacionamento** - Associações bidirecionais e operações em cascata
- **Restrições de Banco de Dados** - Restrições únicas, chaves estrangeiras, índices
- **Ciclo de Vida da Entidade** - @PrePersist, @PostPersist, @PreUpdate, @PostUpdate

### 🗃️ Testes de Banco de Dados
- **Banco H2 em Memória** - Execução rápida de testes
- **Construtores de Dados de Teste** - Criação limpa de dados de teste
- **Migrações de Banco** - Cenários de teste Flyway/Liquibase
- **Testes de Performance** - Validação de consultas N+1 e lazy loading

---

## 🚀 Getting Started / Começando

### Running Tests / Executando Testes
```bash
# Run all entity tests / Executar todos os testes de entidade
mvn test -Dtest="com.galafis.erp.entity.*"

# Run specific entity test / Executar teste específico de entidade
mvn test -Dtest="UserTest"

# Run JPA repository tests / Executar testes de repositório JPA
mvn test -Dtest="*EntityTest"
```

### Best Practices / Melhores Práticas
- Use @DataJpaTest for JPA entity testing
- Test all validation constraints and error scenarios
- Verify bidirectional relationship consistency
- Test entity equality and hash code contracts
- Use TestEntityManager for persistence operations
- Validate lazy loading and fetch strategies
- Test cascading operations (persist, merge, remove)
- Verify database constraint violations

---

*Created for Enterprise Resource Planning System*  
*Criado para o Sistema de Planejamento de Recursos Empresariais*
