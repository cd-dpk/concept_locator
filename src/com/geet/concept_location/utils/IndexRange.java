package com.geet.concept_location.utils;
public class IndexRange {
	private int a = -1;
	private int b = -1;
	public boolean isValid() {
		if (!(a == -1 && b ==-1)) {
			return true;
		}
		return false;
	}
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	
}
