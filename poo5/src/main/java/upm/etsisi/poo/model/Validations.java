package upm.etsisi.poo.model;

public class Validations {

    public static void isNotNull(String variable, String string) throws ModelException {
        if (string == null || string.isEmpty()) {
            throw new ModelException("Dato nulo o vacío en " + variable);
        }
    }

    public static void isMinimum(String variable, String string, int minimum) throws ModelException {
        isNotNull(variable, string);
        if (string.length() < minimum) {
            throw new ModelException(variable + " no cumple la longitud minima de " + minimum);
        }
    }

    public static void isMaximum(String variable, String string, int maximum) throws ModelException {
        isNotNull(variable, string);
        if (string.length() < maximum) {
            throw new ModelException(variable + " es mayor que la longitud maxima de " + maximum);
        }
    }

    public static void isString(String variable, String string) throws ModelException {
        if (!string.matches("[a-zA-Z]+")) {
            throw new ModelException("Formato incorrecto: " + variable + " debe contener solo letras.");
        }
    }
}
