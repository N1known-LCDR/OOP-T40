package dk.sdu.imada.oop26;

import dk.sdu.imada.oop26.Main.GameState;
import javafx.scene.control.Label;

public class GameManager {
    
    private int score = 0;
    private int lives = 3;
    private GameState state = GameState.NORMAL;

    private Label scorLabel;
    private Label livesLabel;
    private Label stateLabel;

    public GameManager(Label scorLabel, Label livesLabel, Label stateLabel){
        this.scorLabel = scorLabel;
        this.livesLabel = livesLabel;
        this.stateLabel = stateLabel;
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
        scorLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
        
        if (state == GameState.FINISHED) {
            stateLabel.setText("Game Over - press x to restart");
            stateLabel.setStyle("-fx-font-size: 16; -fx-text-fill: red; -fx-font-weight: bold;");
        } else if (state == GameState.POWER) {
            stateLabel.setText("State: Power");
            stateLabel.setStyle("-fx-font-size: 16; -fx-text-fill: orange; -fx-font-weight: bold;");
        } else if (state==GameState.IMMUNE) {
            stateLabel.setText("State: Immune");
            stateLabel.setStyle("-fx-font-size: 16; -fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            stateLabel.setText("State: Normal");
            stateLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white; -fx-font-weight: bold;");

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
