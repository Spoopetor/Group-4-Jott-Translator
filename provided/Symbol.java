package provided;

public class Symbol {

    private String name;
    private Types type;
    private String value;

    public Symbol(String n, Types t, String v){
        this.name = n;
        this.type = t;
        this.value = v;
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

    public void setType(Types type) {
        this.type = type;
    }

    public void setValue(String v) {
        this.value = v;
    }
}
