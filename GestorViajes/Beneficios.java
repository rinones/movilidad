package GestorViajes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Beneficios {

    //==========================
    // CONFIGURACIÓN Y ESTADO
    //==========================
    private static boolean reservasPermitidas = true;
    private static boolean bateriaPremiumPermitida = true;
    private static double bateriaMaxima = 100.0;
    private static double bateriaMinimaStandard = 20.0;
    private static double bateriaMinimaPremium = 10.0;

    //==========================
    // ESTRUCTURAS DE DATOS
    //==========================
    private Map<Integer, LocalDateTime> reservas = new HashMap<>();

    //==========================
    // CONSTRUCTOR
    //==========================
    public Beneficios() {
        // Constructor por defecto
    }

    //==========================
    // ACCESO A DATOS
    //==========================

    // Devuelve el nivel máximo de batería permitido
    public static double getBateriaMaxima() {
        // Retorna el valor máximo de batería permitido
        return bateriaMaxima;
    }

    // Devuelve el nivel mínimo de batería para usuarios estándar
    public static double getBateriaMinimaStandard() {
        // Retorna el valor mínimo de batería estándar
        return bateriaMinimaStandard;
    }

    // Devuelve el nivel mínimo de batería para usuarios premium si está permitido
    public static double getBateriaMinimaPremium() {
        // Si la batería premium está permitida, retorna ese valor; si no, retorna el estándar
        if (bateriaPremiumPermitida) {
            return bateriaMinimaPremium;
        } else {
            return bateriaMinimaStandard;
        }
    }

    // Indica si las reservas están habilitadas en el sistema
    public boolean getReservasPermitidas() {
        // Retorna el estado de las reservas permitidas
        return reservasPermitidas;
    }

    public boolean getBateriaPremiumPermitida() {
        // Retorna el estado de la batería premium permitida
        return bateriaPremiumPermitida;
    }

    //==========================
    // GESTIÓN DE BENEFICIOS
    //==========================

    // Configura si se permiten o no las reservas en todo el sistema
    public static void setReservasPermitidas(boolean permitido) {
        // Cambia el valor de reservasPermitidas y muestra un mensaje
        reservasPermitidas = permitido;
        String mensaje;
        if (permitido) {
            mensaje = "Las reservas han sido habilitadas en el sistema.";
        } else {
            mensaje = "Las reservas han sido deshabilitadas en el sistema.";
        }
        System.out.println(mensaje);
    }

    // Configura si se permite o no el uso de batería premium en el sistema
    public static void setBateriaPremiumPermitida(boolean permitido) {
        // Cambia el valor de bateriaPremiumPermitida y muestra un mensaje
        bateriaPremiumPermitida = permitido;
        String mensaje;
        if (permitido) {
            mensaje = "La batería premium ha sido habilitada en el sistema.";
        } else {
            mensaje = "La batería premium ha sido deshabilitada en el sistema.";
        }
        System.out.println(mensaje);
    }

    //==========================
    // GESTIÓN DE RESERVAS
    //==========================

    // Verifica si un vehículo está reservado y elimina la reserva si ya expiró
    public boolean estaReservado(int ID) {
        // Si no existe reserva para el ID, retorna false
        if (!reservas.containsKey(ID)) {
            return false;
        }
        // Obtiene la fecha y hora actual y la fecha de fin de la reserva
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime finReserva = reservas.get(ID);
        // Si la reserva ya expiró, la elimina y retorna false
        if (ahora.isAfter(finReserva)) {
            reservas.remove(ID);
            return false;
        }
        // Si la reserva sigue activa, retorna true
        return true;
    }

    // Intenta reservar un vehículo por 20 minutos
    public boolean reservarVehiculo(int ID) {
        // Solo permite reservar si el vehículo no está reservado
        if (estaReservado(ID)) {
            return false;
        }
        // Asigna una nueva reserva por 20 minutos
        LocalDateTime finReserva = LocalDateTime.now().plusMinutes(20);
        reservas.put(ID, finReserva);
        return true;
    }

    // Intenta reservar un vehículo por un número específico de minutos
    public boolean reservarVehiculo(int ID, int minutos) {
        if (estaReservado(ID)) {
            return false;
        }
        LocalDateTime finReserva = LocalDateTime.now().plusMinutes(minutos);
        reservas.put(ID, finReserva);
        return true;
    }
}