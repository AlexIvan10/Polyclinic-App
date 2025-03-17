package org.example.test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {
    private static Clip clip;

    private static void playSound(String soundFilePath) {
        try {
            if (clip == null || !clip.isRunning()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playElevatorSound() {
        playSound("Sound/jazz-bossa-nova-163669.wav");
    }

    public static void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }


}
