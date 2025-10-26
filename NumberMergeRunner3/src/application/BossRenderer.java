package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;

public class BossRenderer {
    private double width, height;
    private Game game;
    private ImageView boss;
    private Text bossText;

    public BossRenderer(double width, double height, Game game) {
        this.width = width;
        this.height = height;
        this.game = game;
        initializeBoss();
    }

    private void initializeBoss() {
        // Load the boss image from the specified path
        Image originalImage;
        try {
            originalImage = new Image(getClass().getResourceAsStream("/application/resources/skeletronboss.png"));
        } catch (Exception e) {
            System.err.println("Failed to load boss image: " + e.getMessage());
            // Fallback to a default image or behavior if the image fails to load
            originalImage = new Image(getClass().getResourceAsStream("/application/resources/fallback.png"));
        }

        // Create a new WritableImage to modify the original image
        WritableImage writableImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        // Remove near-white and black border pixels by setting them to transparent
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                // Remove near-white pixels (threshold 0.85) and black pixels (threshold 0.1)
                if ((color.getRed() > 0.85 && color.getGreen() > 0.85 && color.getBlue() > 0.85) || 
                    (color.getRed() < 0.1 && color.getGreen() < 0.1 && color.getBlue() < 0.1)) {
                    pixelWriter.setColor(x, y, Color.TRANSPARENT);
                } else {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }

        // Create ImageView with the modified image
        boss = new ImageView(writableImage);
        
        // Set the size and position
        boss.setFitWidth(200);
        boss.setFitHeight(200);
        boss.setX(width / 2 - 100); 
        boss.setY(100);
        
        // Preserve the aspect ratio and ensure smooth scaling
        boss.setPreserveRatio(true);
        boss.setSmooth(true); // Enable smooth scaling to reduce artifacts
        
        // Apply effects
        boss.setEffect(new DropShadow(20, 5, 5, Color.BLACK));
        boss.setEffect(new Glow(0.6));
        boss.setVisible(false);

        // Initialize boss health text
        bossText = new Text("Health: " + game.getBossHealth());
        bossText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        bossText.setFill(Color.web("#D4D4AA"));
        // Position the text below the boss image
        bossText.setX(width / 2 - bossText.getLayoutBounds().getWidth() / 2);
        bossText.setY(100 + 200 + 40);
        bossText.setVisible(false);
    }

    public ImageView getBoss() {
        return boss;
    }

    public Text getBossText() {
        return bossText;
    }

    public void show() {
        boss.setVisible(true);
        bossText.setVisible(true);
    }

    public void updateHealth(int health) {
        bossText.setText("Health: " + health);
        bossText.setX(width / 2 - bossText.getLayoutBounds().getWidth() / 2);
    }
}