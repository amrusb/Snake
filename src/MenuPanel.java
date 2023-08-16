import Utils.Comparators;
import Utils.Sound;
import Utils.SoundTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MenuPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    private static final Color MAIN_COLOR = new Color(0xC2FCE1);
    private  GridBagConstraints c = new GridBagConstraints();
    private Font font = new Font("Rubik", Font.PLAIN, 20);
    private Font font2 = new Font("Rubik Medium", Font.PLAIN, 25);
    private JTextArea leaderboardTextArea = new JTextArea();
    private TreeMap<String, Integer> leaderBoardMap;
    private static Option sizeOption = Option.REGULAR;
    private static Option speedOption = Option.MEDIUM;
    private final MainMenuPanel mainMenuPanel = new MainMenuPanel();
    private final NickNamePanel nicknamePanel = new NickNamePanel();
    private final JPanel leaderboardPanel = new LeaderBoardPanel();
    private final OptionsPanel optionsPanel = new OptionsPanel();
    private final CardLayout cardLayout = new CardLayout();
    private Sound sound;
    private final Runnable BEEP_SOUND =
            (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
    MenuPanel(){
        try{
            sound = new Sound();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });
        setLayout(cardLayout);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.add(mainMenuPanel, "main_panel");
        this.add(nicknamePanel, "nick_panel");
        this.add(leaderboardPanel, "leaderboard_panel");
        this.add(optionsPanel, "options_panel");

    }
    private class MainMenuPanel extends JPanel{
        MainMenuPanel(){
            setLayout(new GridBagLayout());
            setBackground(MAIN_COLOR);

            c.weightx = 100;
            c.weighty = 100;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.insets.set(40, 0,30, 0);

            ImageIcon logo = new ImageIcon("img/snake-logo.png");
            var logoLabel = new JLabel(logo);
            add(logoLabel, c);

            var buttonPanel = new JPanel();
            buttonPanel.setBackground(MAIN_COLOR);
            var layout = new GridLayout(4,1);
            layout.setVgap(15);
            buttonPanel.setLayout(layout);

            var newGame = new JButton("New game".toUpperCase());
            newGame.setFont(font);
            var leaderboardButton = new JButton("Leaderboard".toUpperCase());
            leaderboardButton.setFont(font);
            leaderboardButton.addMouseListener(new MouseSound());
            leaderboardButton.addActionListener(e->{
                cardLayout.show(MenuPanel.this, "leaderboard_panel");
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
            newGame.addMouseListener(new MouseSound());
            newGame.addActionListener(e->cardLayout.show(MenuPanel.this, "nick_panel"));
            buttonPanel.add(leaderboardButton);
            buttonPanel.add(options);
            options.addMouseListener(new MouseSound());
            options.addActionListener(e-> cardLayout.show(MenuPanel.this, "options_panel"));
            buttonPanel.add(exit);
            exit.addMouseListener(new MouseSound());
            exit.addActionListener(e-> System.exit(0));
            c.gridx = 0;
            c.gridy = 1;
            c.fill = GridBagConstraints.BOTH;
            c.insets.set(0, 70,70, 70);

            add(buttonPanel, c);
        }
    }
    private class NickNamePanel extends JPanel{
        private final JTextField nickname =  new JTextField();
        NickNamePanel(){
            setLayout(new GridBagLayout());
            setBackground(MAIN_COLOR);

            JLabel enterLabel = new JLabel("Enter your nickname:");
            enterLabel.setFont(font2);
            enterLabel.setForeground(new Color(0x52B084));
            nickname.setFont(font);
            var returnButton = new JButton("Return");
            returnButton.setFont(font);
            returnButton.addMouseListener(new MouseSound());
            returnButton.addActionListener(e->{
                cardLayout.show(MenuPanel.this, "main_panel");
            });
            var playButton = new JButton("Play");
            playButton.setFont(font);
            playButton.addMouseListener(new MouseSound());
            playButton.addActionListener(e->{
                GameFrame.setState(State.GAME);
                GameFrame.changePanel();
                cardLayout.show(MenuPanel.this, "main_panel");
            });

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            c.insets.set(200, 80,10, 80);
            add(enterLabel, c);
            c.gridy = 1;
            c.insets.set(10, 80,10, 80);
            nickname.setHorizontalAlignment(JTextField.CENTER);
            add(nickname, c);

            var gridLayout = new GridLayout(1, 2);
            gridLayout.setHgap(10);
            var buttonPanel = new JPanel(gridLayout);
            c.gridy = 2;
            c.insets.set(10, 80,200, 80);
            buttonPanel.add(playButton);
            buttonPanel.add(returnButton);

            add(buttonPanel,c);
        }

        public String getText() {
            return nickname.getText();
        }
    }
    private class LeaderBoardPanel extends JPanel{
        LeaderBoardPanel(){
            setLayout(new GridBagLayout());
            setBackground(MAIN_COLOR);

            ImageIcon leaderboardIcon = new ImageIcon("img/leaderboard.png");
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.insets.set(20, 0,5, 0);
            add(new JLabel(leaderboardIcon), c);

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
            add(scrollPane,c);
            c.gridheight = 1;
            c.gridy = 7;
            c.insets.set(10, 80,40, 80);
            c.fill = GridBagConstraints.BOTH;

            var returnButton= new JButton("Return");
            returnButton.setFont(font);
            add(returnButton, c);
            returnButton.addMouseListener(new MouseSound());
            returnButton.addActionListener(e->{
                cardLayout.show(MenuPanel.this, "main_panel");
            });
        }
    }
    private class OptionsPanel extends JPanel{
        OptionsPanel(){
            setLayout(new GridBagLayout());
            setBackground(MAIN_COLOR);

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets.set(150, 0,20, 0);
            ImageIcon logo = new ImageIcon("img/options.png");
            var logoLabel = new JLabel(logo);
            add(logoLabel, c);

            c.gridwidth = 1;
            c.gridy = 1;
            c.insets.set(10, 80,10, 5);
            JLabel speedOptionLabel = new JLabel("Speed of the snake:");
            speedOptionLabel.setFont(font);
            add(speedOptionLabel, c);

            JComboBox<Option> speedOptionCB = new JComboBox<>(new Option[]{Option.SLOW, Option.MEDIUM, Option.FAST});
            speedOptionCB.setEditable(false);
            speedOptionCB.setSelectedIndex(1);
            speedOptionCB .setFont(font);
            c.insets.set(10, 5,10, 80);
            c.gridx = 1;
            add(speedOptionCB, c);
            c.gridx = 0;
            c.gridy = 2;
            c.insets.set(10, 80,10, 5);
            JLabel sizeOptionLabel = new JLabel("Size of the snake:");
            sizeOptionLabel.setFont(font);
            add(sizeOptionLabel, c);

            c.gridx = 1;
            c.insets.set(10, 5,10, 80);
            JComboBox<Option> sizeOptionCB = new JComboBox<>(new Option[]{Option.SMALL, Option.REGULAR, Option.BIG});
            sizeOptionCB.setEditable(false);
            sizeOptionCB.setSelectedIndex(1);
            sizeOptionCB.setFont(font);
            add(sizeOptionCB, c);

            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.insets.set(10, 80,150, 80);
            var gridLayout = new GridLayout(1, 2);
            gridLayout.setHgap(10);
            var buttonPanel = new JPanel(gridLayout);
            JButton acceptButton = new JButton("Accept".toUpperCase());
            acceptButton.setFont(font);
            acceptButton.addMouseListener(new MouseSound());
            acceptButton.addActionListener(e->{
                sizeOption = (Option)sizeOptionCB.getSelectedItem();
                speedOption = (Option)speedOptionCB.getSelectedItem();
                String message = "All changes have been saved.";
                BEEP_SOUND.run();
                JOptionPane.showConfirmDialog(
                        MenuPanel.this.getParent(),
                        message,
                        "Confirmation",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
            });
            buttonPanel.add(acceptButton);
            JButton cancelButton = new JButton("Cancel".toUpperCase());
            cancelButton.setFont(font);
            buttonPanel.add(cancelButton);
            cancelButton.addMouseListener(new MouseSound());
            cancelButton.addActionListener(e->{
                cardLayout.show(MenuPanel.this, "main_panel");
            });
            add(buttonPanel, c);
        }
    }
    class MouseSound extends MouseAdapter{
        public void mouseEntered(MouseEvent e){
            sound.play(SoundTypes.BUTTON_CLICK);
        }
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
        String nick = nicknamePanel.getText();
        if(nick.contains(" ")){
            nick = nick.replaceAll(" ", "_");
        }
        if(nick.equals("")) nick = "unknown_player";
        return nick;
    }
    public static int getSpeedOption(){
        int speed = 0;
        switch (speedOption){
            case SLOW -> speed =  85;
            case MEDIUM -> speed = 65;
            case FAST -> speed = 55;
        }
        return speed;
    }
    public static int getSizeOption(){
        int size = 0;
        switch (sizeOption){
            case SMALL -> size =  20;
            case REGULAR -> size = 40;
            case BIG -> size = 60;
        }
        return size;
    }
    enum Option {SMALL, REGULAR, BIG, SLOW, MEDIUM, FAST};
}
