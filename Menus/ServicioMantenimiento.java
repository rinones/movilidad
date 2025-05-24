package Menus;

import GestorPersonas.Personas.Mantenimiento;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import GestorViajes.Beneficios;
import Utilidades.UtilidadesEntradaDatos;

public class ServicioMantenimiento {

    public void verVehiculosAsignados(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            System.out.println("No hay usuario de mantenimiento activo.");
            return;
        }

        // Tabla para Patinetes y Bicicletas
        System.out.println("\n--- PATINETES Y BICICLETAS ASIGNADOS ---");
        System.out.printf("%-10s %-12s %-20s %-15s\n", "ID", "Tipo", "Base", "Estado");
        mantenimiento.getVehiculosSinBateria().stream()
            .filter(v -> v instanceof GestorVehiculosBases.Vehiculos.Patinete || v instanceof GestorVehiculosBases.Vehiculos.Bicicleta)
            .forEach(v -> {
                VehiculoDeBase vb = (VehiculoDeBase) v;
                String baseNombre = vb.getBase() != null ? vb.getBase().toString() : "Sin base";
                System.out.printf("%-10d %-12s %-20s %-15s\n",
                    vb.getID(),
                    vb.getNombreVehiculo(),
                    baseNombre,
                    "Sin batería"
                );
            });
        mantenimiento.getVehiculosAveriados().stream()
            .filter(v -> v instanceof GestorVehiculosBases.Vehiculos.Patinete || v instanceof GestorVehiculosBases.Vehiculos.Bicicleta)
            .forEach(v -> {
                VehiculoDeBase vb = (VehiculoDeBase) v;
                String baseNombre = vb.getBase() != null ? vb.getBase().toString() : "Sin base";
                System.out.printf("%-10d %-12s %-20s %-15s\n",
                    vb.getID(),
                    vb.getNombreVehiculo(),
                    baseNombre,
                    "Averiado"
                );
            });

        // Tabla para Motos
        System.out.println("\n--- MOTOS ASIGNADAS ---");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", "ID", "Tipo", "Coord X", "Coord Y", "Estado");
        mantenimiento.getVehiculosSinBateria().stream()
            .filter(v -> v instanceof VehiculoDeCoordenadas)
            .forEach(v -> {
                VehiculoDeCoordenadas vm = (VehiculoDeCoordenadas) v;
                System.out.printf("%-10d %-15s %-15d %-15d %-15s\n",
                    vm.getID(),
                    vm.getNombreVehiculo(),
                    vm.getCoordenadaX(),
                    vm.getCoordenadaY(),
                    "Sin batería"
                );
            });
        mantenimiento.getVehiculosAveriados().stream()
            .filter(v -> v instanceof VehiculoDeCoordenadas)
            .forEach(v -> {
                VehiculoDeCoordenadas vm = (VehiculoDeCoordenadas) v;
                System.out.printf("%-10d %-15s %-15d %-15d %-15s\n",
                    vm.getID(),
                    vm.getNombreVehiculo(),
                    vm.getCoordenadaX(),
                    vm.getCoordenadaY(),
                    "Averiado"
                );
            });
    }

    public void realizarMantenimiento(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            System.out.println("No hay usuario de mantenimiento activo.");
            return;
        }

        GestorVehiculos gestorVehiculos = GestorVehiculos.getInstancia();

        int recargados = 0;
        int retirados = mantenimiento.getVehiculosAveriados().size();

        // Reestablecer batería y solicitar traslado para cada vehículo sin batería
        for (var v : mantenimiento.getVehiculosSinBateria()) {
            v.setBateria(100);
            recargados++;
            mantenimiento.incrementarIntervenciones(); // Incrementa por recarga

            if (v instanceof VehiculoDeBase) {
                System.out.println("\nVehículo: " + v.getNombreVehiculo() + " (ID: " + v.getID() + ")");
                String nombreBase;
                boolean trasladoOK = false;
                do {
                    nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base destino", 1, 20);
                    if (gestorVehiculos.moverVehiculoABase(v.getID(), nombreBase)) {
                        trasladoOK = true;
                    } else {
                        System.out.println("No se pudo trasladar el vehículo a la base indicada. Intente de nuevo.");
                    }
                } while (!trasladoOK);
            } else if (v instanceof VehiculoDeCoordenadas) {
                System.out.println("\nVehículo: " + v.getNombreVehiculo() + " (ID: " + v.getID() + ")");
                boolean trasladoOK = false;
                do {
                    Mapa mapa = Mapa.getInstancia();
                    int x = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X destino", 0, mapa.getMaxCoordenadaX());
                    int y = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y destino", 0, mapa.getMaxCoordenadaY());
                    if (gestorVehiculos.moverVehiculoACoordenadas(v.getID(), x, y)) {
                        trasladoOK = true;
                    } else {
                        System.out.println("No se pudo trasladar el vehículo a esas coordenadas. Intente de nuevo.");
                    }
                } while (!trasladoOK);
            }
        }

        // Retirar vehículos averiados usando GestorVehiculos
        mantenimiento.getVehiculosAveriados().forEach(v -> {
            if (v instanceof VehiculoDeBase) {
                gestorVehiculos.removeVehiculoBasePorID(v.getID());
            } else if (v instanceof VehiculoDeCoordenadas) {
                VehiculoDeCoordenadas vc = (VehiculoDeCoordenadas) v;
                gestorVehiculos.removeVehiculoMapaPorCoordenadas(vc.getCoordenadaX(), vc.getCoordenadaY());
            }
            mantenimiento.incrementarIntervenciones();; // Incrementa por retiro
        });

        // Limpiar listas
        mantenimiento.getVehiculosSinBateria().clear();
        mantenimiento.getVehiculosAveriados().clear();

        System.out.println("Mantenimiento realizado con éxito.");
        System.out.println("Vehículos recargados y trasladados: " + recargados);
        System.out.println("Vehículos retirados: " + retirados);
    }

    public void desactivarVehiculo() {
        System.out.println("Introduce el ID del vehículo a reservar:");
        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("ID vehículo", 1, Integer.MAX_VALUE);

        System.out.println("Introduce la duración del periodo de inactividad en minutos:");
        int minutos = UtilidadesEntradaDatos.getEnteroPositivo("Minutos", 1, 1440); // hasta 24h

        Beneficios beneficios = new Beneficios();
        if (beneficios.reservarVehiculo(idVehiculo, minutos)) {
            System.out.println("Vehículo reservado correctamente por " + minutos + " minutos.");
        } else {
            System.out.println("No se pudo reservar el vehículo.");
        }
    }
}