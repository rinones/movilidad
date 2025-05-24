package GestorVehiculosBases.Facturas;

// Una clase genérica para cualquier reparación

import GestorVehiculosBases.Localizaciones.Base;


public class FacturaReparacionBase {

    Base base;
    Double coste;

    public FacturaReparacionBase(Base base) {
        this.base = base;
        this.coste = 120.0; // Coste fijo de reparación de base
    }

    public Base getBase() {
        return base;
    }

    public double getCoste() {
        return coste;
    }

}
