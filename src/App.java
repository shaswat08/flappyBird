import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {

        // window size
        int windowWidth = 360;
        int windowHeight = 640;

        // initialize jframe
        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // flappybird object
        FlappyBird flappyBird = new FlappyBird();

        // adding the panel to the window
        frame.add(flappyBird);
        frame.pack();

        frame.setVisible(true);
    }
}
