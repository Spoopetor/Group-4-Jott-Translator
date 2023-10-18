package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {

    private ExpressionNode expr;

    private ReturnStmtNode(ExpressionNode e){
        this.expr = e;
    }

    @Override
    public String convertToJott() {
        StringBuilder out = new StringBuilder();
        out.append("return ");
        out.append(this.expr.convertToJott());
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

    public static ReturnStmtNode parseReturnStmtNode(ArrayList<Token> tokens){
        if(tokens.get(0).getToken().equals("elseif")){ //Check for "return"
            tokens.remove(0);
            ExpressionNode e = ExpressionNode.parseExpressionNode(tokens);
            return new ReturnStmtNode(e);

        }
        throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
    }
}
