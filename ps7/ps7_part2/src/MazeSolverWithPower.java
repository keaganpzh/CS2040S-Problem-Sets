import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	class Point {
		final int x;
		final int y;
		final Point parent;
		int powerUsed;
		Room room;
		public int steps;
		Point(int x, int y, Point parent, int superpower, Room room) {
			this.x = x;
			this.y = y;
			this.parent = parent;
			this.powerUsed = superpower;
			this.room = room;
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
	private HashMap<Integer, HashSet<Point>> roomCount;

	public MazeSolverWithPower() {
		// TODO: Initialize variables.
		roomCount = new HashMap<>();
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		return pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	boolean canGo(int x, int y, int powerUsed, int superpowerUsed) {
		if (x >= 0 && x < maze.getRows()) {
			if (y >= 0 && y < maze.getColumns()) {
				return powerUsed <= superpowerUsed;
			}
		}
		return false;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		if (roomCount.get(k) == null) {
			return 0;
		}
		return roomCount.get(k).size();
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpower) throws Exception {
		// TODO: Find shortest path with power allowed.
		// Reset the Map and traversed rooms when pathSearch is called.
		roomCount.clear();
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				maze.getRoom(i, j).onPath = false;
			}
		}
		Queue<Point> frontier = new LinkedList<>();
		boolean[][][] visitedWithPower = new boolean[maze.getRows()][maze.getColumns()][superpower + 1];
		boolean[][] visited = new boolean[maze.getRows()][maze.getColumns()];
		Point start = new Point(startRow, startCol, null, 0, maze.getRoom(startRow, startCol));

		visited[start.x][start.y] = true;
		visitedWithPower[start.x][start.y][0] = true;

		frontier.add(start);
		includePoint(start);
		updateParent(start);

		Point end;
		if (startRow == endRow && startCol == endCol) {
			end = start;
		} else {
			end = null;
		}

		while (!frontier.isEmpty()) {
			Point curr = frontier.poll();
			int[][] currPossiblePaths = possiblePaths(curr);
			for (int[] path : currPossiblePaths) {
				int x = path[0];
				int y = path[1];
				int powerUsed = path[2];
				if (canGo(x, y, powerUsed, superpower)) {
					if (!visitedWithPower[x][y][powerUsed]) {
						Point next = new Point(x, y, curr, powerUsed, maze.getRoom(x, y));
						visitedWithPower[x][y][powerUsed] = true;
						frontier.add(next);
						updateParent(next);

						if (!visited[x][y]) {
							visited[x][y] = true;
							includePoint(next);
						}
						//end check
						if (x == endRow && y == endCol) {
							if (end == null || countSteps(start, next) < countSteps(start, end)) {
								end = next;
							}
						}
					}
				}
			}
		}
		Point curr = end;
		if (curr != null) {
			while(curr != start) {
				maze.getRoom(curr.x, curr.y).onPath = true;
				curr = curr.parent;
			}
			maze.getRoom(curr.x, curr.y).onPath = true;
		}
		return countSteps(start, end);
	}

	public void includePoint(Point point) {
		if (!roomCount.containsKey(point.steps)) {
			HashSet<Point> pointSet = new HashSet<>();
			pointSet.add(point);
			roomCount.put(point.steps, pointSet);
		} else {
			roomCount.get(point.steps).add(point);
		}
	}
	public void updateParent(Point point) {
		if (point.parent == null) {
			point.steps = 0;
		} else {
			point.steps = point.parent.steps + 1;
		}
	}

	public Integer countSteps(Point start, Point end) {
		if (end == null) { return null; }
		int count = 0;
		while (end != start) {
			end = end.parent;
			count++;
		}
		return count;
	}

	public int[][] possiblePaths(Point point) {
		return new int[][] {
				{point.x + 1, point.y, point.room.hasSouthWall() ? point.powerUsed + 1 : point.powerUsed},
				{point.x - 1, point.y, point.room.hasNorthWall() ? point.powerUsed + 1 : point.powerUsed},
				{point.x, point.y + 1, point.room.hasEastWall() ? point.powerUsed + 1 : point.powerUsed},
				{point.x, point.y - 1, point.room.hasWestWall() ? point.powerUsed + 1 : point.powerUsed}
		};
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 1, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
