package com.BSPApp.base;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

import java.awt.Graphics;

import com.BSPApp.Utilities.Rectangle;
import com.BSPApp.Utilities.Vector2;

public class BSPTree {
	private static final int HEIGHT = 8;

	public enum AXES_CUT {
		x, y
	};

	private AXES_CUT current = AXES_CUT.x;
	private int[] indexX, indexY;
	private int density_x0 = 0, density_x1 = 0, density_y0 = 0, density_y1 = 0;
	private Vector2 cut = new Vector2(0, 0);
	private Particle[] p;
	private Graphics graphic;
	public Node root;

	public BSPTree(Particle[] p, Node node) {
		this.p = p;
		this.graphic = App.instance.getGraphics();
		indexX = new int[App.instance.getWidth()];
		indexY = new int[App.instance.getHeight()];
		root = node;
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < p.length; i++) {
			l.add(i);
		}
		root.l = l;
	}

	private void BSPLeaf(List<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (i == j) {
					continue;
				}
				if (p[list.get(i)].CheckCollide(p[list.get(j)].GetPosition())) {
					p[list.get(i)].ChangeDir();
					p[list.get(j)].ChangeDir();
				}
			}
//			if (p[list.get(i)].CheckBorder()) {
//				p[list.get(i)].ChangeDir();
//			}

			p[list.get(i)].Move(GameLoop.deltaTime * 10);
			p[list.get(i)].Draw();
		}
		for (int i = 0; i < p.length; i++) {
			if (p[i].CheckBorder()) {
				p[i].ChangeDir();
			}
		}
		return;
	}

	private void DrawDensity(List<Integer> list) {
		this.indexX = new int[App.instance.getWidth()];
		this.indexY = new int[App.instance.getHeight()];

		for (int i = 0; i < list.size(); i++) {
			int x = (int) p[list.get(i)].GetPosition().GetX();
			int y = (int) p[list.get(i)].GetPosition().GetY();

//		mapa de densidade em x
			for (int j = x; j < x + GameLoop.ray; j++) {
				if (j - GameLoop.ray > 0 && j < App.instance.getWidth() - GameLoop.ray) {
					indexX[j]++;
				}
			}
//		mapa de densidade em y
			for (int j = y; j < y + GameLoop.ray; j++) {
				if (j - GameLoop.ray > 0 && j < App.instance.getHeight() - GameLoop.ray) {
					indexY[j]++;
				}
			}
		}
	}

	public void BSPRecursiveSplit(int height, Node node, Rectangle rect) {
		if (height > HEIGHT || node.l.size() <= 5) {
			BSPLeaf(node.l);
			return;
		}

		Rectangle buff1;
		Rectangle buff2;

		DrawDensity(node.l);

		SelectLargDensity(rect);

		if (density_x0 - density_x1 > density_y0 - density_y1) {
			current = AXES_CUT.y;
			smallerDensity(density_y1, density_y0);
//			down
			Vector2 posDown = new Vector2(rect.GetPosition().GetX() - GameLoop.ray,
					rect.GetPosition().GetY() - GameLoop.ray);
			Vector2 sizeDown = new Vector2(rect.GetSize().GetWidth() + GameLoop.ray, cut.GetHeight() + GameLoop.ray);

			buff1 = new Rectangle(posDown, sizeDown);
//			up
			Vector2 posUp = new Vector2(rect.GetPosition().GetX() - GameLoop.ray, cut.GetY() - GameLoop.ray);
			Vector2 sizeUp = new Vector2(rect.GetSize().GetWidth() + GameLoop.ray,
					rect.GetSize().GetHeight() - cut.GetY() + GameLoop.ray);

			buff2 = new Rectangle(posUp, sizeUp);

		} else {
			current = AXES_CUT.x;
			smallerDensity(density_x1, density_x0);
//			left 
			Vector2 posleft = new Vector2(rect.GetPosition().GetX() - GameLoop.ray,
					rect.GetPosition().GetY() - GameLoop.ray);
			Vector2 sizeleft = new Vector2(cut.GetX() + GameLoop.ray, rect.GetSize().GetHeight() + (GameLoop.ray + 1));

			buff1 = new Rectangle(posleft, sizeleft);
//			right
			Vector2 posright = new Vector2(cut.GetX() - GameLoop.ray, rect.GetPosition().GetY() - GameLoop.ray);
			Vector2 sizeright = new Vector2(rect.GetSize().GetWidth() - cut.GetWidth() + GameLoop.ray,
					rect.GetSize().GetY() + GameLoop.ray);

			buff2 = new Rectangle(posright, sizeright);
		}

		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();

		for (int i = 0; i < node.l.size(); i++) {
			if (buff1.CheckInside(p[node.l.get(i)].GetPosition())) {
				l1.add(node.l.get(i));
			}
		}
		for (int i = 0; i < node.l.size(); i++) {
			if (buff2.CheckInside(p[node.l.get(i)].GetPosition())) {
				l2.add(node.l.get(i));
			}
		}
//		System.out.print(current + "[");
//		for (int i = 0; i < l1.size(); i++) {
//			System.out.print(l1.get(i) + ", ");
//		}
//		for (int i = 0; i < l2.size(); i++) {
//			System.out.print(l2.get(i) + ", ");
//		}
//		System.out.print("]");

		node.left = new Node(l1);
		node.right = new Node(l2);

//		System.out.println(node.l.size() + " " + node.left.l.size() + " " + node.right.l.size() + " "
//				+ (node.left.l.size() + node.right.l.size()) + rect);
//		print(node.left.l, node.right.l, buff1, buff2);

		BSPRecursiveSplit(height + 1, node.left, buff1);
		BSPRecursiveSplit(height + 1, node.right, buff2);
		return;
	}

	private void SelectLargDensity(Rectangle rect) {
		for (int i = (int) rect.GetPosition().GetX(); i < indexX.length; i++) {
			if (i > 0) {
				if (indexX[i] > indexX[density_x0]) {
					density_x1 = density_x0;
					density_x0 = i;
				}
			}
		}
		for (int i = (int) rect.GetPosition().GetY(); i < indexY.length; i++) {
			if (i > 0) {
				if (indexY[i] > indexY[density_y0]) {
					density_y1 = density_y0;
					density_y0 = i;
				}
			}
		}
	}

	private void print(List<Integer> l1, List<Integer> l2, Rectangle buff1, Rectangle buff2) {
		System.out.println("eixo = " + current);
		System.out.println("buffer_1 - " + buff1);
		System.out.println("buffer_2 - " + buff2);
		System.out.println("cut - " + cut);
		if (l1.size() > 0) {
			System.out.print("l1 = [");
			for (int i = 0; i < l1.size() - 1; i++) {
				System.out.print(l1.get(i) + ", ");
			}
			System.out.println(l1.get(l1.size() - 1) + "]");
		}
		if (l2.size() > 0) {
			System.out.print("l2 = [");
			for (int i = 0; i < l2.size() - 1; i++) {
				System.out.print(l2.get(i) + ", ");
			}
			System.out.println(l2.get(l2.size() - 1) + "]\n");
		}
	}

	private void smallerDensity(int start, int end) {
		int largDensity = end;
		cut.SetX(0);
		cut.SetY(0);
		int result = 0;
		boolean check = false;
		if (current == AXES_CUT.y) {
			for (int i = start; i < end; i++) {
				if (indexY[i] < largDensity) {
					largDensity = indexY[i];
					result = i;
					check = true;
				}
			}
		} else {
			for (int i = start; i < end; i++) {
				if (indexX[i] < largDensity) {
					largDensity = indexX[i];
					result = i;
					check = true;
				}
			}
		}
		if (!check) {
			if (current == AXES_CUT.x) {
				cut.SetX(largDensity);
			} else {
				cut.SetY(largDensity);
			}
		} else {
			if (current == AXES_CUT.x) {
				cut.SetX(((end - start) / 2) + start);
			} else {
				cut.SetY(((end - start) / 2) + start);
			}
		}
	}
}

class Node {
	public List<Integer> l;
	public Node left, right;

	public Node(List<Integer> l) {
		this.l = l;
	}

}
