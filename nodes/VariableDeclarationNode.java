package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.util.ArrayList;
import java.util.Arrays;

// change to extend body statement
public class VariableDeclarationNode extends BodyStmtNode implements JottTree {

    private TypeNode type;
    private IdNode id;


    public VariableDeclarationNode(TypeNode type, IdNode id){
        this.id = id;
        this.type = type;
    }

    static public VariableDeclarationNode parseVariableDeclarationNode(ArrayList<Token> tokens){
//        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD ||
//                tokens.get(1).getTokenType() != TokenType.ID_KEYWORD ||
//                tokens.get(2).getTokenType() != TokenType.SEMICOLON){
//            Token tok = tokens.remove(0);
//            throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
//        }
        TypeNode t = TypeNode.parseTypeNode(tokens);
        IdNode i = IdNode.parseIdNode(tokens);

        if (tokens.get(0).getTokenType() != TokenType.SEMICOLON){
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting ';', got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        // remove semi-colon
        tokens.remove(0);

        String scope = SymbolTable.getCurrentScope();

        if (SymbolTable.checkInScope(scope, i.getTokenName())) {
            Token tok = tokens.remove(0);
            throw new SemanticException("Variable " + i.getTokenName() + " is already defined in this scope", tok.getFilename(), tok.getLineNum());
        }
        SymbolTable.addToScope(scope, i.getTokenName(), t.getTypeName(), null);
        //pop semicolon
//        tokens.remove(0);

        return new VariableDeclarationNode(t,i);
    }

    @Override
    public String convertToJott() {
        String output = type.convertToJott();
        output += " ";
        output += id.convertToJott();
        output += ';';
        return output;
    }

    @Override
    public String convertToJava(String className) {
        String output = type.convertToJava(className);
        output += " ";
        output += id.convertToJava(className);
        output += ';';
        return output;
    }

    @Override
    public String convertToC() {
        String output = type.convertToC();
        output += " ";
        output += id.convertToC();
        output += ';';
        return output;
    }

    @Override
    public String convertToPython() {
        return "";
    }

    @Override
    public boolean validateTree() {
        if (id.validateTree() && type.validateTree())
            return true;
        return false;
    }
}
