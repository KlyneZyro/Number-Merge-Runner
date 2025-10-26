package application;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

public class WelcomeScreen extends Pane {
    private Runnable onStart;

    public WelcomeScreen(double width, double height, Runnable onStart) {
        this.onStart = onStart;

        LinearGradient bgGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, javafx.scene.paint.Color.DARKGREEN),
                new Stop(1, javafx.scene.paint.Color.FORESTGREEN));
        setBackground(new javafx.scene.layout.Background(
                new javafx.scene.layout.BackgroundFill(bgGradient, null, null)));

        Text title = new Text("Number Merge Runner");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        title.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, javafx.scene.paint.Color.GOLD),
                new Stop(1, javafx.scene.paint.Color.ORANGE)));
        title.setEffect(new DropShadow(10, 5, 5, javafx.scene.paint.Color.BLACK));
        title.setX(width / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(height * 0.3);

        Rectangle button = new Rectangle(200, 60);
        button.setX(width / 2 - 100);
        button.setY(height / 2 - 30);
        button.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, javafx.scene.paint.Color.LIGHTBLUE),
                new Stop(1, javafx.scene.paint.Color.DODGERBLUE)));
        button.setArcWidth(20);
        button.setArcHeight(20);
        button.setEffect(new DropShadow(10, 3, 3, javafx.scene.paint.Color.DARKGRAY));

        Text buttonText = new Text("Start");
        buttonText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        buttonText.setFill(javafx.scene.paint.Color.WHITE);
        buttonText.setX(width / 2 - buttonText.getLayoutBounds().getWidth() / 2);
        buttonText.setY(height / 2 + buttonText.getLayoutBounds().getHeight() / 4);

        button.setOnMouseClicked(this::handleStart);
        buttonText.setOnMouseClicked(this::handleStart);

        getChildren().addAll(title, button, buttonText);
    }

    private void handleStart(MouseEvent event) {
        onStart.run();
    }

    public void fadeOut(Runnable onComplete) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), this);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> onComplete.run());
        fade.play();
    }
}