package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.util.ArrayList;
import java.util.Arrays;

// change to extend body statement
public class AssignmentNode extends BodyStmtNode implements JottTree {

    static ArrayList<String> type_keywords = new ArrayList<>(
            Arrays.asList("Double", "Integer", "String", "Boolean"));

    static final ArrayList<String> all_keywords = new ArrayList<>(
            Arrays.asList(
                    "Void",
                    "Double",
                    "Integer",
                    "String",
                    "Boolean",
                    "while",
                    "if",
                    "elseif",
                    "else",
                    "return",
                    "print",
                    "concat",
                    "length"
            )
    );

    private TypeNode type = null;
    private IdNode id;
    private ExpressionNode value;

    private boolean reassignment;

    public AssignmentNode(TypeNode type, IdNode id, ExpressionNode value, boolean r){
        this.type = type;
        this.id = id;
        this.value = value;
        this.reassignment = r;
    }

    public AssignmentNode(IdNode id, ExpressionNode value){
        this.id = id;
        this.value = value;
    }

    static public AssignmentNode parseAssignmentNode(ArrayList<Token> tokens){

        Boolean var_dec = false;

        // validate type or id node type
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD ){
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting type or ID name, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        TypeNode t = null;
        // case 1 where assignment has var_dec
        if (type_keywords.contains(tokens.get(0).getToken())){
            t = TypeNode.parseTypeNode(tokens);
            var_dec = true;
            // validate if next is id node type
            if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD ){
                Token tok = tokens.remove(0);
                throw new SyntaxException("Expecting ID name, got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
                //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
            }
        }
        // case 2 when no var_dec / remainder of case 1
        IdNode id = IdNode.parseIdNode(tokens);

        // check if next is '=' (assign token type)
        if (tokens.get(0).getTokenType() != TokenType.ASSIGN){
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting '=', got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }
        tokens.remove(0);

        // parse will check if expr is valid
        ExpressionNode v = ExpressionNode.parseExpressionNode(tokens);

        // check if next is semicolon
        if (tokens.get(0).getTokenType() != TokenType.SEMICOLON){
            Token tok = tokens.remove(0);
            throw new SyntaxException("Expecting ';', got " + tok.getToken(), tok.getFilename(), tok.getLineNum());
            //throw new SyntaxException(tok.getToken(), tok.getFilename(), tok.getLineNum());
        }

        String scope = SymbolTable.getCurrentScope();
        if (var_dec) {
            if (SymbolTable.checkInScope(scope, id.getTokenName())) {
                Token tok = tokens.remove(0);
                throw new SemanticException("Variable " + id.getTokenName() + " is already defined in this scope", tok.getFilename(), tok.getLineNum());
            }
            SymbolTable.addToScope(scope, id.getTokenName(), t.getTypeName(), v == null ? null: v.toString());
            tokens.remove(0);
            return new AssignmentNode(t, id, v, false);
        }

        if (!SymbolTable.checkInScope(scope, id.getTokenName())) {
            Token tok = tokens.remove(0);
            throw new SemanticException("Variable " + id.getTokenName() + " is not defined in this scope", tok.getFilename(), tok.getLineNum());
        }
        tokens.remove(0);
        Symbol sym = SymbolTable.getFromCurrentScope(id.getTokenName());
        if (sym == null){
            throw new SemanticException(
                    "Variable" + id + "assigned before declaration",
                    id.getTokenFilename(),
                    id.getTokenLine()
            );
        }
        return new AssignmentNode(new TypeNode(sym.getType()), id, v, true);
    }


    @Override
    public String convertToJott() {
        String output = "";
        if (type != null)
            if (!reassignment){
                output += type.convertToJott();
            }
        output += " ";
        output += id.convertToJott();
        output += "=";
        output += value.convertToJott();
        output += ";";
        return output;
    }

    @Override
    public String convertToJava(String className) {
        String output = "";
        if (type != null)
            if (!reassignment){
                output += type.convertToJava(className);
            }
        output += " ";
        output += id.convertToJava(className);
        output += " = ";
        output += value.convertToJava(className);
        output += ";";
        return output;
    }

    @Override
    public String convertToC() {
        if (value instanceof FuncCallNode && ((FuncCallNode) value).getFuncName().getTokenName().equals("concat")) {
            ExpressionNode concatParam1 = ((FuncCallNode) value).getParams().getParamNames().get(0);
            ExpressionNode concatParam2 = ((FuncCallNode) value).getParams().getParamNames().get(1);
            String output = "";
            output += "char* " + id.convertToC() + " = malloc(strlen(" + concatParam1.convertToC() + ") + " + "strlen(" + concatParam2.convertToC() + ") + 1);\n";
            output += "strcat(" + id.convertToC() + ", " + concatParam1.convertToC() + ");\n";
            output += "strcat(" + id.convertToC() + ", " + concatParam2.convertToC() + ")";
            return output;
        }
        else {
            String output = "";
            if (type != null)
                if (!reassignment){
                    output += type.convertToC();
                }
            output += " ";
            output += id.convertToC();
            output += "=";
            output += value.convertToC();
            output += ";";
            return output;
        }
    }

    @Override
    public String convertToPython() {
        String output = "";

        for (int i = 0; i < ProgramNode.depth; i++) {
            output += '\t';
        }

        output += id.convertToPython();
        output += "=";
        output += value.convertToPython();

        output += "\n";

        return output;
    }

    @Override
    public boolean validateTree() {
        if (!this.type.getTypeName().equals(this.value.getType())) {
            throw new SemanticException("Mismatched types in assignment",
                    id.getTokenFilename(), id.getTokenLine());
        }
        else if (!this.id.validateTree()) {
            throw new SemanticException("Invalid id in assignment",
                    id.getTokenFilename(), id.getTokenLine());
        }
        else if (!this.value.validateTree()) {
            throw new SemanticException("Invalid value in assignment",
                    id.getTokenFilename(), id.getTokenLine());
        }
        else if (all_keywords.contains(this.id.getTokenName())) {
            throw new SemanticException("Keyword used as id in assignment",
                    id.getTokenFilename(), id.getTokenLine());
        }

        return true;
    }
}
