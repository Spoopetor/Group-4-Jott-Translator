package nodes;

import provided.*;
import exceptions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class FuncDefNode implements JottTree {

    public IdNode getFuncName() {
        return funcName;
    }

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
        if (!ProgramNode.builtInFuncs.contains(funcName.getTokenName())) {
            SymbolTable.returnMap.put(funcName.getTokenName(), functionReturn);
        }

        if (this.functionBody.getReturnType() != this.functionReturn){
            throw new SemanticException(
                    "Return does not match expected return type",
                    this.funcName.getTokenFilename(),
                    this.funcName.getTokenLine()
            );
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
                                throw new SyntaxException(
                                        "Invalid return type definition",
                                        currToken.getFilename(),
                                        currToken.getLineNum()
                                );
                        }


                        currToken = tokens.remove(0);

                        // Next token after parsing function return type should be '{'
                        if (currToken.getTokenType() == TokenType.L_BRACE){
                            // Parse the body of this function
                            funcDef.functionBody = BodyNode.parseBodyNode(tokens);

                            if (tokens.isEmpty())
                                throw new SyntaxException(
                                        "Missing } token after function body",
                                        last.getFilename(),
                                        last.getLineNum()
                                );

                            currToken = tokens.remove(0);

                            // Next token after parsing body should be '}'

                            if (currToken.getTokenType() != TokenType.R_BRACE) {
                                throw new SyntaxException(
                                        "Missing } token after function body",
                                        currToken.getFilename(),
                                        currToken.getLineNum()
                                );
                            }

                            SymbolTable.setCurrentScope(null);
                        }
                        else {
                            throw new SyntaxException(
                                    "Missing { token after function definition",
                                    currToken.getFilename(),
                                    currToken.getLineNum()
                            );
                        }
                    }
                    else {
                        throw new SyntaxException(
                                "Missing : token after function params",
                                currToken.getFilename(),
                                currToken.getLineNum()
                        );
                    }
                }
                else {
                    throw new SyntaxException(
                            "Missing ] token after function params",
                            currToken.getFilename(),
                            currToken.getLineNum()
                    );
                }
            }
            else {
                throw new SyntaxException(
                        "Missing [ token after function name",
                        currToken.getFilename(),
                        currToken.getLineNum()
                );
            }
        }
        else {
            throw new SyntaxException(
                    "Missing def keyword to start function definition",
                    currToken.getFilename(),
                    currToken.getLineNum()
            );
        }

        return funcDef;
    }
}
