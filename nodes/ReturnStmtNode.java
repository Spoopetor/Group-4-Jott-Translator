package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {

    private boolean returns;
    private ExpressionNode expr;

    private ReturnStmtNode(ExpressionNode e, Boolean r){
        this.expr = e;
        this.returns = r;
    }

    @Override
    public String convertToJott() {
        StringBuilder out = new StringBuilder();
        if(this.returns){
            out.append("return ");
            out.append(this.expr.convertToJott());
            out.append(";");
        }
        return out.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder out = new StringBuilder();
        if(this.returns){
            out.append("return ");
            out.append(this.expr.convertToJava(className));
            out.append(";");
        }
        return out.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder out = new StringBuilder();
        if(this.returns){
            out.append("return ");
            out.append(this.expr.convertToC());
            out.append(";");
        }
        return out.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder out = new StringBuilder();
        if(this.returns){
            out.append("return ");
            out.append(this.expr.convertToPython());
        }
        return out.toString();
    }

    @Override
    public boolean validateTree() {
        if(returns){
            return expr.validateTree();
        }
        return true;
    }

    public Types getType() {
        if(!returns){
            return Types.VOID;
        }
        return expr.getType();
    }

    public static ReturnStmtNode parseReturnStmtNode(ArrayList<Token> tokens){
        if(tokens.get(0).getToken().equals("return")){ //Check for "return"
            tokens.remove(0);
            ExpressionNode e = ExpressionNode.parseExpressionNode(tokens);
            tokens.remove(0); // removing the semicolon
            return new ReturnStmtNode(e, true);

        }
        if(tokens.get(0).getTokenType() == TokenType.R_BRACE){
            return new ReturnStmtNode(null, false);
        }
        throw new SyntaxException("Missing or invalid return statement", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
    }
}
