package br.com.projCarro.CarroProjeto.service;

import br.com.projCarro.CarroProjeto.entity.Carro;
import br.com.projCarro.CarroProjeto.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

    public String save(Carro carro) {
        validarNomeModelo(carro);

        carroRepository.save(carro);
        return "Carro salvo com sucesso";
    }

    public String update(Carro carro, long id) {
        validarNomeModelo(carro);

        carro.setId(id);
        carroRepository.save(carro);
        return "Carro atualizado com sucesso";
    }

    public String deleteById(long id) {
        this.carroRepository.deleteById(id);
        return "Carro deletado com sucesso";
    }

}
