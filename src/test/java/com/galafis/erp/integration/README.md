# 🧪 Integration Tests | Testes de Integração

*Author: Gabriel Demetrios Lafis*  
*Autor: Gabriel Demetrios Lafis*

---

## 🌍 Languages | Idiomas
- [🇺🇸 English](#english)
- [🇧🇷 Português](#português)

---

## English

### 🎯 Purpose

Welcome to the Integration Tests directory! This is where we validate that multiple components of our ERP system work together harmoniously. Integration tests are crucial for ensuring that the real-world scenarios our users experience function correctly.

**What makes integration tests special?**
- They test **real interactions** between system components
- They use **actual databases** (via TestContainers)
- They validate **complete user workflows**
- They catch issues that unit tests might miss

### 🏗️ Test Architecture

Our integration tests follow a layered approach:

```
┌─────────────────────────────────────────┐
│              Controllers                │ ← HTTP Layer
├─────────────────────────────────────────┤
│               Services                  │ ← Business Logic
├─────────────────────────────────────────┤
│             Repositories                │ ← Data Access
├─────────────────────────────────────────┤
│              Database                   │ ← Persistence
└─────────────────────────────────────────┘
```

### 🛠️ Technologies & Dependencies

#### Core Testing Framework
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

#### TestContainers (Real Database Testing)
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>
```

#### Additional Testing Tools
```xml
<dependency>
    <groupId>com.github.database-rider</groupId>
    <artifactId>rider-spring</artifactId>
    <scope>test</scope>
</dependency>
```

### 📋 Test Categories

#### 🔄 End-to-End API Tests
- **Purpose**: Validate complete user journeys
- **Scope**: HTTP requests → Database persistence
- **Examples**: User registration, product ordering, invoice generation

#### 🗄️ Database Integration Tests
- **Purpose**: Ensure data consistency and complex queries
- **Scope**: Repository layer with real database
- **Examples**: Transaction rollbacks, constraint validation, performance tests

#### 🌐 External System Integration
- **Purpose**: Test third-party service interactions
- **Scope**: APIs, payment gateways, email services
- **Examples**: Payment processing, notification delivery

### 📁 Test Examples

#### UserIntegrationTest
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("erp_test")
            .withUsername("test")
            .withPassword("test");
    
    @Test
    @DisplayName("Should complete user registration workflow")
    void shouldCompleteUserRegistration() {
        // Test implementation
    }
}
```

#### InventoryIntegrationTest
- Product lifecycle management
- Stock transactions and balancing
- Low stock alert workflows
- Multi-user concurrent operations

#### FinancialIntegrationTest
- Invoice generation and processing
- Payment processing workflows
- Financial report generation
- Tax calculation integration

### ⚙️ Configuration

#### Test Properties (application-test.yml)
```yaml
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

#### TestContainers Setup
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

### 🚀 Running Tests

#### Run All Integration Tests
```bash
mvn test -Dtest="*IntegrationTest"
```

#### Run Specific Test
```bash
mvn test -Dtest="UserIntegrationTest"
```

#### Run with Specific Profile
```bash
mvn test -Dspring.profiles.active=integration
```

#### Run with TestContainers
```bash
mvn test -Dtest="*IntegrationTest" -DforkCount=1
```

### 🎨 Best Practices

1. **🔒 Test Isolation**: Each test should clean up after itself
2. **🎭 Realistic Scenarios**: Test real-world use cases, not trivial examples
3. **📊 Proper Test Data**: Create representative datasets
4. **⚡ Performance Awareness**: Monitor test execution times
5. **🔄 Transaction Testing**: Verify rollback scenarios
6. **🛡️ Security Validation**: Test authorization at integration level
7. **📝 Clear Documentation**: Each test should tell a story

### 📈 Test Data Management

- Use `@Sql` annotations for test data setup
- Create reusable test data builders
- Implement cleanup strategies
- Use database migrations for schema setup

### 🎯 Contributing Guidelines

**Ready to contribute? We'd love your help!** 🌟

1. **Choose a test category** that interests you
2. **Follow naming conventions**: `*IntegrationTest.java`
3. **Write descriptive test names**: `shouldProcessPaymentWhenValidDataProvided()`
4. **Include both positive and negative scenarios**
5. **Document complex test logic with comments**

---

## Português

### 🎯 Propósito

Bem-vindo ao diretório de Testes de Integração! Aqui validamos que múltiplos componentes do nosso sistema ERP funcionam juntos harmoniosamente. Os testes de integração são cruciais para garantir que os cenários do mundo real que nossos usuários experimentam funcionem corretamente.

**O que torna os testes de integração especiais?**
- Testam **interações reais** entre componentes do sistema
- Usam **bancos de dados reais** (via TestContainers)
- Validam **fluxos completos do usuário**
- Capturam problemas que testes unitários podem perder

### 🏗️ Arquitetura dos Testes

Nossos testes de integração seguem uma abordagem em camadas:

```
┌─────────────────────────────────────────┐
│            Controladores               │ ← Camada HTTP
├─────────────────────────────────────────┤
│              Serviços                   │ ← Lógica de Negócio
├─────────────────────────────────────────┤
│            Repositórios                 │ ← Acesso a Dados
├─────────────────────────────────────────┤
│            Banco de Dados               │ ← Persistência
└─────────────────────────────────────────┘
```

### 🛠️ Tecnologias e Dependências

#### Framework de Testes Principal
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

#### TestContainers (Testes com Banco Real)
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>
```

### 📋 Categorias de Testes

#### 🔄 Testes de API End-to-End
- **Propósito**: Validar jornadas completas do usuário
- **Escopo**: Requisições HTTP → Persistência no banco
- **Exemplos**: Registro de usuário, pedidos de produtos, geração de faturas

#### 🗄️ Testes de Integração de Banco de Dados
- **Propósito**: Garantir consistência de dados e consultas complexas
- **Escopo**: Camada de repositório com banco real
- **Exemplos**: Rollbacks de transações, validação de constraints, testes de performance

#### 🌐 Integração com Sistemas Externos
- **Propósito**: Testar interações com serviços de terceiros
- **Escopo**: APIs, gateways de pagamento, serviços de email
- **Exemplos**: Processamento de pagamentos, entrega de notificações

### 🚀 Executando os Testes

#### Executar Todos os Testes de Integração
```bash
mvn test -Dtest="*IntegrationTest"
```

#### Executar Teste Específico
```bash
mvn test -Dtest="UserIntegrationTest"
```

#### Executar com Perfil Específico
```bash
mvn test -Dspring.profiles.active=integration
```

### 🎨 Melhores Práticas

1. **🔒 Isolamento de Testes**: Cada teste deve se limpar após execução
2. **🎭 Cenários Realistas**: Teste casos de uso do mundo real
3. **📊 Dados de Teste Adequados**: Crie conjuntos de dados representativos
4. **⚡ Consciência de Performance**: Monitore tempos de execução
5. **🔄 Testes de Transação**: Verifique cenários de rollback
6. **🛡️ Validação de Segurança**: Teste autorização no nível de integração
7. **📝 Documentação Clara**: Cada teste deve contar uma história

### 🎯 Diretrizes para Contribuição

**Pronto para contribuir? Adoraríamos sua ajuda!** 🌟

1. **Escolha uma categoria de teste** que te interesse
2. **Siga as convenções de nomenclatura**: `*IntegrationTest.java`
3. **Escreva nomes descritivos para testes**: `deveProcessarPagamentoQuandoDadosValidosForemFornecidos()`
4. **Inclua cenários positivos e negativos**
5. **documente lógica complexa dos testes com comentários**

### 💡 Motivação para Contribuir

**Por que seus testes de integração são importantes?**

- 🛡️ **Proteção**: Seus testes protegem o sistema contra regressões
- 🚀 **Confiança**: Permitem deploys seguros e refatorações
- 📚 **Documentação Viva**: Seus testes documentam como o sistema funciona
- 🌱 **Crescimento**: Aprenda sobre diferentes partes do sistema
- 🤝 **Colaboração**: Contribua para a qualidade coletiva do projeto

**Suas contribuições fazem a diferença!** Cada teste que você escreve torna nosso sistema mais robusto e confiável.

---

## 📞 Support | Suporte

- **Issues**: [GitHub Issues](https://github.com/galafis/Enterprise-Resource-Planning-System/issues)
- **Documentation**: [Project Wiki](https://github.com/galafis/Enterprise-Resource-Planning-System/wiki)
- **Author**: Gabriel Demetrios Lafis

---

**Happy Testing! | Bons Testes!** 🎉
