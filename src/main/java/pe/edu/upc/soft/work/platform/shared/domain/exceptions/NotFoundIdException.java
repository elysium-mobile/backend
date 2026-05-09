package pe.edu.upc.soft.work.platform.shared.domain.exceptions;

/**
 * Exception thrown when an entity with a specified ID is not found.
 */
public class NotFoundIdException extends RuntimeException {

    /**
     * Constructor for NotFoundIdException.
     *
     * @param entityClass the class of the entity that was not found
     * @param id the ID of the entity that was not found
     */
    public NotFoundIdException(Class<?> entityClass, Long id) {
        super(String.format("%s with id %s does not exist.", entityClass.getSimpleName(), id));
    }
}
