package provided;

public class Symbol {

    private String name;
    private Types type;
    private String value;
    private boolean param = false;

    public Symbol(String n, Types t, String v){
        this.name = n;
        this.type = t;
        this.value = v;
    }

    public Symbol(String n, Types t, String v, boolean param){
        this.name = n;
        this.type = t;
        this.value = v;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public Types getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isParam() {
        return param;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public void setValue(String v) {
        this.value = v;
    }
}
