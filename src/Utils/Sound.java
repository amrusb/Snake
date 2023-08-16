package Utils;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    private Clip clip;
    private final String GAME_OVER_PATH = "sounds/computer-fail-sound-effect.wav";
    private final String POINT_GAINED_PATH = "sounds/Pop-sound-effect.wav";
    private final String NEW_HIGH_SCORE_PATH = "sounds/397355__plasterbrain__tada-fanfare-a.wav";
    private final String BUTTON_CLICK_PATH = "sounds/machine-button-being-pressed-sound-effect.wav";
    public void play(SoundTypes type){
        var soundThread = new Thread(() ->{
            switch(type){
                case GAME_OVER -> {
                    File f = new File(GAME_OVER_PATH);
                    try{
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clip.start();
                }
                case POINT_GAINED ->{
                    File f = new File(POINT_GAINED_PATH);
                    try{
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clip.start();
                }
                case NEW_HIGH_SCORE ->{
                    File f = new File(NEW_HIGH_SCORE_PATH);
                    try{
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clip.start();
                }
                case BUTTON_CLICK ->{
                    File f = new File(BUTTON_CLICK_PATH);
                    try{
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clip.start();
                }
            }
        });
       soundThread.start();
    }

}
