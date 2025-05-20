package br.com.projCarro.CarroProjeto;

import br.com.projCarro.CarroProjeto.entity.Carro;
import br.com.projCarro.CarroProjeto.entity.Marca;
import br.com.projCarro.CarroProjeto.repository.CarroRepository;
import br.com.projCarro.CarroProjeto.service.CarroService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.Collections;
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
        this.marca = new Marca(1L, "Toyota", "Montadora japonesa");

        // Objeto carro
        this.carro = new Carro(1L, "Corolla", "XEi", this.marca);

        // Lista com um carro
        this.lista = List.of(this.carro);

        // Mockando comportamentos do repositório
        when(carroRepository.findAll()).thenReturn(this.lista);
        when(carroRepository.findById(1L)).thenReturn(Optional.of(this.carro));
        when(carroRepository.findById(99L)).thenReturn(Optional.empty()); // Para teste de ID não existente
        when(carroRepository.findByNomeContainingIgnoreCase("corolla")).thenReturn(this.lista);
        when(carroRepository.findByNomeContainingIgnoreCase("inexistente")).thenReturn(Collections.emptyList());

        when(carroRepository.save(any(Carro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(carroRepository.save(this.carro)).thenReturn(this.carro);

        doNothing().when(carroRepository).deleteById(anyLong());
    }

    @Test
    void cenario01_findAll_retornarLista() {
        List<Carro> resultado = carroService.findAll();

        assertNotNull(resultado, "A lista não deve ser nula");
        assertEquals(1, resultado.size(), "Deve conter exatamente 1 elemento");
        assertEquals("Corolla", resultado.get(0).getNome());
        verify(carroRepository, times(1)).findAll();
    }

    @Test
    void cenario02_findById_retornarCarroQuandoExistir() {
        Carro resultado = carroService.findById(1L);

        assertNotNull(resultado, "O carro não deve ser nulo");
        assertEquals(1L, resultado.getId());
        assertEquals("XEi", resultado.getModelo());
        verify(carroRepository, times(1)).findById(1L);
    }

    @Test
    void cenario02_1_findById_retornarNullQuandoNaoExistir() {
        Carro resultado = carroService.findById(99L);
        assertNull(resultado, "O carro deve ser nulo para ID inexistente");
        verify(carroRepository, times(1)).findById(99L);
    }

    @Test
    void cenario03_findByNome_retornarListaNoFiltroNome() {
        List<Carro> resultado = carroService.findByNome("corolla");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "Deve retornar pelo menos um carro");
        assertTrue(resultado.get(0).getNome().equalsIgnoreCase("corolla"));
        verify(carroRepository, times(1)).findByNomeContainingIgnoreCase("corolla");
    }

    @Test
    void cenario03_1_findByNome_retornarListaVaziaQuandoNomeNaoEncontrado() {
        List<Carro> resultado = carroService.findByNome("inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Deve retornar lista vazia para nome não encontrado");
        verify(carroRepository, times(1)).findByNomeContainingIgnoreCase("inexistente");
    }

    @Test
    void cenario04_save_retornarSucesso() {
        String resultado = carroService.save(this.carro);

        assertEquals( "Carro salvo com sucesso", resultado );
        verify(carroRepository, times(1)).save(this.carro);
    }

    @Test
    void cenario05_save_lancarExcecao_quandoNomeIgualModelo() {
        Carro carroInvalido = new Carro(2L, "Gol", "Gol", this.marca);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroInvalido);
        });

        assertEquals( "O nome e o modelo não podem ser iguais.", exception.getMessage() );
        verify(carroRepository, never()).save(carroInvalido);
    }

    @Test
    void cenario06_update_retornarSucesso() {
        long id = 1L;
        Carro carroParaAtualizar = new Carro();
        carroParaAtualizar.setNome("Civic");
        carroParaAtualizar.setModelo("EXL");
        carroParaAtualizar.setMarca(this.marca);

        when(carroRepository.save(carroParaAtualizar)).thenReturn(carroParaAtualizar);

        String resultado = carroService.update(carroParaAtualizar, id);

        assertEquals(id, carroParaAtualizar.getId());
        assertEquals("Carro atualizado com sucesso", resultado);
        verify(carroRepository, times(1)).save(carroParaAtualizar);
    }

    @Test
    void cenario06_1_update_lancarExcecao_quandoNomeIgualModelo() {
        long idParametro = 3L;
        Carro carroParaAtualizar = new Carro(); // ID interno é 0L por padrão
        carroParaAtualizar.setNome("Virtus");
        carroParaAtualizar.setModelo("Virtus");
        carroParaAtualizar.setMarca(this.marca);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            carroService.update(carroParaAtualizar, idParametro);
        });

        assertEquals( "O nome e o modelo não podem ser iguais.", exception.getMessage() );
        // Verifica que o ID do objeto não foi alterado para idParametro, pois a exceção ocorreu antes.
        // O ID original do objeto carroParaAtualizar (0L) deve ser mantido.
        assertEquals(0L, carroParaAtualizar.getId());
        verify(carroRepository, never()).save(carroParaAtualizar);
    }


    @Test
    void cenario07_delete_retornarSucesso() {
        long id = 1L;
        String resultado = carroService.deleteById(id);

        verify(carroRepository, times(1)).deleteById(id);
        assertEquals("Carro deletado com sucesso", resultado);
    }

    @Test
    void cenario08_save_lancarConstraintViolationException_quandoNomeNull() {
        Carro c = new Carro();
        c.setId(10L);
        c.setNome(null);
        c.setModelo("ModeloValido");
        c.setMarca(this.marca);

        assertThrows(ConstraintViolationException.class, () -> carroService.save(c));
        verify(carroRepository, never()).save(c);
    }

    @Test
    void cenario09_save_lancarConstraintViolationException_quandoModeloNull() {
        Carro c = new Carro();
        c.setId(11L);
        c.setNome("NomeValido");
        c.setModelo(null);
        c.setMarca(this.marca);

        assertThrows(ConstraintViolationException.class, () -> carroService.save(c));
        verify(carroRepository, never()).save(c);
    }

    @Test
    void cenario10_save_lancarConstraintViolationException_quandoNomeModeloNull() {
        Carro c = new Carro();
        c.setId(12L);
        c.setNome(null);
        c.setModelo(null);
        c.setMarca(this.marca);

        assertThrows(ConstraintViolationException.class, () -> carroService.save(c));
        verify(carroRepository, never()).save(c);
    }

    @Test
    void cenario11_save_lancarConstraintViolationException_quandoNomeApenasEspacos() {
        Carro c = new Carro();
        c.setId(13L);
        c.setNome("   ");
        c.setModelo("ModeloValido");
        c.setMarca(this.marca);

        assertThrows(ConstraintViolationException.class, () -> carroService.save(c));
        verify(carroRepository, never()).save(c);
    }

    @Test
    void cenario11_1_save_lancarConstraintViolationException_quandoModeloApenasEspacos() {
        Carro c = new Carro();
        c.setId(14L);
        c.setNome("NomeValido");
        c.setModelo("   ");
        c.setMarca(this.marca);

        assertThrows(ConstraintViolationException.class, () -> carroService.save(c));
        verify(carroRepository, never()).save(c);
    }

    @Test
    void cenario12_validarNomeModelo_lancaExcecao_quandoIguaisComEspacosECaseDiferente() {
        Carro c = new Carro();
        c.setId(15L);
        c.setNome(" Fiesta ");
        c.setModelo("fiesta");
        c.setMarca(this.marca);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> carroService.save(c)
        );

        assertEquals("O nome e o modelo não podem ser iguais.", ex.getMessage());
        verify(carroRepository, never()).save(c);
    }
}
