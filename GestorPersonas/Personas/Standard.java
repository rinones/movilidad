
package GestorPersonas.Personas;
import Enum.RolEnum;


public class Standard extends Usuario {

    public Standard(String nombre, String apellidos, String DNI) {
        super(nombre, apellidos, DNI, RolEnum.USUARIO_STANDARD);
    }

}
