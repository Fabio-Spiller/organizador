from fastapi import FastAPI

app = FastAPI(title="ms-notificacao")

# Notificacoes recebidas (em memoria; reinicia vazia se o Pod reiniciar)
notificacoes = []


@app.get("/notificacoes")
def listar_notificacoes():
    return notificacoes
