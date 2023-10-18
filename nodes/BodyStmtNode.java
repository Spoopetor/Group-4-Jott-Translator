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
        String bodyType = tokens.get(0).getToken();

        switch (bodyType) {
            case "if" : return IfNode.parseIfNode(tokens);
            case "while" : return WhileNode.parseWhileNode(tokens);
            // todo Assigment Case
            // todo Variable Declaration Case
            // todo Function Call (make Expression Node extend BodyStmtNode)
            default : throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

    }
}
