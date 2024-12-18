package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.io.FileSystem;

import javax.sound.sampled.*;
import java.io.InputStream;

public class MusicPlayer {
    private Clip clip;

    public void play(FileSystem fileSystem, String soundPath) {
        try {
            InputStream audioSrc = fileSystem.read(soundPath);
            if (audioSrc == null) {
                System.err.println("Fichier audio non trouvé : " + soundPath);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement ou de la lecture de la musique : " + e.getMessage());
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
