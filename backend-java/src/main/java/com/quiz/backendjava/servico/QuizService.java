package com.quiz.backendjava.servico;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.backendjava.Pergunta;
import com.quiz.backendjava.RespostaUsuario;
import com.quiz.backendjava.SubmissaoQuizDTO;
import com.quiz.backendjava.TentativaQuiz;
import com.quiz.backendjava.Usuario;
import com.quiz.backendjava.repository.PerguntaRepository;
import com.quiz.backendjava.repository.TentativaQuizRepository;
import com.quiz.backendjava.repository.UsuarioRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final PerguntaRepository perguntaRepository;
    private final TentativaQuizRepository tentativaQuizRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    public QuizService(PerguntaRepository perguntaRepository, 
                       TentativaQuizRepository tentativaQuizRepository,
                       UsuarioRepository usuarioRepository, 
                       ObjectMapper objectMapper) {
        this.perguntaRepository = perguntaRepository;
        this.tentativaQuizRepository = tentativaQuizRepository;
        this.usuarioRepository = usuarioRepository;
        this.objectMapper = objectMapper;
    }

    public List<Pergunta> carregarPerguntas() {
        return perguntaRepository.findAll();
    }

    public String importarPerguntasDoJson() {
        try {
            InputStream inputStream;
            File fileLocal = new File("src/main/resources/perguntas.json");

            // Tenta ler direto do arquivo físico (permite alterar o JSON dinamicamente)
            if (fileLocal.exists()) {
                inputStream = new FileInputStream(fileLocal);
            } else {
                // Fallback para o classpath
                inputStream = new ClassPathResource("perguntas.json").getInputStream();
            }

            List<Pergunta> perguntas = objectMapper.readValue(inputStream, new TypeReference<List<Pergunta>>() {});
            perguntaRepository.saveAll(perguntas);
            return "Sucesso: " + perguntas.size() + " perguntas importadas para o banco!";
        } catch (Exception e) {
            return "Erro ao importar perguntas: " + e.getMessage();
        }
    }

    @Transactional
    public TentativaQuiz processarEGuardarQuiz(SubmissaoQuizDTO submissao) {
        // 1. Salvar ou recuperar o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(submissao.getUsuario().getEmail())
                .orElseGet(() -> usuarioRepository.save(submissao.getUsuario()));

        // 2. Processar acertos e erros
        int acertos = 0;
        int total = submissao.getRespostas() != null ? submissao.getRespostas().size() : 0;
        List<String> pontosParaMelhorar = new ArrayList<>();

        if (submissao.getRespostas() != null) {
            for (RespostaUsuario resposta : submissao.getRespostas()) {
                Pergunta pergunta = perguntaRepository.findById(resposta.getIdPergunta()).orElse(null);
                if (pergunta != null) {
                    if (pergunta.getRespostaCorreta() == resposta.getOpcaoEscolhida()) {
                        acertos++;
                    } else {
                        pontosParaMelhorar.add("Revisar pergunta ID: " + pergunta.getId());
                    }
                }
            }
        }

        int erros = total - acertos;
        double porcentagem = total > 0 ? ((double) acertos / total) * 100 : 0.0;

        // 3. Registrar a tentativa
        TentativaQuiz tentativa = new TentativaQuiz();
        tentativa.setUsuario(usuario);
        tentativa.setCategoria(submissao.getCategoria());
        tentativa.setTotalPerguntas(total);
        tentativa.setTotalAcertos(acertos);
        tentativa.setTotalErros(erros);
        tentativa.setPorcentagemAcerto(porcentagem);
        tentativa.setDataRealizacao(LocalDateTime.now());
        tentativa.setPontosParaMelhorar(pontosParaMelhorar);

        return tentativaQuizRepository.save(tentativa);
    }

    public List<TentativaQuiz> listarHistorico() {
        return tentativaQuizRepository.findAll();
    }
}