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
        // TODO this could be moved elsewhere if needed
        // Automatically add built-in functions to scope map
        // Note: print accepts more than just string args; FuncCallNode will ensure that arg is simply non-Void
        SymbolTable.scopeMap.put("string", new ArrayList<>(Arrays.asList(
                new Symbol("nonVoid1", Types.STRING, null, true))));
        SymbolTable.scopeMap.put("concat", new ArrayList<>(Arrays.asList(
                new Symbol("string1", Types.STRING, null, true),
                new Symbol("string2", Types.STRING, null, true))));
        SymbolTable.scopeMap.put("length", new ArrayList<>(Arrays.asList(
                new Symbol("string1", Types.STRING, null, true))));
        // Automatically add built-in functions to return map
        // FuncDefNode handles this for user-defined functions
        SymbolTable.returnMap.put("string", Types.VOID);
        SymbolTable.returnMap.put("concat", Types.STRING);
        SymbolTable.returnMap.put("length", Types.INTEGER);

        try {
            if (!SymbolTable.checkForMain()) {
                throw new SemanticException(
                        "No main function in file",
                        funcDefs.get(0).getFuncName().getTokenFilename(),
                        1);
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
