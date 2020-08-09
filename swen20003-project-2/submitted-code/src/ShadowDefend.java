import bagel.AbstractGame;
import bagel.Input;

import java.io.IOException;
/**
 * Shadow Defend is a tower defence game where the player must attempt to defend the map from
 * enemies using towers. As such, there are two main components of the game: slicers (the enemy),
 * and towers.
 * Towers are manually purchased by the player and can be placed on a non-blocked area of the map.
 * There are different types of towers, each with different characteristics, but the overall goal of a
 * tower is to prevent slicers from exiting the map.
 * Slicers are the main antagonists of the game. They spawn at varying points during a wave. Like
 * towers, there are different types of slicers, each with different characteristics, but the overall goal
 * of a slicer is to reach the ‘exit’ of the map.
 * When the game begins, the first level is loaded and rendered, the buy panel and status panels are
 * rendered, and $500 starting cash is awarded to the player as shown in Figure 1. The player can
 * set up their first defensive tower (if they want) and then when they’re ready, they can press ‘S’ to
 * initiate the first wave of enemies.
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class ShadowDefend extends AbstractGame {
    private static final int NUMBER_OF_LEVELS = 2;
    private Level level;
    private int currentLevelNum;
    private boolean isFinished;

    /**
     * The main function call for the game, create's a new instance of ShadowDefend
     * and runs it
     * @param args: The Command line arguments passed into the function when ran
     * @throws IOException: Exception thrown if there is an error in game Input/Output
     */
    public static void main(String[] args) throws IOException{
        new ShadowDefend().run();
    }

    /**
     * Creates a new instance of ShadowDefend
     */
    public ShadowDefend(){
        // Start the game at level 1
        this.currentLevelNum = 1;
        this.isFinished = false;
        // Load the first level
        loadNewLevel();
    }

    /**
     * Updates the game state
     * @param input: Input from the player's keyboard/mouse
     */
    protected void update(Input input){
        level.update(input);
        // if the level is finished, load up the next level if there is one or display
        // 'winner' otherwise
        if (level.isFinished()){
            if (currentLevelNum < NUMBER_OF_LEVELS) {
                currentLevelNum++;
                loadNewLevel();
            }
            else {
                isFinished = true;
            }
        }
    }
    // Loads a new Level from the currentLevelNum variable
    private void loadNewLevel(){
        try {
            this.level = new Level(currentLevelNum, this);
        }catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Returns the current Level
     * @return: The current level
     */
    public Level getLevel() {
        return level;
    }


    /**
     * Returns true if the game is finished, or false otherwise.
     * @return: Boolean as to whether the game is finished or not.
     */
    public boolean isFinished() {
        return isFinished;
    }
}
