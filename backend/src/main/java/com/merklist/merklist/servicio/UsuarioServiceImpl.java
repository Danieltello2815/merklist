package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.OperacionNoPermitidaException;
import com.merklist.merklist.excepcion.RecursoDuplicadoException;
import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.Usuario;
import com.merklist.merklist.repositorio.jpa.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con id " + id));
    }

    @Override
    @Transactional
    public Usuario crear(Usuario usuario) {
        validar(usuario);
        usuario.setId(null);
        usuario.setNombre(usuario.getNombre().trim());
        usuario.setCorreo(usuario.getCorreo().trim());
        if (usuarioRepository.existsByCorreoIgnoreCase(usuario.getCorreo())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el correo " + usuario.getCorreo());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizar(int id, Usuario datos) {
        Usuario actual = obtenerPorId(id);
        validar(datos);
        String correoNuevo = datos.getCorreo().trim();
        if (!actual.getCorreo().equalsIgnoreCase(correoNuevo)
                && usuarioRepository.existsByCorreoIgnoreCase(correoNuevo)) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el correo " + correoNuevo);
        }
        actual.setNombre(datos.getNombre().trim());
        actual.setCorreo(correoNuevo);
        if (datos.getPerfil() != null) {
            if (actual.getPerfil() == null) {
                actual.setPerfil(datos.getPerfil());
            } else {
                actual.getPerfil().setTelefono(datos.getPerfil().getTelefono());
                actual.getPerfil().setCiudad(datos.getPerfil().getCiudad());
            }
        }
        return usuarioRepository.save(actual);
    }

    @Override
    @Transactional
    public void eliminar(int id) {
        Usuario usuario = obtenerPorId(id);
        if (!usuario.getListas().isEmpty()) {
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar el usuario con id " + id + " porque tiene listas de mercado");
        }
        usuarioRepository.delete(usuario);
    }

    private void validar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        if (usuario.getCorreo() == null || !usuario.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo del usuario no es válido");
        }
    }
}