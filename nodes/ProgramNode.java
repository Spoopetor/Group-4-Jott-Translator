package nodes;

import provided.JottTree;
import provided.Token;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgramNode implements JottTree {

    private ArrayList<JottTree> children = new ArrayList<>();

    public ProgramNode(ArrayList<JottTree> childList){
        try {
            this.children.addAll(childList);
        } catch (NullPointerException ignored) {
            ;
        }
    }

    @Override
    public String convertToJott() {
        StringBuilder stringBuilder = new StringBuilder();
        for (JottTree node: children){
            stringBuilder.append(node.convertToJott());
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
        ArrayList<JottTree> funcDefNodes = new ArrayList<>();
        while (!tokens.isEmpty()){
            funcDefNodes.add(FuncDefNode.parseFuncDefNode(tokens));
        }
        return new ProgramNode(funcDefNodes);
    }
}
