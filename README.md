# 🎯 Quiz API (Spring Boot + PostgreSQL + Docker)

Uma API RESTful para gerenciamento, execução e armazenamento de histórico de quizzes, com suporte a importação dinâmica de perguntas a partir de arquivos JSON.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17 / Spring Boot 3** (REST API, Spring Data JPA)
- **PostgreSQL** (Banco de dados relacional)
- **Docker & Docker Compose** (Containerização do ambiente)
- **Jackson** (Processamento e parse de JSON)

---

## 📂 Estrutura de Dados & Persistência

### Armazenado no `perguntas.json`
As perguntas e suas respectivas opções ficam armazenadas externamente em um arquivo JSON na pasta `src/main/resources/perguntas.json`.

### Armazenado no PostgreSQL
O banco de dados relacional é responsável por salvar:
- **Usuários** (Nome, E-mail)
- **Tentativas e Histórico do Quiz** (Categoria/Ramo, Total de acertos/erros, Porcentagem de aproveitamento, Data de realização e Pontos de melhoria)
- **Perguntas e Opções Importadas** (Tabelas `perguntas` e `pergunta_opcoes`)

---

## 📑 Exemplo de Arquivo `perguntas.json`

Crie ou edite o arquivo em `src/main/resources/perguntas.json`:

```json
[
  {
    "id": 1,
    "enunciado": "Qual pilar da POO é usado para esconder detalhes de implementação?",
    "opcoes": [
      "Herança",
      "Encapsulamento",
      "Polimorfismo",
      "Abstração"
    ],
    "respostaCorreta": 1
  },
  {
    "id": 2,
    "enunciado": "Qual anotação do Spring Boot indica que uma classe é um controlador REST?",
    "opcoes": [
      "@Service",
      "@Component",
      "@RestController",
      "@Entity"
    ],
    "respostaCorreta": 2
  }
]

🚀 Endpoints da APIMétodoEndpointDescrição
POST  /api/quiz/importarImporta/ Atualiza as perguntas do perguntas.json para o PostgreSQL.

GET   /api/quiz/perguntas Retorna a lista completa de perguntas cadastradas no banco.

POST  /api/quiz/finalizar Recebe as respostas do usuário, calcula a nota e salva a tentativa no banco.

GET /api/quiz/historico Lista todas as tentativas registradas com estatísticas e usuários.

📝 Exemplo de Submissão (POST /api/quiz/finalizar)

{
  "usuario": {
    "nome": "Tainá Miranda",
    "email": "taina@email.com"
  },
  "categoria": "Concurso",
  "respostas": [
    {
      "idPergunta": 1,
      "opcaoEscolhida": 1
    },
    {
      "idPergunta": 2,
      "opcaoEscolhida": 2
    }
  ]
}

🐳 Como Rodar o Projeto com Docker
1. Subir os Containers (Backend + PostgreSQL)
docker compose up -d --build

2. Popular o Banco de Dados

curl -X POST http://localhost:8080/api/quiz/importar

3. Parar os Containers

docker compose down

