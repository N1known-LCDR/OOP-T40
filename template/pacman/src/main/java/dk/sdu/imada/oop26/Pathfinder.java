package dk.sdu.imada.oop26;

import java.util.*;

public class Pathfinder {

    /*
    Breadth-First Search (BFS) from (startRow, StartCol) towards(targetRow, targetCol).
    Returns first  step [row,col] ghost should take,
    or null if no path exists
     */

    public static int[] nextStep(Map map, int startRow, int startCol, int targetRow, int targetCol){
        if(startRow == targetRow && startCol == targetCol) return null;

        int rows = map.getMapHeight();
        int cols = map.getMapWidth();

        boolean[][] visited = new boolean[rows][cols];
        int [][][] parent = new int[rows][cols][2];//stores the step taken from start

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        int[][] firstStep = new int[rows][cols];
        for (int[]row : firstStep) Arrays.fill(row, -1);

        int [][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        while (!queue.isEmpty()){
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];

            for (int[] d : dirs){
                int nr = r + d[0];
                int nc = c + d[1];

                if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
                if (visited[nr][nc]) continue;
                if (map.isWall(nr, nc)) continue;

                visited[nr][nc] = true;
                parent[nr][nc] = new int[]{r, c};

                if (r == startRow && c == startCol) {
                    firstStep[nr][nc] = encode(nr, nc, cols);
                } else {
                    firstStep[nr][nc] = firstStep[r][c];
                }

                if (nr == targetRow && nc == targetCol) {
                    int code = firstStep[nr][nc];
                    return new int[]{code / cols, code % cols};
                }

                queue.add(new int[]{nr, nc});
            }
        }

        return null;
    }

    private static int encode(int row, int col, int width){
        return row * width + col;
    }
}
