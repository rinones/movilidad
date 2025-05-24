package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;

public class MotoPequena extends VehiculoDeCoordenadas {
    
    public MotoPequena(int ID, VehiculoEnum tipo) {
        super(ID, tipo);
    }

    @Override
    public String getNombreVehiculo() {
        return "Moto Pequeña";
    }
}
