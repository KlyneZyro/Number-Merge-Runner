package application;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import java.util.function.BiConsumer;

public class DifficultyScreen extends Pane {
    private double width, height;
    private Text title;
    private Text easyButton, mediumButton, hardButton;
    private Text easyWeight, mediumWeight, hardWeight;
    private BiConsumer<String, String> onStartGame;

    public DifficultyScreen(double width, double height, BiConsumer<String, String> onStartGame) {
        this.width = width;
        this.height = height;
        this.onStartGame = onStartGame;

        setStyle("-fx-background-color: linear-gradient(to bottom, #2E2E1F, #8A8A5C);");

        title = new Text("Select Difficulty");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setFill(Color.web("#FFFF55"));
        title.setEffect(new DropShadow(10, 3, 3, Color.BLACK));
        title.setX(width / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(150);

        easyButton = createButton("Easy", height / 2 - 50);
        mediumButton = createButton("Medium", height / 2 + 50);
        hardButton = createButton("Hard", height / 2 + 150);

        easyWeight = createWeightText("Weight Limit: 10", height / 2 - 50);
        mediumWeight = createWeightText("Weight Limit: 15", height / 2 + 50);
        hardWeight = createWeightText("Weight Limit: 20", height / 2 + 150);

        getChildren().addAll(title, easyButton, mediumButton, hardButton, easyWeight, mediumWeight, hardWeight);

        easyButton.setOnMouseClicked(e -> onStartGame.accept("easy", ""));
        mediumButton.setOnMouseClicked(e -> onStartGame.accept("medium", ""));
        hardButton.setOnMouseClicked(e -> onStartGame.accept("hard", ""));
    }

    private Text createButton(String text, double y) {
        Text button = new Text(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        button.setFill(Color.web("#D4D4AA"));
        button.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        button.setX(width / 2 - button.getLayoutBounds().getWidth() / 2 - 100);
        button.setY(y);
        button.setOnMouseEntered(e -> {
            button.setFill(Color.web("#FFFF55"));
            button.setScaleX(1.1);
            button.setScaleY(1.1);
        });
        button.setOnMouseExited(e -> {
            button.setFill(Color.web("#D4D4AA"));
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
        return button;
    }

    private Text createWeightText(String text, double y) {
        Text weightText = new Text(text);
        weightText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        weightText.setFill(Color.web("#D4D4AA"));
        weightText.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        weightText.setX(width / 2 + 50);
        weightText.setY(y);
        return weightText;
    }
}