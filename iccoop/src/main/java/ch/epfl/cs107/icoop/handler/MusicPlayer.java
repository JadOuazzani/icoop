package ch.epfl.cs107.icoop.handler;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MusicPlayer {
    private Clip clip;

    public void play(String filePath) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(filePath);
            if (audioSrc == null) {
                throw new FileNotFoundException("Fichier audio non trouvé : " + filePath);
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
