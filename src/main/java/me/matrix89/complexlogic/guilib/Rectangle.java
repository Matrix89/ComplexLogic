package me.matrix89.complexlogic.guilib;

public class Rectangle {
	private int xStart;
	private int yStart;
	private int width;
	private int height;
	private int xEnd;
	private int yEnd;
	
	public Rectangle(int xStart, int yStart, int width, int height) {
		if (xStart < 0) {
			throw new IllegalArgumentException("xStart must be greater or equals 0");
		}
		if (yStart < 0) {
			throw new IllegalArgumentException("yStart must be greater or equals 0");
		}
		if (width <= 0) {
			throw new IllegalArgumentException("width must be greater then 0");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("height must be greater then 0");
		}
		this.xStart = xStart;
		this.yStart = yStart;
		this.width = width;
		this.height = height;
		xEnd = xStart + width;
		yEnd = yStart + height;
	}
	
	public int getXStart() {
		return xStart;
	}
	
	public int getYStart() {
		return yStart;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getXEnd() {
		return xEnd;
	}
	
	public int getYEnd() {
		return yEnd;
	}
	
	public boolean checkCollision(int x, int y) {
		return x > xStart && x < xEnd && y > yStart && y < yEnd;
	}
}
