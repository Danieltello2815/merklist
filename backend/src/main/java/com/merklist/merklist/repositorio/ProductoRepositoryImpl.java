package com.merklist.merklist.repositorio;

import com.merklist.merklist.modelo.Producto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepositoryImpl implements ProductoRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String usuario;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, marca, tipo_producto_id FROM producto";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setId(resultSet.getInt("id"));
                producto.setNombre(resultSet.getString("nombre"));
                producto.setMarca(resultSet.getString("marca"));
                producto.setTipoProductoId(resultSet.getInt("tipo_producto_id"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos", e);
        }
        return productos;
    }

    @Override
    public Producto obtenerPorId(int id) {
        String sql = "SELECT id, nombre, marca, tipo_producto_id FROM producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Producto producto = new Producto();
                    producto.setId(resultSet.getInt("id"));
                    producto.setNombre(resultSet.getString("nombre"));
                    producto.setMarca(resultSet.getString("marca"));
                    producto.setTipoProductoId(resultSet.getInt("tipo_producto_id"));
                    return producto;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto con id " + id, e);
        }
        return null;
    }

    @Override
    public Producto crear(Producto producto) {
        String sql = "INSERT INTO producto (nombre, marca, tipo_producto_id) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getMarca());
            statement.setInt(3, producto.getTipoProductoId());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    producto.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear producto", e);
        }
        return producto;
    }

    @Override
    public Producto actualizar(int id, Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, marca = ?, tipo_producto_id = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getMarca());
            statement.setInt(3, producto.getTipoProductoId());
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto con id " + id, e);
        }
        producto.setId(id);
        return producto;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto con id " + id, e);
        }
    }
}