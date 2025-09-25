# Repository Package / Pacote de Repositórios

English | Português

## English

### Purpose
The repository layer is the persistence boundary of the ERP, implemented with Spring Data JPA. It encapsulates data access, providing a clean interface between domain entities and the underlying database, and enabling query creation via method names, JPQL/native @Query, and Specifications.

Key responsibilities:
- Abstract persistence operations (CRUD, pagination, sorting)
- Define query methods for domain-centric access patterns
- Support transactional services with efficient data retrieval
- Enable projections (interfaces/DTOs) to reduce over-fetching
- Integrate with JPA features (EntityGraph, Specifications, Auditing)

### Repository Structure & Extensions
Repositories typically extend Spring Data base interfaces:
- JpaRepository<Entity, ID>: CRUD + pagination/sorting
- PagingAndSortingRepository<Entity, ID>: pagination & sorting
- JpaSpecificationExecutor<Entity>: dynamic predicates via Specifications
- QueryByExampleExecutor<Entity>: example-based matching

Example base pattern:
```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRolesById(Long id);
}
```

### Examples of Repositories
- UserRepository: user lookups, uniqueness checks, role eager fetching
- ProductRepository: catalog queries (by category, price range, availability)
- InventoryRepository: stock movements and low-stock alerts
- CustomerRepository: search by name/email, relationships to orders
- RoleRepository: role/permission queries, assignment helpers
- FinancialRepository: revenues/expenses, date range, aggregation

Illustrative snippets:
```java
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategorySlugAndActiveTrue(String slug);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    @Query("select p from Product p join p.tags t where t.name in :tags")
    List<Product> findByTags(@Param("tags") Collection<String> tags);

    // Projection to reduce payload
    interface ProductSummary { Long getId(); String getName(); BigDecimal getPrice(); }
    Page<ProductSummary> findByActiveTrue(Pageable pageable);
}

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    @Query("select i from InventoryItem i where i.quantity <= i.reorderPoint")
    List<InventoryItem> findItemsAtOrBelowReorderPoint();
}

public interface FinancialRepository extends JpaRepository<TransactionEntry, Long> {
    @Query("select sum(t.amount) from TransactionEntry t where t.type = 'REVENUE' and t.date between :start and :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
```

Specifications example:
```java
public class UserSpecs {
    public static Specification<User> nameContains(String term) {
        return (root, q, cb) -> term == null ? null : cb.like(cb.lower(root.get("name")), "%" + term.toLowerCase() + "%");
    }
    public static Specification<User> hasRole(String roleName) {
        return (root, q, cb) -> {
            Join<User, Role> roles = root.join("roles", JoinType.LEFT);
            return roleName == null ? null : cb.equal(roles.get("name"), roleName);
        };
    }
}
```

### Query Types Cheat-Sheet
- Derived queries: findByEmail, findByStatusAndCreatedAtAfter
- @Query JPQL: complex joins, aggregations, fetch tuning
- Native queries: DB-specific functions and performance
- EntityGraph: control fetch strategy (avoid N+1)
- Projections: interfaces/classes for partial selects
- Specifications/Criteria: dynamic filters with composition

### Best Practices
- Method naming: keep derived method names expressive but reasonable; prefer Specifications for complex predicates
- Return types: Optional<T> for uniques; Page<T>/Slice<T> for large lists
- Fetching: use @EntityGraph or fetch joins where appropriate; avoid EAGER on entities, tune per-query
- Transactions: keep repositories free of business transactions; annotate at service layer
- Customization: create custom repository fragments for advanced logic (e.g., UserRepositoryCustom + Impl)
- Performance: paginate; project only needed fields; add indexes for filtered columns; avoid N+1; batch writes with saveAll and JDBC batch
- Consistency: prefer immutable projections for read paths; use database constraints to back repository checks (unique indexes)
- Testing: use @DataJpaTest with testcontainers or H2; seed minimal data; verify queries and indexes

Custom fragment example:
```java
public interface UserRepositoryCustom { List<User> search(UserFilter filter, Pageable pageable); }
public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext private EntityManager em;
    @Override public List<User> search(UserFilter f, Pageable p) { /* criteria builder here */ return List.of(); }
}
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom { }
```

### Onboarding for New Developers
1) Prerequisites
- Spring Data JPA fundamentals (repositories, transactions, entity mapping)
- JPQL/Criteria API, Specifications, EntityGraph
- Database indexing and query plans basics

2) Getting started
- Review existing repository interfaces for naming and projections
- Run @DataJpaTest samples to see query behavior
- Inspect service layer usages to understand fetch boundaries

3) Code review checklist
- Queries are paginated and/or projected when needed
- No unnecessary EAGER fetches; N+1 addressed via EntityGraph/fetch joins
- Derived method names are maintainable; complex logic in @Query or Specifications
- Database constraints support repository invariants (unique keys, FKs)

Author: Gabriel Demetrios Lafis

---

## Português

### Propósito
A camada de repositórios é a fronteira de persistência do ERP, implementada com Spring Data JPA. Ela encapsula o acesso a dados, fornecendo uma interface limpa entre entidades de domínio e o banco de dados, habilitando criação de consultas por nomes de métodos, @Query (JPQL/nativa) e Specifications.

Responsabilidades principais:
- Abstrair operações de persistência (CRUD, paginação, ordenação)
- Definir métodos de consulta alinhados ao domínio
- Suportar serviços transacionais com leituras eficientes
- Habilitar projeções (interfaces/DTOs) para reduzir over-fetching
- Integrar recursos do JPA (EntityGraph, Specifications, Auditing)

### Estrutura e Extensões de Repositórios
Repositórios normalmente estendem interfaces base do Spring Data:
- JpaRepository<Entidade, ID>: CRUD + paginação/ordenação
- PagingAndSortingRepository<Entidade, ID>: paginação e ordenação
- JpaSpecificationExecutor<Entidade>: predicados dinâmicos com Specifications
- QueryByExampleExecutor<Entidade>: matching por exemplo

Exemplo base:
```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRolesById(Long id);
}
```

### Exemplos de Repositórios
- UserRepository: buscas de usuário, verificação de unicidade, carregamento de roles
- ProductRepository: catálogo (por categoria, faixa de preço, disponibilidade)
- InventoryRepository: movimentos de estoque e alertas de baixo estoque
- CustomerRepository: busca por nome/email, relacionamento com pedidos
- RoleRepository: consultas de roles/permissões
- FinancialRepository: receitas/despesas, intervalo de datas, agregações

Trechos ilustrativos:
```java
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategorySlugAndActiveTrue(String slug);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    @Query("select p from Product p join p.tags t where t.name in :tags")
    List<Product> findByTags(@Param("tags") Collection<String> tags);

    interface ProductSummary { Long getId(); String getName(); BigDecimal getPrice(); }
    Page<ProductSummary> findByActiveTrue(Pageable pageable);
}

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    @Query("select i from InventoryItem i where i.quantity <= i.reorderPoint")
    List<InventoryItem> findItemsAtOrBelowReorderPoint();
}

public interface FinancialRepository extends JpaRepository<TransactionEntry, Long> {
    @Query("select sum(t.amount) from TransactionEntry t where t.type = 'REVENUE' and t.date between :start and :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
```

Exemplo de Specifications:
```java
public class UserSpecs {
    public static Specification<User> nameContains(String term) {
        return (root, q, cb) -> term == null ? null : cb.like(cb.lower(root.get("name")), "%" + term.toLowerCase() + "%");
    }
    public static Specification<User> hasRole(String roleName) {
        return (root, q, cb) -> {
            Join<User, Role> roles = root.join("roles", JoinType.LEFT);
            return roleName == null ? null : cb.equal(roles.get("name"), roleName);
        };
    }
}
```

### Tipos de Consultas (Resumo)
- Consultas derivadas: findByEmail, findByStatusAndCreatedAtAfter
- @Query JPQL: joins complexos, agregações, ajuste de fetch
- Consultas nativas: funções específicas do SGBD e performance
- EntityGraph: controle de estratégia de fetch (evitar N+1)
- Projeções: interfaces/classes para selects parciais
- Specifications/Criteria: filtros dinâmicos com composição

### Boas Práticas
- Nomeação de métodos: expressivos porém enxutos; prefira Specifications para predicados complexos
- Tipos de retorno: Optional<T> para únicos; Page<T>/Slice<T> para listas grandes
- Fetching: use @EntityGraph ou fetch join quando necessário; evite EAGER nas entidades, ajuste por consulta
- Transações: mantenha repositórios sem regras de negócio; anote transações na camada de serviço
- Customização: crie fragments customizados (ex.: UserRepositoryCustom + Impl)
- Performance: paginação; projete apenas campos necessários; índices para colunas filtradas; evitar N+1; lotes com saveAll e JDBC batch
- Consistência: prefira projeções imutáveis para leitura; apoie checagens com constraints no banco (índices únicos)
- Testes: use @DataJpaTest com testcontainers ou H2; dados mínimos; valide consultas e índices

Exemplo de fragmento customizado:
```java
public interface UserRepositoryCustom { List<User> search(UserFilter filter, Pageable pageable); }
public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext private EntityManager em;
    @Override public List<User> search(UserFilter f, Pageable p) { /* criteria builder aqui */ return List.of(); }
}
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom { }
```

### Onboarding Multilíngue para Novos Desenvolvedores
1) Pré-requisitos
- Fundamentos de Spring Data JPA (repositórios, transações, mapeamento)
- JPQL/Criteria API, Specifications, EntityGraph
- Noções de indexação e planos de consulta

2) Começando
- Revise as interfaces existentes para padronização de nomes e projeções
- Rode @DataJpaTest para observar o comportamento das consultas
- Inspecione usos na camada de serviço para entender limites de fetch

3) Checklist de code review
- Consultas paginadas e/ou projetadas quando necessário
- Nada de EAGER desnecessário; N+1 mitigado com EntityGraph/fetch join
- Métodos derivados mantíveis; lógica complexa em @Query ou Specifications
- Constraints de banco respaldam invariantes dos repositórios (chaves únicas, FKs)

Autor: Gabriel Demetrios Lafis
