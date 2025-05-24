package GestorVehiculosBases.Localizaciones;

import GestorVehiculosBases.Vehiculos.MotoGrande;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapa {

    //=================================================
    // ATRIBUTOS
    //=================================================

    private static Mapa instancia;
    private String nombreMapa;
    private int maxCoordenadaX;
    private int maxCoordenadaY;
    private Object[][] mapaCiudadEnCoordenadas;
    
    private final Map<String, Base> mapaBasesPorNombre = new HashMap<>();
    private Map<Integer, Vehiculo> mapaMotosPorID = new HashMap<>();

    //=================================================
    // CONSTRUCTOR Y SINGLETON
    //=================================================
    private Mapa() {}

    public static Mapa getInstancia() {
        if (instancia == null) {
            instancia = new Mapa();
        }
        return instancia;
    }

    //=================================================
    // INICIALIZACIÓN Y CONFIGURACIÓN
    //=================================================

    /**
     * Método para inicializar el mapa con un nombre y dimensiones.
     * El mapa tendrá las coordenadas desde (0,0) hasta (maxX,maxY).
     */
    public boolean inicializarMapa(String nombreMapa, int maxCoordenadaX, int maxCoordenadaY) {
        if (maxCoordenadaX <= 0 || maxCoordenadaY <= 0) {
            System.out.println("Error: Límites inválidos.");
            return false;
        }
        this.nombreMapa = nombreMapa;
        this.maxCoordenadaX = maxCoordenadaX;
        this.maxCoordenadaY = maxCoordenadaY;
        this.mapaCiudadEnCoordenadas = new Object[maxCoordenadaX + 1][maxCoordenadaY + 1];
        
        System.out.println("Mapa '" + nombreMapa + "' inicializado con dimensiones (" + maxCoordenadaX + ", " + maxCoordenadaY + ").");
        return true;
    }

    //=================================================
    // GESTIÓN DE COORDENADAS Y VALIDACIÓN
    //=================================================

    public boolean sonCoordenadasValidas(int x, int y) {
        return x >= 0 && x <= maxCoordenadaX && y >= 0 && y <= maxCoordenadaY;
    }

    public boolean sonCoordenadasOcupadas(int x, int y) {
        return mapaCiudadEnCoordenadas[x][y] != null;
    }

    public Object getContenidoCelda(int x, int y) {
        if (sonCoordenadasValidas(x, y)) {
            return mapaCiudadEnCoordenadas[x][y];
        }
        return null;
    }

    public Object[][] getMapaCiudadEnCoordenadas() {
        return mapaCiudadEnCoordenadas;
    }


    //=================================================
    // GESTIÓN DE BASES
    //=================================================

    /*
    Método para agregar una base al mapa, indicando su nombre, coordenadas y capacidad
    Devuelve true si se ha añadido correctamente, false si hay un error
    */
    public boolean addBase(String nombreBase, int x, int y, int capacidad) {
        if (!sonCoordenadasValidas(x, y) || sonCoordenadasOcupadas(x, y)) {
            System.out.println("Error: Coordenada ocupada o fuera de límites.");
            return false;
        }
        
        Base base = new Base(nombreBase, x, y, capacidad);
        mapaCiudadEnCoordenadas[x][y] = base;
        mapaBasesPorNombre.put(nombreBase, base);
        
        return true;
    }

    /*
    Método para eliminar una base del mapa indicando sus coordenadas
    Devuelve true si se ha eliminado correctamente, false si hay un error
    */
    public boolean removeBase(int x, int y) {
        if (sonCoordenadasValidas(x, y) && mapaCiudadEnCoordenadas[x][y] instanceof Base) {
            Base base = (Base) mapaCiudadEnCoordenadas[x][y];
            mapaBasesPorNombre.remove(base.getNombre());
            mapaCiudadEnCoordenadas[x][y] = null;
            System.out.println("Base '" + base.getNombre() + "' eliminada.");
            return true;
        }
        System.out.println("Error: No se puede eliminar la base en las coordenadas (" + x + ", " + y + ").");
        return false;
    }

    public boolean removeBase(String nombreBase) {
        Base base = mapaBasesPorNombre.remove(nombreBase);
        if (base != null) {
            int x = base.getCoordenadaX();
            int y = base.getCoordenadaY();
            mapaCiudadEnCoordenadas[x][y] = null;
            System.out.println("Base '" + nombreBase + "' eliminada.");
            return true;
        }
        System.out.println("Error: No se puede eliminar la base con nombre '" + nombreBase + "'.");
        return false;
    }

    //=================================================
    // GETTERS BASES
    //=================================================

    public Base getBasePorNombre(String nombreBase) {
        return mapaBasesPorNombre.get(nombreBase);
    }

    public List<Base> getListaBases() {
        return new ArrayList<>(mapaBasesPorNombre.values());
    }

    public Map<String, Base> getMapaBasesPorNombre() {
        return mapaBasesPorNombre;
    }
    
        public List<Base> getListaBasesSinAverias() {
        List<Base> basesActivas = new ArrayList<>();
        for (Base base : getListaBases()) {
            if (!base.getAverias()) {
                basesActivas.add(base);
            }
        }
        return basesActivas;
    }

    public List<Base> getListaBasesConAverias() {
        List<Base> basesConAverias = new ArrayList<>();
        for (Base base : getListaBases()) {
            if (base.getAverias()) {
                basesConAverias.add(base);
            }
        }
        return basesConAverias;
    }

    //=================================================
    // GESTIÓN DE MOTOS
    //=================================================

    /*
    Método para registrar una moto en el mapa indicando sus coordenadas y el objeto VehiculoDeCoordenadas
    Vereficas si las coordenadas son válidas y si están ocupadas
    Devuelve true si se ha registrado correctamente, false si hay un error
    */
    public boolean addVehiculo(int x, int y, VehiculoDeCoordenadas vehiculo) {
        if (!sonCoordenadasValidas(x, y) || sonCoordenadasOcupadas(x, y)) {
            System.out.println("Error: Coordenada ocupada o fuera de límites.");
            return false;
        }
        if (vehiculo == null) {
            System.out.println("Error: Vehículo nulo.");
            return false;
        } else {
            mapaCiudadEnCoordenadas[x][y] = vehiculo;
            mapaMotosPorID.put(vehiculo.getID(), vehiculo);
            vehiculo.setCoordenadaX(x);
            vehiculo.setCoordenadaY(y);
            System.out.println("Moto registrada en (" + x + ", " + y + "). ID: " + vehiculo.getID());
            return true;
        }
        
    }

    // Eliminar una moto del mapa por sus coordenadas
    public boolean removeVehiculoPorCoordenadas(int x, int y) {
        if (sonCoordenadasValidas(x, y) && mapaCiudadEnCoordenadas[x][y] instanceof Vehiculo) {
            Vehiculo vehiculo = (Vehiculo) mapaCiudadEnCoordenadas[x][y];
            mapaMotosPorID.remove(vehiculo.getID());
            mapaCiudadEnCoordenadas[x][y] = null;
            System.out.println("Moto eliminada de (" + x + ", " + y + "). ID: " + vehiculo.getID());
            return true;
        }
        System.out.println("Error: No se puede eliminar la moto en las coordenadas (" + x + ", " + y + ").");
        return false;
    }

    public void removeVehiculoPorID(int ID) {
        Vehiculo vehiculo = mapaMotosPorID.remove(ID);
        if (vehiculo != null) {
            int x = ((VehiculoDeCoordenadas)vehiculo).getCoordenadaX();
            int y = ((VehiculoDeCoordenadas)vehiculo).getCoordenadaY();
            removeVehiculoPorCoordenadas(x, y);
            System.out.println("Vehículo con ID " + ID + " eliminado.");
        } else {
            System.out.println("Error: Vehículo con ID " + ID + " no encontrado.");
        }
    }


    //=================================================
    // GETTERS
    //=================================================
    public String getNombre() {
        return nombreMapa;
    }

    public int getMaxCoordenadaX() {
        return maxCoordenadaX;
    }

    public int getMaxCoordenadaY() {
        return maxCoordenadaY;
    }  

    //=================================================
    // VISUALIZACIÓN MAPA
    //================================================
    
    // Método para dibujar el mapa en consola con diferenciación de motos grandes y pequeñas
    public void dibujarMapa() {
        if (mapaCiudadEnCoordenadas == null) {
            System.out.println("El mapa no ha sido inicializado. Usa inicializarMapa() primero.");
            return;
        }
        System.out.println("\nMapa: " + nombreMapa + " (" + maxCoordenadaX + "x" + maxCoordenadaY + ")");
        
        System.out.print("   ");
        for (int x = 0; x <= maxCoordenadaX; x++) {
            System.out.print(x % 10);
        }
        System.out.println();
        
        System.out.print("  +");
        for (int x = 0; x <= maxCoordenadaX; x++) {
            System.out.print("-");
        }
        System.out.println("+");
        
        for (int y = maxCoordenadaY; y >= 0; y--) {  
            System.out.printf("%2d|", y);
            for (int x = 0; x <= maxCoordenadaX; x++) {
                if (mapaCiudadEnCoordenadas[x][y] == null) {
                    System.out.print(" ");
                } else if (mapaCiudadEnCoordenadas[x][y] instanceof Base) {
                    System.out.print("B"); 
                } else if (mapaCiudadEnCoordenadas[x][y] instanceof Vehiculo) {
                    Vehiculo moto = (Vehiculo) mapaCiudadEnCoordenadas[x][y];
                    if (mapaCiudadEnCoordenadas[x][y] instanceof MotoGrande) {
                        System.out.print("M");
                    } else {
                        System.out.print("m");
                    }
                } else {
                    System.out.print("?");
                }
            }
            System.out.println("|");
        }
        
        System.out.print("  +");
        for (int x = 0; x <= maxCoordenadaX; x++) {
            System.out.print("-");
        }
        System.out.println("+");
        
        // Añadir leyenda
        System.out.println("\nLeyenda:");
        System.out.println("B = Base");
        System.out.println("M = Moto Grande");
        System.out.println("m = Moto Pequeña");
        System.out.println("? = Otro objeto");
    } 
}

