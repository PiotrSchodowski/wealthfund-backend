package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = WealthFundApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
abstract class TestConfig {

    @Container
    @ServiceConnection
    static final MySQLContainer mySQLContainer = configureMySQLContainer();


    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        mySQLContainer.start();
        registry.add("spring.datasource.url", () -> String.format("jdbc:tc:mysql://localhost:%s/%s", mySQLContainer.getFirstMappedPort(), mySQLContainer.getDatabaseName()));
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
    }


    private static MySQLContainer configureMySQLContainer() {
        return new MySQLContainer(DockerImageName.parse("mysql:latest"))
                .withDatabaseName("q")
                .withUsername("w")
                .withPassword("e");
    }
}

