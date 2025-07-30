package tankgame.factories;

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
}
