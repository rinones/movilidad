package Enum;
public enum VehiculoEnum {
    MOTOPEQUENA(10.0, 10.0, 0.4, "Moto Pequeña"),
    MOTOGRANDE(20.0, 5.0, 0.25, "Moto Grande"),
    BICICLETA(5.0, 7.0, 1.0, "Bicicleta"),
    PATINETE(3.0, 15.0, 0.5, "Patinete");

    private double tarifa;
    private double descuento;
    private double ritmoConsumo;
    private final String textoMostrado;

    VehiculoEnum(double tarifa, double descuento, double ritmoConsumo, String textoMostrado) {
        this.tarifa = tarifa;
        this.descuento = descuento;
        this.ritmoConsumo = ritmoConsumo;
        this.textoMostrado = textoMostrado;
    }

    public double getTarifa() {
        return tarifa;
    }
    
    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
    
    public double getDescuento() {
        return descuento;
    }
    
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getRitmoConsumo() {
        return ritmoConsumo;
    }
    
    public void setRitmoConsumo(double ritmoConsumo) {
        this.ritmoConsumo = ritmoConsumo;
    }
    
    @Override
    public String toString() {
        return textoMostrado;
    }
}
