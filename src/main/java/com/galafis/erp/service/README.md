# Service Package / Pacote de Serviços

**English** | **Português**

## English

### Purpose

The service layer contains the core business logic of the ERP system. Services act as intermediary components between controllers and repositories, implementing business rules, orchestrating data operations, and ensuring transaction management. They provide a clean abstraction for business operations and enforce domain-specific constraints.

**Key responsibilities:**
- Implement business logic and domain rules
- Coordinate operations across multiple repositories/data sources
- Manage transactions and data consistency
- Transform data between domain models and DTOs
- Provide reusable business operations for controllers
- Handle domain-specific validations and constraints

### Service Architecture Pattern

We follow a **Service-Interface pattern** for better testability and loose coupling:

```java
// Interface definition
public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse findById(Long id);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    Page<UserResponse> findAll(Pageable pageable);
}

// Implementation
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    @Override
    public UserResponse createUser(UserCreateRequest request) {
        // Validate business rules
        validateUserCreation(request);
        
        // Map to domain entity
        User user = userMapper.toEntity(request);
        
        // Save to repository
        User savedUser = userRepository.save(user);
        
        // Return response DTO
        return userMapper.toResponse(savedUser);
    }
    
    private void validateUserCreation(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }
    }
}
```

### Example Services in this ERP

#### 1. **UserService**
- User registration and authentication
- Profile management
- Role and permission assignment
- User lifecycle operations

#### 2. **ProductService** 
- Product catalog management
- Inventory tracking
- Pricing and discount logic
- Product categorization

#### 3. **OrderService**
- Order processing workflow
- Payment integration
- Order status management
- Customer notification handling

#### 4. **InventoryService**
- Stock level management
- Reorder point calculations
- Warehouse operations
- Stock movement tracking

#### 5. **FinancialService**
- Revenue and expense tracking
- Financial reporting
- Tax calculations
- Budget management

### Transaction Management

All services use **declarative transaction management**:

```java
@Service
@Transactional // Default: read-write transaction
public class OrderServiceImpl implements OrderService {
    
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        // Read-only operation
    }
    
    @Transactional(rollbackFor = {BusinessException.class, PaymentException.class})
    public OrderResponse processOrder(OrderRequest request) {
        // Complex business operation with custom rollback rules
    }
}
```

### Best Practices

#### **1. Dependency Injection**
- Use constructor injection for mandatory dependencies
- Favor composition over inheritance
- Inject interfaces, not concrete implementations

#### **2. Exception Handling**
- Create domain-specific exceptions (e.g., `UserNotFoundException`)
- Let exceptions bubble up to the controller layer
- Use checked exceptions sparingly

#### **3. Testing**
- Mock external dependencies in unit tests
- Use `@MockBean` for Spring integration tests
- Test business logic thoroughly with edge cases

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void shouldCreateUser_WhenValidRequest() {
        // Given
        UserCreateRequest request = new UserCreateRequest("test@example.com", "John Doe");
        User user = new User(1L, "test@example.com", "John Doe");
        UserResponse expectedResponse = new UserResponse(1L, "test@example.com", "John Doe");
        
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);
        
        // When
        UserResponse result = userService.createUser(request);
        
        // Then
        assertEquals(expectedResponse, result);
        verify(userRepository).save(user);
    }
}
```

#### **4. Interface Segregation**
- Keep interfaces focused and cohesive
- Split large service interfaces into smaller, specialized ones
- Follow Single Responsibility Principle

### Performance Considerations

- Use `@Cacheable` for frequently accessed, rarely changed data
- Implement pagination for large result sets
- Consider async processing for long-running operations
- Use batch operations when dealing with multiple entities

### Onboarding for New Developers

1. **Prerequisites:**
   - Spring Framework (IoC, AOP, Transactions)
   - JPA/Hibernate for data access
   - Mockito for unit testing
   - Understanding of SOLID principles

2. **Getting started:**
   - Review existing service interfaces and implementations
   - Understand the transaction boundaries
   - Study the DTO mapping patterns
   - Practice writing comprehensive unit tests

3. **Code review checklist:**
   - Business logic is properly encapsulated
   - Transactions are correctly configured
   - Exceptions are meaningful and well-handled
   - Unit tests cover edge cases and error scenarios

---

## Português

### Propósito

A camada de serviços contém a lógica de negócio central do sistema ERP. Os serviços atuam como componentes intermediários entre controllers e repositories, implementando regras de negócio, orquestrando operações de dados e garantindo o gerenciamento de transações. Eles fornecem uma abstração limpa para operações de negócio e aplicam restrições específicas do domínio.

**Responsabilidades principais:**
- Implementar lógica de negócio e regras do domínio
- Coordenar operações entre múltiplos repositories/fontes de dados
- Gerenciar transações e consistência de dados
- Transformar dados entre modelos de domínio e DTOs
- Fornecer operações de negócio reutilizáveis para controllers
- Tratar validações e restrições específicas do domínio

### Padrão de Arquitetura de Serviços

Seguimos o padrão **Service-Interface** para melhor testabilidade e baixo acoplamento:

```java
// Definição da interface
public interface UsuarioService {
    UsuarioResponse criarUsuario(UsuarioCriarRequest request);
    UsuarioResponse buscarPorId(Long id);
    UsuarioResponse atualizarUsuario(Long id, UsuarioAtualizarRequest request);
    void excluirUsuario(Long id);
    Page<UsuarioResponse> buscarTodos(Pageable pageable);
}

// Implementação
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }
    
    @Override
    public UsuarioResponse criarUsuario(UsuarioCriarRequest request) {
        // Validar regras de negócio
        validarCriacaoUsuario(request);
        
        // Mapear para entidade de domínio
        Usuario usuario = usuarioMapper.paraEntidade(request);
        
        // Salvar no repository
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        // Retornar DTO de resposta
        return usuarioMapper.paraResponse(usuarioSalvo);
    }
    
    private void validarCriacaoUsuario(UsuarioCriarRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já existe");
        }
    }
}
```

### Exemplos de Serviços neste ERP

#### 1. **UserService (UsuarioService)**
- Registro e autenticação de usuários
- Gerenciamento de perfis
- Atribuição de roles e permissões
- Operações do ciclo de vida do usuário

#### 2. **ProductService (ProdutoService)**
- Gerenciamento de catálogo de produtos
- Rastreamento de estoque
- Lógica de preços e descontos
- Categorização de produtos

#### 3. **OrderService (PedidoService)**
- Fluxo de processamento de pedidos
- Integração com pagamentos
- Gerenciamento de status de pedidos
- Tratamento de notificações ao cliente

#### 4. **InventoryService (EstoqueService)**
- Gerenciamento de níveis de estoque
- Cálculos de ponto de reposição
- Operações de depósito
- Rastreamento de movimentação de estoque

#### 5. **FinancialService (FinanceiroService)**
- Rastreamento de receitas e despesas
- Relatórios financeiros
- Cálculos de impostos
- Gerenciamento de orçamento

### Gerenciamento de Transações

Todos os serviços usam **gerenciamento declarativo de transações**:

```java
@Service
@Transactional // Padrão: transação de leitura-escrita
public class PedidoServiceImpl implements PedidoService {
    
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        // Operação somente leitura
    }
    
    @Transactional(rollbackFor = {BusinessException.class, PaymentException.class})
    public PedidoResponse processarPedido(PedidoRequest request) {
        // Operação complexa de negócio com regras customizadas de rollback
    }
}
```

### Boas Práticas

#### **1. Injeção de Dependência**
- Use injeção por construtor para dependências obrigatórias
- Favoreça composição sobre herança
- Injete interfaces, não implementações concretas

#### **2. Tratamento de Exceções**
- Crie exceções específicas do domínio (ex.: `UsuarioNaoEncontradoException`)
- Deixe exceções borbulhar até a camada de controller
- Use exceções verificadas com parcimônia

#### **3. Testes**
- Mock dependências externas em testes unitários
- Use `@MockBean` para testes de integração Spring
- Teste lógica de negócio completamente com casos extremos

#### **4. Segregação de Interface**
- Mantenha interfaces focadas e coesas
- Divida interfaces de serviço grandes em menores e especializadas
- Siga o Princípio da Responsabilidade Única

### Considerações de Performance

- Use `@Cacheable` para dados acessados frequentemente e raramente alterados
- Implemente paginação para conjuntos de resultados grandes
- Considere processamento assíncrono para operações demoradas
- Use operações em lote ao lidar com múltiplas entidades

### Onboarding para Novos Desenvolvedores

1. **Pré-requisitos:**
   - Spring Framework (IoC, AOP, Transações)
   - JPA/Hibernate para acesso a dados
   - Mockito para testes unitários
   - Entendimento dos princípios SOLID

2. **Começando:**
   - Revisar interfaces e implementações de serviços existentes
   - Entender os limites das transações
   - Estudar os padrões de mapeamento DTO
   - Praticar escrevendo testes unitários abrangentes

3. **Checklist de code review:**
   - Lógica de negócio está adequadamente encapsulada
   - Transações estão corretamente configuradas
   - Exceções são significativas e bem tratadas
   - Testes unitários cobrem casos extremos e cenários de erro

---

**Author / Autor:** Gabriel Demetrios Lafis

This documentation is maintained as part of the Enterprise Resource Planning System project.
Esta documentação é mantida como parte do projeto do Sistema de Planejamento de Recursos Empresariais.
