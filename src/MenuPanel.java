import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    MenuPanel(){
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0xC2FCE1));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        ImageIcon logo = new ImageIcon("img/snake-logo.png");
        var logoLabel = new JLabel(logo);
        this.add(logoLabel, BorderLayout.NORTH);

        var buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xC2FCE1));
        var layout = new GridLayout(3,1);
        layout.setHgap(100);
        layout.setVgap(30);
        buttonPanel.setLayout(layout);
        var newGame = new JButton("New game");
        var leaderboard = new JButton("Leaderboard");
        var options = new JButton("Options");

        buttonPanel.add(newGame);
        buttonPanel.add(leaderboard);
        buttonPanel.add(options);
        this.add(buttonPanel, BorderLayout.SOUTH);

    }
}
