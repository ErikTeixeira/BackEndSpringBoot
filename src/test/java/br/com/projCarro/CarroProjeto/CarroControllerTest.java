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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setup() {
        marca = new Marca(1L, "Toyota", "Montadora japonesa");
        carro = new Carro(1L, "Corolla", "XEi", marca);
        lista = List.of(carro);
    }

    @Test
    void cenario01_findAll_retornarLista() {
        
        when(carroService.findAll()).thenReturn(lista);
        
        ResponseEntity<List<Carro>> response = carroController.findAll();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Corolla", response.getBody().get(0).getNome());
    }

    @Test
    void cenario02_findById_retornarCarroQuandoExistir() {
        
        when(carroService.findById(1L)).thenReturn(carro);
        
        ResponseEntity<Carro> response = carroController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("XEi", response.getBody().getModelo());
    }

    @Test
    void cenario03_findByNome_retornarListaFiltrada() {
        
        when(carroService.findByNome("corolla")).thenReturn(lista);

        ResponseEntity<List<Carro>> response = carroController.findByNome("corolla");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Corolla", response.getBody().get(0).getNome());
    }

    @Test
    void cenario04_deleteById_retornarSucesso() {
        
        when(carroService.deleteById(1L)).thenReturn("Carro deletado com sucesso");

        ResponseEntity<String> response = carroController.deleteById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carro deletado com sucesso", response.getBody());
    }

    @Test
    void cenario05_save_retornarSucesso() {
        
        Carro novoCarro = new Carro();
        novoCarro.setNome("Civic");
        novoCarro.setModelo("EX");
        novoCarro.setMarca(marca);
        when(carroService.save(novoCarro)).thenReturn("Carro salvo com sucesso");

        ResponseEntity<String> response = carroController.save(novoCarro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carro salvo com sucesso", response.getBody());
    }

    @Test
    void cenario06_update_retornarSucesso() {
        
        Carro carroAtualizado = new Carro();
        carroAtualizado.setNome("Focus");
        carroAtualizado.setModelo("SE");
        carroAtualizado.setMarca(marca);
        when(carroService.update(carroAtualizado, 1L)).thenReturn("Carro atualizado com sucesso");

        
        ResponseEntity<String> response = carroController.update(carroAtualizado, 1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carro atualizado com sucesso", response.getBody());
    }
}
