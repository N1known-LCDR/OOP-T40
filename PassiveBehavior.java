package dk.sdu.imada.oop26;

public class PassiveBehavior implements GhostBehavior{
    @Override
    public void move(Ghost ghost, Player player, Map map){

        if (hasLineOfSight(ghost, player, map)){
            ghost.moveTowards(player.getRow(), player.getCol());
        } else {
            ghost.moveRandom();
        }
    }

    private boolean hasLineOfSight(Ghost ghost, Player player, Map map){

        if (ghost.getRow() == player.getRow()){
            int start = Math.min(ghost.getCol(), player.getCol());
            int end = Math.max(ghost.getCol(), player.getCol());

            for (int c = start; c <= end; c++){
                if (map.isWall(ghost.getRow(), c)) return false;
            }
            return true;
        }

        if (ghost.getCol() == player.getCol()){
            int start = Math.min(ghost.getRow(), player.getRow());
            int end = Math.max(ghost.getRow(), player.getRow());

            for (int r = start; r <= end; r++){
                if (map.isWall(r, ghost.getCol())) return false;
            }
            return true;
        }
        return false;
    }

}
