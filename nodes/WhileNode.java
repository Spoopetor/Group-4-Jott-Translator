package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class WhileNode implements JottTree {

    private ExpressionNode exprNode;
    private BodyNode bodyNode;

    public WhileNode(ExpressionNode exprNode, BodyNode bodyNode) {
        this.exprNode = exprNode;
        this.bodyNode = bodyNode;
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        str.append("while[");
        str.append(this.exprNode.convertToJott());
        str.append("]{");
        str.append(this.bodyNode.convertToJott());
        str.append("}");
        return str.toString();
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

    static public WhileNode parseWhileNode(ArrayList<Token> tokens) {

        // if token == "while"
        if (tokens.get(0).getToken().equals("while")) {
            // remove while
            tokens.remove(0);

            // if no left bracket [, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left bracket
            tokens.remove(0);

            // remove next node and store in exprNode
            ExpressionNode exprNode = parseExpNode(tokens);

            // if no right bracket ], throw exception
            if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right bracket ]
            tokens.remove(0);

            // if no left brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left brace {
            tokens.remove(0);

            // store body in bodyNode
            BodyNode bodyNode = parseBodyNode(tokens);

            // if no right brace }, throw exception
            if (tokens.get(0).getTokenType() == TokenType.R_BRACE) {
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right brace
            tokens.remove(0);

            return new WhileNode(exprNode, bodyNode);

        } else {
            throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

    }
}