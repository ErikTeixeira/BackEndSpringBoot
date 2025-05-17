package br.com.projCarro.CarroProjeto.controller;

import br.com.projCarro.CarroProjeto.entity.Carro;
import br.com.projCarro.CarroProjeto.service.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    public CarroController(CarroService carroService) {
        this.carroService = carroService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Carro>> findAll(){
        List<Carro> lista = this.carroService.findAll();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Carro> findById(@PathVariable("id") long id){
        Carro carro = this.carroService.findById(id);
        return new ResponseEntity<>(carro, HttpStatus.OK);
    }

    @GetMapping("/findByNome")
    public ResponseEntity<List<Carro>> findByNome(@RequestParam("nome") String nome){
        List<Carro> lista = this.carroService.findByNome(nome);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") long id){
        String mensagem = this.carroService.deleteById(id);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Carro carro){
        String mensagem = this.carroService.save(carro);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Carro carro, @PathVariable long id){
        String mensagem = this.carroService.update(carro, id);
        return new ResponseEntity<String>(mensagem, HttpStatus.OK);
    }


}
