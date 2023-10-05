package nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

public class FuncDefNode implements JottTree {

    private ArrayList<JottTree> children;

    public FuncDefNode(ArrayList<JottTree> childList){
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

    public static FuncDefNode parseNode(ArrayList<Token> tokens) {
        return null;
    }
}
