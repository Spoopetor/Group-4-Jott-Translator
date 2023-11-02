package exceptions;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String errorMsg, String filename, int lineNumber) {
        super(String.format("Syntax Error:\n%s\n%s:%d\n", errorMsg, filename, lineNumber));
    }
}
