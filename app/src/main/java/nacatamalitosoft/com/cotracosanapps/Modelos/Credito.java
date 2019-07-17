package nacatamalitosoft.com.cotracosanapps.Modelos;

import java.util.Date;
import java.util.List;

public class Credito {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoCredito() {
        return codigoCredito;
    }

    public void setCodigoCredito(String codigoCredito) {
        this.codigoCredito = codigoCredito;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(float montoTotal) {
        this.montoTotal = montoTotal;
    }

    public float getTotalAbonado() {
        return totalAbonado;
    }

    public void setTotalAbonado(float totalAbonado) {
        this.totalAbonado = totalAbonado;
    }

    public int getNumeroAbonos() {
        return numeroAbonos;
    }

    public void setNumeroAbonos(int numeroAbonos) {
        this.numeroAbonos = numeroAbonos;
    }

    public boolean isEstadoCredito() {
        return estadoCredito;
    }

    public void setEstadoCredito(boolean estadoCredito) {
        this.estadoCredito = estadoCredito;
    }

    public List<DetalleDeCredito> getDetallesDeCreditos() {
        return detallesDeCreditos;
    }

    public void setDetallesDeCreditos(List<DetalleDeCredito> detallesDeCreditos) {
        this.detallesDeCreditos = detallesDeCreditos;
    }

    public Credito(int id, String codigoCredito, Date fecha, float montoTotal, float totalAbonado, int numeroAbonos, boolean estadoCredito, List<DetalleDeCredito> detallesDeCreditos) {
        this.id = id;
        this.codigoCredito = codigoCredito;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
        this.totalAbonado = totalAbonado;
        this.numeroAbonos = numeroAbonos;
        this.estadoCredito = estadoCredito;
        this.detallesDeCreditos = detallesDeCreditos;
    }

    private int id;
    private String codigoCredito;
    private Date fecha;
    private float montoTotal;
    private float totalAbonado;
    private int numeroAbonos;
    private boolean estadoCredito;
    private List<DetalleDeCredito> detallesDeCreditos;

}
