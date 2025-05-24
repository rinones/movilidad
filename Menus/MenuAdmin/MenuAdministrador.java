package Menus.MenuAdmin;

import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Administrador;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorViajes.GestorViajes;
import Utilidades.UtilidadesEntradaDatos;


public class MenuAdministrador {

    //=================================================
    // ATRIBUTOS
    //=================================================
    private GestorPersonas gestorPersonas;
    private GestorVehiculos gestorVehiculos ;
    private GestorViajes gestorViajes;
    private Mapa mapa;
    private Administrador administrador;

    //=================================================
    // CONSTRUCTOR
    //=================================================
    public MenuAdministrador() {
        this.gestorPersonas = GestorPersonas.getInstancia();
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.gestorViajes = GestorViajes.getInstancia();
        this.mapa = Mapa.getInstancia();
    }

    //=================================================
    // MENÚ PRINCIPAL
    //=================================================
    /**
     * Muestra el menú principal del administrador y gestiona la navegación entre opciones.
     */
    public void mostrarMenu(Administrador administrador) {
        this.administrador = administrador;
        while (true) {
            System.out.println("\n========== MENÚ ADMINISTRADOR ==========");
            System.out.println("Bienvenido al sistema de movilidad. Selecciona una opción:");            
            System.out.println("\n1. Gestión de Personas");
            System.out.println("2. Gestión de Vehículos");
            System.out.println("3. Gestión de Bases");
            System.out.println("4. Gestión de Viajes y Tarifas");
            System.out.println("5. Gestión de Empleados");
            System.out.println("6. Consultas y Visualización");
            System.out.println("7. Cerrar Sesión");

            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Opción: ", 1, 7);
            
            switch (opcion) {
                case 1 -> menuGestionPersonas();
                case 2 -> menuGestionVehiculos();
                case 3 -> menuGestionBases();
                case 4 -> menuGestionViajesYTarifas();
                case 5 -> menuGestionEmpleados();
                case 6 -> menuConsultas();
                case 7 -> {
                    System.out.println("Cerrando sesión...");
                    return;
                }
                default -> System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
            }
        }
    }

    //=================================================
    // SUBMENÚS PRINCIPALES
    //=================================================
    
    // Muestra el submenú de gestión de personas
    private void menuGestionPersonas() {
        MenuGestionPersonas menuGestionPersonas = new MenuGestionPersonas();
        menuGestionPersonas.mostrarMenu();
    }

    // Muestra el submenú de gestión de vehículos
    private void menuGestionVehiculos() {
        MenuGestionVehiculos menuGestionVehiculos = new MenuGestionVehiculos();
        menuGestionVehiculos.mostrarMenu();
    }

    // Muestra el submenú de gestión de bases
    private void menuGestionBases() {
        MenuGestionBases menuGestionBases = new MenuGestionBases();
        menuGestionBases.mostrarMenu();
    }

    // Muestra el submenú de gestión de viajes y tarifas
    private void menuGestionViajesYTarifas() {
        MenuGestionViajesYTarifas menuGestionViajesYTarifas = new MenuGestionViajesYTarifas();
        menuGestionViajesYTarifas.mostrarMenu();
    }

    // Muestra el submenú de gestión de empleados
    private void menuGestionEmpleados() {
        MenuGestionEmpleados menuGestionEmpleados = new MenuGestionEmpleados();
        menuGestionEmpleados.mostrarMenu();
    }

    // Muestra el submenú de consultas y visualización
    private void menuConsultas() {
        MenuConsultas menuConsultas = new MenuConsultas();
        menuConsultas.mostrarMenu();
    }
}
