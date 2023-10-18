package nodes;

import provided.JottTree;

public class FuncReturnNode implements JottTree {

    private String type_void = "Void";
    private TypeNode type;
    private Boolean isVoid;

    public FuncReturnNode(TypeNode type){
        this.type = type;
        isVoid = false;
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
}
