package Menus.MenuAdmin;

import Enum.VehiculoEnum;
import GestorVehiculosBases.GestorBases;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.MotoGrande;
import GestorVehiculosBases.Vehiculos.MotoPequena;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import java.util.List;

public class ServicioGestionVehiculos {

    private final GestorVehiculos gestorVehiculos;
    private final GestorBases gestorBases;
    private final Mapa mapa;

    public ServicioGestionVehiculos() {
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.gestorBases = GestorBases.getInstancia();
        this.mapa = Mapa.getInstancia();
    }

    public Vehiculo altaVehiculoMapa(VehiculoEnum tipoMoto, int x, int y) {
        return gestorVehiculos.addVehiculoMapa(tipoMoto, x, y);
    }

    public Vehiculo altaVehiculoBase(VehiculoEnum tipoVehiculo, String nombreBase) {
        Base base = mapa.getBasePorNombre(nombreBase);
        if (base == null) return null;
        return gestorVehiculos.addVehiculoBase(tipoVehiculo, base);
    }

    public Vehiculo altaVehiculoAlmacen(VehiculoEnum tipoVehiculo) {
        return gestorVehiculos.addVehiculoAlmacen(tipoVehiculo);
    }

    public boolean bajaVehiculo(int idVehiculo) {
        return gestorVehiculos.desecharVehiculoPorID(idVehiculo);
    }

    public List<Base> getBases() {
        return mapa.getListaBases();
    }

    public List<Vehiculo> getMotosEnMapa() {
        return gestorVehiculos.getVehiculosEnMapa();
    }

    public List<Vehiculo> getVehiculosEnBases() {
        return gestorVehiculos.getVehiculosEnBases();
    }

    public List<Vehiculo> getVehiculosAlmacenados() {
        return gestorVehiculos.getListaVehiculosAlmacenados();
    }

    public Vehiculo getVehiculoPorID(int idVehiculo) {
        return gestorVehiculos.getVehiculoPorID(idVehiculo);
    }

    public boolean moverVehiculoACoordenadas(int idVehiculo, int x, int y) {
        return gestorVehiculos.moverVehiculoACoordenadas(idVehiculo, x, y);
    }

    public boolean moverVehiculoABase(int idVehiculo, String nombreBase) {
        return gestorVehiculos.moverVehiculoABase(idVehiculo, nombreBase);
    }

    public int getMaxCoordenadaX() {
        return mapa.getMaxCoordenadaX();
    }

    public int getMaxCoordenadaY() {
        return mapa.getMaxCoordenadaY();
    }

    public void dibujarMapa() {
        mapa.dibujarMapa();
    }
}