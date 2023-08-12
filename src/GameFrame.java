import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private static State currentState = State.MENU;
    private static CardLayout layout = new CardLayout();
    private static JPanel panel = new JPanel(layout);
    private static GamePanel game = new GamePanel("temp", 0);
    private static MenuPanel menu = new MenuPanel();

    GameFrame(){
        panel.add(game, "game");
        panel.add(menu, "menu");
        layout.next(panel);
        add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
    public static void changePanel(){
        switch(currentState){
            case GAME -> {
                panel.remove(menu);
                panel.repaint();
                panel.revalidate();
                panel.remove(game);
                String nickname = menu.getNickname();
                var temp = menu.readLeaderboard().entrySet();
                int current_high_score = 0;
                if(!temp.isEmpty()) current_high_score = temp.iterator().next().getValue();
                game = new GamePanel(nickname, current_high_score);

                panel.add(game);
                panel.repaint();
                panel.revalidate();
                layout.previous(panel);
            }
            case MENU -> {
                panel.remove(game);
                panel.repaint();
                panel.revalidate();

                panel.remove(menu);
                panel.add(menu);
                panel.repaint();
                panel.revalidate();
                layout.next(panel);
            }
        }
    }

    public static State getCurrentState() {
        return currentState;
    }

    public static void setState(State s){
        currentState = s;
    }

}
enum State {GAME, IN_GAME, GAME_OVER, MENU}