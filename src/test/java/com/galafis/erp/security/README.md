# Security Tests / Testes de Segurança

🇺🇸 **English** | 🇧🇷 **Português**

---

## 🇺🇸 English

### 📋 Overview
This directory contains unit and integration tests for Security components in the ERP system.

### 🧪 Test Coverage
- **Authentication Tests** - Login, logout, token validation, and authentication flows
- **Authorization Tests** - Role-based access control and permission validation
- **JWT Tests** - Token generation, validation, expiration, and refresh
- **Security Configuration Tests** - Spring Security configuration and filters
- **Password Security Tests** - Encryption, validation, and policy enforcement

### 📁 Structure
```
security/
├── authentication/              # Authentication tests
│   ├── JwtAuthenticationTest.java
│   ├── CustomAuthenticationProviderTest.java
│   └── AuthenticationControllerTest.java
├── authorization/               # Authorization tests
│   ├── RoleBasedAccessTest.java
│   ├── MethodSecurityTest.java
│   └── PermissionEvaluatorTest.java
├── jwt/                         # JWT-specific tests
│   ├── JwtTokenProviderTest.java
│   ├── JwtAuthenticationFilterTest.java
│   └── JwtTokenValidationTest.java
├── config/                      # Security configuration tests
│   ├── SecurityConfigTest.java
│   ├── CorsConfigurationTest.java
│   └── WebSecurityTest.java
└── password/                    # Password security tests
    ├── PasswordEncoderTest.java
    ├── PasswordValidationTest.java
    └── PasswordPolicyTest.java
```

### 🔧 Testing Strategy
- **@WebMvcTest** - Security filter chain and endpoint protection tests
- **@SpringBootTest** - Full security context integration tests
- **@WithMockUser** - Mock authenticated users with roles
- **@WithAnonymousUser** - Test anonymous access scenarios
- **MockMvc** - HTTP security testing with request/response validation

### 🎯 Key Test Areas
- **Endpoint Security** - URL-based access control and HTTP method restrictions
- **Authentication Flows** - Login success/failure, token generation, session management
- **Role-Based Access** - @PreAuthorize, @Secured, role hierarchy validation
- **CSRF Protection** - Cross-site request forgery prevention
- **CORS Configuration** - Cross-origin resource sharing policies
- **Password Policies** - Strength requirements, encoding, and validation

### 🔒 Security Test Types
- **Authentication Tests** - Valid/invalid credentials, token expiration
- **Authorization Tests** - Access denied/granted based on roles
- **Filter Tests** - Custom security filters and request processing
- **Exception Handling** - Security-related exception responses
- **Session Management** - Session creation, invalidation, and timeout

---

## 🇧🇷 Português

### 📋 Visão Geral
Este diretório contém testes unitários e de integração para componentes de Segurança no sistema ERP.

### 🧪 Cobertura de Testes
- **Testes de Autenticação** - Login, logout, validação de token e fluxos de autenticação
- **Testes de Autorização** - Controle de acesso baseado em funções e validação de permissões
- **Testes JWT** - Geração, validação, expiração e renovação de tokens
- **Testes de Configuração de Segurança** - Configuração e filtros do Spring Security
- **Testes de Segurança de Senhas** - Criptografia, validação e aplicação de políticas

### 📁 Estrutura
```
security/
├── authentication/              # Testes de autenticação
│   ├── JwtAuthenticationTest.java
│   ├── CustomAuthenticationProviderTest.java
│   └── AuthenticationControllerTest.java
├── authorization/               # Testes de autorização
│   ├── RoleBasedAccessTest.java
│   ├── MethodSecurityTest.java
│   └── PermissionEvaluatorTest.java
├── jwt/                         # Testes específicos de JWT
│   ├── JwtTokenProviderTest.java
│   ├── JwtAuthenticationFilterTest.java
│   └── JwtTokenValidationTest.java
├── config/                      # Testes de configuração de segurança
│   ├── SecurityConfigTest.java
│   ├── CorsConfigurationTest.java
│   └── WebSecurityTest.java
└── password/                    # Testes de segurança de senhas
    ├── PasswordEncoderTest.java
    ├── PasswordValidationTest.java
    └── PasswordPolicyTest.java
```

### 🔧 Estratégia de Testes
- **@WebMvcTest** - Testes da cadeia de filtros de segurança e proteção de endpoints
- **@SpringBootTest** - Testes de integração com contexto de segurança completo
- **@WithMockUser** - Mock de usuários autenticados com funções
- **@WithAnonymousUser** - Testes de cenários de acesso anônimo
- **MockMvc** - Testes de segurança HTTP com validação de requisição/resposta

### 🎯 Áreas Principais de Teste
- **Segurança de Endpoints** - Controle de acesso baseado em URL e restrições de métodos HTTP
- **Fluxos de Autenticação** - Sucesso/falha de login, geração de tokens, gerenciamento de sessão
- **Acesso Baseado em Funções** - @PreAuthorize, @Secured, validação de hierarquia de funções
- **Proteção CSRF** - Prevenção de falsificação de solicitação entre sites
- **Configuração CORS** - Políticas de compartilhamento de recursos entre origens
- **Políticas de Senha** - Requisitos de força, codificação e validação

### 🔒 Tipos de Teste de Segurança
- **Testes de Autenticação** - Credenciais válidas/inválidas, expiração de token
- **Testes de Autorização** - Acesso negado/concedido com base em funções
- **Testes de Filtro** - Filtros de segurança customizados e processamento de requisições
- **Tratamento de Exceções** - Respostas de exceções relacionadas à segurança
- **Gerenciamento de Sessão** - Criação, invalidação e timeout de sessão

---

## 🚀 Getting Started / Começando

### Running Tests / Executando Testes
```bash
# Run all security tests / Executar todos os testes de segurança
mvn test -Dtest="com.galafis.erp.security.*"

# Run authentication tests / Executar testes de autenticação
mvn test -Dtest="com.galafis.erp.security.authentication.*"

# Run JWT tests / Executar testes JWT
mvn test -Dtest="com.galafis.erp.security.jwt.*"
```

### Best Practices / Melhores Práticas
- Test both successful and failed authentication scenarios
- Validate authorization rules for all user roles
- Test JWT token lifecycle (creation, validation, expiration)
- Verify security headers in HTTP responses
- Test CSRF protection and CORS configuration
- Validate password encoding and security policies
- Use @WithMockUser for role-based testing
- Test security exception handling and error responses

---

*Created for Enterprise Resource Planning System*  
*Criado para o Sistema de Planejamento de Recursos Empresariais*
