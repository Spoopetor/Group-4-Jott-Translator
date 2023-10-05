package exceptions;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String invalidToken, String filename, int lineNumber) {
        super(String.format("Syntax Error\nInvalid token \"%s\"\n%s:%d\n", invalidToken, filename, lineNumber));
    }
}
