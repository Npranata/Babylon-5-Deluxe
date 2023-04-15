package space;

import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;
import java.util.Random;

/**
 * A game of Breakout.
 * 
 * Use the mouse to move the paddle to the left and right. The ball will move to the right if it hits right of the middle
 * of the paddle and left if it hits left of the middle of the paddle.
 * If you manage to break all of the bricks along the top, then you win! 
 * If the ball leaves the play zone three tiimes, you lose.
 */
public class SpaceshipGame {
    private static final int CANVAS_WIDTH = 620, CANVAS_HEIGHT = 800;

    private CanvasWindow canvas;

    private GraphicsText lifeDisplay, livesDisplay, gameOverText, gameWinText, startGameText;
    private Button startButton;
    private Button restartButton;

    private GroupManager groupManager;
 
    private Laser laser;
    private Laser oldLaser;
    private PlayerShip playerShip;
    private Image imageBack;
    private EnemyShip enemyShip;
    private EnemyShip selectedEnemyShip;
    private Random rand = new Random();


    private Border rightBorder, leftBorder, topBorder;

    private int HP, currentHP, lives, currentLives;

    private double initialSpeed = 50;

    private boolean outOfBounds, gameOver, pause = true, wonGame = false;

    private Random random;

    public SpaceshipGame() {
        canvas = new CanvasWindow("Breakout!", CANVAS_WIDTH, CANVAS_HEIGHT);
        imageBack = new Image(CANVAS_WIDTH,CANVAS_HEIGHT);
        

        lives = 2; // Stores the total number of lives the user has each round.
        currentLives = 2; // Stores the number of lives the user has at the moment.

        HP = 100;
        currentHP = 100;

        // Text that displays the number of lives the user currently has.
        livesDisplay = new GraphicsText();
        lifeDisplay = new GraphicsText();

        // Game loss announcement text that appears after the user loses the game.
        gameOverText = new GraphicsText();
        gameOverText.setFont(FontStyle.BOLD, 50); 
        gameOverText.setPosition(CANVAS_WIDTH/2 - 150,CANVAS_HEIGHT/2 - 50);
        gameOverText.setText("GAME OVER");

        // Game win announcement text that appears after the user wins the game.
        gameWinText = new GraphicsText();
        gameWinText.setFont(FontStyle.BOLD, 50);
        gameWinText.setPosition(CANVAS_WIDTH/2 - 120,CANVAS_HEIGHT/2 - 50);
        gameWinText.setText("YOU WIN!");

        // Text that appears on the canvas before the game starts.
        startGameText = new GraphicsText();
        startGameText.setFont(FontStyle.BOLD, 50);
        startGameText.setPosition(CANVAS_WIDTH/2 - 150,CANVAS_HEIGHT/2 - 50);
        startGameText.setText("BABYLON 5");

        groupManager = new GroupManager(canvas);

        // Button that the user presses to start the game.
        startButton = new Button("START");
        startButton.setPosition(CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2);
        
        // Button that the user presses to restart the game after a win or loss.
        restartButton = new Button("RESTART");
        restartButton.setPosition(CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2);

        canvas.add(startGameText);
        canvas.add(startButton);

        startButton.onClick(() -> {
            resetGame();;
        });

        restartButton.onClick(() -> {
            resetStatus();
            resetGame();
        });

        // A change in time used to update the ball's position.
        double dt = 0.15;

        /*
         * Animates the canvas when a pause screen (i.e. Start, Win, or Game Over) is not displayed (when pause = false).
         * Keeps track of the current state of the game using booleans and updates the ball's position when the
         * ball is meant to be moving.
         */

        canvas.animate(()->{
            if(!pause){
                for (EnemyShip enemyShip : groupManager.getEnemyShipList()){
                    enemyShip.moveEnemyShip(canvas);
                    if (enemyShip.checkLaserCollision(groupManager)) {
                        selectedEnemyShip = enemyShip;
                    }
                }  
                if (selectedEnemyShip != null) {
                    groupManager.removeEnemyShip(selectedEnemyShip);
                    System.out.println(selectedEnemyShip);
                    selectedEnemyShip = null;
                }
                for (Laser laser : groupManager.getLaserList()) {
                    if (!(laser.getY() < -50 || laser.getY() > CANVAS_HEIGHT + 50)) {
                        laser.moveLaser();
                    } else {
                        oldLaser = laser;
                    }
                }
                if (oldLaser != null) {
                    groupManager.removeLaser(oldLaser);
                    oldLaser = null;
                }
            }
        });

        /*
         * Allows the user to control the paddle's position using the mouse cursor.
         */
        canvas.onMouseMove(mousePosition -> {
            if (!pause) {
                if (mousePosition.getPosition().getX() > leftBorder.getBorderWidth() + leftBorder.getBorderX() &&
                mousePosition.getPosition().getX() < rightBorder.getBorderX()) {
                    playerShip.setLocation(mousePosition.getPosition());
                }
            }
        });

        canvas.onKeyDown(key -> {
            if (key.getKey() == Key.SPACE) {
                if (!pause) {
                    createLaser(playerShip.getPosition().getX(), playerShip.getPosition().getY() - 20, 10, 90);
                }
            }         
        });
    }

    public static void main(String[] args){
        SpaceshipGame game = new SpaceshipGame();
    }

    /**
     * Triggered when the ball leaves the play zone.
     * If the player still has remaining lives, then it recreates the ball and allows the user to continue. 
     * Otherwise, the game is over and a "Game Over" screen is displayed.
     */
    private void continueGame() {
        if (currentLives > 1) {
            canvas.remove(playerShip.getPlayerIcon());
            currentLives --;;
            outOfBounds = false;
            createGame();
            return;
        } else {
            gameOver = true;
            outOfBounds = false;
        }   
    }

    /**
     * Triggered when the user has lost all of their lives or broken all the bricks.
     * Pauses the game to prevent another round from starting and displays the appropriate message.
     */
    private void endGame() {
        pause = true;
        canvas.removeAll();
        if (gameOver) {
            canvas.add(gameOverText);
        } else {
            canvas.add(gameWinText);
        }
        canvas.add(restartButton);
    }

    /**
     * Resets factors that are used to determine the state of the current game.
     */
    private void resetStatus() {
        gameOver = false;
        wonGame = false;
        currentLives = 2;
        currentHP = 100;
    }

    /**
     * Creates a laser on the canvas by calling the constructor in the Laser class.
     */
    public void createLaser(double centerX,
                        double centerY,
                        double initialSpeed,
                        double initialAngle) {
        laser = new Laser(centerX, centerY, initialSpeed, initialAngle);
        groupManager.addLaser(laser);
        canvas.add(groupManager.getLaserGroup());
    }

    /**
     * Creates a paddle on the canvas by calling the constuctor in the Paddle class.
     * The user can control the paddle by moving the mouse cursor horizontally inside the play zone.
     */
    public void createPlayerShip(double upperLeftX, double upperLeftY, double scale) {
        playerShip = new PlayerShip(upperLeftX, upperLeftY, scale);
        canvas.add(playerShip.getPlayerIcon());
    }

    public void createEnemyShip(double upperLeftX, double upperLeftY, double scale, double angle, double speed){
        for (int i =0; i < 1; i++){
            enemyShip = new EnemyShip(i * 30, i * 100, scale);
            groupManager.addEnemyShip(enemyShip);
        }
        canvas.add(groupManager.getEnemyShipGroup());
    }

   

    /**
     * Resets the canvas by removing everything on the canvas and all the bricks that had been created.
     * Then creates the game components for a new round and allows the game to run.
     */
    private void resetGame() {
        // groupManager.removeAllBricks();
        canvas.removeAll();
        createGame();
        pause = false;
    }

    /**
     * Initializes the elements of the play zone, varying its creation based on whether a new game is starting or
     * if the user is continuing a game.
     * After the components are prepared, the game is paused for a moment to ready the player before it starts.
     */
    private void createGame() {
        
        if (currentLives == 2) {
            createBounds();
            livesDisplay.setFont(FontStyle.BOLD, 20);
            livesDisplay.setPosition(15,30);
            canvas.add(livesDisplay);
            imageBack.setImagePath("ship-icons/spaceBackground.png"); 
            imageBack.setCenter(0,0);
            imageBack.setScale(5);
            // canvas.add(imageBack);
        }
        createPlayerShip(220,600, 0.2);
        createEnemyShip(220, 100, 0.170,50,70);
        livesDisplay.setText("Lives: " + currentLives);
        canvas.draw();
        canvas.pause(100);
    }

    /**
     * Creates three border objects along the left, top, and right sides of the play zone to keep the ball
     * from leaving the canvas.
     */
    private void createBounds() {
        rightBorder = new Border(CANVAS_WIDTH-8.5, 4, 6.5, CANVAS_HEIGHT);
        rightBorder.addToCanvas(canvas);
        leftBorder = new Border(2, 4, 6, CANVAS_HEIGHT);
        leftBorder.addToCanvas(canvas);
        topBorder = new Border(6, 4, CANVAS_WIDTH-10, 8);
        topBorder.addToCanvas(canvas);
    }

    /**
     * Convenience to return a random floating point number, min ≤ n < max.
     */
    public double randomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    /**
     * Uses the randomDouble() method to create an angle for the ball to move in at the start of each round.
     * 
     * @return A double at an acceptable angle such that the ball moves down toward the paddle at a more horizontal
     * than vertical angle.
     */
    private double getBallAngle() {
        random = new Random();
        if (randomDouble(0, 1) > 0.5) {
            return randomDouble(-115, -110);
        } else {
            return randomDouble(-70, -60);
        }
    }

    @Override
    public String toString() {
        return "Creates a Breakout Game. The Breakout Game initializes the Ball, Border, Brick, BrickManager, and Paddle " +
        "classes as objects for use in playing breakout. The user has " + lives + " lives, each representing a round " + 
        "during which the user can try to break the bricks by bouncing the ball with the paddle.";
    }  
}

