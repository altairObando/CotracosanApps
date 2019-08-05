package nacatamalitosoft.com.cotracosanapps.Modelos;

public class ArticuloSubClass {

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

    public double getGasto() {
        return gasto;
    }

    public void setGasto(double gasto) {
        this.gasto = gasto;
    }

    public ArticuloSubClass(String codigo, String descripcion, double gasto) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.gasto = gasto;
    }

    private String codigo;
    private String descripcion;
    private double gasto;
}
