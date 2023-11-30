package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.util.ArrayList;

public class ElseifNode implements JottTree {

    private ExpressionNode exprNode;
    private BodyNode bodyNode;
    private Boolean hasIfParent;
    private Token fileInfo;

    public ElseifNode(ExpressionNode exprNode, BodyNode bodyNode, Token fileInfo) {
        this.exprNode = exprNode;
        this.bodyNode = bodyNode;
        this.fileInfo = fileInfo;
    }

    public Types getReturnType(){
        return this.bodyNode.getReturnType();
    }

    /**
     * Returns true if this elseif node has an
     * if statement as its parent
     * @return hasIfParent
     */
    public Boolean getHasIfParent() {
        return hasIfParent;
    }

    /**
     * Sets hasIfParent to true if an if statement
     * precedes this elseif node
     * @param hasIfParent
     */
    public void setHasIfParent(Boolean hasIfParent) {
        this.hasIfParent = hasIfParent;
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        str.append("elseif[");
        str.append(this.exprNode.convertToJott());
        str.append("] {");
        str.append(this.bodyNode.convertToJott());
        str.append("}");
        return str.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder str = new StringBuilder();
        str.append("else if (");
        str.append(this.exprNode.convertToJava(className));
        str.append(") {");
        str.append(this.bodyNode.convertToJava(className));
        str.append("}");
        return str.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder str = new StringBuilder();
        str.append("else if (");
        str.append(this.exprNode.convertToC());
        str.append(") {");
        str.append(this.bodyNode.convertToC());
        str.append("}");
        return str.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder str = new StringBuilder();
        str.append("\nelif ");
        str.append(this.exprNode.convertToPython());
        str.append(": ");
        str.append(this.bodyNode.convertToPython());
        return str.toString();
    }

    @Override
    public boolean validateTree() {

        // validate if elseif node is preceded by an if
        if (!getHasIfParent()) {
            throw new SemanticException("'elseif' without 'if'", fileInfo.getFilename(), fileInfo.getLineNum());
        }

        // if exprNode is not boolean condition, error
        if (!(exprNode instanceof BoolNode)) {
            throw new SemanticException("'elseif' without a boolean condition", fileInfo.getFilename(), fileInfo.getLineNum());
        }

        // if bodyNode is !validated, return false
        if (!bodyNode.validateTree()) {
            return false;
        }

        return true;
    }

    static public ElseifNode parseElseifNode(ArrayList<Token> tokens) {

        // token == "elseif"
        if (tokens.get(0).getToken().equals("elseif")) {
            // remove elseif
            tokens.remove(0);

            // if no left bracket [, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
                throw new SyntaxException("Elseif statement missing left bracket", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left bracket
            tokens.remove(0);

            Token fileInfo = null;
            if (!(tokens.isEmpty())) {
                fileInfo = tokens.get(0);
            }

            // remove next node and store in exprNode
            ExpressionNode exprNode = ExpressionNode.parseExpressionNode(tokens);

            // if no right bracket ], throw exception
            if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
                throw new SyntaxException("Elseif statement missing right bracket", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right bracket ]
            tokens.remove(0);

            // if no left brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
                throw new SyntaxException("Elseif statement missing left brace", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left brace {
            tokens.remove(0);

            // store body in bodyNode
            BodyNode bodyNode = BodyNode.parseBodyNode(tokens);

            // if no right brace, throw exception
            if (tokens.get(0).getTokenType() == TokenType.R_BRACE) {
                throw new SyntaxException("Elseif statement missing right brace", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right brace
            tokens.remove(0);

            return new ElseifNode(exprNode, bodyNode, fileInfo);

        } else {
            throw new SyntaxException("Missing elseif token", tokens.get(0).getToken(), tokens.get(0).getLineNum());
        }

    }
}
