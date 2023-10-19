package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class BodyNode implements JottTree {

    private ArrayList<BodyStmtNode> bodyStatements;
    private ReturnStmtNode returnNode;

    private BodyNode(ArrayList<BodyStmtNode> bodies, ReturnStmtNode r){
        this.bodyStatements = bodies;
        this.returnNode = r;
    }
    @Override
    public String convertToJott() {
        StringBuilder out = new StringBuilder();
        for(BodyStmtNode b : bodyStatements){
            out.append(b.convertToJott());
        }
        out.append(returnNode.convertToJott());
        return out.toString();
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
    public static BodyNode parseBodyNode(ArrayList<Token> tokens){
        ArrayList<BodyStmtNode> bodies = new ArrayList<>();
        while(!(tokens.get(0).getToken().equals("return"))){
            bodies.add(BodyStmtNode.parseBodyStmtNode(tokens));
        }
        ReturnStmtNode re = ReturnStmtNode.parseReturnStmtNode(tokens);
        return new BodyNode(bodies, re);
    }
}
