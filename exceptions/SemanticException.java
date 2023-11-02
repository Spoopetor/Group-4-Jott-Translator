package exceptions;

public class SemanticException extends RuntimeException {
    public SemanticException(String errorMsg, String filename, int lineNumber) {
        super(String.format("Semantic Error:\n%s\n%s:%d\n", errorMsg, filename, lineNumber));
    }
}
