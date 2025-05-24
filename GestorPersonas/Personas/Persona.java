
package GestorPersonas.Personas;
import Enum.RolEnum;

public abstract class Persona {
    private  String nombre;
    private  String apellidos;
    private  final String DNI;
    private  RolEnum rol;

    public Persona(String nombre, String apellidos, String DNI, RolEnum rol) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.DNI = DNI;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDNI() {
        return DNI;
    }

    public RolEnum getRol() {
        return rol;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }
}
