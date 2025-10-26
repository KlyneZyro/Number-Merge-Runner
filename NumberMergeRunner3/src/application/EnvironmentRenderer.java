package application;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnvironmentRenderer {
    private double width, height;
    private List<Node> backgroundElements;
    private List<Rectangle> laneMarkings;
    private List<Rectangle> roadCracks;
    private double markingOffset;
    private double bgOffset;
    private long lastUpdateTime = System.nanoTime();
    private static final double TREE_SPEED = 120.0; // Pixels per second
    private static final double MARKING_SPEED = 240.0; // Pixels per second
    private static final double MAX_DELTA_TIME = 0.1; // Cap deltaTime to prevent large jumps
    private Random random = new Random();
    private int numTrees = 12; // 6 per side

    public EnvironmentRenderer(double width, double height) {
        this.width = width;
        this.height = height;
        this.backgroundElements = new ArrayList<>();
        this.laneMarkings = new ArrayList<>();
        this.roadCracks = new ArrayList<>();
        this.markingOffset = 0;
        this.bgOffset = 0;
        initializeEnvironment();
    }

    private void initializeEnvironment() {
       
        Image treeImage;
        try {
            treeImage = new Image("file:C:/Users/USER/Pictures/Screenshots/treesprite.png");
            if (treeImage.isError()) {
                throw new Exception("Tree image failed to load.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load tree image: " + e.getMessage());
            
            treeImage = null;
        }

        // Trees evenly spread near the road
        double laneLeftEdge = width / 2 - 200;
        double laneRightEdge = width / 2 + 200;
        double ySpacing = height / (numTrees / 2.0); // Even spacing for 6 trees per side
        for (int i = 0; i < numTrees; i++) {
            // Create an ImageView for the tree
            ImageView treeView;
            if (treeImage != null) {
                treeView = new ImageView(treeImage);
               
                treeView.setFitWidth(300); 
                treeView.setFitHeight(375); 
                treeView.setPreserveRatio(true);
                treeView.setSmooth(true); // Enable smooth scaling to reduce artifacts
            } else {
                // Fallback
                Rectangle placeholder = new Rectangle(30, 100, Color.GREEN);
                treeView = new ImageView();
                treeView.setUserData(placeholder);
                backgroundElements.add(placeholder); 
                continue;
            }

            // Positioning near the road
            boolean isLeftSide = i % 2 == 0;
            double baseX, baseY;
            if (isLeftSide) {
                baseX = random.nextDouble() * (laneLeftEdge - 300); 
            } else {
                baseX = laneRightEdge + 10 + random.nextDouble() * (width - laneRightEdge - 310);
            }
            // 
            baseY = (i / 2) * ySpacing;

            treeView.setTranslateX(baseX);
            treeView.setTranslateY(baseY);
            treeView.setRotate(random.nextDouble() * 10 - 5); 
            treeView.setUserData(baseY); 
            backgroundElements.add(treeView);
        }

        // Left lane
        Rectangle leftLane = new Rectangle(width / 2 - 200, 0, 200, height);
        leftLane.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#333333")),
                new Stop(0.3, Color.web("#4A4A4A")),
                new Stop(0.7, Color.web("#3A3A3A")),
                new Stop(1, Color.web("#555555"))));
        leftLane.setStroke(Color.WHITE);
        leftLane.setStrokeWidth(4);
        backgroundElements.add(leftLane);

        // Right lane
        Rectangle rightLane = new Rectangle(width / 2, 0, 200, height);
        rightLane.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#333333")),
                new Stop(0.3, Color.web("#4A4A4A")),
                new Stop(0.7, Color.web("#3A3A3A")),
                new Stop(1, Color.web("#555555"))));
        rightLane.setStroke(Color.WHITE);
        rightLane.setStrokeWidth(4);
        backgroundElements.add(rightLane);

        // Lane markings with reduced spacing
        for (int i = 0;  i < 40; i++) {
            Rectangle marking = new Rectangle(width / 2 - 5, i * 50, 10, 50);
            marking.setFill(Color.web("#FFFFFF"));
            marking.setOpacity(0.9);
            laneMarkings.add(marking);
        }

        // Road cracks with reduced spacing
        for (int i = 0; i < 20; i++) {
            double crackWidth = 160 + (i % 3 - 1) * 40;
            Rectangle crack = new Rectangle(width / 2 - 180 + (i % 2 == 0 ? 0 : 200), i * 75, crackWidth, 2);
            crack.setFill(Color.web("#2A2A1A", 0.5 + (i % 4) * 0.1));
            crack.setRotate(i % 2 == 0 ? 45 + (i % 3) * 10 : -45 - (i % 3) * 10);
            roadCracks.add(crack);
        }
    }

    public List<Node> getBackgroundElements() {
        return backgroundElements;
    }

    public List<Rectangle> getLaneMarkings() {
        return laneMarkings;
    }

    public List<Rectangle> getRoadCracks() {
        return roadCracks;
    }

    public void resetOffsets() {
        bgOffset = 0;
        markingOffset = 0;

        int numTreesPerSide = numTrees / 2;
        double ySpacing = height / (numTreesPerSide / 2.0);

        for (int i = 0; i < backgroundElements.size(); i++) {
            Node element = backgroundElements.get(i);
            if (element instanceof ImageView) {
                int index = i;
                double baseY = (index / 2) * ySpacing;
                element.setTranslateY(baseY);
                element.setUserData(baseY);
            }
        }

        for (int i = 0; i < laneMarkings.size(); i++) {
            laneMarkings.get(i).setY(i * 50);
        }

        for (int i = 0; i < roadCracks.size(); i++) {
            roadCracks.get(i).setY(i * 75);
        }
    }

    public void update() {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
        if (deltaTime > MAX_DELTA_TIME) {
            deltaTime = MAX_DELTA_TIME;
        }
        lastUpdateTime = currentTime;

        double treeDelta = TREE_SPEED * deltaTime;
        bgOffset += treeDelta;
        if (bgOffset >= 150) bgOffset -= 150;

        for (Node element : backgroundElements) {
            if (element instanceof ImageView) {
                double baseY = (double) element.getUserData();
                double newY = element.getTranslateY() + treeDelta;
                if (newY > height) newY -= height + 50;
                element.setTranslateY(newY);
            }
        }

        double markingDelta = MARKING_SPEED * deltaTime;
        markingOffset += markingDelta;
        if (markingOffset >= 50) markingOffset -= 50;
        for (Rectangle marking : laneMarkings) {
            double newY = marking.getY() + markingDelta;
            if (newY > height) newY -= height + 50;
            marking.setY(newY);
        }

        for (Rectangle crack : roadCracks) {
            double newY = crack.getY() + markingDelta;
            if (newY > height) newY -= height + 75;
            crack.setY(newY);
        }
    }
}