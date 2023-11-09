package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ElseNode implements JottTree {

    private BodyNode bodyNode;

    private Boolean hasIfParent;

    private Token fileInfo;

    public ElseNode (BodyNode bodyNode, Token fileInfo) {
        this.bodyNode = bodyNode;
        this.fileInfo = fileInfo;
    }

    @Override
    public String convertToJott() {
        if (this.bodyNode == null) {
            return "";
        } else {
            StringBuilder str = new StringBuilder();
            str.append("else{");
            str.append(this.bodyNode.convertToJott());
            str.append("}");
            return str.toString();
        }
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

        // validate if else node is preceded by an if
        if (!this.getHasIfParent()) {
            throw new SemanticException("'else' without 'if'", fileInfo.getFilename(), fileInfo.getLineNum());
            return false;
        }

        // if bodyNode is !validated, return false
        if (!bodyNode.validateTree()) {
            return false;
        }

        return true;
    }

    static public ElseNode parseElseNode(ArrayList<Token> tokens) {

        // if token == "ELSE"
        if (tokens.get(0).getToken().equals("else")) {
            // remove else
            tokens.remove(0);

            // if no left brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
                throw new SyntaxException("Else statement missing left brace", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left brace {
            tokens.remove(0);

            Token fileInfo = null;
            if (!(tokens.isEmpty())) {
                fileInfo = tokens.get(0);
            }

            // if no immediate right brace {, expect body
            BodyNode bodyNode = BodyNode.parseBodyNode(tokens);

            // if no right brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
                throw new SyntaxException("Else statement missing right brace", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right brace {
            tokens.remove(0);

            return new ElseNode(bodyNode, fileInfo);

        } else {
            // no else for this if statement
            return null;
        }

    }
}
