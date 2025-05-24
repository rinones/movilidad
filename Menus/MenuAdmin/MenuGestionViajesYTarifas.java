package Menus.MenuAdmin;

import Enum.VehiculoEnum;
import Utilidades.UtilidadesEntradaDatos;
import Utilidades.UtilidadesMenus;

public class MenuGestionViajesYTarifas {

    private final ServicioGestionViajesYTarifas servicioGestionViajesYTarifas;

    public MenuGestionViajesYTarifas() {
        this.servicioGestionViajesYTarifas = new ServicioGestionViajesYTarifas();
    }

    public void mostrarMenu() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n========== GESTIÓN DE VIAJES Y TARIFAS ==========");
            System.out.println("1. Configuración de Reservas");
            System.out.println("2. Configuración de Baterías");
            System.out.println("3. Modificar Tarifa");
            System.out.println("4. Modificar Descuento");
            System.out.println("5. Volver al menú principal");

            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 5);

            try {
                switch (opcion) {
                    case 1 -> modificarReservas();
                    case 2 -> modificarBaterias();
                    case 3 -> modificarTarifa();
                    case 4 -> modificarDescuento();
                    case 5 -> volver = true;
                    default -> System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void modificarTarifa() {
        System.out.println("\n--- Modificación de tarifa ---");

        System.out.println("\nTarifas actuales:");
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            double tarifa = servicioGestionViajesYTarifas.getTarifa(tipo);
            System.out.printf("%s: %.2f€ por hora\n", tipo, tarifa);
        }

        System.out.print("\nSelecciona el vehículo cuya tarifa se va a modificar: ");
        VehiculoEnum tipoVehiculo = UtilidadesMenus.seleccionarTipoVehiculo();

        double tarifa = servicioGestionViajesYTarifas.getTarifa(tipoVehiculo);
        System.out.printf("La tarifa actual para %s es: %.2f€ por hora\n", tipoVehiculo, tarifa);

        System.out.print("Introduce la nueva tarifa en euros por hora: ");
        double nuevaTarifa = UtilidadesEntradaDatos.getDecimalPositivo("Introduce la nueva tarifa", 0.01, Double.MAX_VALUE);

        boolean resultado = servicioGestionViajesYTarifas.modificarTarifa(tipoVehiculo, nuevaTarifa);

        if (!resultado) {
            System.out.println("No se pudo modificar la tarifa. Asegúrate de que el valor sea válido.");
        }
    }

    private void modificarDescuento() {
        System.out.println("\n--- Modificación de descuento ---");

        System.out.println("\nDescuentos actuales:");
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            double descuento = servicioGestionViajesYTarifas.getDescuento(tipo);
            System.out.printf("%s: %.0f%%\n", tipo, descuento);
        }

        System.out.print("\nSelecciona el vehículo cuyo descuento se va a modificar: ");
        VehiculoEnum tipoVehiculo = UtilidadesMenus.seleccionarTipoVehiculo();

        double descuentoActual = servicioGestionViajesYTarifas.getDescuento(tipoVehiculo);
        System.out.printf("El descuento actual para %s es: %.0f%%\n", tipoVehiculo, descuentoActual);

        System.out.print("Introduce el nuevo descuento en %: ");
        double nuevoDescuento = UtilidadesEntradaDatos.getDecimalPositivo("Introduce el nuevo descuento", 0, 100);

        if (nuevoDescuento < 0 || nuevoDescuento > 100) {
            System.out.println("Error: El descuento debe estar entre 0 y 100 por ciento.");
            return;
        }

        boolean resultado = servicioGestionViajesYTarifas.modificarDescuento(tipoVehiculo, nuevoDescuento);

        if (!resultado) {
            System.out.println("No se pudo modificar el descuento. Asegúrate de que el valor sea válido.");
        }
    }

    private void modificarReservas() {
        System.out.println("\n--- Modificación de reservas ---");

        boolean estadoActual = servicioGestionViajesYTarifas.getReservasPermitidas();
        System.out.println("Estado actual: Las reservas están " +
                (estadoActual ? "HABILITADAS" : "DESHABILITADAS") + ".");

        System.out.println("Qué deseas hacer");
        if (estadoActual) {
            System.out.print("Deseas deshabilitar las reservas (1 Sí, 2 No) ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 2);
            if (opcion == 1) {
                servicioGestionViajesYTarifas.setReservasPermitidas(false);
                System.out.println("Las reservas han sido deshabilitadas correctamente.");
            } else {
                System.out.println("No se han realizado cambios. Las reservas siguen habilitadas.");
            }
        } else {
            System.out.print("Deseas habilitar las reservas (1 Sí, 2 No) ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 2);
            if (opcion == 1) {
                servicioGestionViajesYTarifas.setReservasPermitidas(true);
                System.out.println("Las reservas han sido habilitadas correctamente.");
            } else {
                System.out.println("No se han realizado cambios. Las reservas siguen deshabilitadas.");
            }
        }
    }

    private void modificarBaterias() {
        System.out.println("\n--- Modificación de batería ---");
        System.out.print("Deseas habilitar o deshabilitar la batería premium (1 Habilitar, 2 Deshabilitar) ");
        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 2);
        if (opcion == 1) {
            servicioGestionViajesYTarifas.setBateriaPremiumPermitida(true);
            System.out.println("La batería premium ha sido habilitada correctamente.");
        } else if (opcion == 2) {
            servicioGestionViajesYTarifas.setBateriaPremiumPermitida(false);
            System.out.println("La batería premium ha sido deshabilitada correctamente.");
        } else {
            System.out.println("Opción no válida. Por favor, selecciona 1 o 2.");
        }
    }
}
