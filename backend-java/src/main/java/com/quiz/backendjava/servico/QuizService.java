package com.quiz.backendjava.servico;

import com.quiz.backendjava.Pergunta;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    public List<Pergunta> carregarPerguntas() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("perguntas.json"), new TypeReference<List<Pergunta>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}