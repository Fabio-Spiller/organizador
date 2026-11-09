package br.pucpr.organizador.msusuario.service;

import br.pucpr.organizador.msusuario.domain.Usuario;
import br.pucpr.organizador.msusuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Optional<Usuario> buscar(Long id) {
        return repository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        usuario.setId(null);
        return repository.save(usuario);
    }
}