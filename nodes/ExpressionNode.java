package nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

abstract class ExpressionNode implements JottTree {


    public static ExpressionNode parseExpNode(ArrayList<Token> tokens){
        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            if (tokens.get(1).getTokenType() == TokenType.REL_OP || tokens.get(1).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseExpNode(tokens);
            else
                return IdNode.parseIdNode(tokens);
        }

        if (tokens.get(0).getTokenType() == TokenType.NUMBER) {
            if (tokens.get(1).getTokenType() == TokenType.REL_OP || tokens.get(1).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseExpNode(tokens);
            else
                return NumberNode.parseNumberNode(tokens);
        }

        if (tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            if (tokens.get(1).getTokenType() == TokenType.REL_OP || tokens.get(1).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseExpNode(tokens);
            else
                //return FCHeaderNode.parseFCNode(tokens);
                return null;
        }

        if (tokens.get(0).getTokenType() == TokenType.STRING){
            return StringNode.parseStringNode(tokens);
        }

        return null;
    }
}
