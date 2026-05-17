package dk.sdu.imada.oop26;

public class AssassinBehavior implements GhostBehavior {

    // How many tiles ahead of the player the ghost will try to target
    private static final int MAX_LOOKAHEAD = 6;

    @Override
    public void move(Ghost ghost, Player player, Map map) {

        // Occasional randomness
        if (Math.random() < 0.15) {
            ghost.moveRandom();
            return;
        }
        // Get the player's current movement direction
        int dx = player.getDx();
        int dy = player.getDy();

        // If the player is standing still, just move directly towards them
        if (dx == 0 && dy == 0) {
            ghost.moveTowards(player.getRow(), player.getCol());
            return;
        }
        // Start the target at the player's current position,
        // then walk it forward along the player's movement direction
        int targetRow = player.getRow();
        int targetCol = player.getCol();

        // Advance the target up to MAX_LOOKAHEAD tiles ahead, stopping at walls
        for (int i = 1; i <= MAX_LOOKAHEAD; i++) {
            int nextRow = player.getRow() + dy * i;
            int nextCol = player.getCol() + dx * i;
            if (map.isWall(nextRow, nextCol)) break; // Can't predict through walls
            targetRow = nextRow;
            targetCol = nextCol;
        }

        // Overshoot sometimes
        if (Math.random() < 0.3) {
            int overshotRow = targetRow += dy * 2;
            int overshotCol = targetCol += dx * 2;
            if(!map.isWall(overshotRow, overshotCol)){
                targetRow = overshotRow;
                targetCol = overshotCol;
            }
        }
        ghost.moveTowards(targetRow, targetCol);
    }
}
