package com.merklist.merklist.servicio;

import com.merklist.merklist.modelo.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listar();

    Usuario obtenerPorId(int id);

    Usuario crear(Usuario usuario);

    Usuario actualizar(int id, Usuario usuario);

    void eliminar(int id);
}