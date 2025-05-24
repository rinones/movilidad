package Menus;

import GestorPersonas.Personas.Mecanico;
import Utilidades.UtilidadesEntradaDatos;

public class MenuMecanico {

    private Mecanico mecanico;
    private final ServicioMecanico servicioMecanico;

    public MenuMecanico() {
        this.servicioMecanico = new ServicioMecanico();
    }

    public void mostrarMenu(Mecanico mecanico) {
        this.mecanico = mecanico;
        servicioMecanico.setMecanico(mecanico);
        while (true) {
            System.out.println("\n========== MENÚ MECÁNICO ==========");

            // GESTIÓN DE REPARACIONES
            System.out.println("\n--- GESTIÓN DE REPARACIONES ---");
            System.out.println("1. Ver vehículos asignados | 2. Reparar vehículo");

            // GESTIÓN DE BASES
            System.out.println("\n--- GESTIÓN DE BASES ---");
            System.out.println("3. Ver bases asignadas | 4. Reparar base");

            // SISTEMA
            System.out.println("\n5. Cerrar Sesión");

            System.out.print("\nSeleccione opción: ");

            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 6);

            switch (opcion) {
                case 1:
                    servicioMecanico.verVehiculosYBasesAsignados();
                    break;
                case 2:
                    servicioMecanico.realizarReparacionVehiculo();
                    break;
                case 3:
                    servicioMecanico.verVehiculosYBasesAsignados();
                    break;
                case 4:
                    servicioMecanico.realizarReparacionBase();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
            }
        }
    }
}
