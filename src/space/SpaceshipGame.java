package space;

import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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

    private GraphicsText lifeDisplay, livesDisplay, gameOverText, gameWinText, startGameText, scoreDisplay;
    private Button startButton;
    private Button restartButton;

    private GroupManager groupManager;
 
    private Laser laser;
    private List<Laser> oldLasers;
    private PlayerShip playerShip;
    private Image imageBack;
    private EnemyShip enemyShip;
    private Boss bossShip;
    private EnemyShip selectedEnemyShip;
    private Random rand = new Random();
    private int movementCounter = 0; // Used for timing the enemy laser shots
    private int playerCounter = 0; // Used for timing the player laser shots
   
    private int HP, currentHP, lives, currentLives, currentScore;

    private double initialSpeed = 50;

    private boolean outOfBounds, gameOver, pause = true, wonGame = false;

    private Random random;

    public SpaceshipGame() {
        canvas = new CanvasWindow("Babylon-5", CANVAS_WIDTH, CANVAS_HEIGHT);
        imageBack = new Image(CANVAS_WIDTH,CANVAS_HEIGHT);        

        lives = 2; // Stores the total number of lives the user has each round.
        currentLives = 2; // Stores the number of lives the user has at the moment.
        currentScore = 0;
        HP = 100;
        currentHP = 100;
        //Text that displays current Health
        lifeDisplay = new GraphicsText();
        
        //Text that displays the current score
        scoreDisplay = new GraphicsText();

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
        canvas.add(scoreDisplay);

        startButton.onClick(() -> {
            resetGame();;
        });

        restartButton.onClick(() -> {
            resetStatus();
            resetGame();
        });

        /*
         * Animates the canvas when a pause screen (i.e. Start, Win, or Game Over) is not displayed (when pause = false).
         * Keeps track of the current state of the game using booleans and updates the ball's position when the
         * ball is meant to be moving.
         */

        canvas.animate(()->{
            if(!pause){
                if (currentScore < 200) {
                    enemyShipMovementAndLasers();
                } else {
                    createBossShip(100, 100);
                    bossShipCollision();
                }
                playerShipCollision();
                laserBounds(groupManager.getLaserList());
                laserBounds(groupManager.getEnemyLaserList());
            }
        });
        mouseControl();
        keyControl();
    }
    
    private void bossShipCollision() {

    }

    /**
     * Handles enemy ships' movement and the laser collison with the enemy ships. It also updates the
     * current score every time an enemy ship is removed from the canvas, and it keeps track of how many times
     * the ships have moved and uses that number to time when they fire lasers.
     */
    private void enemyShipMovementAndLasers() {
        for (EnemyShip enemyShip : groupManager.getEnemyShipList()){
            enemyShip.moveEnemyShip(canvas);
            movementCounter ++;
            if (enemyShip.checkLaserCollision(groupManager)) {
                selectedEnemyShip = enemyShip;
            }
            if (movementCounter == 111) {
                movementCounter = 0;
                createLaser(enemyShip.getPosition().getX(), enemyShip.getPosition().getY() + 40, 10, -90, 0);
                updateCurrentHealth();
            }
        } 
        updateCurrentScore();
    }
    
    /**
     * Updates current score by 10 every time an enemy ship is destroyed
     */
    private void updateCurrentScore(){
         if (selectedEnemyShip != null) {
            groupManager.removeEnemyShip(selectedEnemyShip);
            currentScore += 10;
            scoreDisplay.setText("Score: " + currentScore);
            selectedEnemyShip = null;
        }
    }

    private void updateCurrentHealth() {
        int currentHP = playerShip.getPlayerHealth();
        lifeDisplay.setText("Health: " + currentHP);
    }

    /**
    *
    */
    private void playerShipCollision() {
        if (playerShip.checkLaserCollision(groupManager)) {
            groupManager.removePlayerShip(playerShip);
            
            
            // if (movementCounter == 50) {
            //     groupManager.getCanvas().remove(groupManager.getExplosion());
            // }
        }
    }

    /** 
     * Check laser bounds and removes it from the canvas if it goes out of bounds
     */
    private void laserBounds(List<Laser> currentLaserList) {
        oldLasers = new ArrayList<>();
        for (Laser laser : currentLaserList) {
            if (!(laser.getY() < -50 || laser.getY() > CANVAS_HEIGHT + 50)) {
                laser.moveLaser();
            } else {
                oldLasers.add(laser);
            }
        }  
        if (oldLasers != null) {
            for (Laser laser : oldLasers) {
                if (currentLaserList == groupManager.getLaserList()) {
                    groupManager.removePlayerLaser(laser);
                } else {
                    groupManager.removeEnemyLaser(laser);
                }
            }
            oldLasers = null;        
        }
    }
    
    /** 
     * Makes player ship move according to where the mouse moves
     */
    private void mouseControl(){
        canvas.onMouseMove(mousePosition -> {
            if (!pause) {
                if (mousePosition.getPosition().getX() > 0 &&
                mousePosition.getPosition().getX() < CANVAS_WIDTH) {
                    playerShip.setLocation(mousePosition.getPosition());
                }
            }
        });
    }
    /**
     * Makes a laser shoot out of the player's ship when the player presses the space bar. In order to both enable the
     * player to shoot continuously by holding the button and with individual shots with a single button press, every other
     * shot is a double laser shot. 
     */
    private void keyControl(){
        canvas.onKeyUp(key -> {
            if (key.getKey() == Key.SPACE) {
                if (!pause) {
                    createLaser(playerShip.getPosition().getX(), playerShip.getPosition().getY() - 20, 10, 90, 1);
                }
            }
        });
        
        canvas.onKeyDown(key -> {
            if (key.getKey() == Key.SPACE) {
                playerCounter += 1;
                if (!pause && playerCounter == 2) {
                    createLaser(playerShip.getPosition().getX(), playerShip.getPosition().getY() - 20, 10, 90, 1);
                    playerCounter = 0;
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
            canvas.remove(playerShip.getPlayerShipImage());
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
        // currentHP = 100;
    }
    
    /**
     * Keeps track of the score depending on how many enemy ships have been destroyed
     */
    

    /**
     * Creates a laser on the canvas by calling the constructor in the Laser class.
     */
    public void createLaser(double centerX,
                        double centerY,
                        double initialSpeed,
                        double initialAngle,
                        int chooseColor) {
        laser = new Laser(centerX, centerY, initialSpeed, initialAngle, chooseColor);
        if (chooseColor == 1) {
            groupManager.addLaser(laser);
        } else {
            groupManager.addEnemyLaser(laser);
        }
        canvas.add(groupManager.getLaserGroup());
        canvas.add(groupManager.getEnemyLaserGroup());
    }

    /**
     * Creates a paddle on the canvas by calling the constuctor in the Paddle class.
     * The user can control the paddle by moving the mouse cursor horizontally inside the play zone.
     */
    public void createPlayerShip(double upperLeftX, double upperLeftY, double scale) {
        playerShip = new PlayerShip(upperLeftX, upperLeftY, scale);
        canvas.add(playerShip.getPlayerShipImage());
    }

    public void createEnemyShip(double upperLeftX, double upperLeftY, double scale, double angle, double speed){
        for (int i = 0; i < 5; i++){
            for (int j = -400; j < 0; j += 100) {
            enemyShip = new EnemyShip(i * 30, j, scale);
            groupManager.addEnemyShip(enemyShip);
            }
        }
        canvas.add(groupManager.getEnemyShipGroup());
    }

    public void createBossShip(double centerX, double centerY) {
        bossShip = new Boss(centerX, centerY, 2);
        groupManager.addBossShip(bossShip);
        
    }

    /**
     * Resets the canvas by removing everything on the canvas and all the bricks that had been created.
     * Then creates the game components for a new round and allows the game to run.
     */
    private void resetGame() {
        groupManager.removeAll();
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
            livesDisplay.setFont(FontStyle.BOLD, 20);
            livesDisplay.setPosition(15,30);
            canvas.add(livesDisplay);
            imageBack.setImagePath("ship-icons/spaceBackground.png"); 
            imageBack.setCenter(0,0);
            imageBack.setScale(5);
            canvas.add(imageBack);
            scoreDisplay.setFont(FontStyle.BOLD,30);
            scoreDisplay.setFillColor(Color.WHITE);
            scoreDisplay.setPosition(15,50);
            lifeDisplay.setFont(FontStyle.BOLD,30);
            lifeDisplay.setFillColor(Color.WHITE);
            lifeDisplay.setPosition(200,50);
        }
        livesDisplay.setText("Lives: " + currentLives);
        scoreDisplay.setText("Score: " + currentScore);
        canvas.add(scoreDisplay);
        canvas.add(lifeDisplay);
        createPlayerShip(220, 600, 0.2);
        createEnemyShip(220, 100, 0.17, 50, 70);
        //createBossShip(100, 100);
        canvas.draw();
        canvas.pause(100);
    }

    /**
     * Convenience to return a random floating point number, min ≤ n < max.
     */
    public double randomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    @Override
    public String toString() {
        return "Creates a Breakout Game. The Breakout Game initializes the Ball, Border, Brick, BrickManager, and Paddle " +
        "classes as objects for use in playing breakout. The user has " + lives + " lives, each representing a round " + 
        "during which the user can try to break the bricks by bouncing the ball with the paddle.";
    }  
}

