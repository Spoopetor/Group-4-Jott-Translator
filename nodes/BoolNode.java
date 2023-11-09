package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class BoolNode extends ExpressionNode{

    private Token token;
    private Types type;

    public BoolNode(Token t, Types ts){
        this.token = t;
        this.type = ts;
    }

    static public BoolNode parseBoolNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting keyword, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        return new BoolNode(tokens.remove(0), Types.BOOLEAN);
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
        if (type == Types.BOOLEAN)
            return true;
        return false;
    }
}
