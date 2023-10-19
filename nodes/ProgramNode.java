package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;

import java.io.SyncFailedException;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
        return false;
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
