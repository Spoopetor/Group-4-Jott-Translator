package nodes;

import provided.JottTree;
import provided.TokenType;

import java.util.ArrayList;

public class FuncParamsNode implements JottTree {
    private final ArrayList<ExpressionNode> paramNames;

    public FuncParamsNode(ArrayList<ExpressionNode> pNames) {
        this.paramNames = pNames;
    }

    public static FuncParamsNode parseFuncParamsNode(ArrayList<provided.Token> tokens) {
        if (tokens.get(0).getTokenType() == TokenType.R_BRACKET)
            return new FuncParamsNode(new ArrayList<ExpressionNode>());

        ArrayList<ExpressionNode> pNames = new ArrayList<ExpressionNode>();
        while (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            pNames.add(ExpressionNode.parseExpressionNode(tokens));
            if (tokens.get(0).getTokenType() == TokenType.COMMA){
                tokens.remove(0);
            }
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
        if (paramNames.isEmpty())
            return "";

        StringBuilder output = new StringBuilder();
        output.append(paramNames.get(0).convertToJava(className));
        for (int i = 1; i < paramNames.size(); i++) {
            output.append(", ");
            output.append(paramNames.get(i).convertToJava(className));
        }
        return output.toString();
    }

    @Override
    public String convertToC() {

        if (paramNames.isEmpty())
            return "";

        StringBuilder output = new StringBuilder();
        output.append(paramNames.get(0).convertToC());
        for (int i = 1; i < paramNames.size(); i++) {
            output.append(", ");
            output.append(paramNames.get(i).convertToC());
        }
        return output.toString();
    }

    @Override
    public String convertToPython() {
        if (paramNames.isEmpty())
            return "";

        StringBuilder output = new StringBuilder();
        output.append(paramNames.get(0).convertToPython());
        for (int i = 1; i < paramNames.size(); i++) {
            output.append(", ");
            output.append(paramNames.get(i).convertToPython());
        }
        return output.toString();
    }

    @Override
    public boolean validateTree() {
        for (ExpressionNode param : paramNames) {
            if (!param.validateTree()) { return false; }
        }
        return true;
    }

    public int getParamsLength() {return this.paramNames.size();}

    public ArrayList<ExpressionNode> getParamNames() {return this.paramNames;}
}
