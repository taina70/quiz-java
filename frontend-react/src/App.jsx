import { useState, useEffect } from "react";
import { quizService } from "./services/api";
import "./App.css";

export default function App() {
  const [tela, setTela] = useState("inicio"); // inicio, quiz, resultado, historico
  const [usuario, setUsuario] = useState({ nome: "", email: "" });
  const [categoria, setCategoria] = useState("Tecnologia");
  const [perguntas, setPerguntas] = useState([]);
  const [respostas, setRespostas] = useState({});
  const [resultado, setResultado] = useState(null);
  const [historico, setHistorico] = useState([]);
  const [loading, setLoading] = useState(false);

  // Carregar perguntas ao iniciar o Quiz
  const iniciarQuiz = async (e) => {
    e.preventDefault();
    if (!usuario.nome || !usuario.email)
      return alert("Preencha todos os campos!");

    setLoading(true);
    try {
      const dadosPerguntas = await quizService.getPerguntas();
      setPerguntas(dadosPerguntas);
      setTela("quiz");
    } catch (err) {
      alert(
        "Erro ao carregar perguntas da API. Certifique-se de que rodou o /importar no backend!",
      );
    } finally {
      setLoading(false);
    }
  };

  // Selecionar uma opção de resposta
  const selecionarResposta = (idPergunta, opcaoIndex) => {
    setRespostas({ ...respostas, [idPergunta]: opcaoIndex });
  };

  // Enviar respostas para o backend
  const submeterQuiz = async () => {
    if (Object.keys(respostas).length < perguntas.length) {
      if (
        !confirm(
          "Você deixou perguntas sem responder. Deseja finalizar assim mesmo?",
        )
      )
        return;
    }

    const payload = {
      usuario,
      categoria,
      respostas: Object.entries(respostas).map(
        ([idPergunta, opcaoEscolhida]) => ({
          idPergunta: Number(idPergunta),
          opcaoEscolhida,
        }),
      ),
    };

    setLoading(true);
    try {
      const res = await quizService.finalizarQuiz(payload);
      setResultado(res);
      setTela("resultado");
    } catch (err) {
      alert("Erro ao enviar respostas para a API.");
    } finally {
      setLoading(false);
    }
  };

  // Carregar histórico do banco de dados
  const carregarHistorico = async () => {
    setLoading(true);
    try {
      const dados = await quizService.getHistorico();
      setHistorico(dados);
      setTela("historico");
    } catch (err) {
      alert("Erro ao buscar histórico.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      {/* TELA DE INÍCIO */}
      {tela === "inicio" && (
        <form onSubmit={iniciarQuiz}>
          <h1>🎯 Quiz App</h1>
          <div className="form-group">
            <label>Seu Nome:</label>
            <input
              type="text"
              required
              value={usuario.nome}
              onChange={(e) => setUsuario({ ...usuario, nome: e.target.value })}
            />
          </div>
          <div className="form-group">
            <label>Seu E-mail:</label>
            <input
              type="email"
              required
              value={usuario.email}
              onChange={(e) =>
                setUsuario({ ...usuario, email: e.target.value })
              }
            />
          </div>
          <div className="form-group">
            <label>Categoria:</label>
            <select
              value={categoria}
              onChange={(e) => setCategoria(e.target.value)}>
              <option value="Tecnologia">Tecnologia</option>
              <option value="Concurso">Concurso</option>
              <option value="Geral">Geral</option>
            </select>
          </div>
          <button type="submit" disabled={loading}>
            {loading ? "Carregando..." : "Iniciar Quiz"}
          </button>
          <button
            type="button"
            className="btn-secondary"
            onClick={carregarHistorico}>
            Ver Histórico
          </button>
        </form>
      )}

      {/* TELA DO QUIZ */}
      {tela === "quiz" && (
        <div>
          <h2>Respondendo Quiz: {categoria}</h2>
          {perguntas.map((p, index) => (
            <div key={p.id} style={{ marginBottom: "25px" }}>
              <p>
                <strong>
                  {index + 1}. {p.enunciado}
                </strong>
              </p>
              <div className="opcoes-container">
                {p.opcoes.map((op, opIdx) => (
                  <button
                    key={opIdx}
                    type="button"
                    className={`opcao-btn ${respostas[p.id] === opIdx ? "selecionada" : ""}`}
                    onClick={() => selecionarResposta(p.id, opIdx)}>
                    {op}
                  </button>
                ))}
              </div>
            </div>
          ))}
          <button onClick={submeterQuiz} disabled={loading}>
            {loading ? "Processando..." : "Finalizar Quiz"}
          </button>
        </div>
      )}

      {/* TELA DE RESULTADO */}
      {tela === "resultado" && resultado && (
        <div>
          <h2>Resultado Final</h2>
          <div className="resultado-card">
            <h3>{resultado.porcentagemAcerto}% de Aproveitamento</h3>
            <p>Total de Perguntas: {resultado.totalPerguntas}</p>
            <p>
              Acertos: {resultado.totalAcertos} | Erros: {resultado.totalErros}
            </p>
          </div>

          {resultado.pontosParaMelhorar.length > 0 && (
            <div className="pontos-melhoria">
              <h4>📌 Temas para Revisão:</h4>
              <ul>
                {resultado.pontosParaMelhorar.map((ponto, i) => (
                  <li key={i}>{ponto}</li>
                ))}
              </ul>
            </div>
          )}

          <button onClick={() => setTela("inicio")}>Fazer Outro Quiz</button>
          <button
            type="button"
            className="btn-secondary"
            onClick={carregarHistorico}>
            Ver Histórico Completo
          </button>
        </div>
      )}

      {/* TELA DE HISTÓRICO */}
      {tela === "historico" && (
        <div>
          <h2>Histórico de Tentativas</h2>
          {historico.length === 0 ? (
            <p>Nenhuma tentativa registrada ainda.</p>
          ) : (
            historico.map((item) => (
              <div key={item.id} className="historico-item">
                <p>
                  <strong>Usuário:</strong> {item.usuario?.nome || "Anônimo"} (
                  {item.categoria})
                </p>
                <p>
                  <strong>Aproveitamento:</strong> {item.porcentagemAcerto}% (
                  {item.totalAcertos}/{item.totalPerguntas} acertos)
                </p>
                <p>
                  <small>
                    Data:{" "}
                    {new Date(item.dataRealizacao).toLocaleString("pt-BR")}
                  </small>
                </p>
              </div>
            ))
          )}
          <button
            onClick={() => setTela("inicio")}
            style={{ marginTop: "20px" }}>
            Voltar ao Início
          </button>
        </div>
      )}
    </div>
  );
}
