# Controller Package / Pacote de Controllers

English | Português

---

## English

### Purpose
The controllers expose the ERP system APIs via REST and/or SOAP endpoints. They act as the presentation layer for services and use cases, handling HTTP/SOAP requests, validating input, orchestrating service calls, and producing standardized responses and error handling.

Key responsibilities:
- Define API contracts (URLs, methods, payloads, status codes)
- Delegate business logic to services and facades
- Map domain/data transfer objects (DTOs) to I/O models
- Enforce validation, authentication, and authorization at the edge
- Emit consistent responses and errors (problem details)

### Typical Structure of a REST Controller
```java
package com.galafis.erp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Example of a typical REST controller
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse body = productService.findById(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse body = productService.create(request);
        return ResponseEntity.status(201).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse body = productService.update(id, request);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### SOAP Controller (Endpoint) Sketch
```java
// Pseudocode with Spring-WS
@Endpoint
public class OrderEndpoint {
    private static final String NAMESPACE = "http://galafis.com/erp/orders";

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetOrderRequest")
    @ResponsePayload
    public GetOrderResponse getOrder(@RequestPayload GetOrderRequest request) {
        // validate, call service, map response
        return response;
    }
}
```

### Best Practices
- Standardized responses:
  - Use ResponseEntity with clear status codes
  - Consistent envelope (data, meta, errors) if applicable
  - Return Problem Details (RFC 7807) for errors where possible
- Validation and security:
  - Bean Validation on request DTOs (@Valid, constraints)
  - Method-level security annotations (@PreAuthorize) when needed
  - Avoid leaking internal error details
- Exception handling:
  - Centralize with @ControllerAdvice and @ExceptionHandler
  - Map domain exceptions to HTTP status (e.g., NotFound -> 404)
  - Log with correlation IDs/trace IDs
- Versioning and compatibility:
  - Prefix routes (e.g., /api/v1)
  - Avoid breaking changes; introduce new versions
- Pagination and filtering:
  - Support page, size, sort; validate limits
  - Use 206 Partial Content or metadata in response
- Documentation:
  - Annotate endpoints for OpenAPI/Swagger
  - Keep examples up to date

### Example: Standard Error Model (Problem Details)
```json
{
  "type": "https://galafis.com/errors/resource-not-found",
  "title": "Resource not found",
  "status": 404,
  "detail": "Product 123 not found",
  "instance": "/api/v1/products/123",
  "traceId": "c5f8..."
}
```

### Onboarding for New Developers
1. Prerequisites:
   - Spring MVC / Spring WebFlux (for REST) and/or Spring-WS (for SOAP)
   - Bean Validation (Jakarta Validation)
   - Understanding of DTOs, mapping, and error handling
2. Getting started:
   - Review existing controllers (e.g., DashboardController) and service interfaces
   - Follow package conventions: controller -> service -> repository
   - Use request/response DTOs; do not expose entities directly
3. Local testing:
   - Run the application and test with HTTP client (cURL, Postman)
   - Validate OpenAPI docs if available (e.g., /swagger-ui)
4. Code review checklist:
   - Input validation and null-safety
   - Proper status codes and idempotency
   - Error mapping via ControllerAdvice
   - Security annotations/scopes present where needed

---

## Português

### Propósito
Os controllers expõem as APIs do ERP via endpoints REST e/ou SOAP. Eles atuam como a camada de apresentação para serviços e casos de uso, recebendo requisições, validando entradas, orquestrando chamadas de serviço e produzindo respostas padronizadas com tratamento de erros.

Responsabilidades principais:
- Definir contratos de API (URLs, métodos, payloads, códigos de status)
- Delegar a lógica de negócio para serviços e fachadas
- Mapear DTOs de domínio para modelos de entrada/saída
- Aplicar validação, autenticação e autorização na borda
- Emitir respostas e erros consistentes (problem details)

### Estrutura Típica de um Controller REST
```java
package com.galafis.erp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        ProdutoResponse body = produtoService.buscarPorId(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoCriarRequest request) {
        ProdutoResponse body = produtoService.criar(request);
        return ResponseEntity.status(201).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ProdutoAtualizarRequest request) {
        ProdutoResponse body = produtoService.atualizar(id, request);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Esboço de Controller SOAP (Endpoint)
```java
@Endpoint
public class PedidoEndpoint {
    private static final String NAMESPACE = "http://galafis.com/erp/pedidos";

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetPedidoRequest")
    @ResponsePayload
    public GetPedidoResponse getPedido(@RequestPayload GetPedidoRequest request) {
        return response;
    }
}
```

### Boas Práticas
- Respostas padronizadas:
  - Use ResponseEntity com códigos claros
  - Envelope consistente (data, meta, errors) quando aplicável
  - Retornar Problem Details (RFC 7807) para erros quando possível
- Validação e segurança:
  - Bean Validation em DTOs de request (@Valid, constraints)
  - Anotações de segurança (@PreAuthorize) quando necessário
  - Evitar vazar detalhes internos de erros
- Tratamento de exceções:
  - Centralizar com @ControllerAdvice e @ExceptionHandler
  - Mapear exceções de domínio para HTTP adequado (ex.: NotFound -> 404)
  - Logar com IDs de correlação/trace
- Versionamento e compatibilidade:
  - Prefixar rotas (ex.: /api/v1)
  - Evitar breaking changes; introduzir novas versões
- Paginação e filtragem:
  - Suportar page, size, sort; validar limites
  - Usar 206 Partial Content ou metadados na resposta
- Documentação:
  - Anotar endpoints para OpenAPI/Swagger
  - Manter exemplos atualizados

### Exemplo: Modelo de Erro Padronizado (Problem Details)
```json
{
  "type": "https://galafis.com/errors/recurso-nao-encontrado",
  "title": "Recurso não encontrado",
  "status": 404,
  "detail": "Produto 123 não encontrado",
  "instance": "/api/v1/produtos/123",
  "traceId": "c5f8..."
}
```

### Onboarding para Novos Desenvolvedores
1. Pré-requisitos:
   - Spring MVC / Spring WebFlux (REST) e/ou Spring-WS (SOAP)
   - Bean Validation (Jakarta Validation)
   - Entendimento de DTOs, mapeamento e tratamento de erros
2. Começando:
   - Revisar controllers existentes (ex.: DashboardController) e interfaces de serviço
   - Seguir convenções de camadas: controller -> service -> repository
   - Usar DTOs de request/response; não expor entidades diretamente
3. Testes locais:
   - Rodar a aplicação e testar com cliente HTTP (cURL, Postman)
   - Validar docs OpenAPI se disponíveis (ex.: /swagger-ui)
4. Checklist de code review:
   - Validação de entrada e null-safety
   - Códigos de status e idempotência corretos
   - Mapeamento de erros via ControllerAdvice
   - Anotações/escopos de segurança quando necessário

---

Author / Autor: Gabriel Demetrios Lafis
This documentation is maintained as part of the Enterprise Resource Planning System project.
Esta documentação é mantida como parte do projeto do Sistema de Planejamento de Recursos Empresariais.
