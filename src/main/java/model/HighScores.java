package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScores {

    public static final Comparator<GameResult> GAME_RESULT_COMPARATOR =
            Comparator.comparing(GameResult::getGameLevel)
            .thenComparingLong(GameResult::getGameDuration).reversed()
            .thenComparing((item1, item2) -> item1.getPlayerName().compareToIgnoreCase(item2.getPlayerName()));

    private final String filePath;
    private final ArrayList<GameResult> list = new ArrayList<>();

    public HighScores(String filePath) {
        this.filePath = filePath;
    }

    public boolean add(GameResult gameResult) {
        if (!list.contains(gameResult)) {
            list.add(gameResult);
            list.sort(GAME_RESULT_COMPARATOR);
            return true;
        }
        return false;
    }

    public void load() {
        try {
            if (Files.isReadable(Path.of(filePath))) {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<GameResult> loadedList = (ArrayList<GameResult>) objectInputStream.readObject();
                list.clear();
                list.addAll(loadedList);
                objectInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GameResult> getList() {
        return Collections.unmodifiableList(list);
    }

}
