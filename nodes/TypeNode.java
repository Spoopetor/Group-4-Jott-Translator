package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class TypeNode implements JottTree {
    public static final String DOUBLE = "Double";
    public static final String INTEGER = "Integer";
    public static final String STRING = "String";
    public static final String BOOLEAN = "Boolean";

    private final String typeName;

    public TypeNode(String tName) {
        this.typeName = tName;
    }

    public static TypeNode parseTypeNode(ArrayList<Token> tokens) {
        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            return switch (tokens.get(0).getToken()) {
                case DOUBLE -> {
                    tokens.remove(0);
                    yield new TypeNode(DOUBLE);
                }
                case INTEGER -> {
                    tokens.remove(0);
                    yield new TypeNode(INTEGER);
                }
                case STRING -> {
                    tokens.remove(0);
                    yield new TypeNode(STRING);
                }
                case BOOLEAN -> {
                    tokens.remove(0);
                    yield new TypeNode(BOOLEAN);
                }
                default ->
                        throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            };
        }
        else {
            throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
    }

    @Override
    public String convertToJott() {
        return typeName;
    }

    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
