package nacatamalitosoft.com.cotracosanapps.Modelos;

import java.io.Serializable;

public class Buses implements Serializable {
    private int id;
    private int socioId;
    private String placa;
    private boolean estado;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getSocioId() {
        return socioId;
    }

    public void setSocioId(int socioId) {
        this.socioId = socioId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Buses(int id, int socioId, String placa, boolean estado) {
        this.setId(id);
        this.setSocioId(socioId);
        this.setPlaca(placa);
        this.setEstado(estado);
    }
}