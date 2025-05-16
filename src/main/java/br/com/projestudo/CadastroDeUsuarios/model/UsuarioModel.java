package br.com.projestudo.CadastroDeUsuarios.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table( name = "tb_cadastro_users")
public class UsuarioModel {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn( name = "tarefas_id" )
    private TarefasModel tarefas;

    public UsuarioModel() {
    }

    public UsuarioModel(String nome, String sobrenome, String email, String senha, String telefone, LocalDate dataNascimento) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
