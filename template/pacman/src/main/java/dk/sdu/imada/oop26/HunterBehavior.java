package dk.sdu.imada.oop26;

public class HunterBehavior implements GhostBehavior{
    @Override
    public void move(Ghost ghost, Player player, Map map){
        ghost.moveTowards(player.getRow(), player.getCol());
    }
}
