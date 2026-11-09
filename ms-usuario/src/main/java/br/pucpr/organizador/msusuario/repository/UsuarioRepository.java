package br.pucpr.organizador.msusuario.repository;

import br.pucpr.organizador.msusuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}