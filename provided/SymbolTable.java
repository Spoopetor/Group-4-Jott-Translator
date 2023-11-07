package provided;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    /** Hashmap where key is the name of the scope, and the value is the list of symbols in the scope */
    public static HashMap<String, ArrayList<Symbol>> scopeMap = new HashMap<>();

    /**
     * adds a new symbol to a given scope
     * @param scopeName the name of the scope to add to
     * @param name the name of the symbol
     * @param type the type of the symbol (Integer, Double, String, Boolean)
     * @param value the value of the symbol
     */
    public static void addToScope(String scopeName, String name, Types type, String value){
        Symbol newSymbol = new Symbol(name, type, value);
        ArrayList<Symbol> scopeVars = scopeMap.get(scopeName);
        if (scopeVars == null){
            scopeVars = new ArrayList<>();
        }
        scopeVars.add(newSymbol);
        scopeMap.put(scopeName, scopeVars);
    }

    /**
     * create a new scope
     * @param scopeName the name of the scope to create
     */
    public static void createScope(String scopeName){
        scopeMap.put(scopeName, new ArrayList<Symbol>());
    }

    /**
     * removes a scope (and therefore all symbols in the scope)
     * @param scopeName the name of the scope to destroy
     */
    public static void destroyScope(String scopeName){
        scopeMap.remove(scopeName);
    }

    /**
     * Check if a symbol with the given name is in the given scope
     * @param scopeName the name of the scope
     * @param name the name of the symbol to look for
     * @return true if present, false otherwise
     */
    public static boolean checkInScope(String scopeName, String name) {
        ArrayList<Symbol> scopeSymbols = scopeMap.get(scopeName);
        for (Symbol sym : scopeSymbols){
            if (sym.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}