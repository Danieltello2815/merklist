package com.merklist.merklist.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "item_lista")
public class ItemLista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private EstadoItem estado = EstadoItem.PENDIENTE;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lista_id", nullable = false)
    private ListaMercado lista;

    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    public ItemLista() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoItem getEstado() {
        return estado;
    }

    public void setEstado(EstadoItem estado) {
        this.estado = estado;
    }

    public ListaMercado getLista() {
        return lista;
    }

    public void setLista(ListaMercado lista) {
        this.lista = lista;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
}