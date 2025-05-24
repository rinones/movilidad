package Enum;

public enum RolEnum {
    MECANICO("Mecánico"),
    MANTENIMIENTO("Personal de mantenimiento"),
    ADMINISTRADOR("Administrador"),
    USUARIO_STANDARD("Usuario estándar"), 
    USUARIO_PREMIUM("Usuario premium");
    
    private final String textoMostrado;
    
    RolEnum(String textoMostrado) {
        this.textoMostrado = textoMostrado;
    }
    
    @Override
    public String toString() {
        return textoMostrado;
    }
}
