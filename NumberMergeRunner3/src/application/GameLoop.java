package application;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private GameScreen gameScreen;
    private EnvironmentRenderer environmentRenderer;
    private BlockManager blockManager;
    private PlayerController playerController;
    private UIManager uiManager;

    public GameLoop(GameScreen gameScreen, EnvironmentRenderer environmentRenderer,
                    BlockManager blockManager, PlayerController playerController,
                    UIManager uiManager) {
        this.gameScreen = gameScreen;
        this.environmentRenderer = environmentRenderer;
        this.blockManager = blockManager;
        this.playerController = playerController;
        this.uiManager = uiManager;
    }

    @Override
    public void handle(long now) {
        if (!gameScreen.isGameEnded()) {
            environmentRenderer.update();
            blockManager.update(playerController.getPlayer().getCenterX(),
                    playerController.getPlayer().getCenterY());
            uiManager.updateStatsPosition();
        } else {
            stop(); // Explicitly stop the animation timer
        }
    }
}