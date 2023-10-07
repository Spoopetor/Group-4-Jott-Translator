package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FuncCallNode implements JottTree {
    private final IdNode funcName; // TODO pull in code for IdNode
    private final FuncParamsNode params;

    public FuncCallNode (IdNode fName, FuncParamsNode fp) {
        this.funcName = fName;
        this.params = fp;
    }

    public static FuncCallNode parseFuncCallNode(ArrayList<Token> tokens) {
        if (tokens.get(0).getTokenType() != TokenType.FC_HEADER)
            throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        tokens.remove(0);
        IdNode fName = IdNode.parseIdNode(tokens);
        if (tokens.get(0).getTokenType() != TokenType.L_BRACE)
            throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        tokens.remove(0);
        FuncParamsNode fp = FuncParamsNode.parseFuncParamsNode(tokens);
        if (tokens.get(0).getTokenType() != TokenType.R_BRACE)
            throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        tokens.remove(0);
        return new FuncCallNode(fName, fp);
    }

    @Override
    public String convertToJott() {
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
