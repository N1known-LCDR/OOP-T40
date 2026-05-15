package dk.sdu.imada.oop26;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    public static MediaPlayer mainMusic;
    public static MediaPlayer powerUpMusic;
    public static MediaPlayer powerUpSound;
    public static MediaPlayer collect;
    public static MediaPlayer playerDeath;
    public static MediaPlayer ghostDeath;
    public static MediaPlayer levelComplete;
    public static MediaPlayer gameLose;

    private static Timer timer = new Timer();
    private static TimerTask currentTask;

    //Init function to create all the Mediaplayers
    public static void init() {

        mainMusic = createPlayer("/sounds/mainmusic.mp3");
        mainMusic.setVolume(0.3);
        mainMusic.setCycleCount(MediaPlayer.INDEFINITE);

        powerUpMusic = createPlayer("/sounds/powerupmusic.mp3");

        powerUpSound = createPlayer("/sounds/powerupsound.mp3");
        collect = createPlayer("/sounds/collect.mp3");
        playerDeath = createPlayer("/sounds/playerdeath.mp3");
        ghostDeath = createPlayer("/sounds/ghostdeath.mp3");
        levelComplete = createPlayer("/sounds/levelcomplete.mp3");
        gameLose = createPlayer("/sounds/gamelose.mp3");

    }

    //helper function to create MediaPlayer objects
    public static MediaPlayer createPlayer(String path) {
        String uri = SoundManager.class.getResource(path).toExternalForm();
        return new MediaPlayer(new Media(uri));
    }    

    //helper function to stop sounds before playing new ones
    public static void play(MediaPlayer player) {
        player.stop();
        player.play();
    }

    //methods to play the sounds/music
    public static void playMainMusic() {
        play(mainMusic);
    }

    public static void playPowerUpMusic() {
        mainMusic.setVolume(0.0);
        play(powerUpMusic);

        //cancel to stop duplicate sounds
        if (currentTask != null) {
        currentTask.cancel();
        }


        //after 10 seconds setVolume to 1.0
        currentTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    mainMusic.setVolume(1.0);
                });
            }
        };
        timer.schedule(currentTask, 10000);
    }

    public static void playPowerUpSound() {
        play(powerUpSound);
    }

    public static void playCollectSound() {
        play(collect);
    }

    public static void playPlayerDeathSound() {
        play(playerDeath);
    }

    public static void playGhostDeathSound() {
        play(ghostDeath);
    }

    public static void playLevelCompleteSound() {
        play(levelComplete);
    }

    public static void playGameLoseSound() {
        play(gameLose);
    }
}
