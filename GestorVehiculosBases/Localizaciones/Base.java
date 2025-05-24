package GestorVehiculosBases.Localizaciones;

import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Base {

    //=================================================
    // ATRIBUTOS
    //=================================================
    private String nombre;
    private int coordenadaX;
    private int coordenadaY;
    private int capacidad;
    private boolean averias;
    private int viajesIniciados = 0;
    private int viajesFinalizados = 0;
    private List<Vehiculo> vehiculos = new ArrayList<>();
    private Map<Integer, Vehiculo> mapaVehiculosPorID = new HashMap<>();

    //=================================================
    // CONSTRUCTOR
    //=================================================
    public Base(String nombre, int coordenadaX, int coordenadaY, int capacidad) {
        this.nombre = nombre;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.capacidad = capacidad;
        this.vehiculos = new ArrayList<>();
        this.mapaVehiculosPorID = new HashMap<>();
    }

    //=================================================
    // MÉTODOS PARA AÑADIR/ELIMINAR VEHÍCULOS
    //=================================================

    /*
    Método para añadir un vehículo a la base.
    Verificas si el vehiculo es nulo y si hay espacio libre en la base.
    Si hay espacio, se añade el vehículo a la lista de vehículos de la base.
    Si el vehículo es nulo, se imprime un mensaje de error y se devuelve false.
    Solo puede ser llamado desde este paquete.
    */

    public boolean addVehiculo(Vehiculo vehiculo, int ID) {
        if (vehiculo == null) {
            System.out.println("El vehiculo no puede ser nulo");
            return false;
        }
        if (vehiculo instanceof VehiculoDeCoordenadas) {
            System.out.println("El vehiculo no puede ser una moto");
            return false;
        }
        if (ID <= 0) {
            System.out.println("El ID del vehículo no puede ser menor o igual a cero");
            return false;
        }
        if (getEspaciosLibres() > 0) {
            mapaVehiculosPorID.put(ID, vehiculo);
            vehiculos.add(vehiculo);
                return true; 
            } else {
                System.out.println("No hay espacio libre en la base");
                return false;
            }
    }

    /*
    Método para eliminar un vehículo de la base.
    Verificas si el ID del vehículo es válido y si existe en la lista de vehículos de la base.
    Si existe, se elimina el vehículo de la lista de vehículos de la base.
    Si no existe, se imprime un mensaje de error y se devuelve false.
     */
     public boolean removeVehiculoPorID(int ID) {   
        if (ID <= 0) {
            System.out.println("El ID del vehículo no puede ser menor o igual a cero");
            return false;
        }
        if (mapaVehiculosPorID.containsKey(ID)) {
            Vehiculo vehiculo = mapaVehiculosPorID.get(ID);
            vehiculos.remove(vehiculo);
            mapaVehiculosPorID.remove(ID);
            return true;
        } else {
            System.out.println("El vehículo con ID " + ID + " no existe en la base");
            return false;
        }
    }

    //=================================================
    // GETTER ASIGNAR VEHÍCULO
    //=================================================
    // Regresa un vehiculo de la base del tipo VehiculoEnum que no tenga averias y que tenga bateria suficiente
    public Vehiculo getVehiculoActivoPorTipo(VehiculoEnum vehiculoEnum, Usuario usuario) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getVehiculoEnum() == vehiculoEnum && vehiculo.getDisponible(usuario)) {
                return vehiculo;
            }
        }
        return null;
    }

    //=================================================
    // GETTERS
    //=================================================
    public String getNombre() {
        return nombre;
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean getAverias() {
        return averias;
    }

    public int getEspaciosLibres() {
        return capacidad - vehiculos.size();
    }

    public int getEspaciosOcupados() {
        return vehiculos.size();
    }

    public List<Vehiculo> getListaVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public int getViajesIniciadosEnBase() {
        return viajesIniciados;
    }

    public int getViajesFinalizadosEnBase() {
        return viajesFinalizados;
    }

    public int getPatinetesDisponibles(Usuario usuario) {
        return getListaPatinetesDisponibles(usuario).size();
    }

    public int getBicicletasDisponibles(Usuario usuario) {
        return getListaBicicletasDisponibles(usuario).size();
    }

    public int getVehiculosDisponibles(Usuario usuario) {
        return getPatinetesDisponibles(usuario) + getBicicletasDisponibles(usuario);
    }

    public int getTotalPatinetes() {
        int contador = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getVehiculoEnum() == VehiculoEnum.PATINETE) {
                contador++;
            }
        }
        return contador;    
    }

    public int getTotalBicicletas() {
        int contador = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getVehiculoEnum() == VehiculoEnum.BICICLETA) {
                contador++;
            }
        }
        return contador;    
    }
    
    //=================================================
    // GETTERS PARA LISTAS DE VEHÍCULOS
    //=================================================
    public List<Vehiculo> getListaVehiculosDisponibles(Usuario usuario) {
        List<Vehiculo> vehiculosActivos = new ArrayList<>();
        vehiculosActivos.addAll(getListaPatinetesDisponibles(usuario));
        vehiculosActivos.addAll(getListaBicicletasDisponibles(usuario));
        return vehiculosActivos;
    }

    private List<Vehiculo> getListaVehiculosActivosPorTipo(VehiculoEnum tipo, Usuario usuario) {
        List<Vehiculo> vehiculosActivos = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getVehiculoEnum() == tipo && vehiculo.getDisponible(usuario)) {
                vehiculosActivos.add(vehiculo);
            }
        }
        return vehiculosActivos;
    }

    public List<Vehiculo> getListaPatinetesDisponibles(Usuario usuario) {
        return getListaVehiculosActivosPorTipo(VehiculoEnum.PATINETE, usuario);
    }

    public List<Vehiculo> getListaBicicletasDisponibles(Usuario usuario) {
        return getListaVehiculosActivosPorTipo(VehiculoEnum.BICICLETA, usuario);
    }

    //=================================================
    // SETTERS
    //=================================================
    public void setAverias(boolean averias) {
        this.averias = averias;
    }

    public void incrementarViajesIniciadosBase() {
        this.viajesIniciados++;
    }

    public void incrementarViajesFinalizadosBase() {
        this.viajesFinalizados++;
    }
}
