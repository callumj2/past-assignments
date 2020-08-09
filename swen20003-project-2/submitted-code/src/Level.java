import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main working class of ShadowDefend, handles all update calls for
 * the main components of the game
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class Level {
    private static final int TIME_MODIFIER_MIN = 1;
    private static final int TIME_MODIFIER_MAX = 5;
    private final ShadowDefend game;
    private final Player player;
    private final TiledMap map;
    private final WaveSet waveSet;
    private final BuyPanel buyPanel;
    private final StatusPanel statusPanel;
    private final ArrayList<Tower> passiveTowers;
    private final ArrayList<Moveable> kineticObjects;
    private final SlicerManager slicerManager;
    private int timeModifier;
    private boolean isFinished;

    /**
     * Creates a new instance of 'Level'
     * @param levelNum: The integer level number referring to which map to load
     * @param game: The parent game class
     * @throws IOException: Throws an exception if there is an error creating classes from
     *                     input files
     */
    public Level(int levelNum, ShadowDefend game) throws IOException {
        this.game = game;
        this.player = new Player();
        this.timeModifier = 1;
        // Load in the map file for the level
        this.map = new TiledMap(String.format("res/levels/%d.tmx", levelNum));
        this.waveSet = new WaveSet("res/levels/waves.txt", this);

        // Instantiate space for projectiles, towers and slicers
        this.passiveTowers = new ArrayList<>();
        this.kineticObjects = new ArrayList<>();

        // Create a new slicer manager to handle slicer death instructions
        this.slicerManager = new SlicerManager(this);

        // Load in the UI
        this.statusPanel = new StatusPanel(this);
        this.buyPanel = new BuyPanel(this);

    }

    /**
     * Updates all the Level components
     * @param input: Input from the player's keyboard and mouse
     */
    public void update(Input input){
        // Draw the map
        map.draw(0,0,0,0, Window.getWidth(), Window.getHeight());

        // if 'L' or 'K' is pressed, increase or decrease the time modifier respectively
        if (input.wasPressed(Keys.L) && timeModifier < TIME_MODIFIER_MAX){
            timeModifier ++;
        }
        if (input.wasPressed(Keys.K) && timeModifier > TIME_MODIFIER_MIN){
            timeModifier --;
        }
        //Update the waves
        if (!waveSet.isFinished()) {
            waveSet.update(input, timeModifier);
        }
        else {
            isFinished = true;
        }

        // for each moving object, update it then check if it has finished updating yet
        ArrayList<Moveable> objectsToRemove = new ArrayList<>();
        for (Moveable movingObject : kineticObjects) {
            movingObject.update(timeModifier);
            if (movingObject.isFinished()){
                objectsToRemove.add(movingObject);
            }
        }
        // remove any finished objects from the game
        for (Moveable finishedObject : objectsToRemove){
            kineticObjects.remove(finishedObject);
        }

        // update the slicer manager
        slicerManager.update();

        // Update all passive towers
        for (Tower tower: passiveTowers){
            tower.update(timeModifier);
        }
        
        // Update all panels
        buyPanel.update(input);
        statusPanel.update();

        // Check if the player has run out of lives
        if (player.getLives() <= 0){
            System.exit(0);
        }
    }

    /**
     * Adds a slicer to both the slicers list and the kinetic objects list for updating
     * @param slicer: The Slicer to be added.
     */
    public void addSlicer(Slicer slicer) {
        kineticObjects.add(slicer);
        slicerManager.addSlicer(slicer);
    }

    /**
     * Adds a passive Tower to the level.
     * @param tower: The tower to be added.
     */
    public void addPassiveTower(Tower tower){ passiveTowers.add(tower);}

    /**
     * Adds a new Kinetic Object to the level, referencing classes
     * that implement the 'Moveable' interface.
     * @param object: A class that implements 'Moveable'
     */
    public void addKineticObject(Moveable object){
        kineticObjects.add(object);
    }
    /**
     * @return Fetches the level's Player Class
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Fetches the levels WaveSet Class
     */
    public WaveSet getWaveSet() {
        return waveSet;
    }
    /**
     * @return Fetches the levels passive towers
     */
    public ArrayList<Tower> getPassiveTowers() {
        return passiveTowers;
    }

    /**
     * @return Fetches the level time Modifier
     */
    public int getTimeModifier() {
        return timeModifier;
    }
    /**
     * @return Fetches the level map
     */
    public TiledMap getMap() {
        return map;
    }
    /**
     * @return Fetches the level Buy Panel
     */
    public BuyPanel getBuyPanel() {
        return buyPanel;
    }
    /**
     * @return Fetches the level Status Panel
     */
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }
    /**
     * @return Returns true if the level is finished, or false otherwise
     */
    public boolean isFinished() {
        return isFinished;
    }
    /**
     * @return Fetches the game instance
     */
    public ShadowDefend getGame() {
        return game;
    }
    /**
     * @return Fetches the level Slicer Manager
     */
    public SlicerManager getSlicerManager() {
        return slicerManager;
    }
}
