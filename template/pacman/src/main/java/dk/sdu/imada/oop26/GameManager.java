package dk.sdu.imada.oop26;

import dk.sdu.imada.oop26.Main.GameState;
import javafx.scene.control.Label;

public class GameManager {
    
    private int score = 0;
    private int lives = 3;
    private GameState state = GameState.NORMAL;

    private Label uiLabel;

    public GameManager(Label uiLabel){
        this.uiLabel = uiLabel;
        updateUI();
    }

    public void addScore(int amount){
        score += amount;
        updateUI();
    }

    public void loseLife(){
        if (state == GameState.IMMUNE) return;

        lives--;

        if (lives <= 0) {
            state = GameState.FINISHED;
        } else {
            setState(GameState.IMMUNE);
            startImmuneTimer();
        }

        updateUI();
    }

    public void setState(GameState newState) {
        state = newState;
    }

    public GameState getState(){
        return state;
    }

    private void updateUI(){
        if (state == GameState.FINISHED){
            uiLabel.setText("Game Over - Score: " + score + " | Press X to restart");
        } else {
        uiLabel.setText("Score: " + score + " | Lives: " + lives + " | State: " + state);
        }
    }

    private void startImmuneTimer() {
    javafx.animation.PauseTransition pause = 
        new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));

        pause.setOnFinished(e -> {
            if (state == GameState.IMMUNE) {
                state = GameState.NORMAL;
                updateUI();
            }
        });

        pause.play();
    }

    public void startPowerMode() {
    state = GameState.POWER;
    updateUI();

    javafx.animation.PauseTransition pause = 
        new javafx.animation.PauseTransition(javafx.util.Duration.seconds(10));

        pause.setOnFinished(e -> {
            if (state == GameState.POWER) {
                state = GameState.NORMAL;
                updateUI();
            }
        });

        pause.play();
    }

}
