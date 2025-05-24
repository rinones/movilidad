package Menus.MenuAdmin;

import Utilidades.UtilidadesEntradaDatos;

public class MenuGestionEmpleados {

    private final ServicioGestionEmpleados servicioGestionEmpleados;

    public MenuGestionEmpleados() {
        this.servicioGestionEmpleados = new ServicioGestionEmpleados();
    }

    // Muestra el menú principal de gestión de empleados y gestiona la navegación de opciones
    public void mostrarMenu() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========== GESTIÓN DE EMPLEADOS ==========");
            System.out.println("1. Asignar vehículo por recarga de batería");
            System.out.println("2. Asignar vehículo por avería");
            System.out.println("3. Asignar base por avería");
            System.out.println("4. Volver al menú principal");
            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Opción: ", 1, 4);

            try {
                switch (opcion) {
                    case 1 -> asignarVehiculoSinBateria();
                    case 2 -> asignarVehiculoPorAveria();
                    case 3 -> asignarBasePorAveria();
                    case 4 -> volver = true;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Asigna un vehículo a mantenimiento por batería baja
    private void asignarVehiculoSinBateria() {
        System.out.println("\n=== ASIGNAR VEHÍCULO POR RECARGA DE BATERÍA ===");
        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID del vehículo: ", 1, Integer.MAX_VALUE);
        String dni = UtilidadesEntradaDatos.getDNI("Introduce el DNI del trabajador de mantenimiento: ");
        boolean resultado = servicioGestionEmpleados.asignarVehiculoSinBateria(dni, idVehiculo);
        if (resultado) {
            System.out.println("Vehículo asignado correctamente a mantenimiento por batería baja.");
        } else {
            System.out.println("No se pudo asignar el vehículo a mantenimiento por batería baja.");
        }
    }

    // Asigna un vehículo a mantenimiento y mecánico por avería
    private void asignarVehiculoPorAveria() {
        System.out.println("\n=== ASIGNAR VEHÍCULO POR AVERÍA ===");
        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID del vehículo: ", 1, Integer.MAX_VALUE);
        String dniMantenimiento = UtilidadesEntradaDatos.getDNI("Introduce el DNI del trabajador de mantenimiento: ");
        String dniMecanico = UtilidadesEntradaDatos.getDNI("Introduce el DNI del mecánico: ");
        boolean resultado = servicioGestionEmpleados.asignarVehiculoPorAveria(dniMantenimiento, dniMecanico, idVehiculo);
        if (resultado) {
            System.out.println("Vehículo asignado correctamente a mantenimiento y mecánico por avería.");
        } else {
            System.out.println("No se pudo asignar el vehículo a mantenimiento y mecánico por avería.");
        }
    }

    // Asigna una base averiada a un mecánico
    private void asignarBasePorAveria() {
        System.out.println("\n=== ASIGNAR BASE POR AVERÍA ===");
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base: ", 1, 50);
        String dniMecanico = UtilidadesEntradaDatos.getDNI("Introduce el DNI del mecánico: ");
        boolean resultado = servicioGestionEmpleados.asignarBasePorAveria(dniMecanico, nombreBase);
        if (resultado) {
            System.out.println("Base asignada correctamente al mecánico por avería.");
        } else {
            System.out.println("No se pudo asignar la base al mecánico por avería.");
        }
    }
}
