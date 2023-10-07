package nodes;

import provided.JottTree;

import java.util.ArrayList;

public class FuncParamsNode implements JottTree {
    private ArrayList<ExpressionNode> paramNames;

    public FuncParamsNode(ArrayList<ExpressionNode> pNames) {
        this.paramNames = pNames;
    }

    @Override
    public String convertToJott() {
        if (paramNames.isEmpty())
            return "";
        else {
            StringBuilder output = new StringBuilder();
            output.append(paramNames.get(0).convertToJott());
            for (int i = 1; i < paramNames.size(); i++) {
                output.append(",");
                output.append(paramNames.get(i).convertToJott());
            }
            return output.toString();
        }
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
