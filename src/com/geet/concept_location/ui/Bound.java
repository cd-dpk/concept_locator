package com.geet.concept_location.ui;
public class Bound {
	private int x,y, width, height;
	public Bound(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeigh() {
		return height;
	}
	public void setHeigh(int heigh) {
		this.height = heigh;
	}
}
