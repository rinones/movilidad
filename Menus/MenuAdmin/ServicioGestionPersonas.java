package Menus.MenuAdmin;

import Enum.RolEnum;
import GestorPersonas.GestorPersonas;
import GestorPersonas.GestorPromocion;
import GestorPersonas.Personas.Persona;
import GestorPersonas.Personas.Usuario;
import GestorViajes.GestorViajes;
import Utilidades.UtilidadesMenus;
import java.util.Map;

public class ServicioGestionPersonas {

    private final GestorPersonas gestorPersonas;
    private final GestorViajes gestorViajes;
    private final UtilidadesMenus utilsMenu;

    public ServicioGestionPersonas() {
        this.gestorPersonas = GestorPersonas.getInstancia();
        this.gestorViajes = GestorViajes.getInstancia();
        this.utilsMenu = new UtilidadesMenus();
    }

    public void altaUsuario(String nombre, String apellidos, String dni, RolEnum rolEnum) {
        gestorPersonas.addPersona(nombre, apellidos, dni, rolEnum);
    }

    public boolean bajaUsuario(String dni) {
        return gestorPersonas.removePersona(dni);
    }

    public boolean existeDNI(String dni) {
        return gestorPersonas.existeDNI(dni);
    }

    public Persona getPersonaPorDNI(String dni) {
        return gestorPersonas.getPersonaPorDNI(dni);
    }

    public void modificarPersona(String dni, String nuevoNombre, String nuevosApellidos, RolEnum rolEnum) {
        gestorPersonas.modificarPersona(dni, nuevoNombre, nuevosApellidos, rolEnum);
    }

    public void mostrarUsuariosPromocion() {
        GestorPromocion.getInstancia().mostrarUsuariosPromocion();
    }

    public boolean cambiarRolUsuario(String dni, boolean aPremium) {
        return gestorPersonas.cambiarRolUsuario(dni, aPremium);
    }

    public Map<String, Object> obtenerDetallesPromocionUsuario(Usuario usuario) {
        return GestorPromocion.getInstancia().obtenerDetallesPromocionUsuario(usuario);
    }

    public void buscarPersonaPorDNI() {
        gestorPersonas.buscarPersonaPorDNI();
    }

    public void buscarPersonaPorNombre() {
        gestorPersonas.buscarPersonaPorNombre();
    }

    public void buscarPersonaPorApellido() {
        gestorPersonas.buscarPersonaPorApellido();
    }

    public void filtrarPersonasPorRol() {
        gestorPersonas.filtrarPersonasPorRol();
    }

    public RolEnum seleccionarRol() {
        return utilsMenu.seleccionarRol();
    }
}