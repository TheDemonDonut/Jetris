import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.lang.reflect.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

public class Leaderboard extends JPanel
{
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static List<ScoreEntry> leaderboard = new ArrayList<>();
    public static File file = new File("src/leaderBoard.json");

    public Leaderboard()
    {
        setBackground(new Color(13,17,23));

        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                repaint();
            }
        }, 0, 1000 / 30);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 50));
        g.drawString("Leaderboard:", 5, 50);

        g.setFont(new Font("Consolas", Font.BOLD, 30));

        List<ScoreEntry> tempLeaderboard = sortLeaderboard();
        int d = 50;
        for (int i = 0; i < tempLeaderboard.size(); i++)
        {
            d += 30;
            g.drawString(tempLeaderboard.get(i).getScore() + " ".repeat(7 - String.valueOf(tempLeaderboard.get(i).getScore()).length()) + ": " + tempLeaderboard.get(i).getName(), 5, d);
        }
    }

    public static void newScore(String name, int score)
    {
        ArrayList<ScoreEntry> tempLeaderboard;
        tempLeaderboard = (ArrayList<ScoreEntry>) readLeaderboard();
        tempLeaderboard.add(new ScoreEntry(name,score));
        writeLeaderboard(tempLeaderboard);
    }

    public static List<ScoreEntry> readLeaderboard()
    {
        try (FileReader reader = new FileReader(file))
        {
            Type listType = new TypeToken<List<ScoreEntry>>() {}.getType();
            leaderboard = gson.fromJson(reader, listType);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return leaderboard;
    }

    public static void writeLeaderboard(ArrayList<ScoreEntry> leaderboard)
    {
        try (FileWriter writer = new FileWriter(file))
        {
            gson.toJson(leaderboard, writer);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<ScoreEntry> sortLeaderboard()
    {
        List<ScoreEntry> tempLeaderboard = readLeaderboard();
        List<Integer> justScores = new ArrayList<>();
        List<String> justNames = new ArrayList<>();
        for (int i = 0; i < tempLeaderboard.size(); i++)
        {
            justScores.add(tempLeaderboard.get(i).getScore());
            justNames.add(tempLeaderboard.get(i).getName());
        }
        for (int i = 0; i < tempLeaderboard.size() - 1; i++)
        {
            int max_idx = i;

            for (int j = i + 1; j < tempLeaderboard.size(); j++)
            {
                if (justScores.get(j) > justScores.get(max_idx))
                {
                    max_idx = j;
                }
            }

            int tempInt = justScores.get(i);
            String tempName = justNames.get(i);
            justScores.set(i, justScores.get(max_idx));
            justNames.set(i, justNames.get(max_idx));
            justScores.set(max_idx, tempInt);
            justNames.set(max_idx, tempName);
        }
        for (int i = 0; i < tempLeaderboard.size(); i++)
        {

            tempLeaderboard.set(i, new ScoreEntry(justNames.get(i), justScores.get(i)));
        }
        return tempLeaderboard;
    }

    public static void launchLeaderboard(Game game)
    {
        JFrame leaderboardFrame = new JFrame("Leaderboard");
        leaderboardFrame.setSize(500, 500);
        leaderboardFrame.setResizable(true);
        leaderboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Leaderboard leaderboardPanel = new Leaderboard();  // Create the panel
        leaderboardFrame.add(leaderboardPanel);            // Add it to the frame

        leaderboardFrame.setVisible(true);
    }
}
