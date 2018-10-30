package cell;

public class Cell {
	private int col;
	private int row;
	private boolean live;

	public Cell(int row, int col) {
		this.col = col;
		this.row = row;
		live = false;
	}
	public Cell(Cell other) {
		col = other.col;
		row = other.row;
		live = other.live;
	}
	
	public int GetRow() {
		return row;
	}
	
	public int GetCol() {
		return col;
	}
	
	
	public boolean isLive() {
		return live;
	}
	
	public void SetLive(boolean isLive) {
		live = isLive;
	}
}
