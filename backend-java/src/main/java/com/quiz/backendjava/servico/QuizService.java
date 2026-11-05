package com.quiz.backendjava.servico;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.backendjava.Categoria; 
import com.quiz.backendjava.Pergunta;
import com.quiz.backendjava.RespostaUsuario;
import com.quiz.backendjava.SubmissaoQuizDTO;
import com.quiz.backendjava.TentativaQuiz;
import com.quiz.backendjava.Usuario;
import com.quiz.backendjava.repository.CategoriaRepository; 
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
    private final CategoriaRepository categoriaRepository; 
    private final ObjectMapper objectMapper;

    public QuizService(PerguntaRepository perguntaRepository, 
                       TentativaQuizRepository tentativaQuizRepository,
                       UsuarioRepository usuarioRepository, 
                       CategoriaRepository categoriaRepository,
                       ObjectMapper objectMapper) {
        this.perguntaRepository = perguntaRepository;
        this.tentativaQuizRepository = tentativaQuizRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.objectMapper = objectMapper;
    }

    public Pergunta salvarPergunta(Pergunta pergunta) {
        if (pergunta.getCategoria() != null && pergunta.getCategoria().getId() != null) {
            Categoria categoriaExistente = categoriaRepository.findById(pergunta.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));
            pergunta.setCategoria(categoriaExistente);
        }
        return perguntaRepository.save(pergunta);
    }

    public Pergunta atualizarPergunta(Long id, Pergunta novaPergunta) {
        return perguntaRepository.findById(id).map(pergunta -> {
            pergunta.setEnunciado(novaPergunta.getEnunciado());
            pergunta.setOpcoes(novaPergunta.getOpcoes());
            pergunta.setRespostaCorreta(novaPergunta.getRespostaCorreta());
            if (novaPergunta.getCategoria() != null && novaPergunta.getCategoria().getId() != null) {
                Categoria categoriaExistente = categoriaRepository.findById(novaPergunta.getCategoria().getId())
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));
                pergunta.setCategoria(categoriaExistente);
            }
            return perguntaRepository.save(pergunta);
        }).orElseThrow(() -> new RuntimeException("Pergunta não encontrada com o ID: " + id));
    }

    public void deletarPergunta(Long id) {
        if (!perguntaRepository.existsById(id)) {
            throw new RuntimeException("Pergunta não encontrada com o ID: " + id);
        }
        perguntaRepository.deleteById(id);
    }

    public List<Pergunta> carregarPerguntas() {
        return perguntaRepository.findAll();
    }

    public List<TentativaQuiz> listarHistoricoPorUsuario(Long usuarioId) {
        return tentativaQuizRepository.findByUsuarioId(usuarioId);
    }

    public String importarPerguntasDoJson() {
        try {
            InputStream inputStream;
            File fileLocal = new File("src/main/resources/perguntas.json");

            if (fileLocal.exists()) {
                inputStream = new FileInputStream(fileLocal);
            } else {
                inputStream = new ClassPathResource("perguntas.json").getInputStream();
            }

            // Garante que existe pelo menos uma categoria no banco antes de importar o JSON
            Categoria categoriaPadrao = categoriaRepository.findAll().stream().findFirst()
                    .orElseGet(() -> categoriaRepository.save(new Categoria("Tecnologia")));

            List<Pergunta> perguntas = objectMapper.readValue(inputStream, new TypeReference<List<Pergunta>>() {});
            
            // Associa a categoria às perguntas importadas do JSON
            for (Pergunta p : perguntas) {
                if (p.getCategoria() == null) {
                    p.setCategoria(categoriaPadrao);
                }
            }

            perguntaRepository.saveAll(perguntas);
            return "Sucesso: " + perguntas.size() + " perguntas importadas para o banco!";
        } catch (Exception e) {
            return "Erro ao importar perguntas: " + e.getMessage();
        }
    }

    @Transactional
    public TentativaQuiz processarEGuardarQuiz(SubmissaoQuizDTO submissao) {
        Usuario usuario = usuarioRepository.findByEmail(submissao.getUsuario().getEmail())
                .orElseGet(() -> usuarioRepository.save(submissao.getUsuario()));

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