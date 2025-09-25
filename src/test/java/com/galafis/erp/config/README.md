# Configuration Tests / Testes de Configuração

🇺🇸 **English** | 🇧🇷 **Português**

---

## 🇺🇸 English

### 📋 Overview
This directory contains unit and integration tests for configuration classes in the ERP system.

### 🧪 Test Coverage
- **Database Configuration Tests** - Connection pool, transaction manager, and JPA settings
- **Security Configuration Tests** - Authentication, authorization, and CORS configurations
- **Application Configuration Tests** - Custom beans, profiles, and properties validation
- **Cache Configuration Tests** - Redis configuration and cache policies
- **Integration Tests** - Full application context loading and configuration validation

### 📁 Structure
```
config/
├── DatabaseConfigTest.java      # Database configuration tests
├── SecurityConfigTest.java      # Security configuration tests
├── ApplicationConfigTest.java   # Application configuration tests
├── CacheConfigTest.java         # Cache configuration tests
└── integration/                 # Integration tests
    └── ConfigIntegrationTest.java
```

### 🔧 Testing Strategy
- **@SpringBootTest** - Full application context tests
- **@TestConfiguration** - Custom test configurations
- **@MockBean** - Mocking external dependencies
- **@TestPropertySource** - Custom properties for tests

---

## 🇧🇷 Português

### 📋 Visão Geral
Este diretório contém testes unitários e de integração para classes de configuração no sistema ERP.

### 🧪 Cobertura de Testes
- **Testes de Configuração de Banco** - Pool de conexões, gerenciador de transações e configurações JPA
- **Testes de Configuração de Segurança** - Autenticação, autorização e configurações CORS
- **Testes de Configuração da Aplicação** - Beans customizados, perfis e validação de propriedades
- **Testes de Configuração de Cache** - Configuração Redis e políticas de cache
- **Testes de Integração** - Carregamento completo do contexto e validação de configurações

### 📁 Estrutura
```
config/
├── DatabaseConfigTest.java      # Testes de configuração de banco
├── SecurityConfigTest.java      # Testes de configuração de segurança
├── ApplicationConfigTest.java   # Testes de configuração da aplicação
├── CacheConfigTest.java         # Testes de configuração de cache
└── integration/                 # Testes de integração
    └── ConfigIntegrationTest.java
```

### 🔧 Estratégia de Testes
- **@SpringBootTest** - Testes com contexto completo da aplicação
- **@TestConfiguration** - Configurações customizadas para testes
- **@MockBean** - Mock de dependências externas
- **@TestPropertySource** - Propriedades customizadas para testes

---

## 🚀 Getting Started / Começando

### Running Tests / Executando Testes
```bash
# Run all config tests / Executar todos os testes de config
mvn test -Dtest="com.galafis.erp.config.*"

# Run specific test / Executar teste específico
mvn test -Dtest="DatabaseConfigTest"
```

### Best Practices / Melhores Práticas
- Always test configuration loading and bean creation
- Validate configuration properties and defaults
- Test configuration profiles (dev, test, prod)
- Mock external dependencies in unit tests
- Use integration tests for full context validation

---

*Created for Enterprise Resource Planning System*  
*Criado para o Sistema de Planejamento de Recursos Empresariais*
