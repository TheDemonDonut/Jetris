public class Jetris
{
    public static void main(String[] args)
    {
        System.out.println("Starting game...");
        Game game = new Game();
        JetrisWindow.launch(game);
        game.start();
    }
}
