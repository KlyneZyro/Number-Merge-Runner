package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Game {
    private List<Checkpoint> checkpoints;
    private Player player;
    private int weightLimit;
    private int bossHealth;

    public Game(List<Checkpoint> checkpoints, String difficulty) {
        this.checkpoints = checkpoints;
        this.player = new Player();
        setWeightLimit(difficulty);
        setBossHealth();
    }

    private void setWeightLimit(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                weightLimit = 10;
                break;
            case "medium":
                weightLimit = 15;
                break;
            case "hard":
                weightLimit = 20;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }

    private void setBossHealth() {
        List<String> optimalChoices = computeOptimalKnapsack();
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < optimalChoices.size(); i++) {
            String choice = optimalChoices.get(i);
            Checkpoint cp = checkpoints.get(i);
            Block chosenBlock = choice.equals("left") ? cp.getLeftBlock() : cp.getRightBlock();
            totalValue += chosenBlock.getValue();
            totalWeight += chosenBlock.getWeight();
        }
        bossHealth = totalValue;
        System.out.println("Difficulty: " + getDifficulty() + ", Optimal Path Weight: " + totalWeight + ", Boss Health: " + bossHealth);
    }

    public boolean play(List<String> choices) {
        int playerWeight = player.getTotalWeight();
        int playerValue = player.getTotalValue();
        boolean win = playerWeight <= weightLimit && playerValue >= bossHealth;
        System.out.println("Player Stats - Weight: " + playerWeight + ", Value: " + playerValue + ", Weight Limit: " + weightLimit + ", Boss Health: " + bossHealth + ", Win: " + win);
        return win;
    }

    public List<String> computeOptimalKnapsack() {
        int n = checkpoints.size();
        int W = weightLimit;
        int[][] dp = new int[n + 1][W + 1];
        List<List<String>> choices = new ArrayList<>();

        for (int i = 0; i <= n; i++) {
            choices.add(new ArrayList<>());
            for (int j = 0; j <= W; j++) {
                choices.get(i).add("");
            }
        }

        for (int i = 1; i <= n; i++) {
            Checkpoint cp = checkpoints.get(i - 1);
            Block left = cp.getLeftBlock();
            Block right = cp.getRightBlock();

            for (int w = 0; w <= W; w++) {
                int valueWithout = dp[i - 1][w];
                int valueLeft = (left.getWeight() <= w) ? dp[i - 1][w - left.getWeight()] + left.getValue() : 0;
                int valueRight = (right.getWeight() <= w) ? dp[i - 1][w - right.getWeight()] + right.getValue() : 0;

                if (valueLeft >= valueRight && valueLeft >= valueWithout) {
                    dp[i][w] = valueLeft;
                    choices.get(i).set(w, "left");
                } else if (valueRight >= valueLeft && valueRight >= valueWithout) {
                    dp[i][w] = valueRight;
                    choices.get(i).set(w, "right");
                } else {
                    dp[i][w] = valueWithout;
                    choices.get(i).set(w, choices.get(i - 1).get(w));
                }
            }
        }

        List<String> optimalChoices = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0; i--) {
            String choice = choices.get(i).get(w);
            optimalChoices.add(choice);
            Checkpoint cp = checkpoints.get(i - 1);
            Block chosenBlock = choice.equals("left") ? cp.getLeftBlock() : cp.getRightBlock();
            w -= chosenBlock.getWeight();
        }
        Collections.reverse(optimalChoices);
        return optimalChoices;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public int getBossHealth() {
        return bossHealth;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public Player getPlayer() {
        return player;
    }

    private String getDifficulty() {
        if (weightLimit == 10) return "Easy";
        if (weightLimit == 15) return "Medium";
        return "Hard";
    }
}