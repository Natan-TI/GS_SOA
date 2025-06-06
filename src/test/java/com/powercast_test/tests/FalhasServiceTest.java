package com.powercast_test.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.powercast.PowerCastApplication;
import com.powercast.falhas.dto.FalhaDto;
import com.powercast.falhas.repository.FalhasRepository;
import com.powercast.falhas.service.FalhasService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = PowerCastApplication.class)
public class FalhasServiceTest {
    @Autowired
    private FalhasRepository repo;

    private FalhasService service;

    @BeforeEach
    void setup() {
        repo.deleteAll();
        service = new FalhasService(repo);
    }

    @Test
    void testSalvarEBuscar() {
        FalhaDto dto = FalhaDto.builder()
            .bairro("TestBairro")
            .descricao("Teste desc")
            .build();
        FalhaDto saved = service.salvar(dto);
        assertNotNull(saved.getId());

        List<FalhaDto> list = service.buscaPorBairro("TestBairro");
        assertEquals(1, list.size());
    }
}
