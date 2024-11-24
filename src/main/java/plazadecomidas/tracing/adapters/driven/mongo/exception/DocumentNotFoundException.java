package plazadecomidas.tracing.adapters.driven.mongo.exception;

public class DocumentNotFoundException extends RuntimeException{

    public DocumentNotFoundException(String message) {
        super(message);
    }
}
