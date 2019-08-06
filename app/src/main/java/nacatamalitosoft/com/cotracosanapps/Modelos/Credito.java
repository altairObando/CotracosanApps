package nacatamalitosoft.com.cotracosanapps.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Credito implements Serializable {

    public Credito()
    {

    }
    public Credito(int id, String codigoCredito, Date fecha, double montoTotal, double totalAbonado, int numeroAbonos, boolean creditoAnulado, boolean estadoCredito, ArrayList<DetalleDeCredito> detallesDeCreditos) {
        this.id = id;
        this.codigoCredito = codigoCredito;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
        this.totalAbonado = totalAbonado;
        this.numeroAbonos = numeroAbonos;
        this.creditoAnulado = creditoAnulado;
        this.estadoCredito = estadoCredito;
        this.detallesDeCreditos = detallesDeCreditos;
    }

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

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getTotalAbonado() {
        return totalAbonado;
    }

    public void setTotalAbonado(double totalAbonado) {
        this.totalAbonado = totalAbonado;
    }

    public int getNumeroAbonos() {
        return numeroAbonos;
    }

    public void setNumeroAbonos(int numeroAbonos) {
        this.numeroAbonos = numeroAbonos;
    }

    public boolean isCreditoAnulado() {
        return creditoAnulado;
    }

    public void setCreditoAnulado(boolean creditoAnulado) {
        this.creditoAnulado = creditoAnulado;
    }

    public boolean isEstadoCredito() {
        return estadoCredito;
    }

    public void setEstadoCredito(boolean estadoCredito) {
        this.estadoCredito = estadoCredito;
    }

    public ArrayList<DetalleDeCredito> getDetallesDeCreditos() {
        return detallesDeCreditos;
    }

    public void setDetallesDeCreditos(ArrayList<DetalleDeCredito> detallesDeCreditos) {
        this.detallesDeCreditos = detallesDeCreditos;
    }

    private int id;
    private String codigoCredito;
    private Date fecha;
    private double montoTotal;
    private double totalAbonado;
    private int numeroAbonos;
    private boolean creditoAnulado;
    private boolean estadoCredito;
    private ArrayList<DetalleDeCredito> detallesDeCreditos;

}
