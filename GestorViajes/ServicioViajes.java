package GestorViajes;

import Enum.RolEnum;
import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import java.util.Date;

public class ServicioViajes {

    //==========================
    // SINGLETON Y DEPENDENCIAS
    //==========================
    private static ServicioViajes instancia;
    private GestorViajes gestorViajes;
    private GestorVehiculos gestorVehiculos;

    //==========================
    // CONSTRUCTOR
    //==========================
    private ServicioViajes() {
        this.gestorViajes = GestorViajes.getInstancia();
        this.gestorVehiculos = GestorVehiculos.getInstancia();
    }

    // Devuelve la instancia singleton
    public static ServicioViajes getInstancia() {
        if (instancia == null) {
            instancia = new ServicioViajes();
        }
        return instancia;
    }

    //==========================
    // INICIO DE VIAJES
    //==========================

    // Inicia viaje con bicicleta o patinete desde una base
    public Viaje iniciarViaje(Usuario usuario, VehiculoEnum vehiculoEnum, String nombreBase) {
        // Verificación de argumentos nulos
        if (usuario == null) {
            System.out.println("Error: El usuario no puede ser nulo.");
            return null;
        }
        if (vehiculoEnum == null) {
            System.out.println("Error: El tipo de vehículo no puede ser nulo.");
            return null;
        }
        if (nombreBase == null || nombreBase.trim().isEmpty()) {
            System.out.println("Error: El nombre de la base no puede ser nulo o vacío.");
            return null;
        }

        // Verificación de saldo
        if (usuario.getSaldo() <= 0) {
            System.out.println("Error: El usuario no tiene saldo positivo.");
            return null;
        }

        // Verificación de tipo de vehículo permitido
        if (vehiculoEnum != VehiculoEnum.BICICLETA && vehiculoEnum != VehiculoEnum.PATINETE) {
            System.out.println("Error: Solo se pueden iniciar viajes de bicicleta o patinete desde una base.");
            return null;
        }

        // Verificación de existencia de la base
        Mapa mapa = Mapa.getInstancia();
        Base base = mapa.getBasePorNombre(nombreBase);
        if (base == null) {
            System.out.println("Error: La base especificada no existe.");
            return null;
        }

        // Verificación de disponibilidad de vehículo
        boolean disponible = false;
        if (vehiculoEnum == VehiculoEnum.BICICLETA) {
            disponible = base.getBicicletasDisponibles(usuario) > 0;
        } else if (vehiculoEnum == VehiculoEnum.PATINETE) {
            disponible = base.getPatinetesDisponibles(usuario) > 0;
        }
        if (!disponible) {
            System.out.println("Error: No hay vehículos disponibles de tipo " + vehiculoEnum + " en la base.");
            return null;
        }

        // Obtener vehículo activo
        Vehiculo vehiculo = base.getVehiculoActivoPorTipo(vehiculoEnum, usuario);
        if (vehiculo == null) {
            System.out.println("Error: No se pudo obtener un vehículo disponible.");
            return null;
        }

        // Registrar viaje
        Viaje nuevoViaje = new Viaje(vehiculo, usuario, base);
        vehiculo.setEnUso(true);
        usuario.registrarViaje(nuevoViaje);
        gestorViajes.registrarViaje(nuevoViaje);
        gestorVehiculos.removeVehiculoBasePorID(vehiculo.getID());
        base.incrementarViajesIniciadosBase();
        ((VehiculoDeBase) vehiculo).setBase(null);

        System.out.println("Vehículo alquilado con éxito. ¡Buen viaje!");
        return nuevoViaje;
    }

    // Inicia viaje con moto en coordenadas específicas
    public Viaje iniciarViajeMoto(Usuario usuario, VehiculoEnum vehiculoEnum, int coordenadaX, int coordenadaY) {
        // Verificación de argumentos nulos
        if (usuario == null) {
            System.out.println("Error: El usuario no puede ser nulo.");
            return null;
        }
        if (vehiculoEnum == null) {
            System.out.println("Error: El tipo de vehículo no puede ser nulo.");
            return null;
        }

        // Verificación de saldo
        if (usuario.getSaldo() <= 0) {
            System.out.println("Error: El usuario no tiene saldo positivo.");
            return null;
        }

        // Verificación de tipo de vehículo permitido
        if (vehiculoEnum != VehiculoEnum.MOTOPEQUENA && vehiculoEnum != VehiculoEnum.MOTOGRANDE) {
            System.out.println("Error: Solo se pueden iniciar viajes de motos con este método.");
            return null;
        }

        // Verificación de coordenadas válidas
        Mapa mapa = Mapa.getInstancia();
        if (!mapa.sonCoordenadasValidas(coordenadaX, coordenadaY)) {
            System.out.println("Error: Las coordenadas están fuera de los límites del mapa.");
            return null;
        }

        // Verificación de existencia de vehículo en la celda
        Object contenidoCelda = mapa.getContenidoCelda(coordenadaX, coordenadaY);
        if (!(contenidoCelda instanceof Vehiculo)) {
            System.out.println("Error: No hay un vehículo en esa posición.");
            return null;
        }
        Vehiculo vehiculo = (Vehiculo) contenidoCelda;

        // Verificación de tipo de vehículo en la celda
        if (vehiculo.getVehiculoEnum() != vehiculoEnum) {
            System.out.println("Error: El vehículo en esa posición no es del tipo solicitado.");
            return null;
        }

        // Verificación de estado del vehículo
        if (!vehiculo.getDisponible(usuario)) {
            System.out.println("Error: El vehículo no está disponible (en uso o averiado).");
            return null;
        }

        // Registrar viaje
        Viaje nuevoViaje = new Viaje(vehiculo, usuario, coordenadaX, coordenadaY);
        vehiculo.setEnUso(true);
        usuario.registrarViaje(nuevoViaje);
        gestorViajes.registrarViaje(nuevoViaje);
        gestorVehiculos.removeVehiculoMapaPorCoordenadas(coordenadaX, coordenadaY);
        ((VehiculoDeCoordenadas) vehiculo).setCoordenadaX(-1);
        ((VehiculoDeCoordenadas) vehiculo).setCoordenadaY(-1);

        System.out.println("Moto alquilada con éxito. ¡Buen viaje!");
        return nuevoViaje;
    }

    //==========================
    // FINALIZACIÓN DE VIAJES
    //==========================

    // Finaliza viaje de bicicleta o patinete en una base
    public boolean finalizarViajeBase(Usuario usuario, Viaje viaje, String nombreBase) {
        // Validaciones de argumentos
        if (!validarFinalizacionViaje(usuario, viaje)) {
            return false;
        }
        if (nombreBase == null || nombreBase.trim().isEmpty()) {
            System.out.println("Error: El nombre de la base no puede ser nulo o vacío.");
            return false;
        }

        Mapa mapa = Mapa.getInstancia();
        Base base = mapa.getBasePorNombre(nombreBase);
        if (base == null) {
            System.out.println("Error: La base especificada no existe.");
            return false;
        }

        Vehiculo vehiculo = viaje.getVehiculo();
        VehiculoEnum tipoVehiculo = vehiculo.getVehiculoEnum();
        if (tipoVehiculo != VehiculoEnum.PATINETE && tipoVehiculo != VehiculoEnum.BICICLETA) {
            System.out.println("Error: Este método solo es válido para finalizar viajes de patinete o bicicleta.");
            return false;
        }
        if (base.getEspaciosLibres() <= 0) {
            System.out.println("Error: La base " + nombreBase + " está llena. Seleccione otra base.");
            return false;
        }

        // Lógica específica de base
        viaje.setBaseFin(base);

        // Lógica común
        finalizarViajeComun(viaje, usuario);

        // Actualizar base del vehículo
        ((VehiculoDeBase) vehiculo).setBase(base);
        gestorVehiculos.moverVehiculoABase(vehiculo.getID(), nombreBase);
        base.incrementarViajesFinalizadosBase();

        // Mostrar resumen
        System.out.println("\n=== VIAJE FINALIZADO ===");
        mostrarResumenViajeFinalizado(viaje);
        System.out.println("Saldo disponible: " + String.format("%.2f", usuario.getSaldo()) + "€");
        System.out.println("Vehículo estacionado correctamente en la base " + nombreBase);

        return true;
    }

    // Finaliza viaje de moto en coordenadas específicas
    public boolean finalizarViajeCoordenadas(Usuario usuario, Viaje viaje, int coordX, int coordY) {
        // Validaciones de argumentos
        if (!validarFinalizacionViaje(usuario, viaje)) {
            return false;
        }

        Mapa mapa = Mapa.getInstancia();
        if (!mapa.sonCoordenadasValidas(coordX, coordY)) {
            System.out.println("Error: Las coordenadas especificadas están fuera de los límites del mapa.");
            return false;
        }
        if (mapa.sonCoordenadasOcupadas(coordX, coordY)) {
            System.out.println("Error: La posición especificada ya está ocupada.");
            return false;
        }

        // Lógica específica de coordenadas
        viaje.setCoordenadaFin(coordX, coordY);

        // Lógica común
        finalizarViajeComun(viaje, usuario);

        // Actualizar coordenadas del vehículo
        VehiculoDeCoordenadas vehiculoDeCoordenadas = (VehiculoDeCoordenadas) viaje.getVehiculo();
        vehiculoDeCoordenadas.setCoordenadaX(coordX);
        vehiculoDeCoordenadas.setCoordenadaY(coordY);
        gestorVehiculos.moverVehiculoACoordenadas(vehiculoDeCoordenadas.getID(), coordX, coordY);

        // Mostrar resumen
        System.out.println("\n=== VIAJE FINALIZADO ===");
        mostrarResumenViajeFinalizado(viaje);
        System.out.println("Saldo disponible: " + String.format("%.2f", usuario.getSaldo()) + "€");
        System.out.println("La " + vehiculoDeCoordenadas.getNombreVehiculo() + " ha sido estacionada correctamente en las coordenadas (" + coordX + ", " + coordY + ")");

        return true;
    }

    // Método privado para validaciones comunes de finalización de viaje
    private boolean validarFinalizacionViaje(Usuario usuario, Viaje viaje) {
        if (viaje == null) {
            System.out.println("Error: El viaje no existe.");
            return false;
        }
        if (usuario == null) {
            System.out.println("Error: El usuario no puede ser nulo.");
            return false;
        }
        if (!viaje.estaActivo()) {
            System.out.println("Error: El viaje ya ha sido finalizado.");
            return false;
        }
        Vehiculo vehiculo = viaje.getVehiculo();
        if (vehiculo == null) {
            System.out.println("Error: El vehículo del viaje no existe.");
            return false;
        }
        return true;
    }

    // Finaliza viaje y actualiza datos comunes (tiempo, costo, estado)
    private void finalizarViajeComun(Viaje viaje, Usuario usuario) {
        // Actualizar tiempo y estado
        viaje.setTiempoFin(System.currentTimeMillis());
        viaje.setActivo(false);

        // Actualizar batería del vehículo
        viaje.actualizarBateriaVehiculo();

        // Calcular y descontar costo
        double costo = calcularCosto(viaje);

        // Aplicar penalización si la batería está agotada
        if (viaje.getVehiculo().getBateria() <= 0) {
            double penalizacion = Penalizaciones.obtenerPenalizacionPorBateriaAgotada();
            costo += penalizacion;
            System.out.println("Penalización aplicada por batería agotada: " + penalizacion + "€");
        }

        usuario.descontarCosto(costo);
        viaje.setCosto(costo);

        // Marcar vehículo como no en uso
        viaje.getVehiculo().setEnUso(false);
    }
    
    //==========================
    // CÁLCULOS Y UTILIDADES
    //==========================

    // Calcula el costo de un viaje finalizado
    private double calcularCosto(Viaje viaje) {
        // Obtención de datos relevantes
        Vehiculo vehiculo = viaje.getVehiculo();
        Usuario usuario = viaje.getUsuario();
        double duracionHoras = (double)(viaje.getTiempoFin() - viaje.getTiempoInicio()) / 3600000.0;
        double tarifaBase = vehiculo.getVehiculoEnum().getTarifa();
        double costoBase = duracionHoras * tarifaBase;

        // Aplicar descuento si es usuario premium
        if (usuario.getRol() == RolEnum.USUARIO_PREMIUM) {
            double descuento = vehiculo.getVehiculoEnum().getDescuento();
            return costoBase * (1 - descuento / 100.0);
        }
        return costoBase;
    }


    //==========================
    // CONSULTAS Y RESÚMENES
    //==========================
    // Muestra información de un viaje activo
    public boolean mostrarViajeActivo(Viaje viaje) {
        // Validación de existencia y estado del viaje
        if (viaje == null) {
            System.out.println("Error: El viaje no existe.");
            return false;
        }
        if (!viaje.estaActivo()) {
            System.out.println("Error: El viaje ya ha sido finalizado. Use obtenerResumenViaje() para viajes completados.");
            return false;
        }

        // Cálculo de tiempo transcurrido
        long tiempoActual = System.currentTimeMillis();
        double duracionHoras = (double)(tiempoActual - viaje.getTiempoInicio()) / 3600000.0;
        double duracionMinutos = duracionHoras * 60;

        // Obtención de datos relevantes
        Vehiculo vehiculo = viaje.getVehiculo();
        Usuario usuario = viaje.getUsuario();
        double tarifaBase = vehiculo.getVehiculoEnum().getTarifa();

        // Cálculo del costo estimado (con descuento si es premium)
        double costoEstimado = (duracionMinutos / 60.0) * tarifaBase;
        if (usuario instanceof GestorPersonas.Personas.Premium) {
            double descuento = vehiculo.getVehiculoEnum().getDescuento();
            costoEstimado *= (1 - descuento / 100.0);
        }

        // Impresión de información del viaje activo
        System.out.println("=== VIAJE ACTIVO ===");
        System.out.println("Vehículo: " + vehiculo.getNombreVehiculo());
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Inicio: " + new java.util.Date(viaje.getTiempoInicio()));
        System.out.println("Tiempo transcurrido: " + String.format("%.2f", duracionHoras) +
                " horas (" + String.format("%.1f", duracionMinutos) + " minutos)");
        System.out.println("Costo actual estimado: " + String.format("%.2f", costoEstimado) + "€");
        System.out.println("Saldo disponible: " + String.format("%.2f", usuario.getSaldo()) + "€");
        return true;
    }

    // Muestra resumen detallado de un viaje finalizado
    public static void mostrarResumenViajeFinalizado(Viaje viaje) {
        // Validación de existencia del viaje
        if (viaje == null) {
            System.out.println("Error: El viaje no existe.");
            return;
        }

        // Obtención de datos relevantes
        Vehiculo vehiculo = viaje.getVehiculo();
        Usuario usuario = viaje.getUsuario();
        VehiculoEnum tipoVehiculo = vehiculo.getVehiculoEnum();
        double duracionHoras = (double)(viaje.getTiempoFin() - viaje.getTiempoInicio()) / 3600000.0;
        double duracionMinutos = duracionHoras * 60;

        // Calcular batería consumida
        double consumoPorMinuto = vehiculo.getVehiculoEnum().getRitmoConsumo();
        double bateriaConsumida = duracionMinutos * consumoPorMinuto;
        double bateriaFinal = vehiculo.getBateria();

        // Impresión de datos generales
        System.out.println("Vehículo ID: " + vehiculo.getID());
        System.out.println("Vehículo: " + vehiculo.getNombreVehiculo());
        System.out.println("Usuario: " + usuario.getNombre() + " " + usuario.getApellidos());
        System.out.println("Inicio: " + new Date(viaje.getTiempoInicio()));
        System.out.println("Fin: " + new Date(viaje.getTiempoFin()));
        System.out.println("Duración: " + String.format("%.1f", duracionHoras) + " horas (" +
                String.format("%.1f", duracionMinutos) + " minutos)");

        // Información específica según tipo de vehículo
        if (tipoVehiculo == VehiculoEnum.MOTOPEQUENA || tipoVehiculo == VehiculoEnum.MOTOGRANDE) {
            int xInicio = viaje.getCoordenadaXInicio();
            int yInicio = viaje.getCoordenadaYInicio();
            int xFin = viaje.getCoordenadaXFin();
            int yFin = viaje.getCoordenadaYFin();
            System.out.println("Coordenada Inicio: (" + xInicio + ", " + yInicio + ")");
            if (viaje.estaActivo()) {
                System.out.println("El viaje está en curso.");
            } else {
                System.out.println("Coordenada Fin: (" + xFin + ", " + yFin + ")");
            }
        } else if (tipoVehiculo == VehiculoEnum.BICICLETA || tipoVehiculo == VehiculoEnum.PATINETE) {
            Base baseInicio = viaje.getBaseInicio();
            Base baseFin = viaje.getBaseFin();
            if (baseInicio != null) {
                System.out.println("Base Inicio: " + baseInicio.getNombre() +
                        " (Coordenadas: " + baseInicio.getCoordenadaX() + ", " +
                        baseInicio.getCoordenadaY() + ")");
            }
            if (viaje.estaActivo()) {
                System.out.println("El viaje está en curso.");
            } else if (baseFin != null) {
                System.out.println("Base Fin: " + baseFin.getNombre() +
                        " (Coordenadas: " + baseFin.getCoordenadaX() + ", " +
                        baseFin.getCoordenadaY() + ")");
            }
        }

        // Impresión del costo final y descuento si aplica
        System.out.print("Costo final: " + String.format("%.2f", viaje.getCosto()) + "€");
        if (usuario.getRol() == RolEnum.USUARIO_PREMIUM) {
            double descuento = vehiculo.getVehiculoEnum().getDescuento();
            System.out.print(" (Incluye descuento premium del " + String.format("%.0f", descuento) + "%)");
        }
        System.out.println();

        // Mostrar batería consumida y batería final
        System.out.println("Batería consumida en el viaje: " + String.format("%.2f", bateriaConsumida) + "%");
        System.out.println("Batería restante del vehículo: " + String.format("%.2f", bateriaFinal) + "%");
    }
}