package nodes;

import exceptions.SyntaxException;
import provided.*;

import java.io.SyncFailedException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ProgramNode implements JottTree {

    private ArrayList<FuncDefNode> funcDefs = new ArrayList<>();

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
                new Symbol("nonVoid1", Types.STRING, "", true))));
        SymbolTable.scopeMap.put("concat", new ArrayList<>(Arrays.asList(
                new Symbol("string1", Types.STRING, "", true),
                new Symbol("string2", Types.STRING, "", true))));
        SymbolTable.scopeMap.put("length", new ArrayList<>(Arrays.asList(
                new Symbol("string1", Types.STRING, "", true))));
        // Automatically add built-in functions to return map
        SymbolTable.returnMap.put("string", Types.VOID);
        SymbolTable.returnMap.put("concat", Types.STRING);
        SymbolTable.returnMap.put("length", Types.INTEGER);
        // TODO add same functionality to FuncDefNode when it encounters returnTypes

        return false;
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens){
        try {
            ArrayList<FuncDefNode> funcDefNodes = new ArrayList<>();
            while (!tokens.isEmpty()) {
                funcDefNodes.add(FuncDefNode.parseFuncDefNode(tokens));
            }
            if (!SymbolTable.checkForMain()) {
                throw new Exception();
            }
            return new ProgramNode(funcDefNodes);
        } catch (SyntaxException s) {
            System.err.println(s.getMessage());
            return null;
        } catch (Exception e){
            // TODO -- add semantic ex?
            return null;
        }
    }
}
