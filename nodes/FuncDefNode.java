package nodes;

import provided.JottTree;
import provided.Token;
import exceptions.*;
import provided.TokenType;

import java.util.ArrayList;

public class FuncDefNode implements JottTree {

    private IdNode funcName;

    private ArrayList<FuncDefParamsNode> defParams;

    private TypeNode functionReturn;

    private BodyNode functionBody;

    @Override
    public String convertToJott() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("def ");
        stringBuilder.append(this.funcName.convertToJott());
        stringBuilder.append("[");
        int i = 1;
        for (FuncDefParamsNode defParam : this.defParams) {
            stringBuilder.append(defParam.convertToJott());
            i++;
            if (i <= this.defParams.size()){
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]:");
        stringBuilder.append(functionReturn.convertToJott());
        stringBuilder.append(" {\n");
        stringBuilder.append(functionBody.convertToJott());
        stringBuilder.append("}\n");

        return stringBuilder.toString();
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
        // ArrayList<JottTree> childNodeList = new ArrayList<>();
        FuncDefNode funcDef = new FuncDefNode();
        Token currToken = tokens.remove(0);

        if (currToken.getToken().equals("def")) {
            // The token after def will be the function name
            funcDef.funcName = IdNode.parseIdNode(tokens);
            currToken = tokens.remove(0);

            // Next token after parsing the function id should be a left bracket
            if (currToken.getTokenType() == TokenType.L_BRACKET){
                // Parse the function params present after the left bracket
                funcDef.defParams = FuncDefParamsNode.parseFuncDefParamsNode(tokens);
                currToken = tokens.remove(0);

                // Next token after parsing the function def params should be right bracket
                if (currToken.getTokenType() == TokenType.R_BRACKET) {
                    currToken = tokens.remove(0);

                    // Next token after the right bracket should be a colon
                    if (currToken.getTokenType() == TokenType.COLON){
                        // Parse the function return type
                        funcDef.functionReturn = TypeNode.parseTypeNode(tokens);
                        currToken = tokens.remove(0);

                        // Next token after parsing function return type should be '{'
                        if (currToken.getTokenType() == TokenType.L_BRACE){
                            // Parse the body of this function
                            funcDef.functionBody = BodyNode.parseBodyNode(tokens);
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

        return funcDef;
    }

    private static void throwParseEx(Token token){
        throw new SyntaxException(
                token.getToken(),
                token.getFilename(),
                token.getLineNum()
        );
    }
}
