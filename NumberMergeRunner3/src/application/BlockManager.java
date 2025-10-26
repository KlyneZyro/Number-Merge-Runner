package application;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Glow;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockManager {
    private double width, height;
    private Game game;
    private List<Circle> leftBlocks;
    private List<Circle> rightBlocks;
    private List<Text> leftBlockTexts;
    private List<Text> rightBlockTexts;
    private double blockY;
    private Consumer<String> onBlockCollected;
    private boolean checkpointCollected; 

    public BlockManager(double width, double height, Game game, Consumer<String> onBlockCollected) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.leftBlocks = new ArrayList<>();
        this.rightBlocks = new ArrayList<>();
        this.leftBlockTexts = new ArrayList<>();
        this.rightBlockTexts = new ArrayList<>();
        this.blockY = -50;
        this.onBlockCollected = onBlockCollected;
        this.checkpointCollected = false;
    }

    public List<Node> spawnNextCheckpoint(int currentCheckpoint) {
        if (currentCheckpoint >= game.getCheckpoints().size()) return new ArrayList<>();

        blockY = -50;
        checkpointCollected = false; // Reset for the new checkpoint
        Checkpoint cp = game.getCheckpoints().get(currentCheckpoint);
        Circle leftBlock = new Circle(width / 2 - 100, blockY, 40);
        leftBlock.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#55AAFF", 0.8)),
                new Stop(1, Color.web("#003366", 0.8))));
        leftBlock.setStroke(Color.WHITE);
        leftBlock.setStrokeWidth(2);

        Lighting lighting = new Lighting();
        lighting.setSurfaceScale(5);
        Glow glow = new Glow(0.6);
        ColorAdjust sparkle = new ColorAdjust();
        sparkle.setBrightness(0.3);
        leftBlock.setEffect(lighting);
        leftBlock.setEffect(glow);
        leftBlock.setEffect(sparkle);

        ScaleTransition pulseLeft = new ScaleTransition(Duration.millis(1000), leftBlock);
        pulseLeft.setFromX(1.0);
        pulseLeft.setFromY(1.0);
        pulseLeft.setToX(1.1);
        pulseLeft.setToY(1.1);
        pulseLeft.setAutoReverse(true);
        pulseLeft.setCycleCount(ScaleTransition.INDEFINITE);
        pulseLeft.play();

        Text leftBlockText = new Text("V: " + cp.getLeftBlock().getValue() + ", W: " + cp.getLeftBlock().getWeight());
        leftBlockText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        leftBlockText.setFill(Color.web("#D4D4AA"));
        leftBlockText.setEffect(new DropShadow(3, 1, 1, Color.BLACK));
        leftBlockText.setX(width / 2 - 100 - leftBlockText.getLayoutBounds().getWidth() / 2);
        leftBlockText.setY(blockY + 60);

        Circle rightBlock = new Circle(width / 2 + 100, blockY, 40);
        rightBlock.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#55AAFF", 0.8)),
                new Stop(1, Color.web("#003366", 0.8))));
        rightBlock.setStroke(Color.WHITE);
        rightBlock.setStrokeWidth(2);

        rightBlock.setEffect(lighting);
        rightBlock.setEffect(glow);
        rightBlock.setEffect(sparkle);

        ScaleTransition pulseRight = new ScaleTransition(Duration.millis(1000), rightBlock);
        pulseRight.setFromX(1.0);
        pulseRight.setFromY(1.0);
        pulseRight.setToX(1.1);
        pulseRight.setToY(1.1);
        pulseRight.setAutoReverse(true);
        pulseRight.setCycleCount(ScaleTransition.INDEFINITE);
        pulseRight.play();

        Text rightBlockText = new Text("V: " + cp.getRightBlock().getValue() + ", W: " + cp.getRightBlock().getWeight());
        rightBlockText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        rightBlockText.setFill(Color.web("#D4D4AA"));
        rightBlockText.setEffect(new DropShadow(3, 1, 1, Color.BLACK));
        rightBlockText.setX(width / 2 + 100 - rightBlockText.getLayoutBounds().getWidth() / 2);
        rightBlockText.setY(blockY + 60);

        leftBlocks.add(leftBlock);
        leftBlockTexts.add(leftBlockText);
        rightBlocks.add(rightBlock);
        rightBlockTexts.add(rightBlockText);

        List<Node> elements = new ArrayList<>();
        elements.add(leftBlock);
        elements.add(leftBlockText);
        elements.add(rightBlock);
        elements.add(rightBlockText);
        return elements;
    }

    public List<Circle> getLeftBlocks() {
        return leftBlocks;
    }

    public List<Circle> getRightBlocks() {
        return rightBlocks;
    }

    public List<Text> getLeftBlockTexts() {
        return leftBlockTexts;
    }

    public List<Text> getRightBlockTexts() {
        return rightBlockTexts;
    }

    public void update(double playerX, double playerY) {
        blockY += 2;

        for (int i = 0; i < leftBlocks.size(); i++) {
            Circle leftBlock = leftBlocks.get(i);
            Text leftText = leftBlockTexts.get(i);
            Circle rightBlock = rightBlocks.get(i);
            Text rightText = rightBlockTexts.get(i);

            leftBlock.setCenterY(blockY);
            leftText.setY(blockY + 60);
            rightBlock.setCenterY(blockY);
            rightText.setY(blockY + 60);

            if (blockY > height) {
                leftBlocks.remove(i);
                leftBlockTexts.remove(i);
                rightBlocks.remove(i);
                rightBlockTexts.remove(i);
                i--;
            } else if (!checkpointCollected && blockY >= height - 230 && blockY <= height - 170) {
                if (Math.abs(playerX - leftBlock.getCenterX()) < 40 && Math.abs(playerY - leftBlock.getCenterY()) < 40) {
                    checkpointCollected = true;
                    onBlockCollected.accept("left");
                    fadeOutAndRemove(i);
                    i--;
                } else if (Math.abs(playerX - rightBlock.getCenterX()) < 40 && Math.abs(playerY - rightBlock.getCenterY()) < 40) {
                    checkpointCollected = true;
                    onBlockCollected.accept("right");
                    fadeOutAndRemove(i);
                    i--;
                }
            }
        }
    }

    private void fadeOutAndRemove(int index) {
        Circle leftBlock = leftBlocks.get(index);
        Text leftText = leftBlockTexts.get(index);
        Circle rightBlock = rightBlocks.get(index);
        Text rightText = rightBlockTexts.get(index);

        FadeTransition fadeLeftBlock = new FadeTransition(Duration.millis(500), leftBlock);
        fadeLeftBlock.setFromValue(1.0);
        fadeLeftBlock.setToValue(0.0);

        FadeTransition fadeLeftText = new FadeTransition(Duration.millis(500), leftText);
        fadeLeftText.setFromValue(1.0);
        fadeLeftText.setToValue(0.0);

        FadeTransition fadeRightBlock = new FadeTransition(Duration.millis(500), rightBlock);
        fadeRightBlock.setFromValue(1.0);
        fadeRightBlock.setToValue(0.0);

        FadeTransition fadeRightText = new FadeTransition(Duration.millis(500), rightText);
        fadeRightText.setFromValue(1.0);
        fadeRightText.setToValue(0.0);

        fadeLeftBlock.setOnFinished(e -> {
            leftBlocks.remove(index);
            leftBlockTexts.remove(index);
            rightBlocks.remove(index);
            rightBlockTexts.remove(index);
        });

        fadeLeftBlock.play();
        fadeLeftText.play();
        fadeRightBlock.play();
        fadeRightText.play();
    }
}