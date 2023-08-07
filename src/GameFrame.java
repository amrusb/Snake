import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame {
    private static State currentState = State.MENU;
    private static final String MENU_PANEL = "Menu";
    private static final String GAME_PANEL = "Game";
    private static CardLayout layout = new CardLayout();
    private static JPanel panel = new JPanel(layout);
    private static GamePanel game = new GamePanel();
    private static MenuPanel menu = new MenuPanel();
    static int counter = 0;

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
                game = new GamePanel();

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