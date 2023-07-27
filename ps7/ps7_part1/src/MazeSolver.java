import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

public class MazeSolver implements IMazeSolver {
	class Point {
		final int x;
		final int y;
		final Point previous;

		Point(int x, int y, Point previous) {
			this.x = x;
			this.y = y;
			this.previous = previous;
		}
	}
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Maze maze;
	private boolean solved = false;
	private boolean[][] visited;
	private List<Integer> roomCount;

	public MazeSolver() {
		// TODO: Initialize variables.
		solved = false;
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		visited = new boolean[maze.getRows()][maze.getColumns()];
		solved = false;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		solved = false;
		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				maze.getRoom(i, j).onPath = false;
			}
		}

		Queue<Point> frontier = new LinkedList<>();
		Point start = new Point(startRow, startCol, null);
		frontier.add(start);
		Point parent = start;
		int steps = 0;
		roomCount = new ArrayList<>();

		while (!frontier.isEmpty()) {

			roomCount.add(steps, frontier.size());
			Queue<Point> nextFrontier = new LinkedList<>();
			for (Point curr : frontier) {
				int x = curr.x;
				int y = curr.y;
				//if at end point, terminate
				if (x == endRow && y == endCol) {
					solved = true;
					parent = curr;
				}
				visited[x][y] = true;
				for (int i = 0; i < 4; i++) {
					if (canGo(x, y, i) && !visited[x + DELTAS[i][0]][y + DELTAS[i][1]]) {
						nextFrontier.add(new Point(x + DELTAS[i][0], y + DELTAS[i][1], curr));
						visited[x + DELTAS[i][0]][y + DELTAS[i][1]] = true;
					}
				}
			}
			frontier = nextFrontier;
			steps++;
		}
		// backtrack to find the path.
		if (solved) {
			int rooms = 0;
			while(!(parent == null)) {
				// set its path as true
				int x = parent.x;
				int y = parent.y;
				maze.getRoom(x, y).onPath = true;
				parent = parent.previous;
				rooms++;
			}
			return rooms - 1;
		}
		return null;
	}

	private boolean canGo(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}
		return false;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		if (k > roomCount.size() - 1) {
			return 0;
		}
		return roomCount.get(k);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-dense.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3));
			MazePrinter.printMaze(maze);
			//ImprovedMazePrinter.printMaze(maze, 0, 0);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}