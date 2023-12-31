package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

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
        if (left.getType() == right.getType()) {
            if (op.getTokenType() == TokenType.REL_OP){
                return Types.BOOLEAN;
            }
            else if (op.getTokenType() == TokenType.MATH_OP){
                return left.getType();
            }
        }
        throw new SemanticException("Type mismatch", op.getFilename(), op.getLineNum());
    }

    @Override
    public String convertToJott() {
        String output = left.convertToJott();
        output += " ";
        output += op.getToken();
        output += " ";
        output += right.convertToJott();
        return output;
    }

    @Override
    public String convertToJava(String className) {
        String output = left.convertToJava(className);
        output += " ";
        output += op.getToken();
        output += " ";
        output += right.convertToJava(className);
        return output;
    }

    @Override
    public String convertToC() {
        String output = left.convertToC();
        output += " ";
        output += op.getToken();
        output += " ";
        output += right.convertToC();
        return output;
    }

    @Override
    public String convertToPython() {
        String output = left.convertToPython();
        output += " ";
        output += op.getToken();
        output += " ";
        output += right.convertToPython();
        return output;
    }

    @Override
    public boolean validateTree() {
        if (left.validateTree() && right.validateTree() && left.getType() == right.getType()) {
            if (left instanceof IdNode) {
                Symbol lSym = SymbolTable.getFromCurrentScope(((IdNode) left).getTokenName());
                if (!lSym.isParam() && lSym.getValue() == null) {
                    String varName = ((IdNode) left).getTokenName();
                    throw new SemanticException("Unitialized variable \"" + varName + "\" used in expression",
                            op.getFilename(),
                            op.getLineNum()
                    );
                }
            }
            if (right instanceof IdNode) {
                Symbol rSym = SymbolTable.getFromCurrentScope(((IdNode) right).getTokenName());
                if (!rSym.isParam() && rSym.getValue() == null) {
                    String varName = ((IdNode) right).getTokenName();
                    throw new SemanticException("Unitialized variable \"" + varName + "\" used in expression",
                            op.getFilename(),
                            op.getLineNum()
                    );
                }
            }
            return true;
        }
        throw new SemanticException("Type mismatch", op.getFilename(), op.getLineNum());
    }
}
