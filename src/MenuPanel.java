import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MenuPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    MenuPanel(){
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });
        setLayout(new GridBagLayout());
        var c = new GridBagConstraints();

        c.weightx = 100;
        c.weighty = 100;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(40, 0,30, 0);

        setBackground(new Color(0xC2FCE1));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        ImageIcon logo = new ImageIcon("img/snake-logo.png");
        var logoLabel = new JLabel(logo);
        add(logoLabel, c);

        var buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xC2FCE1));
        var layout = new GridLayout(4,1);
        layout.setVgap(15);
        buttonPanel.setLayout(layout);
        var font = new Font("Rubik", Font.PLAIN, 20);
        var newGame = new JButton("New game".toUpperCase());
        newGame.setFont(font);
        var leaderboard = new JButton("Leaderboard".toUpperCase());
        leaderboard.setFont(font);
        var options = new JButton("Options".toUpperCase());
        options.setFont(font);
        var exit = new JButton("Exit".toUpperCase());
        exit.setFont(font);
        buttonPanel.add(newGame);
        newGame.addActionListener(e->{
            GameFrame.setState(State.GAME);
            GameFrame.changePanel();
        });
        buttonPanel.add(leaderboard);
        buttonPanel.add(options);
        buttonPanel.add(exit);
        exit.addActionListener(e-> System.exit(0));
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(0, 70,70, 70);

        add(buttonPanel, c);
    }
}
