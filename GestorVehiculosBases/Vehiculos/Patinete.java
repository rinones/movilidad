package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;

public class Patinete extends VehiculoDeBase {
    
    public Patinete(int ID, VehiculoEnum tipo) {
        super(ID, tipo);
    }

    @Override
    public String getNombreVehiculo() {
        return "Patinete";
    }
}
