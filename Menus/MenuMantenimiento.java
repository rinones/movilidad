package Menus;

import GestorPersonas.Personas.Mantenimiento;
import Utilidades.UtilidadesEntradaDatos;

public class MenuMantenimiento {

    Mantenimiento mantenimiento;
    private final ServicioMantenimiento servicioMantenimiento;

    public MenuMantenimiento() {
        this.servicioMantenimiento = new ServicioMantenimiento();
    }

    public void mostrarMenu(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
        while (true) {
            System.out.println("\n========== MENÚ MANTENIMIENTO ==========");
            System.out.println("1. Ver vehículos asignados");
            System.out.println("2. Realizar mantenimiento");
            System.out.println("3. Reservar (desactivar) vehículo");
            System.out.println("4. Cerrar sesión");

            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 4);

            switch (opcion) {
                case 1:
                    servicioMantenimiento.verVehiculosAsignados(mantenimiento);
                    break;
                case 2:
                    servicioMantenimiento.realizarMantenimiento(mantenimiento);
                    break;
                case 3:
                    servicioMantenimiento.desactivarVehiculo();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
            }
        }
    }
}
