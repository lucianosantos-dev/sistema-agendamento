package com.lucianodev.sistemaagendamento;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
class SistemaagendamentoApplicationTests {

    @Test
    void contextLoads() {
    }

}
