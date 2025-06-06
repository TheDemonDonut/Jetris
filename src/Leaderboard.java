import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard
{
    public static Gson gson = new Gson();
    public static List<ScoreEntry> leaderboard = new ArrayList<>();
    public static File file = new File("D:\\GitHub\\Jetris\\src\\leaderBoard.json");

    public static void newScore(String name, int score)
    {
        ArrayList<ScoreEntry> tempLeaderboard;
        tempLeaderboard = (ArrayList<ScoreEntry>) readLeaderboard();
        tempLeaderboard.add(new ScoreEntry(name,score));
        writeLeaderboard(tempLeaderboard);
    }

    public static void printLeaderboard()
    {
        for (ScoreEntry entry : leaderboard)
        {
            System.out.println(entry.getName() + ": " + entry.getScore());
        }
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
        for (ScoreEntry entry : leaderboard)
        {
            System.out.println(entry.getName() + " : " + entry.getScore());
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
}
