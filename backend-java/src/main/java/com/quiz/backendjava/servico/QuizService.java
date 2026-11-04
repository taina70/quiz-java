package com.quiz.backendjava.servico;

import com.quiz.backendjava.*;
import com.quiz.backendjava.repository.TentativaQuizRepository;
import com.quiz.backendjava.repository.UsuarioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final UsuarioRepository usuarioRepository;
    private final TentativaQuizRepository tentativaQuizRepository;

    public QuizService(UsuarioRepository usuarioRepository, TentativaQuizRepository tentativaQuizRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tentativaQuizRepository = tentativaQuizRepository;
    }

    public List<Pergunta> carregarPerguntas() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("perguntas.json"), new TypeReference<List<Pergunta>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public TentativaQuiz processarEGuardarQuiz(SubmissaoQuizDTO submissao) {
        // 1. Busca ou cria o Usuário no Postgres
        Usuario usuario = usuarioRepository.findByEmail(submissao.getUsuario().getEmail())
                .orElseGet(() -> usuarioRepository.save(submissao.getUsuario()));

        // 2. Valida as respostas com base no JSON
        List<Pergunta> perguntas = carregarPerguntas();
        int acertos = 0;
        int erros = 0;
        List<String> pontosParaMelhorar = new ArrayList<>();

        for (RespostaUsuario resp : submissao.getRespostas()) {
            Pergunta p = perguntas.stream()
                    .filter(pergunta -> pergunta.getId().equals(resp.getIdPergunta()))
                    .findFirst()
                    .orElse(null);

            if (p != null) {
                if (p.getRespostaCorreta() == resp.getOpcaoEscolhida()) {
                    acertos++;
                } else {
                    erros++;
                    pontosParaMelhorar.add(p.getEnunciado());
                }
            }
        }

        int total = submissao.getRespostas().size();
        double porcentagem = total > 0 ? ((double) acertos / total) * 100 : 0.0;

        // 3. Monta e salva a Tentativa no Postgres
        TentativaQuiz tentativa = new TentativaQuiz();
        tentativa.setUsuario(usuario);
        tentativa.setCategoria(submissao.getCategoria());
        tentativa.setTotalPerguntas(total);
        tentativa.setTotalAcertos(acertos);
        tentativa.setTotalErros(erros);
        tentativa.setPorcentagemAcerto(porcentagem);
        tentativa.setPontosParaMelhorar(pontosParaMelhorar);

        return tentativaQuizRepository.save(tentativa);
    }

    public List<TentativaQuiz> listarHistorico() {
        return tentativaQuizRepository.findAll();
    }
}