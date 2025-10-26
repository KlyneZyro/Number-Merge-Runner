package application;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private double width, height;
    private Game game;
    private Circle player;
    private Text statsText;
    private Text checkpointText;
    private Text startPrompt;
    private Text resultText;
    private List<Text> texts;

    public UIManager(double width, double height, Game game, Circle player, String playerName) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.player = player;
        this.texts = new ArrayList<>();
        initializeUI(playerName);
    }

    private void initializeUI(String playerName) {
        statsText = new Text("Value: 0, Weight: 0");
        statsText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        statsText.setFill(Color.web("#D4D4AA"));
        statsText.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        updateStatsPosition();
        texts.add(statsText);

        checkpointText = new Text("Checkpoint 1/" + game.getCheckpoints().size());
        checkpointText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        checkpointText.setFill(Color.web("#FFFF55"));
        checkpointText.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        checkpointText.setX(width - 220);
        checkpointText.setY(50);
        texts.add(checkpointText);

        startPrompt = new Text("Press Space to Start, " + (playerName.isEmpty() ? "Player" : playerName));
        startPrompt.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        startPrompt.setFill(Color.web("#FFFF55"));
        startPrompt.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        startPrompt.setX(width / 2 - startPrompt.getLayoutBounds().getWidth() / 2);
        startPrompt.setY(height / 2);
        texts.add(startPrompt);
    }

    public List<Text> getTexts() {
        return texts;
    }

    public Text getStartPrompt() {
        return startPrompt;
    }

    public Text getResultText() {
        return resultText;
    }

    public void updateStats() {
        int value = game.getPlayer().getTotalValue();
        int weight = game.getPlayer().getTotalWeight();
        statsText.setText("Value: " + value + ", Weight: " + weight);
        Color statsColor = weight > game.getWeightLimit() ? Color.web("#FF5555") : Color.web("#D4D4AA");
        statsText.setFill(statsColor);
        updateStatsPosition();
    }

    public void updateStatsPosition() {
        statsText.setX(player.getCenterX() - statsText.getLayoutBounds().getWidth() / 2);
        statsText.setY(player.getCenterY() - 40);
    }

    public void updateCheckpointText(int current, int total) {
        checkpointText.setText("Checkpoint " + current + "/" + total);
    }

    public void showResult(boolean victory) {
        String resultMessage = victory ? "Victory!" : "Defeat: " +
                (game.getPlayer().getTotalWeight() > game.getWeightLimit() ? "Weight too high!" : "Not enough value!");
        resultText = new Text(resultMessage);
        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        resultText.setFill(victory ? Color.web("#FFFF55") : Color.web("#FF5555"));
        resultText.setEffect(new DropShadow(10, 3, 3, Color.BLACK));
        resultText.setX(width / 2 - resultText.getLayoutBounds().getWidth() / 2);
        resultText.setY(height / 2);
    }
}