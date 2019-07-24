package nacatamalitosoft.com.cotracosanapps.Modelos;

import java.util.Date;

public class Abonos {
    public int getIdAbono() {
        return IdAbono;
    }

    public void setIdAbono(int idAbono) {
        IdAbono = idAbono;
    }

    public int getCreditoId() {
        return CreditoId;
    }

    public void setCreditoId(int creditoId) {
        CreditoId = creditoId;
    }

    public int getVehiculoId() {
        return VehiculoId;
    }

    public void setVehiculoId(int vehiculoId) {
        VehiculoId = vehiculoId;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public Date getFechaDeAbono() {
        return FechaDeAbono;
    }

    public void setFechaDeAbono(Date fechaDeAbono) {
        FechaDeAbono = fechaDeAbono;
    }

    public String getCodigoAbono() {
        return CodigoAbono;
    }

    public void setCodigoAbono(String codigoAbono) {
        CodigoAbono = codigoAbono;
    }

    public double getMontoDeAbono() {
        return MontoDeAbono;
    }

    public void setMontoDeAbono(double montoDeAbono) {
        MontoDeAbono = montoDeAbono;
    }

    public boolean isAbonoAnulado() {
        return AbonoAnulado;
    }

    public void setAbonoAnulado(boolean abonoAnulado) {
        AbonoAnulado = abonoAnulado;
    }

    public Abonos(int idAbono, int creditoId, int vehiculoId, String placa, Date fechaDeAbono, String codigoAbono, double montoDeAbono, boolean abonoAnulado) {
        IdAbono = idAbono;
        CreditoId = creditoId;
        VehiculoId = vehiculoId;
        Placa = placa;
        FechaDeAbono = fechaDeAbono;
        CodigoAbono = codigoAbono;
        MontoDeAbono = montoDeAbono;
        AbonoAnulado = abonoAnulado;
    }

    private int IdAbono;
    private int CreditoId;
    private int VehiculoId;
    private String Placa;
    private Date FechaDeAbono;
    private String CodigoAbono;
    private double MontoDeAbono;
    private boolean AbonoAnulado;
}
