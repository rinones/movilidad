package GestorPersonas.Personas;

import Enum.RolEnum;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class Mantenimiento extends Trabajador {

    private List<Vehiculo> vehiculosSinBateria;
    private List<Vehiculo> vehiculosAveriados;
    private int intervencionesRealizadas; // Contador de intervenciones realizadas

    public Mantenimiento(String nombre, String apellidos, String DNI) {
        super(nombre, apellidos, DNI, RolEnum.MANTENIMIENTO);
        this.vehiculosSinBateria = new ArrayList<>();
        this.vehiculosAveriados = new ArrayList<>();
    }

    public List<Vehiculo> getVehiculosSinBateria() {
        return vehiculosSinBateria;
    }

    public List<Vehiculo> getVehiculosAveriados() {
        return vehiculosAveriados;
    }

    public void setVehiculosSinBateria(List<Vehiculo> vehiculosSinBateria) {
        this.vehiculosSinBateria = vehiculosSinBateria;
    }

    public void setVehiculosAveriados(List<Vehiculo> vehiculosAveriados) {
        this.vehiculosAveriados = vehiculosAveriados;
    }

    public void incrementarIntervenciones() {
        this.intervencionesRealizadas++;
    }

    public int getIntervencionesRealizadas() {
        return intervencionesRealizadas;
    }
}
