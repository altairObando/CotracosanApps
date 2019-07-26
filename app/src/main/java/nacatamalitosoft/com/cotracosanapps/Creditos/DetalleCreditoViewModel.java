package nacatamalitosoft.com.cotracosanapps.Creditos;

import java.io.Serializable;

public class DetalleCreditoViewModel implements Serializable {
    private int id;
    private int cantidad;
    private int creditoId;
    private int articuloId;

    public DetalleCreditoViewModel(int id, int cantidad, int creditoId, int articuloId) {
        this.id = id;
        this.cantidad = cantidad;
        this.creditoId = creditoId;
        this.articuloId = articuloId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCreditoId() {
        return creditoId;
    }

    public void setCreditoId(int creditoId) {
        this.creditoId = creditoId;
    }

    public int getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(int articuloId) {
        this.articuloId = articuloId;
    }
}
