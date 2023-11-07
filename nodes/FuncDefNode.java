package nodes;

import provided.JottTree;
import provided.SymbolTable;
import provided.Token;
import exceptions.*;
import provided.TokenType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FuncDefNode implements JottTree {

    private IdNode funcName;

    private ArrayList<FuncDefParamsNode> defParams;

    private static String currentScope;

    public static String getCurrentScope() {
        return currentScope;
    }

    private Token functionReturn;
    private static final ArrayList<String> returnTypes =
            new ArrayList<>(Arrays.asList("DOUBLE", "INTEGER", "STRING", "BOOLEAN", "VOID"));

    private BodyNode functionBody;

    @Override
    public String convertToJott() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("def ");
        stringBuilder.append(this.funcName.convertToJott());
        stringBuilder.append("[");
        for (int i = 0; i < this.defParams.size(); i++){
            stringBuilder.append(this.defParams.get(i).convertToJott());
            if (i+1 < this.defParams.size()){
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]:");
        stringBuilder.append(functionReturn.getToken());
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

        Token last = tokens.get(tokens.size()-1);

        if (currToken.getToken().equals("def")) {
            // The token after def will be the function name
            funcDef.funcName = IdNode.parseIdNode(tokens);
            SymbolTable.createScope(funcDef.funcName.getTokenName());
            currentScope = funcDef.funcName.getTokenName();
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
                        currToken = tokens.remove(0);
                        if (returnTypes.contains(currToken.getToken().toUpperCase())) {
                            funcDef.functionReturn = currToken;
                        }
                        else {
                            FuncDefNode.throwParseEx(currToken);
                        }
                        currToken = tokens.remove(0);

                        // Next token after parsing function return type should be '{'
                        if (currToken.getTokenType() == TokenType.L_BRACE){
                            // Parse the body of this function
                            funcDef.functionBody = BodyNode.parseBodyNode(tokens);

                            if (tokens.isEmpty())
                                FuncDefNode.throwParseEx(last);

                            currToken = tokens.remove(0);

                            // Next token after parsing body should be '}'

                            if (currToken.getTokenType() != TokenType.R_BRACE) {
                                FuncDefNode.throwParseEx(currToken);
                            }

                            // SymbolTable.destroyScope(funcDef.funcName.getTokenName());
                            currentScope = null;
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
