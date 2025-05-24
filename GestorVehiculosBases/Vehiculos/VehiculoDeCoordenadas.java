package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;

public abstract class VehiculoDeCoordenadas extends Vehiculo {
    private int coordenadaX;
    private int coordenadaY;
    
    public VehiculoDeCoordenadas(int ID, VehiculoEnum tipo) {
        super(ID, tipo);
    }
    
    public int getCoordenadaX() {
        return coordenadaX;
    }
    
    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }
    
    public int getCoordenadaY() {
        return coordenadaY;
    }
    
    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }
}
