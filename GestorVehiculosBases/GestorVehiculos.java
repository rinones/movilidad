package GestorVehiculosBases;

import Enum.VehiculoEnum;
import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Mantenimiento;
import GestorPersonas.Personas.Mecanico;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Bicicleta;
import GestorVehiculosBases.Vehiculos.MotoGrande;
import GestorVehiculosBases.Vehiculos.MotoPequena;
import GestorVehiculosBases.Vehiculos.Patinete;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import java.util.ArrayList;
import java.util.List;

public class GestorVehiculos {

    private static GestorVehiculos instancia;
    private List<Vehiculo> vehiculos = new ArrayList<>();
    private List<Vehiculo> vehiculosEnMapa = new ArrayList<>();
    private List<Vehiculo> vehiculosEnBases = new ArrayList<>();
    private int contadorID = 1;
    private Mapa mapa = Mapa.getInstancia();

    private GestorVehiculos() {
    }

    public static GestorVehiculos getInstancia() {
        if (instancia == null) {
            instancia = new GestorVehiculos();
        }
        return instancia;
    }

    //=================================================
    // GETTERS
    //=================================================
    public Vehiculo getVehiculoPorID(int ID) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getID() == ID) {
                return vehiculo;
            }
        }
        return null;
    }


    public List<Vehiculo> getVehiculosEnMapa() {
        return new ArrayList<>(vehiculosEnMapa);
    }

    public List<Vehiculo> getVehiculosEnBases() {
        return new ArrayList<>(vehiculosEnBases);
    }

    public List<Vehiculo> getListaVehiculosAlmacenados() {
        List<Vehiculo> vehiculosAlmacenados = new ArrayList<>(vehiculos);
        vehiculosAlmacenados.removeAll(vehiculosEnMapa);
        vehiculosAlmacenados.removeAll(vehiculosEnBases);
        return vehiculosAlmacenados;
    }

    public List<Vehiculo> getListaVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    //=================================================
    // MÉTODOS DE GESTIÓN DE VEHÍCULOS
    //=================================================

    /*
    Metodo para añadir un vehículo a la base  y a la lista completa de vehiculos
    Verifica si el vehiculo es nulo, si la base es nula y si la base tiene espacios libres.
     */
    public Vehiculo addVehiculoBase(VehiculoEnum tipoVehiculo, Base base) {
        if (base == null) {
            System.out.println("La base no puede ser nula");
            return null;
        }
        if (tipoVehiculo == null) {
            System.out.println("El tipo de vehículo no puede ser nulo");
            return null;
        }
        if (base.getEspaciosLibres() <= 0) {
            System.out.println("La base '" + base.getNombre() + "' no tiene espacios disponibles");
            return null;
        }
        VehiculoDeBase vehiculo;
        if (tipoVehiculo == VehiculoEnum.BICICLETA) {
            vehiculo = new Bicicleta(contadorID, tipoVehiculo);
            contadorID++;
        } else if (tipoVehiculo == VehiculoEnum.PATINETE) {
            vehiculo = new Patinete(contadorID, tipoVehiculo);
            contadorID++;
        } else {
            System.out.println("Tipo de vehículo no válido");
            return null;
        }
        if (base.addVehiculo(vehiculo, contadorID)) {
            vehiculosEnBases.add(vehiculo);
            vehiculos.add(vehiculo);
            vehiculo.setBase(base);
            return vehiculo;
        } else {
            return null;
        }
    }

    /*
    Metodo para sacar un vehículo de la base sin eliminarlo de la lista completa de vehiculos
    Verifica si el ID es valido, si el vehiculo es instancia de VehiculoDeBase y si la base es nula.
         */
    public boolean removeVehiculoBasePorID(int ID) {
        if (ID <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }
        Vehiculo vehiculo = getVehiculoPorID(ID);
        if (vehiculo instanceof VehiculoDeBase) {
            Base base = ((VehiculoDeBase) vehiculo).getBase();
            if (base == null) {
                System.out.println("Error: El vehículo con ID " + ID + " no está asociado a ninguna base.");
                return false;
            }
            if (base.removeVehiculoPorID(ID)) {
                vehiculosEnBases.remove(vehiculo);
                System.out.println("Vehículo ID " + ID + " retirado de la base y trasladado al almacen.");
                return true;
            } else {
                System.out.println("Error: No se pudo retirar el vehículo ID " + ID + " de la base.");
                return false;
            }
        } else {
            System.out.println("Error: El vehículo con ID " + ID + " no es un VehiculoDeBase.");
            return false;
        }
    }

    /*
    Método para añadir un vehículo al mapa y a la lista completa de vehículos.
    Verifica si el tipo de vehículo es nulo, si las coordenadas son válidas y si la posición está ocupada.
    */
    public Vehiculo addVehiculoMapa(VehiculoEnum tipoVehiculo, int x, int y) {
        if (tipoVehiculo == null) {
            System.out.println("El tipo dehículo no puede ser nulo");
            return null;
        }
        if (!mapa.sonCoordenadasValidas(x, y)) {
            System.out.println("Las coordenadas (" + x + ", " + y + ") son inválidas");
            return null;
        }
        if (mapa.sonCoordenadasOcupadas(x, y)) {
            System.out.println("La posición (" + x + ", " + y + ") ya está ocupada");
            return null;
        }
        VehiculoDeCoordenadas vehiculo;
        if (tipoVehiculo == VehiculoEnum.MOTOGRANDE) {
            vehiculo = new MotoGrande(contadorID, tipoVehiculo);
            contadorID++;
        } else if (tipoVehiculo == VehiculoEnum.MOTOPEQUENA) {
            vehiculo = new MotoPequena(contadorID, tipoVehiculo);
            contadorID++;
        } else
        {
            System.out.println("Tipo de vehículo no válido");
            return null;
        }
        if (mapa.addVehiculo(x, y, vehiculo)) {
            vehiculosEnMapa.add(vehiculo);
            vehiculos.add(vehiculo);
            vehiculo.setCoordenadaX(x);
            vehiculo.setCoordenadaY(y);
            return vehiculo;
        } else {
            return null;
        }
    }

    /*
    Metodo para sacar un vehículo del mapa sin eliminarlo de la lista completa de vehiculos
    Verifica si el ID es valido
    */
    public boolean removeVehiculoMapaPorCoordenadas(int x, int y) {
        if (!mapa.sonCoordenadasValidas(x, y)){
            System.out.println("Error: Las coordenadas (" + x + ", " + y + ") son inválidas");
            return false;
        }
        if (mapa.removeVehiculoPorCoordenadas(x, y)) {
            Object contenidoCelda = mapa.getContenidoCelda(x, y);
            int ID = ((Vehiculo) contenidoCelda).getID();
            if (contenidoCelda instanceof Vehiculo) {
                Vehiculo vehiculo = getVehiculoPorID(ID);
                vehiculosEnMapa.remove(vehiculo);
            }
            System.out.println("Vehículo ID " + ID + " retirado del mapa y trasladado al almacen.");
            return true;
        } else {
            System.out.println("Error: No se pudo retirar el vehículo de las coordenadas (" + x + ", " + y + ")");
            return false;
        }
    }

    

    public boolean desecharVehiculoPorID(int ID) {
        if (ID <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }
        Vehiculo vehiculo = getVehiculoPorID(ID);
        if (vehiculo != null) {
            if (vehiculo instanceof VehiculoDeCoordenadas) {
                vehiculosEnMapa.remove(vehiculo);
                mapa.removeVehiculoPorID(ID);
            } else if (vehiculo instanceof VehiculoDeBase) {
                Base base = ((VehiculoDeBase) vehiculo).getBase();
                if (base != null) {
                    base.removeVehiculoPorID(ID);
                    vehiculosEnBases.remove(vehiculo);
                }
            }
            vehiculos.remove(vehiculo);
            System.out.println("Vehículo ID " + ID + " retirado de la flota y desechado.");
            return true;
        } else {
            System.out.println("Error: No se encontró el vehículo con ID " + ID);
            return false;
        }
    }

    public Vehiculo addVehiculoAlmacen(VehiculoEnum tipoVehiculo) {
        Vehiculo vehiculo = null;
        switch (tipoVehiculo) {
            case BICICLETA:
                vehiculo = new Bicicleta(contadorID, tipoVehiculo);
                break;
            case PATINETE:
                vehiculo = new Patinete(contadorID, tipoVehiculo);
                break;
            case MOTOGRANDE:
                vehiculo = new MotoGrande(contadorID, tipoVehiculo);
                break;
            case MOTOPEQUENA:
                vehiculo = new MotoPequena(contadorID, tipoVehiculo);
                break;
        }
        if (vehiculo != null) {
            vehiculos.add(vehiculo);
            contadorID++;
        }
        return vehiculo;
    }

    //=================================================
    // MÉTODOS DE GESTIÓN DE VEHÍCULOS
    //=================================================

    /*
    Método para mover un vehículo de coordenadas a otra posición en el mapa.
    Verifica si el ID es válido, si el vehículo existe, si es un vehículo de coordenadas,
    si las nuevas coordenadas son válidas y si la posición está ocupada.
     */
    public boolean moverVehiculoACoordenadas(int ID, int nuevaCoordenadaX, int nuevaCoordenadaY) {
        if (ID <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }
        Vehiculo vehiculo = getVehiculoPorID(ID);
        if (vehiculo == null) {
            System.out.println("Error: No se encontró ningún vehículo con ID " + ID);
            return false;
        }
        if (!(vehiculo instanceof VehiculoDeCoordenadas)) {
            System.out.println("Error: El vehículo con ID " + ID + " no es un vehículo de coordenadas.");
            return false;
        }
        if (!vehiculosEnMapa.contains(vehiculo)) {
            System.out.println("Error: El vehículo con ID " + ID + " no está actualmente en el mapa.");
            return false;
        }
        if (!mapa.sonCoordenadasValidas(nuevaCoordenadaX, nuevaCoordenadaY)) {
            System.out.println("Error: Las coordenadas (" + nuevaCoordenadaX + ", " + nuevaCoordenadaY + ") están fuera de los límites del mapa.");
            return false;
        }
        VehiculoDeCoordenadas vehiculoMovido = (VehiculoDeCoordenadas) vehiculo;
        int coordenadaXActual = vehiculoMovido.getCoordenadaX();
        int coordenadaYActual = vehiculoMovido.getCoordenadaY();
        if (coordenadaXActual == nuevaCoordenadaX && coordenadaYActual == nuevaCoordenadaY) {
            System.out.println("El vehículo ya está en la posición (" + nuevaCoordenadaX + ", " + nuevaCoordenadaY + ")");
            return false;
        }
        if (mapa.sonCoordenadasOcupadas(nuevaCoordenadaX, nuevaCoordenadaY)) {
            System.out.println("Error: La posición (" + nuevaCoordenadaX + ", " + nuevaCoordenadaY + ") ya está ocupada.");
            return false;
        }
        if (!mapa.sonCoordenadasValidas(coordenadaXActual, coordenadaYActual)) {
            System.out.println("Error: Las coordenadas actuales del vehículo son inválidas.");
            return false;
        }
        if (mapa.removeVehiculoPorCoordenadas(coordenadaXActual, coordenadaYActual)) {
            vehiculoMovido.setCoordenadaX(nuevaCoordenadaX);
            vehiculoMovido.setCoordenadaY(nuevaCoordenadaY);
            if (mapa.addVehiculo(nuevaCoordenadaX, nuevaCoordenadaY, vehiculoMovido)) {
                System.out.println("Vehículo " + vehiculo.getVehiculoEnum() + " con ID " + ID
                        + " movido de (" + coordenadaXActual + ", " + coordenadaYActual + ") a (" + nuevaCoordenadaX + ", " + nuevaCoordenadaY + ")");
                return true;
            } else {
                System.out.println("Error: No se pudo mover el vehículo a la nueva posición.");
            }
        } else {
            System.out.println("Error: No se pudo quitar el vehículo de su posición actual.");
        }
        return false;
    }

    /*
    Método para mover un vehículo de una base a otra base.
    Verifica si el ID es válido, si el vehículo existe, si es un VehiculoDeBase,
    si la base destino existe, si tiene espacios disponibles y si el vehículo ya está en la base destino.
     */
    public boolean moverVehiculoABase(int ID, String nombre) {
        if (ID <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("Error: Nombre de base inválido");
            return false;
        }
        Vehiculo vehiculo = getVehiculoPorID(ID);
        if (vehiculo == null) {
            System.out.println("Error: No se encontró el vehículo con ID " + ID);
            return false;
        }
        if (!(vehiculo instanceof VehiculoDeBase)) {
            System.out.println("Error: El vehículo con ID " + ID + " no es un VehiculoDeBase.");
            return false;
        }
        Base baseDestino = mapa.getBasePorNombre(nombre);
        if (baseDestino == null) {
            System.out.println("Error: No se encontró la base '" + nombre + "'");
            return false;
        }
        if (baseDestino.getEspaciosLibres() <= 0) {
            System.out.println("Error: La base '" + nombre + "' no tiene espacios disponibles");
            return false;
        }
        Base baseActual = ((VehiculoDeBase) vehiculo).getBase();
        if (baseActual != null && baseActual.equals(baseDestino)) {
            System.out.println("Error: El vehículo ya está en la base '" + nombre + "'");
            return false;
        }
        // Quitar de la base actual si es necesario
        if (baseActual != null) {
            baseActual.removeVehiculoPorID(ID);
        }
        // Añadir a la base destino
        if (baseDestino.addVehiculo(vehiculo, ID)) {
            ((VehiculoDeBase) vehiculo).setBase(baseDestino);
            if (!vehiculosEnBases.contains(vehiculo)) {
                vehiculosEnBases.add(vehiculo);
            }
            System.out.println("Vehículo ID " + ID + " movido a la base '" + nombre + "'");
            return true;
        } else {
            System.out.println("Error: No se pudo añadir el vehículo a la base '" + nombre + "'");
            return false;
        }
    }

    
    //=================================================
    // MÉTODO LOCALIZAR VEHÍCULO MÁS CERCANO
    //=================================================

    public Vehiculo getVehiculoMasCercano(int x, int y, VehiculoEnum tipoVehiculo, Usuario usuario) {
        double distanciaMinima = 0;
        Vehiculo vehiculoMasCercano = null;
        boolean esPrimero = true;

        if (tipoVehiculo == VehiculoEnum.MOTOPEQUENA || tipoVehiculo == VehiculoEnum.MOTOGRANDE) {
            for (Vehiculo vehiculo : vehiculosEnMapa) {
                if (vehiculo.getVehiculoEnum() == tipoVehiculo && vehiculo.getDisponible(usuario)) {
                    double distancia = calcularDistancia(x, y, ((VehiculoDeCoordenadas) vehiculo).getCoordenadaX(), 
                    ((VehiculoDeCoordenadas) vehiculo).getCoordenadaY());
                    if (esPrimero || distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        vehiculoMasCercano = vehiculo;
                        esPrimero = false;
                    }
                }
            }
        } else if (tipoVehiculo == VehiculoEnum.BICICLETA || tipoVehiculo == VehiculoEnum.PATINETE) {
            for (Vehiculo vehiculo : vehiculosEnBases) {
                if (vehiculo.getVehiculoEnum() == tipoVehiculo && vehiculo.getDisponible(usuario)) {
                    double distancia = calcularDistancia(x, y, ((VehiculoDeBase) vehiculo).getBase().getCoordenadaX(), 
                    ((VehiculoDeBase) vehiculo).getBase().getCoordenadaY());
                    if (esPrimero || distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        vehiculoMasCercano = vehiculo;
                        esPrimero = false;
                    }
                }
            }
        }

        return vehiculoMasCercano;
    }

    // MÉTODOS AUXILIARES
    private double calcularDistancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    //=================================================
    // MÉTODOS DE VISUALIZACIÓN VEHÍCULOS
    //=================================================

    /**
     * Muestra un resumen y el detalle completo de todos los vehículos del sistema,
     * incluyendo estado de batería, averías, uso, reserva y ubicación.
     */
    public void mostrarDetallesVehiculos() {
        List<Vehiculo> vehiculosAlmacenados = getListaVehiculosAlmacenados();
        List<Vehiculo> vehiculosMapa = getVehiculosEnMapa();
        List<Vehiculo> vehiculosBases = getVehiculosEnBases();

        System.out.println("\n===============================");
        System.out.println("   RESUMEN DE VEHÍCULOS");
        System.out.println("===============================");
        System.out.printf("Total de vehículos: %d\n", getListaVehiculos().size());
        System.out.printf(" - En almacén: %d\n", vehiculosAlmacenados.size());
        System.out.printf(" - En mapa:    %d\n", vehiculosMapa.size());
        System.out.printf(" - En bases:   %d\n", vehiculosBases.size());

        System.out.println("\n-------------------------------------------------------------");
        System.out.println("DETALLE DE VEHÍCULOS");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-10s %-8s %-8s %-10s %-20s\n",
                "ID", "Tipo", "Batería", "Averías", "En Uso", "Reservado", "Ubicación");
        System.out.println("-------------------------------------------------------------------------------");

        // Unificar todos los vehículos sin duplicados
        List<Vehiculo> todosLosVehiculos = new ArrayList<>();
        for (Vehiculo v : getListaVehiculos()) {
            if (!todosLosVehiculos.contains(v)) {
                todosLosVehiculos.add(v);
            }
        }

        for (Vehiculo vehiculo : todosLosVehiculos) {
            String ubicacion;
            if (vehiculosAlmacenados.contains(vehiculo)) {
                ubicacion = "Almacén";
            } else if (vehiculosMapa.contains(vehiculo)) {
                if (vehiculo instanceof VehiculoDeCoordenadas vdc) {
                    ubicacion = "Mapa (" + vdc.getCoordenadaX() + "," + vdc.getCoordenadaY() + ")";
                } else {
                    ubicacion = "Mapa";
                }
            } else if (vehiculosBases.contains(vehiculo)) {
                if (vehiculo instanceof VehiculoDeBase vdb) {
                    Base base = vdb.getBase();
                    if (base != null) {
                        ubicacion = "Base: " + base.getNombre();
                    } else {
                        ubicacion = "Sin base";
                    }
                } else {
                    ubicacion = "Base";
                }
            } else {
                ubicacion = "Desconocida";
            }

            String averias = vehiculo.getAverias() ? "SÍ" : "NO";
            String enUso = vehiculo.getEnUso() ? "SÍ" : "NO";
            String reservado = vehiculo.getReservado() ? "SÍ" : "NO";

            System.out.printf("%-5d %-15s %-10s %-8s %-8s %-10s %-20s\n",
                    vehiculo.getID(),
                    vehiculo.getVehiculoEnum(),
                    vehiculo.getBateria() + "%",
                    averias,
                    enUso,
                    reservado,
                    ubicacion);
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Fin del listado.\n");
    }
    

    //=================================================
    // MÉTODOS ASIGNACION MANTENIMIENTO Y MECANICO
    //=================================================
    public boolean asignarMantenimiento(String DNI, int idVehiculo) {
        if (DNI == null || DNI.isEmpty()) {
            System.out.println("Error: DNI inválido");
            return false;
        }

        // Buscar el trabajador de mantenimiento por DNI
        GestorPersonas gestorPersonas = GestorPersonas.getInstancia();
        Object persona = gestorPersonas.getPersonaPorDNI(DNI);
        if (!(persona instanceof Mantenimiento)) {
            System.out.println("Error: El DNI no corresponde a un trabajador de mantenimiento.");
            return false;
        }
        Mantenimiento mantenimiento = (Mantenimiento) persona;

        Vehiculo vehiculo = getVehiculoPorID(idVehiculo);
        if (vehiculo == null) {
            System.out.println("Error: Vehículo no encontrado.");
            return false;
        }

        boolean asignado = false;

        if (vehiculo.getBateria() < 20) {
            vehiculo.setEnUso(false);
            vehiculo.setReservado(true);
            if (!mantenimiento.getVehiculosSinBateria().contains(vehiculo)) {
                mantenimiento.getVehiculosSinBateria().add(vehiculo);
            }
            System.out.println("Vehículo ID " + vehiculo.getID() + " asignado a mantenimiento por batería baja.");
            asignado = true;
        }
        if (vehiculo.getAverias()) {
            vehiculo.setEnUso(false);
            vehiculo.setReservado(true);
            if (!mantenimiento.getVehiculosAveriados().contains(vehiculo)) {
                mantenimiento.getVehiculosAveriados().add(vehiculo);
            }
            System.out.println("Vehículo ID " + vehiculo.getID() + " asignado a mantenimiento por avería.");
            asignado = true;
        }

        if (!asignado) {
            System.out.println("El vehículo no requiere mantenimiento (ni batería baja ni avería).");
            return false;
        }

        return true;
    }

    /**
     * Asigna un vehículo a mantenimiento solo si tiene batería baja (<20%).
     */
    public boolean asignarMantenimientoBateriaBaja(String DNI, int idVehiculo) {
        if (DNI == null || DNI.isEmpty()) {
            System.out.println("Error: DNI inválido");
            return false;
        }
        GestorPersonas gestorPersonas = GestorPersonas.getInstancia();
        Object persona = gestorPersonas.getPersonaPorDNI(DNI);
        if (!(persona instanceof Mantenimiento)) {
            System.out.println("Error: El DNI no corresponde a un trabajador de mantenimiento.");
            return false;
        }
        Mantenimiento mantenimiento = (Mantenimiento) persona;

        Vehiculo vehiculo = getVehiculoPorID(idVehiculo);
        if (vehiculo == null) {
            System.out.println("Error: Vehículo no encontrado.");
            return false;
        }

        if (vehiculo.getBateria() < 20) {
            vehiculo.setEnUso(false);
            vehiculo.setReservado(true);
            if (!mantenimiento.getVehiculosSinBateria().contains(vehiculo)) {
                mantenimiento.getVehiculosSinBateria().add(vehiculo);
            }
            System.out.println("Vehículo ID " + vehiculo.getID() + " asignado a mantenimiento por batería baja.");
            return true;
        } else {
            System.out.println("El vehículo no tiene batería baja.");
            return false;
        }
    }

    public boolean asignarMantenimientoYMecanicoAveria(String DNIMantenimiento, String DNIMecanico, int idVehiculo) {
        if (DNIMantenimiento == null || DNIMantenimiento.isEmpty() || DNIMecanico == null || DNIMecanico.isEmpty()) {
            System.out.println("Error: DNI inválido");
            return false;
        }
        GestorPersonas gestorPersonas = GestorPersonas.getInstancia();
        Object personaMantenimiento = gestorPersonas.getPersonaPorDNI(DNIMantenimiento);
        Object personaMecanico = gestorPersonas.getPersonaPorDNI(DNIMecanico);

        if (!(personaMantenimiento instanceof Mantenimiento)) {
            System.out.println("Error: El DNI no corresponde a un trabajador de mantenimiento.");
            return false;
        }
        if (!(personaMecanico instanceof Mecanico)) {
            System.out.println("Error: El DNI no corresponde a un mecánico.");
            return false;
        }
        Mantenimiento mantenimiento = (Mantenimiento) personaMantenimiento;
        Mecanico mecanico = (Mecanico) personaMecanico;

        Vehiculo vehiculo = getVehiculoPorID(idVehiculo);
        if (vehiculo == null) {
            System.out.println("Error: Vehículo no encontrado.");
            return false;
        }

        if (vehiculo.getAverias()) {
            vehiculo.setEnUso(false);
            vehiculo.setReservado(true);
            if (!mantenimiento.getVehiculosAveriados().contains(vehiculo)) {
                mantenimiento.getVehiculosAveriados().add(vehiculo);
            }
            if (!mecanico.getVehiculosAveriados().contains(vehiculo)) {
                mecanico.getVehiculosAveriados().add(vehiculo);
            }
            System.out.println("Vehículo ID " + vehiculo.getID() + " asignado a mantenimiento y mecánico por avería.");
            return true;
        } else {
            System.out.println("El vehículo no tiene avería.");
            return false;
        }
    }
    
    // Asignar mecánico a avería en base
    public boolean asignarMecanicoAveriaBase(String DNIMecanico, String nombreBase) {
        if (DNIMecanico == null || DNIMecanico.isEmpty() || nombreBase == null || nombreBase.isEmpty()) {
            System.out.println("Error: DNI o nombre de base inválido");
            return false;
        }
        GestorPersonas gestorPersonas = GestorPersonas.getInstancia();
        Object personaMecanico = gestorPersonas.getPersonaPorDNI(DNIMecanico);
        if (!(personaMecanico instanceof Mecanico)) {
            System.out.println("Error: El DNI no corresponde a un mecánico.");
            return false;
        }
        Mecanico mecanico = (Mecanico) personaMecanico;

        Base base = mapa.getBasePorNombre(nombreBase);
        if (base == null) {
            System.out.println("Error: No se encontró la base '" + nombreBase + "'");
            return false;
        }

        if (base.getAverias()) {
            if (!mecanico.getBasesAveriadas().contains(base)) {
                mecanico.addBaseAveriada(base);
                System.out.println("Base '" + nombreBase + "' averiada asignada al mecánico " + DNIMecanico);
                return true;
            } else {
                System.out.println("La base '" + nombreBase + "' ya está asignada al mecánico.");
                return false;
            }
        } else {
            System.out.println("La base '" + nombreBase + "' no está averiada.");
            return false;
        }
    }

}
