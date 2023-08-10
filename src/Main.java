import java.awt.*;

public class Main {
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            var frame = new GameFrame();
            frame.setVisible(true);
        });

    }
}
