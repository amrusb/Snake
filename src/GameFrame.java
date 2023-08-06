import javax.swing.*;

public class GameFrame extends JFrame {
    private static State currentState = State.MENU;
    GamePanel game = new GamePanel();
    GameFrame(){

        this.add(game);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    public static State getCurrentState() {
        return currentState;
    }

    public static void setState(State s){
        currentState = s;
    }

}
enum State {IN_GAME, GAME_OVER, MENU}