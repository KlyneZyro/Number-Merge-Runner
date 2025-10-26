# Number-Merge-Runner
**Project Title : **
Number Merge Runner

**Description: **

  Number Merge Runner is an interactive game that was developed using Java and JavaFX. In this game, the player controls a character that progresses through a series of checkpoints along a path. At each checkpoint, the player must choose one of two paths, each offering a mathematical operation that adds a specific value to the character’s total value (e.g., +10 or +20) and contributes a specific weight (e.g., 1 or 2 units). This setup simulates the Multiple-Choice Knapsack Problem, where:
- Checkpoints act as groups of items.
- Operations at each checkpoint represent items within a group, each with an associated value (added to the total) and weight.
- The player must select exactly one operation (addition) per checkpoint.
  
**The game has two main objectives:**
1. Maximize the character’s total value by the end of the path through strategic additions.
2. Ensure the total weight accumulated stays below a predefined limit (e.g., 10 units).
  At the journey’s end, the character confronts a final boss with a specific health value (e.g., 50). To defeat the boss, the player’s total value must meet or exceed the boss’s health while keeping the total weight within the limit. The game features a graphical interface built with JavaFX, showing the character’s movement, current total value, current weight, and the choices available at each checkpoint. 

**Algorithm / Pseudocode**

  The algorithm for "Number Merge Runner: A Knapsack Adventure" is based on the Multiple-Choice Knapsack Problem, a variation of the classic 0/1 Knapsack Problem. In this game, the player moves through a series of checkpoints, where each checkpoint presents two choices (operations) that contribute a specific value to the player’s total score and a specific weight. The objective is to maximize the total value while keeping the cumulative weight under a predefined limit. To achieve this, the algorithm uses dynamic programming with a 1D array, dp, where dp[w] stores the maximum value attainable for exactly w units of weight. For each checkpoint, the algorithm iterates over all possible weights from 0 to the maximum weight limit. For each operation at a checkpoint, it calculates whether including that operation’s weight keeps the total within the limit and updates dp[w] with the maximum value possible by choosing that operation. This ensures that exactly one operation is selected per checkpoint, mirroring the game’s rules. The final answer is the highest value in the dp array that adheres to the weight constraint, representing the optimal score achievable.

**Big O Notation**

  O(n * W), where n represents the number of checkpoints and W is the maximum weight capacity. The space complexity is O(W) because the algorithm relies on a single 1D array of size W+1 to track the maximum values for each possible weight. This efficient use of time and space makes the algorithm well-suited for the game’s optimization challenge.

<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/9100741f-9dc2-47a1-a763-c1b039a4a6f8" />
