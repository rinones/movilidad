package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;
import GestorPersonas.Personas.Premium;
import GestorPersonas.Personas.Usuario;
import GestorViajes.Beneficios;

public abstract class Vehiculo {

    private int ID;
    private VehiculoEnum tipoVehiculo;
    private double bateria;
    private boolean averias = false;
    private boolean reservado = false;
    private boolean enUso = false;

    public Vehiculo(int ID, VehiculoEnum tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
        this.ID = ID;
        this.bateria = Beneficios.getBateriaMaxima();
        this.averias = false;
        this.reservado = false;
    }

    public int getID() {
        return ID;
    }

    public double getBateria() {
        return bateria;
    }

    public void setBateria(double bateria) {
        this.bateria = bateria;
    }

    public VehiculoEnum getVehiculoEnum() {
        return tipoVehiculo;
    }

    public abstract String getNombreVehiculo();

    public boolean getAverias(){
        return averias;
    }

    public void setAverias(boolean averias) {
        this.averias = averias;
    }
    
    public boolean getReservado() {
        return reservado;
    }

    public double getBateriaMinima(Usuario usuario) {
        if (usuario instanceof Premium) {
            return Beneficios.getBateriaMinimaPremium();
        } else {
            return Beneficios.getBateriaMinimaStandard();
        }
    }

    public boolean getEnUso() {
        return enUso; 
    }

    public void setEnUso(boolean enUso) {
        this.enUso = enUso;
    }


    public boolean getDisponible(Usuario usuario) {
        return getBateria() > getBateriaMinima(usuario) && !getAverias() && !getReservado() && !getEnUso();
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }
}
