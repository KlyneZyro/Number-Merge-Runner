package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends Pane {
    private double width, height;
    private Game game;
    private String playerName;
    private int currentCheckpoint;
    private boolean gameStarted;
    private boolean gameEnded;
    private boolean movingToBoss;
    private boolean resultShown;
    private String difficulty;
    private EnvironmentRenderer environmentRenderer;
    private PlayerController playerController;
    private UIManager uiManager;
    private BlockManager blockManager;
    private KnapsackPanel knapsackPanel;
    private BossRenderer bossRenderer;
    private GameLoop gameLoop;
    private List<String> choices;
    private Main main;
    private CheckpointList checkpointList;

    public GameScreen(double width, double height, Game game, String playerName, Main main) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.playerName = playerName.isEmpty() ? "Player" : playerName;
        this.currentCheckpoint = 0;
        this.gameStarted = false;
        this.gameEnded = false;
        this.movingToBoss = false;
        this.resultShown = false;
        this.choices = new ArrayList<>();
        this.main = main;
        this.checkpointList = new CheckpointList();

        // Set difficulty before initializing the scene
        this.difficulty = determineDifficulty();

        initializeScene();
        setupEventHandlers();
        gameLoop.start();

        setFocusTraversable(true);
        this.focusedProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Focus changed: " + newVal + ", gameStarted: " + gameStarted + ", gameEnded: " + gameEnded);
            if (!newVal) {
                this.requestFocus();
            }
        });
        this.requestFocus();
        System.out.println("GameScreen initialized - gameStarted: " + gameStarted + ", gameEnded: " + gameEnded + ", Difficulty: " + this.difficulty);
    }

    private void initializeScene() {
        
        setBackground(new javafx.scene.layout.Background(
                new javafx.scene.layout.BackgroundFill(Color.web("#DDFAB8"), null, null)));

        environmentRenderer = new EnvironmentRenderer(width, height);
        playerController = new PlayerController(width, height, game.getPlayer());
        uiManager = new UIManager(width, height, game, playerController.getPlayer(), playerName);
        blockManager = new BlockManager(width, height, game, this::handleBlockCollection);
        knapsackPanel = new KnapsackPanel(width, height, game);
        bossRenderer = new BossRenderer(width, height, game);
        gameLoop = new GameLoop(this, environmentRenderer, blockManager, playerController, uiManager);

        getChildren().addAll(environmentRenderer.getBackgroundElements());
        getChildren().addAll(environmentRenderer.getLaneMarkings());
        getChildren().addAll(environmentRenderer.getRoadCracks());
        getChildren().add(playerController.getPlayer());
        getChildren().addAll(uiManager.getTexts());
        getChildren().add(knapsackPanel);
        getChildren().add(knapsackPanel.getShowKnapsackButton());
        getChildren().add(knapsackPanel.getHideKnapsackButton());
    }

    private void setupEventHandlers() {
        setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode() + ", gameStarted: " + gameStarted + ", gameEnded: " + gameEnded);
            
            if (event.getCode() == KeyCode.SPACE && !gameStarted && !gameEnded) {
                startGame();
            }
            
            if (gameStarted && !gameEnded) {
                if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                    playerController.getPlayer().setCenterX(width / 2 - 100);
                    uiManager.updateStatsPosition();
                    this.requestFocus();
                } else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                    playerController.getPlayer().setCenterX(width / 2 + 100);
                    uiManager.updateStatsPosition();
                    this.requestFocus();
                }
            }
        });

        playerController.setupEventHandlers(this, uiManager::updateStatsPosition, uiManager::updateStatsPosition);

        setOnMouseClicked(e -> {
            System.out.println("Mouse clicked, requesting focus");
            this.requestFocus();
        });

        knapsackPanel.getShowKnapsackButton().setFocusTraversable(false);
        knapsackPanel.getHideKnapsackButton().setFocusTraversable(false);
    }

    private void startGame() {
        gameStarted = true;
        System.out.println("Game started - gameStarted: " + gameStarted);
        getChildren().remove(uiManager.getStartPrompt());
        List<Node> checkpointElements = blockManager.spawnNextCheckpoint(currentCheckpoint);
        getChildren().addAll(checkpointElements);
        this.requestFocus();
    }

    private void handleBlockCollection(String side) {
        Checkpoint cp = game.getCheckpoints().get(currentCheckpoint);
        Block block = side.equals("left") ? cp.getLeftBlock() : cp.getRightBlock();
        game.getPlayer().addBlock(block);
        choices.add(side);
        uiManager.updateStats();
        proceedToNextCheckpoint();
    }

    private void proceedToNextCheckpoint() {
        if (currentCheckpoint < game.getCheckpoints().size() - 1) {
            currentCheckpoint++;
            uiManager.updateCheckpointText(currentCheckpoint + 1, game.getCheckpoints().size());
            List<Node> checkpointElements = blockManager.spawnNextCheckpoint(currentCheckpoint);
            getChildren().addAll(checkpointElements);
        } else {
            gameEnded = true;
            startMovingToBoss();
        }
    }

    private void startMovingToBoss() {
        if (!movingToBoss) {
            movingToBoss = true;
            bossRenderer.show();
            if (!getChildren().contains(bossRenderer.getBoss())) {
                getChildren().add(bossRenderer.getBoss());
            }
            if (!getChildren().contains(bossRenderer.getBossText())) {
                getChildren().add(bossRenderer.getBossText());
            }
            int playerValue = game.getPlayer().getTotalValue();
            int bossHealth = game.getBossHealth();
            boolean victory = playerValue >= bossHealth && game.getPlayer().getTotalWeight() <= game.getWeightLimit();
            showResult(victory);
        }
    }

    private void showResult(boolean victory) {
        if (!resultShown) {
            uiManager.showResult(victory);
            getChildren().add(uiManager.getResultText());
            resultShown = true;
            gameLoop.stop();
            showEndGameOptions();
        }
    }

    private void showEndGameOptions() {
        Text retryButton = new Text("Retry");
        retryButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        retryButton.setFill(Color.web("#D4D4AA"));
        retryButton.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        retryButton.setX(width / 2 - retryButton.getLayoutBounds().getWidth() / 2 - 150);
        retryButton.setY(height / 2 + 100);
        retryButton.setOnMouseClicked(e -> restartGame());
        retryButton.setOnMouseEntered(e -> retryButton.setFill(Color.web("#FFFF55")));
        retryButton.setOnMouseExited(e -> retryButton.setFill(Color.web("#D4D4AA")));

        Text menuButton = new Text("Menu");
        menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        menuButton.setFill(Color.web("#D4D4AA"));
        menuButton.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        menuButton.setX(width / 2 - menuButton.getLayoutBounds().getWidth() / 2);
        menuButton.setY(height / 2 + 100);
        menuButton.setOnMouseClicked(e -> main.showDifficultyScreen());
        menuButton.setOnMouseEntered(e -> menuButton.setFill(Color.web("#FFFF55")));
        menuButton.setOnMouseExited(e -> menuButton.setFill(Color.web("#D4D4AA")));

        Text exitButton = new Text("Exit");
        exitButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        exitButton.setFill(Color.web("#D4D4AA"));
        exitButton.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
        exitButton.setX(width / 2 - exitButton.getLayoutBounds().getWidth() / 2 + 150);
        exitButton.setY(height / 2 + 100);
        exitButton.setOnMouseClicked(e -> System.exit(0));
        exitButton.setOnMouseEntered(e -> exitButton.setFill(Color.web("#FFFF55")));
        exitButton.setOnMouseExited(e -> exitButton.setFill(Color.web("#D4D4AA")));

        getChildren().addAll(retryButton, menuButton, exitButton);
    }

    private void restartGame() {
        // Stop the current game loop
        gameLoop.stop();

        // Preserve the current difficulty
        String currentDifficulty = this.difficulty;

        // Generate a new set of checkpoints from the same difficulty pool
        checkpointList.generateCheckpointsForDifficulty(currentDifficulty);
        List<Checkpoint> newCheckpoints = checkpointList.getCheckpoints();

        // Create a new Game object with the fresh checkpoints and difficulty
        game = new Game(newCheckpoints, currentDifficulty);

        // Reset game state
        getChildren().clear();
        currentCheckpoint = 0;
        gameStarted = false;
        gameEnded = false;
        movingToBoss = false;
        resultShown = false;
        choices.clear();

        // Ensure the difficulty is preserved
        this.difficulty = currentDifficulty;

        // Reinitialize the scene with the new Game object
        initializeScene();
        setupEventHandlers();
        gameLoop.start();
        System.out.println("Game restarted - gameStarted: " + gameStarted + ", gameEnded: " + gameEnded + ", Difficulty: " + this.difficulty + ", Weight Limit: " + game.getWeightLimit());
    }

    private String determineDifficulty() {
        int weightLimit = game.getWeightLimit();
        if (weightLimit == 10) return "easy";
        if (weightLimit == 15) return "medium";
        return "hard";
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public String getDifficulty() {
        return difficulty;
    }
}