package Menus.MenuAdmin;

import Enum.VehiculoEnum;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Vehiculos.MotoGrande;
import GestorVehiculosBases.Vehiculos.MotoPequena;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import Utilidades.UtilidadesEntradaDatos;
import java.util.List;

public class MenuGestionVehiculos {

    private final ServicioGestionVehiculos servicioGestionVehiculos;

    public MenuGestionVehiculos() {
        this.servicioGestionVehiculos = new ServicioGestionVehiculos();
    }

    public void mostrarMenu() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n========== GESTIÓN DE VEHÍCULOS ==========");
            System.out.println("1. Alta Vehículo en Mapa por Coordenadas (Motos)");
            System.out.println("2. Alta Vehículo en Base por Nombre de Base (Bicicletas/Patinetes)");
            System.out.println("3. Alta Vehículo en Almacén");
            System.out.println("4. Baja Vehículo por ID");
            System.out.println("5. Submenu - Reubicar Vehículos");
            System.out.println("6. Volver al menú principal");

            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 6);

            try {
                switch (opcion) {
                    case 1 -> altaVehiculoMapa();
                    case 2 -> altaVehiculoBase();
                    case 3 -> altaVehiculoAlmacen();
                    case 4 -> bajaVehiculo();
                    case 5 -> menuReubicarVehiculos();
                    case 6 -> volver = true;
                    default -> System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Alta de moto en el mapa por coordenadas
    private void altaVehiculoMapa() {
        System.out.println("\n--- Alta de Moto en Posición del Mapa ---");
        System.out.println("Selecciona el tipo de moto:");
        System.out.println("1. " + VehiculoEnum.MOTOPEQUENA);
        System.out.println("2. " + VehiculoEnum.MOTOGRANDE);

        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 2);
        VehiculoEnum tipoMoto;
        switch (opcion) {
            case 1 -> tipoMoto = VehiculoEnum.MOTOPEQUENA;
            case 2 -> tipoMoto = VehiculoEnum.MOTOGRANDE;
            default -> {
                System.out.println("Opción no válida. Operación cancelada.");
                return;
            }
        }

        System.out.println("\nIntroduce las coordenadas donde posicionar la moto:");
        int x = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioGestionVehiculos.getMaxCoordenadaX());
        int y = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioGestionVehiculos.getMaxCoordenadaY());

        Vehiculo nuevoVehiculo = servicioGestionVehiculos.altaVehiculoMapa(tipoMoto, x, y);

        if (nuevoVehiculo != null) {
            System.out.println("Moto añadida exitosamente. ID asignado: " + nuevoVehiculo.getID());
            servicioGestionVehiculos.dibujarMapa();
        } else {
            System.out.println("No se pudo añadir la moto en la posición especificada.");
        }
    }

    // Alta de bicicleta o patinete en una base
    private void altaVehiculoBase() {
        System.out.println("\n--- Alta de Vehículo en Base ---");
        System.out.println("Selecciona el tipo de vehículo:");
        System.out.println("1. " + VehiculoEnum.BICICLETA);
        System.out.println("2. " + VehiculoEnum.PATINETE);

        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 2);
        VehiculoEnum tipoVehiculo;
        switch (opcion) {
            case 1 -> tipoVehiculo = VehiculoEnum.BICICLETA;
            case 2 -> tipoVehiculo = VehiculoEnum.PATINETE;
            default -> {
                System.out.println("Opción no válida. Operación cancelada.");
                return;
            }
        }

        List<Base> bases = servicioGestionVehiculos.getBases();
        if (bases.isEmpty()) {
            System.out.println("No hay bases disponibles en el sistema. Primero debe crear una base.");
            return;
        }

        System.out.println("\nBases disponibles:");
        for (Base base : bases) {
            System.out.println("- " + base.getNombre() +
                    " (Coordenadas: " + base.getCoordenadaX() + ", " + base.getCoordenadaY() +
                    ", Espacios libres: " + base.getEspaciosLibres() + ")");
        }

        System.out.print("\nIntroduce el nombre de la base donde añadir el vehículo: ");
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 50);

        Vehiculo nuevoVehiculo = servicioGestionVehiculos.altaVehiculoBase(tipoVehiculo, nombreBase);

        if (nuevoVehiculo != null) {
            System.out.println("Vehículo añadido exitosamente a la base. ID asignado: " + nuevoVehiculo.getID());
        } else {
            System.out.println("No se pudo añadir el vehículo a la base especificada.");
        }
    }

    // Alta de vehículo en el almacén
    private void altaVehiculoAlmacen() {
        System.out.println("\n--- Alta de Vehículo en Almacén ---");
        System.out.println("Selecciona el tipo de vehículo:");
        System.out.println("1. " + VehiculoEnum.BICICLETA);
        System.out.println("2. " + VehiculoEnum.PATINETE);
        System.out.println("3. " + VehiculoEnum.MOTOPEQUENA);
        System.out.println("4. " + VehiculoEnum.MOTOGRANDE);
        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 4);

        VehiculoEnum tipoVehiculo;
        switch (opcion) {
            case 1 -> tipoVehiculo = VehiculoEnum.BICICLETA;
            case 2 -> tipoVehiculo = VehiculoEnum.PATINETE;
            case 3 -> tipoVehiculo = VehiculoEnum.MOTOPEQUENA;
            case 4 -> tipoVehiculo = VehiculoEnum.MOTOGRANDE;
            default -> {
                System.out.println("Opción no válida. Operación cancelada.");
                return;
            }
        }

        Vehiculo nuevoVehiculo = servicioGestionVehiculos.altaVehiculoAlmacen(tipoVehiculo);
        if (nuevoVehiculo != null) {
            System.out.println("Vehículo añadido exitosamente al almacén. ID asignado: " + nuevoVehiculo.getID());
        } else {
            System.out.println("No se pudo añadir el vehículo al almacén.");
        }
    }

    // Baja de un vehículo por ID
    private void bajaVehiculo() {
        System.out.println("\n--- Baja de Vehículo ---");
        int ID = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID del vehículo", 1, Integer.MAX_VALUE);
        boolean resultado = servicioGestionVehiculos.bajaVehiculo(ID);
        if (resultado) {
            System.out.println("Vehículo eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el vehículo. Verifica el ID.");
        }
    }

    // Submenú para reubicar vehículos
    private void menuReubicarVehiculos() {
        System.out.println("\n=== REUBICAR VEHÍCULOS ===");
        System.out.println("1. Mover moto a nuevas coordenadas");
        System.out.println("2. Mover bicicleta/patinete a nueva base");
        System.out.println("3. Volver al menú anterior");

        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 3);

        switch (opcion) {
            case 1 -> moverMotoACoordenadas();
            case 2 -> moverVehiculoANuevaBase();
            case 3 -> { return; }
            default -> System.out.println("Opción no válida.");
        }
    }

    // Mover una moto a nuevas coordenadas en el mapa
    private void moverMotoACoordenadas() {
        System.out.println("\n--- Mover Moto a Nuevas Coordenadas ---");

        List<Vehiculo> motosEnMapa = servicioGestionVehiculos.getMotosEnMapa();
        if (motosEnMapa.isEmpty()) {
            System.out.println("No hay motos disponibles en el mapa para mover.");
            return;
        }

        System.out.println("\nMotos disponibles en el mapa:");
        for (Vehiculo vehiculo : motosEnMapa) {
            if (vehiculo instanceof MotoPequena moto) {
                System.out.print("ID: " + moto.getID() +
                        ", Tipo: " + moto.getVehiculoEnum() +
                        ", Coordenadas: (" + moto.getCoordenadaX() + ", " + moto.getCoordenadaY() + ")" +
                        ", Batería: " + moto.getBateria() + "%" +
                        ", Averías: " + (moto.getAverias() ? "Sí" : "No") + "\n");
            } else if (vehiculo instanceof MotoGrande moto) {
                System.out.print("ID: " + moto.getID() +
                        ", Tipo: " + moto.getVehiculoEnum() +
                        ", Coordenadas: (" + moto.getCoordenadaX() + ", " + moto.getCoordenadaY() + ")" +
                        ", Batería: " + moto.getBateria() + "%" +
                        ", Averías: " + (moto.getAverias() ? "Sí" : "No") + "\n");
            }
        }

        int idMoto = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID de la moto", 1, Integer.MAX_VALUE);
        int nuevaX = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioGestionVehiculos.getMaxCoordenadaX());
        int nuevaY = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioGestionVehiculos.getMaxCoordenadaY());

        boolean resultado = servicioGestionVehiculos.moverVehiculoACoordenadas(idMoto, nuevaX, nuevaY);

        if (resultado) {
            System.out.println("Moto movida exitosamente. Mostrando mapa actualizado:");
            servicioGestionVehiculos.dibujarMapa();
        } else {
            System.out.println("No se pudo mover la moto a las nuevas coordenadas.");
        }
    }

    // Mover bicicleta o patinete a una nueva base
    private void moverVehiculoANuevaBase() {
        System.out.println("\n--- Mover Vehículo a Nueva Base ---");

        List<Vehiculo> vehiculosEnBases = servicioGestionVehiculos.getVehiculosEnBases();
        List<Vehiculo> vehiculosAlmacenados = servicioGestionVehiculos.getVehiculosAlmacenados();

        boolean hayVehiculosParaMover = false;

        System.out.println("\nVehículos en bases disponibles para mover:");
        for (Vehiculo vehiculo : vehiculosEnBases) {
            if (vehiculo instanceof VehiculoDeBase vehiculoDeBase) {
                Base baseActual = vehiculoDeBase.getBase();
                System.out.print("ID: " + vehiculo.getID() +
                        ", Tipo: " + vehiculo.getVehiculoEnum() +
                        ", Base actual: " + (baseActual != null ? baseActual.getNombre() : "Sin base") +
                        ", Batería: " + vehiculo.getBateria() + "%" +
                        ", Averías: " + (vehiculo.getAverias() ? "Sí" : "No") + "\n");
                hayVehiculosParaMover = true;
            }
        }

        System.out.println("\nVehículos en almacén disponibles para mover:");
        for (Vehiculo vehiculo : vehiculosAlmacenados) {
            if (vehiculo instanceof VehiculoDeBase) {
                System.out.print("ID: " + vehiculo.getID() +
                        ", Tipo: " + vehiculo.getVehiculoEnum() +
                        ", Ubicación: Almacén" +
                        ", Batería: " + vehiculo.getBateria() + "%" +
                        ", Averías: " + (vehiculo.getAverias() ? "Sí" : "No") + "\n");
                hayVehiculosParaMover = true;
            }
        }

        if (!hayVehiculosParaMover) {
            System.out.println("No hay bicicletas ni patinetes disponibles para mover.");
            return;
        }

        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID del vehículo", 1, Integer.MAX_VALUE);

        List<Base> bases = servicioGestionVehiculos.getBases();
        if (bases.isEmpty()) {
            System.out.println("No hay bases disponibles en el sistema.");
            return;
        }

        System.out.println("\nBases disponibles:");
        for (Base base : bases) {
            System.out.println("- " + base.getNombre() +
                    " (Coordenadas: " + base.getCoordenadaX() + ", " + base.getCoordenadaY() +
                    ", Espacios libres: " + base.getEspaciosLibres() + ")");
        }

        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 50);

        boolean resultado = servicioGestionVehiculos.moverVehiculoABase(idVehiculo, nombreBase);

        if (resultado) {
            System.out.println("Vehículo movido exitosamente a la nueva base.");
        } else {
            System.out.println("No se pudo mover el vehículo a la nueva base.");
        }
    }
}
