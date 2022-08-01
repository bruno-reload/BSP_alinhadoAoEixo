package com.BSPApp.Utilities;

import com.BSPApp.base.App;
import java.awt.*;

public class Rectangle implements GameObject {
	protected Vector2 position;
	protected Vector2 size;
	private Graphics praphic;

	public Rectangle(Vector2 position, Vector2 size) {
		super();
		this.position = position;
		this.size = size;
		this.praphic = App.instance.getGraphics();
	}

	@Override
	public void Draw() {
		this.praphic.setColor(Color.white);
		this.praphic.fillRect((int) this.position.GetX(), (int) this.position.GetY(), (int) this.size.GetWidth(),
				(int) this.size.GetHeight());
	}

	public Vector2 GetPosition() {
		return this.position;
	}

	public Vector2 GetSize() {
		return this.size;
	}

	public boolean CheckInside(Vector2 point) {
		if (point.GetX() >= position.GetX() && point.GetY() >= position.GetY()
				&& point.GetX() <= position.GetX() + size.GetX() && point.GetY() <= position.GetY() + size.GetY()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[x: " + position.GetX() + ", y: " + position.GetY() + "] [ width: " + size.GetWidth() + ", height:"
				+ size.GetHeight() + "]";
	}
}
