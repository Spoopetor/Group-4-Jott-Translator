package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ElseNode implements JottTree {

    private BodyNode b;

    public ElseNode (BodyNode b) {
        this.bodyNode = b;
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

    static public ElseNode parseElseNode(ArrayList<Token> tokens) {

        // if token == ID_KEYWORD && is token == "ELSE"
        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equalsIgnoreCase("else")) {
            // remove else
            tokens.remove(0);

            // if no left brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left brace {
            tokens.remove(0);

            // if no immediate right brace {, expect body
            BodyNode bodyNode = null;
            if (tokens.get(0) != TokenType.R_BRACE) {
                // remove next token and store in bodyNode
                bodyNode = parseBodyNode(tokens.remove(0));
            }

            // if no right brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
                throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right brace {
            tokens.remove(0);

            return new ElseNode(bodyNode);

        } else {
            // no else for this if statement
            return new ElseNode(null);
        }

    }
}
