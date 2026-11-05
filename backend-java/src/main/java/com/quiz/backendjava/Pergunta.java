package com.quiz.backendjava;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "perguntas")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;

    @ElementCollection
    private List<String> opcoes;

    private int respostaCorreta;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private String pontoMelhoria;

    public Pergunta() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public List<String> getOpcoes() { return opcoes; }
    public void setOpcoes(List<String> opcoes) { this.opcoes = opcoes; }

    public int getRespostaCorreta() { return respostaCorreta; }
    public void setRespostaCorreta(int respostaCorreta) { this.respostaCorreta = respostaCorreta; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getPontoMelhoria() { return pontoMelhoria; }
    public void setPontoMelhoria(String pontoMelhoria) { this.pontoMelhoria = pontoMelhoria; }
}