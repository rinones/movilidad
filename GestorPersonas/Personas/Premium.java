 
package GestorPersonas.Personas;
import Enum.RolEnum;

public class Premium extends Usuario {

    public Premium(String nombre, String apellidos, String DNI) {
        super(nombre, apellidos, DNI, RolEnum.USUARIO_PREMIUM);
    }


}
