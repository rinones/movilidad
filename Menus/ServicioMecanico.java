package Menus;

import GestorPersonas.Personas.Mecanico;
import GestorVehiculosBases.Facturas.FacturaReparacionBase;
import GestorVehiculosBases.Facturas.FacturaReparacionVehiculo;
import GestorVehiculosBases.GestorBases;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import GestorViajes.Beneficios;
import Utilidades.UtilidadesEntradaDatos;
import java.time.LocalDate;
import java.util.List;

public class ServicioMecanico {

    private Mecanico mecanico;

    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
    }

    public void verVehiculosYBasesAsignados() {
        if (mecanico == null) {
            System.out.println("No hay usuario mecánico activo.");
            return;
        }

        GestorVehiculos gestorVehiculos = GestorVehiculos.getInstancia();
        List<Vehiculo> vehiculosEnAlmacen = gestorVehiculos.getListaVehiculosAlmacenados();

        // Vehículos averiados asignados al mecánico
        System.out.println("\n--- VEHÍCULOS AVERIADOS ASIGNADOS AL MECÁNICO ---");
        System.out.printf("%-10s %-15s %-20s %-10s\n", "ID", "Tipo", "Ubicación", "En almacén");
        for (Vehiculo v : mecanico.getVehiculosAveriados()) {
            String ubicacion;
            if (vehiculosEnAlmacen.contains(v)) {
                ubicacion = "Almacén";
            } else if (v instanceof VehiculoDeCoordenadas) {
                VehiculoDeCoordenadas vc = (VehiculoDeCoordenadas) v;
                ubicacion = "Mapa (" + vc.getCoordenadaX() + "," + vc.getCoordenadaY() + ")";
            } else if (v instanceof VehiculoDeBase) {
                VehiculoDeBase vb = (VehiculoDeBase) v;
                if (vb.getBase() != null) {
                    ubicacion = "Base: " + vb.getBase().getNombre();
                } else {
                    ubicacion = "Desconocida";
                }
            } else {
                ubicacion = "Desconocida";
            }

            String enAlmacen = vehiculosEnAlmacen.contains(v) ? "Sí" : "No";

            System.out.printf("%-10d %-15s %-20s %-10s\n",
                v.getID(),
                v.getNombreVehiculo(),
                ubicacion,
                enAlmacen
            );
        }

        // Bases averiadas asignadas al mecánico
        System.out.println("\n--- BASES AVERIADAS ASIGNADAS ---");
        System.out.printf("%-20s %-20s\n", "Nombre Base", "Ubicación");
        for (Base base : mecanico.getBasesAveriadas()) {
            String ubicacion = "X: " + base.getCoordenadaX() + ", Y: " + base.getCoordenadaY();
            System.out.printf("%-20s %-20s\n", base.getNombre(), ubicacion);
        }
    }

    public void realizarReparacionVehiculo() {
        if (mecanico == null) {
            System.out.println("No hay usuario mecánico activo.");
            return;
        }

        int IDVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID del vehículo a reparar", 1, Integer.MAX_VALUE);

        GestorVehiculos gestorVehiculos = GestorVehiculos.getInstancia();
        Vehiculo vehiculo = gestorVehiculos.getVehiculoPorID(IDVehiculo);

        if (vehiculo == null) {
            System.out.println("El vehículo no existe.");
            return;
        }
        if (!gestorVehiculos.getListaVehiculosAlmacenados().contains(vehiculo)) {
            System.out.println("El vehículo no está en el almacén.");
            return;
        }
        if (!mecanico.getVehiculosAveriados().contains(vehiculo)) {
            System.out.println("El vehículo no está asignado a este mecánico.");
            return;
        }

        vehiculo.setAverias(false);

        FacturaReparacionVehiculo reparacion = new FacturaReparacionVehiculo(vehiculo, LocalDate.now());
        double coste = reparacion.getCoste();

        mecanico.removeVehiculoAveriado(vehiculo);
        mecanico.incrementarIntervenciones();; // Incrementa por reparación de vehículo

        System.out.println("Vehículo reparado correctamente. Coste: " + coste + "€");
    }

    public void realizarReparacionBase() {
        if (mecanico == null) {
            System.out.println("No hay usuario mecánico activo.");
            return;
        }

        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base a reparar", 1, 50);
        Base base = Mapa.getInstancia().getBasePorNombre(nombreBase);

        if (base == null) {
            System.out.println("La base no existe.");
            return;
        }
        if (!base.getAverias()) {
            System.out.println("La base no está averiada.");
            return;
        }
        if (!mecanico.getBasesAveriadas().contains(base)) {
            System.out.println("La base no está asignada a este mecánico.");
            return;
        }

        base.setAverias(false);

        FacturaReparacionBase reparacion = new FacturaReparacionBase(base);
        double coste = reparacion.getCoste();

        mecanico.removeBaseAveriada(base);
        mecanico.incrementarIntervenciones(); // Incrementa por reparación de base

        System.out.println("Base reparada correctamente. Coste: " + coste + "€");
    }

    public void desactivarVehiculo() {
        System.out.println("Introduce el ID del vehículo a reservar:");
        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("ID vehículo", 1, Integer.MAX_VALUE);

        System.out.println("Introduce la duración del periodo de inactividad en minutos:");
        int minutos = UtilidadesEntradaDatos.getEnteroPositivo("Minutos", 1, 1440); // hasta 24h

        Beneficios beneficios = new Beneficios();
        if (beneficios.reservarVehiculo(idVehiculo, minutos)) {
            System.out.println("Vehículo reservado correctamente por " + minutos + " minutos.");
        } else {
            System.out.println("No se pudo reservar el vehículo.");
        }
    }

    public void desactivarBase() {
        System.out.println("Introduce el nombre de la base a desactivar:");
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Nombre de la base", 1, 50);

        System.out.println("Introduce la duración del periodo de inactividad en minutos:");
        int minutos = UtilidadesEntradaDatos.getEnteroPositivo("Minutos", 1, 1440); // hasta 24h

        GestorBases gestorBases = GestorBases.getInstancia();
        boolean resultado = gestorBases.desactivarBase(nombreBase, minutos);

        if (resultado) {
            System.out.println("Base '" + nombreBase + "' desactivada correctamente por " + minutos + " minutos.");
        } else {
            System.out.println("No se pudo desactivar la base (puede que no exista o ya esté desactivada).");
        }
    }

    public void mostrarHistorialReparaciones() {
        if (mecanico == null) {
            System.out.println("No hay usuario mecánico activo.");
            return;
        }

        // Tabla de reparaciones de vehículos
        List<FacturaReparacionVehiculo> reparacionesVehiculos = mecanico.getReparacionesVehiculosRealizadas();
        System.out.println("\n--- HISTORIAL DE REPARACIONES DE VEHÍCULOS ---");
        if (reparacionesVehiculos.isEmpty()) {
            System.out.println("No hay reparaciones de vehículos registradas.");
        } else {
            System.out.printf("%-10s %-15s %-10s\n", "ID", "Tipo", "Coste (€)");
            for (FacturaReparacionVehiculo rv : reparacionesVehiculos) {
                Vehiculo v = rv.getVehiculo();
                System.out.printf("%-10d %-15s %-10.2f\n", v.getID(), v.getNombreVehiculo(), rv.getCoste());
            }
        }

        // Tabla de reparaciones de bases
        List<FacturaReparacionBase> reparacionesBases = mecanico.getReparacionesBasesRealizadas();
        System.out.println("\n--- HISTORIAL DE REPARACIONES DE BASES ---");
        if (reparacionesBases.isEmpty()) {
            System.out.println("No hay reparaciones de bases registradas.");
        } else {
            System.out.printf("%-20s %-10s\n", "Nombre Base", "Coste (€)");
            for (FacturaReparacionBase rb : reparacionesBases) {
                Base b = rb.getBase();
                System.out.printf("%-20s %-10.2f\n", b.getNombre(), rb.getCoste());
            }
        }
    }
}