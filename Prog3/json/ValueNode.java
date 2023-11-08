public class ValueNode extends Node {
    private final Object value;

    ValueNode(double value) {
        this.value = value;
    }

    ValueNode(boolean value) {
        this.value = value;
    }

    ValueNode(String value) {
        this.value = value;
    }

    public boolean isNumber() {
        return value instanceof Double;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isNull() {
        return value == null;
    }

    public double getNumber() {
        if (!isNumber()) {
            throw new IllegalArgumentException();
        }
        return ((Number) value).doubleValue();
    }

    public boolean getBoolean()  {
        if(!isBoolean()) {
           throw new IllegalArgumentException();
        }
        return (boolean) value;
    }

    public String getString() {
        if(!isString()) {
            throw new IllegalArgumentException();
        }
        return value.toString();
    }
}
