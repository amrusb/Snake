import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] snakeX = new int[GAME_UNITS];
    final int[] snakeY = new int[GAME_UNITS];
    int bodyParts = 2;
    int score = 0;
    Coordinates apple;
    enum dirs {LEFT, RIGHT, UP, DOWN}
    dirs direction = dirs.RIGHT;
    boolean running = false;
    Timer timer;
    boolean paused = true;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(0x43524B));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        //timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        /* //GRID
        for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, WIDTH, i*UNIT_SIZE);
        }*/

        if(running){
            g.setColor(new Color(0xB05F53));
            g.fillOval(apple.getX(), apple.getY(), UNIT_SIZE, UNIT_SIZE);
            for (int i = 0;  i < bodyParts; i++) {
                if(i == 0){
                    g.setColor(new Color(0x54BD4F));
                    g.fillRect(snakeX[0], snakeY[0], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(0x54C770));
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
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

    public void gameOver(Graphics g){
        Image image = new ImageIcon("img/GameOver.png").getImage();
        int x = (WIDTH - image.getWidth(null)) / 2;
        g.drawImage(image, x, 10, null);
        String scoreInfo = "Score: " + score;
        g.setColor(new Color(0x65C27B));

        var font = new Font("Rubik Black", Font.PLAIN, 30);
        g.setFont(font);
        var g2 = (Graphics2D) g;
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(scoreInfo, context);
        x = (int)((WIDTH - bounds.getWidth()) / 2);
        g.drawString(scoreInfo, x, image.getHeight(null) + 30);
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
    }
}
