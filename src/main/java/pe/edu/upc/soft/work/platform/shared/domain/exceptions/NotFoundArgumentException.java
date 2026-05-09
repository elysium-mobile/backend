package pe.edu.upc.soft.work.platform.shared.domain.exceptions;

/**
 * Exception thrown when a required argument is not found
 */
public class NotFoundArgumentException extends RuntimeException {

    /**
     * Constructor for NotFoundArgumentException
     *
     * @param message The exception message
     */
    public NotFoundArgumentException(String message) {
        super(message);
    }

}
