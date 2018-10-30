package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import cell.Cell;
import cell.CellMap;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int xStart;
	private int yStart;
	private int width = 800;
	private int height = 800;
	private static int cellWidth = 20;
	private static int cellHeight = 20;
	private Graphics2D g2;
	private int rows;
	private int cols;
	private Timer gameTimer;
	private int delay = 300;
	private CellMap cellMap;
	private int maxIt;
	private int itCount = 0;

	private boolean update =false;
	
	public GamePanel(int row, int col, int maxIt) {
		this.rows = row;
		this.maxIt = maxIt;
		this.cols = col;
		xStart = (width - col * cellWidth) / 2;
		yStart = (height - row * cellHeight) / 2;
		cellMap = new CellMap(row, col);
		setBackground(new Color(255, 255, 255));
	
	}
	
	public void Run() {
		ActionListener gameEvent = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Update();
			}
		};
		gameTimer = new Timer(delay, gameEvent);
		gameTimer.start();
	}
	public void Update() {
		if(cellMap.IsOver() || itCount > maxIt) {
			gameTimer.stop();
			JOptionPane.showMessageDialog(this, "游戏结束, 喵～");
			return;
		}
		update = true;
		updateUI();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g2 = (Graphics2D)g;
		String itContext = new String("当前迭代次数: " + itCount);
		g2.drawString(itContext, (width - itContext.length()) /2, 40);
		if(update) {
			Stack<Cell> changedCell = cellMap.NextLife();
			while(!changedCell.isEmpty()) {
				Cell cell = changedCell.pop();
				DrawCell(cell);
			}
			itCount++;
		}
		g2.setColor(new Color(0, 0,0 ));
		for(int i = 0; i <= (cellWidth) * (cols); i += cellWidth){
			g2.drawLine(xStart + i, yStart, xStart+i, yStart + rows * cellHeight);
		}
		for(int i = 0; i <= (cellHeight) * (rows); i += cellHeight) {
			g2.drawLine(xStart, yStart + i, xStart + cellWidth * cols, yStart + i);
		}
	}
	
	private void DrawCell(Cell cell) {
		int row  = cell.GetRow();
		int col = cell.GetCol();
		int x = xStart + col * cellWidth;
		int y = yStart + row * cellHeight;
		if(cell.isLive()) {
			g2.setColor(new Color(0, 0, 0));
		} else {
			g2.setColor(new Color(255, 255, 255));
		}
	     g2.fillRect(x, y, cellWidth-1, cellHeight-1);
	}
}
