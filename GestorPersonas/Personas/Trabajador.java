package GestorPersonas.Personas;
import Enum.RolEnum;

public abstract class Trabajador extends Persona {

    public Trabajador(String nombre, String apellidos, String DNI, RolEnum rol) {
        super(nombre, apellidos, DNI, rol);
    }

}
