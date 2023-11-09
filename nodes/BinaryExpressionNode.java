package nodes;

import exceptions.SyntaxException;
import provided.Token;
import provided.TokenType;
import provided.Types;

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

    public static BinaryExpressionNode parseBinaryExpressionNode(ArrayList<Token> tokens, ExpressionNode l){
        if (tokens.get(0).getTokenType() != TokenType.MATH_OP && tokens.get(0).getTokenType() != TokenType.REL_OP) {
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting operand, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        Token op = tokens.remove(0);

        ExpressionNode r = ExpressionNode.parseExpressionNode(tokens);


        return new BinaryExpressionNode(l, op, r);
    }

    public static BinaryExpressionNode parseBinaryExpressionNode(ArrayList<Token> tokens){
        ExpressionNode l;

        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            l = IdNode.parseIdNode(tokens);
        }
        else if (tokens.get(0).getTokenType() == TokenType.NUMBER) {
            l = NumberNode.parseNumberNode(tokens);
        }
        else{
            l = StringNode.parseStringNode(tokens);
        }


        if (tokens.get(0).getTokenType() != TokenType.MATH_OP && tokens.get(0).getTokenType() != TokenType.REL_OP) {
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting operand, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        Token op = tokens.remove(0);

        ExpressionNode r = ExpressionNode.parseExpressionNode(tokens);


        return new BinaryExpressionNode(l, op, r);
    }

    public Types getType(){
        if (left.getType() == right.getType())
            return left.getType();
        return null;
    }

    @Override
    public String convertToJott() {
        String output = left.convertToJott();
        output += op.getToken();
        output += right.convertToJott();
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
        if (left.validateTree() && right.validateTree() && left.getType() == right.getType())
            return true;
        return false;
    }
}
