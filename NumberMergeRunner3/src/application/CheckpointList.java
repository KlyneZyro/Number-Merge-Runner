package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CheckpointList {
    private List<Checkpoint> checkpoints;
    private Random random;

    public CheckpointList() {
        this.checkpoints = new ArrayList<>();
        this.random = new Random();
    }

    public void generateCheckpointsForDifficulty(String difficulty) {
        checkpoints.clear();

        List<Checkpoint> easyPool = new ArrayList<>();
        List<Checkpoint> mediumPool = new ArrayList<>();
        List<Checkpoint> hardPool = new ArrayList<>();

        easyPool.add(new Checkpoint(new Block(3, 2), new Block(2, 1)));
        easyPool.add(new Checkpoint(new Block(4, 3), new Block(5, 2)));
        easyPool.add(new Checkpoint(new Block(5, 2), new Block(4, 3)));
        easyPool.add(new Checkpoint(new Block(2, 1), new Block(3, 2)));
        easyPool.add(new Checkpoint(new Block(6, 4), new Block(5, 3)));
        easyPool.add(new Checkpoint(new Block(3, 1), new Block(4, 2)));

        mediumPool.add(new Checkpoint(new Block(5, 3), new Block(6, 2)));
        mediumPool.add(new Checkpoint(new Block(6, 4), new Block(5, 2)));
        mediumPool.add(new Checkpoint(new Block(4, 2), new Block(3, 2)));
        mediumPool.add(new Checkpoint(new Block(7, 3), new Block(6, 4)));
        mediumPool.add(new Checkpoint(new Block(3, 1), new Block(4, 3)));
        mediumPool.add(new Checkpoint(new Block(8, 5), new Block(7, 3)));

        hardPool.add(new Checkpoint(new Block(7, 4), new Block(8, 3)));
        hardPool.add(new Checkpoint(new Block(8, 5), new Block(6, 3)));
        hardPool.add(new Checkpoint(new Block(6, 2), new Block(5, 3)));
        hardPool.add(new Checkpoint(new Block(9, 4), new Block(8, 5)));
        hardPool.add(new Checkpoint(new Block(5, 2), new Block(4, 3)));
        hardPool.add(new Checkpoint(new Block(10, 6), new Block(9, 4)));

        List<Checkpoint> selectedPool;
        int numCheckpoints;

        switch (difficulty.toLowerCase()) {
            case "easy":
                selectedPool = easyPool;
                numCheckpoints = 3;
                break;
            case "medium":
                selectedPool = mediumPool;
                numCheckpoints = 4;
                break;
            case "hard":
                selectedPool = hardPool;
                numCheckpoints = 5;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }

        Collections.shuffle(selectedPool, random);
        checkpoints.addAll(selectedPool.subList(0, numCheckpoints));
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}