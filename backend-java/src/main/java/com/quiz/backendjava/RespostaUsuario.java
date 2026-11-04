package com.quiz.backendjava;

public class RespostaUsuario {
    private Long idPergunta;
    private int opcaoEscolhida;

    public RespostaUsuario() {}

    public Long getIdPergunta() { return idPergunta; }
    public void setIdPergunta(Long idPergunta) { this.idPergunta = idPergunta; }

    public int getOpcaoEscolhida() { return opcaoEscolhida; }
    public void setOpcaoEscolhida(int opcaoEscolhida) { this.opcaoEscolhida = opcaoEscolhida; }
}