const API_BASE_URL = "http://localhost:8080/api/quiz";

export const quizService = {
  // Buscar perguntas
  getPerguntas: async () => {
    const response = await fetch(`${API_BASE_URL}/perguntas`);
    if (!response.ok) throw new Error("Erro ao buscar perguntas");
    return response.json();
  },

  // Enviar respostas e calcular resultado
  finalizarQuiz: async (dadosSubmissao) => {
    const response = await fetch(`${API_BASE_URL}/finalizar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dadosSubmissao),
    });
    if (!response.ok) throw new Error("Erro ao finalizar quiz");
    return response.json();
  },

  // Obter histórico de tentativas
  getHistorico: async () => {
    const response = await fetch(`${API_BASE_URL}/historico`);
    if (!response.ok) throw new Error("Erro ao buscar histórico");
    return response.json();
  },
};
