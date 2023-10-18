package nodes;

import provided.JottTree;
import provided.TokenType;

import java.util.ArrayList;

public class FuncParamsNode implements JottTree {
    private final ArrayList<ExpressionNode> paramNames; // TODO pull in code for ExpressionNode

    public FuncParamsNode(ArrayList<ExpressionNode> pNames) {
        this.paramNames = pNames;
    }

    public static FuncParamsNode parseFuncParamsNode(ArrayList<provided.Token> tokens) {
        if (tokens.get(0).getTokenType() == TokenType.R_BRACE)
            return new FuncParamsNode(new ArrayList<ExpressionNode>());

        ArrayList<ExpressionNode> pNames = new ArrayList<ExpressionNode>();
        while (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            pNames.add(ExpressionNode.parseExpressionNode(tokens));
        }
        return new FuncParamsNode(pNames);
    }

    @Override
    public String convertToJott() {
        if (paramNames.isEmpty())
            return "";

        StringBuilder output = new StringBuilder();
        output.append(paramNames.get(0).convertToJott());
        for (int i = 1; i < paramNames.size(); i++) {
            output.append(",");
            output.append(paramNames.get(i).convertToJott());
        }
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