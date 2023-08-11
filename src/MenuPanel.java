import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class MenuPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    private static final Color MAIN_COLOR = new Color(0xC2FCE1);
    private final JTextField nickname =  new JTextField();
    private final JPanel mainMenuPanel = new JPanel(new GridBagLayout());
    private final JPanel nicknamePanel = new JPanel(new GridBagLayout());
    private final JPanel leaderboardPanel = new JPanel(new GridBagLayout());
    private JTextArea leaderboardTextArea = new JTextArea();
    private TreeMap<String, Integer> leaderBoardMap;

    MenuPanel(){
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });
        var cardLayout = new CardLayout();
        setLayout(cardLayout );
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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

        mainMenuPanel.setBackground(MAIN_COLOR);

        ImageIcon logo = new ImageIcon("img/snake-logo.png");
        var logoLabel = new JLabel(logo);
        mainMenuPanel.add(logoLabel, c);

        var buttonPanel = new JPanel();
        buttonPanel.setBackground(MAIN_COLOR);
        var layout = new GridLayout(4,1);
        layout.setVgap(15);
        buttonPanel.setLayout(layout);
        var font = new Font("Rubik", Font.PLAIN, 20);
        var newGame = new JButton("New game".toUpperCase());
        newGame.setFont(font);
        var leaderboardButton = new JButton("Leaderboard".toUpperCase());
        leaderboardButton.setFont(font);
        leaderboardButton.addActionListener(e->{
            cardLayout.show(this, "leaderboard_panel");
            leaderboardTextArea.setText("");
            leaderBoardMap = readLeaderboard();
            var entrySet = leaderBoardMap.entrySet();
            int i = 1;
            for (Map.Entry entry:
                 entrySet) {
                leaderboardTextArea.append(String.format("%3d. %-20s\t%5d\n", i++, entry.getKey() + ":", (int)entry.getValue()));
            }
        });
        var options = new JButton("Options".toUpperCase());
        options.setFont(font);
        var exit = new JButton("Exit".toUpperCase());
        exit.setFont(font);
        buttonPanel.add(newGame);

        newGame.addActionListener(e->{
            cardLayout.show(this, "nick_panel");
        });
        buttonPanel.add(leaderboardButton);
        buttonPanel.add(options);
        buttonPanel.add(exit);
        exit.addActionListener(e-> System.exit(0));
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(0, 70,70, 70);

        mainMenuPanel.add(buttonPanel, c);

        this.add(mainMenuPanel, "main_panel");

        nicknamePanel.setBackground(MAIN_COLOR);
        JLabel enterLabel = new JLabel("Enter your nickname:");
        var font2 = new Font("Rubik Medium", Font.PLAIN, 25);
        enterLabel.setFont(font2);
        enterLabel.setForeground(new Color(0x52B084));
        nickname.setFont(font);
        var returnButtons = new JButton[2];
        returnButtons[0]= new JButton("Return");
        returnButtons[0].setFont(font);
        returnButtons[0].addActionListener(e->{
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
        nicknamePanel.add(playButton, c);
        c.gridx = 1;
        c.insets.set(10, 10,200, 80);
        nicknamePanel.add(returnButtons[0], c);

        this.add(nicknamePanel, "nick_panel");

        leaderboardPanel.setBackground(MAIN_COLOR);
        ImageIcon leaderboardIcon = new ImageIcon("img/leaderboard.png");
        var leaderboardLabel = new JLabel(leaderboardIcon);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.insets.set(20, 0,5, 0);
        leaderboardPanel.add(leaderboardLabel, c);


        var contextPanel = new JPanel();
        contextPanel.setBackground(MAIN_COLOR);
        var scrollPane = new JScrollPane(contextPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        leaderboardTextArea.setFont(new Font("Rubik", Font.PLAIN, 25));
        leaderboardTextArea.setEditable(false);
        leaderboardTextArea.setBorder(BorderFactory.createEmptyBorder());
        leaderboardTextArea.setBackground(MAIN_COLOR);
        leaderboardTextArea.setForeground(new Color(0x43524B));
        scrollPane.setPreferredSize(new Dimension(440, 350));
        contextPanel.add(leaderboardTextArea);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.gridheight = 5;
        c.insets.set(5, 80,5, 80);
        leaderboardPanel.add(scrollPane,c);
        c.gridheight = 1;
        c.gridy = 7;
        c.insets.set(10, 80,40, 80);
        c.fill = GridBagConstraints.BOTH;
        returnButtons[1]= new JButton("Return");
        returnButtons[1].setFont(font);
        leaderboardPanel.add(returnButtons[1], c);

        returnButtons[1].addActionListener(e->{
            cardLayout.show(this, "main_panel");
        });
        this.add(leaderboardPanel, "leaderboard_panel");


    }

    public TreeMap<String, Integer> readLeaderboard(){
        File file = new File("leaderboard");
        var leaderboard = new TreeMap<String, Integer>();

        try {
            if (file.exists()) {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String nickname = myReader.next();
                    int score = myReader.nextInt();
                    if(leaderboard.containsKey(nickname)) score = Math.max(score, leaderboard.get(nickname));
                    leaderboard.put(nickname, score);
                }
                myReader.close();
            } else {
                file.createNewFile();
            }
        }
        catch (IOException e){
            System.out.print(e.getMessage());
        }
        var sortedLeaderboard= Comparators.sortByValues(leaderboard);
        return sortedLeaderboard;
    }
    public String getNickname(){
        String nick = nickname.getText();
        if(nick.contains(" ")){
            nick = nick.replaceAll(" ", "_");
        }
        if(nick.equals("")) nick = "unknown_player";
        return nick;
    }
}
