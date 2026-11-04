package com.merklist.merklist.repositorio.jpa;

import com.merklist.merklist.modelo.ListaMercado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListaMercadoRepository extends JpaRepository<ListaMercado, Integer> {

    @Override
    @EntityGraph(attributePaths = "items")
    Optional<ListaMercado> findById(Integer id);

    @EntityGraph(attributePaths = "items")
    List<ListaMercado> findByUsuarioId(Integer usuarioId);
}