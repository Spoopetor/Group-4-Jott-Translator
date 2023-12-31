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
        stringBuilder.append(switch (functionReturn){
            case VOID: yield "Void";
            case INTEGER: yield "Integer";
            case DOUBLE: yield "Double";
            case STRING: yield "String";
            case BOOLEAN: yield "Boolean";
        });
        stringBuilder.append(" {\n");
        stringBuilder.append(functionBody.convertToJott());
        stringBuilder.append("}\n");

        return stringBuilder.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public static ");
        stringBuilder.append(switch (functionReturn){
            case VOID: yield "void ";
            case INTEGER: yield "int ";
            case DOUBLE: yield "double ";
            case STRING: yield "String ";
            case BOOLEAN: yield "bool ";
        });
        stringBuilder.append(funcName.convertToJava(className));
        stringBuilder.append("(");
        if (!funcName.getTokenName().equals("main")) {
            for (int i = 0; i < this.defParams.size(); i++) {
                stringBuilder.append(this.defParams.get(i).convertToJava(className));
                if (i + 1 < this.defParams.size()) {
                    stringBuilder.append(", ");
                }
            }
        }
        else {
            stringBuilder.append("String[] args");
        }
        stringBuilder.append(") {");
        stringBuilder.append(functionBody.convertToJava(className));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder stringBuilder = new StringBuilder();
        if(!funcName.getTokenName().equals("main")){
            stringBuilder.append(switch (functionReturn){
                case VOID: yield "void ";
                case INTEGER: yield "int ";
                case DOUBLE: yield "double ";
                case STRING: yield "char* ";
                case BOOLEAN: yield "bool ";
            });
        }
        else {
            stringBuilder.append("int ");
        }

        stringBuilder.append(funcName.convertToC());
        stringBuilder.append("(");

        if (this.defParams.size() > 0) {
            for (int i = 0; i < this.defParams.size(); i++) {
                stringBuilder.append(this.defParams.get(i).convertToC());
                if (i + 1 < this.defParams.size()) {
                    stringBuilder.append(", ");
                }
            }
        }
        else {
            stringBuilder.append("void");
        }
        stringBuilder.append("){");
        stringBuilder.append(functionBody.convertToC());
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("def ");
        stringBuilder.append(funcName.convertToPython());
        stringBuilder.append("(");

        for (int i = 0; i < this.defParams.size(); i++) {
            stringBuilder.append(this.defParams.get(i).convertToPython());
            if (i + 1 < this.defParams.size()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("):\n");
        ProgramNode.depth += 1;
        stringBuilder.append(functionBody.convertToPython());
        ProgramNode.depth -= 1;
        return stringBuilder.toString();
    }

    @Override
    public boolean validateTree() {
        SymbolTable.setCurrentScope(this.funcName.getTokenName());
        if (!ProgramNode.builtInFuncs.contains(funcName.getTokenName())) {
            SymbolTable.returnMap.put(funcName.getTokenName(), functionReturn);
        }
        else {
            throw new SemanticException(
                    "Cannot redefine built-in function \"" + this.funcName.getTokenName() + "\".",
                    this.funcName.getTokenFilename(),
                    this.funcName.getTokenLine()
            );
        }

        if (this.functionBody.getReturnType() != this.functionReturn){
            throw new SemanticException(
                    "Return does not match expected return type",
                    this.funcName.getTokenFilename(),
                    this.funcName.getTokenLine()
            );
        }
        else {
            if (this.funcName.getTokenName().equals("main")){
                if (this.functionReturn != Types.VOID) {
                    throw new SemanticException(
                            "Main should return Void",
                            this.funcName.getTokenFilename(),
                            this.funcName.getTokenLine()
                    );
                }
            }
            ArrayList<String> paramNames = new ArrayList<>();
            for (FuncDefParamsNode defParams: this.defParams){
                if (paramNames.contains(defParams.getDefParamName().getTokenName())){
                    throw new SemanticException(
                            "Parameters must have unique names",
                            this.funcName.getTokenFilename(),
                            this.funcName.getTokenLine()
                    );
                }
                paramNames.add(defParams.getDefParamName().getTokenName());
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
