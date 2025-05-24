package GestorViajes;

/**
 * Clase que gestiona las diferentes penalizaciones aplicables a los viajes.
 */
public class Penalizaciones {

    //==========================
    // VALORES DE PENALIZACIONES
    //==========================
    private static double penalizacionBateriaAgotada = 1.0; // Valor inicial en euros

    //==========================
    // OBTENER PENALIZACIÓN POR BATERÍA AGOTADA
    //==========================
    public static double obtenerPenalizacionPorBateriaAgotada() {
        // Devuelve el valor actual de la penalización por batería agotada
        return penalizacionBateriaAgotada;
    }

    //==========================
    // DEFINIR PENALIZACIÓN POR BATERÍA AGOTADA
    //==========================
    public static boolean definirPenalizacionPorBateríaAgotada(double valor) {
        // Verifica que el valor no sea negativo
        if (valor < 0) {
            System.out.println("Error: La penalización no puede ser negativa.");
            return false;
        }
        // Actualiza el valor de la penalización
        penalizacionBateriaAgotada = valor;
        System.out.println("Penalización por batería agotada actualizada: " + valor + "€");
        return true;
    }
}