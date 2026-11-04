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