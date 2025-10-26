package application;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;

public class PlayerController {
    private double width, height;
    private Circle player;
    private boolean dragging;
    private double dragStartX;
    private Player gamePlayer;

    public PlayerController(double width, double height, Player gamePlayer) {
        this.width = width;
        this.height = height;
        this.gamePlayer = gamePlayer;
        this.dragging = false;
        this.dragStartX = 0;
        initializePlayer();
    }

    private void initializePlayer() {
        // Create Circle
        player = new Circle(width / 2 - 100, height - 200, 30);

        // Load sprite and remove white background
        Image raw = new Image(getClass().getResourceAsStream("/application/resources/playersprite.png"));
        WritableImage sprite = removeWhiteBackground(raw);

        // Fill circle with sprite pattern
        player.setFill(new ImagePattern(sprite));

        // Apply effects
        DropShadow ds = new DropShadow(15, 5, 5, Color.BLACK);
        Glow glow = new Glow(0.5);
        player.setEffect(ds);
        player.setEffect(glow);
    }

    private WritableImage removeWhiteBackground(Image image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        WritableImage wi = new WritableImage(w, h);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = wi.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = reader.getColor(x, y);
                if (c.getRed() == 1.0 && c.getGreen() == 1.0 && c.getBlue() == 1.0) {
                    writer.setColor(x, y, Color.TRANSPARENT);
                } else {
                    writer.setColor(x, y, c);
                }
            }
        }
        return wi;
    }

    public Circle getPlayer() {
        return player;
    }

    public void setupEventHandlers(Pane pane, Runnable onMoveLeft, Runnable onMoveRight) {
        player.setOnMousePressed(e -> {
            dragging = true;
            dragStartX = e.getSceneX();
            pane.requestFocus();
        });

        pane.setOnMouseDragged(e -> {
            if (dragging) {
                double currentX = e.getSceneX();
                if (currentX > dragStartX) {
                    player.setCenterX(width / 2 + 100);
                    onMoveRight.run();
                } else if (currentX < dragStartX) {
                    player.setCenterX(width / 2 - 100);
                    onMoveLeft.run();
                }
                dragStartX = currentX;
            }
        });

        pane.setOnMouseReleased(e -> {
            dragging = false;
            pane.requestFocus();
        });
    }
}
