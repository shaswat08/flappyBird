import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel {

    // game size
    int gameWidth = 360;
    int gameHeight = 640;

    // images
    Image bgImg;
    Image birdImg;
    Image topImg;
    Image bottomImg;

    // bird
    int birdX = gameWidth / 10;
    int birdY = gameHeight / 2;
    int birdWidth = 35;
    int birdHeight = 30;

    // pipes
    int pipeX = gameWidth;
    int pipeY = 0;
    int pipeWidth = 60;
    int pipeHeight = 512;

    // movement
    int velocityY = 0;
    int gravity = 1;
    int velocityX = -5;

    // game
    Timer gameLoop;
    Timer pipeLoop;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    double score = 0;
    double highScore = 0;

    // instances
    Bird bird;

    FlappyBird() {
        setPreferredSize(new Dimension(gameWidth, gameHeight));

        bgImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImg);

        pipes = new ArrayList<Pipe>();

        setFocusable(true);

        gameLoop = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
                repaint();

                if (gameOver) {
                    gameLoop.stop();
                    pipeLoop.stop();
                }
            }
        });
        gameLoop.start();

        pipeLoop = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        pipeLoop.start();

        // keypress
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    velocityY = -12;
                }
                if (gameOver) {
                    // restart the game
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        bird.y = birdY;
                        velocityY = 0;
                        pipes.clear();
                        score = 0;
                        gameOver = false;

                        gameLoop.start();
                        pipeLoop.start();
                    }
                }
            }
        });
    }

    // bird class
    public class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // pipe class

    public class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void placePipes() {
        int randomY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2)); // so the top pipe' y position
                                                                                         // starts in a random position
                                                                                         // everytime
        int gap = gameHeight / 4;

        Pipe topPipe = new Pipe(topImg);
        topPipe.y = randomY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomImg);
        bottomPipe.y = topPipe.y + pipeHeight + gap;
        pipes.add(bottomPipe);
    }

    // movement function
    public void move() {
        bird.y += velocityY;
        velocityY += gravity;

        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }

            if (gameOver) {
                if (score > highScore) {
                    highScore = score;
                }
            }
        }

        if (bird.y > gameHeight) {
            gameOver = true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // draw images into the panel
    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, gameWidth, gameHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
            g.drawString("High Score: " + String.valueOf((int) highScore), 10, 75);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
            g.drawString("High Score: " + String.valueOf((int) highScore), 10, 75);
        }
    }
}
