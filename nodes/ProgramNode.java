package nodes;

import provided.JottTree;
import provided.Token;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgramNode implements JottTree {

    private ArrayList<JottTree> children;

    public ProgramNode(ArrayList<JottTree> childList){
        try {
            children.addAll(childList);
        } catch (NullPointerException ignored) {
            ;
        }
    }

    @Override
    public String convertToJott() {
        return null;
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
        ArrayList<JottTree> funcDefNodes = new ArrayList<>();
        while (!tokens.isEmpty()){
            funcDefNodes.add(FuncDefNode.parseFuncDefNode(tokens));
        }
        return new ProgramNode(funcDefNodes);
    }
}
