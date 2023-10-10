package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class BinaryExpressionNode extends ExpressionNode{

    private ExpressionNode left;
    private ExpressionNode right;
    private Token op;

    public BinaryExpressionNode(ExpressionNode left, Token op, ExpressionNode right){
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public static BinaryExpressionNode parseBiExpNode(ArrayList<Token> tokens){
        ExpressionNode l = ExpressionNode.parseExpNode(tokens);

        if (tokens.get(0).getTokenType() != TokenType.MATH_OP || tokens.get(0).getTokenType() != TokenType.REL_OP) {
            Token tok = tokens.remove(0);
            throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        Token op = tokens.remove(0);

        ExpressionNode r = ExpressionNode.parseExpNode(tokens);
        return new BinaryExpressionNode(l, op, r);
    }

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
}
