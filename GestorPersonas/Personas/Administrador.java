package GestorPersonas.Personas;

import Enum.RolEnum;

public class Administrador extends Trabajador {

    public Administrador(String nombre, String apellidos, String DNI) {
        super(nombre, apellidos, DNI, RolEnum.ADMINISTRADOR);
    }
}
