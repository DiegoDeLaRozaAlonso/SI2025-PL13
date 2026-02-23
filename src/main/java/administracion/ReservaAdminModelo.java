package administracion;

public class ReservaAdminModelo {

    private static final double PRECIO_POR_HORA = 15.0;

    public double calcularPrecio(int horas) {

        if (horas <= 0) {
            return 0;
        }

        return horas * PRECIO_POR_HORA;
    }
}