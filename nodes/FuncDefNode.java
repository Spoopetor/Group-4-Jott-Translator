package nodes;

import provided.*;
import exceptions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FuncDefNode implements JottTree {

    private IdNode funcName;

    private ArrayList<FuncDefParamsNode> defParams;

    private Types functionReturn;

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
        stringBuilder.append(functionReturn.toString());
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
        if (this.functionBody.getReturnType() != this.functionReturn){
            throw new Exception; //TODO - Better exception!
        }
        else {
            if (!this.funcName.validateTree()) {
                return false;
            }
            for (FuncDefParamsNode defParams: this.defParams){
                if (!defParams.validateTree()){
                    return false;
                }
            }
            return this.functionBody.validateTree();
        }
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
            SymbolTable.setCurrentScope(funcDef.funcName.getTokenName());
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
                        switch (currToken.getToken().toUpperCase()){
                            case "VOID":
                                funcDef.functionReturn = Types.VOID;
                                break;
                            case "INTEGER":
                                funcDef.functionReturn = Types.INTEGER;
                                break;
                            case "DOUBLE":
                                funcDef.functionReturn = Types.DOUBLE;
                                break;
                            case "STRING":
                                funcDef.functionReturn = Types.STRING;
                                break;
                            case "BOOLEAN":
                                funcDef.functionReturn = Types.BOOLEAN;
                                break;
                            default:
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
                            SymbolTable.setCurrentScope(null);
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
