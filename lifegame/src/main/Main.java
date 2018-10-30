package main;

import gui.LifeGameUI;
public class Main {

	public static void main(String[] args) {
		LifeGameUI ui = new LifeGameUI(30, 20, 100);
		System.out.println("OK!");
		ui.Run();
	}
}
