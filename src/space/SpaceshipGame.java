package space;

import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

/**
 * A game of [REDACTED].
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
    private Button continueButton;
    private Button mainMenuButton;


    private GroupManager groupManager;
 
    private Laser laser;
    private List<Laser> oldLasers;
    private PlayerShip playerShip;
    private Image imageBack;
    private Image menuBack;
    private EnemyShip enemyShip, selectedEnemyShip;
    private Boss bossShip, selectedBossShip;
    private Random rand = new Random();
    private int movementCounter = 0; // Used for timing the enemy laser shots
    private int bossLaserCounter = 0; // Used for timing the boss laser shots
    private int playerCounter = 0; // Used for timing the player laser shots
    private int explosionCounter = 0;
   
    private int currentHP, lives, currentLives, currentScore;

    private double initialSpeed = 50;

    private boolean outOfBounds, gameOver, pause = true, wonGame = false, bossTime = false, explosionExists = false;

    private Random random;

    public SpaceshipGame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        canvas = new CanvasWindow("Babylon-5", CANVAS_WIDTH, CANVAS_HEIGHT);
        imageBack = new Image(CANVAS_WIDTH, CANVAS_HEIGHT);   
        menuBack = new Image(CANVAS_WIDTH, CANVAS_HEIGHT);

        lives = 2; // Stores the total number of lives the user has each round.
        currentLives = 2; // Stores the number of lives the user has at the moment.
        currentScore = 0;

        File file = new File("res/ship-icons/Without_Fear.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();     
        clip.loop(-1);

        //Text that displays current Health
        lifeDisplay = new GraphicsText();
        
        //Text that displays the current score
        scoreDisplay = new GraphicsText();

        // Text that displays the number of lives the user currently has.
        livesDisplay = new GraphicsText();
        livesDisplay.setFont(FontStyle.BOLD,30);
        livesDisplay.setFillColor(Color.WHITE);
        livesDisplay.setPosition(430, CANVAS_HEIGHT - 20);
        livesDisplay.setText("LIVES: " + currentLives);

        // Game loss announcement text that appears after the user loses the game.
        gameOverText = new GraphicsText();
        gameOverText.setFont(FontStyle.BOLD, 50); 
        gameOverText.setFillColor(Color.white);
        gameOverText.setPosition(CANVAS_WIDTH/2 - 150,CANVAS_HEIGHT/2 - 50);
        gameOverText.setText("GAME OVER");

        // Game win announcement text that appears after the user wins the game.
        gameWinText = new GraphicsText();
        gameWinText.setFont(FontStyle.BOLD, 50);
        gameWinText.setFillColor(Color.white);
        gameWinText.setPosition(CANVAS_WIDTH/2 - 120,CANVAS_HEIGHT/2 - 50);
        gameWinText.setText("YOU WIN!");

        // Text that appears on the canvas before the game starts.
        startGameText = new GraphicsText();
        startGameText.setFont(FontStyle.BOLD_ITALIC, 50);
        startGameText.setFillColor(Color.white);
        startGameText.setPosition(CANVAS_WIDTH/2 - 150, 150);
        startGameText.setText("BABYLON-5");

        groupManager = new GroupManager(canvas);

        // Button that the user presses to start the game.
        startButton = new Button("START");
        startButton.setScale(50);
        startButton.setPosition(CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2);
        
        // Button that the user presses to restart the game after a win or loss.
        continueButton = new Button("CONTINUE");
        continueButton.setPosition(CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2);
       

        mainMenuButton = new Button("Return Back to Menu");
        mainMenuButton.setPosition(CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2 + 50);

        canvas.add(scoreDisplay);
        createMainMenu();

        startButton.onClick(() -> {
            resetGame();
            // bossShip.setBossHealth(1000);
            // enemyShip.setEnemyHealth(50);
        });
  
        continueButton.onClick(() -> {
            resetStatus();
            resetGame();            
        });

        mainMenuButton.onClick(()->{
            resetStatus();
            currentScore = 0;
            createMainMenu();
        });

        /*
         * Animates the canvas when a pause screen (i.e. Start, Win, or Game Over) is not displayed (when pause = false).
         * Keeps track of the current state of the game using booleans and updates the ball's position when the
         * ball is meant to be moving.
         */

        canvas.animate(() -> {
            if(!pause){
                explosionCounter ++;
                if (!bossTime) {
                    if (groupManager.enemiesExist()) {
                        enemyShipMovementAndLasers();
                    } else {
                        bossTime = true;
                        createBossShip(CANVAS_WIDTH/2, -500);
                    }
                }
                if (bossTime && !gameOver) {
                    bossShip.moveBoss();
                    bossShipCollision();
                }
                playerShipCollision();
                laserBounds(groupManager.getLaserList());
                laserBounds(groupManager.getEnemyLaserList());
                if (explosionCounter == 35) {
                    explosionCheck();
                    explosionCounter = 0;
                }
            }
        });
        mouseControl();
        keyControl();
    }

    private void createMainMenu() {
        canvas.removeAll();
        groupManager.removeAll();
        menuBack.setImagePath("ship-icons/MenuBackground.jpg"); 
        menuBack.setCenter(300,400);
        menuBack.setScale(.69);
        canvas.add(menuBack);
        canvas.add(startGameText);
        canvas.add(startButton);
     }
    
    private void bossShipCollision() {
        bossLaserCounter ++;
        if (bossShip.checkLaserCollision(groupManager)) {
            selectedBossShip = bossShip;
        }
        if (bossLaserCounter == 40) {
            createMiddleLasers();
            bossLaserCounter = 0;
        }
        if (bossLaserCounter == 30) {
            createSideLasers();
        }
        updateCurrentHealth();
        updateCurrentScore();
    }

    public void createSideLasers() {
        createLaser(bossShip.getBossX() - 160, bossShip.getBossY() + 250, 10, -45, 0);
        createLaser(bossShip.getBossX() + 150, bossShip.getBossY() + 250, 10, -135, 0);
    }

    public void createMiddleLasers() {
        createLaser(bossShip.getBossX() - 30, bossShip.getBossY() + 200, 10, -90, 0);
        createLaser(bossShip.getBossX() + 30, bossShip.getBossY() + 200, 10, -90, 0);
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
                explosionCounter = 0;
            }
            if (movementCounter == 111) {
                movementCounter = 0;
                createLaser(enemyShip.getEnemyX(), enemyShip.getEnemyY() + 40, 10, -90, 0);

            }
        } 
        updateCurrentHealth();
        updateCurrentScore();
    }
    
    /**
     * Updates current score every time an enemy ship is destroyed, differentiating between destroying
     * normal enemy ships and boss ships.
     */
    private void updateCurrentScore(){
        if (selectedEnemyShip != null) {
            groupManager.removeEnemyShip(selectedEnemyShip);
            currentScore += 10;
            selectedEnemyShip = null;
            explosionExists = true;
        } else if (selectedBossShip != null) {
            canvas.remove(bossShip.getBossIcon());
            currentScore += 150;
            selectedBossShip = null;
            gameOver = false;
            endGame();
        }
        scoreDisplay.setText("Score: " + currentScore);
    }

    private void updateCurrentHealth() {
        currentHP = playerShip.getPlayerHealth();
        lifeDisplay.setText("Health: " + currentHP);
    }

    /**
    *
    */
    private void playerShipCollision() {
        if (playerShip.checkLaserCollision(groupManager)) {
            explosionCounter = 0;
            explosionExists = true;
            continueGame();
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

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        SpaceshipGame game = new SpaceshipGame();
    }

    /**
     * Triggered when the ball leaves the play zone.
     * If the player still has remaining lives, then it recreates the ball and allows the user to continue. 
     * Otherwise, the game is over and a "Game Over" screen is displayed.
     */
    private void continueGame() {
        canvas.remove(playerShip.getPlayerShipImage());
        currentLives --;;
        livesDisplay.setText("LIVES: " + currentLives);

        if (currentLives > 0) {
            createGame();
            return;
        } else {
            gameOver = true;
            endGame();
        }   
    }

    /**
     * Triggered when the user has lost all of their lives or broken all the bricks.
     * Pauses the game to prevent another round from starting and displays the appropriate message.
     */
    private void endGame() {
        pause = true;
        if (gameOver) {
            canvas.add(gameOverText);
        } else {
            canvas.add(gameWinText);
            canvas.add(continueButton);
        }
        canvas.add(mainMenuButton);
    }

    /**
     * Resets factors that are used to determine the state of the current game.
     */
    private void resetStatus() {
        gameOver = false;
        wonGame = false;
        bossTime = false;
        explosionExists = false;
        playerCounter = 0;
        currentLives = 2;
    }

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
        for (int i = 0; i < 5; i++){ //was i <5
            for (int j = -400; j < 0; j += 100) { //was j<0
                enemyShip = new EnemyShip(i * 15, j, scale);
                groupManager.addEnemyShip(enemyShip);
            }
        }
        canvas.add(groupManager.getEnemyShipGroup());
    }

    public void createBossShip(double centerX, double centerY) {
        bossShip = new Boss(centerX, centerY, 2);
        canvas.add(bossShip.getBossIcon());
    }

    public void explosionCheck() {
        if (explosionExists) {
            groupManager.getCanvas().remove(groupManager.getExplosion());
            explosionExists = false;
        }
    }

    /**
     * Resets the canvas by removing everything on the canvas and all the bricks that had been created.
     * Then creates the game components for a new round and allows the game to run.
     */
    private void resetGame() {
        groupManager.removeAll();
        canvas.removeAll();
        createGame();
        startGame();
        pause = false;
        if (currentScore == 0) {
            playerShip.setPlayerHealth(100);
        } else {
            playerShip.setPlayerHealth(currentHP);
        }
    }

    /**
     * Initializes the elements of the play zone, varying its creation based on whether a new game is starting or
     * if the user is continuing a game.
     * After the components are prepared, the game is paused for a moment to ready the player before it starts.
     */
    private void createGame() {
        imageBack.setImagePath("ship-icons/spaceBackground.png"); 
        imageBack.setCenter(0,0);
        imageBack.setScale(2.5); //was 5
        canvas.add(imageBack);
        createPlayerShip(CANVAS_WIDTH/2, CANVAS_HEIGHT/2, 0.2);
        livesDisplay.setText("Lives: " + currentLives);
        scoreDisplay.setText("Score: " + currentScore);
        canvas.add(scoreDisplay);
        canvas.add(lifeDisplay);
        canvas.add(livesDisplay);
        canvas.pause(100);
    }

    private void startGame() {
        scoreDisplay.setFont(FontStyle.BOLD,30);
        scoreDisplay.setFillColor(Color.WHITE);
        scoreDisplay.setPosition(15,CANVAS_HEIGHT - 20);
        lifeDisplay.setFont(FontStyle.BOLD,30);
        lifeDisplay.setFillColor(Color.WHITE);
        lifeDisplay.setPosition(200,CANVAS_HEIGHT - 20);
        createEnemyShip(220, 100, 0.17, 50, 70);
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