package nodes;

import exceptions.SyntaxException;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.Types;

import java.util.ArrayList;
import java.util.Arrays;

//extend body class
abstract class ExpressionNode extends BodyStmtNode implements JottTree {

    static ArrayList<String> bool_keywords = new ArrayList<>(
            Arrays.asList("True", "False"));

    public abstract Types getType();

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
            FuncCallNode f = FuncCallNode.parseFuncCallNode(tokens);
            if (tokens.get(0).getTokenType() == TokenType.REL_OP || tokens.get(0).getTokenType() == TokenType.MATH_OP)
                return BinaryExpressionNode.parseBinaryExpressionNode(tokens, f);
            else
                return f;
        }

        if (tokens.get(0).getTokenType() == TokenType.STRING){
            return StringNode.parseStringNode(tokens);
        }

        throw new SyntaxException(tokens.get(0).getToken(), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
    }
}
