import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.stream.Stream;

public class MenuPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    private final JTextField nickname =  new JTextField();
    private final JPanel mainMenuPanel = new JPanel(new GridBagLayout());
    private final JPanel nicknamePanel = new JPanel(new GridBagLayout());

    MenuPanel(){
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });
        var cardLayout = new CardLayout();
        setLayout(cardLayout );
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

        mainMenuPanel.setBackground(new Color(0xC2FCE1));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        ImageIcon logo = new ImageIcon("img/snake-logo.png");
        var logoLabel = new JLabel(logo);
        mainMenuPanel.add(logoLabel, c);

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
            cardLayout.show(this, "nick_panel");
        });
        buttonPanel.add(leaderboard);
        buttonPanel.add(options);
        buttonPanel.add(exit);
        exit.addActionListener(e-> System.exit(0));
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(0, 70,70, 70);

        mainMenuPanel.add(buttonPanel, c);

        nicknamePanel.setBackground(new Color(0xC2FCE1));
        JLabel enterLabel = new JLabel("Enter your nickname:");
        var font2 = new Font("Rubik Medium", Font.PLAIN, 25);
        enterLabel.setFont(font2);
        enterLabel.setForeground(new Color(0x52B084));
        nickname.setFont(font);
        var returnButton = new JButton("Return");
        returnButton.setFont(font);
        returnButton.addActionListener(e->{
            cardLayout.show(this, "main_panel");
        });
        var playButton = new JButton("Play");
        playButton.setFont(font);
        playButton.addActionListener(e->{
            GameFrame.setState(State.GAME);
            GameFrame.changePanel();
            cardLayout.show(this, "main_panel");
        });


        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(200, 80,10, 80);
        nicknamePanel.add(enterLabel, c);
        c.gridy = 1;
        c.insets.set(10, 80,10, 80);
        nickname.setHorizontalAlignment(JTextField.CENTER);
        nicknamePanel.add(nickname, c);

        c.gridwidth = 1;
        c.gridy = 2;
        c.insets.set(10, 80,200, 10);
        nicknamePanel.add(returnButton, c);
        c.gridx = 1;
        c.insets.set(10, 10,200, 80);
        nicknamePanel.add(playButton, c);

        this.add(mainMenuPanel, "main_panel");
        this.add(nicknamePanel, "nick_panel");


    }

    public String getNickname(){
        String nick = nickname.getText();
        nickname.setText("");
        if(nick.contains(" ")){
            nick = nick.replaceAll(" ", "_");
        }
        if(nick.equals("")) nick = "unknown_player";
        return nick;
    }
}
