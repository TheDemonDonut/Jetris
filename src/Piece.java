class Piece {
    private int[][] shape;
    private int row, col;

    public Piece(int[][] shape, int row, int col)
    {
        this.shape = shape;
        this.row = row;
        this.col = col;
    }

    public int[][] getShape()
    {
        return shape;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void moveDown()
    {
        row++;
    }

    public void moveLeft()
    {
        col--;
    }

    public void moveRight()
    {
        col++;
    }

    public void rotateClockwise()
    {
        int n = shape.length;
        int[][] rotated = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                rotated[j][n - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }

    public void rotateCounterClockwise()
    {
        int n = shape.length;
        int[][] rotated = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                rotated[n - 1 - j][i] = shape[i][j];
            }
        }
        shape = rotated;
    }

    public void tryRotateClockwise(Board board)
    {
        rotateClockwise();
        if (!board.canMove(this, 0, 0))
        {
            if (board.canMove(this, 0, -1))
            {
                col -= 1;
            }
            else if (board.canMove(this, 0, 1))
            {
                col += 1;
            }
            else {
                rotateCounterClockwise();
            }
        }
    }

    public void tryRotateCounterClockwise(Board board)
    {
        rotateCounterClockwise();
        if (!board.canMove(this, 0, 0))
        {
            if (board.canMove(this, 0, -1))
            {
                col -= 1;
            }
            else if (board.canMove(this, 0, 1))
            {
                col += 1;
            }
            else
            {
                rotateClockwise();
            }
        }
    }
}