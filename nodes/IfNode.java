package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class IfNode extends BodyStmtNode implements JottTree {

    private ExpressionNode expr;
    private BodyNode body;
    private ArrayList<ElseifNode> elifLst;
    private ElseNode elseCase;


    private IfNode(ExpressionNode e, BodyNode b, ArrayList<ElseifNode> elif, ElseNode el){
        this.expr = e;
        this.body = b;
        this.elifLst = elif;
        this.elseCase = el;
    }

    @Override
    public String convertToJott() {
        StringBuilder out = new StringBuilder();
        out.append("if[");
        out.append(this.expr.convertToJott());
        out.append("]{\n");
        out.append(this.body.convertToJott());
        out.append("}");
        for(ElseifNode i: this.elifLst) {
            out.append(i.convertToJott());
        }
        out.append(this.elseCase.convertToJott());
        return out.toString();
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

    public static IfNode parseIfNode(ArrayList<Token> tokens){
        if (tokens.get(0).getToken().equals("if")){ //check for "if"
            tokens.remove(0); // pop "if"

            if (tokens.get(0).getTokenType() == TokenType.L_BRACKET){ //check for "["
                tokens.remove(0); // pop "["

                ExpressionNode exprNode = ExpressionNode.parseExpressionNode(tokens);

                if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) { //check for "]"
                    tokens.remove(0); // pop "]"

                    if (tokens.get(0).getTokenType() == TokenType.L_BRACE) { //check for "{"
                        tokens.remove(0); // pop "{"

                        BodyNode bodyNode = BodyNode.parseBodyNode(tokens);

                        if (tokens.get(0).getTokenType() == TokenType.R_BRACE) { //check for "}"
                            tokens.remove(0); // pop "}"

                            ArrayList<ElseifNode> elifs = new ArrayList<ElseifNode>();
                            while (tokens.get(0).getToken().equals("elseif")) {//get all elseif nodes
                                elifs.add(ElseifNode.parseElseifNode(tokens));
                            }
                            ElseNode el = ElseNode.parseElseNode(tokens);

                            return new IfNode(exprNode, bodyNode, elifs, el);
                        }
                    }
                }
            }
        }
        throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
    }
}
