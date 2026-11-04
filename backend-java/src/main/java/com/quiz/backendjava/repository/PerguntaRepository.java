package com.quiz.backendjava.repository;

import com.quiz.backendjava.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
}