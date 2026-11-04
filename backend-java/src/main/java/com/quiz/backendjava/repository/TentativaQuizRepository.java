package com.quiz.backendjava.repository;

import com.quiz.backendjava.TentativaQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TentativaQuizRepository extends JpaRepository<TentativaQuiz, Long> {
    List<TentativaQuiz> findByUsuarioId(Long usuarioId);
}