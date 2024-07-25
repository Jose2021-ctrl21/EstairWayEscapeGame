
package PracticeBounce;
//importing necessary libraries

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
//We have a class BounceGame and we extends the JPanel and implements KeyListener
public class StairwayEscape extends JPanel implements KeyListener {
    
    

    //Initialized a value of variables and it can be change or can hold different values
    int x = 0, y = 90, xa = 0, ya = 1, count = 0, cnt = 0, level, score = 0, highestScore = 0,
            tempScore = 0, life = 4, hit = 0, starter = 1;
    String playerName = "", previousPlayerName = "";
    //Created a constant variables means there values cannot be changed
    private static final int STAIRS_COUNT = 6;
    private static final int STAIRS_WIDTH = 50;
    private static final int STAIRS_HEIGHT = 10;
    private int[] stairsX = new int[STAIRS_COUNT];
    private int[] stairsY = new int[STAIRS_COUNT];
    private int[] racquetSpeed = new int[STAIRS_COUNT];

    //Create a movePlayer method
    private void movePlayer() {
        // Create a condition that adds 'xa' to the current value of 'x' only if
        // the position of the ball is greater than or equal to 0 and less than
        // the height of the frame minus 30, which is 400 - 30 = 370 pixels along the x-axis.

        if (x + xa >= 0 && x + xa <= getWidth() - 30) {
            x += xa;
        }
        //Same process width x-axis
        if (y + ya >= 0 && y + ya <= getHeight() - 30) {
            y += ya;
        }

        // create an intersected boolean variable and initialize to false.
        boolean intersected = false;
        for (int i = 0; i < STAIRS_COUNT; i++) {
            if (getPlayerBounds().intersects(getRacquetBounds(i))) {
                if (cnt >= 1) {
                    // Adapt to racquet's movement
                    ya = -racquetSpeed[i] * (cnt);
                } else {
                    // Adapt to racquet's movement
                    ya = -racquetSpeed[i];
                }
                intersected = true;
                // Break out of the loop to avoid interference with other racquets
                break;
            }
        }

        // If not intersected, continue moving downward
        if (!intersected) {
            // Set to a positive value for continuous downward movement
            if (level == 0) {
                ya = 1;
            } else {
                //when level increased, the movement downward also increase
                ya = level;
            }
        }
        //Condition if an objects hits the spikes. In technical speaking, when
        // the ball location reaches less than 88 or greater than 691 pixels, then
        //y will be set to 300
        if (y < 80 || y > 615) {
            y = 300;
            // Inside the condition (y < 88 || y > 691), there are two sub-conditions.
            // First, if 'life' is equal to 0, then the gameOver method is called,
            // redirecting to that method. Second, if the 'hit' value is less than or equal to zero,
            // decrement 'life' by 1 and set 'hit' to 200. The 'hit' variable acts as
            // immunity, providing a 200-millisecond delay before the object loses immunity
            // against spikes.
            if (life == 0) {
                gameOver();
            } else {
                if (hit <= 0) {
                    life -= 1;
                    hit = 200;
                }
            }
        }
        hit--;

    }

    //Method for the racquet's movement
    private void moveStairs() {
        // We initilized the speed of racquet to 1
        int initialSpeed = 1;

        //We create a speed level here
        count++;
        if (count > 1000) {
            count = 0;
            cnt += 1;
            if (cnt % 2 == 0 && cnt != 0) {
                level += 1;
            }
        }

        // Check for intersections and adjust positions
        for (int i = 0; i < STAIRS_COUNT; i++) {
            racquetSpeed[i] = initialSpeed;
            // Decrease the Y-coordinate or increase the speed of racquet
            stairsY[i] -= racquetSpeed[i] + level;
            // Check for intersections with other racquets
            for (int j = 0; j < STAIRS_COUNT; j++) {
                if (i != j && getRacquetBounds(i).intersects(getRacquetBounds(j))) {
                    // If intersection occurs, adjust the position of the current racquet
                    stairsX[i] = (int) (Math.random() * (getWidth() - STAIRS_WIDTH));
                    stairsY[i] = getHeight() - STAIRS_HEIGHT;
                }
            }

            //if racquet reaches a pixels of less than 90, then it will reset
            //its y and x axis.
            if (stairsY[i] < 90) {
                // Reset the racquet x axis position
                stairsX[i] = (int) (Math.random() * (getWidth() - STAIRS_WIDTH));
                // Reset the racquet to the bottom when it goes above the frame
                stairsY[i] = getHeight() - STAIRS_HEIGHT;
            }
        }
    }

    //create a new shape of the ball
    private Rectangle getPlayerBounds() {
        return new Rectangle(x, y, 30, 30);
    }

    //create new shape for racquets
    private Rectangle getRacquetBounds(int index) {
        return new Rectangle(stairsX[index], stairsY[index], STAIRS_WIDTH, STAIRS_HEIGHT - 9);
    }

    private void checkPlayerInput() {
        while (true) {
            playerName = JOptionPane.showInputDialog("Enter your name:");
            if (playerName == null) {
                // User canceled the input, handle accordingly (e.g., exit the application or show a message)
                JOptionPane.showMessageDialog(null, "System will now terminate", "Notification", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // or handle it in another way based on your application's logic
            } else if (playerName.isEmpty()) {
                // Show a simple information message
                JOptionPane.showMessageDialog(null, "Input cannot be empty", "Notification", JOptionPane.INFORMATION_MESSAGE);
            } else {
                break;
            }
        }
    }

    private void startGame() {
        // Since the starter initialization is 1, the first loop will display the pane
        if (starter == 1) {
            checkPlayerInput();

            // Create a variable to track whether the game should start
            boolean shouldStartGame = false;

            // Create a loop to handle user input
            while (true) {
                int start = JOptionPane.showOptionDialog(
                        this,
                        "Play the game?",
                        "Bouncing ball",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null
                );

                if (start == JOptionPane.YES_OPTION) {
                    shouldStartGame = true;
                    break;
                } else if (start == JOptionPane.NO_OPTION) {
                    // If no, then exit the game
                    checkPlayerInput();
                } else {
                    // If the user closed the dialog (e.g., by clicking the 'X' button)
                    // Assume they want to exit the game
                    shouldStartGame = false;
                    break;
                }
            }

            // If the user decided to start the game, set starter to 0
            if (shouldStartGame) {
                starter = 0;
            } else {
                // If the user chose to exit, exit the game
                System.exit(0);
            }
        }
    }

    //Create a method gameover so we can call this later
    private void gameOver() {
        //This condition that if previous is less than score, that is the moment that the score will be passed to previousScore and previousPlayerName
        if (highestScore < score) {
            highestScore = score;
            previousPlayerName = playerName;
        }
        //We just create a choice varible and pass the option dialog as value
        int choice = JOptionPane.showOptionDialog(
                this,
                "Game over!\nYour score: " + score + "\nDo you want to continue?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        //set life to 4
        life = 4;
        //This condition means that if user chose the yes option, then the game will be reseted
        if (choice == JOptionPane.YES_OPTION) {
            for (int i = 0; i < STAIRS_COUNT; i++) {
                stairsY[i] = getHeight() - STAIRS_HEIGHT;
                level = 0;
                cnt = 0;
                score = 0;
                tempScore = 0;
                xa = 0;
                stairsX[i] = (int) (Math.random() * (getWidth() - STAIRS_WIDTH));
                stairsY[i] = (int) (Math.random() * (getHeight() - STAIRS_HEIGHT));
                break;
            }
        } else {
            for (int i = 0; i < STAIRS_COUNT; i++) {
                stairsY[i] = getHeight() - STAIRS_HEIGHT;
                level = 0;
                cnt = 0;
                score = 0;
                tempScore = 0;
                xa = 0;
                stairsX[i] = (int) (Math.random() * (getWidth() - STAIRS_WIDTH));
                stairsY[i] = (int) (Math.random() * (getHeight() - STAIRS_HEIGHT));
                break;
            }
            starter = 1;
            startGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Handle arrow key inputs to control the player. Only left and right arrows
        if (key == KeyEvent.VK_LEFT) {
            xa = -1 + (-level);
        } else if (key == KeyEvent.VK_RIGHT) {
            xa = 1 + level;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Stop player movement when arrow key is released
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            xa = 0;
        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            ya = 0;
        }
    }

    @Override
    //This is a method with a parameter of Graphics library
    public void paint(Graphics g) {
        // Call the paint method of the superclass (JPanel) to ensure proper repainting of the panel
        super.paint(g);

        // Create a Graphics2D object (g2d) by casting the Graphics object (g) to Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        // Set a rendering hint for antialiasing to improve the visual quality of graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //This is to change the frame background each up to 5 levels.
        int v = level + 1;
        if (v == 1) {
            setBackground(Color.white);
        } else if (v == 2) {
            setBackground(Color.cyan);
        } else if (v == 3) {
            setBackground(Color.green);
        } else if (v == 4) {
            setBackground(Color.yellow);
        } else {
            setBackground(Color.pink);
        }

        //draw racquets based on its number then set its x and y axis randomly
        g2d.setColor(Color.black);
        for (int i = 0; i < STAIRS_COUNT; i++) {
            g.fillRect(stairsX[i], stairsY[i], STAIRS_WIDTH, STAIRS_HEIGHT);
        }

        // SPIKES TOP
        //create a 3 random variables that will be use change color randomly
        int r1 = (int) (Math.random() * 255);
        int g1 = (int) (Math.random() * 1);
        int b1 = (int) (Math.random() * 1);
        //This condition checks if level is greater or equal to 3, then it will 
        //set the color randomly. Otherwise, the color will be black
        if (v >= 3) {
            g.setColor(new Color(r1, g1, b1));
        } else {
            g.setColor(Color.black);
        }
        //These spikes top and bottom is to draw a triangulat shapes and loop
        //it 30 times.
        //SPIKES TOP
        for (int i = 0; i < 30; i++) {
            int[] xPoints = {10 + i * 20, 20 + i * 20, 30 + i * 20};
            int[] yPoints = {50, 90, 50};
            g.fillPolygon(xPoints, yPoints, 3);
        }
        // SPIKES BOTTOM
        for (int i = 0; i < 30; i++) {
            int[] xPoints = {10 + i * 20, 20 + i * 20, 30 + i * 20};
            int[] yPoints = {690, 640, 690};
            g.fillPolygon(xPoints, yPoints, 3);
        }

        //set the backgraound header to cyan
        g.setColor(Color.cyan);
        g.fillRect(0, 0, 400, 70);

        //This is to draw 5 red dots as life, it will draw based on the
        //life value
        //LIFE
        g.setColor(Color.red);
        for (int i = 0; i <= life; i++) {
            g.fillOval(160 + i * 18, 24, 15, 15);
        }

        // This condition checks if the value of the 'hit' variable is less than or equal to 0.
        // If true, it sets the color to blue. Otherwise, it sets the color randomly.
        // This condition serves as an indicator that the ball was hit by spikes.
        if (hit <= 0) {
            g.setColor(Color.blue);
        } else {
            g.setColor(new Color(r1, b1, g1));
        }
        g.fillOval(x, y, 30, 30);

        int fontSize1 = 16;
        Font font1 = new Font("Arial", Font.PLAIN, fontSize1); // You can change the font name and style if needed
        g.setFont(font1);
        //These is a condition that when level is less or equal to zero, level will set to 1,
        //Otherwise, it will display the current level
        g2d.setColor(Color.black);
        if (level <= 0) {
            g.drawString("Level: " + 1, 300, 60);
        } else {
            g.drawString("Level: " + v, 300, 60);
        }
        //We just increment a tempScore, then divide this into 2 that serve as score
        tempScore++;
        score = tempScore / 2;
        g.drawString("Score: " + score, 20, 40);
        g.drawString("Player: " + playerName, 20, 60);

        g.drawString("[STAIRWAY ESCAPE]", 130, 20);
        //This is to change the font of a texts
        int fontSize = 10;
        Font font = new Font("Arial", Font.PLAIN, fontSize); // You can change the font name and style if needed
        g.setFont(font);
        g.drawString("HighestScore: " + highestScore, 165, 50);
        g.drawString(previousPlayerName, 190, 60);

    }

    //This is the main method where the first execution of the program begins
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Bounce game!");
        StairwayEscape game = new StairwayEscape();
        frame.add(game);
        frame.setSize(400, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();
        // Put the frame to the center of the window screen
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(game);

        // Initialize racquet speeds to a common value and adjust initial positions randomly
        for (int i = 0; i < STAIRS_COUNT; i++) {
            game.stairsX[i] = (int) (Math.random() * (frame.getWidth() - STAIRS_WIDTH));
            game.stairsY[i] = (int) (Math.random() * (frame.getHeight() - STAIRS_HEIGHT));
        }
        // We invoke the methods of the 'game' object within the while loop to create a continuous loop.
        // This ensures that the game keeps running repeatedly.
        while (true) {
            game.startGame();
            game.moveStairs();
            game.movePlayer();
            game.repaint();
            Thread.sleep(10);
        }
    }
}
