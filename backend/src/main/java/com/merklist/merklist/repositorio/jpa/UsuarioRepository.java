package com.merklist.merklist.repositorio.jpa;

import com.merklist.merklist.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCorreoIgnoreCase(String correo);
}
