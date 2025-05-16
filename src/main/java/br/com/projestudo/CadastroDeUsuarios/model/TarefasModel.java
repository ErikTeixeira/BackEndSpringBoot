package br.com.projestudo.CadastroDeUsuarios.model;

import br.com.projestudo.CadastroDeUsuarios.model.enums.Prioridade;
import br.com.projestudo.CadastroDeUsuarios.model.enums.Status;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_tarefas")
public class TarefasModel {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    Long id;
    String titulo;
    String descricao;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private String categoria;

    @OneToMany( mappedBy = "tarefas" )
    private List<UsuarioModel> usuario;
}
