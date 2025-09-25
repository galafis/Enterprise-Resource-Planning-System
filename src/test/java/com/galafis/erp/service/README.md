# Service Tests README / README de Testes de Serviços

English | Português

---

## English

### Purpose
This directory contains unit tests for service-layer components and business rules of the ERP application. The goal is to validate service logic in isolation, ensuring correctness for positive and negative scenarios, edge cases, and contract expectations.

### Typical Structure
- Naming: <ServiceName>Test.java (e.g., UserServiceTest.java)
- Given/When/Then sections inside each test
- Arrange/Act/Assert pattern (AAA)
- Use Mockito for collaborators; avoid hitting DB/network

Example layout:
- setUp(): initialize mocks, service under test (SUT)
- Test methods: shouldDoX_whenY, shouldFail_whenInvalidInput, etc.
- tearDown(): reset/close resources if needed

### Example with JUnit 5 and Mockito
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private UserRepository userRepository;
  @InjectMocks private UserService userService;

  @BeforeEach
  void setUp() { /* additional setup if required */ }

  @Test
  void shouldCreateUser_whenValidInput() {
    // Arrange
    var req = new CreateUserRequest("john", "john@example.com");
    when(userRepository.existsByEmail(req.email())).thenReturn(false);
    when(userRepository.save(any(User.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    // Act
    var created = userService.create(req);

    // Assert
    assertNotNull(created);
    assertEquals("john@example.com", created.getEmail());
    verify(userRepository).save(any(User.class));
  }

  @Test
  void shouldThrow_whenEmailAlreadyExists() {
    var req = new CreateUserRequest("john", "john@example.com");
    when(userRepository.existsByEmail(req.email())).thenReturn(true);

    assertThrows(EmailAlreadyExistsException.class, () -> userService.create(req));
    verify(userRepository, never()).save(any());
  }
}
```

### Setup and Teardown
- @BeforeEach: build SUT and test data
- @AfterEach: verifyNoMoreInteractions if helpful, clear static state
- Testcontainers or Spring context are NOT used for unit tests; prefer pure Mockito

### Best Practices
- Isolation: mock all external collaborators (repositories, clients)
- Deterministic: no time/system randomness without abstraction (use Clock)
- Coverage: include positive, negative, and edge cases; assert side effects
- Readability: one assertion concept per test, meaningful names
- Contracts: document assumptions in test names/Javadoc
- Maintainability: avoid duplication with builders/fixtures

### Onboarding Guide
1) Tools: JDK 17+, JUnit 5, Mockito, AssertJ/Hamcrest optionally
2) Build: `./mvnw test` or `./gradlew test`
3) Where to add tests: this folder mirrors service package structure
4) Style: AAA/GWT sections with clear naming
5) Running selectively: use IDE or `-Dtest=UserServiceTest`
6) Writing new tests: start from examples above and follow best practices

---

## Português

### Propósito
Este diretório contém testes unitários para componentes de serviço e regras de negócio do ERP. O objetivo é validar a lógica de serviços em isolamento, cobrindo cenários positivos e negativos, casos de borda e expectativas contratuais.

### Estrutura Típica
- Nomenclatura: <NomeDoServico>Test.java (ex.: UserServiceTest.java)
- Seções Dado/Quando/Então (Given/When/Then)
- Padrão Organizar/Executar/Verificar (AAA)
- Use Mockito para colaboradores; evite acessar DB/rede

Fluxo típico:
- setUp(): inicializar mocks e o SUT (service sob teste)
- Métodos: shouldFazerX_quandoY, shouldFalhar_quandoEntradaInválida, etc.
- tearDown(): liberar/fechar recursos se necessário

### Exemplo com JUnit 5 e Mockito
```java
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
  @Mock private InventoryRepository repo;
  @InjectMocks private InventoryService service;

  @Test
  void deveAtualizarEstoque_quandoEntradaValida() {
    when(repo.findBySku("SKU-1")).thenReturn(Optional.of(new Inventory("SKU-1", 10)));

    service.addStock("SKU-1", 5);

    verify(repo).save(argThat(inv -> inv.getQuantity() == 15));
  }

  @Test
  void deveLancarExcecao_quandoSkuInexistente() {
    when(repo.findBySku("SKU-X")).thenReturn(Optional.empty());

    assertThrows(SkuNotFoundException.class, () -> service.addStock("SKU-X", 1));
    verify(repo, never()).save(any());
  }
}
```

### Setup e Teardown
- @BeforeEach: construir SUT e dados de teste
- @AfterEach: `verifyNoMoreInteractions` quando fizer sentido; limpar estado estático
- Não use Testcontainers ou contexto Spring em testes unitários; prefira Mockito puro

### Boas Práticas
- Isolamento: mockar todos colaboradores externos (repositórios, clientes)
- Determinismo: sem aleatoriedade/tempo do sistema sem abstração (use Clock)
- Cobertura: inclua positivos, negativos e bordas; verifique efeitos colaterais
- Legibilidade: um conceito de asserção por teste; nomes claros
- Contratos: documente suposições nos nomes/Javadoc
- Manutenibilidade: evite duplicação com builders/fixtures

### Guia de Onboarding
1) Ferramentas: JDK 17+, JUnit 5, Mockito, AssertJ/Hamcrest (opcional)
2) Build: `./mvnw test` ou `./gradlew test`
3) Onde adicionar: este diretório espelha a estrutura dos serviços
4) Estilo: AAA/GWT com nomenclatura descritiva
5) Execução seletiva: IDE ou `-Dtest=UserServiceTest`
6) Ao criar testes: parta dos exemplos e siga as boas práticas

---

Autor: Gabriel Demetrios Lafis
