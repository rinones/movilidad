package GestorVehiculosBases.Facturas;

import Enum.VehiculoEnum;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import java.time.LocalDate;

public class FacturaReparacionVehiculo {

    private Vehiculo vehiculo;
    private int coste;
    private LocalDate fecha; // NUEVO CAMPO

    public FacturaReparacionVehiculo(Vehiculo vehiculo, LocalDate fecha) {
        this.vehiculo = vehiculo;
        this.fecha = fecha; // ASIGNAR FECHA
        VehiculoEnum tipo = vehiculo.getVehiculoEnum();
        switch (tipo) {
            case MOTOGRANDE:
                this.coste = 85;
                break;
            case MOTOPEQUENA:
                this.coste = 70;
                break;
            case PATINETE:
                this.coste = 50;
                break;
            case BICICLETA:
                this.coste = 35;
                break;
            default:
                this.coste = 0;
        }
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public int getCoste() {
        return coste;
    }

    public LocalDate getFecha() { // GETTER PARA LA FECHA
        return fecha;
    }
}
