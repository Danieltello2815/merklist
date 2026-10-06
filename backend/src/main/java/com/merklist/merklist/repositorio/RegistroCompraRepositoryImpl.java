package com.merklist.merklist.repositorio;

import com.merklist.merklist.modelo.RegistroCompra;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegistroCompraRepositoryImpl implements RegistroCompraRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String usuario;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public List<RegistroCompra> listar() {
        List<RegistroCompra> lista = new ArrayList<>();
        String sql = "SELECT id, precio, fecha, lugar_compra, producto_id FROM registro_compra";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                lista.add(mapResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar registros de compra", e);
        }
        return lista;
    }

    @Override
    public RegistroCompra obtenerPorId(int id) {
        String sql = "SELECT id, precio, fecha, lugar_compra, producto_id FROM registro_compra WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar registro de compra con id " + id, e);
        }
        return null;
    }

    @Override
    public List<RegistroCompra> listarPorProductoId(int productoId) {
        List<RegistroCompra> lista = new ArrayList<>();
        String sql = "SELECT id, precio, fecha, lugar_compra, producto_id FROM registro_compra WHERE producto_id = ? ORDER BY fecha DESC, id DESC";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productoId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(mapResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar historial del producto " + productoId, e);
        }
        return lista;
    }

    @Override
    public RegistroCompra crear(RegistroCompra registroCompra) {
        String sql = "INSERT INTO registro_compra (precio, fecha, lugar_compra, producto_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setDouble(1, registroCompra.getPrecio());
            statement.setDate(2, Date.valueOf(registroCompra.getFecha() != null ? registroCompra.getFecha() : LocalDate.now()));
            statement.setString(3, registroCompra.getLugarCompra());
            statement.setInt(4, registroCompra.getProductoId());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    registroCompra.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear registro de compra", e);
        }
        return registroCompra;
    }

    @Override
    public RegistroCompra actualizar(int id, RegistroCompra registroCompra) {
        String sql = "UPDATE registro_compra SET precio = ?, fecha = ?, lugar_compra = ?, producto_id = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, registroCompra.getPrecio());
            statement.setDate(2, Date.valueOf(registroCompra.getFecha()));
            statement.setString(3, registroCompra.getLugarCompra());
            statement.setInt(4, registroCompra.getProductoId());
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar registro de compra con id " + id, e);
        }
        registroCompra.setId(id);
        return registroCompra;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM registro_compra WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar registro de compra con id " + id, e);
        }
    }

    private RegistroCompra mapResultSet(ResultSet resultSet) throws SQLException {
        RegistroCompra registro = new RegistroCompra();
        registro.setId(resultSet.getInt("id"));
        registro.setPrecio(resultSet.getDouble("precio"));
        Date fechaSql = resultSet.getDate("fecha");
        if (fechaSql != null) {
            registro.setFecha(fechaSql.toLocalDate());
        }
        registro.setLugarCompra(resultSet.getString("lugar_compra"));
        registro.setProductoId(resultSet.getInt("producto_id"));
        return registro;
    }
}
