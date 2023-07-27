import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	public class Point implements Comparable<Point> {
		private int x;
		private int y;
		private int fear = Integer.MAX_VALUE;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void setFear(int fear) {
			this.fear = fear;
		}

		@Override
		public int compareTo(Point p) {
			if (this.fear < p.fear) {
				return -1;
			} else if (this.fear > p.fear) {
				return 1;
			} else {
				return 0;
			}
		}

	}
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;
	private int[][] fear2D;
	PriorityQueue<Point> queue;

	public MazeSolver() {
		// TODO: Initialize variables.
		this.maze = null;
		this.fear2D = null;
		this.queue = null;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		Point start = new Point(startRow, startCol);
		start.setFear(0);
		queue = new PriorityQueue<>();
		queue.add(start);
		fear2D = new int[maze.getRows()][maze.getColumns()];
		resetFear(startRow, startCol);

		while (!queue.isEmpty()) {
			Point curr = queue.poll();
			Room currRoom = maze.getRoom(curr.x, curr.y);

			for (int d = 0; d < 4; d++) {
				Point next = new Point(curr.x + DELTAS[d][0], curr.y + DELTAS[d][1]);

				if (next.x >= 0 && next.x < maze.getRows() && next.y >= 0 && next.y < maze.getColumns()) {
					int wall = TRUE_WALL;
					int dX = curr.x - next.x;
					int dY = curr.y - next.y;
					if (dX < 0) {
						wall = currRoom.getSouthWall();
					} else if (dY < 0) {
						wall = currRoom.getEastWall();
					} else if (dX > 0) {
						wall = currRoom.getNorthWall();
					} else {
						wall = currRoom.getWestWall();
					}
					if (wall == EMPTY_SPACE) {
						wall = 1;
					}
					if (wall != TRUE_WALL && fear2D[curr.x][curr.y] + wall < fear2D[next.x][next.y]) {
						fear2D[next.x][next.y] = fear2D[curr.x][curr.y] + wall;
						next.setFear(fear2D[next.x][next.y]);
						queue.add(next);
					}
				}
			}
		}

		if (fear2D[endRow][endCol] != Integer.MAX_VALUE) {
			return fear2D[endRow][endCol];
		}
		return null;

	}

	public void resetFear(int row, int col) {
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				fear2D[i][j] = Integer.MAX_VALUE;
			}
		}
		fear2D[row][col] = 0;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		Point start = new Point(startRow, startCol);
		start.setFear(0);
		queue = new PriorityQueue<>();
		queue.add(start);
		fear2D = new int[maze.getRows()][maze.getColumns()];
		resetFear(startRow, startCol);

		while (!queue.isEmpty()) {
			Point curr = queue.poll();
			Room currRoom = maze.getRoom(curr.x, curr.y);

			for (int d = 0; d < 4; d++) {
				Point next = new Point(curr.x + DELTAS[d][0], curr.y + DELTAS[d][1]);

				if (next.x >= 0 && next.x < maze.getRows() && next.y >= 0 && next.y < maze.getColumns()) {
					int wall = TRUE_WALL;
					int dX = curr.x - next.x;
					int dY = curr.y - next.y;
					if (dX < 0) {
						wall = currRoom.getSouthWall();
					} else if (dY < 0) {
						wall = currRoom.getEastWall();
					} else if (dX > 0) {
						wall = currRoom.getNorthWall();
					} else {
						wall = currRoom.getWestWall();
					}
					if (wall != TRUE_WALL && fear2D[curr.x][curr.y] + wall < fear2D[next.x][next.y]) {
						if (wall != EMPTY_SPACE) {
							fear2D[next.x][next.y] = Math.max(fear2D[curr.x][curr.y], wall);
						} else {
							fear2D[next.x][next.y] = fear2D[curr.x][curr.y] + 1;
						}
						next.setFear(fear2D[next.x][next.y]);
						queue.add(next);
					}
				}
			}
		}

		if (fear2D[endRow][endCol] != Integer.MAX_VALUE) {
			return fear2D[endRow][endCol];
		}
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		Point start = new Point(startRow, startCol);
		start.setFear(0);
		queue = new PriorityQueue<>();
		queue.add(start);
		fear2D = new int[maze.getRows()][maze.getColumns()];
		resetFear(startRow, startCol);

		while (!queue.isEmpty()) {
			Point curr = queue.poll();
			if (curr.x == sRow && curr.y == sCol) {
				fear2D[curr.x][curr.y] = -1;
			}
			Room currRoom = maze.getRoom(curr.x, curr.y);

			for (int d = 0; d < 4; d++) {
				Point next = new Point(curr.x + DELTAS[d][0], curr.y + DELTAS[d][1]);
				if (next.x >= 0 && next.x < maze.getRows() && next.y >= 0 && next.y < maze.getColumns()) {
					int wall = TRUE_WALL;
					int dX = curr.x - next.x;
					int dY = curr.y - next.y;
					if (dX < 0) {
						wall = currRoom.getSouthWall();
					} else if (dY < 0) {
						wall = currRoom.getEastWall();
					} else if (dX > 0) {
						wall = currRoom.getNorthWall();
					} else {
						wall = currRoom.getWestWall();
					}
					if (wall != TRUE_WALL && fear2D[curr.x][curr.y] + wall < fear2D[next.x][next.y]) {
						if (wall != EMPTY_SPACE) {
							fear2D[next.x][next.y] = Math.max(fear2D[curr.x][curr.y], wall);
						} else {
							fear2D[next.x][next.y] = fear2D[curr.x][curr.y] + 1;
						}
						next.setFear(fear2D[next.x][next.y]);
						queue.add(next);
					}
				}
			}
		}

		if (fear2D[endRow][endCol] != Integer.MAX_VALUE) {
			return fear2D[endRow][endCol];
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.bonusSearch(0, 0, 0, 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
