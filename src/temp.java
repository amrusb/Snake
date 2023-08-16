import Utils.Sound;
import Utils.SoundTypes;

public class temp {
    public static void main(String[] args) {
        Sound sound;
        try{
            sound = new Sound();
            for (int i = 0; i < 20; i++) {
                System.out.println("Beep no: " + i);
                sound.play(SoundTypes.POINT_GAINED);
                Thread.sleep(700);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}
