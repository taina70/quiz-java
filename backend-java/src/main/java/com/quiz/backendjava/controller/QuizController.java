package com.quiz.backendjava.controller;

import com.quiz.backendjava.Pergunta;
import com.quiz.backendjava.SubmissaoQuizDTO;
import com.quiz.backendjava.TentativaQuiz;
import com.quiz.backendjava.servico.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/perguntas")
    public List<Pergunta> listarPerguntas() {
        return quizService.carregarPerguntas();
    }

    @PostMapping("/importar")
    public ResponseEntity<String> importarPerguntas() {
        String resultado = quizService.importarPerguntasDoJson();
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/finalizar")
    public TentativaQuiz finalizarQuiz(@RequestBody SubmissaoQuizDTO submissao) {
        return quizService.processarEGuardarQuiz(submissao);
    }

    @GetMapping("/historico")
    public List<TentativaQuiz> obterHistorico() {
        return quizService.listarHistorico();
    }
}