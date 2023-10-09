package nodes;

import provided.JottTree;
import provided.Token;
import exceptions.*;

import java.util.ArrayList;

public class FuncDefNode implements JottTree {

    private ArrayList<JottTree> children;

    public FuncDefNode(ArrayList<JottTree> childList){
        try {
            children.addAll(childList);
        } catch (NullPointerException ignored) {
            ;
        }
    }

    @Override
    public String convertToJott() {
        return null;
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

    public static FuncDefNode parseFuncDefNode(ArrayList<Token> tokens) {
        ArrayList<JottTree> child_node_list = new ArrayList<>();

        // ArrayList.remove(0) essentially acts like pop.
        // Removes first element, shifts everything else up
        Token currToken = tokens.remove(0);

        if (currToken.getToken().equals("def")) {
            // This IdNode will be the function name
            // NOTE: I'm not responsible for IdNode, so this class doesn't exist yet (on this branch)
            // It's my assumption that it'll function like the other nodes
            // REMOVE THIS NOTE FOR MERGE
            child_node_list.add(IdNode.parseNode(tokens));
            currToken = tokens.remove(0);

            // Next token after parsing the function id should be '['
            if (currToken.getToken().equals("[")){
                // Parse the function params present after the '['
                child_node_list.addAll(FuncParamsNode.parseNode(tokens));
                currToken = tokens.remove(0);

                // Next token after parsing the function params should be ']'
                if (currToken.getToken().equals("]")) {
                    currToken = tokens.remove(0);

                    // Next token after ']' should be ':'
                    if (currToken.getToken().equals(":")){
                        // Parse the function return type
                        // NOTE: See note on IdNode above, same applies to TypeNode
                        // REMOVE THIS NOTE FOR MERGE
                        child_node_list.add(TypeNode.parseNode(tokens));
                        currToken = tokens.remove(0);

                        // Next token after parsing function return type should be '{'
                        if (currToken.getToken().equals("{")){
                            // Parse the body of this function
                            // NOTE: See note on IdNode above, same applies to BodyNode
                            // REMOVE THIS NOTE FOR MERGE
                            child_node_list.add(BodyNode.parseNode(tokens));
                            currToken = tokens.remove(0);

                            // Next token after parsing body should be '}'
                            if (!currToken.getToken().equals("}")) {
                                FuncDefNode.throwParseEx(currToken);
                            }
                        }
                        else {
                            FuncDefNode.throwParseEx(currToken);
                        }
                    }
                    else {
                        FuncDefNode.throwParseEx(currToken);
                    }
                }
                else {
                    FuncDefNode.throwParseEx(currToken);
                }
            }
            else {
                FuncDefNode.throwParseEx(currToken);
            }
        }
        else {
            FuncDefNode.throwParseEx(currToken);
        }

        return new FuncDefNode(child_node_list);
    }

    private static void throwParseEx(Token token){
        throw new SyntaxException(
                token.getToken(),
                token.getFilename(),
                token.getLineNum()
        );
    }
}
