import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private final int UNIT_SIZE = MenuPanel.getSizeOption();
    private final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    private final int DELAY = MenuPanel.getSpeedOption();
    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private int bodyParts = 2;
    private int score = 0;
    private final String nickname;
    private Coordinates apple;
    private enum dirs {LEFT, RIGHT, UP, DOWN}
    private dirs direction = dirs.RIGHT;
    private boolean running = false;
    private Timer timer;
    private boolean paused = true;
    private final Random random;
    private final int current_high_score;
    private Sound sound;

    GamePanel(String nickname, int high_score){
        this.nickname = nickname;
        current_high_score = high_score;
        random = new Random();
        try{
            sound = new Sound();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0x43524B));
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });

        startGame();
    }

    public void startGame(){
        GameFrame.setState(State.IN_GAME);
        newApple();
        running = true;
        timer = new Timer(DELAY, this);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(paused) {
            pause(g);
        }
        else{
            draw(g);
        }

    }

    public void draw(Graphics g){
        if(running){
            g.setColor(new Color(0xB05F53));
            g.fillOval(apple.getX(), apple.getY(), UNIT_SIZE, UNIT_SIZE);
            for (int i = 0;  i < bodyParts; i++) {
                if(i == 0){
                    g.setColor(new Color(0x54C770));
                    g.fillRect(snakeX[0], snakeY[0], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(0x96D0A5));
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            if(paused) System.out.println("DUPA BLADA");
        }
        else gameOver(g);
    }

    public void newApple(){
        boolean valid = false;

        checkIfCorrect:
        do{
            int x = random.nextInt(WIDTH/UNIT_SIZE) * UNIT_SIZE;
            int y = random.nextInt(HEIGHT/UNIT_SIZE)* UNIT_SIZE;

            for (int i = 0; i < bodyParts; i++) {
                if(snakeX[i] == x && snakeY[i] == y){
                    valid = false;

                    continue checkIfCorrect;
                }
                else valid = true;
            }
            apple = new Coordinates(x, y);

        }while(!valid);


    }
    public void move(){
        for (int i = bodyParts; i > 0 ; i--) {
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }

        switch (direction){
            case UP ->  snakeY[0] = snakeY[0] - UNIT_SIZE;
            case DOWN -> snakeY[0] = snakeY[0] + UNIT_SIZE;
            case LEFT -> snakeX[0] = snakeX[0] - UNIT_SIZE;
            case RIGHT -> snakeX[0] = snakeX[0] + UNIT_SIZE;
        }
    }

    public void checkApple(){
        if((snakeX[0] == apple.getX() && snakeY[0] == apple.getY())){
            bodyParts++;
            score++;
            sound.play(SoundTypes.POINT_GAINED);
            newApple();
        }
    }

    public void checkCollisions(){
        for(int i = bodyParts; i > 0; i--){
            if((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) running = false;
            if((snakeX[0] >= WIDTH) || (snakeY[0] >= HEIGHT)) running = false;
            if((snakeX[0]< 0) || (snakeY[0]  < 0)) running = false;
        }
    }
    public void pause(Graphics g){
        Image image = new ImageIcon("img/play.png").getImage();
        int x = (WIDTH - image.getWidth(null)) / 2;
        int y = (HEIGHT - image.getHeight(null)) / 2;
        g.drawImage(image, x, y, null);

        String pauseInfo = "Press space to start";
        var font = new Font("Rubik Bold", Font.PLAIN, 20);
        g.setColor(new Color(0x65C27B));
        g.setFont(font);
        var g2 = (Graphics2D) g;
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(pauseInfo, context);
        x = (int)((WIDTH - bounds.getWidth()) / 2);
        g.drawString(pauseInfo, x, image.getHeight(null) + y + 40);
    }
    public void gameOver(Graphics g){
        var g2 = (Graphics2D) g;
        timer.stop();
        Image image = new ImageIcon("img/game-over.png").getImage();
        int x = (WIDTH - image.getWidth(null)) / 2;
        int y = 80;
        g2.drawImage(image, x, y, null);
        String scoreInfo = "Score: " + score;
        g2.setColor(new Color(0x65C27B));

        var font = new Font("Rubik Black", Font.PLAIN, 30);
        g2.setFont(font);

        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(scoreInfo, context);
        x = (int)((WIDTH - bounds.getWidth()) / 2);
        y += image.getHeight(this)  + 50;
        g2.drawString(scoreInfo, x, y);

        GameFrame.setState(State.GAME_OVER);
        if(score > current_high_score && score != 0) {
            sound.play(SoundTypes.NEW_HIGH_SCORE);
            image = new ImageIcon("img/new-high-score.png").getImage();
            x = (WIDTH - image.getWidth(this)) / 2;
            y += bounds.getHeight();
            g2.drawImage(image, x, y, null);
            y += image.getHeight(this) + 40;
        }
        else {
            sound.play(SoundTypes.GAME_OVER);
            y += bounds.getHeight() + 10;
        };

        var font2 = new Font("Rubik", Font.PLAIN, 25);
        g2.setFont(font2);
        g2.setColor(new Color(0x96D0A5));
        String playAgainInfo = "Press space to play again.";
        bounds = font2.getStringBounds(playAgainInfo, context);
        x = (int)((WIDTH - bounds.getWidth()) / 2);
        g2.drawString(playAgainInfo, x, y);
        String exitInfo = "Press esc to exit to main menu.";
        bounds = font2.getStringBounds(exitInfo, context);
        x = (int)((WIDTH - bounds.getWidth()) / 2);
        y += bounds.getHeight() + 10;
        g2.drawString(exitInfo, x, y);
        addScoreToFile();
    }

    private void addScoreToFile(){
        String fileName = "leaderboard";
        Path path = Paths.get(fileName );
        File file = new File(fileName);

        try{
            BufferedWriter writer;
            if(!file.exists()) {
                file.createNewFile();
                writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE);
                writer.write(nickname + "\t" + score);
            }
            else{
                writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND);
                writer.write("\n"+nickname + "\t" + score);
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            if(GameFrame.getCurrentState() == State.IN_GAME){
                switch (e.getKeyCode()){
                    case 37 -> {
                        if(!paused)
                            if(direction != dirs.RIGHT)
                                direction = dirs.LEFT;
                    }
                    case 38 -> {
                        if(!paused)
                            if(direction != dirs.DOWN)
                                direction = dirs.UP;
                    }
                    case 39 -> {
                        if(!paused)
                            if(direction != dirs.LEFT)
                                direction = dirs.RIGHT;
                    }
                    case 40 ->{
                        if(!paused)
                            if(direction != dirs.UP)
                                direction = dirs.DOWN;
                    }
                    case 32 ->{
                        if(!paused){
                            timer.stop();
                        }
                        else{
                            timer.start();
                        }
                        paused = !paused;
                    }
                }
            }
            if(GameFrame.getCurrentState() == State.GAME_OVER){
                switch (e.getKeyCode()){
                    case 27 -> GameFrame.setState(State.MENU);
                    case 32 -> GameFrame.setState(State.GAME);
                }
                GameFrame.changePanel();
            }
        }
    }
}
