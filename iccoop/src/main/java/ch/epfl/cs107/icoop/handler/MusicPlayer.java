package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.io.FileSystem;

import javax.sound.sampled.*;
import java.io.InputStream;

/**
 * MusicPlayer is a utility class for playing and controlling background music in the game.
 *
 * <p>This class uses Java's `javax.sound.sampled` library to load and play audio files. It supports continuous looping
 * playback and allows stopping the currently playing track.</p>*/
public class MusicPlayer {
    /** The audio clip that is currently playing. */

    private Clip clip;
    /**
     * Plays the specified audio file in a continuous loop.
     *
     * @param fileSystem (FileSystem): The file system used to locate and read the audio file.
     * @param soundPath  (String): The path to the audio file within the file system.
     */
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
    /**
     * Stops the currently playing audio track.
     *
     * <p>If no audio is currently playing, this method does nothing.</p>
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
