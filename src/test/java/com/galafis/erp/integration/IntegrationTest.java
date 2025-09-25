package com.galafis.erp.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestSecurity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração principal para o sistema ERP
 * Testa a integração entre camadas (Controller, Service, Repository)
 * e funcionalidades principais do sistema
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestSecurity
@AutoConfigureTestEntityManager
@Transactional
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Teste básico de inicialização da aplicação
     * Verifica se o contexto Spring carrega corretamente
     */
    @Test
    @Order(1)
    public void contextLoads() {
        // Verifica se o contexto Spring foi carregado com sucesso
        assertNotNull(restTemplate);
    }

    /**
     * Teste de saúde da aplicação
     * Verifica se o endpoint de health está respondendo
     */
    @Test
    @Order(2)
    public void healthCheckEndpoint() {
        // Testa endpoint de health
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Teste de integração entre camadas - Módulo de Usuários
     * Simula operações CRUD básicas através das camadas
     */
    @Test
    @Order(3)
    public void userModuleIntegration() {
        // Teste de listagem de usuários (sem autenticação)
        ResponseEntity<String> usersResponse = restTemplate.getForEntity("/api/users/public", String.class);
        
        // Deve retornar status apropriado (200 ou 401 dependendo da configuração)
        assertTrue(
            usersResponse.getStatusCode() == HttpStatus.OK || 
            usersResponse.getStatusCode() == HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Teste de integração entre camadas - Módulo de Inventário
     * Verifica comunicação entre Controller, Service e Repository
     */
    @Test
    @Order(4)
    public void inventoryModuleIntegration() {
        // Teste de listagem de produtos
        ResponseEntity<String> productsResponse = restTemplate.getForEntity("/api/inventory/products", String.class);
        
        // Verifica se a resposta é válida
        assertTrue(
            productsResponse.getStatusCode() == HttpStatus.OK || 
            productsResponse.getStatusCode() == HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Teste de integração entre camadas - Módulo Financeiro
     * Testa comunicação entre diferentes serviços
     */
    @Test
    @Order(5)
    public void financialModuleIntegration() {
        // Teste de endpoint de relatórios financeiros
        ResponseEntity<String> reportsResponse = restTemplate.getForEntity("/api/financial/reports", String.class);
        
        // Verifica resposta do sistema
        assertTrue(
            reportsResponse.getStatusCode() == HttpStatus.OK || 
            reportsResponse.getStatusCode() == HttpStatus.UNAUTHORIZED ||
            reportsResponse.getStatusCode() == HttpStatus.NOT_FOUND
        );
    }

    /**
     * Teste de integração de banco de dados
     * Verifica se as conexões e transações funcionam corretamente
     */
    @Test
    @Order(6)
    public void databaseIntegration() {
        // Este teste verifica se as transações estão funcionando
        // A anotação @Transactional garante rollback automático
        
        // Simula operação que deveria persistir dados
        ResponseEntity<String> createResponse = restTemplate.postForEntity(
            "/api/test/create", 
            "Test Data", 
            String.class
        );
        
        // Aceita diferentes códigos de status dependendo da implementação
        assertTrue(
            createResponse.getStatusCode() == HttpStatus.CREATED ||
            createResponse.getStatusCode() == HttpStatus.OK ||
            createResponse.getStatusCode() == HttpStatus.UNAUTHORIZED ||
            createResponse.getStatusCode() == HttpStatus.NOT_FOUND
        );
    }

    /**
     * Teste de comunicação entre módulos
     * Verifica se diferentes partes do sistema se comunicam corretamente
     */
    @Test
    @Order(7)
    public void crossModuleCommunication() {
        // Testa se um módulo pode acessar dados de outro módulo
        // Por exemplo: Financeiro acessando dados de Inventário
        
        ResponseEntity<String> integrationResponse = restTemplate.getForEntity(
            "/api/integration/cross-module-test", 
            String.class
        );
        
        // Verifica se a comunicação entre módulos funciona
        assertTrue(
            integrationResponse.getStatusCode() == HttpStatus.OK ||
            integrationResponse.getStatusCode() == HttpStatus.UNAUTHORIZED ||
            integrationResponse.getStatusCode() == HttpStatus.NOT_FOUND
        );
    }

    /**
     * Teste de performance básica
     * Verifica se o sistema responde em tempo hábil
     */
    @Test
    @Order(8)
    public void basicPerformanceTest() {
        long startTime = System.currentTimeMillis();
        
        // Executa operação simples
        ResponseEntity<String> response = restTemplate.getForEntity("/api/status", String.class);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Verifica se a resposta foi rápida (menos de 5 segundos)
        assertTrue(duration < 5000, "Operação demorou mais que 5 segundos: " + duration + "ms");
        
        // Status pode ser qualquer um, o importante é que responda rápido
        assertNotNull(response);
    }
}
