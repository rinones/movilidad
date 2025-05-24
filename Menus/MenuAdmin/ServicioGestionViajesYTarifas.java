package Menus.MenuAdmin;

import Enum.VehiculoEnum;
import GestorViajes.Beneficios;
import GestorViajes.GestorViajes;
import GestorViajes.TarifasYDescuentos;

public class ServicioGestionViajesYTarifas {

    private final GestorViajes gestorViajes;
    private final TarifasYDescuentos tarifasYDescuentos;
    private final Beneficios beneficios;

    public ServicioGestionViajesYTarifas() {
        this.gestorViajes = GestorViajes.getInstancia();
        this.tarifasYDescuentos = new TarifasYDescuentos();
        this.beneficios = new Beneficios();
    }

    public double getTarifa(VehiculoEnum tipo) {
        return tipo.getTarifa();
    }

    public boolean modificarTarifa(VehiculoEnum tipo, double nuevaTarifa) {
        return tarifasYDescuentos.modificarTarifa(tipo, nuevaTarifa);
    }

    public double getDescuento(VehiculoEnum tipo) {
        return tipo.getDescuento();
    }

    public boolean modificarDescuento(VehiculoEnum tipo, double nuevoDescuento) {
        return tarifasYDescuentos.modificarDescuento(tipo, nuevoDescuento);
    }

    public boolean getReservasPermitidas() {
        return beneficios.getReservasPermitidas();
    }

    public void setReservasPermitidas(boolean permitido) {
        Beneficios.setReservasPermitidas(permitido);
    }

    public void setBateriaPremiumPermitida(boolean permitido) {
        Beneficios.setBateriaPremiumPermitida(permitido);
    }
}