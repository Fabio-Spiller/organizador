package br.pucpr.organizador.mstarefa.messaging;

import br.pucpr.organizador.mstarefa.domain.Tarefa;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TarefaCriadaPublisher {

    private final RabbitTemplate rabbit;

    public TarefaCriadaPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publicar(Tarefa tarefa) {
        String mensagem = "TAREFA_CRIADA|" + tarefa.getId() + "|"
                + tarefa.getTitulo() + "|" + tarefa.getUsuarioId();
        rabbit.convertAndSend("", TarefaMessagingConfiguration.FILA_TAREFA_CRIADA, mensagem);
        System.out.println("[TAREFA] Mensagem publicada: " + mensagem);
    }
}