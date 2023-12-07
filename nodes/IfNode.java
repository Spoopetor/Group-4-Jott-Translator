package nodes;

import exceptions.*;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class IfNode extends BodyStmtNode implements JottTree {

    private ExpressionNode expr;
    private BodyNode body;
    private ArrayList<ElseifNode> elifLst;
    private ElseNode elseCase;

    private String filename;

    private int linenum;


    private IfNode(ExpressionNode e, BodyNode b, ArrayList<ElseifNode> elif, ElseNode el, String f, int l){
        this.expr = e;
        this.body = b;
        this.elifLst = elif;
        this.elseCase = el;
        this.filename = f;
        this.linenum = l;
    }

    public Types getReturnType(){
        for(ElseifNode el : elifLst){
            if((el.getReturnType() != Types.VOID) && (el.getReturnType() != body.getReturnType())){
                throw new SemanticException("Not all ElseIf in If statement return same type OR VOID", filename, linenum);
            }
        }
        if (elseCase != null) {
            if ((elseCase.getReturnType() != Types.VOID) && (elseCase.getReturnType() != body.getReturnType())) {
                throw new SemanticException("Else in If statement does not return same type OR VOID", filename, linenum);
            }
        }
        return body.getReturnType();
    }

    public boolean allReturn(){

        for (ElseifNode el : elifLst){
            if (el.getReturnType() == Types.VOID){
                return false;
            }
        }
        if(elseCase != null && elseCase.getReturnType() == Types.VOID){
            return false;
        }

        return body.getReturnType() != Types.VOID;
    }

    @Override
    public String convertToJott() {
        StringBuilder out = new StringBuilder();
        out.append("if[");
        out.append(this.expr.convertToJott());
        out.append("]{\n");
        out.append(this.body.convertToJott());
        out.append("}");
        for(ElseifNode i: this.elifLst) {
            out.append(i.convertToJott());
        }
        if (this.elseCase != null) {
            out.append(this.elseCase.convertToJott());
        }
        return out.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder out = new StringBuilder();
        out.append("if(");
        out.append(this.expr.convertToJava(className));
        out.append("){\n");
        out.append(this.body.convertToJava(className));
        out.append("}");
        for(ElseifNode i: this.elifLst) {
            out.append(i.convertToJava(className));
        }
        if (this.elseCase != null) {
            out.append(this.elseCase.convertToJava(className));
        }
        return out.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder out = new StringBuilder();
        out.append("if (");
        out.append(this.expr.convertToC());
        out.append("){\n");
        out.append(this.body.convertToC());
        out.append("}");
        for(ElseifNode i: this.elifLst) {
            out.append(i.convertToC());
        }
        if (this.elseCase != null) {
            out.append(this.elseCase.convertToC());
        }
        return out.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder out = new StringBuilder();
        out.append("\t".repeat(ProgramNode.depth));
        out.append("if (");
        out.append(this.expr.convertToPython().trim());
        out.append("):\n");
        ProgramNode.depth++;
        out.append(this.body.convertToPython());
        ProgramNode.depth--;

        for(ElseifNode i: this.elifLst) {
            out.append(i.convertToPython());
        }
        if (this.elseCase != null) {
            out.append(this.elseCase.convertToPython());
        }
        return out.toString();
    }

    @Override
    public boolean validateTree() {

        if(expr.getType() != Types.BOOLEAN){
            throw(new SemanticException("If condition not boolean", filename, linenum));
        }
        if(!body.validateTree()){
            return false;
        }

        if (!expr.validateTree()) {
            return false;
        }

        for(ElseifNode e : elifLst) {
            e.setHasIfParent(true);
            if (!e.validateTree()) {
                return false;
            }
        }
        if (elseCase == null) {
            return true;
        }
        elseCase.setHasIfParent(true);
        return elseCase.validateTree();
    }

    public static IfNode parseIfNode(ArrayList<Token> tokens){
        if (tokens.get(0).getToken().equals("if")){ //check for "if"
            Token iftoken = tokens.remove(0); // pop "if"



            if (tokens.get(0).getTokenType() == TokenType.L_BRACKET){ //check for "["
                tokens.remove(0); // pop "["

                ExpressionNode exprNode = ExpressionNode.parseExpressionNode(tokens);

                if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) { //check for "]"
                    tokens.remove(0); // pop "]"

                    if (tokens.get(0).getTokenType() == TokenType.L_BRACE) { //check for "{"
                        tokens.remove(0); // pop "{"

                        BodyNode bodyNode = BodyNode.parseBodyNode(tokens);

                        if (tokens.get(0).getTokenType() == TokenType.R_BRACE) { //check for "}"
                            tokens.remove(0); // pop "}"

                            ArrayList<ElseifNode> elifs = new ArrayList<ElseifNode>();
                            while (tokens.get(0).getToken().equals("elseif")) {//get all elseif nodes
                                elifs.add(ElseifNode.parseElseifNode(tokens));
                            }
                            ElseNode el = ElseNode.parseElseNode(tokens);

                            return new IfNode(exprNode, bodyNode, elifs, el, iftoken.getFilename(), iftoken.getLineNum());
                        } else {
                            throw new SyntaxException("Missing closing }", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
                        }
                    } else {
                        throw new SyntaxException("Expected { after []", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
                    }
                } else{
                    throw new SyntaxException("Missing closing ]", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
                }
            } else{
                throw new SyntaxException("Expected [ after if", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        } else{
            throw new SyntaxException("Expected if statement", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

    }
}
