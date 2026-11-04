package com.quiz.backendjava;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "perguntas")
public class Pergunta {

    @Id
    private Long id;

    private String enunciado;

    @ElementCollection
    @CollectionTable(name = "pergunta_opcoes", joinColumns = @JoinColumn(name = "pergunta_id"))
    @Column(name = "opcao")
    private List<String> opcoes;

    private int respostaCorreta;

    public Pergunta() {}

    public Pergunta(Long id, String enunciado, List<String> opcoes, int respostaCorreta) {
        this.id = id;
        this.enunciado = enunciado;
        this.opcoes = opcoes;
        this.respostaCorreta = respostaCorreta;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public List<String> getOpcoes() { return opcoes; }
    public void setOpcoes(List<String> opcoes) { this.opcoes = opcoes; }

    public int getRespostaCorreta() { return respostaCorreta; }
    public void setRespostaCorreta(int respostaCorreta) { this.respostaCorreta = respostaCorreta; }
}