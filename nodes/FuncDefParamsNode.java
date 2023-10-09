package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefParamsNode implements JottTree {

    private ArrayList<JottTree> children = new ArrayList<>();

    public FuncDefParamsNode(ArrayList<JottTree> childList){
        try {
            this.children.addAll(childList);
        } catch (NullPointerException ignored) {
            ;
        }
    }

    @Override
    public String convertToJott() {
        return children.get(0).convertToJott() + ":" + children.get(1).convertToJott();
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

    public static ArrayList<FuncDefParamsNode> parseFuncDefParamsNode(ArrayList<Token> tokens) {
        ArrayList<FuncDefParamsNode> defParamsNodes = new ArrayList<>();
        ArrayList<JottTree> childNodeList = new ArrayList<>();

        Token currToken = tokens.get(0);
        if (currToken.getTokenType() != TokenType.ID_KEYWORD){
            // No params for the function def. FuncDefNode will handle this token,
            // which should be a right bracket
            return defParamsNodes;
        }
        while (true) {
            // Parse the id
            childNodeList.add(IdNode.parseIdNode(tokens));
            currToken = tokens.remove(0);

            // After id is parsed, next token should be a colon
            if (currToken.getTokenType() == TokenType.COLON) {
                childNodeList.add(TypeNode.parseTypeNode(tokens));
                defParamsNodes.add(new FuncDefParamsNode(childNodeList));
                currToken = tokens.get(0);

                // If the current token isn't a comma, then this is the end of the param list
                if (currToken.getTokenType() != TokenType.COMMA) {
                    return defParamsNodes;
                }

                // If it is a comma, remove it and loop to parse the next param
                tokens.remove(0);

            } else {
                throw new SyntaxException(
                        currToken.getToken(),
                        currToken.getFilename(),
                        currToken.getLineNum()
                );
            }
        }
    }
}
