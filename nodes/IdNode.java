package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.util.ArrayList;

public class IdNode extends ExpressionNode{

    private Token token;

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
    public Types getType(){
        if (!SymbolTable.checkInScope(SymbolTable.getCurrentScope(), token.getToken())) {
            throw new SemanticException("Cannot find type for " + token.getToken() + " in current scope",
                    token.getFilename(), token.getLineNum());
        }
        for (Symbol symbol : SymbolTable.scopeMap.get(SymbolTable.getCurrentScope())) {
            if (symbol.getName().equals(token.getToken())) {
                return symbol.getType();
            }
        }
        throw new SemanticException("Cannot find type for " + token.getToken() + " in current scope",
                token.getFilename(), token.getLineNum());
    }

    public String getTokenFilename() { return token.getFilename(); }

    public int getTokenLine() { return token.getLineNum(); }

    @Override
    public String convertToJott() {
        return this.token.getToken();
    }
    @Override
    public String convertToJava(String className) {
        return this.token.getToken();
    }

    @Override
    public String convertToC() {
        return this.token.getToken();
    }

    @Override
    public String convertToPython() {
        return this.token.getToken();
    }

    @Override
    public boolean validateTree() {
        // if defined in sym table return true
        String scope = SymbolTable.getCurrentScope();
        if (!SymbolTable.checkInScope(scope, token.getToken())) {
            throw new SemanticException("Variable " + token.getToken() + " is not defined in this scope", this.token.getFilename(), this.token.getLineNum());
        }
        return true;
    }
}
