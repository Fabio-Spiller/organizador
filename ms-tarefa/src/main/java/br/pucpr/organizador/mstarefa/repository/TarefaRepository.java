package br.pucpr.organizador.mstarefa.repository;

import br.pucpr.organizador.mstarefa.domain.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}