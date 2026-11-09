package br.pucpr.organizador.mstarefa.service;

import br.pucpr.organizador.mstarefa.client.UsuarioClient;
import br.pucpr.organizador.mstarefa.domain.Tarefa;
import br.pucpr.organizador.mstarefa.messaging.TarefaCriadaPublisher;
import br.pucpr.organizador.mstarefa.repository.TarefaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository repository;
    private final UsuarioClient usuarioClient;
    private final TarefaCriadaPublisher publisher;

    public TarefaService(TarefaRepository repository,
                         UsuarioClient usuarioClient,
                         TarefaCriadaPublisher publisher) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
        this.publisher = publisher;
    }

    public List<Tarefa> listar() {
        return repository.findAll();
    }

    public Tarefa salvar(Tarefa tarefa) {
        if (tarefa.getUsuarioId() == null || !usuarioClient.existe(tarefa.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Usuario " + tarefa.getUsuarioId() + " nao existe");
        }
        tarefa.setId(null);
        Tarefa salva = repository.save(tarefa);
        publisher.publicar(salva);
        return salva;
    }
}