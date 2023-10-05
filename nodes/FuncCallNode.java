package nodes;

import provided.JottTree;
import provided.Token;

import java.util.ArrayList;

public class FuncCallNode implements JottTree {
    private IdNode funcName; // TODO pull in code for IdNode
    private FuncParamsNode params;

    public FuncCallNode (IdNode fName, FuncParamsNode fp) {
        this.funcName = fName;
        this.params = fp;
    }
    public static FuncCallNode parseFuncCallNode(ArrayList<provided.Token> tokens) {

        return null; // TODO
    }
    @Override
    public String convertToJott() {   // TODO continue here
        StringBuilder output = new StringBuilder();
        output.append("::");
        output.append(funcName.convertToJott());
        output.append("[");
        output.append(params.convertToJott());
        output.append("]");
        return output.toString();
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

}
