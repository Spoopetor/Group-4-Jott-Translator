package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class StringNode extends ExpressionNode {

    private Token token;
    private Types type;

    public StringNode(Token t, Types ts) {
        this.token = t;
        this.type = ts;
    }


    static public StringNode parseStringNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.STRING) {
            Token tok = tokens.remove(0);
            throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        return new StringNode(tokens.remove(0), Types.STRING);
    }

    public Types getType(){return type;}

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
        if (type == Types.STRING)
            return true;
        return false;
    }
}
