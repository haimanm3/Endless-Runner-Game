package com.mycompany.tweetybird;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public final class TweetyBird implements ActionListener,KeyListener{
    // Cloud management attributes and methods
    private ArrayList<Rectangle> skyline;

private void initializeSkyline() {
    skyline = new ArrayList<>();
    int buildingWidth = 40;
    int baseHeight = SCREEN_HEIGHT - 1;
    int[] buildingHeights = {160, 190, 175, 160, 190, 150, 190, 180, 160, 190, 160, 170, 190, 180, 160, 190, 160, 180, 175};
    for (int i = 0; i < buildingHeights.length; i++) {
        int x = i * (buildingWidth + 5);
        int height = buildingHeights[i];
        skyline.add(new Rectangle(x, baseHeight - height, buildingWidth, height));
    }
}

private void updateSkyline() {
    int maxRight = 0; // Start with zero to find the maximum right edge from existing buildings

    // Calculate the maximum right edge of the currently visible buildings
    for (Rectangle building : skyline) {
        if (building.x + building.width > maxRight) {
            maxRight = building.x + building.width;
        }
        building.x -= 2;  // Move each building to the left
    }

    // Reposition buildings that have moved off-screen
    for (Rectangle building : skyline) {
        if (building.x + building.width < 0) {  // Check if building is completely off the left side
            building.x = maxRight + 5;  // Place the building right after the last one on the right
            maxRight = building.x + building.width; // Update maxRight to include this building
        }
    }
}


    private ArrayList<Rectangle> clouds = new ArrayList<>();

    private void initializeClouds() {
        for (int i = 0; i < 5; i++) { // Initialize five clouds at the start
            addCloud(true);
        }
    }

    private void drawSkyline(Graphics g) {
    g.setColor(Color.LIGHT_GRAY);  // Color for the buildings
    for (Rectangle building : skyline) {
        g.fillRect(building.x, building.y, building.width, building.height);
    }
}


    private void addCloud(boolean start) {
        int width = 200 + new Random().nextInt(100);
        int height = 60 + new Random().nextInt(40);
        int x = start ? new Random().nextInt(800) : 800;
        int y = new Random().nextInt(300);
        clouds.add(new Rectangle(x, y, width, height));
    }

    private void updateClouds() {
        // Create a new ArrayList to hold the updated cloud positions
        ArrayList<Rectangle> updatedClouds = new ArrayList<>();

        // Iterate over the original clouds ArrayList
        for (Rectangle cloud : clouds) {
            // Create a copy of each cloud with updated position
            Rectangle updatedCloud = new Rectangle(cloud);
            updatedCloud.x -= 5; // Move the cloud to the left by 5 units

            // Check if the updated cloud is still on-screen
            if (updatedCloud.x + updatedCloud.width > 0) {
                updatedClouds.add(updatedCloud); // Add the updated cloud to the new ArrayList
            } else {
                // If the cloud is off-screen, add a new cloud at a random position on the right
                updatedClouds.add(new Rectangle(SCREEN_WIDTH, new Random().nextInt(300), updatedCloud.width, updatedCloud.height));
            }
        }

        // Replace the original clouds ArrayList with the updated ArrayList
        clouds = updatedClouds;
    }

    private boolean showMenu; // Flag to indicate whether to show the menu

    private void renderClouds(Graphics g) {
        g.setColor(new Color(230, 230, 230, 150)); // Light gray color for clouds
        for (Rectangle cloud : clouds) {
            g.fillRect(cloud.x, cloud.y, cloud.width, cloud.height);
        }
    }

     public static void main(String[] args) throws Exception {
        tweetybird = new TweetyBird();
    }

    public static TweetyBird tweetybird;
    public Renderer renderer;
    public Rectangle bird;
    public int ticks, yMotion, score;
    public int highScore;

    // Method to load high score from a file
    private void loadHighScore() {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("highscore.txt"))) {
            highScore = Integer.parseInt(reader.readLine());
        } catch (java.io.IOException e) {
            highScore = 0; // If the file does not exist or is unreadable, set high score to 0
        }
    }

    // Method to save high score to a file
    private void saveHighScore() {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("highscore.txt"))) {
            writer.write(String.valueOf(highScore));
        } catch (java.io.IOException e) {
            System.err.println("Error writing high score");
        }
    }

    private BufferedImage userImage;
    private boolean passedObstacle = false;
    public ArrayList<Rectangle> obstaclesList;
    public Random random;
    public boolean gameOver, gameStarted;

    public final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 800;

    public TweetyBird() {
         initializeClouds();
    initializeSkyline();  // Initialize the skyline

        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        random = new Random();

        jframe.add(renderer);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setTitle("Tweety Bird");
        jframe.setVisible(true);

        try {
            java.net.URL imageUrl = getClass().getClassLoader().getResource("tweetybird.png");
            if (imageUrl != null) {
                userImage = ImageIO.read(imageUrl);
            } else {
                System.err.println("Error: Image resource not found.");
            }
        } catch (IOException e) {
            System.err.println("Error loading user image: " + e.getMessage());
        }

        bird = new Rectangle(SCREEN_WIDTH / 2 - 10, SCREEN_HEIGHT / 2 - 10, 40, 40);
        obstaclesList = new ArrayList<>();

        addObstacle(true);
        addObstacle(true);
        addObstacle(true);
        addObstacle(true);

                initializeClouds();
        timer.start();
    }

    public void addObstacle(boolean startGame) {
        loadHighScore();

        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300); // Min height 50, max height 300.

        if (startGame) {

            obstaclesList.add(new Rectangle(SCREEN_WIDTH + width + obstaclesList.size() * 300, SCREEN_HEIGHT - height - 120, width, height)); // Place
                                                                                                                    // a
                                                                                                                    // column/pipe.
            obstaclesList.add(new Rectangle(SCREEN_WIDTH + width + (obstaclesList.size() - 1) * 300, 0, width, SCREEN_HEIGHT - height - space)); // Place
                                                                                                                       // a
                                                                                                                       // column/pipe.
        } else {

            obstaclesList.add(new Rectangle(obstaclesList.get(obstaclesList.size() - 1).x + 600, SCREEN_HEIGHT - height - 120, width, height)); // Place
                                                                                                                       // a
                                                                                                                       // column/pipe.
            obstaclesList.add(new Rectangle(obstaclesList.get(obstaclesList.size() - 1).x, 0, width, SCREEN_HEIGHT - height - space)); // Place a
                                                                                                              // column/pipe.
        }

    }

    public void paintObstacle(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker().darker());
        // Existing pillar drawing
g.fillRect(column.x, column.y, column.width, column.height);

// Set the color for the white trim
g.setColor(Color.WHITE);

// Check if the column is a top column by comparing its y position
if (column.y < 100) {  // This threshold might need adjustment
    // Top column, trim at the bottom
    g.fillRect(column.x, column.y + column.height - 11, column.width, 6);
} else {
    // Bottom column, trim a bit lower than the top edge
    g.fillRect(column.x, column.y + 5, column.width, 6);
}

// Reset the color back to the original color of the pillars
g.setColor(Color.green); // Adjust this if your pillars are a different color

    }

    public void makeJump() {
    if (gameOver) {
        // Possibly add a return statement here to ignore further commands until game is restarted.
        return;
    }

    if (!gameStarted) {
        gameStarted = true;
    } else {
        if (yMotion > 0) {
            yMotion = 0;
        }
        yMotion -= 16;
    }
}

//actionPerformed method
@Override
public void actionPerformed(ActionEvent arg0) {
    int speed = 10;

    if (gameOver) {
        showMenu = true;
        renderer.repaint();  // Repaint immediately if the game is over
        return;  // Exit the method to avoid further processing
    }

    updateClouds();
    updateSkyline();  // Update the skyline's position

    ticks++;
    if (gameStarted) {
        if (ticks % 2 == 0 && yMotion < 15) {
            yMotion += 3;
        }
        bird.y += yMotion;

        // Create a copy of obstaclesList to avoid ConcurrentModificationException
        ArrayList<Rectangle> obstaclesCopy = new ArrayList<>(obstaclesList);
        for (Rectangle obstacle : obstaclesCopy) {
            obstacle.x -= speed;

            if (obstacle.x + obstacle.width < 0) {
                obstaclesList.remove(obstacle);
                addObstacle(false); // Add a new obstacle
            }

            if (obstacle.intersects(bird)) {
                gameOver = true;
                bird.x = obstacle.x - bird.width;
                break;
            }
            if (obstacle.y == 0 && bird.x + bird.width / 2 > obstacle.x + obstacle.width / 2 - 10 && bird.x + bird.width / 2 < obstacle.x + obstacle.width / 2 + 10) {
                score++;
            }
        }

            // Adjust bird position upon game constraints
        if (bird.y > SCREEN_HEIGHT - 120 || bird.y < 0) {
            bird.y = Math.max(0, Math.min(bird.y, SCREEN_HEIGHT - 120 - bird.height));
            gameOver = true;
        }
    }

    renderer.repaint();  // Always repaint at the end of actionPerformed

    if (score > highScore) {
        highScore = score;
        saveHighScore();
    }
}

//repaint method

public void repaint(Graphics g) {
    // Background Color.
    g.setColor(Color.cyan);
    g.fillRect(0, 0, SCREEN_HEIGHT, SCREEN_WIDTH);

    drawSkyline(g); // Draw the skyline after setting the background
    renderClouds(g);

    // Add Ground.
    g.setColor(Color.orange);
    g.fillRect(0, SCREEN_HEIGHT - 120, SCREEN_WIDTH, 120);

    // Add Grass.
    g.setColor(Color.green);
    g.fillRect(0, SCREEN_HEIGHT - 120, SCREEN_WIDTH, 20);

    // Bird (player) icon.
    if (userImage != null) {
        g.drawImage(userImage, bird.x, bird.y, bird.width, bird.height, null);
    }

    for (Rectangle obstacle : obstaclesList) {
        paintObstacle(g, obstacle);
    }

    // Text and menu adjustments
    g.setColor(Color.black);
    g.setFont(new Font("Arial", 1, 40));

    // Separate handling for start menu and game over menu
    if (!gameStarted) {
        // Start Menu
        int menuHeight = 85;
        int menuYPosition = bird.y - menuHeight - 125; // Position the menu 50 pixels above the bird
        if (menuYPosition < 50) menuYPosition = 50; // Ensure the menu doesn't go off the screen top

        g.setColor(new Color(128, 128, 128, 150)); // Semi-transparent gray for menu background
        g.fillRect(SCREEN_WIDTH / 2 - 225, menuYPosition, 450, menuHeight);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("Tap SPACE to jump!", SCREEN_WIDTH / 2 - 165, menuYPosition + 330);
        g.drawString("Welcome to Tweety Bird", SCREEN_WIDTH / 2 - 185, menuYPosition + 45);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("High Score: " + highScore, SCREEN_WIDTH / 2 - 185, menuYPosition + 70);
    } else if (gameOver) {
        // Game Over Menu
        int menuHeight = 210;
        int menuYPosition = SCREEN_HEIGHT / 2 - menuHeight / 2; // Centered vertically

        g.setColor(new Color(128, 128, 128, 150)); // Semi-transparent gray for menu background
        g.fillRect(SCREEN_WIDTH / 2 - 225, menuYPosition, 450, menuHeight);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        g.drawString("Game Over!", SCREEN_WIDTH / 2 - 120, menuYPosition + 50);
        g.drawString("Score: " + score, SCREEN_WIDTH / 2 - 120, menuYPosition + 85);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Press 'R' or SPACE to Restart", SCREEN_WIDTH / 2 - 120, menuYPosition + 145);
        g.drawString("Press 'Q' to QUIT", SCREEN_WIDTH / 2 - 120, menuYPosition + 175);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("High Score: " + highScore, SCREEN_WIDTH / 2 - 120, menuYPosition + 115);
    }

    if (!gameOver && gameStarted) {
        g.drawString(String.valueOf(score), SCREEN_WIDTH / 2 - 20, 100);
        g.drawString("High Score: " + highScore, 250, 50);
    }
}



  
//keyPressed method
@Override
public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        if (!gameStarted && !gameOver) {
            gameStarted = true; // Start the game from the welcome screen
        } else if (gameOver) {
            restartGame();
            gameStarted = true; // Restart the game automatically from game over
        } else {
            makeJump(); // Allows the player to jump during the game
        }
    }
    if (gameOver) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
            gameStarted = true; // Make the game loop like the space bar
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            restartGame(); // Reset to the welcome screen without starting the game
            gameStarted = false; // Make sure the game doesn't start automatically
        }
    }
}



      @Override
    public void keyTyped(KeyEvent e) {}
      @Override
    public void keyReleased(KeyEvent e) {}

    private void restartGame() {
    gameOver = false;
    gameStarted = false;
    score = 0;
    bird = new Rectangle(SCREEN_WIDTH / 2 - 10, SCREEN_HEIGHT / 2 - 10, 40, 40);
    obstaclesList.clear();

    // Reset motion dynamics
    yMotion = 0;

    // Reinitialize clouds if they're affected by game dynamics
    clouds.clear();
    initializeClouds();

    // Reinitialize the skyline if necessary
    skyline.clear();
    initializeSkyline();

    // Add new obstacles to start the game fresh
    addObstacle(true);
    addObstacle(true);
    addObstacle(true);
    addObstacle(true);

    // Reset the high score if needed, or load it from file
    loadHighScore();
}
}