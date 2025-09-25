# Templates Package / Pacote de Templates

**English** | **Português**

## English

### Purpose

The Templates package centralizes all view templates for the ERP application's web interface. This directory contains dynamic page templates that are processed by template engines like Thymeleaf, FreeMarker, or JSP to generate HTML responses for web browsers. Templates serve as the presentation layer, separating business logic from user interface concerns and enabling responsive, interactive web experiences.

**Key benefits:**
- **Separation of concerns**: Decouples presentation logic from business logic
- **Reusable layouts**: Common page structures shared across multiple views
- **Dynamic content**: Server-side rendering with data binding capabilities
- **Responsive design**: Mobile-first approach with flexible layouts
- **Accessibility**: WCAG compliance and screen reader support
- **Maintainability**: Organized structure for easy updates and modifications

### Template Organization Structure

#### Dashboard Templates
```
dashboard/
├── index.html              # Main dashboard overview
├── analytics.html          # Business analytics and reports
├── widgets/               # Reusable dashboard components
│   ├── revenue-chart.html
│   ├── user-stats.html
│   └── notifications.html
└── layouts/              # Dashboard-specific layouts
    └── dashboard-base.html
```

#### Forms and CRUD Operations
```
forms/
├── customer/
│   ├── create.html        # Customer creation form
│   ├── edit.html          # Customer edit form
│   └── list.html          # Customer listing with search
├── inventory/
│   ├── product-form.html  # Product management
│   └── stock-adjustment.html
└── financial/
    ├── invoice-form.html
    └── payment-form.html
```

#### Fragments and Components
```
fragments/
├── layout.html            # Base page layout
├── navigation.html        # Main navigation menu
├── sidebar.html           # Application sidebar
├── footer.html            # Common footer
├── modals.html            # Reusable modal dialogs
└── components/
    ├── data-table.html    # Sortable, filterable tables
    ├── form-fields.html   # Standardized form inputs
    └── alerts.html        # Success/error/warning messages
```

### Template Engine Examples

#### Thymeleaf Template Example
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="#{page.dashboard.title}">Dashboard</title>
    <link th:href="@{/css/dashboard.css}" rel="stylesheet">
</head>
<body>
    <div th:replace="~{fragments/navigation :: nav}"></div>
    
    <main class="dashboard-content">
        <h1 th:text="#{dashboard.welcome(${user.name})}">Welcome</h1>
        
        <div class="stats-grid">
            <div th:each="metric : ${metrics}" class="stat-card">
                <h3 th:text="${metric.title}"></h3>
                <p th:text="${metric.value}" class="stat-value"></p>
            </div>
        </div>
        
        <div th:replace="~{dashboard/widgets/revenue-chart :: chart}"></div>
    </main>
    
    <script th:src="@{/js/dashboard.js}"></script>
</body>
</html>
```

#### Form Template with Validation
```html
<form th:action="@{/customers}" th:object="${customer}" method="post">
    <div class="form-group">
        <label for="name" th:text="#{customer.name}">Name</label>
        <input type="text" id="name" th:field="*{name}" 
               class="form-control" th:classappend="${#fields.hasErrors('name')} ? 'is-invalid' : ''">
        <div th:if="${#fields.hasErrors('name')}" class="invalid-feedback">
            <p th:each="error : ${#fields.errors('name')}" th:text="${error}"></p>
        </div>
    </div>
    
    <div class="form-group">
        <label for="email" th:text="#{customer.email}">Email</label>
        <input type="email" id="email" th:field="*{email}" 
               class="form-control" th:classappend="${#fields.hasErrors('email')} ? 'is-invalid' : ''">
        <div th:if="${#fields.hasErrors('email')}" class="invalid-feedback">
            <p th:each="error : ${#fields.errors('email')}" th:text="${error}"></p>
        </div>
    </div>
    
    <button type="submit" class="btn btn-primary" th:text="#{button.save}">Save</button>
</form>
```

### Best Practices

#### Layout Separation
- **Base templates**: Create master layouts with common structure
- **Content blocks**: Define replaceable sections for page-specific content
- **Fragment inclusion**: Reuse common components across multiple pages
- **CSS organization**: Separate stylesheets for different sections

#### Data Context Management
```html
<!-- Pass data efficiently to templates -->
<div th:object="${customer}">
    <span th:text="*{name}"></span>
    <span th:text="*{email}"></span>
</div>

<!-- Use conditional rendering -->
<div th:if="${user.hasRole('ADMIN')}">
    <a th:href="@{/admin}">Admin Panel</a>
</div>

<!-- Iterate over collections -->
<table>
    <tr th:each="product : ${products}">
        <td th:text="${product.name}"></td>
        <td th:text="${#numbers.formatCurrency(product.price)}"></td>
    </tr>
</table>
```

#### Accessibility Guidelines
- **Semantic HTML**: Use proper heading hierarchy (h1-h6)
- **ARIA labels**: Add descriptive labels for screen readers
- **Keyboard navigation**: Ensure all interactive elements are accessible
- **Color contrast**: Maintain WCAG AA compliance
- **Alt text**: Provide meaningful descriptions for images

```html
<!-- Accessible form example -->
<div class="form-group">
    <label for="customer-search" class="sr-only">Search customers</label>
    <input type="search" id="customer-search" 
           placeholder="Search customers..." 
           aria-describedby="search-help">
    <small id="search-help">Search by name, email, or phone number</small>
</div>

<!-- Accessible table -->
<table role="table" aria-label="Customer list">
    <caption class="sr-only">List of customers with their details</caption>
    <thead>
        <tr role="row">
            <th role="columnheader" scope="col">Name</th>
            <th role="columnheader" scope="col">Email</th>
            <th role="columnheader" scope="col">Actions</th>
        </tr>
    </thead>
    <tbody>
        <tr role="row" th:each="customer : ${customers}">
            <td th:text="${customer.name}"></td>
            <td th:text="${customer.email}"></td>
            <td>
                <a th:href="@{/customers/{id}/edit(id=${customer.id})}" 
                   class="btn btn-sm btn-outline-primary"
                   th:attr="aria-label='Edit ' + ${customer.name}">Edit</a>
            </td>
        </tr>
    </tbody>
</table>
```

### Internationalization (i18n)
```html
<!-- Message properties usage -->
<h1 th:text="#{page.title.dashboard}">Dashboard</h1>
<p th:text="#{welcome.message(${user.name})}">Welcome message</p>

<!-- Conditional content based on locale -->
<div th:switch="${#locale.language}">
    <p th:case="'en'">English content</p>
    <p th:case="'pt'">Conteúdo em português</p>
    <p th:case="*">Default content</p>
</div>
```

### Onboarding Guide

1. **Understand the structure**: Review the template organization and naming conventions
2. **Learn template syntax**: Study Thymeleaf/FreeMarker documentation
3. **Create fragments**: Build reusable components for common UI elements
4. **Implement layouts**: Design responsive, mobile-first templates
5. **Test accessibility**: Validate templates with screen readers and accessibility tools
6. **Optimize performance**: Minimize template complexity and enable caching
7. **Document templates**: Comment complex logic and provide usage examples

### Performance Optimization
- **Template caching**: Enable caching in production environments
- **Lazy loading**: Load heavy components only when needed
- **Asset optimization**: Minify CSS/JS and optimize images
- **CDN integration**: Use content delivery networks for static assets

---

## Português

### Propósito

O pacote Templates centraliza todos os templates de visualização para a interface web da aplicação ERP. Este diretório contém templates de páginas dinâmicas que são processados por engines de template como Thymeleaf, FreeMarker ou JSP para gerar respostas HTML para navegadores web. Os templates servem como camada de apresentação, separando a lógica de negócio das preocupações de interface do usuário e habilitando experiências web responsivas e interativas.

**Principais benefícios:**
- **Separação de responsabilidades**: Desacopla lógica de apresentação da lógica de negócio
- **Layouts reutilizáveis**: Estruturas de página comuns compartilhadas entre múltiplas visualizações
- **Conteúdo dinâmico**: Renderização server-side com capacidades de binding de dados
- **Design responsivo**: Abordagem mobile-first com layouts flexíveis
- **Acessibilidade**: Conformidade WCAG e suporte a leitores de tela
- **Manutenibilidade**: Estrutura organizada para fáceis atualizações e modificações

### Estrutura de Organização de Templates

#### Templates de Dashboard
```
dashboard/
├── index.html              # Visão geral principal do dashboard
├── analytics.html          # Analytics de negócio e relatórios
├── widgets/               # Componentes reutilizáveis do dashboard
│   ├── revenue-chart.html
│   ├── user-stats.html
│   └── notifications.html
└── layouts/              # Layouts específicos do dashboard
    └── dashboard-base.html
```

#### Formulários e Operações CRUD
```
forms/
├── customer/
│   ├── create.html        # Formulário de criação de cliente
│   ├── edit.html          # Formulário de edição de cliente
│   └── list.html          # Listagem de clientes com busca
├── inventory/
│   ├── product-form.html  # Gestão de produtos
│   └── stock-adjustment.html
└── financial/
    ├── invoice-form.html
    └── payment-form.html
```

#### Fragmentos e Componentes
```
fragments/
├── layout.html            # Layout base da página
├── navigation.html        # Menu de navegação principal
├── sidebar.html           # Barra lateral da aplicação
├── footer.html            # Rodapé comum
├── modals.html            # Diálogos modais reutilizáveis
└── components/
    ├── data-table.html    # Tabelas ordenáveis e filtráveis
    ├── form-fields.html   # Campos de formulário padronizados
    └── alerts.html        # Mensagens de sucesso/erro/aviso
```

### Boas Práticas

#### Separação de Layout
- **Templates base**: Crie layouts mestres com estrutura comum
- **Blocos de conteúdo**: Defina seções substituíveis para conteúdo específico da página
- **Inclusão de fragmentos**: Reutilize componentes comuns em múltiplas páginas
- **Organização CSS**: Separe folhas de estilo para diferentes seções

#### Gestão de Contexto de Dados
- **Binding eficiente**: Passe dados de forma otimizada para templates
- **Renderização condicional**: Use lógica condicional para mostrar/ocultar elementos
- **Iteração**: Processe coleções de dados adequadamente
- **Formatação**: Use utilitários de formatação para datas, números e moedas

#### Diretrizes de Acessibilidade
- **HTML semântico**: Use hierarquia adequada de cabeçalhos (h1-h6)
- **Labels ARIA**: Adicione labels descritivos para leitores de tela
- **Navegação por teclado**: Assegure que todos elementos interativos sejam acessíveis
- **Contraste de cores**: Mantenha conformidade WCAG AA
- **Texto alternativo**: Forneça descrições significativas para imagens

### Guia de Onboarding

1. **Entenda a estrutura**: Revise a organização de templates e convenções de nomenclatura
2. **Aprenda sintaxe de template**: Estude documentação Thymeleaf/FreeMarker
3. **Crie fragmentos**: Construa componentes reutilizáveis para elementos UI comuns
4. **Implemente layouts**: Projete templates responsivos, mobile-first
5. **Teste acessibilidade**: Valide templates com leitores de tela e ferramentas de acessibilidade
6. **Otimize performance**: Minimize complexidade de templates e habilite cache
7. **Documente templates**: Comente lógica complexa e forneça exemplos de uso

### Customização e Extensibilidade

- **Herança de templates**: Use templates base para estruturas comuns
- **Composição**: Combine múltiplos fragmentos para criar páginas complexas
- **Tematização**: Implemente múltiplos temas com CSS variáveis
- **Localização**: Suporte a múltiplos idiomas com i18n
- **Componentes dinâmicos**: Crie componentes que se adaptam ao contexto

**Autor: Gabriel Demetrios Lafis**
