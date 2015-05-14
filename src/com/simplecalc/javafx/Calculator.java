package com.simplecalc.javafx;

public class Calculator {
    
    private int total;   
    
    public Calculator() {
    	total = 0;
    }
    
    public int getTotalString() {
        return total;
    }
    
    public void setTotal(String n) {
    	total = Integer.parseInt(n);
    }
    
    public void add(String n) {
    	total += Integer.parseInt(n);
    }
    
    public void subtract(String n) {
    	total -= Integer.parseInt(n);
    }
    
    public void multiply(String n) {
    	total *= Integer.parseInt(n);
    }
    
    public void divide(String n) {
    	total /= Integer.parseInt(n);
    }
   
}