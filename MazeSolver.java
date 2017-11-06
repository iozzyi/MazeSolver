import java.io.FileNotFoundException;
import java.util.HashMap;

// Uses BFS to solve shortest path to End point

public class MazeSolver {
	
	// prefer to move right first, then down, then left, then up
	public static final int MOVE_LEFT = 2,
			MOVE_UP = 3,
			MOVE_RIGHT = 0,
			MOVE_DOWN = 1;
	
	private static boolean isValidMove(Maze maze, int x, int y, int move) {
		switch (move) {
		case MOVE_LEFT:
			if (x==0 || maze.maze_data[y][x-1] == Maze.STDIN_WALL) return false; break;
		case MOVE_UP:
			if (y==0 || maze.maze_data[y-1][x] == Maze.STDIN_WALL) return false; break;
		case MOVE_RIGHT:
			if (x == maze.width-1 || maze.maze_data[y][x+1] == Maze.STDIN_WALL) return false; break;
		case MOVE_DOWN:
			if (y == maze.height-1 || maze.maze_data[y+1][x] == Maze.STDIN_WALL) return false; break;
		}
		return true;
	}
	
	private static void findLinks(Maze maze, MazeCell cell) {
		cell.visit();
		for (int move = MOVE_RIGHT; move <= MOVE_UP; move++) {
			if (isValidMove(maze, cell.x, cell.y, move)) {
				MazeCell adjacent = cell.link(move);
				if (!adjacent.visited())
					findLinks(maze, adjacent);
			}
		}
	}
	
	private static void markPathFromCell(Maze maze, MazeCell cell) {
		while (cell.hasParent()) {
			cell = cell.parent;
			if (cell.hasParent()) maze.maze_data[cell.y][cell.x] = Maze.STDIN_PATH;
		}
	}
	
	private static void markPath(Maze maze) {
		findLinks(maze, new MazeCell(maze.start_x, maze.start_y));
		MazeCell end = MazeCell.getCell(maze.end_x, maze.end_y);
		if (end != null) {
			markPathFromCell(maze, end);
			System.out.println(maze);
		} else {
			System.out.println("No path found.");
		}
		MazeCell.resetLinks();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String[] tests = {"input.txt", "small.txt", "medium_input.txt", "large_input.txt", "sparse_medium.txt"};
		long t1, t2;
		for (String test : tests) {
			Maze testMaze = Maze.readFromFile(test);
			t1 = System.currentTimeMillis();
			markPath(testMaze);
			t2 = System.currentTimeMillis();
			System.out.printf("Time taken: %.3f seconds.%n%n", (t2-t1)/1000d);
		}
	}
	
	// Utility classes below
	// MazeCell : each instance stores one node in the graph
	// Key : used to create unique int,int x,y coordinate key pairs for a HashMap<Key,MazeCell> table
	
	static class MazeCell {
		int x;
		int y;
		
		MazeCell parent;
		
		MazeCell left, up, right, down;
		
		MazeCell(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		boolean hasParent() { return parent != null; }
		
		MazeCell link(int move) {
			switch(move) {
			case MOVE_LEFT:
				this.left = new MazeCell(x-1, y); 
				this.left.parent = this;
				return this.left;
			case MOVE_UP:
				this.up = new MazeCell(x, y-1);
				this.up.parent = this;
				return this.up;
			case MOVE_RIGHT:
				this.right = new MazeCell(x+1, y);
				this.right.parent = this;
				return this.right;
			case MOVE_DOWN:
				this.down = new MazeCell(x, y+1);
				this.down.parent = this;
				return this.down;
			}
			return null;
		}
	    
		static HashMap<Key, MazeCell> instance_map = new HashMap<>();
		
		void visit() {
			instance_map.put(new Key(this.x, this.y), this);
		}
		
		boolean visited() {
			return instance_map.containsKey(new Key(this.x, this.y));
		}
		
		static MazeCell getCell(int x, int y) {
			return instance_map.get(new Key(x,y));
		}
		
		static void resetLinks() {
			instance_map.clear();
		}
	}
	
	static class Key {
		int x, y;
		
		Key(int x, int y) {
			this.x = x;
			this.y = y;
		}

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Key)) return false;
	        Key cell = (Key) o;
	        return x == cell.x && y == cell.y;
	    }
	    
	    @Override
	    public int hashCode() {
	        int result = x;
	        result = 31 * result + y;
	        return result;
	    }
	}

}
