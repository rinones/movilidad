package Menus.MenuAdmin;

import GestorPersonas.GestorPersonas;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.GestorBases;

public class ServicioGestionEmpleados {

    private final GestorPersonas gestorPersonas;
    private final GestorVehiculos gestorVehiculos;
    private final GestorBases gestorBases;

    public ServicioGestionEmpleados() {
        this.gestorPersonas = GestorPersonas.getInstancia();
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.gestorBases = GestorBases.getInstancia();
    }

    public boolean asignarVehiculoSinBateria(String dni, int idVehiculo) {
        return gestorVehiculos.asignarMantenimientoBateriaBaja(dni, idVehiculo);
    }

    public boolean asignarVehiculoPorAveria(String dniMantenimiento, String dniMecanico, int idVehiculo) {
        return gestorVehiculos.asignarMantenimientoYMecanicoAveria(dniMantenimiento, dniMecanico, idVehiculo);
    }

    public boolean asignarBasePorAveria(String dniMecanico, String nombreBase) {
        return gestorBases.asignarMecanicoAveriaBase(dniMecanico, nombreBase);
    }
}