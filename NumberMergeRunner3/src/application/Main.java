package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;

public class Main extends Application {
    private Stage primaryStage;
    private WelcomeScreen welcomeScreen;
    private GameScreen gameScreen;
    private String selectedDifficulty;
    private String playerName;
    private double screenWidth;
    private double screenHeight;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Number Merge Runner");

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.screenWidth = screenBounds.getWidth();
        this.screenHeight = screenBounds.getHeight();

        welcomeScreen = new WelcomeScreen(screenWidth, screenHeight, () -> {
            showDifficultyScreen();
        });

        Scene scene = new Scene(welcomeScreen, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    public void showDifficultyScreen() {
        DifficultyScreen difficultyScreen = new DifficultyScreen(screenWidth, screenHeight, this::startGame);
        Scene scene = new Scene(difficultyScreen, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");	
        difficultyScreen.requestFocus();
    }

    private void startGame(String difficulty, String playerName) {
        this.selectedDifficulty = difficulty;
        this.playerName = playerName;
        showGameScreen();
    }

    private void showGameScreen() {
        CheckpointList checkpointList = new CheckpointList();
        checkpointList.generateCheckpointsForDifficulty(selectedDifficulty);
        Game game = new Game(checkpointList.getCheckpoints(), selectedDifficulty);
        
        gameScreen = new GameScreen(screenWidth, screenHeight, game, playerName, this);
        Scene scene = new Scene(gameScreen, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        gameScreen.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
