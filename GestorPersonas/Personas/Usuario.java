package GestorPersonas.Personas;
import Enum.RolEnum;
import GestorViajes.Viaje;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario extends Persona {

    // =========================
    // ATRIBUTOS
    // =========================
    private double saldo; // Saldo del usuario
    private List<Viaje> historialViajes; // Lista de viajes del usuario

    // =========================
    // CONSTRUCTOR
    // =========================
    // Constructor de Usuario
    public Usuario(String nombre, String apellidos, String DNI, RolEnum rol) {
        super(nombre, apellidos, DNI, rol);
        this.saldo = 0.0; // Inicializar el saldo en 0
        this.historialViajes = new ArrayList<>(); // Inicializar la lista de viajes
    }

    // =========================
    // MÉTODOS DE SALDO
    // =========================

    // Devuelve el saldo actual del usuario
    public double getSaldo() {
        return saldo;
    }

    // Agrega saldo al usuario
    public void agregarSaldo(double cantidad) {
        if (cantidad <= 0) {
            return;
        }
        this.saldo += cantidad;
    }

    // Descuenta una cantidad del saldo del usuario
    public boolean descontarCosto(double cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        this.saldo -= cantidad;
        return true;
    }

    // Establece el saldo del usuario
    public void setSaldo(double saldo) {
        if (saldo < 0) {
            return;
        }
        this.saldo = saldo;
    }
    
    // =========================
    // MÉTODOS DE VIAJES
    // =========================

    // Devuelve una copia del historial de viajes del usuario
    public List<Viaje> getHistorialViajes() {
        return new ArrayList<>(historialViajes); // Devuelve una copia para evitar modificaciones directas
    }
    
    // Registra un nuevo viaje en el historial del usuario
    public void registrarViaje(Viaje viaje) {
        if (viaje != null) {
            this.historialViajes.add(viaje);
        }
    }
}
