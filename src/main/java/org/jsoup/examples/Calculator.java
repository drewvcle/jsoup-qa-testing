package org.jsoup.examples;

public class Calculator {
	
	
	public int add(int a, int b) {
		return a + b;
	}
	
	public int divide(int a, int b) {
		if (b == 0) {
			throw new IllegalArgumentException("b must not be zero");
		}
		return a / b;
	}
	
	public int max(int a, int b) {
		return (a >= b) ? a : b;
	}
	
	
	
	
}