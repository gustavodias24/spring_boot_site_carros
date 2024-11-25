package com.andrey.projeto.carro;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "carros")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String foto;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private int tempoDeUso;

    @Column(nullable = false)
    private int avaliacaoPintura;

    @Column(nullable = false)
    private int avaliacaoFreios;

    @Column(nullable = false)
    private int avaliacaoPneus;

    @Column(nullable = false)
    private Long usuarioId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTempoDeUso() {
        return tempoDeUso;
    }

    public void setTempoUso(int tempoDeUso) {
        this.tempoDeUso = tempoDeUso;
    }

    public int getAvaliacaoPintura() {
        return avaliacaoPintura;
    }

    public void setAvaliacaoPintura(int avaliacaoPintura) {
        this.avaliacaoPintura = avaliacaoPintura;
    }

    public int getAvaliacaoFreios() {
        return avaliacaoFreios;
    }

    public void setAvaliacaoFreios(int avaliacaoFreios) {
        this.avaliacaoFreios = avaliacaoFreios;
    }

    public int getAvaliacaoPneus() {
        return avaliacaoPneus;
    }

    public void setAvaliacaoPneus(int avaliacaoPneus) {
        this.avaliacaoPneus = avaliacaoPneus;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
