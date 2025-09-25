# Entity Package / Pacote de Entidades

## English

### Purpose

This package contains the persistent entities of the Enterprise Resource Planning (ERP) system. These entities represent the core data models that are persisted to the database, including:

- **User**: System users and their authentication data
- **Product**: Product catalog and inventory items
- **Order**: Purchase and sales orders
- **Inventory**: Stock management and warehouse data
- **Invoice**: Billing and financial transaction records
- **Customer**: Customer relationship management data
- **Supplier**: Vendor and supplier information

### Standard File Structure

Each entity should follow the standard JPA entity pattern:

```java
package com.galafis.erp.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Entity representing a [Entity Name] in the ERP system.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "table_name")
public class EntityName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public EntityName() {}

    public EntityName(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ... other getters and setters

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object obj) {
        // Implementation
    }

    @Override
    public int hashCode() {
        // Implementation
    }

    @Override
    public String toString() {
        // Implementation
    }
}
```

### Best Practices

#### Naming Conventions
- **Classes**: PascalCase (e.g., `CustomerOrder`, `ProductCategory`)
- **Fields**: camelCase (e.g., `firstName`, `createdAt`)
- **Table names**: snake_case (e.g., `customer_orders`, `product_categories`)
- **Column names**: snake_case (e.g., `first_name`, `created_at`)

#### Relationships
- Use `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany` appropriately
- Always define `fetch` strategy explicitly
- Use `cascade` operations carefully
- Implement bidirectional relationships properly

#### Validation
- Use Bean Validation annotations (`@NotNull`, `@Size`, `@Email`, etc.)
- Implement custom validators when needed
- Add database-level constraints

#### Documentation
- All entities must have comprehensive Javadoc
- Document all fields and relationships
- Include usage examples in class documentation

### Contributing Guidelines

1. **Before creating a new entity:**
   - Check if a similar entity already exists
   - Review the domain model documentation
   - Discuss with the team lead

2. **Entity requirements:**
   - Must implement `Serializable`
   - Must have proper JPA annotations
   - Must include audit fields (`createdAt`, `updatedAt`)
   - Must have comprehensive Javadoc
   - Must follow naming conventions

3. **Testing:**
   - Create unit tests for entity logic
   - Test all relationships
   - Validate constraints

4. **Code review:**
   - All entities require code review
   - Performance implications should be considered
   - Security aspects must be validated

### Onboarding for New Developers

1. **Prerequisites:**
   - Understanding of JPA/Hibernate
   - Knowledge of Bean Validation
   - Familiarity with database design principles

2. **Getting started:**
   - Review existing entities as examples
   - Understand the project's domain model
   - Set up local development environment

3. **Resources:**
   - [JPA Documentation](https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm)
   - [Bean Validation Guide](https://beanvalidation.org/2.0/spec/)
   - Project wiki and documentation

---

## Português

### Propósito

Este pacote contém as entidades persistentes do sistema de Planejamento de Recursos Empresariais (ERP). Essas entidades representam os modelos de dados centrais que são persistidos no banco de dados, incluindo:

- **User**: Usuários do sistema e seus dados de autenticação
- **Product**: Catálogo de produtos e itens de inventário
- **Order**: Pedidos de compra e venda
- **Inventory**: Gestão de estoque e dados de armazém
- **Invoice**: Registros de faturamento e transações financeiras
- **Customer**: Dados de gestão de relacionamento com clientes
- **Supplier**: Informações de fornecedores e vendors

### Estrutura Padrão dos Arquivos

Cada entidade deve seguir o padrão padrão de entidade JPA:

```java
package com.galafis.erp.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Entidade representando um [Nome da Entidade] no sistema ERP.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "nome_da_tabela")
public class NomeDaEntidade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String nome;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Construtores
    public NomeDaEntidade() {}

    public NomeDaEntidade(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    // ... métodos equals, hashCode e toString
}
```

### Boas Práticas

#### Convenções de Nomenclatura
- **Classes**: PascalCase (ex: `PedidoCliente`, `Categoriaproduto`)
- **Campos**: camelCase (ex: `primeiroNome`, `criadoEm`)
- **Nomes de tabelas**: snake_case (ex: `pedidos_clientes`, `categorias_produtos`)
- **Nomes de colunas**: snake_case (ex: `primeiro_nome`, `criado_em`)

#### Relacionamentos
- Use `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany` apropriadamente
- Sempre defina a estratégia `fetch` explicitamente
- Use operações `cascade` com cuidado
- Implemente relacionamentos bidirecionais adequadamente

#### Validação
- Use anotações de Bean Validation (`@NotNull`, `@Size`, `@Email`, etc.)
- Implemente validadores personalizados quando necessário
- Adicione restrições a nível de banco de dados

#### Documentação
- Todas as entidades devem ter Javadoc abrangente
- Documente todos os campos e relacionamentos
- Inclua exemplos de uso na documentação da classe

### Diretrizes para Contribuição

1. **Antes de criar uma nova entidade:**
   - Verifique se uma entidade similar já existe
   - Revise a documentação do modelo de domínio
   - Discuta com o líder da equipe

2. **Requisitos da entidade:**
   - Deve implementar `Serializable`
   - Deve ter anotações JPA adequadas
   - Deve incluir campos de auditoria (`criadoEm`, `atualizadoEm`)
   - Deve ter Javadoc abrangente
   - Deve seguir convenções de nomenclatura

3. **Testes:**
   - Criar testes unitários para lógica da entidade
   - Testar todos os relacionamentos
   - Validar restrições

4. **Revisão de código:**
   - Todas as entidades requerem revisão de código
   - Implicações de performance devem ser consideradas
   - Aspectos de segurança devem ser validados

### Onboarding para Novos Desenvolvedores

1. **Pré-requisitos:**
   - Entendimento de JPA/Hibernate
   - Conhecimento de Bean Validation
   - Familiaridade com princípios de design de banco de dados

2. **Começando:**
   - Revisar entidades existentes como exemplos
   - Entender o modelo de domínio do projeto
   - Configurar ambiente de desenvolvimento local

3. **Recursos:**
   - [Documentação JPA](https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm)
   - [Guia Bean Validation](https://beanvalidation.org/2.0/spec/)
   - Wiki e documentação do projeto

---

## Author / Autor

**Gabriel Demetrios Lafis**

---

*This documentation is maintained as part of the Enterprise Resource Planning System project.*  
*Esta documentação é mantida como parte do projeto do Sistema de Planejamento de Recursos Empresariais.*
