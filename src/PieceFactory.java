class PieceFactory
{
    public static final int[][] I =
    {
        {0, 0, 0, 0},
        {2, 2, 2, 2},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };

    public static final int[][] O =
    {
        {3, 3},
        {3, 3}
    };

    public static final int[][] T =
    {
        {0, 4, 0},
        {4, 4, 4},
        {0, 0, 0}
    };

    public static final int[][] S =
    {
        {0, 5, 5},
        {5, 5, 0},
        {0, 0, 0}
    };

    public static final int[][] Z =
    {
        {6, 6, 0},
        {0, 6, 6},
        {0, 0, 0}
    };

    public static final int[][] J =
    {
        {7, 0, 0},
        {7, 7, 7},
        {0, 0, 0}
    };

    public static final int[][] L =
    {
        {0, 0, 8},
        {8, 8, 8},
        {0, 0, 0}
    };

    public static Piece getRandomPiece()
    {
        int choice = (int)(Math.random() * 7);
        int[][] shape;

        switch (choice)
        {
            case 0: shape = I; break;
            case 1: shape = O; break;
            case 2: shape = T; break;
            case 3: shape = S; break;
            case 4: shape = Z; break;
            case 5: shape = J; break;
            case 6: shape = L; break;
            default: shape = O;
        }

        int[][] copiedShape = copyShape(shape);

        int initialRow = 0;
        int initialCol = (10 - copiedShape[0].length) / 2;

        return new Piece(copiedShape, initialRow, initialCol);
    }

    private static int[][] copyShape(int[][] shape)
    {
        int[][] copy = new int[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++)
        {
            System.arraycopy(shape[i], 0, copy[i], 0, shape[i].length);
        }
        return copy;
    }
}
