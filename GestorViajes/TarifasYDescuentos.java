package GestorViajes;

import Enum.VehiculoEnum;

public class TarifasYDescuentos {

    //==========================
    // GESTIÓN DE TARIFAS Y DESCUENTOS
    //==========================

    // Modifica la tarifa de un tipo de vehículo
    public boolean modificarTarifa(VehiculoEnum tipoVehiculo, double nuevaTarifa) {
        // Validación de tipo de vehículo
        if (tipoVehiculo == null) {
            System.out.println("Error: Tipo de vehículo no válido.");
            return false;
        }
        // Validación de tarifa
        if (nuevaTarifa < 0) {
            System.out.println("Error: La tarifa no puede ser negativa.");
            return false;
        }
        try {
            // Actualiza la tarifa en el enum
            tipoVehiculo.setTarifa(nuevaTarifa);
            System.out.println("Tarifa para " + tipoVehiculo + " actualizada correctamente a " + nuevaTarifa + "€ por hora.");
            // Muestra las tarifas actualizadas
            mostrarTarifasActuales();
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar la tarifa: " + e.getMessage());
            return false;
        }
    }

    // Modifica el descuento de un tipo de vehículo
    public boolean modificarDescuento(VehiculoEnum tipoVehiculo, double nuevoDescuento) {
        // Validación de tipo de vehículo
        if (tipoVehiculo == null) {
            System.out.println("Error: Tipo de vehículo no válido.");
            return false;
        }
        // Validación de descuento
        if (nuevoDescuento < 0 || nuevoDescuento > 100) {
            System.out.println("Error: El descuento debe estar entre 0 y 100 por ciento.");
            return false;
        }
        try {
            // Actualiza el descuento en el enum
            tipoVehiculo.setDescuento(nuevoDescuento);
            System.out.println("Descuento para " + tipoVehiculo + " actualizado correctamente al " + nuevoDescuento + "%.");
            // Muestra los descuentos actualizados
            mostrarDescuentosActuales();
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar el descuento: " + e.getMessage());
            return false;
        }
    }

    //==========================
    // VISUALIZACIÓN
    //==========================

    // Muestra las tarifas actuales de todos los tipos de vehículos
    public void mostrarTarifasActuales() {
        System.out.println("\n=== TARIFAS ACTUALES ===");
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            double tarifa = tipo.getTarifa();
            System.out.printf("%s: %.2f€ por hora\n", tipo, tarifa);
        }
    }

    // Muestra los descuentos actuales de todos los tipos de vehículos
    public void mostrarDescuentosActuales() {
        System.out.println("\n=== DESCUENTOS ACTUALES ===");
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            double descuento = tipo.getDescuento();
            System.out.printf("%s: %.1f%%\n", tipo, descuento);
        }
    }
}