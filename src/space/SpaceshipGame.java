package space;

import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * Babylon-5
 * 
 * Use the mouse to move the ship to anywhere in the game window. Enemy ships will come down from the top and shoot at your
 * ship, so try to avoid their lasers! If you manage to destroy all of the enemy ships, then you must defeat the boss
 * ship in order to win. Make sure to target its center and avoid the lasers.
 * If your ship is destroyed two times, you lose.
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
    private int movementCounter = 0; // Used for timing the enemy laser shots
    private int bossLaserCounter = 0; // Used for timing the boss laser shots
    private int explosionCounter = 0; // Used for timing when the explosion is removed
   
    private int currentHP, lives, currentLives, currentScore;

    private boolean gameOver, pause = true, bossTime = false, explosionExists = false;
   /**
    * Constructs the game's background, initial score, intial lives, music, and animations.
    */
    public SpaceshipGame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        canvas = new CanvasWindow("Babylon-5", CANVAS_WIDTH, CANVAS_HEIGHT);
        imageBack = new Image(CANVAS_WIDTH, CANVAS_HEIGHT);   
        menuBack = new Image(CANVAS_WIDTH, CANVAS_HEIGHT);

        lives = 2; // Stores the total number of lives the user has each round.
        currentLives = 2; // Stores the number of lives the user has at the moment.
        currentScore = 0;

        //Uses a wav file for the game's music and make sure it is looped so the music goes on forever
        File file = new File("res/ship-icons/Without_Fear.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();     
        clip.loop(-1);

        //Text that displays current Health
        lifeDisplay.setFont(FontStyle.BOLD, 30);
        lifeDisplay.setFillColor(Color.WHITE);
        lifeDisplay = new GraphicsText();
        
        //Text that displays the current score
        scoreDisplay.setFont(FontStyle.BOLD,30);
        scoreDisplay.setFillColor(Color.WHITE);
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
        continueButton.setPosition(CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2 - 15);
       
        // Button that the user presses to return back to the main menu 
        mainMenuButton = new Button("Return Back to Menu");
        mainMenuButton.setPosition(CANVAS_WIDTH/2 - 80, CANVAS_HEIGHT/2 + 20);

        canvas.add(scoreDisplay);

        createMainMenu();

        startButton.onClick(() -> {
            resetGame();
        });
  
        continueButton.onClick(() -> {
            resetStatus();
            resetGame();            
        });

        mainMenuButton.onClick(()->{
            currentScore = 0;
            resetStatus();
            createMainMenu();
        });

        mouseControl();
        keyControl();

        /*
         * Animates the canvas when a pause screen (i.e. Start, Win, or Game Over) is not displayed (when pause = false).
         * Keeps track of the current state of the game using booleans and runs methods that handle ship movement
         * and laser firing. 
         */
        canvas.animate(() -> {
            if(!pause){
                explosionCounter ++;
                if (!bossTime) {
                    if (groupManager.enemiesExist()) {
                        enemyShipMovementAndLasers();
                    } else { // When all the enemy ships are destroyed, creates the boss ship
                        createBossShip(CANVAS_WIDTH/2, -500);
                    }
                }
                if (bossTime && !gameOver) {
                    if (bossShip.getBossY() < 30) {
                        bossShip.moveBoss();
                    }
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
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        SpaceshipGame game = new SpaceshipGame();
    }

   /**
    * Creates the game's main menu. This method is also used when the user presses
    * the return to main menu button
    */
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
    
   /**
    * Controls the frequency of the laser that the boss ship shoots using a laser counter, 
    * handles the laser collision with the boss ship, updates the boss ship's current health, 
    * updates the boss ship's current health, and updates the current score if the boss ship is destroyed.
    */
    private void bossShipCollision() {
        bossLaserCounter ++;
        if (bossShip.checkLaserCollision(groupManager)) {
            selectedBossShip = bossShip;
        }
        if (bossLaserCounter == 30) {
            createSideLasers();
        }
        if (bossLaserCounter == 40) {
            createMiddleLasers();
        }
        if (bossLaserCounter == 50) {
            createLowerLasers();
            bossLaserCounter = 0;
        }
        updateCurrentHealth();
        updateCurrentScore();
    }

   /**
    * Creates the lasers that the boss shoots at a 45 degree angle.
    */
    public void createSideLasers() {
        createLaser(bossShip.getBossX() - 160, bossShip.getBossY() + 250, 10, -45, 0);
        createLaser(bossShip.getBossX() + 150, bossShip.getBossY() + 250, 10, -135, 0);
    }
   /**
    * Creates the lasers the boss shoots directly downward from the middle of the ship.
    */
    public void createMiddleLasers() {
        createLaser(bossShip.getBossX() - 30, bossShip.getBossY() + 10, 12, -90, 0);
        createLaser(bossShip.getBossX() + 30, bossShip.getBossY() + 10, 12, -90, 0);
    }

   /**
    * Creates the lasers that the boss shoots directly downward from the lower side of 
    * boss ship.
    */
    public void createLowerLasers() {
        createLaser(bossShip.getBossX() - 130, bossShip.getBossY() + 250, 10, -90, 0);
        createLaser(bossShip.getBossX() + 130, bossShip.getBossY() + 250, 10, -90, 0);
    }

    /**
     * Handles enemy ships' movement and the laser collison with the enemy ships. It also updates the
     * current score every time an enemy ship is removed from the canvas, and it keeps track of how many times
     * the ships have moved and uses that number to time when they fire lasers.
     */
    private void enemyShipMovementAndLasers() {
        for (EnemyShip enemyShip : groupManager.getEnemyShipList()){
            enemyShip.moveEnemyShip(CANVAS_WIDTH, CANVAS_HEIGHT);
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

   /**
    * Updates the player's current health in the game.
    */
    private void updateCurrentHealth() {
        currentHP = playerShip.getPlayerHealth();
        lifeDisplay.setText("Health: " + currentHP);
    }

    /**
    * Calls the laser collision method in the PlayerShip class. This determines whether a laser has hit the player
    * or if the player ship has been destroyed.
    */
    private void playerShipCollision() {
        if (playerShip.checkLaserCollision(groupManager)) {
            explosionCounter = 0;
            explosionExists = true;
            continueGame();
        }
    }

    /**
     * Checks laser bounds and removes lasers from the canvas if they go out of bounds.
     * @param currentLaserList The list of lasers that needs to be changed.
     */
    private void laserBounds(List<Laser> currentLaserList) {
        oldLasers = new ArrayList<>();
        for (Laser laser : currentLaserList) {
            if (!(laser.getLaserY() < -50 || laser.getLaserY() > CANVAS_HEIGHT + 50)) {
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
     * Makes player ship move according to where the mouse's position.
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
     * Makes a laser shoot out of the player's ship when the player presses the space bar. The player can shoot a
     * continuous stream of lasers by holding down the space button.
     */
    private void keyControl() {
        canvas.onKeyDown(key -> {
            if (key.getKey() == Key.SPACE) {
                createLaser(playerShip.getPosition().getX(), playerShip.getPosition().getY() - 20, 10, 90, 1);
            }         
        });
    }

    /**
     * Triggered when the ball leaves the play zone.
     * If the player still has remaining lives, then it recreates the ball and allows the user to continue. 
     * Otherwise, the game is over and a "Game Over" screen is displayed.
     */
    private void continueGame() {
        canvas.remove(playerShip.getPlayerShipImage());
        currentLives --;
        livesDisplay.setText("LIVES: " + currentLives);

        if (currentLives > 0) {
            currentHP = 100;
            playerShip.setPlayerHealth(currentHP);
            createGame();
            return;
        } else {
            gameOver = true;
            endGame();
        }   
    }

    /**
     * Triggered when the user has lost all of their lives or beaten the boss.
     * Pauses the game to prevent another round from starting and displays the appropriate message.
     */
    private void endGame() {
        updateCurrentHealth();
        updateCurrentScore();
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
        bossTime = false;
        explosionExists = false;
        if (currentScore == 0) { // Ensures that the number of lives is not reset when the player presses "CONTINUE"
            currentLives = 2;
        }
    }

    /**
     * Creates a laser on the canvas by calling the constructor in the Laser class. These lasers can be shot by
     * the player ship, enemy ships, and the boss ship.
     * 
     * @param centerX The center x-value of the player ship image on the canvas.
     * @param centerY The center y-value of the player ship image on the canvas.
     * @param initialSpeed The speed a laser is moving at when it is created.
     * @param initialAngle The angle a laser is moving at.
     * @param chooseColor An integer that determines the color and properties of a laser.
     */
    public void createLaser(double centerX, double centerY, double initialSpeed, double initialAngle, int chooseColor) {
        laser = new Laser(centerX, centerY, initialSpeed, initialAngle, chooseColor);
        if (chooseColor == 1) {
            groupManager.addLaser(laser);
        } else {
            groupManager.addEnemyLaser(laser);
        }
    }

    /**
     * Creates a player ship on the canvas by calling the constuctor in the PlayerShip class.
     * The user can control the player ship by moving the mouse cursor inside the canvas.
     * 
     * @param centerX The center x-value of the player ship image on the canvas.
     * @param centerY The center y-value of the player ship image on the canvas.
     * @param scale The size of the player ship image on the canvas.
     */
    public void createPlayerShip(double centerX, double centerY, double scale) {
        playerShip = new PlayerShip(centerX, centerY, scale);
        playerShip.setPlayerHealth(currentHP);
        canvas.add(playerShip.getPlayerShipImage());
    }

    /**
     * Creates five rows of enemy ships that are spread across the top of the game window. Each enemy ship is 
     * added to a graphics group and a list, and that graphics group is added to the canvas once all of the 
     * enemy ships have been added.
     * 
     * @param scale The size of the enemy ship image on the canvas.
     * @param angle The angle an enemy ship is moving at.
     * @param speed The speed an enemy ship is moving at when it is created.
     */
    public void createEnemyShip(double scale, double angle, double speed){
        for (int i = 0; i < 5; i++) {
            for (int j = -400; j < 0; j += 100) {
                enemyShip = new EnemyShip(i * 15, j, scale);
                groupManager.addEnemyShip(enemyShip);
            }
        }
        canvas.add(groupManager.getEnemyShipGroup());
    }

    /**
     * Initializes a boss ship object and add's its image to the canvas.
     * 
     * @param centerX The center x-value of the boss ship on the canvas.
     * @param centerY The center y-value of the boss ship on the canvas.
     */
    public void createBossShip(double centerX, double centerY) {
        bossTime = true;
        bossShip = new Boss(centerX, centerY, 2);
        canvas.remove(groupManager.getLaserGroup()); // Ensures that player lasers go over the boss image
        canvas.add(bossShip.getBossIcon());
        canvas.add(groupManager.getLaserGroup());
    }

    /**
     * Whenever the explosion is added to the canvas, a boolean is updated and
     * a counter keeps track of how long it has been on the canvas. Once the counter reaches a certain point, it runs this
     * method, which removes the explosion from the canvas.
     */
    public void explosionCheck() {
        if (explosionExists) {
            groupManager.getCanvas().remove(groupManager.getExplosion());
            explosionExists = false;
        }
    }

    /**
     * Resets the game by removing everything on the canvas and all the lasers and enemy ships that had been created.
     * Creates the game components for a new round and allows the game to run.
     */
    private void resetGame() {
        groupManager.removeAll();
        canvas.removeAll();
        startGame();
        createGame();
        pause = false;
        if (currentScore == 0) {
            playerShip.setPlayerHealth(100);
        } else {
            playerShip.setPlayerHealth(currentHP);
        }
    }

    /**
     * Updates the elements of the play zone and prepares the game for user interaction. The canvas' elements are removed
     * before this method runs, so it adds the HUD data as well.
     */
    private void createGame() {
        createPlayerShip(CANVAS_WIDTH/2, CANVAS_HEIGHT/2, 0.2);
        livesDisplay.setText("Lives: " + currentLives);
        scoreDisplay.setText("Score: " + currentScore);
        canvas.add(scoreDisplay);
        canvas.add(lifeDisplay);
        canvas.add(livesDisplay);
    }

    /**
     * Adds the background image to the game window, and then subsequently the score and health indicators
     * to the HUD. Babylon-5 uses this method to create the first stage of the game before the boss, so the method also,
     * calls the constructor for the enemy ships.
     */
    private void startGame() {
        imageBack.setImagePath("ship-icons/spaceBackground.png"); 
        imageBack.setCenter(0,0);
        imageBack.setScale(2);
        canvas.add(imageBack);

        scoreDisplay.setPosition(15,CANVAS_HEIGHT - 20);
        lifeDisplay.setPosition(200,CANVAS_HEIGHT - 20);
        
        createEnemyShip(0.17, 50, 70);
        canvas.add(groupManager.getLaserGroup());
        canvas.add(groupManager.getEnemyLaserGroup());
    }

    @Override
    public String toString() {
        return "Creates a Babylon-5 game. The game initializes the player ship, enemy ship, boss, and lasers " +
        "classes as objects for use in playing Babylon-5. The user has " + lives + " lives, each representing the number of tries the player has to win " + 
        "during which the user can try to defeat all enemy ships and the boss.";
    }  
}