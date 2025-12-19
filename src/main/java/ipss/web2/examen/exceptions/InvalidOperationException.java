package ipss.web2.examen.exceptions;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida
 * Status HTTP: 400 BAD REQUEST o 422 UNPROCESSABLE ENTITY
 */
public class InvalidOperationException extends RuntimeException {
    
    private String errorCode;
    
    public InvalidOperationException(String message) {
        super(message);
    }
    
    public InvalidOperationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
