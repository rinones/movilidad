import Enum.RolEnum;
import Enum.VehiculoEnum;
import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Administrador;
import GestorPersonas.Personas.Persona;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorViajes.Beneficios;
import GestorViajes.GestorViajes;
import GestorViajes.Penalizaciones;
import GestorViajes.Viaje;
import Menus.Menu;
import Utilidades.UtilidadesEntradaDatos;
import java.util.List;

public final class Movilidad {

    // ===========================
    // ATRIBUTOS PRINCIPALES
    // ===========================
    private final GestorPersonas gestorPersonas;
    private final GestorViajes gestorViajes;
    private final GestorVehiculos gestorVehiculos;
    private final Mapa mapa;
    private final InicializadorSistema inicializadorSistema;

    // ===========================
    // CONSTRUCTOR Y FLUJO PRINCIPAL
    // ===========================
    public Movilidad() {
        System.out.println("Constructor de Movilidad ejecutado.");

        // Instancia los componentes principales del sistema
        mapa = Mapa.getInstancia();
        gestorPersonas = GestorPersonas.getInstancia();
        gestorViajes = GestorViajes.getInstancia();
        gestorVehiculos = GestorVehiculos.getInstancia();
        inicializadorSistema = new InicializadorSistema();


        // Crea el superusuario administrador
        crearSuperUser();

        // Muestra el menú de inicialización y ejecuta la opción elegida
        menuInicializacion();

        // Dibuja el mapa inicial en consola
        mapa.dibujarMapa();

        // Muestra un resumen del estado inicial del sistema
        mostrarResumenInicializacion();

        // Inicia el menú principal de la aplicación
        iniciarMenu();

        System.out.println("Aplicación finalizada.");
    }

    // ===========================
    // INICIALIZACIÓN DEL SISTEMA
    // ===========================

    // Crea el usuario administrador principal (superuser)
    private void crearSuperUser() {
        // Crea un administrador por defecto
        Administrador superuser = new Administrador("Super", "User", "00000000A");
        // Añade el superuser al gestor de personas
        gestorPersonas.addAdministradorInicial(superuser);
        System.out.println("Administrador inicial (superuser) creado correctamente.");
    }

    // Muestra el menú de inicialización y ejecuta la opción seleccionada
    private void menuInicializacion() {
        System.out.println("\n==== MENÚ DE INICIALIZACIÓN ====");
        System.out.println("1. Inicializar sistema vacío (solo superuser)");
        System.out.println("2. Inicializar sistema con semilla predeterminada");
        System.out.println("3. Inicializar sistema con semilla personalizada");
        System.out.println("4. Configuración manual");

        // Solicita al usuario una opción válida
        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Seleccione una opción", 1, 4);

        switch (opcion) {
            case 1:
                // Inicializa el sistema vacío
                inicializadorSistema.inicializarVacio();
                break;
            case 2:
                // Inicializa el sistema con una semilla predeterminada
                inicializadorSistema.inicializarConSemilla(12345);
                break;
            case 3:
                // Solicita una semilla personalizada y la usa para inicializar
                int semilla = solicitarSemilla();
                inicializadorSistema.inicializarConSemilla(semilla);
                break;
            case 4:
                // Permite la configuración manual del sistema
                inicializadorSistema.inicializarManualmente();
                break;
        }
    }

    // Solicita una semilla de inicialización de 5 dígitos al usuario
    private int solicitarSemilla() {
        int semilla;
        do {
            System.out.print("Introduzca una semilla de inicialización (5 dígitos): ");
            try {
                // Solicita un número entre 10000 y 99999
                semilla = UtilidadesEntradaDatos.getEnteroPositivo("Semilla", 10000, 99999);
                if (semilla < 10000 || semilla > 99999) {
                    // Si la semilla no tiene 5 dígitos, muestra un mensaje de error
                    System.out.println("La semilla debe tener exactamente 5 dígitos.");
                    semilla = -1;
                }
            } catch (NumberFormatException e) {
                // Si la entrada no es válida, muestra un mensaje de error
                System.out.println("Por favor, introduzca un número válido.");
                semilla = -1;
            }
        } while (semilla == -1); // Repite hasta que la semilla sea válida
        return semilla;
    }

    // ===========================
    // MENÚ PRINCIPAL
    // ===========================

    // Inicia el menú principal de la aplicación
    private void iniciarMenu() {
        // Crea una instancia del menú principal
        Menu menu = new Menu();
        // Inicia la interacción con el usuario
        menu.iniciar();
    }

    // ===========================
    // RESUMEN DE INICIALIZACIÓN
    // ===========================

    // Muestra un resumen del estado inicial del sistema
    private void mostrarResumenInicializacion() {
        System.out.println("\n==== RESUMEN DE INICIALIZACIÓN DEL SISTEMA ====");

        // Información del mapa
        System.out.println("\n--- INFORMACIÓN DEL MAPA ---");
        System.out.printf("Nombre: %s | Dimensiones: %d x %d | Bases: %d\n",
                mapa.getNombre(), mapa.getMaxCoordenadaX(), mapa.getMaxCoordenadaY(), mapa.getListaBases().size());

        // Resumen de usuarios
        System.out.println("\n--- RESUMEN DE USUARIOS ---");
        List<Usuario> usuarios = gestorPersonas.obtenerTodosLosUsuarios();
        int usuariosStandard = 0, usuariosPremium = 0;
        // Cuenta usuarios estándar y premium
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() == RolEnum.USUARIO_STANDARD) usuariosStandard++;
            else if (usuario.getRol() == RolEnum.USUARIO_PREMIUM) usuariosPremium++;
        }
        System.out.printf("Total: %d | Estándar: %d | Premium: %d\n",
                usuarios.size(), usuariosStandard, usuariosPremium);

        // Resumen de personal
        System.out.println("\n--- RESUMEN DE PERSONAL ---");
        int totalMecanicos = 0, totalMantenimiento = 0, totalAdmins = 0;
        // Cuenta el personal por rol
        for (Object persona : gestorPersonas.getPersonas()) {
            RolEnum rol = ((Persona) persona).getRol();
            if (rol == RolEnum.MECANICO) totalMecanicos++;
            else if (rol == RolEnum.MANTENIMIENTO) totalMantenimiento++;
            else if (rol == RolEnum.ADMINISTRADOR) totalAdmins++;
        }
        int totalPersonal = totalMecanicos + totalMantenimiento + totalAdmins;
        System.out.printf("Total: %d | Administradores: %d | Mecánicos: %d | Mantenimiento: %d\n",
                totalPersonal, totalAdmins, totalMecanicos, totalMantenimiento);

        // Resumen de vehículos
        System.out.println("\n--- RESUMEN DE VEHÍCULOS ---");
        int enAlmacen = gestorVehiculos.getListaVehiculosAlmacenados().size();
        int enMapa = gestorVehiculos.getVehiculosEnMapa().size();
        int enBases = gestorVehiculos.getVehiculosEnBases().size();
        int totalVehiculos = enAlmacen + enMapa + enBases;
        // Muestra el total y el desglose de vehículos
        System.out.printf("Total: %d | En almacén: %d | En mapa: %d | En bases: %d\n",
                totalVehiculos, enAlmacen, enMapa, enBases);

        // Desglose por tipo de vehículo
        int bicicletas = 0, patinetes = 0, motosGrandes = 0, motosPequenas = 0;
        // Contar en almacén
        for (var v : gestorVehiculos.getListaVehiculosAlmacenados()) {
            switch (v.getVehiculoEnum()) {
                case BICICLETA: bicicletas++; break;
                case PATINETE: patinetes++; break;
                case MOTOGRANDE: motosGrandes++; break;
                case MOTOPEQUENA: motosPequenas++; break;
            }
        }
        // Contar en mapa
        for (var v : gestorVehiculos.getVehiculosEnMapa()) {
            switch (v.getVehiculoEnum()) {
                case BICICLETA: bicicletas++; break;
                case PATINETE: patinetes++; break;
                case MOTOGRANDE: motosGrandes++; break;
                case MOTOPEQUENA: motosPequenas++; break;
            }
        }
        // Contar en bases
        for (var v : gestorVehiculos.getVehiculosEnBases()) {
            switch (v.getVehiculoEnum()) {
                case BICICLETA: bicicletas++; break;
                case PATINETE: patinetes++; break;
                case MOTOGRANDE: motosGrandes++; break;
                case MOTOPEQUENA: motosPequenas++; break;
            }
        }
        System.out.printf("Bicicletas: %d | Patinetes: %d | Motos grandes: %d | Motos pequeñas: %d\n",
                bicicletas, patinetes, motosGrandes, motosPequenas);

        // Resumen de viajes
        System.out.println("\n--- RESUMEN DE VIAJES ---");
        List<Viaje> todosLosViajes = gestorViajes.getListaTodosViajes();
        int viajesBicicleta = 0, viajesPatinete = 0;
        int viajesMotoGrande = 0, viajesMotoPequena = 0;
        // Cuenta los viajes por tipo de vehículo
        for (Viaje viaje : todosLosViajes) {
            VehiculoEnum tipoVehiculo = viaje.getVehiculo().getVehiculoEnum();
            switch (tipoVehiculo) {
                case BICICLETA: viajesBicicleta++; break;
                case PATINETE: viajesPatinete++; break;
                case MOTOGRANDE: viajesMotoGrande++; break;
                case MOTOPEQUENA: viajesMotoPequena++; break;
            }
        }
        System.out.printf("Total viajes: %d | Bicicletas: %d | Patinetes: %d | Motos grandes: %d | Motos pequeñas: %d\n",
                todosLosViajes.size(), viajesBicicleta, viajesPatinete, viajesMotoGrande, viajesMotoPequena);

        // Información de tarifas y descuentos
        System.out.println("\n--- INFORMACIÓN DE TARIFAS Y DESCUENTOS ---");
        gestorViajes.mostrarTarifasActuales();
        gestorViajes.mostrarDescuentosActuales();

        // Estado del sistema (reservas y privilegios)
        Beneficios beneficios = new Beneficios();
        System.out.println("\n--- ESTADO DEL SISTEMA ---");
        // Muestra si las reservas están habilitadas
        if (beneficios.getReservasPermitidas()) {
            System.out.println("Reservas: HABILITADAS\n");
        } else {
            System.out.println("Reservas: DESHABILITADAS\n");
        }
        // Muestra si el usuario premium puede usar vehículos con poca batería
        if (beneficios.getBateriaPremiumPermitida()) {
            System.out.println("Usuario Premium puede utilizar vehiculo con bateria entre 10%-20%: HABILITADO\n");
        } else {
            System.out.println("Usuario Premium puede utilizar vehiculo con bateria entre 10%-20%: DESHABILITADA\n");
        }

        // Penalización por agotar la batería antes de entregar el vehículo
        System.out.println("Penalización por agotar la batería antes de entregar el vehículo: " +
                Penalizaciones.obtenerPenalizacionPorBateriaAgotada() + "€\n");

        System.out.println("\n==== SISTEMA INICIADO Y LISTO PARA USAR ====");
    }
}
