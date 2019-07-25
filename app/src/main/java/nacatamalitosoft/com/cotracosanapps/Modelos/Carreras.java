package nacatamalitosoft.com.cotracosanapps.Modelos;

import java.io.Serializable;

public class Carreras implements Serializable {


    public Carreras(int id, String codigoCarrera, String fechaDeCarrera, double montoRecaudado,
                    double multa, String conductor, int conductorId, String vehiculo, int vehiculoId,
                    String lugarFinalRecorrido, String horaDeLlegada, String turno, boolean carreraAnulada,
                    double montoRestante) {
        Id = id;
        CodigoCarrera = codigoCarrera;
        FechaDeCarrera = fechaDeCarrera;
        MontoRecaudado = montoRecaudado;
        Multa = multa;
        Conductor = conductor;
        ConductorId = conductorId;
        Vehiculo = vehiculo;
        VehiculoId = vehiculoId;
        LugarFinalRecorrido = lugarFinalRecorrido;
        HoraDeLlegada = horaDeLlegada;
        Turno = turno;
        CarreraAnulada = carreraAnulada;
        MontoRestante = montoRestante;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCodigoCarrera() {
        return CodigoCarrera;
    }

    public void setCodigoCarrera(String codigoCarrera) {
        CodigoCarrera = codigoCarrera;
    }

    public String getFechaDeCarrera() {
        return FechaDeCarrera;
    }

    public void setFechaDeCarrera(String fechaDeCarrera) {
        FechaDeCarrera = fechaDeCarrera;
    }

    public double getMontoRecaudado() {
        return MontoRecaudado;
    }

    public void setMontoRecaudado(double montoRecaudado) {
        MontoRecaudado = montoRecaudado;
    }

    public double getMulta() {
        return Multa;
    }

    public void setMulta(double multa) {
        Multa = multa;
    }

    public String getConductor() {
        return Conductor;
    }

    public void setConductor(String conductor) {
        Conductor = conductor;
    }

    public int getConductorId() {
        return ConductorId;
    }

    public void setConductorId(int conductorId) {
        ConductorId = conductorId;
    }

    public String getVehiculo() {
        return Vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        Vehiculo = vehiculo;
    }

    public int getVehiculoId() {
        return VehiculoId;
    }

    public void setVehiculoId(int vehiculoId) {
        VehiculoId = vehiculoId;
    }

    public String getLugarFinalRecorrido() {
        return LugarFinalRecorrido;
    }

    public void setLugarFinalRecorrido(String lugarFinalRecorrido) {
        LugarFinalRecorrido = lugarFinalRecorrido;
    }

    public String getHoraDeLlegada() {
        return HoraDeLlegada;
    }

    public void setHoraDeLlegada(String horaDeLlegada) {
        HoraDeLlegada = horaDeLlegada;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String turno) {
        Turno = turno;
    }

    public boolean getCarreraAnulada() {
        return CarreraAnulada;
    }

    public void setCarreraAnulada(boolean carreraAnulada) {
        CarreraAnulada = carreraAnulada;
    }

    public double getMontoRestante() {
        return MontoRestante;
    }

    public void setMontoRestante(double montoRestante) {
        MontoRestante = montoRestante;
    }

    private int Id;
    private String CodigoCarrera;
    private String FechaDeCarrera;
    private double MontoRecaudado;
    private double Multa;
    private String Conductor;
    private int ConductorId;
    private String Vehiculo;
    private int VehiculoId;
    private String LugarFinalRecorrido;
    private String HoraDeLlegada;
    private String Turno;
    private boolean CarreraAnulada;
    private double MontoRestante;
}
