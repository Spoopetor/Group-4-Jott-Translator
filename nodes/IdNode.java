package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;
import provided.Types;
import provided.SymbolTable;

import java.util.ArrayList;

public class IdNode extends ExpressionNode{

    private Token token;
    //private Types type; // look up type from sym table

    public IdNode(Token t){
        this.token = t;
    }

    static public IdNode parseIdNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting ID name, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        return new IdNode(tokens.remove(0));
    }

    public String getTokenName() {
        return token.getToken();
    }
    public Types getType(){return type;}

    public String getTokenFilename() { return token.getFilename(); }

    public int getTokenLine() { return token.getLineNum(); }

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
        // if defined in sym table return true
        String scope = SymbolTable.getCurrentScope();
        if (!SymbolTable.checkInScope(scope, token.getToken())) {
            throw SemanticException("Variable " + id.getTokenName() + " is not defined in this scope", this.token.getFilename(), this.token.getLineNum());
        }
        return true;
    }
}
