public class Board
{
    private static final int ROWS = 20;
    private static final int COLS = 10;
    private int[][] grid;
    private final Game game;

    public Board(Game game)
    {
        this.game = game;
        grid = new int[ROWS][COLS];
    }

    public void draw(Piece piece)
    {
        if (!Game.classicMode) return;

        int[][] tempGrid = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++)
        {
            System.arraycopy(grid[i], 0, tempGrid[i], 0, COLS);
        }

        int[][] shape = piece.getShape();
        int pr = piece.getRow();
        int pc = piece.getCol();

        for (int i = 0; i < shape.length; i++)
        {
            for (int j = 0; j < shape[i].length; j++)
            {
                if (shape[i][j] != 0)
                {
                    int row = pr + i;
                    int col = pc + j;
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS)
                    {
                        tempGrid[row][col] = shape[i][j];
                    }
                }
            }
        }

        for (int[] row : tempGrid)
        {
            System.out.print("<!");
            for (int cell : row)
            {
                System.out.print(cell == 0 ? " ." : "[]");
            }
            System.out.print("!>");
            System.out.println();
        }
        System.out.println("<!=====score:" + game.getScore() + "=".repeat(9 - String.valueOf(game.getScore()).length()) + "!>");
    }

    public boolean canMove(Piece piece, int rowOffset, int colOffset)
    {
        int[][] shape = piece.getShape();
        int newRow = piece.getRow() + rowOffset;
        int newCol = piece.getCol() + colOffset;

        for (int i = 0; i < shape.length; i++)
        {
            for (int j = 0; j < shape[i].length; j++)
            {
                if (shape[i][j] != 0)
                {
                    int r = newRow + i;
                    int c = newCol + j;
                    if (r < 0 || r >= ROWS || c < 0 || c >= COLS || grid[r][c] != 0)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void lockPiece(Piece piece)
    {
        int[][] shape = piece.getShape();
        int row = piece.getRow();
        int col = piece.getCol();

        for (int i = 0; i < shape.length; i++)
        {
            for (int j = 0; j < shape[i].length; j++)
            {
                if (shape[i][j] != 0) {
                    int r = row + i;
                    int c = col + j;
                    if (r >= 0 && r < ROWS && c >= 0 && c < COLS)
                    {
                        grid[r][c] = shape[i][j];
                    }
                }
            }
        }
    }

    public int clearFullRows()
    {
        int rowsCleared = 0;

        for (int r = 0; r < ROWS; r++)
        {
            boolean full = true;
            for (int c = 0; c < COLS; c++)
            {
                if (grid[r][c] == 0)
                {
                    full = false;
                    break;
                }
            }

            if (full)
            {
                rowsCleared++;
                for (int i = r; i > 0; i--)
                {
                    grid[i] = grid[i - 1].clone();
                }
                grid[0] = new int[COLS];
            }
        }

        return rowsCleared;
    }

    public int[][] getGrid()
    {
        int[][] copy = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++)
        {
            System.arraycopy(grid[i], 0, copy[i], 0, grid[i].length);
        }
        return copy;
    }
}
