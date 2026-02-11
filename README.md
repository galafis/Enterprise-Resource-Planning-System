# 🏢 Enterprise Resource Planning System

## 🖼️ Imagem Hero

<p align="center">
  <img src="./hero_image.png" alt="Enterprise Resource Planning System Hero Image" width="700"/>
</p>

<p align="center">
</p>

<p align="center"><strong>🚀 Modern Enterprise Resource Planning System built with Java Spring Boot</strong></p>

<p align="center">
  <a href="#english">🇺🇸 English</a> |
  <a href="#português">🇧🇷 Português</a>
</p>

---

## 🇺🇸 English

### 📋 Overview
A comprehensive Enterprise Resource Planning (ERP) system designed for modern businesses. This system integrates inventory management, financial tracking, human resources, CRM, and analytics into a unified platform.

### ✨ Features
#### 🏭 Core Modules
- 📦 Inventory Management – Real-time stock tracking and automated reordering
- 💰 Financial Management – Accounting, invoicing, and financial reporting
- 👥 Human Resources – Employee management, payroll, performance tracking
- 🤝 CRM – Lead management and customer support
- 📊 Business Intelligence – Analytics dashboards and reporting

#### 🔧 Technical Features
- 🔐 Role-based Access Control (JWT + Spring Security)
- 📱 Responsive UI (Thymeleaf + Bootstrap)
- 🔄 Real-time updates (where applicable)
- 🌐 i18n ready (multi-language)
- ☁️ Cloud-ready, containerized deployment
- 📈 Observability (Spring Boot Actuator)

### 🛠️ Technology Stack
- Backend: Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA, Validation
- Database: MySQL 8.0; H2 (tests); Redis (optional caching)
- Frontend: Thymeleaf, Bootstrap 5, WebJars
- Build: Maven 3.8+
- Testing: JUnit 5, Mockito, Testcontainers, Jacoco
- Docs: springdoc-openapi (Swagger UI)

### 🚀 Quick Start
#### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Redis (optional, for caching)

#### Installation
1) Clone the repository
```bash
git clone https://github.com/galafis/Enterprise-Resource-Planning-System.git
cd Enterprise-Resource-Planning-System
```
2) Create database
```sql
-- MySQL
CREATE DATABASE erp_system;
```
3) Configure application properties
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_system
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  redis:
    host: localhost
    port: 6379
  profiles:
    active: dev

jwt:
  secret: change_me_in_production
```
4) Build and run
```bash
mvn clean install
mvn spring-boot:run
```
5) Access the app
```
http://localhost:8080
```

### 📁 Project Structure
```
Enterprise-Resource-Planning-System/
├── src/
│   ├── main/
│   │   ├── java/com/galafis/erp/
│   │   │   ├── config/        # Configuration classes
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── entity/        # JPA entities
│   │   │   ├── repository/    # Data repositories
│   │   │   ├── security/      # Security (JWT, filters)
│   │   │   └── service/       # Business logic
│   │   └── resources/
│   │       ├── static/        # CSS, JS, images
│   │       ├── templates/     # Thymeleaf templates
│   │       └── application.yml
│   └── test/                  # Unit and integration tests
├── docker/                    # Docker configs, compose files, scripts
├── docs/                      # Documentation center (API, guides, architecture)
├── pom.xml                    # Maven configuration
└── Dockerfile                 # Dev image
```

### 🔐 Security
- JWT Authentication (jjwt 0.12.x)
- Role-based Authorization
- Password hashing (BCrypt)
- CSRF protection (where applicable)
- SQL Injection prevention (parameterized queries)
- XSS protection (input validation)

### 📊 API Documentation
Swagger UI (springdoc-openapi):
```
http://localhost:8080/swagger-ui.html
```

### 🧪 Testing
```bash
# Run all tests
mvn test

# Run integration tests
mvn verify

# Generate coverage report
mvn jacoco:report
```

### 🐳 Docker
Development and production configurations are provided under ./docker and root compose files.
```bash
# Build image
docker build -t erp-system .

# Development stack
docker-compose up -d

# Production stack
docker-compose -f docker-compose.prod.yml up -d
```
See docker/README.md for full details (services: erp-app, mysql, redis, nginx).

### 📈 Performance & Metrics
- Response time: < 200ms typical
- Concurrency: 1000+ users (reference setup)
- DB: Indexed queries
- Cache: Redis integration
- Actuator: /actuator/health, /actuator/metrics, /actuator/prometheus

### 🤝 Contributing
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'feat: Add amazing feature'`
4. Push: `git push origin feature/amazing-feature`
5. Open a Pull Request

### 📄 License
MIT License — see [LICENSE](LICENSE)

---

## 🇧🇷 Português

### 📋 Visão Geral
Sistema ERP abrangente para empresas modernas. Integra módulos de estoque, finanças, RH, CRM e analytics em uma plataforma unificada.

### ✨ Funcionalidades
#### 🏭 Módulos Principais
- 📦 Gestão de Estoque – Rastreamento em tempo real e reposição automática
- 💰 Gestão Financeira – Contabilidade, faturamento e relatórios
- 👥 Recursos Humanos – Gestão de funcionários, folha, desempenho
- 🤝 CRM – Gestão de leads e suporte ao cliente
- 📊 Business Intelligence – Dashboards e relatórios

#### 🔧 Recursos Técnicos
- 🔐 RBAC (JWT + Spring Security)
- 📱 UI responsiva (Thymeleaf + Bootstrap)
- 🔄 Atualizações em tempo real (quando aplicável)
- 🌐 Pronto para i18n
- ☁️ Pronto para nuvem (containers)
- 📈 Observabilidade (Actuator)

### 🛠️ Stack Tecnológico
- Backend: Java 17, Spring Boot 3.2, Security, Data JPA, Validation
- Banco: MySQL 8.0; H2 (testes); Redis (cache opcional)
- Frontend: Thymeleaf, Bootstrap 5, WebJars
- Build: Maven 3.8+
- Testes: JUnit 5, Mockito, Testcontainers, Jacoco
- Docs: springdoc-openapi (Swagger UI)

### 🚀 Início Rápido
#### Pré-requisitos
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Redis (opcional, cache)

#### Instalação
1) Clonar o repositório
```bash
git clone https://github.com/galafis/Enterprise-Resource-Planning-System.git
cd Enterprise-Resource-Planning-System
```
2) Criar banco
```sql
CREATE DATABASE erp_system;
```
3) Configurar propriedades
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_system
    username: seu_usuario
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  redis:
    host: localhost
    port: 6379
  profiles:
    active: dev

jwt:
  secret: troque_no_producao
```
4) Compilar e executar
```bash
mvn clean install
mvn spring-boot:run
```
5) Acessar a aplicação
```
http://localhost:8080
```

### 📁 Estrutura do Projeto
(veja acima — igual)

### 🔐 Segurança
- Autenticação JWT (jjwt 0.12.x)
- Autorização por Função
- Hash de senha (BCrypt)
- Proteção CSRF (quando aplicável)
- Prevenção de SQL Injection (consultas parametrizadas)
- Proteção XSS (validação de entrada)

### 📊 Documentação da API
```
http://localhost:8080/swagger-ui.html
```

### 🧪 Testes
```bash
mvn test
mvn verify
mvn jacoco:report
```

### 🐳 Docker
Vide docker/README.md e os arquivos docker-compose*.yml.
```bash
docker build -t erp-system .
docker-compose up -d
# Produção
docker-compose -f docker-compose.prod.yml up -d
```

### 📈 Métricas & Performance
- Tempo de resposta: < 200ms (típico)
- Concorrência: 1000+ usuários (referência)
- BD: Índices em consultas
- Cache: Integração Redis
- Actuator: /actuator/health, /actuator/metrics, /actuator/prometheus

### 🤝 Contribuição
1. Fork
2. Branch: `git checkout -b feature/recurso`
3. Commits: `git commit -m 'feat: adicionar recurso'`
4. Push: `git push origin feature/recurso`
5. PR

### 📄 Licença
MIT — ver [LICENSE](LICENSE)

---

### 👨‍💻 Author
Gabriel Demetrios Lafis  •  GitHub: [@galafis](https://github.com/galafis)  •  LinkedIn: [Gabriel Lafis](https://linkedin.com/in/gabriel-lafis)

---

- Build: GitHub Actions (CI)
- Coverage: JaCoCo
- Quality: SonarCloud
- Containers: Docker

### 💡 Documentation improvements
- Link to docs center: see `/docs` for API, guides, and architecture
- Add Postman collection under `/docs/api/postman`
- Add CHANGELOG under `/docs/changelog`

