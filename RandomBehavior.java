package dk.sdu.imada.oop26;

public class RandomBehavior implements GhostBehavior{
    @Override
    public void move(Ghost ghost, Player player, Map map){
        ghost.moveRandom();
    }
}
