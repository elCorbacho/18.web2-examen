package ipss.web2.examen.exceptions;

/**
 * Excepción lanzada cuando se intenta acceder a un endpoint que no existe
 * Status HTTP: 404 NOT FOUND
 */
public class EndpointNotFoundException extends RuntimeException {
    
    private String method;
    private String path;
    
    public EndpointNotFoundException(String method, String path) {
        super(String.format("Endpoint no encontrado: %s %s", method, path));
        this.method = method;
        this.path = path;
    }
    
    public EndpointNotFoundException(String message) {
        super(message);
    }
    
    public String getMethod() {
        return method;
    }
    
    public String getPath() {
        return path;
    }
}
