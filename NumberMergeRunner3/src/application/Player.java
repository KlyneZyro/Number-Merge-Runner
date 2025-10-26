package application;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Block> blocks;
    private int totalValue;
    private int totalWeight;

    public Player() {
        this.blocks = new ArrayList<>();
        this.totalValue = 0;
        this.totalWeight = 0;
    }

    public void addBlock(Block block) {
        blocks.add(block);
        totalValue += block.getValue();
        totalWeight += block.getWeight();
        System.out.println("Player stats updated - Total Value: " + totalValue + ", Total Weight: " + totalWeight);
    }

    public int getTotalValue() {
        return totalValue;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void reset() {
        blocks.clear();
        totalValue = 0;
        totalWeight = 0;
    }
}