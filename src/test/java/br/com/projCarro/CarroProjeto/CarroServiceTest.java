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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(carroRepository.save(carro)).thenReturn(carro);
    }

    @Test
    void cenario01_findAll_retornarLista() {
        List<Carro> resultado = carroService.findAll();

        assertNotNull(resultado, "A lista não deve ser nula");
        assertEquals(1, resultado.size(), "Deve conter exatamente 1 elemento");
        assertEquals("Corolla", resultado.get(0).getNome());
    }

    @Test
    void cenario02_findById_retornarCarroQuandoExistir() {
        Carro resultado = carroService.findById(1L);

        assertNotNull(resultado, "O carro não deve ser nulo");
        assertEquals(1L, resultado.getId());
        assertEquals("XEi", resultado.getModelo());
    }

    @Test
    void cenario03_findByNome_retornarListaNoFiltroNome() {
        List<Carro> resultado = carroService.findByNome("corolla");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "Deve retornar pelo menos um carro");
        assertTrue(resultado.get(0).getNome().equalsIgnoreCase("corolla"));
    }

    @Test
    void cenario04_save_retornarSucesso() {
        String resultado = carroService.save(carro);

        assertEquals( "Carro salvo com sucesso", resultado );
    }

    @Test
    void cenario05_save_lancarExcecao_quandoNomeIgualModelo() {

        Carro carroInvalido = new Carro(2L, "Gol", "Gol", marca);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroInvalido);
        });

        assertEquals( "O nome e o modelo não podem ser iguais.", exception.getMessage() );
    }

    @Test
    void cenario06_update_retornarSucesso() {
        long id = 1L;
        Carro carroParaAtualizar = new Carro(); // objeto novo para simular atualização
        carroParaAtualizar.setNome("Civic");
        carroParaAtualizar.setModelo("EX");
        carroParaAtualizar.setMarca(marca);

        // Simula que o carro será salvo e retornado corretamente
        when(carroRepository.save(any(Carro.class))).thenReturn(carroParaAtualizar);

        String resultado = carroService.update(carroParaAtualizar, id);


        verify(carroRepository, times(1)).save(carroParaAtualizar);
            // verify -> Comando do Mockito que verifica se um método de um mock (falso) foi chamado

        assertEquals(id, carroParaAtualizar.getId()); // confirma que o ID foi setado
        assertEquals("Carro atualizado com sucesso", resultado);
    }

    @Test
    void cenario07_delete_retornarSucesso() {
        long id = 1L;

        String resultado = carroService.deleteById(id);

        verify(carroRepository, times(1)).deleteById(id); // garante que o método foi chamado
        assertEquals("Carro deletado com sucesso", resultado);
    }


    @Test
    void cenario08_validarNomeModelo_naoLanca_quandoNomeNull() {
        Carro c = new Carro();
        c.setNome(null);
        c.setModelo("X");

        assertDoesNotThrow(() -> carroService.save(c));
    }

    @Test
    void cenario09_validarNomeModelo_naoLanca_quandoModeloNull() {
        Carro c = new Carro();
        c.setNome("Y");
        c.setModelo(null);

        assertDoesNotThrow(() -> carroService.save(c));
    }

    @Test
    void cenario10_validarNomeModelo_naoLanca_quandoAmbosNull() {
        Carro c = new Carro();
        c.setNome(null);
        c.setModelo(null);

        assertDoesNotThrow(() -> carroService.save(c));
    }

    @Test
    void cenario11_validarNomeModelo_naoLanca_quandoApenasEspacos() {
        Carro c = new Carro();
        c.setNome("   ");
        c.setModelo("   ");

        assertDoesNotThrow(() -> carroService.save(c));
    }

    @Test
    void cenario12_validarNomeModelo_lancaExcecao_quandoIguaisComEspacosECaseDiferente() {
        Carro c = new Carro();
        c.setNome(" Fiesta ");
        c.setModelo("fiesta");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> carroService.save(c)
        );

        assertEquals("O nome e o modelo não podem ser iguais.", ex.getMessage());
    }


}
