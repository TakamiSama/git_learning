package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.geom.Line2D;

public class Line {
	public static void main(String[] args) {
		LineFrame frame = new LineFrame();
		frame.setDefaultCloseOperation(3);
		frame.show();
	}
}

class LineFrame extends JFrame{
	LinePanel panel;
	public LineFrame(){
		setTitle("Line");
		setResizable(false);
		setSize(300,200);
		panel = new LinePanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
	}

	public void U() {
		panel.updateUI();
	}
}

class LinePanel extends JPanel {
	Graphics2D g2;

	public LinePanel(){

	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g2 = (Graphics2D)g;
		g2.draw(line);
		System.out.println("panint");
	}

	private double leftX = 100.0;
	private double topY =500.0 ;
	private double W = 50.0;
	private double H = 50.0;
	private double MovLen = 5.0;
	private Line2D line =new Line2D.Double(leftX,topY,W,H);
}