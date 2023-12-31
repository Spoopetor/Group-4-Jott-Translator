package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.util.ArrayList;

public class ElseNode implements JottTree {

    private BodyNode bodyNode;
    private Boolean hasIfParent;
    private Token fileInfo;

    public ElseNode (BodyNode bodyNode, Token fileInfo) {
        this.bodyNode = bodyNode;
        this.fileInfo = fileInfo;
    }

    public Types getReturnType(){
        return this.bodyNode.getReturnType();
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
        if (this.bodyNode == null) {
            return "";
        } else {
            StringBuilder str = new StringBuilder();
            str.append("else {");
            str.append(this.bodyNode.convertToJava(className));
            str.append("}");
            return str.toString();
        }
    }

    @Override
    public String convertToC() {
        if (this.bodyNode == null) {
            return "";
        } else {
            StringBuilder str = new StringBuilder();
            str.append("else {");
            str.append(this.bodyNode.convertToC());
            str.append("}");
            return str.toString();
        }
    }

    @Override
    public String convertToPython() {
        if (this.bodyNode == null) {
            return "";
        } else {
            StringBuilder str = new StringBuilder();

            // append (num tabs) else
            for (int i = 0; i < ProgramNode.depth; i++) {
                str.append("\t");
            }
            str.append("else:");
            str.append("\n");

            // increase depth, convert bodyNode, decrease depth
            ProgramNode.depth += 1;
            str.append(this.bodyNode.convertToPython());
            ProgramNode.depth -= 1;

            str.append("\n");

            return str.toString();
        }
    }

    @Override
    public boolean validateTree() {

        // validate if else node is preceded by an if
        if (!this.getHasIfParent()) {
            throw new SemanticException("'else' without 'if'", fileInfo.getFilename(), fileInfo.getLineNum());
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
                throw new SyntaxException("Else statement missing left brace", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
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
                throw new SyntaxException("Else statement missing right brace", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
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
