package nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.Types;

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
            if(b instanceof FuncCallNode){
                out.append(";");
            }
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

    public Types getReturnType(){
        return returnNode.getType();
    }

    @Override
    public boolean validateTree() {

        for(BodyStmtNode b : bodyStatements){
            if(!b.validateTree()){
                return false;
            }
        }

        return returnNode.validateTree();
    }
    public static BodyNode parseBodyNode(ArrayList<Token> tokens){
        ArrayList<BodyStmtNode> bodies = new ArrayList<>();
        while(!tokens.isEmpty()){
            if (tokens.get(0).getToken().equals("return") || tokens.get(0).getTokenType() == TokenType.R_BRACE)
                break;
        //while(!((tokens.get(0).getToken().equals("return") || tokens.get(0).getTokenType() == TokenType.R_BRACE))){
            bodies.add(BodyStmtNode.parseBodyStmtNode(tokens));
        }
        ReturnStmtNode re;
        if (!tokens.isEmpty()) {
            re = ReturnStmtNode.parseReturnStmtNode(tokens);
            return new BodyNode(bodies, re);
        }
        return null;
    }
}
