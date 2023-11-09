package nodes;

import exceptions.SyntaxException;
import exceptions.SemanticException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class WhileNode extends BodyStmtNode implements JottTree {

    private ExpressionNode exprNode;
    private BodyNode bodyNode;

    private Token fileInfo;

    public WhileNode(ExpressionNode exprNode, BodyNode bodyNode, Token fileInfo) {
        this.exprNode = exprNode;
        this.bodyNode = bodyNode;
        this.fileInfo = fileInfo;
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        str.append("while[");
        str.append(this.exprNode.convertToJott());
        str.append("]{");
        str.append(this.bodyNode.convertToJott());
        str.append("}");
        return str.toString();
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

        // if exprNode is not boolean condition, error
        if (!(exprNode instanceof BoolNode)) {
            throw new SemanticException("'while' without a condition", fileInfo.getFilename(), fileInfo.getLineNum());
            return false;
        }

        // if bodyNode is !validated, return false
        if (!bodyNode.validateTree()) {
            return false;
        }

        return true;
    }

    static public WhileNode parseWhileNode(ArrayList<Token> tokens) {

        // if token == "while"
        if (tokens.get(0).getToken().equals("while")) {
            // remove while
            tokens.remove(0);

            // if no left bracket [, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
                throw new SyntaxException("While statement missing left bracket", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left bracket
            tokens.remove(0);

            Token fileInfo = null;
            if (!(tokens.isEmpty())) {
                fileInfo = tokens.get(0);
            }

            // remove next node and store in exprNode
            ExpressionNode exprNode = ExpressionNode.parseExpressionNode(tokens);

            // if no right bracket ], throw exception
            if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
                throw new SyntaxException("While statement missing right bracket", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right bracket ]
            tokens.remove(0);

            // if no left brace {, throw exception
            if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
                throw new SyntaxException("While statement missing left brace", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove left brace {
            tokens.remove(0);

            // store body in bodyNode
            BodyNode bodyNode = BodyNode.parseBodyNode(tokens);

            // if no right brace }, throw exception
            if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
                throw new SyntaxException("While statement missing right brace", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

            // remove right brace
            tokens.remove(0);

            return new WhileNode(exprNode, bodyNode, fileInfo);

        } else {
            throw new SyntaxException("Missing while token", tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

    }
}
