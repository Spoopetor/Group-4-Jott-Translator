package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class NumberNode extends ExpressionNode{

    private Token token;

    public NumberNode(Token t){
        this.token = t;
    }

    static public NumberNode parseNumberNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.NUMBER) {
            Token tok = tokens.remove(0);
            throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        return new NumberNode(tokens.remove(0));
    }


    @Override
    public String convertToJott() {
        return this.token.getToken();
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

    public Types getNumType() {
        if (token.getToken().contains(".")) { return Types.DOUBLE; }
        else { return Types.STRING; }
    }
}
