package Menus;

import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorVehiculosBases.Vehiculos.VehiculoDeBase;
import GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas;
import GestorViajes.Beneficios;
import GestorViajes.Viaje;
import Utilidades.UtilidadesEntradaDatos;

public class MenuUsuario {

    private final ServicioUsuario servicioUsuario = new ServicioUsuario();

    public MenuUsuario() {}

    public void mostrarMenu(Usuario usuario) {
        while (true) {
            System.out.println("\n========== MENÚ USUARIO ==========");
            System.out.println("Usuario: " + usuario.getNombre() + " " + usuario.getApellidos() +
                              " | Tipo: " + usuario.getRol());

            System.out.println("1. Submenú - Iniciar Viaje (con Información Vehículos Disponibles)");
            System.out.println("2. Submenú - Finalizar viaje (con Información del Viaje Activo)");
            System.out.println("3. Submenú - Reportar fallo");
            System.out.println("4. Ver historial de viajes realizados");
            System.out.println("5. Submenú - Saldo");
            System.out.println("6. Buscar vehículo más cercano");
            boolean esPremium = usuario instanceof GestorPersonas.Personas.Premium;
            if (esPremium) {
                System.out.println("7. Reservar vehículo");
                System.out.println("8. Cerrar Sesión");
            } else {
                System.out.println("7. Cerrar Sesión");
            }
            System.out.print("\nSeleccione opción: ");

            int maxOpcion = esPremium ? 8 : 7;
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, maxOpcion);

            switch (opcion) {
                case 1 -> mostrarSubmenuViajes(usuario);
                case 2 -> mostrarSubmenuInformacionFinalizar(usuario);
                case 3 -> mostrarSubmenuReportarFallo(usuario);
                case 4 -> servicioUsuario.mostrarHistorialViajes(usuario);
                case 5 -> mostrarSubmenuSaldo(usuario);
                case 6 -> getVehiculoMasCercano(usuario);
                case 7 -> {
                    if (esPremium) {
                        reservarVehiculo(usuario);
                    } else {
                        return;
                    }
                }
                case 8 -> {
                    if (esPremium) return;
                }
                default -> System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
            }
        }
    }

    // Submenú 1: Viajes (consultar/iniciar)
    private void mostrarSubmenuViajes(Usuario usuario) {
        while (true) {
            System.out.println("\n--- Viajes (consultar/iniciar) ---");
            System.out.println("1. Consulta de vehículos disponibles");
            System.out.println("2. Iniciar viaje bicicleta/patinete");
            System.out.println("3. Iniciar viaje moto");
            System.out.println("4. Volver al menú principal");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 4);
            switch (opcion) {
                case 1 -> servicioUsuario.mostrarConsultaVehiculosDisponibles(usuario);
                case 2 -> iniciarViajeBase(usuario);
                case 3 -> iniciarViajeCoordenadas(usuario);
                case 4 -> { return; }
            }
        }
    }

    // Submenú 2: Información/Finalizar viaje (información primero)
    private void mostrarSubmenuInformacionFinalizar(Usuario usuario) {
        while (true) {
            System.out.println("\n--- Información/Finalizar viaje ---");
            System.out.println("1. Ver información del viaje activo");
            System.out.println("2. Finalizar viaje bicicleta/patinete");
            System.out.println("3. Finalizar viaje moto");
            System.out.println("4. Volver al menú principal");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 4);
            switch (opcion) {
                case 1 -> mostrarInformacionViajeActivo(usuario);
                case 2 -> finalizarViajeBase(usuario);
                case 3 -> finalizarViajeCoordenadas(usuario);
                case 4 -> { return; }
            }
        }
    }

    // Submenú 3: Reportar fallo
    private void mostrarSubmenuReportarFallo(Usuario usuario) {
        while (true) {
            System.out.println("\n--- Reportar fallo ---");
            System.out.println("1. Reportar fallo en vehículo");
            System.out.println("2. Reportar fallo en base");
            System.out.println("3. Volver al menú principal");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 3);
            switch (opcion) {
                case 1 -> reportarFalloVehiculo(usuario);
                case 2 -> reportarFalloBase(usuario);
                case 3 -> { return; }
            }
        }
    }

    private void mostrarSubmenuSaldo(Usuario usuario) {
        while (true) {
            System.out.println("\n--- Saldo ---");
            System.out.println("1. Ver saldo disponible");
            System.out.println("2. Recargar saldo");
            System.out.println("3. Volver al menú principal");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 3);
            switch (opcion) {
                case 1 -> servicioUsuario.mostrarSaldo(usuario);
                case 2 -> recargarSaldo(usuario);
                case 3 -> { return; }
            }
        }
    }

    private void iniciarViajeBase(Usuario usuario) {
        System.out.println("\n--- Iniciar viaje de bicicleta o patinete ---");
        System.out.println("1. Alquilar " + VehiculoEnum.BICICLETA);
        System.out.println("2. Alquilar " + VehiculoEnum.PATINETE);
        int opcionVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona el tipo de vehículo", 1, 2);

        VehiculoEnum tipoVehiculo = (opcionVehiculo == 1) ? VehiculoEnum.BICICLETA : VehiculoEnum.PATINETE;

        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 20);

        servicioUsuario.iniciarViajeBase(usuario, tipoVehiculo, nombreBase);

        Viaje viaje = servicioUsuario.getViajeActivo(usuario);
        if (viaje != null) {
            System.out.println("Viaje iniciado correctamente.");
        } else {
            System.out.println("No se pudo iniciar el viaje.");
        }
    }

    private void iniciarViajeCoordenadas(Usuario usuario) {
        System.out.println("\n--- Iniciar viaje de moto ---");
        System.out.println("1. Alquilar " + VehiculoEnum.MOTOPEQUENA);
        System.out.println("2. Alquilar " + VehiculoEnum.MOTOGRANDE);
        int opcionVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona el tipo de moto", 1, 2);

        VehiculoEnum tipoVehiculo = (opcionVehiculo == 1) ? VehiculoEnum.MOTOPEQUENA : VehiculoEnum.MOTOGRANDE;

        int coordX = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioUsuario.getMapa().getMaxCoordenadaX());
        int coordY = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioUsuario.getMapa().getMaxCoordenadaY());

        servicioUsuario.iniciarViajeCoordenadas(usuario, tipoVehiculo, coordX, coordY);

        Viaje viaje = servicioUsuario.getViajeActivo(usuario);
        if (viaje != null) {
            System.out.println("Viaje de moto iniciado correctamente.");
        } else {
            System.out.println("No se pudo iniciar el viaje de moto.");
        }
    }

    private void finalizarViajeBase(Usuario usuario) {
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 20);
        boolean terminado = servicioUsuario.finalizarViajeBase(usuario, nombreBase);
        if (terminado) {
            System.out.println("Viaje finalizado correctamente.");
        } else {
            System.out.println("No se pudo finalizar el viaje.");
        }
    }

    private void finalizarViajeCoordenadas(Usuario usuario) {
        int coordX = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioUsuario.getMapa().getMaxCoordenadaX());
        int coordY = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioUsuario.getMapa().getMaxCoordenadaY());
        boolean terminado = servicioUsuario.finalizarViajeCoordenadas(usuario, coordX, coordY);
        if (terminado) {
            System.out.println("Viaje de moto finalizado correctamente.");
        } else {
            System.out.println("No se pudo finalizar el viaje de moto.");
        }
    }

    private void mostrarInformacionViajeActivo(Usuario usuario) {
        Viaje viajeActivo = servicioUsuario.getViajeActivo(usuario);
        if (viajeActivo == null) {
            System.out.println("No tienes ningún viaje activo.");
            return;
        }
        GestorViajes.ServicioViajes.getInstancia().mostrarViajeActivo(viajeActivo);
    }

    private void reportarFalloVehiculo(Usuario usuario) {
        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("Introduce el ID del vehículo", 1, Integer.MAX_VALUE);
        Vehiculo vehiculo = servicioUsuario.getGestorVehiculos().getVehiculoPorID(idVehiculo);
        vehiculo.setAverias(true);
        if (vehiculo.getAverias()) {
            System.out.println("Fallo reportado correctamente.");
        } else {
            System.out.println("No se pudo reportar el fallo del vehículo.");
        }
    }

    private void reportarFalloBase(Usuario usuario) {
        String nombreBase = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre de la base", 1, 20);
        Base base = servicioUsuario.getMapa().getBasePorNombre(nombreBase);
        if (base != null) {
            base.setAverias(true);
            System.out.println("Fallo reportado correctamente en la base " + nombreBase + ".");
        } else {
            System.out.println("No se pudo reportar el fallo de la base. Base no encontrada.");
        }
    }

    public void recargarSaldo(Usuario usuario) {
        double cantidad = UtilidadesEntradaDatos.getDecimalPositivo("Cantidad a recargar", 0.01, Double.MAX_VALUE);
        servicioUsuario.recargarSaldo(usuario, cantidad);
    }

    public void reservarVehiculo(Usuario usuario) {
        Beneficios beneficios = new Beneficios();
        if (!beneficios.getReservasPermitidas()) {
            System.out.println("Las reservas están deshabilitadas actualmente.");
            return;
        }
        servicioUsuario.mostrarConsultaVehiculosDisponibles(usuario);
        int idVehiculo = UtilidadesEntradaDatos.getEnteroPositivo("ID del vehículo", 1, Integer.MAX_VALUE);
        servicioUsuario.reservarVehiculo(usuario, idVehiculo);
    }

    public void getVehiculoMasCercano(Usuario usuario) {
        System.out.println("\n--- Buscar vehículo más cercano ---");
        System.out.println("Selecciona el tipo de vehículo:");
        System.out.println("1. Bicicleta");
        System.out.println("2. Patinete");
        System.out.println("3. Moto pequeña");
        System.out.println("4. Moto grande");
        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Tipo de vehículo", 1, 4);

        VehiculoEnum tipoVehiculo;
        switch (opcion) {
            case 1 -> tipoVehiculo = VehiculoEnum.BICICLETA;
            case 2 -> tipoVehiculo = VehiculoEnum.PATINETE;
            case 3 -> tipoVehiculo = VehiculoEnum.MOTOPEQUENA;
            case 4 -> tipoVehiculo = VehiculoEnum.MOTOGRANDE;
            default -> {
                System.out.println("Opción no válida.");
                return;
            }
        }

        int coordX = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada X", 0, servicioUsuario.getMapa().getMaxCoordenadaX());
        int coordY = UtilidadesEntradaDatos.getEnteroPositivo("Introduce la coordenada Y", 0, servicioUsuario.getMapa().getMaxCoordenadaY());

        Vehiculo vehiculoCercano = servicioUsuario.getVehiculoMasCercano(usuario, tipoVehiculo, coordX, coordY);

        if (vehiculoCercano != null) {
            if (tipoVehiculo == VehiculoEnum.BICICLETA || tipoVehiculo == VehiculoEnum.PATINETE) {
                System.out.println("La base más cercana con " + tipoVehiculo + " disponible es: " +
                    ((VehiculoDeBase) vehiculoCercano).getBase().getNombre());
            } else {
                System.out.println("La moto más cercana está en las coordenadas: (" +
                    ((VehiculoDeCoordenadas) vehiculoCercano).getCoordenadaX() + ", " +
                    ((VehiculoDeCoordenadas) vehiculoCercano).getCoordenadaY() + ")");
            }
            System.out.println("ID del vehículo: " + vehiculoCercano.getID());
        } else {
            System.out.println("No se ha encontrado ningún vehículo disponible cercano.");
        }
    }
}
