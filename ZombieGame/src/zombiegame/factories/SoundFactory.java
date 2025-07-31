package zombiegame.factories;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;

public class SoundFactory {
    private static final HashMap<String, Clip> soundCache = new HashMap<>();

    public static void playSound(String path) {
        try {
            if (!soundCache.containsKey(path)) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                        SoundFactory.class.getClassLoader().getResourceAsStream(path));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundCache.put(path, clip);
            }

            Clip clip = soundCache.get(path);
            if (clip != null) {
                clip.setFramePosition(0); // rewind
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + path);
        }
    }

    public static void playLoopedSound(String path) {
        try {
            if (!soundCache.containsKey(path)) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                        SoundFactory.class.getClassLoader().getResourceAsStream(path));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // loop music
                soundCache.put(path, clip);
            }

            Clip clip = soundCache.get(path);
            if (clip != null && !clip.isRunning()) {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Failed to play looped sound: " + path + e.getMessage());
        }
    }

    public static void stopAllSounds() {
        for (Clip clip : soundCache.values()) {
            if (clip != null) {
                clip.stop();
                clip.close(); // release system resources
            }
        }
        soundCache.clear();
    }
}
