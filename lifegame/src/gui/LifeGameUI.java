package gui;

import java.awt.Container;

import javax.swing.JFrame;
public class LifeGameUI extends JFrame {
	private GamePanel gamePanel = null;

	public LifeGameUI(int row, int col, int maxIt) {
		setTitle("LifeGame");
		setResizable(false);
		setSize(800, 800);
		gamePanel = new GamePanel(row, col, maxIt);
		Container contentPane = getContentPane();
		contentPane.add(gamePanel);
	}
	public void Run() {
		this.setVisible(true);
		gamePanel.Run();
	}
}
