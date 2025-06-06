import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

class Game
{
    private Board board;
    private static Jetris jetris;
    private boolean gameOver;
    public static boolean classicMode = false;
    private int score;
    private Piece currentPiece;
    private Timer gameTimer;
    private final long fallInterval = 500; // milliseconds
    private long lastFallTime;

    public Game()
    {
        board = new Board(this);
        gameOver = false;
        score = 0;
        spawnNewPiece();
        lastFallTime = System.currentTimeMillis();
    }

    public void start()
    {
        startInputThread();

        final int targetFPS = 30;
        final int frameDuration = 1000 / targetFPS;

        gameTimer = new Timer(frameDuration, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!gameOver)
                {
                    update();
                }
                else
                {
                    System.out.println("Score: " + score);
                    gameTimer.stop();
                }
            }
        });
        gameTimer.start();
    }

    private void update()
    {
        long now = System.currentTimeMillis();

        if (now - lastFallTime >= fallInterval)
        {
            if (board.canMove(currentPiece, 1, 0))
            {
                currentPiece.moveDown();
            }
            else
            {
                board.lockPiece(currentPiece);
                checkFullRows();
                spawnNewPiece();
                if (!board.canMove(currentPiece, 0, 0))
                {
                    gameOver = true;
                }
            }
            lastFallTime = now;
        }
        draw();
    }

    private void draw()
    {
        board.draw(currentPiece);
    }

    public void checkFullRows()
    {
        int rowsCleared = board.clearFullRows();
        //score += rowsCleared * 100;
        if (rowsCleared == 1)
        {
            score += 40;
        }
        if (rowsCleared == 2)
        {
            score += 100;
        }
        if (rowsCleared == 3)
        {
            score += 300;
        }
        if (rowsCleared == 4)
        {
            score += 1200;
        }
    }

    public void spawnNewPiece()
    {
        currentPiece = PieceFactory.getRandomPiece();
    }

    public Board getBoard()
    {
        return board;
    }

    public int getScore()
    {
        return score;
    }

    public Piece getCurrentPiece()
    {
        return currentPiece;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public void setGameOver(boolean over)
    {
        gameOver = over;
    }

    public void reset()
    {
        if (gameTimer != null) {
            gameTimer.stop(); // Important to stop the old timer
        }

        board = new Board(this);
        score = 0;
        gameOver = false;
        spawnNewPiece();
        lastFallTime = System.currentTimeMillis();

        start(); // restart timer loop
    }


    // Thread that runs alongside the main thread to read user input
    private static void startInputThread()
    {
        new Thread(() ->
        {
            Scanner scanner = new Scanner(System.in);
            while (true)
            {
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("classic()"))
                {
                    Game.classicMode = true;
                    JetrisWindow.setClassic(true);  // Set classic mode in GUI
                    System.out.println("ASCII mode enabled.");
                }
                else if (input.equals("graphic()"))
                {
                    Game.classicMode = false;
                    JetrisWindow.setClassic(false);  // Set GUI back to graphic
                    System.out.println("Graphic mode only.");
                }
            }
        }).start();
    }

}
