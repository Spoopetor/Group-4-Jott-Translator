package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class NumberNode extends ExpressionNode{

    private Token token;
    private Types type;

    public NumberNode(Token t, Types ts){
        this.token = t;
        this.type = ts;
    }

    static public NumberNode parseNumberNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.NUMBER) {
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting number, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        Types t;
        if (tokens.get(0).getToken().contains(".")){
            t = Types.DOUBLE;
        }else{
            t = Types.INTEGER;
        }
        return new NumberNode(tokens.remove(0), t);
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
        if (type == Types.DOUBLE || type == Types.INTEGER) {
            return true;
        }
        return false;
    }

    public Types getNumType() {
        if (token.getToken().contains(".")) { return Types.DOUBLE; }
        else { return Types.INTEGER; }
    }
}
