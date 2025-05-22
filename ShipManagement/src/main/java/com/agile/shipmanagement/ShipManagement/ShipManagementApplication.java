package com.agile.shipmanagement.ShipManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.agile.shipmanagement.ShipManagement.repository")
@EntityScan(basePackages = "com.agile.shipmanagement.ShipManagement.model")
@EnableJpaAuditing
public class ShipManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipManagementApplication.class, args);
    }

}
