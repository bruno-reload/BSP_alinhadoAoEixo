package com.BSPApp.Utilities;

import java.awt.*;
import java.awt.Graphics;

import com.BSPApp.base.App;

public class Circle implements GameObject {
	protected Vector2 position;
	public Graphics graphic;
	protected float ray;

	public Circle(Vector2 position, float ray) {
		this.graphic = App.instance.getGraphics();
		position.SetX(position.GetX() + (int) ray);
		position.SetY(position.GetY() + (int) ray);
		this.graphic.setColor(Color.white);
		this.position = position;
		this.ray = ray;
	}

	@Override
	public void Draw() {
		this.graphic.fillOval((int) position.GetX(), (int) position.GetY(), (int) ray, (int) ray);
	}

	public void ChangeColor() {
		this.graphic.setColor(Color.red);
	}
}
