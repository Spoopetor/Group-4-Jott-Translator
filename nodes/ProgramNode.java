package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.io.SyncFailedException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ProgramNode implements JottTree {

    private ArrayList<FuncDefNode> funcDefs = new ArrayList<>();

    public static ArrayList<String> builtInFuncs = new ArrayList<>(Arrays.asList("print", "concat", "length"));

    public ProgramNode(ArrayList<FuncDefNode> childList){
        this.funcDefs.addAll(childList);
    }

    public static int depth;

    @Override
    public String convertToJott() {
        StringBuilder stringBuilder = new StringBuilder();
        for (FuncDefNode funcDef: funcDefs){
            stringBuilder.append(funcDef.convertToJott());
        }
        return stringBuilder.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public class ");
        stringBuilder.append(className);
        stringBuilder.append(" {");
        for (FuncDefNode funcDef: funcDefs){
            stringBuilder.append(funcDef.convertToJava(className));
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#include <stdio.h>\n#include <string.h>\n#include <stdlib.h>\n\n");
        for (FuncDefNode funcDef: funcDefs){
            stringBuilder.append(funcDef.convertToC());
        }
        return stringBuilder.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder stringBuilder = new StringBuilder();
        ProgramNode.depth = 0;
        for (FuncDefNode funcDef: funcDefs){
            stringBuilder.append(funcDef.convertToPython());
        }
        stringBuilder.append("main()");
        return stringBuilder.toString();
    }

    @Override
    public boolean validateTree() {
        try {
            if (!SymbolTable.checkForMain()) {
                throw new SemanticException(
                        "No main function in file",
                        funcDefs.get(0).getFuncName().getTokenFilename(),
                        1);
            }
            ArrayList<String> duplicateFuncNameCheck = new ArrayList<>();
            for (FuncDefNode defNode: this.funcDefs) {
                if (duplicateFuncNameCheck.contains(defNode.getFuncName().getTokenName())) {
                    throw new SemanticException(
                            "Duplicate function declaration, cannot declare functions with the same name",
                            defNode.getFuncName().getTokenFilename(),
                            defNode.getFuncName().getTokenLine()
                    );
                }
                duplicateFuncNameCheck.add(defNode.getFuncName().getTokenName());
            }
            for (FuncDefNode defNode : this.funcDefs) {
                if (!defNode.validateTree()) {
                    return false;
                }
            }
        } catch (SemanticException s) {
            System.err.println(s.getMessage());
            return false;
        }
        return true;
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens){
        try {
            // Automatically add built-in functions to scope map
            // Note: print accepts more than just string args; FuncCallNode will ensure that arg is simply non-Void
            SymbolTable.scopeMap.put("print", new ArrayList<>(Arrays.asList(
                    new Symbol("nonVoid1", Types.STRING, null, true))));
            SymbolTable.scopeMap.put("concat", new ArrayList<>(Arrays.asList(
                    new Symbol("string1", Types.STRING, null, true),
                    new Symbol("string2", Types.STRING, null, true))));
            SymbolTable.scopeMap.put("length", new ArrayList<>(Arrays.asList(
                    new Symbol("string1", Types.STRING, null, true))));
            // Automatically add built-in functions to return map
            // FuncDefNode handles this for user-defined functions
            SymbolTable.returnMap.put("print", Types.VOID);
            SymbolTable.returnMap.put("concat", Types.STRING);
            SymbolTable.returnMap.put("length", Types.INTEGER);

            ArrayList<FuncDefNode> funcDefNodes = new ArrayList<>();
            while (!tokens.isEmpty()) {
                funcDefNodes.add(FuncDefNode.parseFuncDefNode(tokens));
            }
            return new ProgramNode(funcDefNodes);
        } catch (SyntaxException s) {
            System.err.println(s.getMessage());
            return null;
        }
    }
}
