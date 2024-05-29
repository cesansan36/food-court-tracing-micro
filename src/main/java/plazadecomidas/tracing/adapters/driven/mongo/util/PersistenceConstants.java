package plazadecomidas.tracing.adapters.driven.mongo.util;

public class PersistenceConstants {

    private PersistenceConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DOCUMENT_NOT_FOUND_MESSAGE = "The document you are trying to get couldn't be found";
}
