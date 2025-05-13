package exceptions;

public class DocumentoMalFormatoException extends RuntimeException {
    public DocumentoMalFormatoException(String message) {
        super(message);
    }
}
