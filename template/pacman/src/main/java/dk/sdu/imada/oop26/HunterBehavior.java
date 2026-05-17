package dk.sdu.imada.oop26;

// Behaviour to hunt down the player
public class HunterBehavior implements GhostBehavior{
    @Override
    public void move(Ghost ghost, Player player, Map map){
        // Moves towards the player using Breadth-first search
        ghost.moveTowards(player.getRow(), player.getCol());
    }
}
