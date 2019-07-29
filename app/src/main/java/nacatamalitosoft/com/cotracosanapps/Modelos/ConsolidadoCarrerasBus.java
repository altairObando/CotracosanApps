package nacatamalitosoft.com.cotracosanapps.Modelos;

public class ConsolidadoCarrerasBus {
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getMonto() {
        return Monto;
    }

    public void setMonto(double monto) {
        Monto = monto;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    private  int Id;

    public ConsolidadoCarrerasBus(int id, double monto, String placa) {
        Id = id;
        Monto = monto;
        Placa = placa;
    }

    private double Monto;
    private String Placa;
}
