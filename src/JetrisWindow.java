import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JetrisWindow extends JPanel implements KeyListener
{
    private static final int TILE_SIZE = 30;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;

    static JTextField textField;

    private static final boolean textVisible = false;

    private static boolean classicMode = false;

    private static boolean readyToRestart = false;

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
        if (!game.isGameOver() && textField != null)
        {
            textField.setVisible(false);
        }

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
        g.setFont(new Font("Consolas", Font.BOLD, 40));

        String scoreText = "Score: " + game.getScore();
        int centerX = getWidth()/ 6;
        int centerY = getHeight()/ 2;
        g.drawString(scoreText, centerX, tileSize);
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        g.drawString("Controls:", centerX, tileSize * 2);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("← ↓ → to move", centerX, tileSize * 3);
        g.drawString("Space to drop", centerX, tileSize * 4);
        g.drawString("Z and C to spin", centerX, tileSize * 5);
        if (game.isGameOver())
        {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,getWidth(),getHeight());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.fillRect(((getWidth()/ 10) * 3), ((getHeight()/ 2) - (getHeight()/ 10)), ((getWidth()/ 10) * 4), (getHeight()/ 4));
            g2d.setComposite(AlphaComposite.SrcOver);

            String text = "Game Over";
            Font font = new Font("Consolas", Font.BOLD, 50);
            g2d.setFont(font);

            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = centerY;

            GlyphVector glyphVector = font.createGlyphVector(metrics.getFontRenderContext(), text);
            Shape textShape = glyphVector.getOutline(x, y);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(8));
            g2d.draw(textShape);
            g2d.setColor(Color.WHITE);
            g2d.fill(textShape);

            if (readyToRestart)
            {
                text = "Press Enter to play";
            }
            else
            {
                text = "Enter name to submit score:";
            }
            font = new Font("Consolas", Font.BOLD, 30);
            g2d.setFont(font);

            metrics = g2d.getFontMetrics();
            textWidth = metrics.stringWidth(text);
            textHeight = metrics.getHeight();
            x = (getWidth() - textWidth) / 2;
            y = (getHeight() / 2) + textHeight;

            glyphVector = font.createGlyphVector(metrics.getFontRenderContext(), text);
            textShape = glyphVector.getOutline(x, y);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(textShape);
            g2d.setColor(Color.WHITE);
            g2d.fill(textShape);

            if (textField != null && !readyToRestart)
            {
                textField.setBounds(x, y + 10, textWidth, 35); // <-- taller height
                textField.setVisible(true);
                textField.requestFocusInWindow();
            }
        }
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
                if (game.isGameOver() && readyToRestart && !textField.isVisible()) {
                    game.reset();
                    readyToRestart = false;

                    textField.setEnabled(true);
                    textField.setText(null);

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
        panel.setLayout(null); // allow absolute positioning

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.add(panel);
        frame.setVisible(true);

        textField = new JTextField(20);
        textField.setFont(new Font("Consolas", Font.BOLD, 30));
        textField.setPreferredSize(new Dimension(400, 50));
        textField.setCaretColor(Color.BLACK);
        textField.addActionListener(e ->
        {
            String playerName = textField.getText().trim();

            if (!playerName.isEmpty())
            {
                System.out.println("Player submitted name: " + playerName);
                textField.setToolTipText("Enter your name and press Enter");
                textField.setText(" ");

                readyToRestart = true;

                int score = game.getScore();
                Leaderboard.newScore(playerName, score);

                textField.setVisible(false);
                textField.setEnabled(false);
                textField.setText(null);

                SwingUtilities.getWindowAncestor(textField).repaint();
            }
        });

        textField.setVisible(false);
        panel.add(textField);

        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
}
