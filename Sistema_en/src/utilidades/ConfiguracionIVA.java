package utilidades;

/**
 * Utilidades compartidas para el IVA configurable.
 *
 * El porcentaje se guarda en la tabla Configuraciones como un entero entre 0 y
 * 100. Si el valor falta o no es valido, se usa el valor por defecto.
 */
public final class ConfiguracionIVA {

    public static final String CLAVE = "porcentaje_iva";
    public static final int PORCENTAJE_POR_DEFECTO = 16;

    private ConfiguracionIVA() {
    }

    /**
     * Convierte el valor almacenado en un porcentaje valido.
     *
     * @param valor texto leido desde la base de datos
     * @return porcentaje entre 0 y 100, o el valor por defecto si no es valido
     */
    public static int parsearPorcentaje(String valor) {
        if (valor == null) {
            return PORCENTAJE_POR_DEFECTO;
        }

        try {
            int porcentaje = Integer.parseInt(valor.trim());
            if (porcentaje >= 0 && porcentaje <= 100) {
                return porcentaje;
            }
        } catch (NumberFormatException e) {
            // Se usa el valor por defecto.
        }

        return PORCENTAJE_POR_DEFECTO;
    }

    /**
     * Texto que se muestra en la etiqueta del IVA.
     *
     * @param porcentaje porcentaje configurado
     * @return texto con el formato "IVA 16%"
     */
    public static String etiqueta(int porcentaje) {
        return "IVA " + porcentaje + "%";
    }

    /**
     * Calcula el importe del IVA para un subtotal.
     *
     * @param subtotal importe antes de impuestos
     * @param porcentaje porcentaje configurado
     * @return importe del IVA
     */
    public static float calcularIva(float subtotal, int porcentaje) {
        return subtotal * porcentaje / 100f;
    }
}
