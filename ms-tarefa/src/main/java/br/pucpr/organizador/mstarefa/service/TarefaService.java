package br.pucpr.organizador.mstarefa.service;

import br.pucpr.organizador.mstarefa.domain.Tarefa;
import br.pucpr.organizador.mstarefa.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    public List<Tarefa> listar() {
        return repository.findAll();
    }

    public Tarefa salvar(Tarefa tarefa) {
        tarefa.setId(null);
        return repository.save(tarefa);
    }
}