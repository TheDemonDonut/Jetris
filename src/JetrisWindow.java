import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JetrisWindow extends JPanel implements KeyListener
{
    private static final int TILE_SIZE = 30;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;

    private static boolean classicMode = false;

    private final Game game;

    public JetrisWindow(Game game)
    {
        this.game = game;
        setBackground(Color.BLACK);
        addKeyListener(this);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                repaint();
            }
        }, 0, 1000 / 30); // 30 FPS
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (classicMode)
            return;

        int panelWidth = getWidth();
        int panelHeight = getHeight() - 30; // reserve some space for score

        int tileSize = Math.min(panelWidth / WIDTH, panelHeight / HEIGHT);

        int xOffset = (panelWidth - (tileSize * WIDTH)) / 2;
        int yOffset = (panelHeight - (tileSize * HEIGHT)) / 2;

        Board board = game.getBoard();
        Piece piece = game.getCurrentPiece();

        int[][] tempGrid = board.getGrid();
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
                    if (row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH)
                    {
                        tempGrid[row][col] = shape[i][j];
                    }
                }
            }
        }

        for (int r = 0; r < HEIGHT; r++)
        {
            for (int c = 0; c < WIDTH; c++)
            {
                drawTile(g, c, r, tempGrid[r][c], tileSize, xOffset, yOffset);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("Score: " + game.getScore(), 10, panelHeight + 25);
    }

    private void drawTile(Graphics g, int x, int y, int type, int tileSize, int xOffset, int yOffset)
    {
        Color color = switch (type)
        {
            case 1 -> Color.CYAN;
            case 2 -> Color.YELLOW;
            case 3 -> Color.MAGENTA;
            case 4 -> Color.GREEN;
            case 5 -> Color.RED;
            case 6 -> Color.BLUE;
            case 7 -> Color.ORANGE;
            case 8 -> Color.CYAN;
            default -> Color.DARK_GRAY;
        };

        int px = xOffset + x * tileSize;
        int py = yOffset + y * tileSize;

        g.setColor(color);
        g.fillRect(px, py, tileSize, tileSize);
        g.setColor(Color.BLACK);
        g.drawRect(px, py, tileSize, tileSize);
    }


    @Override
    public void keyPressed(KeyEvent e)
    {
        Piece piece = game.getCurrentPiece();
        Board board = game.getBoard();

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_LEFT:
                if (board.canMove(piece, 0, -1))
                {
                    piece.moveLeft();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (board.canMove(piece, 0, 1))
                {
                    piece.moveRight();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (board.canMove(piece, 1, 0))
                {
                    piece.moveDown();
                }
                break;
            case KeyEvent.VK_Z:
                piece.tryRotateCounterClockwise(board);
                break;
            case KeyEvent.VK_C:
                piece.tryRotateClockwise(board);
                break;
            case KeyEvent.VK_SPACE:
                while (board.canMove(piece, 1, 0))
                {
                    piece.moveDown();
                }
                board.lockPiece(piece);
                game.checkFullRows();
                game.spawnNewPiece();
                if (!board.canMove(game.getCurrentPiece(), 0, 0))
                {
                    System.out.println("Game Over!");
                    game.setGameOver(true);
                }
                break;
            case KeyEvent.VK_ENTER:
                if (game.isGameOver()) {
                    game.reset();
                    repaint();
                }
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    public static void setClassic(boolean classic)
    {
        classicMode = classic;
    }

    public static void launch(Game game)
    {
        JFrame frame = new JFrame("Jetris");
        JetrisWindow panel = new JetrisWindow(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        frame.setUndecorated(true); // Optional: remove window borders
        frame.add(panel);
        frame.setVisible(true);

        frame.setVisible(true);
        panel.setFocusable(true); // ensure this
        panel.requestFocusInWindow(); // required!
    }
}
