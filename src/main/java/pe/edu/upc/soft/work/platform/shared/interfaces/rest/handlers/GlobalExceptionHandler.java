package pe.edu.upc.soft.work.platform.shared.interfaces.rest.handlers;

import jakarta.persistence.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundIdException;
import pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources.BadRequestResponse;
import pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources.InternalServerErrorResponse;
import pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources.NotFoundResponse;
import pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources.ServiceUnavailableResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     *
     * @param ex the MethodArgumentNotValidException
     * @return a ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BadRequestResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        var response = new BadRequestResponse(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "JSON validation failed", errors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles illegal argument exceptions.
     *
     * @param ex the IllegalArgumentException
     * @return a ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BadRequestResponse> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("argument", ex.getMessage());

        var response = new BadRequestResponse(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Internal validation failed", errors);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles persistence exceptions.
     *
     * @param ex the PersistenceException
     * @return a ResponseEntity with error details
     */
    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ServiceUnavailableResponse> handlePersistenceException(
            PersistenceException ex) {

        var response = new ServiceUnavailableResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Handles null pointer exceptions.
     *
     * @param ex the NullPointerException
     * @return a ResponseEntity with error details
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<InternalServerErrorResponse> handleNullPointerException(
            NullPointerException ex) {

        var response = new InternalServerErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * Handles NotFoundIdException.
     *
     * @param ex the NotFoundIdException
     * @return a ResponseEntity with error details
     */
    @ExceptionHandler(NotFoundIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NotFoundResponse> handleNotFoundIdException(
            NotFoundIdException ex) {
        var response = new NotFoundResponse(
                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles NotFoundArgumentException.
     *
     * @param ex the NotFoundArgumentException
     * @return a ResponseEntity with error details
     */
    @ExceptionHandler(NotFoundArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NotFoundResponse> handleNotFoundArgumentException(
            NotFoundArgumentException ex) {
        var response = new NotFoundResponse(
                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
