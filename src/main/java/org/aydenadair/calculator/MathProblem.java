package org.aydenadair.calculator;

public class MathProblem {

    private String operator;
    private int number1, number2;
    private double result;

    public MathProblem() {

    }
    public MathProblem(String operator, int number1, int number2, double result) {
        this.operator = operator;
        this.number1 = number1;
        this.number2 = number2;
        this.result = result;
    }

    public String getOperator() {
        return operator;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public double getResult() {
        return result;
    }

}