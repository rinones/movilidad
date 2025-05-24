package Menus.MenuAdmin;

import Utilidades.UtilidadesEntradaDatos;

public class MenuGestionBases {

    // ===========================
    // ATRIBUTOS Y CONSTRUCTOR
    // ===========================
    private final ServicioGestionBases servicioGestionBases;

    public MenuGestionBases() {
        this.servicioGestionBases = new ServicioGestionBases();
    }

    // ===========================
    // MENÚ PRINCIPAL
    // ===========================
    public void mostrarMenu() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n========== GESTIÓN DE BASES ==========");
            System.out.println("1. Alta Base por Coordenadas");
            System.out.println("2. Baja Base por Coordenadas");
            System.out.println("3. Baja Base por Nombre");
            System.out.println("4. Volver al menú principal");

            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 4);

            try {
                switch (opcion) {
                    case 1 -> altaBasePorCoordenadas();
                    case 2 -> bajaBasePorCoordenadas();
                    case 3 -> bajaBasePorNombre();
                    case 4 -> volver = true;
                    default -> System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ===========================
    // ALTA DE BASE POR COORDENADAS
    // ===========================
    private void altaBasePorCoordenadas() {
        System.out.println("\n--- Alta de Base Manual ---");

        System.out.print("Introduce el nombre de la base: ");
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 20);

        if (servicioGestionBases.existeBasePorNombre(nombreBase)) {
            System.out.println("Error: Ya existe una base con el nombre '" + nombreBase + "'.");
            return;
        }

        System.out.print("Introduce la coordenada X: ");
        int coordX = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioGestionBases.getMaxCoordenadaX());

        System.out.print("Introduce la coordenada Y: ");
        int coordY = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioGestionBases.getMaxCoordenadaY());

        if (!servicioGestionBases.coordenadasValidasYLibres(coordX, coordY)) {
            System.out.println("Error: Las coordenadas (" + coordX + ", " + coordY +
                    ") no son válidas o ya están ocupadas.");
            return;
        }

        System.out.print("Introduce la capacidad de la base: ");
        int capacidad = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la capacidad de la base", 1, Integer.MAX_VALUE);

        if (capacidad <= 0) {
            System.out.println("Error: La capacidad debe ser un número positivo.");
            return;
        }

        boolean resultado = servicioGestionBases.altaBasePorCoordenadas(nombreBase, coordX, coordY, capacidad);

        if (resultado) {
            System.out.println("Base '" + nombreBase + "' creada correctamente en coordenadas ("
                    + coordX + ", " + coordY + ") con capacidad para " + capacidad + " vehículos.");
            servicioGestionBases.dibujarMapa();
        } else {
            System.out.println("Error al crear la base. Verifica que todos los datos sean correctos.");
        }
    }

    // ===========================
    // BAJA DE BASE POR COORDENADAS
    // ===========================
    private void bajaBasePorCoordenadas() {
        System.out.println("\n--- Baja de Base por Coordenadas ---");

        System.out.print("Introduce la coordenada X de la base a eliminar: ");
        int coordX = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioGestionBases.getMaxCoordenadaX());

        System.out.print("Introduce la coordenada Y de la base a eliminar: ");
        int coordY = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioGestionBases.getMaxCoordenadaY());

        if (!servicioGestionBases.existeBaseEnCoordenadas(coordX, coordY)) {
            System.out.println("Error: No existe una base en las coordenadas (" + coordX + ", " + coordY + ").");
            return;
        }

        boolean resultado = servicioGestionBases.bajaBasePorCoordenadas(coordX, coordY);

        if (resultado) {
            System.out.println("Base en coordenadas (" + coordX + ", " + coordY + ") eliminada correctamente.");
            servicioGestionBases.dibujarMapa();
        } else {
            System.out.println("Error al eliminar la base. Verifica que los datos sean correctos.");
        }
    }

    // ===========================
    // BAJA DE BASE POR NOMBRE
    // ===========================
    private void bajaBasePorNombre() {
        System.out.println("\n--- Baja de Base ---");

        System.out.print("Introduce el nombre de la base a eliminar: ");
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 20);

        if (!servicioGestionBases.existeBasePorNombre(nombreBase)) {
            System.out.println("Error: No existe una base con el nombre '" + nombreBase + "'.");
            return;
        }

        boolean resultado = servicioGestionBases.bajaBasePorNombre(nombreBase);

        if (resultado) {
            System.out.println("Base '" + nombreBase + "' eliminada correctamente.");
            servicioGestionBases.dibujarMapa();
        } else {
            System.out.println("Error al eliminar la base. Verifica que los datos sean correctos.");
        }
    }
}
