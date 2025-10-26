package application;

import java.util.Arrays;
import java.util.List;

class DPSolver {
    public static int solve(List<Checkpoint> checkpoints, int weightLimit) {
        int[] dp = new int[weightLimit + 1];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = 0;

        for (Checkpoint checkpoint : checkpoints) {
            int[] nextDp = new int[weightLimit + 1];
            Arrays.fill(nextDp, Integer.MIN_VALUE);
            nextDp[0] = 0;
            for (int w = 0; w <= weightLimit; w++) {
                if (dp[w] != Integer.MIN_VALUE) {
                    int leftWt = checkpoint.getLeftBlock().getWeight();
                    int leftVal = checkpoint.getLeftBlock().getValue();
                    if (w + leftWt <= weightLimit) {
                        nextDp[w + leftWt] = Math.max(nextDp[w + leftWt], dp[w] + leftVal);
                    }
                    int rightWt = checkpoint.getRightBlock().getWeight();
                    int rightVal = checkpoint.getRightBlock().getValue();
                    if (w + rightWt <= weightLimit) {
                        nextDp[w + rightWt] = Math.max(nextDp[w + rightWt], dp[w] + rightVal);
                    }
                }
            }
            dp = nextDp;
        }

        int maxValue = Integer.MIN_VALUE;
        for (int val : dp) {
            if (val > maxValue) maxValue = val;
        }
        return maxValue == Integer.MIN_VALUE ? 0 : maxValue;
    }
}