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

    // Increases the score
    public void addScore(int amount){
        score += amount;
        updateUI();
    }

    // Checks if gamestate is immune and if not then damages player. If player is at 0 or below hp after getting damaged, 
    // then the gamestate changes to finished and the game is over. Also calls methods for sounds for losing and dying.
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

    // Increases level counter and runs the gameWon method if all levels have been completed.
    public void nextLevel() {
        currentLevel++;
        if (currentLevel >= 10){
            gameWon();
        } else {
            state = GameState.NORMAL;
            updateUI();
        }
    }

    // Plays level complete sound and sets the gamestate to level complete.
    public void levelComplete() {
        SoundManager.playLevelCompleteSound();
        state = GameState.LEVEL_COMPLETE;
        updateUI();
    }

    public void gameWon(){
        state = GameState.GAME_WON;
        updateUI();
    }

    // Updates the ui, according to the gamestate, after winning/losing with text and score
    private void updateUI(){
        if (state == GameState.FINISHED){
            uiLabel.setText("Game Over - Score: " + score + " | Press X to restart");
        } else if (state == GameState.LEVEL_COMPLETE){
            uiLabel.setText("Level Complete! Press N for next level");
        } else if (state == GameState.GAME_WON){
            uiLabel.setText("YOU WIN!! Final Score: " + score + " | Press x to restart");
        } else {
            uiLabel.setText("Score: " + score + " | Lives: " + lives + " | Level: " + (currentLevel + 1) + " | State: " + state);
        }
    }

    // Starts a 2 seconds timer and sets the gamestate to normal, if its immune afterwards
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

    // Changes gamestate to power and starts a 10 second timer that will return it back to normal afterwards
    public void startPowerMode() {
        state = GameState.POWER;
        updateUI();

        // Checks if the timer is already running, and if it is, stops it
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
