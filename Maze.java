import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//The input is a maze description file in plain text.  
// 1 - denotes walls
// 0 - traversable passage way
//
//INPUT:
//<WIDTH> <HEIGHT><CR>
//<START_X> <START_Y><CR>		(x,y) location of the start. (0,0) is upper left and (width-1,height-1) is lower right
//<END_X> <END_Y><CR>		(x,y) location of the end
//<HEIGHT> rows where each row has <WIDTH> {0,1} integers space delimited
//
//OUTPUT:
// the maze with a path from start to end
// walls marked by '#', passages marked by ' ', path marked by 'X', start/end marked by 'S'/'E'

public class Maze {
	
	int width;
	int height;
	int start_x;
	int start_y;
	int end_x;
	int end_y;
	
	int[][] maze_data;
	
	public Maze(int width, int height, int start_x, int start_y, int end_x, int end_y, int[][] maze_data) {
		this.width = width;
		this.height = height;
		this.start_x = start_x;
		this.start_y = start_y;
		this.end_x = end_x;
		this.end_y = end_y;
		this.maze_data = maze_data;
		this.maze_data[start_y][start_x] = STDIN_START;
		this.maze_data[end_y][end_x] = STDIN_END;
	}
	
	public static Maze readFromFile(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		Scanner sc = new Scanner(file);
		
		int width = sc.nextInt();
		int height = sc.nextInt();
		int start_x = sc.nextInt();
		int start_y = sc.nextInt();
		int end_x = sc.nextInt();
		int end_y = sc.nextInt();
		
		int[][] maze_data = new int[height][width];
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				maze_data[i][j] = sc.nextInt();
			}
		}
		
		sc.close();
		
		return new Maze(width, height, start_x, start_y, end_x, end_y, maze_data);
	}
	
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<this.height; i++) {
			for (int j=0; j<this.width; j++) {
				sb.append(map_in_to_out(this.maze_data[i][j]));
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	private static final char map_in_to_out(int in) {
		switch (in) {
		case STDIN_WALL: 
			return STDOUT_WALL; 
		case STDIN_PASSAGE:
			return STDOUT_PASSAGE; 
		case STDIN_PATH:
			return STDOUT_PATH; 
		case STDIN_START:
			return STDOUT_START; 
		case STDIN_END:
			return STDOUT_END;
		}
		return STDOUT_ERROR;
	}
	
	static final char 
		STDIN_WALL = 1,
		STDIN_PASSAGE = 0,
		STDIN_PATH = 2,
		STDIN_START = 3,
		STDIN_END = 4,
		STDOUT_WALL = '#',
		STDOUT_PASSAGE = ' ',
		STDOUT_PATH = 'X',
		STDOUT_START = 'S',
		STDOUT_END = 'E',
		STDOUT_ERROR = '?';
	
	// test maze loading and printing
	
	public static void main(String[] args) throws FileNotFoundException {
		int width = 4, 
				height = 5, 
				start_x = 0, 
				start_y = 0, 
				end_x = 1, 
				end_y = 4;
		int[][] maze_data = {
				{0,0,0,1},
				{1,1,0,0},
				{1,1,1,0},
				{1,0,0,0},
				{1,0,1,1}
		};
		Maze testMaze = new Maze(width, height, start_x, start_y, end_x, end_y, maze_data);
		System.out.println(testMaze);
		
		testMaze = Maze.readFromFile("input.txt");
		System.out.println(testMaze);
		
		testMaze = Maze.readFromFile("small.txt");
		System.out.println(testMaze);
		
		testMaze = Maze.readFromFile("medium_input.txt");
		System.out.println(testMaze);
		
		testMaze = Maze.readFromFile("large_input.txt");
		System.out.println(testMaze);
		
		testMaze = Maze.readFromFile("sparse_medium.txt");
		System.out.println(testMaze);
	}

}
