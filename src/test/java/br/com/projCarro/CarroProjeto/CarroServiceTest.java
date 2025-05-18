package br.com.projCarro.CarroProjeto;

import br.com.projCarro.CarroProjeto.entity.Carro;
import br.com.projCarro.CarroProjeto.entity.Marca;
import br.com.projCarro.CarroProjeto.repository.CarroRepository;
import br.com.projCarro.CarroProjeto.service.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
public class CarroServiceTest {

    @Autowired
    CarroService carroService;

    @MockitoBean
    CarroRepository carroRepository;

    Marca marca;
    Carro carro;
    List<Carro> lista;

    @BeforeEach
    void setup() {
        // Marca associada
        Marca marca = new Marca(1L, "Toyota", "Montadora japonesa");

        // Objeto carro
        carro = new Carro(1L, "Corolla", "XEi", marca);

        // Lista com um carro
        lista = List.of(carro);

        // Mockando comportamentos do repositório
        when(carroRepository.findAll()).thenReturn(lista);
        when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
        when(carroRepository.findByNomeContainingIgnoreCase("corolla")).thenReturn(lista);
    }

    @Test
    void cenario01_findAll_deveRetornarLista() {
        List<Carro> resultado = carroService.findAll();

        assertNotNull(resultado, "A lista não deve ser nula");
        assertEquals(1, resultado.size(), "Deve conter exatamente 1 elemento");
        assertEquals("Corolla", resultado.get(0).getNome());
    }

    @Test
    void cenario02_findById_deveRetornarCarroQuandoExistir() {
        Carro resultado = carroService.findById(1L);

        assertNotNull(resultado, "O carro não deve ser nulo");
        assertEquals(1L, resultado.getId());
        assertEquals("XEi", resultado.getModelo());
    }

    @Test
    void cenario03_findByNome_deveRetornarListaNoFiltroNome() {
        List<Carro> resultado = carroService.findByNome("corolla");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "Deve retornar pelo menos um carro");
        assertTrue(resultado.get(0).getNome().equalsIgnoreCase("corolla"));
    }

}
