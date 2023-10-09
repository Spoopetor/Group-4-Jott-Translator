package nodes;

import provided.JottTree;
import provided.Token;
import exceptions.*;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefNode implements JottTree {

    private ArrayList<JottTree> children;

    public FuncDefNode(ArrayList<JottTree> childList){
        try {
            children.addAll(childList);
        } catch (NullPointerException ignored) {
            ;
        }
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

    public static FuncDefNode parseFuncDefNode(ArrayList<Token> tokens) {

        // ArrayList.remove(0) essentially acts like pop.
        // Removes first element, shifts everything else up
        ArrayList<JottTree> childNodeList = new ArrayList<>();
        Token currToken = tokens.remove(0);

        if (currToken.getToken().equals("def")) {
            // The token after def will be the function name
            childNodeList.add(IdNode.parseIdNode(tokens));
            currToken = tokens.remove(0);

            // Next token after parsing the function id should be a left bracket
            if (currToken.getTokenType() == TokenType.L_BRACKET){
                // Parse the function params present after the left bracket
                childNodeList.addAll(FuncDefParamsNode.parseFuncDefParamsNode(tokens));
                currToken = tokens.remove(0);

                // Next token after parsing the function def params should be right bracket
                if (currToken.getTokenType() == TokenType.R_BRACKET) {
                    currToken = tokens.remove(0);

                    // Next token after the right bracket should be a colon
                    if (currToken.getTokenType() == TokenType.COLON){
                        // Parse the function return type
                        childNodeList.add(TypeNode.parseTypeNode(tokens));
                        currToken = tokens.remove(0);

                        // Next token after parsing function return type should be '{'
                        if (currToken.getTokenType() == TokenType.L_BRACE){
                            // Parse the body of this function
                            childNodeList.add(BodyNode.parseBodyNode(tokens));
                            currToken = tokens.remove(0);

                            // Next token after parsing body should be '}'
                            if (currToken.getTokenType() != TokenType.R_BRACE) {
                                FuncDefNode.throwParseEx(currToken);
                            }
                        }
                        else {
                            FuncDefNode.throwParseEx(currToken);
                        }
                    }
                    else {
                        FuncDefNode.throwParseEx(currToken);
                    }
                }
                else {
                    FuncDefNode.throwParseEx(currToken);
                }
            }
            else {
                FuncDefNode.throwParseEx(currToken);
            }
        }
        else {
            FuncDefNode.throwParseEx(currToken);
        }

        return new FuncDefNode(childNodeList);
    }

    private static void throwParseEx(Token token){
        throw new SyntaxException(
                token.getToken(),
                token.getFilename(),
                token.getLineNum()
        );
    }
}
