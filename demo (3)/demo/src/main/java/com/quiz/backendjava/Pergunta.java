package com.quiz.backendjava;

import java.util.List;

public class Pergunta {
    private Long id;
    private String enunciado;
    private List<String> opcoes;
    private int respostaCorreta;

    public Pergunta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public List<String> getOpcoes() { return opcoes; }
    public void setOpcoes(List<String> opcoes) { this.opcoes = opcoes; }

    public int getRespostaCorreta() { return respostaCorreta; }
    public void setRespostaCorreta(int respostaCorreta) { this.respostaCorreta = respostaCorreta; }
}