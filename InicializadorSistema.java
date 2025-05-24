import Enum.RolEnum;
import Enum.VehiculoEnum;
import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.GestorBases;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorViajes.GestorViajes;
import GestorViajes.Viaje;
import Utilidades.UtilidadesEntradaDatos;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InicializadorSistema {

    // ==========================
    // ATRIBUTOS PRINCIPALES
    // ==========================
    private final GestorPersonas gestorPersonas;
    private final GestorViajes gestorViajes;
    private final GestorVehiculos gestorVehiculos;
    private final GestorBases gestorBases;
    private final Mapa mapa;

    // ==========================
    // CONSTRUCTOR
    // ==========================
    public InicializadorSistema() {
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.gestorPersonas = GestorPersonas.getInstancia();
        this.gestorViajes = GestorViajes.getInstancia();
        this.gestorBases = GestorBases.getInstancia();
        this.mapa = Mapa.getInstancia();
    }

    // =============================================================
    // ================== INICIALIZADORES PRINCIPALES ==============
    // =============================================================

    // === Inicialización Vacía ===
    public void inicializarVacio() {
        System.out.println("\n--- Inicialización vacía: configuración del mapa ---");
        String nombreMapa = UtilidadesEntradaDatos.getStringLongitud("Nombre de la ciudad", 5, 20);
        int maxX = UtilidadesEntradaDatos.getEnteroPositivo("Dimensión máxima del mapa en X", 25, 100);
        int maxY = UtilidadesEntradaDatos.getEnteroPositivo("Dimensión máxima del mapa en Y", 25, 100);

        inicializarMapa(nombreMapa, maxX, maxY);

        System.out.println("Inicialización vacía: solo superuser registrado. El sistema está listo para configurarse.");
    }

    // === Inicialización con Semilla ===
    public void inicializarConSemilla(int semilla) {
        System.out.println("\n==== INICIALIZACIÓN CON SEMILLA: " + semilla + " ====");

        Random random = new Random(semilla);

        String nombreMapa = "Ciudad Semilla " + semilla;
        int maxX = 25 + random.nextInt(75);
        int maxY = 25 + random.nextInt(75);

        int numMecanicos = 1 + random.nextInt(4);
        int numMantenimiento = 1 + random.nextInt(4);
        int numAdministradores = 1 + random.nextInt(4);
        int numUsuariosStandard = 50 + random.nextInt(50);
        double saldoInicial = 5000 + (random.nextInt(10) * 500);
        int numBases = 5 + random.nextInt(10);
        int capacidad = 25 + random.nextInt(25);
        int minViajesPorUsuario = 25 + random.nextInt(75);
        int maxViajesPorUsuario = 100 + random.nextInt(150);
        Map<VehiculoEnum, Integer> cantidades = new HashMap<>();
        cantidades.put(VehiculoEnum.BICICLETA, 25 + random.nextInt(25));
        cantidades.put(VehiculoEnum.PATINETE, 25 + random.nextInt(25));
        cantidades.put(VehiculoEnum.MOTOGRANDE, 25 + random.nextInt(25));
        cantidades.put(VehiculoEnum.MOTOPEQUENA, 25 + random.nextInt(25));

        maxViajesPorUsuario = Math.max(maxViajesPorUsuario, minViajesPorUsuario + 5);

        System.out.println("Valores generados con semilla " + semilla + ":");
        System.out.println("- Mapa: " + nombreMapa + " (" + maxX + "x" + maxY + ")");
        System.out.println("- Mecánicos: " + numMecanicos);
        System.out.println("- Personal de mantenimiento: " + numMantenimiento);
        System.out.println("- Administradores adicionales: " + numAdministradores);
        System.out.println("- Usuarios estándar: " + numUsuariosStandard);
        System.out.println("- Saldo inicial: " + saldoInicial + "€");
        System.out.println("- Bases: " + numBases);
        System.out.println("- Vehículos por tipo: " + cantidades);
        System.out.println("- Viajes por usuario: " + minViajesPorUsuario + "-" + maxViajesPorUsuario);

        inicializarMapa(nombreMapa, maxX, maxY);
        inicializarPersonas(numMecanicos, numMantenimiento, numAdministradores, numUsuariosStandard, saldoInicial);
        inicializarBases(numBases, capacidad);
        inicializarVehiculos(cantidades);
        inicializarViajes(minViajesPorUsuario, maxViajesPorUsuario, semilla);

        System.out.println("Sistema inicializado correctamente con semilla: " + semilla);
    }

    // === Inicialización Manual ===
    public void inicializarManualmente() {
        System.out.println("\n==== CONFIGURACIÓN MANUAL ====");

        System.out.println("\n--- Configuración del Mapa ---");
        String nombreMapa = UtilidadesEntradaDatos.getStringLongitud("Nombre de la ciudad", 5, 20);
        int maxX = UtilidadesEntradaDatos.getEnteroPositivo("Dimensión máxima del mapa en X", 25, 100);
        int maxY = UtilidadesEntradaDatos.getEnteroPositivo("Dimensión máxima del mapa en Y", 25, 100);

        inicializarMapa(nombreMapa, maxX, maxY);

        System.out.println("\n--- Configuración de Personal ---");
        int numMecanicos = UtilidadesEntradaDatos.getEnteroPositivo("Número de mecánicos", 1, 25);
        int numMantenimiento = UtilidadesEntradaDatos.getEnteroPositivo("Número de personal de mantenimiento", 1, 25);
        int numAdministradores = UtilidadesEntradaDatos.getEnteroPositivo("Número de administradores adicionales", 0, 5);
        int numUsuariosStandard = UtilidadesEntradaDatos.getEnteroPositivo("Número de usuarios estándar", 1, 250);
        double saldoInicial = UtilidadesEntradaDatos.getDecimalPositivo("Saldo inicial para usuarios", 0.0, 10000.0);

        System.out.println("\n--- Configuración de Infraestructura ---");
        int numBases = UtilidadesEntradaDatos.getEnteroPositivo("Número de bases", 1, 20);
        int capacidad = UtilidadesEntradaDatos.getEnteroPositivo("Capacidad de cada base", 25, 100);
        Map<VehiculoEnum, Integer> cantidades = new HashMap<>();
        cantidades.put(VehiculoEnum.BICICLETA, UtilidadesEntradaDatos.getEnteroPositivo("Cantidad de bicicletas", 0, 100));
        cantidades.put(VehiculoEnum.PATINETE, UtilidadesEntradaDatos.getEnteroPositivo("Cantidad de patinetes", 0, 100));
        cantidades.put(VehiculoEnum.MOTOGRANDE, UtilidadesEntradaDatos.getEnteroPositivo("Cantidad de motos grandes", 0, 100));
        cantidades.put(VehiculoEnum.MOTOPEQUENA, UtilidadesEntradaDatos.getEnteroPositivo("Cantidad de motos pequeñas", 0, 100));

        System.out.println("\n--- Configuración de Viajes ---");
        int minViajesPorUsuario = UtilidadesEntradaDatos.getEnteroPositivo("Mínimo de viajes por usuario", 0, 50);
        int maxViajesPorUsuario = UtilidadesEntradaDatos.getEnteroPositivo("Máximo de viajes por usuario", minViajesPorUsuario, 100);
        int semilla = UtilidadesEntradaDatos.getEnteroPositivo("Semilla para generación de viajes y bases", 10000, 99999);

        inicializarPersonas(numMecanicos, numMantenimiento, numAdministradores, numUsuariosStandard, saldoInicial);
        inicializarBases(numBases, capacidad);
        inicializarVehiculos(cantidades);
        inicializarViajes(minViajesPorUsuario, maxViajesPorUsuario, semilla);

        System.out.println("Sistema inicializado correctamente con configuración manual.");
    }

    // =============================================================
    // =============== MÉTODOS AUXILIARES DE INICIO ================
    // =============================================================

    // --- Inicializar Mapa ---
    private void inicializarMapa(String nombreMapa, int maxX, int maxY) {
        System.out.println("Inicializando mapa del sistema...");
        if (mapa.inicializarMapa(nombreMapa, maxX, maxY)) {
            System.out.println("Mapa inicializado: " + nombreMapa + " (Dimensiones: " + maxX + "x" + maxY + ")");
            System.out.printf("Nombre: %s | Dimensiones: %d x %d | Bases: %d\n",
                    mapa.getNombre(), mapa.getMaxCoordenadaX(), mapa.getMaxCoordenadaY(), mapa.getListaBases().size());
        } else {
            System.out.println("Error al inicializar el mapa. No se pudo crear el mapa con los valores proporcionados.");
        }
    }

    // --- Inicializar Personas ---
    private void inicializarPersonas(int numMecanicos, int numMantenimiento, int numAdministradores, int numUsuariosStandard, double saldoInicial) {
        System.out.println("Inicializando personas en el sistema...");
        inicializarPersonalTecnico(numMecanicos, numMantenimiento);
        inicializarAdministradores(numAdministradores);
        inicializarUsuariosStandard(numUsuariosStandard, saldoInicial);
        System.out.println("Personas inicializadas correctamente.");
    }

    // --- Inicializar Personal Técnico ---
    private void inicializarPersonalTecnico(int numMecanicos, int numMantenimiento) {
        System.out.println("Inicializando personal técnico...");
        for (int i = 1; i <= numMecanicos; i++) {
            String dni = String.format("%08d", i) + "M";
            String nombre = "Mecanico" + i;
            String apellido = "Apellido" + i;
            gestorPersonas.addPersona(nombre, apellido, dni, RolEnum.MECANICO);
            System.out.println("- Mecánico creado: " + nombre + " " + apellido + " (DNI: " + dni + ")");
        }
        for (int i = 1; i <= numMantenimiento; i++) {
            String dni = String.format("%08d", i) + "T";
            String nombre = "Mantenimiento" + i;
            String apellido = "Apellido" + i;
            gestorPersonas.addPersona(nombre, apellido, dni, RolEnum.MANTENIMIENTO);
            System.out.println("- Personal de mantenimiento creado: " + nombre + " " + apellido + " (DNI: " + dni + ")");
        }
        System.out.println("Total: " + numMecanicos + " mecánicos y " + numMantenimiento + " personal de mantenimiento creados.");
    }

    // --- Inicializar Administradores ---
    private void inicializarAdministradores(int numAdministradores) {
        System.out.println("Inicializando administradores adicionales...");
        for (int i = 1; i <= numAdministradores; i++) {
            String dni = String.format("%08d", i) + "A";
            String nombre = "Administrador" + i;
            String apellido = "Apellido" + i;
            gestorPersonas.addPersona(nombre, apellido, dni, RolEnum.ADMINISTRADOR);
            System.out.println("- Administrador creado: " + nombre + " " + apellido + " (DNI: " + dni + ")");
        }
        System.out.println("Total: " + numAdministradores + " administradores adicionales creados.");
    }

    // --- Inicializar Usuarios Standard ---
    private void inicializarUsuariosStandard(int numUsuariosStandard, double saldoInicial) {
        System.out.println("Inicializando usuarios estándar...");
        for (int i = 1; i <= numUsuariosStandard; i++) {
            String dni = String.format("%08d", i) + "S";
            String nombre = "Standard" + i;
            String apellido = "Apellido" + i;
            gestorPersonas.addPersona(nombre, apellido, dni, RolEnum.USUARIO_STANDARD);
            Usuario usuario = (Usuario) gestorPersonas.getPersonaPorDNI(dni);
            if (usuario != null) {
                usuario.agregarSaldo(saldoInicial);
            }
            System.out.println("- Usuario estándar creado: " + nombre + " " + apellido + " (DNI: " + dni + ", Saldo: " + saldoInicial + "€)");
        }
        System.out.println("Total: " + numUsuariosStandard + " usuarios estándar creados con saldo inicial de " + saldoInicial + "€.");
    }

    // --- Inicializar Bases ---
    private void inicializarBases(int numeroBases, int capacidad) {
        System.out.println("Inicializando bases en el sistema...");
        int i;
        int maxX = mapa.getMaxCoordenadaX();
        int maxY = mapa.getMaxCoordenadaY();
        Random random = new Random();
        for (i = 1; i <= numeroBases; i++) {
            int x, y;
            boolean libre;
            do {
                x = random.nextInt(maxX);
                y = random.nextInt(maxY);
                libre = true;
                for (Base b : mapa.getListaBases()) {
                    if (b.getCoordenadaX() == x && b.getCoordenadaY() == y) {
                        libre = false;
                        break;
                    }
                }
            } while (!libre);
            gestorBases.addBasePorCoordenadas("Base" + i, x, y, capacidad);
            System.out.println("Base" + i + " creada en la posición (" + x + ", " + y + ").");
        }
        System.out.println((i - 1) + " bases inicializadas correctamente.");
    }

    // --- Inicializar Vehículos ---
    private void inicializarVehiculos(Map<VehiculoEnum, Integer> cantidadPorTipo) {
        System.out.println("Inicializando vehículos en el sistema...");
        int totalVehiculos = 0;
        int maxX = mapa.getMaxCoordenadaX();
        int maxY = mapa.getMaxCoordenadaY();
        Random random = new Random();
        for (Map.Entry<VehiculoEnum, Integer> entry : cantidadPorTipo.entrySet()) {
            VehiculoEnum tipo = entry.getKey();
            int cantidad = entry.getValue();
            for (int i = 0; i < cantidad; i++) {
                if (tipo == VehiculoEnum.BICICLETA || tipo == VehiculoEnum.PATINETE) {
                    Base base = getBaseMenosVehiculosDisponibles();
                    if (base != null && gestorVehiculos.addVehiculoBase(tipo, base) != null) {
                        totalVehiculos++;
                    }
                } else if (tipo == VehiculoEnum.MOTOGRANDE || tipo == VehiculoEnum.MOTOPEQUENA) {
                    int x, y;
                    boolean libre;
                    do {
                        x = random.nextInt(maxX);
                        y = random.nextInt(maxY);
                        libre = true;
                        for (Vehiculo v : gestorVehiculos.getListaVehiculos()) {
                            if ((v.getVehiculoEnum() == VehiculoEnum.MOTOGRANDE || v.getVehiculoEnum() == VehiculoEnum.MOTOPEQUENA)
                                    && v instanceof GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas) {
                                GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas vCoord = (GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas) v;
                                if (vCoord.getCoordenadaX() == x && vCoord.getCoordenadaY() == y) {
                                    libre = false;
                                    break;
                                }
                            }
                        }
                    } while (!libre);
                    if (gestorVehiculos.addVehiculoMapa(tipo, x, y) != null) {
                        totalVehiculos++;
                    }
                }
            }
        }
        System.out.println("Total: " + totalVehiculos + " vehículos añadidos y distribuidos automáticamente.");
    }

    // --- Obtener Base con Menos Vehículos Disponibles ---
    private Base getBaseMenosVehiculosDisponibles() {
        Base baseMenosVehiculos = null;
        int minVehiculosActivos = 0;
        boolean primeraBase = true;

        for (Base base : mapa.getListaBasesSinAverias()) {
            if (base.getEspaciosLibres() > 0) {

                List<Vehiculo> vehiculosActivos = new ArrayList<>();
                for (Vehiculo vehiculo : base.getListaVehiculos()) {
                    if (!vehiculo.getAverias() && vehiculo.getBateria() > 20 && !vehiculo.getReservado()) {
                        vehiculosActivos.add(vehiculo);
                    }
                }

                if (primeraBase || vehiculosActivos.size() < minVehiculosActivos) {
                    minVehiculosActivos = vehiculosActivos.size();
                    baseMenosVehiculos = base;
                    primeraBase = false;
                }
            }
        }
        return baseMenosVehiculos;
    }

    // --- Inicializar Viajes ---
    private void inicializarViajes(int minViajesPorUsuario, int maxViajesPorUsuario, int seed) {
        System.out.println("Generando viajes de ejemplo para usuarios...");
        Random random = new Random(seed);
        List<Usuario> usuarios = gestorPersonas.obtenerTodosLosUsuarios();
        VehiculoEnum[] tiposVehiculo = {
            VehiculoEnum.BICICLETA,
            VehiculoEnum.PATINETE,
            VehiculoEnum.MOTOGRANDE,
            VehiculoEnum.MOTOPEQUENA
        };
        int totalViajes = 0;
        int usuariosConViajes = 0;
        LocalDateTime ahora = LocalDateTime.now();

        List<Base> bases = mapa.getListaBases();
        int maxX = mapa.getMaxCoordenadaX();
        int maxY = mapa.getMaxCoordenadaY();

        for (Usuario usuario : usuarios) {
            int numViajes = random.nextInt(maxViajesPorUsuario - minViajesPorUsuario + 1) + minViajesPorUsuario;
            boolean usuarioTieneViajes = false;
            LocalDateTime fechaViaje = ahora.minusDays(random.nextInt(30));

            for (int i = 0; i < numViajes; i++) {
                VehiculoEnum tipoVehiculo = tiposVehiculo[random.nextInt(tiposVehiculo.length)];

                List<Vehiculo> listaVehiculos = gestorVehiculos.getListaVehiculos();
                Vehiculo vehiculo = null;
                List<Vehiculo> listaVehiculosPosibles = new ArrayList<>();
                for (Vehiculo v : listaVehiculos) {
                    if (v.getVehiculoEnum() == tipoVehiculo && v.getDisponible(usuario)) {
                        listaVehiculosPosibles.add(v);
                    }
                }
                if (!listaVehiculosPosibles.isEmpty()) {
                    vehiculo = listaVehiculosPosibles.get(random.nextInt(listaVehiculosPosibles.size()));
                }

                if (vehiculo == null) {
                    continue;
                }

                int minutos = 1 + random.nextInt(120);
                LocalDateTime fechaFin = fechaViaje.plusMinutes(minutos);

                if (tipoVehiculo == VehiculoEnum.BICICLETA || tipoVehiculo == VehiculoEnum.PATINETE) {
                    Base baseOrigen = bases.get(random.nextInt(bases.size()));
                    Base baseDestino = bases.get(random.nextInt(bases.size()));
                    while (baseDestino == baseOrigen && bases.size() > 1) {
                        baseDestino = bases.get(random.nextInt(bases.size()));
                    }
                    Viaje viaje = new Viaje(vehiculo, usuario, fechaViaje, fechaFin, baseOrigen, baseDestino);
                    gestorViajes.registrarViaje(viaje);

                    double duracionHoras = (double)(fechaFin.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() -
                            fechaViaje.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()) / 3600000.0;
                    double tarifaBase = tipoVehiculo.getTarifa();
                    double costoBase = duracionHoras * tarifaBase;
                    double costoFinal = costoBase;
                    if (usuario.getRol() == RolEnum.USUARIO_PREMIUM) {
                        double descuento = tipoVehiculo.getDescuento();
                        costoFinal = costoBase * (1 - descuento / 100.0);
                    }
                    usuario.descontarCosto(costoFinal);
                    viaje.setCosto(costoFinal);

                    vehiculo.setEnUso(false);
                    GestorVehiculosBases.Vehiculos.VehiculoDeBase vBase = (GestorVehiculosBases.Vehiculos.VehiculoDeBase) vehiculo;
                    if (vBase.getBase() == null || !vBase.getBase().getNombre().equals(baseDestino.getNombre())) {
                        gestorVehiculos.moverVehiculoABase(vehiculo.getID(), baseDestino.getNombre());
                        baseDestino.incrementarViajesFinalizadosBase();
                    }
                } else {
                    int origenX = random.nextInt(maxX);
                    int origenY = random.nextInt(maxY);
                    int destinoX = random.nextInt(maxX);
                    int destinoY = random.nextInt(maxY);
                    while (origenX == destinoX && origenY == destinoY) {
                        destinoX = random.nextInt(maxX);
                        destinoY = random.nextInt(maxY);
                    }
                    Viaje viaje = new Viaje(vehiculo, usuario, fechaViaje, fechaFin, origenX, origenY, destinoX, destinoY);
                    gestorViajes.registrarViaje(viaje);

                    double duracionHoras = (double)(fechaFin.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() -
                            fechaViaje.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()) / 3600000.0;
                    double tarifaBase = tipoVehiculo.getTarifa();
                    double costoBase = duracionHoras * tarifaBase;
                    double costoFinal = costoBase;
                    if (usuario.getRol() == RolEnum.USUARIO_PREMIUM) {
                        double descuento = tipoVehiculo.getDescuento();
                        costoFinal = costoBase * (1 - descuento / 100.0);
                    }
                    usuario.descontarCosto(costoFinal);
                    viaje.setCosto(costoFinal);

                    vehiculo.setEnUso(false);
                    GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas vCoord = (GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas) vehiculo;
                    if (vCoord.getCoordenadaX() != destinoX || vCoord.getCoordenadaY() != destinoY) {
                        gestorVehiculos.moverVehiculoACoordenadas(vehiculo.getID(), destinoX, destinoY);
                    }
                }

                totalViajes++;
                usuarioTieneViajes = true;

                fechaViaje = fechaViaje.minusHours(random.nextInt(24)).minusMinutes(random.nextInt(60));
            }
            if (usuarioTieneViajes) {
                usuariosConViajes++;
            }
        }
        System.out.println("Viajes generados: " + totalViajes + " | Usuarios con viajes: " + usuariosConViajes);
    }
}