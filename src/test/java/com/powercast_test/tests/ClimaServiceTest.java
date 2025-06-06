package com.powercast_test.tests;

import com.powercast.PowerCastApplication;
import com.powercast.clima.dto.ClimaDto;
import com.powercast.clima.model.ClimaResponse;
import com.powercast.clima.service.ClimaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PowerCastApplication.class)
public class ClimaServiceTest {
    @Autowired
    private ClimaService climaService;

	@MockBean
    private RestTemplate restTemplate;

    @Test
    void testBuscarPorCidade() {
        ClimaResponse resp = new ClimaResponse();
        var main = new ClimaResponse.Main(); main.setTemp(15.5); main.setHumidity(30.0);
        var weather = new ClimaResponse.Weather(); weather.setDescription("sunny");
        var wind = new ClimaResponse.Wind(); wind.setSpeed(3.0);
        resp.setMain(main);
        resp.setWeather(List.of(weather));
        resp.setWind(wind);

        Mockito.when(
            restTemplate.getForObject(
                Mockito.anyString(), Mockito.eq(ClimaResponse.class)
            )
        ).thenReturn(resp);

        ClimaDto dto = climaService.buscarPorCidade("AnyCity");
        assertEquals(15.5, dto.getTemperatura());
        assertEquals(30.0, dto.getUmidade());
        assertEquals("sunny", dto.getDescricao());
        assertEquals(3.0, dto.getVelocidadeVento());
    }
}