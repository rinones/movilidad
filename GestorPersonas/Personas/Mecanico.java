package GestorPersonas.Personas;
import Enum.RolEnum;
import GestorVehiculosBases.Facturas.FacturaReparacionBase;
import GestorVehiculosBases.Facturas.FacturaReparacionVehiculo;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class Mecanico extends Trabajador {
    private List<Vehiculo> vehiculosAveriados; // Lista de vehículos asignados al mecánico
    private List<Base> basesAveriadas; // Lista de bases asignadas al mecánico
    private List<FacturaReparacionVehiculo> reparacionesVehiculos;
    private List<FacturaReparacionBase> reparacionesBases;
    private int intervencionesRealizadas; // Contador de intervenciones realizadas

    public Mecanico(String nombre, String apellidos, String DNI) {
        super(nombre, apellidos, DNI, RolEnum.MECANICO);
        this.vehiculosAveriados = new ArrayList<>();
        this.basesAveriadas = new ArrayList<>();
        this.reparacionesVehiculos = new ArrayList<>();
        this.reparacionesBases = new ArrayList<>();
        this.intervencionesRealizadas = 0;
    }

    public List<Vehiculo> getVehiculosAveriados() {
        return vehiculosAveriados;
    }
    public void setVehiculosAveriados(List<Vehiculo> vehiculosAveriados) {
        this.vehiculosAveriados = vehiculosAveriados;
    }
    public List<Base> getBasesAveriadas() {
        return basesAveriadas;
    }
    public void setBasesAveriadas(List<Base> basesAveriadas) {
        this.basesAveriadas = basesAveriadas;
    }
    public void addVehiculoAveriado(Vehiculo vehiculo) {
        this.vehiculosAveriados.add(vehiculo);
    }
    public void removeVehiculoAveriado(Vehiculo vehiculo) {
        this.vehiculosAveriados.remove(vehiculo);
    }
    public void addBaseAveriada(Base base) {
        this.basesAveriadas.add(base);
    }
    public void removeBaseAveriada(Base base) {
        this.basesAveriadas.remove(base);
    }

    public List<FacturaReparacionVehiculo> getReparacionesVehiculosRealizadas() {
        if (reparacionesVehiculos == null) {
            reparacionesVehiculos = new ArrayList<>();
        }
        return reparacionesVehiculos;
    }

    public void addReparacionVehiculo(FacturaReparacionVehiculo reparacion) {
        getReparacionesVehiculosRealizadas().add(reparacion);
    }

    public List<FacturaReparacionBase> getReparacionesBasesRealizadas() {
        if (reparacionesBases == null) {
            reparacionesBases = new ArrayList<>();
        }
        return reparacionesBases;
    }

    public void addReparacionBase(FacturaReparacionBase reparacion) {
        getReparacionesBasesRealizadas().add(reparacion);
    }

    public void incrementarIntervenciones() {
        this.intervencionesRealizadas++;
    }

    public int getIntervencionesRealizadas() {
        return intervencionesRealizadas;
    }
}
