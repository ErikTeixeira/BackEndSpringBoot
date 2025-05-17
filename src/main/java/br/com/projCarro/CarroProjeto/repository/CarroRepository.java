package br.com.projCarro.CarroProjeto.repository;

import br.com.projCarro.CarroProjeto.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    public List<Carro> findByNomeContainingIgnoreCase(String nome);

}
