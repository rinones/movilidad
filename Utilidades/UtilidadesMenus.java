package Utilidades;

import Enum.RolEnum;
import Enum.VehiculoEnum;
public class UtilidadesMenus {

    // Método para seleccionar el tipo de vehículo
    public static void mostrarMenuTipoVehiculo() {
        System.out.println("Selecciona el tipo de vehículo:");
        System.out.println("1. " + VehiculoEnum.MOTOPEQUENA);
        System.out.println("2. " + VehiculoEnum.MOTOGRANDE);
        System.out.println("3. " + VehiculoEnum.BICICLETA);
        System.out.println("4. " + VehiculoEnum.PATINETE);
    }
    

    // Método para seleccionar el rol
    public static void mostrarMenuRol() {
        System.out.println("Selecciona el rol del nuevo registro:");
        System.out.println("1. " + RolEnum.USUARIO_STANDARD);
        System.out.println("2. " + RolEnum.MECANICO);
        System.out.println("3. " + RolEnum.MANTENIMIENTO);
        System.out.println("4. " + RolEnum.ADMINISTRADOR);
    }

    public static VehiculoEnum seleccionarTipoVehiculo() {
        while (true) {
            System.out.println("Selecciona el tipo de vehículo:");
            System.out.println("1. " + VehiculoEnum.MOTOPEQUENA);
            System.out.println("2. " + VehiculoEnum.MOTOGRANDE);
            System.out.println("3. " + VehiculoEnum.BICICLETA);
            System.out.println("4. " + VehiculoEnum.PATINETE);
            System.out.print("Introduce una opción (1-4): ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Introduce una opción (1-4): ", 1, 4);
            return switch (opcion) {
                case 1 -> VehiculoEnum.MOTOPEQUENA;
                case 2 -> VehiculoEnum.MOTOGRANDE;
                case 3 -> VehiculoEnum.BICICLETA;
                case 4 -> VehiculoEnum.PATINETE;
                default -> {
                    System.out.println("Opción no válida. Por favor, selecciona una opción del 1 al 4.");
                    yield null;
                }
            };
        }
    }

    public static RolEnum seleccionarRol() {
        while (true) {
            System.out.println("Introduce el rol del nuevo registro:");
            System.out.println("1. " + RolEnum.USUARIO_STANDARD); // toString() implícito
            System.out.println("2. " + RolEnum.MECANICO);
            System.out.println("3. " + RolEnum.MANTENIMIENTO);
            System.out.println("4. " + RolEnum.ADMINISTRADOR);
            System.out.print("Selecciona una opción (1-4): ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Introduce una opción (1-4): ", 1, 4);
            return switch (opcion) {
                case 1 -> RolEnum.USUARIO_STANDARD;
                case 2 -> RolEnum.MECANICO;
                case 3 -> RolEnum.MANTENIMIENTO;
                case 4 -> RolEnum.ADMINISTRADOR;
                default -> {
                    System.out.println("Opción no válida. Por favor, selecciona una opción del 1 al 4.");
                    yield null;
                }
            };
        }
    }
}


