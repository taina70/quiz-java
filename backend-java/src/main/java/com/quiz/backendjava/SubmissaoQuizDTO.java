package com.quiz.backendjava;

import java.util.List;

public class SubmissaoQuizDTO {
    private Usuario usuario;
    private String categoria; // Ex: "Concurso", "Faculdade"
    private List<RespostaUsuario> respostas;

    public SubmissaoQuizDTO() {}

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public List<RespostaUsuario> getRespostas() { return respostas; }
    public void setRespostas(List<RespostaUsuario> respostas) { this.respostas = respostas; }
}