package dk.sdu.imada.oop26;

public class AssassinBehavior implements GhostBehavior {

    private static final int MAX_LOOKAHEAD = 6;

    @Override
    public void move(Ghost ghost, Player player, Map map) {

        // occasional randomness
        if (Math.random() < 0.15) {
            ghost.moveRandom();
            return;
        }

        int dx = player.getDx();
        int dy = player.getDy();

        if (dx == 0 && dy == 0) {
            ghost.moveTowards(player.getRow(), player.getCol());
            return;
        }

        int targetRow = player.getRow();
        int targetCol = player.getCol();

        for (int i = 1; i <= MAX_LOOKAHEAD; i++) {

            int nextRow = player.getRow() + dy * i;
            int nextCol = player.getCol() + dx * i;

            if (map.isWall(nextRow, nextCol)) break;

            targetRow = nextRow;
            targetCol = nextCol;
        }

        // overshoot sometimes
        if (Math.random() < 0.3) {
            targetRow += dy * 2;
            targetCol += dx * 2;
        }

        ghost.moveTowards(targetRow, targetCol);
    }
}
