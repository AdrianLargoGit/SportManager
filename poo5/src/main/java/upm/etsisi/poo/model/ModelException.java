package upm.etsisi.poo.model;

import java.io.Serial;

public class ModelException extends Exception {
    @Serial
    private static final long serialVersionUID = 7322478738749203984L;

    public ModelException(String message) {
        super(message);
    }
}
