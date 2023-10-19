package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public abstract class BodyStmtNode implements JottTree {

    @Override
    public String convertToJott() {
        return null;
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

    public static BodyStmtNode parseBodyStmtNode(ArrayList<Token> tokens){
        String bodyName = tokens.get(0).getToken();
        TokenType bodyType = tokens.get(0).getTokenType();

        if(bodyName.equals("if")){return IfNode.parseIfNode(tokens);}
        if(bodyName.equals("while")){return WhileNode.parseWhileNode(tokens);}
        if(bodyType == TokenType.FC_HEADER){
            FuncCallNode fc = FuncCallNode.parseFuncCallNode(tokens);
            if(tokens.get(0).getTokenType() == TokenType.SEMICOLON){
                tokens.remove(0); //remove following semicolon
                return fc; //return func call node
            }else{
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }
        if(tokens.size() >= 3) {
            if (bodyType == TokenType.ID_KEYWORD && tokens.get(1).getTokenType() == TokenType.ID_KEYWORD) {
                if (tokens.get(2).getTokenType() == TokenType.SEMICOLON) {
                    return VariableDeclarationNode.parseVariableDeclarationNode(tokens);
                }
            }
            if (bodyType == TokenType.ID_KEYWORD && (tokens.get(1).getTokenType() == TokenType.ASSIGN || tokens.get(2).getTokenType() == TokenType.ASSIGN)) {
                return AssignmentNode.parseAssignmentNode(tokens);
            }
        }
        throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());


    }
}
