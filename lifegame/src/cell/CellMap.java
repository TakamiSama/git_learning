package cell;

import java.util.BitSet;
import java.util.Random;
import java.util.Stack;

public class CellMap {
	private int row;
	private int col;
	private int size;

	private Stack<Cell> liveCells;
	private Stack<Cell> nextLiveCells;
	private Stack<Cell> changedCells;

	private BitSet liveSet;
	private BitSet nextLiveSet;

	private char [][]map;
	private char [][]nextMap;

	private boolean isInit;

	private boolean isOver = false;

	private int count = 0;
	
	
	
	
	static private Point []points = {
			new Point(1, 0),
			new Point(1, 1),
			new Point(0, 1),
			new Point(-1, 1),
			new Point(-1, 0),
			new Point(-1, -1),
			new Point(0,  -1),
			new Point(1, -1),
	};
	
	public CellMap(int row, int col) {
		this.col = col;
		this.row = row;
		size = col * row;
		isInit = true;
		InitMap();
	}

	public void InitMap() {
		liveSet = new BitSet(row * col);
		liveSet.clear();
		nextLiveSet = new BitSet(row * col);
		nextLiveSet.clear();
		map = new char[this.row][this.col];
		nextMap = new char[this.row][this.col];
		liveCells = new Stack<Cell>();
		nextLiveCells = new Stack<Cell>();
		changedCells = new Stack<Cell>();
		for(int i = 0; i < row; i++) {
			for(int j = 0;  j < col; j++)
				map[i][j] = 0;
		}
	}
/*
	
	private Stack<Cell> RandomInit()
	{
		map[0][0]  = 0;
		map[0][1] = 1;
		map[0][2] = 1;
		map[0][3] = 2;
		map[0][4] = 1;

		map[1][0] = 1;
		map[1][1] = 3;
		map[1][2] = 5;
		map[1][3] = 3;
		map[1][4] = 2;

		map[2][0] = 1;
		map[2][1] = 1;
		map[2][2] = 3;
		map[2][3] = 2;
		map[2][4] = 2;

		map[3][0] = 1;
		map[3][1] = 2;
		map[3][2] = 3;
		map[3][3] = 2;
		map[3][4] = 1;

		liveSet.set(2);
		liveSet.set(33);
		liveSet.set(61);
		liveSet.set(62);
		liveSet.set(63);

		nextLiveSet.or(liveSet);
		Cell cell = new Cell(0, 2);
		cell.SetLive(true);
		liveCells.push(cell);

		cell = new Cell(1, 3);
		cell.SetLive(true);
		liveCells.push(cell);

		cell = new Cell(2, 1);
		cell.SetLive(true);
		liveCells.push(cell);

		cell = new Cell(2, 2);
		cell.SetLive(true);
		liveCells.push(cell);

		cell = new Cell(2, 3);
		cell.SetLive(true);
		liveCells.push(cell);
		Stack<Cell> initCells = new Stack<Cell>();
		for(int i = 0; i < liveCells.size(); i++) {
			initCells.push(new Cell(liveCells.get(i)));
		}
		return initCells;
	}
*/
	private Stack<Cell> RandomMap() {
		Random random = new Random();
		int initCells = 0;
		while(initCells <= 0) {
			initCells = random.nextInt(size/10);
		}
		for(int i = 0; i < initCells; i++) {
			int cindex = random.nextInt(size);
			if(liveSet.get(cindex)) {
				i--;
				continue;
			} else {
				int nc;
				int nr;
				int cr = cindex / col;
				int cc = cindex % col;
				Cell cell = new Cell(cr, cc);
				cell.SetLive(true);
				liveCells.push(cell);
				liveSet.set(cindex);
				for(int c = 0; c < 8; c++) {
					Point n = points[c];
					nr = n.row + cr;
					nc = n.col + cc;
					if(nr >= row || nr < 0)
						continue;
					if(nc < 0 || nc >= col)
						continue;
					map[nr][nc]++;
				}
			}
		}
		nextLiveSet.or(liveSet);
		System.out.println(liveCells.size());
		for(int i = 0; i < liveCells.size(); i++)
			changedCells.push(new Cell(liveCells.get(i)));
		return changedCells;
	}
	
	private void SwapMapAndSet() {
		char [][]tmpMap = nextMap;
		nextMap = map;
		map = tmpMap;
		Stack<Cell> tmpCellStack = nextLiveCells;
		nextLiveCells = liveCells;
		liveCells = tmpCellStack;
		liveSet.clear();
		liveSet.or(nextLiveSet);
	}

	public Stack<Cell> NextLife()
	{
		if(isInit) {
			isInit = false;
			return RandomMap();
		} 
		for(int i = 0; i < row; i++) 
			for(int j = 0; j  < col; j++)
				nextMap[i][j] = map[i][j];
		while(!liveCells.empty()) {
			Cell cell = liveCells.pop();
			UpdateCell(cell);
		}
		//CheckRight(nextMap);
		SwapMapAndSet();
		if(changedCells.empty()) {
			isOver = true;
		}
		for(int i = 0; i < liveCells.size(); i++) {
			changedCells.push(new Cell(liveCells.get(i)));
		}
		return changedCells;
	}
	
	public boolean IsOver() {
		return isOver;
	}

	private void UpdateCell(Cell cell) {
		int cr = cell.GetRow();
		int cc = cell.GetCol();
		int ncount = map[cr][cc];
		int nr;
		int nc;
		for(int i = 0; i < 8; i++) {
			Point n = points[i];
			nr = n.row + cr;
			nc = n.col + cc;
			if(nr >= row || nr < 0)
				continue;
			if(nc < 0 || nc >= col)
				continue;
			int index = nr * col + nc;
			if((!liveSet.get(index)) && !nextLiveSet.get(index) && map[nr][nc] == 3) {
				Cell cLive = new Cell(nr, nc);
				OnLive(cLive, nextMap, index);
			}
		}
		if(ncount == 2 || ncount == 3) {
			nextLiveCells.push(cell);
		} else {
			int cindex = cr * col + cc;
			OnDead(cell, cr, cc, cindex);
		}
	}

	private void OnLive(Cell cell, char[][] nextMap, int index)
	{
		int cr = cell.GetRow();
		int cc = cell.GetCol();
		int nr;
		int nc;
		for(int i = 0; i < 8; i++) {
			nr = points[i].row + cr;
			nc = points[i].col + cc;
			if(nr >= row || nr < 0)
				continue;
			if(nc < 0 || nc >= col)
				continue;
			nextMap[nr][nc]++;
		}
		cell.SetLive(true);
		nextLiveSet.set(index);
		nextLiveCells.push(cell);
	}
	
	private void OnDead(Cell cell, int cr, int cc, int index) {
		int nr;
		int nc;
		for(int i = 0; i < 8; i++) {
			nr = points[i].row + cr;
			nc = points[i].col + cc;
			if(nr >= row || nr < 0)
				continue;
			if(nc < 0 || nc >= col)
				continue;
			nextMap[nr][nc]--;
		}
		cell.SetLive(false);
		changedCells.push(cell);
		nextLiveSet.clear(index);
	}

	private void CheckRight(char [][]nextMap) {
		System.out.println("count :" + count);
		count++;
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				int index = i * col +j;
				boolean lastlive = liveSet.get(index);
				boolean nextlive = nextLiveSet.get(index);
				if(!lastlive && nextlive) {
					if(!(map[i][j] == 3)) {
						System.out.println("nextlive chekRight err : " + i + " " +j + "v : " + (int)map[i][j]);
						System.exit(1);
					}
				}
				if(!lastlive && !nextlive) {
					if(map[i][j] == 3) {
						System.out.println("lastlive chekRight err");
						System.exit(1);
					}
				}
			}
		}
	}


}
