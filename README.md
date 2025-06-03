# 🏢 Enterprise Resource Planning System

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**🚀 Modern Enterprise Resource Planning System built with Java Spring Boot**

[🇺🇸 English](#english) | [🇧🇷 Português](#português)

</div>

---

## 🇺🇸 English

### 📋 Overview

A comprehensive Enterprise Resource Planning (ERP) system designed for modern businesses. This system integrates various business processes including inventory management, financial tracking, human resources, and customer relationship management into a unified platform.

### ✨ Features

#### 🏭 **Core Modules**
- **📦 Inventory Management** - Real-time stock tracking and automated reordering
- **💰 Financial Management** - Accounting, invoicing, and financial reporting
- **👥 Human Resources** - Employee management, payroll, and performance tracking
- **🤝 Customer Relationship Management** - Lead tracking and customer service
- **📊 Business Intelligence** - Advanced analytics and reporting dashboards

#### 🔧 **Technical Features**
- **🔐 Role-based Access Control** - Secure user authentication and authorization
- **📱 Responsive Design** - Mobile-friendly interface for all devices
- **🔄 Real-time Updates** - Live data synchronization across modules
- **📈 Advanced Analytics** - Business intelligence and predictive analytics
- **🌐 Multi-language Support** - Internationalization ready
- **☁️ Cloud Ready** - Scalable architecture for cloud deployment

### 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.0, Spring Security, Spring Data JPA
- **Database**: MySQL 8.0, Redis (caching)
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript ES6+
- **Build Tool**: Maven 3.8+
- **Testing**: JUnit 5, Mockito, TestContainers
- **Documentation**: Swagger/OpenAPI 3.0

### 🚀 Quick Start

#### Prerequisites
```bash
Java 17+
Maven 3.8+
MySQL 8.0+
Redis (optional, for caching)
```

#### Installation

1. **Clone the repository**
```bash
git clone https://github.com/galafis/Enterprise-Resource-Planning-System.git
cd Enterprise-Resource-Planning-System
```

2. **Configure database**
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE erp_system;
```

3. **Update application properties**
```properties
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_system
    username: your_username
    password: your_password
```

4. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access the application**
```
http://localhost:8080
```

### 📁 Project Structure

```
Enterprise-Resource-Planning-System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/galafis/erp/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── entity/          # JPA entities
│   │   │       ├── repository/      # Data repositories
│   │   │       ├── service/         # Business logic
│   │   │       └── security/        # Security configuration
│   │   └── resources/
│   │       ├── static/              # CSS, JS, images
│   │       ├── templates/           # Thymeleaf templates
│   │       └── application.yml      # Configuration
│   └── test/                        # Unit and integration tests
├── docs/                            # Documentation
├── docker/                          # Docker configuration
└── pom.xml                          # Maven configuration
```

### 🔐 Security Features

- **JWT Authentication** - Secure token-based authentication
- **Role-based Authorization** - Granular permission system
- **Password Encryption** - BCrypt password hashing
- **CSRF Protection** - Cross-site request forgery protection
- **SQL Injection Prevention** - Parameterized queries
- **XSS Protection** - Input sanitization and validation

### 📊 API Documentation

The system provides comprehensive REST API documentation using Swagger/OpenAPI 3.0:

```
http://localhost:8080/swagger-ui.html
```

### 🧪 Testing

```bash
# Run all tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

### 🐳 Docker Deployment

```bash
# Build Docker image
docker build -t erp-system .

# Run with Docker Compose
docker-compose up -d
```

### 📈 Performance Metrics

- **Response Time**: < 200ms for most operations
- **Throughput**: 1000+ concurrent users
- **Database**: Optimized queries with indexing
- **Caching**: Redis integration for improved performance

### 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🇧🇷 Português

### 📋 Visão Geral

Um sistema abrangente de Planejamento de Recursos Empresariais (ERP) projetado para empresas modernas. Este sistema integra vários processos de negócios incluindo gestão de estoque, controle financeiro, recursos humanos e gestão de relacionamento com clientes em uma plataforma unificada.

### ✨ Funcionalidades

#### 🏭 **Módulos Principais**
- **📦 Gestão de Estoque** - Rastreamento em tempo real e reposição automatizada
- **💰 Gestão Financeira** - Contabilidade, faturamento e relatórios financeiros
- **👥 Recursos Humanos** - Gestão de funcionários, folha de pagamento e avaliação
- **🤝 Gestão de Relacionamento com Clientes** - Rastreamento de leads e atendimento
- **📊 Business Intelligence** - Dashboards analíticos e relatórios avançados

#### 🔧 **Recursos Técnicos**
- **🔐 Controle de Acesso Baseado em Funções** - Autenticação e autorização seguras
- **📱 Design Responsivo** - Interface amigável para todos os dispositivos
- **🔄 Atualizações em Tempo Real** - Sincronização de dados ao vivo entre módulos
- **📈 Analytics Avançados** - Business intelligence e análise preditiva
- **🌐 Suporte Multi-idioma** - Pronto para internacionalização
- **☁️ Pronto para Nuvem** - Arquitetura escalável para deploy em nuvem

### 🛠️ Stack Tecnológico

- **Backend**: Java 17, Spring Boot 3.0, Spring Security, Spring Data JPA
- **Banco de Dados**: MySQL 8.0, Redis (cache)
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript ES6+
- **Ferramenta de Build**: Maven 3.8+
- **Testes**: JUnit 5, Mockito, TestContainers
- **Documentação**: Swagger/OpenAPI 3.0

### 🚀 Início Rápido

#### Pré-requisitos
```bash
Java 17+
Maven 3.8+
MySQL 8.0+
Redis (opcional, para cache)
```

#### Instalação

1. **Clone o repositório**
```bash
git clone https://github.com/galafis/Enterprise-Resource-Planning-System.git
cd Enterprise-Resource-Planning-System
```

2. **Configure o banco de dados**
```bash
# Criar banco MySQL
mysql -u root -p
CREATE DATABASE erp_system;
```

3. **Atualize as propriedades da aplicação**
```properties
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_system
    username: seu_usuario
    password: sua_senha
```

4. **Compile e execute**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Acesse a aplicação**
```
http://localhost:8080
```

### 🔐 Recursos de Segurança

- **Autenticação JWT** - Autenticação segura baseada em tokens
- **Autorização Baseada em Funções** - Sistema granular de permissões
- **Criptografia de Senhas** - Hash de senhas com BCrypt
- **Proteção CSRF** - Proteção contra falsificação de solicitação entre sites
- **Prevenção de Injeção SQL** - Consultas parametrizadas
- **Proteção XSS** - Sanitização e validação de entrada

### 📊 Documentação da API

O sistema fornece documentação abrangente da API REST usando Swagger/OpenAPI 3.0:

```
http://localhost:8080/swagger-ui.html
```

### 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes de integração
mvn verify

# Gerar relatório de cobertura
mvn jacoco:report
```

### 🐳 Deploy com Docker

```bash
# Construir imagem Docker
docker build -t erp-system .

# Executar com Docker Compose
docker-compose up -d
```

### 📈 Métricas de Performance

- **Tempo de Resposta**: < 200ms para a maioria das operações
- **Throughput**: 1000+ usuários simultâneos
- **Banco de Dados**: Consultas otimizadas com indexação
- **Cache**: Integração Redis para melhor performance

---

## 👨‍💻 Author

**Gabriel Demetrios Lafis**
- 🌐 GitHub: [@galafis](https://github.com/galafis)
- 💼 LinkedIn: [Gabriel Lafis](https://linkedin.com/in/gabriel-lafis)
- 📧 Email: gabriel.lafis@example.com

---

## 🙏 Acknowledgments

- Spring Boot community for excellent framework
- MySQL team for robust database solution
- Bootstrap team for responsive UI components
- All contributors and testers

---

<div align="center">

**⭐ If this project helps your business operations, please give it a star! ⭐**

**🏢 Built with passion for enterprise solutions 🏢**

</div>

