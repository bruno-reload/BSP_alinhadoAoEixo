package com.BSPApp.base;

import java.awt.*;

import javax.swing.JFrame;

public class App extends Canvas {
	private static final long serialVersionUID = 1L;
	public boolean quit = false;
	public static App instance;
	private static GameLoop gl;
	private JFrame frame;

	public App() {
		frame = new JFrame();
		frame.add(this);
		frame.setSize(720, 600);
		frame.setVisible(true);
		setBackground(Color.black);
	}

	public static void main(String[] args) {
		instance = new App();
		gl = new GameLoop();
		gl.start();
	}
}
