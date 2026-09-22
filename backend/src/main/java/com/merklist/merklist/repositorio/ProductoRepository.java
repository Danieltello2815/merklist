package com.merklist.merklist.repositorio;

import com.merklist.merklist.modelo.Producto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/merklist";
    private static final String USUARIO = "merklist_user";
    private static final String PASSWORD = "Software2026!";

    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT id, nombre, marca, tipo_producto_id FROM producto";

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
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
            e.printStackTrace();
        }

        return productos;
    }

    public Producto obtenerPorId(int id) {
        String sql = "SELECT id, nombre, marca, tipo_producto_id FROM producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
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
            e.printStackTrace();
        }

        return null;
    }

    public Producto crear(Producto producto) {
        String sql = "INSERT INTO producto (nombre, marca, tipo_producto_id) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

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
            e.printStackTrace();
        }

        return producto;
    }

    public Producto actualizar(int id, Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, marca = ?, tipo_producto_id = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getMarca());
            statement.setInt(3, producto.getTipoProductoId());
            statement.setInt(4, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        producto.setId(id);
        return producto;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

