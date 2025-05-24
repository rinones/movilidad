package GestorViajes;

import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Viaje {

    //==========================
    // ATRIBUTOS PRINCIPALES
    //==========================
    private Vehiculo vehiculo;
    private Usuario usuario;
    private long tiempoInicio;
    private long tiempoFin;
    private double costo;
    private boolean activo;

    //==========================
    // ATRIBUTOS DE LOCALIZACIÓN
    //==========================
    private int coordenadaXInicio;
    private int coordenadaYInicio;
    private int coordenadaXFin;
    private int coordenadaYFin;
    private Base baseInicio;
    private Base baseFin;

    //==========================
    // DEPENDENCIAS INTERNAS
    //==========================
    private GestorViajes gestorViajes;

     //==========================
    // CONSTRUCTORES
    //==========================
    // Viaje en tiempo real con coordenadas (motos)
    public Viaje(Vehiculo vehiculo, Usuario usuario, int coordX, int coordY) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.costo = 0;
        this.activo = true;
        this.tiempoInicio = System.currentTimeMillis();
        if (vehiculo.getVehiculoEnum() == VehiculoEnum.MOTOPEQUENA ||
            vehiculo.getVehiculoEnum() == VehiculoEnum.MOTOGRANDE) {
            this.coordenadaXInicio = coordX;
            this.coordenadaYInicio = coordY;
        }
    }
    // Viaje en tiempo real con base (bicicletas/patinetes)
    public Viaje(Vehiculo vehiculo, Usuario usuario, Base base) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.costo = 0;
        this.activo = true;
        this.tiempoInicio = System.currentTimeMillis();
        if (vehiculo.getVehiculoEnum() == VehiculoEnum.BICICLETA ||
            vehiculo.getVehiculoEnum() == VehiculoEnum.PATINETE) {
            this.baseInicio = base;
        }
    }

    //==========================
    // MÉTODOS DE CÁLCULO
    //==========================
    // Calcula la duración en horas
    public double calcularDuracion() {
        if (tiempoFin == 0) {
            System.out.println("Error: El viaje aún está activo. No se puede calcular la duración.");
            return -1;
        }
        return (double) (tiempoFin - tiempoInicio) / 3600000.0;
    }
    
    // Calcula el costo del viaje
    public double calcularCosto(Usuario usuario) {
        // Si el viaje está activo, no se puede calcular el costo
        if (activo) {
            System.out.println("Error: El viaje aún está activo. No se puede calcular el costo.");
            return -1;
        }

        // Calcula la duración en horas
        double duracionHoras = calcularDuracion();
        if (duracionHoras < 0) return -1;

        // Calcula el costo base según la tarifa del vehículo
        VehiculoEnum tipoVehiculo = vehiculo.getVehiculoEnum();
        double tarifaBase = tipoVehiculo.getTarifa();
        double costoBase = duracionHoras * tarifaBase;

        // Aplica descuento si el usuario es Premium
        if (usuario instanceof GestorPersonas.Personas.Premium) {
            double porcentajeDescuento = tipoVehiculo.getDescuento();
            double descuento = costoBase * (porcentajeDescuento / 100.0);
            double costoConDescuento = costoBase - descuento;
            System.out.println("Se aplicó un descuento del " + porcentajeDescuento +
                               "% por ser usuario Premium. Ahorro: " + String.format("%.2f", descuento) + "€");
            return costoConDescuento;
        }

        // Retorna el costo base si no hay descuento
        return costoBase;
    }

    // Calcula y descuenta el consumo de batería al vehículo según la duración del viaje
    public void actualizarBateriaVehiculo() {
        double duracionMinutos = ((double) (tiempoFin - tiempoInicio)) / 60000.0;
        double consumoPorMinuto = vehiculo.getVehiculoEnum().getRitmoConsumo();
        double consumoTotal = duracionMinutos * consumoPorMinuto;
        double bateriaActual = vehiculo.getBateria();
        double nuevaBateria = bateriaActual - consumoTotal;
        if (nuevaBateria < 0) nuevaBateria = 0;
        vehiculo.setBateria(nuevaBateria);
    }

    //==========================
    // GETTERS
    //==========================
    // Devuelve el vehículo asociado
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    // Devuelve el usuario asociado
    public Usuario getUsuario() {
        return usuario;
    }
    // Devuelve el tiempo de inicio
    public long getTiempoInicio() {
        return tiempoInicio;
    }
    // Devuelve el tiempo de fin
    public long getTiempoFin() {
        return tiempoFin;
    }
    // Devuelve el costo
    public double getCosto() {
        return costo;
    }
    // Devuelve la coordenada X de inicio
    public int getCoordenadaXInicio() {
        return coordenadaXInicio;
    }
    // Devuelve la coordenada Y de inicio
    public int getCoordenadaYInicio() {
        return coordenadaYInicio;
    }
    // Devuelve la coordenada X de fin
    public int getCoordenadaXFin() {
        return coordenadaXFin;
    }
    // Devuelve la coordenada Y de fin
    public int getCoordenadaYFin() {
        return coordenadaYFin;
    }
    // Devuelve la base de inicio
    public Base getBaseInicio() {
        return baseInicio;
    }
    // Devuelve la base de fin
    public Base getBaseFin() {
        return baseFin;
    }
    // Indica si el viaje está activo
    public boolean estaActivo() {
        return activo;
    }

    //==========================
    // SETTERS
    //==========================
    // Cambia el estado del viaje
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    // Establece el tiempo de fin
    public void setTiempoFin(long tiempoFin) {
        this.tiempoFin = tiempoFin;
    }
    // Establece el costo
    public void setCosto(double costo) {
        this.costo = costo;
    }
    // Establece coordenadas finales (motos)
    public void setCoordenadaFin(int x, int y) {
        this.coordenadaXFin = x;
        this.coordenadaYFin = y;
    }
    // Establece base final (bicicletas/patinetes)
    public void setBaseFin(Base base) {
        this.baseFin = base;
    }
  
    //============================
    // CONSTRUCTORES PARA PRUEBAS
    //============================
     // Constructor para pruebas: crea un viaje de moto con fechas y coordenadas.
     // Si fechaFin es null, el viaje queda activo.
    public Viaje(Vehiculo vehiculo, Usuario usuario,
                 LocalDateTime fechaInicio, LocalDateTime fechaFin,
                 int xInicio, int yInicio, int xFin, int yFin) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        // Convierte la fecha de inicio a milisegundos
        this.tiempoInicio = fechaInicio.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        VehiculoEnum tipoVehiculo = vehiculo.getVehiculoEnum();
        // Solo asigna coordenadas si es moto
        if (tipoVehiculo == VehiculoEnum.MOTOPEQUENA || tipoVehiculo == VehiculoEnum.MOTOGRANDE) {
            this.coordenadaXInicio = xInicio;
            this.coordenadaYInicio = yInicio;
            if (fechaFin != null) {
                this.coordenadaXFin = xFin;
                this.coordenadaYFin = yFin;
            }
        }

        if (fechaFin != null) {
            // Si hay fecha de fin, el viaje está terminado
            this.tiempoFin = fechaFin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            this.activo = false;
            this.costo = this.calcularCosto(usuario);
        } else {
            // Si no hay fecha de fin, el viaje está activo
            this.tiempoFin = 0;
            this.activo = true;
            this.costo = 0;
        }
        // Registra el viaje en el usuario
        usuario.registrarViaje(this);
    }

    // Constructor para pruebas: crea un viaje de bicicleta o patinete con fechas y bases.
    // Si fechaFin es null, el viaje queda activo.
    public Viaje(Vehiculo vehiculo, Usuario usuario,
                 LocalDateTime fechaInicio, LocalDateTime fechaFin,
                 Base baseInicio, Base baseFin) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        // Convierte la fecha de inicio a milisegundos
        this.tiempoInicio = fechaInicio.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        VehiculoEnum tipoVehiculo = vehiculo.getVehiculoEnum();
        // Solo asigna bases si es bicicleta o patinete
        if (tipoVehiculo == VehiculoEnum.BICICLETA || tipoVehiculo == VehiculoEnum.PATINETE) {
            this.baseInicio = baseInicio;
            if (fechaFin != null) {
                this.baseFin = baseFin;
            }
        }

        if (fechaFin != null) {
            // Si hay fecha de fin, el viaje está terminado
            this.tiempoFin = fechaFin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            this.activo = false;
            this.costo = this.calcularCosto(usuario);
        } else {
            // Si no hay fecha de fin, el viaje está activo
            this.tiempoFin = 0;
            this.activo = true;
            this.costo = 0;
        }
        // Registra el viaje en el usuario
        usuario.registrarViaje(this);
    }
}
