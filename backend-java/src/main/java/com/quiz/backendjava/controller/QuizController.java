package com.quiz.backendjava.controller;

import com.quiz.backendjava.Pergunta;
import com.quiz.backendjava.SubmissaoQuizDTO;
import com.quiz.backendjava.TentativaQuiz;
import com.quiz.backendjava.servico.QuizService;
import org.springframework.http.HttpStatus; // <-- Import que faltava
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

    @PostMapping("/perguntas")
    public ResponseEntity<Pergunta> cadastrarPergunta(@RequestBody Pergunta pergunta) {
        Pergunta novaPergunta = quizService.salvarPergunta(pergunta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPergunta);
    }

    @PutMapping("/perguntas/{id}")
    public ResponseEntity<Pergunta> atualizarPergunta(@PathVariable Long id, @RequestBody Pergunta pergunta) {
        Pergunta atualizada = quizService.atualizarPergunta(id, pergunta);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/perguntas/{id}")
    public ResponseEntity<Void> deletarPergunta(@PathVariable Long id) {
        quizService.deletarPergunta(id);
        return ResponseEntity.noContent().build();
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

    @GetMapping("/historico/usuario/{usuarioId}")
    public List<TentativaQuiz> obterHistoricoPorUsuario(@PathVariable Long usuarioId) {
        return quizService.listarHistoricoPorUsuario(usuarioId);
    }
}