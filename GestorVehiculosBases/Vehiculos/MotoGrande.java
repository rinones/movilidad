package GestorVehiculosBases.Vehiculos;

import Enum.VehiculoEnum;

public class MotoGrande extends VehiculoDeCoordenadas {
    
    public MotoGrande(int ID, VehiculoEnum tipo) {
        super(ID, tipo);
    }

    @Override
    public String getNombreVehiculo() {
        return "Moto Grande";
    }
}
