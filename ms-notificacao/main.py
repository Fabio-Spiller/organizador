import os
import threading
import time
from datetime import datetime

import pika
from fastapi import FastAPI

# Conexao com o RabbitMQ (no Kubernetes os valores vem do YAML / Secret)
RABBITMQ_HOST = os.getenv("RABBITMQ_HOST", "localhost")
RABBITMQ_PORT = int(os.getenv("RABBITMQ_PORT", "5672"))
RABBITMQ_USER = os.getenv("RABBITMQ_USER", "organizador")
RABBITMQ_PASSWORD = os.getenv("RABBITMQ_PASSWORD", "change-me-lab")
FILA = "tarefa.criada"

app = FastAPI(title="ms-notificacao")

# Notificacoes recebidas (em memoria; reinicia vazia se o Pod reiniciar)
notificacoes = []


@app.get("/notificacoes")
def listar_notificacoes():
    return notificacoes


def processar_mensagem(canal, metodo, propriedades, corpo):
    mensagem = corpo.decode("utf-8")
    print(f"[NOTIFICACAO] Iniciando processamento: {mensagem}")

    # Formato: TAREFA_CRIADA|id|titulo|usuarioId
    partes = mensagem.split("|")
    notificacao = {
        "evento": partes[0],
        "tarefaId": partes[1] if len(partes) > 1 else None,
        "titulo": partes[2] if len(partes) > 2 else None,
        "usuarioId": partes[3] if len(partes) > 3 else None,
        "recebidaEm": datetime.now().isoformat(timespec="seconds"),
    }

    time.sleep(1.5)  # simula o envio de um e-mail
    notificacoes.append(notificacao)
    print(f"[NOTIFICACAO] Concluido: usuario {notificacao['usuarioId']} "
          f"avisado sobre a tarefa '{notificacao['titulo']}'")

    canal.basic_ack(delivery_tag=metodo.delivery_tag)


def consumir_fila():
    credenciais = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASSWORD)
    parametros = pika.ConnectionParameters(
        host=RABBITMQ_HOST, port=RABBITMQ_PORT, credentials=credenciais
    )

    while True:
        try:
            conexao = pika.BlockingConnection(parametros)
            canal = conexao.channel()
            canal.queue_declare(queue=FILA, durable=True)
            canal.basic_qos(prefetch_count=1)
            canal.basic_consume(queue=FILA, on_message_callback=processar_mensagem)
            print(f"[NOTIFICACAO] Conectado ao RabbitMQ. Aguardando mensagens na fila {FILA}...")
            canal.start_consuming()
        except Exception as erro:
            print(f"[NOTIFICACAO] RabbitMQ indisponivel ({erro!r}). Nova tentativa em 5 segundos...")
            time.sleep(5)


# Inicia o consumidor em segundo plano, junto com a API
threading.Thread(target=consumir_fila, daemon=True).start()
