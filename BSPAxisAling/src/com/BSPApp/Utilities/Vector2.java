package com.BSPApp.Utilities;

import java.lang.Math;

public class Vector2 {
	private float[] values = new float[2];
	private static Vector2 buffer = new Vector2(0, 0);

	public Vector2(float x, float y) {
		this.values[0] = x;
		this.values[1] = y;
	}

	public float GetX() {
		return this.values[0];
	}

	public float GetY() {
		return this.values[1];
	}

	public void SetX(float x) {
		this.values[0] = x;
	}

	public void SetY(float y) {
		this.values[1] = y;
	}

	public float GetWidth() {
		return this.values[0];
	}
	
	public float GetHeight() {
		return this.values[1];
	}

	public static Vector2 Sum(Vector2 v1, Vector2 v2) {
		v1.SetX(v1.GetX() + v2.GetX());
		v1.SetY(v1.GetY() + v2.GetY());
		return v1;
	}

	public static Vector2 ScalarProduct(Vector2 v1, double s) {
		buffer.SetX(v1.GetX() * (float) s);
		buffer.SetY(v1.GetY() * (float) s);
		return buffer;
	}

	public static Vector2 Normalize(Vector2 v1) {
		float magnitude = (float) Math.sqrt((v1.GetX() * v1.GetX()) + (v1.GetY() * v1.GetY()));
		v1.SetX(v1.GetX() / magnitude);
		v1.SetY(v1.GetY() / magnitude);
		return v1;
	}

	@Override
	public String toString() {
		return "[x: " + values[0] + ", y:" + values[1] + "]";
	}
}