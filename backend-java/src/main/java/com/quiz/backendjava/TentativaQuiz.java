package com.quiz.backendjava;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tentativas_quiz")
public class TentativaQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String categoria;
    private int totalPerguntas;
    private int totalAcertos;
    private int totalErros;
    private double porcentagemAcerto;
    private LocalDateTime dataRealizacao;

    @ElementCollection
    private List<String> pontosParaMelhorar;

    public TentativaQuiz() {
        this.dataRealizacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { this.dataRealizacao = dataRealizacao; }
    public List<String> getPontosParaMelhorar() { return pontosParaMelhorar; }
    public void setPontosParaMelhorar(List<String> pontosParaMelhorar) { this.pontosParaMelhorar = pontosParaMelhorar; }
}