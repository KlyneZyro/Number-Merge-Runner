package application;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class KnapsackPanel extends Pane {
    private double width, height;
    private Game game;
    private Text showKnapsackButton, hideKnapsackButton;
    private List<Text> knapsackTexts;
    private boolean knapsackVisible;

    public KnapsackPanel(double width, double height, Game game) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.knapsackTexts = new ArrayList<>();
        this.knapsackVisible = false;
        initializePanel();
    }

    private void initializePanel() {
        setPrefSize(300, height);
        setLayoutX(0);
        setLayoutY(0);
        setStyle("-fx-background-color: rgba(46, 46, 31, 0.9);");
        setVisible(false);

        showKnapsackButton = new Text("Show Knapsack");
        showKnapsackButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        showKnapsackButton.setFill(Color.web("#D4D4AA"));
        showKnapsackButton.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        showKnapsackButton.setX(150);
        showKnapsackButton.setY(30);
        showKnapsackButton.setOnMouseClicked(e -> show());
        showKnapsackButton.setOnMouseEntered(e -> showKnapsackButton.setFill(Color.web("#FFFF55")));
        showKnapsackButton.setOnMouseExited(e -> showKnapsackButton.setFill(Color.web("#D4D4AA")));

        hideKnapsackButton = new Text("Hide Knapsack");
        hideKnapsackButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        hideKnapsackButton.setFill(Color.web("#D4D4AA"));
        hideKnapsackButton.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        hideKnapsackButton.setX(150);
        hideKnapsackButton.setY(70);
        hideKnapsackButton.setOnMouseClicked(e -> hide());
        hideKnapsackButton.setOnMouseEntered(e -> hideKnapsackButton.setFill(Color.web("#FFFF55")));
        hideKnapsackButton.setOnMouseExited(e -> hideKnapsackButton.setFill(Color.web("#D4D4AA")));
        hideKnapsackButton.setVisible(false);
    }

    public Text getShowKnapsackButton() {
        return showKnapsackButton;
    }

    public Text getHideKnapsackButton() {
        return hideKnapsackButton;
    }

    public void show() {
        if (knapsackVisible) return;
        knapsackVisible = true;
        setVisible(true);
        showKnapsackButton.setVisible(false);
        hideKnapsackButton.setVisible(true);

        List<String> optimalChoices = game.computeOptimalKnapsack();
        getChildren().clear();
        knapsackTexts.clear();

        Text title = new Text("Optimal Knapsack");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.web("#FFFF55"));
        title.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        title.setX(150 - title.getLayoutBounds().getWidth() / 2);
        title.setY(30);
        getChildren().add(title);

        int totalValue = 0, totalWeight = 0;
        for (int i = 0; i < optimalChoices.size(); i++) {
            String choice = optimalChoices.get(i);
            Checkpoint cp = game.getCheckpoints().get(i);
            Block chosenBlock = choice.equals("left") ? cp.getLeftBlock() : cp.getRightBlock();
            totalValue += chosenBlock.getValue();
            totalWeight += chosenBlock.getWeight();

            Text text = new Text("Checkpoint " + (i + 1) + ": " + choice + " (Value: " + chosenBlock.getValue() + ", Weight: " + chosenBlock.getWeight() + ")");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            text.setFill(Color.web("#D4D4AA"));
            text.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
            text.setX(20);
            text.setY(120 + i * 50);
            text.setWrappingWidth(260);
            text.setOpacity(0);
            knapsackTexts.add(text);

            FadeTransition fade = new FadeTransition(Duration.millis(500), text);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(i * 500));
            fade.play();
        }

        Text totalText = new Text("Total Value: " + totalValue + ", Total Weight: " + totalWeight);
        totalText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        totalText.setFill(totalWeight <= game.getWeightLimit() ? Color.web("#D4D4AA") : Color.web("#FF5555"));
        totalText.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        totalText.setX(20);
        totalText.setY(120 + optimalChoices.size() * 50 + 40);
        totalText.setWrappingWidth(260);
        totalText.setOpacity(0);
        knapsackTexts.add(totalText);

        FadeTransition fadeTotal = new FadeTransition(Duration.millis(500), totalText);
        fadeTotal.setFromValue(0);
        fadeTotal.setToValue(1);
        fadeTotal.setDelay(Duration.millis(optimalChoices.size() * 500));
        fadeTotal.play();

        getChildren().addAll(knapsackTexts);
    }

    public void hide() {
        if (!knapsackVisible) return;
        knapsackVisible = false;
        setVisible(false);
        showKnapsackButton.setVisible(true);
        hideKnapsackButton.setVisible(false);
        getChildren().clear();
        knapsackTexts.clear();
    }
}