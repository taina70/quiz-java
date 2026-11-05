const API_BASE_URL = "http://localhost:8080/api/quiz";

export const quizService = {
  // Buscar perguntas
  getPerguntas: async () => {
    const response = await fetch(`${API_BASE_URL}/perguntas`);
    if (!response.ok) throw new Error("Erro ao buscar perguntas");
    return response.json();
  },

  cadastrarPergunta: async (dadosPergunta) => {
  const response = await fetch(`${API_BASE_URL}/perguntas`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dadosPergunta),
  });
  
  if (!response.ok) throw new Error('Erro ao cadastrar pergunta');
  return response.json();
  },
  
  atualizarPergunta: async (id, dadosPergunta) => {
    const response = await fetch(`${API_BASE_URL}/perguntas/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(dadosPergunta),
    });
    if (!response.ok) throw new Error('Erro ao atualizar pergunta');
    return response.json();
  },

  deletarPergunta: async (id) => {
    const response = await fetch(`${API_BASE_URL}/perguntas/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Erro ao deletar pergunta');
  },

  getHistoricoPorUsuario: async (usuarioId) => {
    const response = await fetch(`${API_BASE_URL}/historico/usuario/${usuarioId}`);
    if (!response.ok) throw new Error('Erro ao buscar histórico do usuário');
    return response.json();
  },

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
