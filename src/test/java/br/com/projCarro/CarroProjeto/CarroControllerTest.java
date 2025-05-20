package br.com.projCarro.CarroProjeto;

import br.com.projCarro.CarroProjeto.controller.CarroController;
import br.com.projCarro.CarroProjeto.entity.Carro;
import br.com.projCarro.CarroProjeto.entity.Marca;
import br.com.projCarro.CarroProjeto.service.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
public class CarroControllerTest {

    @Autowired
    CarroController carroController;

    @MockitoBean
    CarroService carroService;

    Marca marca;
    Carro carro;
    List<Carro> lista;
    Carro carroParaSalvarOuAtualizar;

    @BeforeEach
    void setup() {
        marca = new Marca(1L, "Toyota", "Montadora japonesa");
        carro = new Carro(1L, "Corolla", "XEi", marca);
        lista = List.of(carro);

        carroParaSalvarOuAtualizar = new Carro(); // Objeto genérico para save/update
        carroParaSalvarOuAtualizar.setNome("Civic");
        carroParaSalvarOuAtualizar.setModelo("EX");
        carroParaSalvarOuAtualizar.setMarca(marca);
    }

    @Test
    void cenario01_findAll_retornarLista() {
        when(carroService.findAll()).thenReturn(lista);

        ResponseEntity<List<Carro>> response = carroController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Corolla", response.getBody().get(0).getNome());
        verify(carroService, times(1)).findAll();
    }

    @Test
    void cenario01_1_findAll_retornarListaVazia() {
        when(carroService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Carro>> response = carroController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(carroService, times(1)).findAll();
    }

    @Test
    void cenario02_findById_retornarCarroQuandoExistir() {
        when(carroService.findById(1L)).thenReturn(carro);

        ResponseEntity<Carro> response = carroController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("XEi", response.getBody().getModelo());
        verify(carroService, times(1)).findById(1L);
    }

    @Test
    void cenario02_1_findById_retornarOkComBodyNullQuandoNaoExistir() {
        when(carroService.findById(99L)).thenReturn(null); // Serviço retorna null se não achar

        ResponseEntity<Carro> response = carroController.findById(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Controller atual retorna OK com corpo null
        assertNull(response.getBody());
        verify(carroService, times(1)).findById(99L);
    }


    @Test
    void cenario03_findByNome_retornarListaFiltrada() {
        when(carroService.findByNome("corolla")).thenReturn(lista);

        ResponseEntity<List<Carro>> response = carroController.findByNome("corolla");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Corolla", response.getBody().get(0).getNome());
        verify(carroService, times(1)).findByNome("corolla");
    }

    @Test
    void cenario03_1_findByNome_retornarListaVaziaQuandoNomeNaoEncontrado() {
        when(carroService.findByNome("inexistente")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Carro>> response = carroController.findByNome("inexistente");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(carroService, times(1)).findByNome("inexistente");
    }

    @Test
    void cenario04_deleteById_retornarSucesso() {
        when(carroService.deleteById(1L)).thenReturn("Carro deletado com sucesso");

        ResponseEntity<String> response = carroController.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carro deletado com sucesso", response.getBody());
        verify(carroService, times(1)).deleteById(1L);
    }

    @Test
    void cenario05_save_retornarSucesso() {
        when(carroService.save(carroParaSalvarOuAtualizar)).thenReturn("Carro salvo com sucesso");

        ResponseEntity<String> response = carroController.save(carroParaSalvarOuAtualizar);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carro salvo com sucesso", response.getBody());
        verify(carroService, times(1)).save(carroParaSalvarOuAtualizar);
    }

    @Test
    void cenario06_update_retornarSucesso() {
        long idParaAtualizar = 1L;
        when(carroService.update(carroParaSalvarOuAtualizar, idParaAtualizar)).thenReturn("Carro atualizado com sucesso");

        ResponseEntity<String> response = carroController.update(carroParaSalvarOuAtualizar, idParaAtualizar);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carro atualizado com sucesso", response.getBody());
        verify(carroService, times(1)).update(carroParaSalvarOuAtualizar, idParaAtualizar);
    }
}
