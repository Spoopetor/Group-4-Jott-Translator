package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;
import java.util.Arrays;

// change to extend body statement
public class VariableDeclarationNode implements JottTree {

    private TypeNode type;
    private IdNode id;


    public VariableDeclarationNode(TypeNode type, IdNode id){
        this.id = id;
        this.type = type;
    }

    static public VariableDeclarationNode parseVariableDeclarationNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD ||
                tokens.get(1).getTokenType() != TokenType.ID_KEYWORD ||
                tokens.get(2).getTokenType() != TokenType.SEMICOLON){
            Token tok = tokens.remove(0);
            throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        TypeNode t = TypeNode.parseTypeNode(tokens);
        IdNode i = IdNode.parseIdNode(tokens);

        //pop semicolon
        tokens.remove(0);

        return new VariableDeclarationNode(t,i);
    }

    @Override
    public String convertToJott() {
        String output = type.convertToJott();
        output += id.convertToJott();
        output += ';';
        return output;
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
