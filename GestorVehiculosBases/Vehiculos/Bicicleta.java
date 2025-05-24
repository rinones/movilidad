package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;

public class Bicicleta extends VehiculoDeBase {
    
    public Bicicleta(int ID, VehiculoEnum tipo) {
        super(ID, tipo);
    }

    @Override
    public String getNombreVehiculo() {
        return "Bicicleta";
    }
}
