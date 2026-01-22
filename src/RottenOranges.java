import java.util.LinkedList;
import java.util.Queue;

/*
LeetCode 994. Rotting Oranges
*/
public class RottenOranges {

    /*
     APPROACH 1: BFS (Multi-source BFS)
     ---------------------------------
     Idea:
     - Put all initially rotten oranges into a queue (multi-source).
     - Count how many fresh oranges exist.
     - Process the queue level by level (each level = 1 minute).
     - Each minute, rot all adjacent fresh oranges and push them into the queue.
     - Stop when fresh becomes 0.

     Time Complexity: O(m * n)
       - each cell is processed at most once

     Space Complexity: O(m * n)
       - queue can hold up to all cells in worst case
     */
    public int orangesRotting(int[][] grid) {
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}}; // 4 directions

        int m = grid.length;
        int n = grid[0].length;

        int fresh = 0;
        Queue<int[]> q = new LinkedList<>(); // each entry = {row, col}

        // 1) Initialize queue with all rotten oranges, count fresh oranges
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) q.add(new int[]{i, j});
                else if (grid[i][j] == 1) fresh++;
            }
        }

        // If no fresh oranges, time is 0
        if (fresh == 0) return 0;

        int minutes = 0;

        while (!q.isEmpty()) {
            int size = q.size();
            boolean rottedThisMinute = false;

            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                int r = cur[0], c = cur[1];

                for (int[] d : dirs) {
                    int nr = r + d[0];
                    int nc = c + d[1];

                    // boundary check
                    if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2; // rot it
                        q.add(new int[]{nr, nc});
                        fresh--;
                        rottedThisMinute = true;
                    }
                }
            }

            // Only increment minutes if we actually rotted something in this layer
            if (rottedThisMinute) minutes++;

            if (fresh == 0) return minutes;
        }

        return -1; // still fresh oranges left but no more rotting possible
    }

    // Approach 2: DFS
    /*
     APPROACH 2: DFS time marking
     ---------------------------
     Idea:
     - Start DFS from each rotten orange and "spread" a time label.
       Example: original rotten = 2, next minute cells become 3, next 4, etc.
     - If we reach a cell later than an already recorded earlier time, stop.
     - At the end:
       - if any cell is still 1, impossible -> -1
       - otherwise answer is (maxTime - 2)

     Time Complexity: O(m * n)
     Space Complexity: O(m * n)
     */
    private int[][] dirs;
    private int m;
    private int n;

    public int orangesRottingUsingDFS(int[][] grid) {
        this.dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        this.m = grid.length;
        this.n = grid[0].length;

        // Start DFS from every rotten orange, time starts at 2
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    dfs(grid, i, j, 2);
                }
            }
        }

        int maxTime = 2;

        // Check for remaining fresh oranges and compute max time label
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) return -1;          // still fresh -> impossible
                maxTime = Math.max(maxTime, grid[i][j]); // track max time label
            }
        }

        return maxTime - 2;
    }

    private void dfs(int[][] grid, int row, int col, int time) {
        // boundary check
        if (row < 0 || col < 0 || row >= m || col >= n) return;

        // 0 is empty -> cannot rot through it
        if (grid[row][col] == 0) return;

        /*
          If cell already has a smaller time label (rotted earlier), do not update.
          Also: a fresh orange is 1, which should always be updated when reached.
        */
        if (grid[row][col] != 1 && grid[row][col] < time) return;

        // mark rotting time
        grid[row][col] = time;

        // spread to neighbors at time+1
        for (int[] d : dirs) {
            dfs(grid, row + d[0], col + d[1], time + 1);
        }
    }

    //  Simple main method to test
    public static void main(String[] args) {
        RottenOranges solver = new RottenOranges();

        int[][] grid1 = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };

        int[][] grid2 = {
                {2, 1, 1},
                {0, 1, 1},
                {1, 0, 1}
        };

        // Helper to deep copy because methods mutate the grid
        int[][] copy1ForBFS = copyGrid(grid1);
        int[][] copy1ForDFS = copyGrid(grid1);

        System.out.println("Grid1 BFS minutes: " + solver.orangesRotting(copy1ForBFS));
        System.out.println("Grid1 DFS minutes: " + solver.orangesRottingUsingDFS(copy1ForDFS));

        int[][] copy2ForBFS = copyGrid(grid2);
        int[][] copy2ForDFS = copyGrid(grid2);

        System.out.println("Grid2 BFS minutes: " + solver.orangesRotting(copy2ForBFS));
        System.out.println("Grid2 DFS minutes: " + solver.orangesRottingUsingDFS(copy2ForDFS));
    }

    private static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, grid[0].length);
        }
        return copy;
    }
}
