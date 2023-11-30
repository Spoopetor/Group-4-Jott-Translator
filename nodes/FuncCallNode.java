package nodes;

import exceptions.SemanticException;
import exceptions.SyntaxException;
import provided.*;

import java.util.ArrayList;

public class FuncCallNode extends ExpressionNode {
    private final IdNode funcName;
    private final FuncParamsNode params;

    private final String filename;

    private final int lineNumber;

    public FuncCallNode (IdNode fName, FuncParamsNode fp, String fn, int ln) {
        this.funcName = fName;
        this.params = fp;
        this.filename = fn;
        this.lineNumber = ln;
    }

    public static FuncCallNode parseFuncCallNode(ArrayList<Token> tokens) {
        // Check for :: and get rid of it
        if (tokens.get(0).getTokenType() != TokenType.FC_HEADER)
            throw new SyntaxException("Function call missing function header", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        tokens.remove(0);

        // Save token which we hope is an IdNode storing the function name.
        // This can be used for filename and line number details in
        // SemanticException error messages.
        Token fNameToken = null;
        if (!tokens.isEmpty()) {
            fNameToken = tokens.get(0);
        }
        // Parse IdNode which stores name of function being called
        IdNode fName = IdNode.parseIdNode(tokens);

        // Check for [ and get rid of it
        if (tokens.get(0).getTokenType() != TokenType.L_BRACKET)
            throw new SyntaxException("Function call missing left bracket", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        tokens.remove(0);

        // Parse FuncParamsNode which stores names of params being called with function
        FuncParamsNode fp = FuncParamsNode.parseFuncParamsNode(tokens);

        // Check for ] and get rid of it
        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET)
            throw new SyntaxException("Function call missing right bracket", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        tokens.remove(0);

        // fNameToken would only be null and cause a NullPointerException if tokens was empty
        // before calling IdNode.parseIdNode(tokens) to get the function name. In that case,
        // IdNode.parseIdNode() would throw a SyntaxException before we get to this return.
        return new FuncCallNode(fName, fp, fNameToken.getFilename(), fNameToken.getLineNum());
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
        String funcNameStr = funcName.getTokenName();
        if (funcNameStr.equals("print")) {
            return "System.out.println(" + params.convertToJava(className) + ")";
        }
        else if (funcNameStr.equals("concat")) {
            return params.getParamNames().get(0).convertToJava(className) + " + " +
                    params.getParamNames().get(1).convertToJava(className);
        }
        else if (funcNameStr.equals("length")) {
            return params.getParamNames().get(0).convertToJava(className) + ".length()";
        }
        else {
            return funcNameStr + "(" + params.convertToJava(className) + ")";
        }
    }

    @Override
    public String convertToC() {
        String funcNameStr = funcName.getTokenName();
        if (funcNameStr.equals("print")) {
            ExpressionNode printParam = params.getParamNames().get(0);
            if (printParam.getType().equals(Types.INTEGER) || printParam.getType().equals(Types.BOOLEAN)) {
                return "printf(%d, " +  printParam.convertToC() + ")";
            }
            else if (printParam.getType().equals(Types.DOUBLE)) {
                return "printf(%lf, " +  printParam.convertToC() + ")";
            }
            else {
                return "printf(%s, " +  printParam.convertToC() + ")";
            }
        }
        // assignment with concat in C handled in AssignmentNode
        else if (funcNameStr.equals("concat")) {
            return "";
        }
        else if (funcNameStr.equals("length")) {
            return "strlen(" + params.getParamNames().get(0).convertToC() + ")";
        }
        else {
            return funcNameStr + "(" + params.convertToC() + ")";
        }
    }

    @Override
    public String convertToPython() {
        String output = "";
        for (int i = 0; i < ProgramNode.depth; i++) {
            output += '\t';
        }

        String funcNameStr = funcName.getTokenName();
        if (funcNameStr.equals("print")) {
            output += "print(" + params.convertToPython() + ")";
        }
        else if (funcNameStr.equals("concat")) {
            output += params.getParamNames().get(0).convertToPython() + " + " +
                    params.getParamNames().get(1).convertToPython();
        }
        else if (funcNameStr.equals("length")) {
            output += "len(" + params.convertToPython() + ")";
        }
        else {
            output += funcNameStr + "(" + params.convertToPython() + ")";
        }

        return output;
    }

    @Override
    public boolean validateTree() {
        if (!params.validateTree()) {
            return false;
        }

        ArrayList<ExpressionNode> paramNames = params.getParamNames();

        // If funcName not in symbol table, semantic error
        if (!SymbolTable.scopeMap.containsKey(funcName.getTokenName())) {
            throw new SemanticException("Function " + funcName.getTokenName() + " called before being defined",
                    filename, lineNumber);
        }

        // If number of params != number of args for funcName in symbol table, semantic error
        ArrayList<Symbol> funcArgs = new ArrayList<>();
        for (Symbol symbol : SymbolTable.scopeMap.get(funcName.getTokenName())) {
            if (symbol.isParam()) {
                funcArgs.add(symbol);
            }
        }
        if (funcArgs.size() != params.getParamsLength()) {
            throw new SemanticException("Function " + funcName.getTokenName() + " called with incorrect number of params",
                    filename, lineNumber);
        }

        // Check that types match for all params
        for (int i = 0; i < paramNames.size(); i++) {
            ExpressionNode param = paramNames.get(i);
            Types paramType = null;
            // if param is id/keyword, check that it's in scope
            if (param instanceof IdNode) {
                // look in symbol table for param
                // if param not in symbol table for current scope, semantic error
                String paramName = ((IdNode) param).getTokenName();
                if (!SymbolTable.checkInScope(SymbolTable.getCurrentScope(), paramName)) {
                    throw new SemanticException("Param " + paramName + " not defined in scope " + SymbolTable.getCurrentScope(),
                            filename, lineNumber);
                }
                // use symbol table to get type
                for (Symbol symbol : SymbolTable.scopeMap.get(SymbolTable.getCurrentScope())) {
                    if (symbol.getName().equals(paramName)) {
                        paramType = symbol.getType();
                        break;
                    }
                }
            }
            // if param is func call, check return type
            else if (param instanceof FuncCallNode) {
                paramType = param.getType();
            }
            else {
                // if param not id/keyword or func call, use node class to get type
                if (param instanceof StringNode) {
                    paramType = Types.STRING;
                } else if (param instanceof BoolNode) {
                    paramType = Types.BOOLEAN;
                } else if (param instanceof NumberNode) {
                    paramType = ((NumberNode) param).getNumType();
                }
            }
            // if param type doesn't match arg type for funcName in symbol table, semantic error
            // (note: print() params can be any type but void)
            Types argType = funcArgs.get(i).getType();
            if (funcName.getTokenName().equals("print")) {
                if (Types.VOID.equals(paramType)) {
                    throw new SemanticException("VOID param type passed to print function", filename, lineNumber);
                }
            }
            else if (!argType.equals(paramType)) {
                throw new SemanticException("Incorrect param type passed to " + funcName.getTokenName()
                        + " function (expected " + argType + ", " + "got " + paramType + ")", filename, lineNumber);
            }
        }

        return true;
    }

    @Override
    public Types getType() {
        // throw error for undefined function
        if (!SymbolTable.scopeMap.containsKey(funcName.getTokenName()) ||
                !SymbolTable.returnMap.containsKey(funcName.getTokenName())) {
            throw new SemanticException("Function " + funcName.getTokenName() +
                    " used as param before being defined", filename, lineNumber);
        }
        return SymbolTable.returnMap.get(funcName.getTokenName());
    }

    public IdNode getFuncName() {
        return funcName;
    }

    public FuncParamsNode getParams() {
        return params;
    }
}
