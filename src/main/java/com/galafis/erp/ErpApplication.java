package com.galafis.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enterprise Resource Planning System
 * Main Application Class
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class ErpApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }
}
