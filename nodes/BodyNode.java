package nodes;

import exceptions.SemanticException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class BodyNode implements JottTree {

    private ArrayList<BodyStmtNode> bodyStatements;
    private ReturnStmtNode returnNode;

    private String filename;
    private int linenum;

    private BodyNode(ArrayList<BodyStmtNode> bodies, ReturnStmtNode r, String f, int l){
        this.bodyStatements = bodies;
        this.returnNode = r;
        this.filename = f;
        this.linenum = l;
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

    public boolean mustReturn(){

        for(BodyStmtNode b : bodyStatements){
            if(b instanceof IfNode){
                IfNode i = (IfNode) b;
                if(i.allReturn()){
                   return false;
                }
            }
        }
        return true;
    }

    public Types getReturnType(){

        Types whileReturn = Types.VOID;
        Types ifReturn = Types.VOID;

        for(BodyStmtNode b : bodyStatements){

            if(b instanceof WhileNode){
                WhileNode w = (WhileNode) b;
                if(w.getBody().getReturnType() != Types.VOID){
                    if(returnNode.getType() != w.getBody().getReturnType()){
                        throw new SemanticException("Not all returns in body have same type", filename, linenum);
                    }else{
                        whileReturn = w.getBody().getReturnType();
                    }
                }
            } else if(b instanceof IfNode){
                IfNode i = (IfNode) b;
                ifReturn = i.getReturnType();
            }
        }
        if (returnNode.getType() != Types.VOID) {
            if ((whileReturn == Types.VOID || (whileReturn == returnNode.getType())) && (ifReturn == Types.VOID || ifReturn == returnNode.getType())) {
                return returnNode.getType();
            }
        }
        else {
            return ifReturn;
        }
        throw new SemanticException("Not all returns in body have same type", filename, linenum);
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
            return new BodyNode(bodies, re, tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
        return null;
    }
}
