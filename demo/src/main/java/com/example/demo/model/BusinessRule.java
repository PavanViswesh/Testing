package com.example.demo.model;

public class BusinessRule {

    private String field;
    private String operator;
    private Object value;
    private String expression;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }



    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    @Override
    public String toString() {
        if (expression != null) {
            return expression;
        }
        if (field != null) {
            return field + " " + operator + " " + value;
        }
        return "Invalid Rule";
    }
}