package com.BSPApp.base;

import java.awt.Color;

import com.BSPApp.Utilities.Circle;
import com.BSPApp.Utilities.Vector2;

public class Particle extends Circle {
	private Vector2 lasPosition;
	private Vector2 direction;
	private int speed = 0;

	public Particle(Vector2 position, Vector2 direction, int speed, int ray) {
		super(position, ray);
		this.speed = speed;
		this.direction = direction;
		lasPosition = new Vector2(position.GetX(), position.GetY());
	}

	public void Move(double time) {
		lasPosition.SetX(position.GetX());
		lasPosition.SetY(position.GetY());
		this.position = Vector2.Sum(this.position, Vector2.ScalarProduct(this.direction, speed * time));

	}

	public void ChangeDir(Vector2... direction) {
		this.position.SetX(lasPosition.GetX());
		this.position.SetY(lasPosition.GetY());

		this.direction.SetX(RandomRange(0, 2) - 1);
		this.direction.SetY(RandomRange(0, 2) - 1);

		Vector2.Normalize(this.direction);
	}

	private float RandomRange(int min, int max) {
		return (float) ((Math.random() * (max - min)) + min);
	}

	public boolean CheckCollide(Vector2 target) {
		this.lasPosition.SetX(position.GetX());
		this.lasPosition.SetY(position.GetY());
		int distance = ((int) Math
				.sqrt(Math.pow(position.GetX() - target.GetX(), 2) + Math.pow(position.GetY() - target.GetY(), 2)));
		if (distance <= ray) {
			return true;
		}
		return false;
	}

	public boolean CheckBorder() {
		Vector2 screenBounding = new Vector2(App.instance.getWidth(), App.instance.getHeight());

		this.lasPosition.SetX(position.GetX()
				+ (position.GetX() > screenBounding.GetX() ? -direction.GetX() * 2 : direction.GetX() * 2));
		this.lasPosition.SetY(position.GetY()
				+ (position.GetY() > screenBounding.GetY() ? -direction.GetY() * 2 : direction.GetY() * 2));

		if (position.GetX() >= screenBounding.GetX() - ray || position.GetY() >= screenBounding.GetY() - ray
				|| position.GetX() < 0 || position.GetY() < 0) {
			return true;
		}
		return false;
	}

	public Vector2 Reflection() {
		return new Vector2(0, 0);
	}

	public Vector2 GetPosition() {
		return position;
	}
}
