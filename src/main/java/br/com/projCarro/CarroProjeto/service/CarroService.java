package br.com.projCarro.CarroProjeto.service;

import br.com.projCarro.CarroProjeto.entity.Carro;
import br.com.projCarro.CarroProjeto.repository.CarroRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;


    private void validarNomeModelo(Carro carro) {
        String nome = carro.getNome()   != null ? carro.getNome().trim().toLowerCase()   : "";
        String modelo = carro.getModelo() != null ? carro.getModelo().trim().toLowerCase() : "";

        if (!nome.isEmpty() && nome.equals(modelo)) {
            throw new IllegalArgumentException("O nome e o modelo não podem ser iguais.");
        }
    }

    public List<Carro> findAll(){
        return this.carroRepository.findAll();
    }

    public Carro findById(long id) {
        Optional<Carro> carro = this.carroRepository.findById(id);
        return carro.orElse(null);
    }

    public List<Carro> findByNome(String nome) {
        return this.carroRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public String save(@Valid Carro carro) {
        validarNomeModelo(carro);

        carroRepository.save(carro);
        return "Carro salvo com sucesso";
    }

    @Transactional
    public String update(@Valid Carro carro, long id) {
        validarNomeModelo(carro);

        carro.setId(id);
        carroRepository.save(carro);
        return "Carro atualizado com sucesso";
    }

    @Transactional
    public String deleteById(long id) {
        this.carroRepository.deleteById(id);
        return "Carro deletado com sucesso";
    }



}
