# Organizador de Tarefas — Cloud Native com Microsserviços

Avaliação Somativa N2 — Backend: Cloud Computing (PUCPR)

| Microsserviço | Responsável | Linguagem | Banco | Porta |
|---|---|---|---|---|
| ms-usuario | Vinícius Wamser | Java (Spring Boot) | PostgreSQL | 8082 |
| ms-tarefa | Lucas Gabriel Nunes dos Santos | Java (Spring Boot) | MySQL | 8081 |
| ms-notificacao | Fabio Henrique Spiller Veloso | Python (FastAPI) | — | 8000 |

## Arquitetura

```text
Cliente
   |
   v
AWS Load Balancer -> NGINX Ingress Controller -> Ingress
   |
   +--> /usuarios      --> Service ms-usuario     --> Pod ms-usuario --> PostgreSQL
   +--> /tarefas       --> Service ms-tarefa      --> 3 Pods ms-tarefa --> MySQL
   +--> /notificacoes  --> Service ms-notificacao --> Pod ms-notificacao

ms-tarefa --REST (GET /usuarios/{id})--> ms-usuario
ms-tarefa --RabbitMQ (fila tarefa.criada)--> ms-notificacao
```

## Fluxo principal

1. `POST /usuarios` cria um usuário (PostgreSQL).
2. `POST /tarefas` consulta o ms-usuario via REST, salva a tarefa (MySQL) e publica `TAREFA_CRIADA` no RabbitMQ.
3. O ms-notificacao consome a mensagem e registra a notificação (`GET /notificacoes`).

## Estrutura

```text
organizador/
├── ms-usuario/       (Java + PostgreSQL)
├── ms-tarefa/        (Java + MySQL)
├── ms-notificacao/   (Python)
├── k8s/              (manifests Kubernetes)
└── aws/              (configuração do cluster EKS)
```
