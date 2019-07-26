package nacatamalitosoft.com.cotracosanapps.Modelos;

import java.io.Serializable;

public class Articulos implements Serializable {
    private int id;
    private String codigo;
    private String descripcion;
    private double precio;
    public int Cantidad;
    public Articulos(int id, String codigo, String descripcion, double precio) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
