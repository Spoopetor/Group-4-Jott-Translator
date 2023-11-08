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
        if (!funcName.validateTree() || !params.validateTree()) {
            return false;
        }

        ArrayList<ExpressionNode> paramNames = params.getParamNames();
        // Built-in functions
        if (funcName.getTokenName().equals("print")) {
            if (params.getParamsLength() != 1) {
                throw new SemanticException("Built-in function print must be called with 1 param",
                        filename, lineNumber);
            }
        }
        else if (funcName.getTokenName().equals("concat")) {
            if (params.getParamsLength() != 2) {
                throw new SemanticException("Built-in function concat must be called with 2 params",
                        filename, lineNumber);
            }
            if (!(paramNames.get(0) instanceof StringNode && paramNames.get(1) instanceof StringNode)) {
                throw new SemanticException("Built-in function concat must be called with String params",
                        filename, lineNumber);
            }
        }
        else if (funcName.getTokenName().equals("length")) {
            if (params.getParamsLength() != 1) {
                throw new SemanticException("Built-in function length must be called with 1 param",
                        filename, lineNumber);
            }
            if (!(paramNames.get(0) instanceof StringNode)) {
                throw new SemanticException("Built-in function length must be called with String param",
                        filename, lineNumber);
            }
        }
        // Non-built-in functions
        else {
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

            // For each param:
            // if param is id/keyword, it must be in scope
            // look in symbol table for param
            // if param not in symbol table for current scope, semantic error
            // if param is id/keyword, use symbol table to get type, otherwise use node type
            // if param type doesn't match arg type for funcName in symbol table, semantic error
            for (int i = 0; i < paramNames.size(); i++) {
                ExpressionNode param = paramNames.get(i);
                Types paramType = null;
                if (param instanceof IdNode) {
                    String paramName = ((IdNode) param).getTokenName();
                    if (!SymbolTable.checkInScope(FuncDefNode.getCurrentScope(), paramName)) {
                        throw new SemanticException("Param " + paramName + " not defined in scope " + FuncDefNode.getCurrentScope(),
                                filename, lineNumber);
                    }
                    for (Symbol symbol : SymbolTable.scopeMap.get(FuncDefNode.getCurrentScope())) {
                        if (symbol.getName().equals(paramName)) {
                            paramType = symbol.getType();
                            break;
                        }
                    }
                }
                else {
                    if (param instanceof StringNode) {
                        paramType = Types.STRING;
                    } else if (param instanceof BoolNode) {
                        paramType = Types.BOOLEAN;
                    } else if (param instanceof NumberNode) {
                        paramType = ((NumberNode) param).getNumType();
                    }
                }
                Types argType = funcArgs.get(i).getType();
                if (!argType.equals(paramType)) {
                    throw new SemanticException("Incorrect param type passed to " + funcName + " (expected " + argType + ", " +
                            "got " + paramType + ")", filename, lineNumber);
                }
            }
        }

        return true;
    }

}
