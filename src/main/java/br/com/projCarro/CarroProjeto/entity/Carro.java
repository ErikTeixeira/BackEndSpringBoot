package br.com.projCarro.CarroProjeto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O nome não pode ficar em branco")
    private String nome;

    @NotBlank
    @Size(min = 2, max = 20)
    private String modelo;

    @ManyToOne( cascade = CascadeType.ALL )
    private Marca marca;

}


