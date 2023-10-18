package nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;
import java.util.Arrays;

//extend body class
abstract class ExpressionNode implements JottTree {

    static ArrayList<String> bool_keywords = new ArrayList<>(
            Arrays.asList("True", "False"));

    public static ExpressionNode parseExpressionNode(ArrayList<Token> tokens){

        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            if (bool_keywords.contains(tokens.get(0).getToken()))
                return BoolNode.parseBoolNode(tokens);

            if (tokens.get(1).getTokenType() == TokenType.REL_OP || tokens.get(1).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseBinaryExpressionNode(tokens);
            else
                return IdNode.parseIdNode(tokens);
        }

        if (tokens.get(0).getTokenType() == TokenType.NUMBER) {
            if (tokens.get(1).getTokenType() == TokenType.REL_OP || tokens.get(1).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseBinaryExpressionNode(tokens);
            else
                return NumberNode.parseNumberNode(tokens);
        }

        if (tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            if (tokens.get(1).getTokenType() == TokenType.REL_OP || tokens.get(1).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseBinaryExpressionNode(tokens);
            else
                return FuncCallNode.parseFuncCallNode(tokens);
        }

        if (tokens.get(0).getTokenType() == TokenType.STRING){
            return StringNode.parseStringNode(tokens);
        }

        return null;
    }
}
