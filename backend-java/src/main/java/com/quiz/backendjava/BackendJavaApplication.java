package com.quiz.backendjava;

import com.quiz.backendjava.repository.CategoriaRepository;
import com.quiz.backendjava.repository.PerguntaRepository;
import com.quiz.backendjava.servico.QuizService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendJavaApplication.class, args);
    }

    @Bean
    public CommandLineRunner carregarDadosIniciais(
            QuizService quizService, 
            PerguntaRepository perguntaRepository,
            CategoriaRepository categoriaRepository) {
        return args -> {
            // Cadastra a categoria padrão "Tecnologia" (ID 1) se não houver categorias
            if (categoriaRepository.count() == 0) {
                System.out.println("--> Cadastrando categoria padrão 'Tecnologia'...");
                categoriaRepository.save(new Categoria("Tecnologia"));
            }

            // Importa as perguntas do JSON se a tabela estiver vazia
            if (perguntaRepository.count() == 0) {
                System.out.println("--> Banco vazio! Importando perguntas do JSON automaticamente...");
                String resultado = quizService.importarPerguntasDoJson();
                System.out.println("--> " + resultado);
            }
        };
    }
}