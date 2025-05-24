package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;
import GestorVehiculosBases.Localizaciones.Base;

public abstract class VehiculoDeBase extends Vehiculo {
    private Base base;
    
    public VehiculoDeBase(int ID, VehiculoEnum tipo) {
        super(ID, tipo);
    }
    
    public Base getBase() {
        return base;
    }
    
    public void setBase(Base base) {
        this.base = base;
    }
}
