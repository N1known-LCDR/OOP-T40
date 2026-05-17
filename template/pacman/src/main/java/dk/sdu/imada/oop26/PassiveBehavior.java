package dk.sdu.imada.oop26;

public class PassiveBehavior implements GhostBehavior{
    @Override
    public void move(Ghost ghost, Player player, Map map){
        // Moves based on whether ghost can see the player or not
        if (hasLineOfSight(ghost, player, map)){
            ghost.moveTowards(player.getRow(), player.getCol());
        } else {
            ghost.moveRandom();
        }
    }
    // Checks if Ghost has a unobstucted, horizntal or vertical straight line view of the player
    private boolean hasLineOfSight(Ghost ghost, Player player, Map map){
        
        // Check horizontal line of sight (same row)
        if (ghost.getRow() == player.getRow()){
            int start = Math.min(ghost.getCol(), player.getCol());
            int end = Math.max(ghost.getCol(), player.getCol());

            // Scan every column between the ghost and the player
            for (int c = start; c <= end; c++){
                if (map.isWall(ghost.getRow(), c)) return false;
            }
            return true;
        }

        // Check vertical line of sight (same column)
        if (ghost.getCol() == player.getCol()){
            int start = Math.min(ghost.getRow(), player.getRow());
            int end = Math.max(ghost.getRow(), player.getRow());

            // Scan every row between the ghost and the player
            for (int r = start; r <= end; r++){
                if (map.isWall(r, ghost.getCol())) return false;
            }
            return true;
        }
        // Ghost and player are not aligned — no line of sight
        return false;
    }

}
