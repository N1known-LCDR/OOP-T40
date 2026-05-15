package dk.sdu.imada.oop26;

import dk.sdu.imada.oop26.Main.GameState;
import javafx.scene.control.Label;

public class GameManager {
    
    private int score = 0;
    private int lives = 3;
    private int currentLevel = 0;

    private GameState state = GameState.NORMAL;

    private Label uiLabel;

    private javafx.animation.PauseTransition powerTimer;

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
            SoundManager.playGameLoseSound();
            state = GameState.FINISHED;
        } else {
            SoundManager.playPlayerDeathSound();
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

    public int getLevel() {
        return currentLevel;
    }

    public void nextLevel() {
        currentLevel++;
        state = GameState.NORMAL;
        updateUI();
    }

    public void levelComplete() {
        SoundManager.playLevelCompleteSound();
        state = GameState.LEVEL_COMPLETE;
        updateUI();
    }

    private void updateUI(){
        if (state == GameState.FINISHED){
            uiLabel.setText("Game Over - Score: " + score + " | Press X to restart");
        } else if (state == GameState.LEVEL_COMPLETE){
            uiLabel.setText("Level Complete! Press N for next level");
        } else {
            uiLabel.setText("Score: " + score + " | Lives: " + lives + " | Level: " + (currentLevel + 1) + " | State: " + state);
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

        if (powerTimer != null) powerTimer.stop();
        powerTimer = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(10));
        powerTimer.setOnFinished(e ->{
            if (state == GameState.POWER){
                state = GameState.NORMAL;
                updateUI();
            }
        });

        powerTimer.play();
    }
}