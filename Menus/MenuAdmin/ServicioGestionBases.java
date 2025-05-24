package Menus.MenuAdmin;

import GestorVehiculosBases.GestorBases;
import GestorVehiculosBases.Localizaciones.Mapa;

public class ServicioGestionBases {
    private final GestorBases gestorBases;
    private final Mapa mapa;

    public ServicioGestionBases() {
        this.gestorBases = GestorBases.getInstancia();
        this.mapa = Mapa.getInstancia();
    }

    public boolean existeBasePorNombre(String nombreBase) {
        return mapa.getBasePorNombre(nombreBase) != null;
    }

    public boolean coordenadasValidasYLibres(int coordX, int coordY) {
        return mapa.sonCoordenadasValidas(coordX, coordY) && !mapa.sonCoordenadasOcupadas(coordX, coordY);
    }

    public boolean altaBasePorCoordenadas(String nombreBase, int coordX, int coordY, int capacidad) {
        return gestorBases.addBasePorCoordenadas(nombreBase, coordX, coordY, capacidad);
    }

    public boolean existeBaseEnCoordenadas(int coordX, int coordY) {
        return mapa.getContenidoCelda(coordX, coordY) != null;
    }

    public boolean bajaBasePorCoordenadas(int coordX, int coordY) {
        return gestorBases.removeBasePorCoordenadas(coordX, coordY);
    }

    public boolean bajaBasePorNombre(String nombreBase) {
        return gestorBases.removeBasePorNombre(nombreBase);
    }

    public void dibujarMapa() {
        mapa.dibujarMapa();
    }

    public int getMaxCoordenadaX() {
        return mapa.getMaxCoordenadaX();
    }

    public int getMaxCoordenadaY() {
        return mapa.getMaxCoordenadaY();
    }
}