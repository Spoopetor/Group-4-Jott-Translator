package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class IdNode extends ExpressionNode{

    private Token token;

    public IdNode(Token t){
        this.token = t;
    }

    static public IdNode parseIdNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            Token tok = tokens.remove(0);
            throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        return new IdNode(tokens.remove(0));
    }

    public String getTokenName() {
        return token.getToken();
    }

    public String getTokenFilename() { return token.getTokenFilename(); }

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
}
