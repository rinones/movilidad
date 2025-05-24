package Utilidades;

import java.util.Scanner;

public class UtilidadesEntradaDatos {
    private static final Scanner scanner = new Scanner(System.in);

    // Solicita un número entero positivo dentro de un rango al usuario
    public static int getEnteroPositivo(String mensaje, int min, int max) {
        int valor;
        do {
            // Muestra el mensaje y el rango permitido
            System.out.print(mensaje + " (" + min + "-" + max + "): ");
            try {
                // Intenta leer el valor como entero
                valor = Integer.parseInt(scanner.nextLine());
                // Verifica que esté dentro del rango
                if (valor < min || valor > max) {
                    System.out.println("El valor debe estar entre " + min + " y " + max + ".");
                    valor = -1;
                }
            } catch (NumberFormatException e) {
                // Si no es un número válido, muestra mensaje de error
                System.out.println("Por favor, introduzca un número entero válido.");
                valor = -1;
            }
        } while (valor == -1); // Repite hasta que el valor sea válido

        return valor;
    }

    // Solicita un número decimal positivo dentro de un rango al usuario
    public static double getDecimalPositivo(String mensaje, double min, double max) {
        double valor;
        do {
            // Muestra el mensaje y el rango permitido
            System.out.print(mensaje + " (" + min + "-" + max + "): ");
            try {
                // Intenta leer el valor como decimal
                valor = Double.parseDouble(scanner.nextLine());
                // Verifica que esté dentro del rango
                if (valor < min || valor > max) {
                    System.out.println("El valor debe estar entre " + min + " y " + max + ".");
                    valor = -1;
                }
            } catch (NumberFormatException e) {
                // Si no es un número válido, muestra mensaje de error
                System.out.println("Por favor, introduzca un número decimal válido.");
                valor = -1;
            }
        } while (valor == -1); // Repite hasta que el valor sea válido

        return valor;
    }

    // Solicita un String con longitud mínima y máxima al usuario
    public static String getStringLongitud(String mensaje, int min, int max) {
        String valor;
        do {
            System.out.print(mensaje + " (entre " + min + " y " + max + " caracteres): ");
            valor = scanner.nextLine();
            if (valor.length() < min || valor.length() > max) {
                System.out.println("La entrada debe tener entre " + min + " y " + max + " caracteres.");
            }
        } while (valor.length() < min || valor.length() > max);
        return valor;
    }

    //Socilitar DNI con formato 8 dígitos y letra
    public static String getDNI(String mensaje) {
        String dni;
        do {
            System.out.print(mensaje + " (8 dígitos + letra): ");
            dni = scanner.nextLine();
            if (!dni.matches("\\d{8}[A-Z]")) {
                System.out.println("El DNI debe tener 8 dígitos seguidos de una letra mayúscula.");
            }
        } while (!dni.matches("\\d{8}[A-Z]"));
        return dni;
    }
}
