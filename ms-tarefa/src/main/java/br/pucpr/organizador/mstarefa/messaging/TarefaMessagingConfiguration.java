package br.pucpr.organizador.mstarefa.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarefaMessagingConfiguration {

    public static final String FILA_TAREFA_CRIADA = "tarefa.criada";

    @Bean
    public Queue tarefaCriadaQueue() {
        return new Queue(FILA_TAREFA_CRIADA, true);
    }
}