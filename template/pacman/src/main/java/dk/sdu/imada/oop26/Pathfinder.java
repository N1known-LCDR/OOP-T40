package dk.sdu.imada.oop26;

import java.util.*;

public class Pathfinder {

    /*
    Breadth-First Search (BFS) from (startRow, StartCol) towards(targetRow, targetCol).
    Returns first  step [row,col] ghost should take,
    or null if no path exists
     */

    public static int[] nextStep(Map map, int startRow, int startCol, int targetRow, int targetCol){
        // Already at the target — no movement needed
        if(startRow == targetRow && startCol == targetCol) return null;

        int rows = map.getMapHeight();
        int cols = map.getMapWidth();

        // Which cells have been added to queue 
        boolean[][] visited = new boolean[rows][cols];
        // Stores parent cell for each visited cell
        int [][][] parent = new int[rows][cols][2];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        // Store the encoded position of the first step taken from start.
        // -1 means the cell hasn't been reached yet.
        int[][] firstStep = new int[rows][cols];
        for (int[]row : firstStep) Arrays.fill(row, -1);

        // The four directions
        int [][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        while (!queue.isEmpty()){
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];

            // Explore all neighboring cells
            for (int[] d : dirs){
                int nr = r + d[0];
                int nc = c + d[1];

                // Skip out-of-bounds cells
                if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
                // Skip already-visited cells
                if (visited[nr][nc]) continue;
                // Skip walls
                if (map.isWall(nr, nc)) continue;

                visited[nr][nc] = true;
                parent[nr][nc] = new int[]{r, c};

                // If we're stepping directly from start, this neighbor IS the first step
                if (r == startRow && c == startCol) {
                    firstStep[nr][nc] = encode(nr, nc, cols);
                } else {
                    // Otherwise, inherit the first step from the current cell
                    firstStep[nr][nc] = firstStep[r][c];
                }

                // If we've reached the target, decode and return the first step
                if (nr == targetRow && nc == targetCol) {
                    int code = firstStep[nr][nc];
                    return new int[]{code / cols, code % cols};
                }

                queue.add(new int[]{nr, nc});
            }
        }
        // No path found
        return null;
    }

    private static int encode(int row, int col, int width){
        return row * width + col;
    }
}
