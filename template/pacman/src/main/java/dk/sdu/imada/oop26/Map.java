package dk.sdu.imada.oop26;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Map {
    public final int TILE_SIZE = 40;

    public static final int WALL = 1;
    public static final int DOT = 0;
    public static final int POWER = 2;
    public static final int EMPTY = -1;
    

    // 0 = dot, 1= wall, 2 = power
    private int[][] map ={
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1}, 
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private Circle[][] dots;

    private Pane root;

    public int getTile(int row, int col){
        return map[row][col];
    }

    public void setTile(int row, int col, int value){
        map[row][col] = value;
    }

    public Map(Pane root){
        this.root = root;
        dots = new Circle[map.length][map[0].length];
        draw();
    }

    public void draw(){

        for (int row = 0; row < map.length; row++){
            for(int col = 0; col < map[row].length; col++){

                // WALLS (draw once, never stored)
                if (map[row][col] == WALL){
                    Rectangle wall = new Rectangle(
                        col * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                    );
                    wall.setFill(javafx.scene.paint.Color.BLUE);
                   root.getChildren().add(wall);
                }

                // DOTS (store them!)
                if (map[row][col] == DOT){
                    Circle dot = new Circle(
                        col * TILE_SIZE + TILE_SIZE / 2,
                        row * TILE_SIZE + TILE_SIZE / 2,
                        5
                    );
                    dot.setFill(javafx.scene.paint.Color.YELLOW);

                    dots[row][col] = dot; // store reference
                    root.getChildren().add(dot);
                }

                // POWER DOTS (also store!)
                if (map[row][col] == POWER){
                    Circle dot = new Circle(
                        col * TILE_SIZE + TILE_SIZE / 2,
                        row * TILE_SIZE + TILE_SIZE / 2,
                        10
                    );
                    dot.setFill(javafx.scene.paint.Color.ORANGE);

                    dots[row][col] = dot; // store reference
                    root.getChildren().add(dot);
                }
            }
        }
    }

    public void removeDot(int row, int col){
        if (dots[row][col] != null){
            root.getChildren().remove(dots[row][col]);
            dots[row][col] = null;
        }
    }

    public void redraw(){
        root.getChildren().clear();
        draw();
    }

    public boolean isWall(int row, int col) {

        if (row < 0 || col < 0 || row >= map.length || col >= map[0].length) {
            return true; // out of bounds = wall
        }

        return map[row][col] == WALL;
}
}
