package com.merklist.merklist.repositorio;

import com.merklist.merklist.modelo.TipoProducto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TipoProductoRepositoryImpl implements TipoProductoRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String usuario;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public List<TipoProducto> listar() {
        List<TipoProducto> tipos = new ArrayList<>();
        String sql = "SELECT id, nombre FROM tipo_producto";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                TipoProducto tipo = new TipoProducto();
                tipo.setId(resultSet.getInt("id"));
                tipo.setNombre(resultSet.getString("nombre"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar tipos de producto", e);
        }
        return tipos;
    }

    @Override
    public TipoProducto obtenerPorId(int id) {
        String sql = "SELECT id, nombre FROM tipo_producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    TipoProducto tipo = new TipoProducto();
                    tipo.setId(resultSet.getInt("id"));
                    tipo.setNombre(resultSet.getString("nombre"));
                    return tipo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tipo de producto con id " + id, e);
        }
        return null;
    }

    @Override
    public boolean existePorNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM tipo_producto WHERE LOWER(nombre) = LOWER(?)";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el nombre del tipo de producto", e);
        }
    }

    @Override
    public TipoProducto crear(TipoProducto tipoProducto) {
        String sql = "INSERT INTO tipo_producto (nombre) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, tipoProducto.getNombre());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    tipoProducto.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear tipo de producto", e);
        }
        return tipoProducto;
    }

    @Override
    public TipoProducto actualizar(int id, TipoProducto tipoProducto) {
        String sql = "UPDATE tipo_producto SET nombre = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tipoProducto.getNombre());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tipo de producto con id " + id, e);
        }
        tipoProducto.setId(id);
        return tipoProducto;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM tipo_producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar tipo de producto con id " + id, e);
        }
    }
}