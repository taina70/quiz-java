package com.quiz.backendjava.servico;

import com.quiz.backendjava.Pergunta;
import com.quiz.backendjava.RespostaUsuario;
import com.quiz.backendjava.SubmissaoQuizDTO;
import com.quiz.backendjava.TentativaQuiz;
import com.quiz.backendjava.Usuario;
import com.quiz.backendjava.repository.PerguntaRepository;
import com.quiz.backendjava.repository.TentativaQuizRepository;
import com.quiz.backendjava.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private PerguntaRepository perguntaRepository;

    @Mock
    private TentativaQuizRepository tentativaQuizRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private QuizService quizService;

    private Pergunta pergunta1;
    private Pergunta pergunta2;

    @BeforeEach
    void setUp() {
        pergunta1 = new Pergunta();
        pergunta1.setId(1L);
        pergunta1.setEnunciado("Qual pilar esconde detalhes?");
        pergunta1.setRespostaCorreta(1);

        pergunta2 = new Pergunta();
        pergunta2.setId(2L);
        pergunta2.setEnunciado("Qual anotação indica Controller REST?");
        pergunta2.setRespostaCorreta(2);
    }

    @Test
    @DisplayName("Deve calcular 100% de acerto quando todas as respostas forem corretas")
    void deveCalcularCemPorCentoDeAcerto() {
        // Arrange (Preparação)
        Usuario usuario = new Usuario();
        usuario.setEmail("taina@email.com");
        usuario.setNome("Tainá");

        SubmissaoQuizDTO submissao = new SubmissaoQuizDTO();
        submissao.setUsuario(usuario);
        submissao.setCategoria("Tecnologia");

        RespostaUsuario r1 = new RespostaUsuario();
        r1.setIdPergunta(1L);
        r1.setOpcaoEscolhida(1); // Correto

        RespostaUsuario r2 = new RespostaUsuario();
        r2.setIdPergunta(2L);
        r2.setOpcaoEscolhida(2); // Correto

        submissao.setRespostas(Arrays.asList(r1, r2));

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuario));
        when(perguntaRepository.findById(1L)).thenReturn(Optional.of(pergunta1));
        when(perguntaRepository.findById(2L)).thenReturn(Optional.of(pergunta2));
        when(tentativaQuizRepository.save(any(TentativaQuiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act (Execução)
        TentativaQuiz resultado = quizService.processarEGuardarQuiz(submissao);

        // Assert (Verificação)
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalPerguntas());
        assertEquals(2, resultado.getTotalAcertos());
        assertEquals(0, resultado.getTotalErros());
        assertEquals(100.0, resultado.getPorcentagemAcerto());
        assertTrue(resultado.getPontosParaMelhorar().isEmpty());
    }

    @Test
    @DisplayName("Deve registrar erros e gerar pontos de melhoria quando errar respostas")
    void deveRegistrarErrosEPontosDeMelhoria() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("taina@email.com");

        SubmissaoQuizDTO submissao = new SubmissaoQuizDTO();
        submissao.setUsuario(usuario);
        submissao.setCategoria("Tecnologia");

        RespostaUsuario r1 = new RespostaUsuario();
        r1.setIdPergunta(1L);
        r1.setOpcaoEscolhida(0); // Errado (correto é 1)

        submissao.setRespostas(Arrays.asList(r1));

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuario));
        when(perguntaRepository.findById(1L)).thenReturn(Optional.of(pergunta1));
        when(tentativaQuizRepository.save(any(TentativaQuiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TentativaQuiz resultado = quizService.processarEGuardarQuiz(submissao);

        // Assert
        assertEquals(1, resultado.getTotalPerguntas());
        assertEquals(0, resultado.getTotalAcertos());
        assertEquals(1, resultado.getTotalErros());
        assertEquals(0.0, resultado.getPorcentagemAcerto());
        assertEquals(1, resultado.getPontosParaMelhorar().size());
        assertEquals("Revisar pergunta ID: 1", resultado.getPontosParaMelhorar().get(0));
    }
}
