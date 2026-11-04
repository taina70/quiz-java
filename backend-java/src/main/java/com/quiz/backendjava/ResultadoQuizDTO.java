package com.quiz.backendjava;

import java.util.List;

public class ResultadoQuizDTO {
    private Usuario usuario;
    private String categoria;
    private int totalPerguntas;
    private int totalAcertos;
    private int totalErros;
    private double porcentagemAcerto;
    private List<String> pontosParaMelhorar; // Lista dos enunciados das questões erradas

    public ResultadoQuizDTO() {}

    // Getters e Setters
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getTotalPerguntas() { return totalPerguntas; }
    public void setTotalPerguntas(int totalPerguntas) { this.totalPerguntas = totalPerguntas; }

    public int getTotalAcertos() { return totalAcertos; }
    public void setTotalAcertos(int totalAcertos) { this.totalAcertos = totalAcertos; }

    public int getTotalErros() { return totalErros; }
    public void setTotalErros(int totalErros) { this.totalErros = totalErros; }

    public double getPorcentagemAcerto() { return porcentagemAcerto; }
    public void setPorcentagemAcerto(double porcentagemAcerto) { this.porcentagemAcerto = porcentagemAcerto; }

    public List<String> getPontosParaMelhorar() { return pontosParaMelhorar; }
    public void setPontosParaMelhorar(List<String> pontosParaMelhorar) { this.pontosParaMelhorar = pontosParaMelhorar; }
}