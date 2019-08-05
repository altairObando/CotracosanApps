package nacatamalitosoft.com.cotracosanapps.Modelos;

public class AbonoSubClass {
    public int getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(int idAbono) {
        this.idAbono = idAbono;
    }

    public int getCreditoId() {
        return creditoId;
    }

    public void setCreditoId(int creditoId) {
        this.creditoId = creditoId;
    }

    public String getCodigoAbono() {
        return codigoAbono;
    }

    public void setCodigoAbono(String codigoAbono) {
        this.codigoAbono = codigoAbono;
    }

    public String getFechaAbono() {
        return fechaAbono;
    }

    public void setFechaAbono(String fechaAbono) {
        this.fechaAbono = fechaAbono;
    }

    public Double getMontoAbono() {
        return montoAbono;
    }

    public void setMontoAbono(Double montoAbono) {
        this.montoAbono = montoAbono;
    }

    public AbonoSubClass(int idAbono, int creditoId, String codigoAbono, String fechaAbono, Double montoAbono) {
        this.idAbono = idAbono;
        this.creditoId = creditoId;
        this.codigoAbono = codigoAbono;
        this.fechaAbono = fechaAbono;
        this.montoAbono = montoAbono;
    }

    private int idAbono;
    private int creditoId;
    private String codigoAbono;
    private String fechaAbono;
    private Double montoAbono;
}
