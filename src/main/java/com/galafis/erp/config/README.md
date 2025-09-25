# Config Package / Pacote de Configuração

English | Português

## English

### Purpose
The config package centralizes Spring Boot application configuration. It defines application beans, data sources, security rules, CORS, and environment profile wiring to keep concerns separated from business logic and ensure consistent, testable setup across environments.

Key areas:
- Beans: reusable infrastructure components (ObjectMapper, ModelMapper, PasswordEncoder, Clock, RestTemplate/WebClient)
- Data sources: primary and optional read replicas; JPA/Hibernate settings
- Security: HTTP security filter chain, authentication providers, password encoding, method security
- CORS: allowed origins, methods, headers; per-path customization
- Environment profiles: dev, test, prod; conditional beans via @Profile; property externalization

### Typical Files
- Config.java (general beans):
  - PasswordEncoder bean (BCrypt)
  - ObjectMapper customization (JavaTimeModule, snake_case if needed)
  - Clock.systemUTC() for time consistency
  - WebClient.Builder or RestTemplate with timeouts
- SecurityConfig.java (security):
  - SecurityFilterChain with stateless session
  - CSRF disabled/enabled depending on use (enable for browser forms)
  - Authorization per path (e.g., /api/admin/** requires ROLE_ADMIN)
  - AuthenticationManager and PasswordEncoder
  - CORS customization when acting as an API for SPAs

Example: SecurityConfig (simplified)
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
```

Example: General Config (beans)
```java
@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    }

    @Bean
    public Clock clock() { return Clock.systemUTC(); }
}
```

### Best Practices
- Single responsibility per config class: split SecurityConfig, WebConfig, DataConfig, CacheConfig
- Externalize settings: application.yml + application-{profile}.yml; never hardcode secrets
- Use @ConfigurationProperties for typed, testable properties
- Prefer constructor injection for config collaborators
- Make time deterministic using Clock bean; avoid direct Instant.now() in code
- CORS: restrict to known origins in prod; allow credentials only if needed
- Profiles: use @Profile("dev") for H2, verbose logging; @Profile("prod") for real DB and caches
- Keep security rules explicit and least-privilege; guard admin endpoints and actuator
- Document defaults and environment variables in this README

### Onboarding for New Developers
1) Read SecurityConfig.java to understand protected routes and authentication
2) Check application.yml and per-profile overrides for DB, CORS, ports
3) Verify beans exposed by AppConfig and how services consume them
4) Run the app with a profile, e.g.: `SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run`
5) For tests, prefer @DataJpaTest/@SpringBootTest with test profile and Testcontainers

Author: Gabriel Demetrios Lafis

---

## Português

### Propósito
O pacote config centraliza a configuração da aplicação Spring Boot. Ele define beans, datasources, regras de segurança, CORS e perfis de ambiente para manter a separação de responsabilidades e garantir uma configuração consistente e testável entre ambientes.

Áreas principais:
- Beans: componentes de infraestrutura reutilizáveis (ObjectMapper, ModelMapper, PasswordEncoder, Clock, RestTemplate/WebClient)
- Datasources: primário e opcionais réplicas de leitura; ajustes de JPA/Hibernate
- Segurança: cadeia de filtros HTTP, provedores de autenticação, codificação de senha, segurança por método
- CORS: origens, métodos e headers permitidos; customização por caminho
- Perfis de ambiente: dev, test, prod; beans condicionais com @Profile; externalização de propriedades

Arquivos típicos
- Config.java (beans gerais):
  - Bean de PasswordEncoder (BCrypt)
  - Customização do ObjectMapper (JavaTimeModule, snake_case se necessário)
  - Clock.systemUTC() para consistência temporal
  - WebClient.Builder ou RestTemplate com timeouts
- SecurityConfig.java (segurança):
  - SecurityFilterChain com sessão stateless
  - CSRF desabilitado/habilitado conforme uso (habilitar para formulários web)
  - Autorização por caminho (ex.: /api/admin/** requer ROLE_ADMIN)
  - AuthenticationManager e PasswordEncoder
  - CORS customizado quando atuando como API para SPAs

Exemplo: SecurityConfig (simplificado)
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
```

Exemplo: Configuração Geral (beans)
```java
@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    }

    @Bean
    public Clock clock() { return Clock.systemUTC(); }
}
```

### Boas Práticas
- Responsabilidade única por classe de configuração: SecurityConfig, WebConfig, DataConfig, CacheConfig
- Externalize configurações: application.yml + application-{profile}.yml; nunca versionar segredos
- Use @ConfigurationProperties para propriedades tipadas e testáveis
- Prefira injeção por construtor
- Tempo determinístico com bean Clock; evite Instant.now() direto no código
- CORS: restrinja a origens conhecidas em produção; permita credenciais só quando necessário
- Perfis: use @Profile("dev") para H2 e logs verbosos; @Profile("prod") para banco real e caches
- Regras de segurança explícitas e de menor privilégio; proteja endpoints administrativos e actuator
- Documente padrões e variáveis de ambiente neste README

### Onboarding Multilíngue
1) Leia o SecurityConfig.java para entender rotas protegidas e autenticação
2) Confira application.yml e overrides por perfil para DB, CORS e portas
3) Verifique os beans expostos por AppConfig e como os serviços os consomem
4) Rode a aplicação com um perfil: `SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run`
5) Para testes, use @DataJpaTest/@SpringBootTest com profile de teste e Testcontainers

Autor: Gabriel Demetrios Lafis
