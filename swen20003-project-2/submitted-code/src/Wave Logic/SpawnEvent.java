import bagel.map.TiledMap;
/**
 * Refers to a Spawn Event, which spawns a specific type of Slicer
 * a set number of times at a given time interval
 *
 * Made for SWEN20003, Project 2,
 * @author Callum Johnson, 910519.
 */
public class SpawnEvent extends WaveEvent {
    private final String slicerType;
    private final Level parentLevel;
    private final int numberOfSlicers;
    private final int delay;
    private Timekeeper releaseTimer;
    private int numReleased;
    private boolean hasStarted;

    /**
     * Creates a new Spawn Event
     * @param event: The String array of instructions for this event
     * @param parentLevel: The current level of the game
     */
    public SpawnEvent(String[] event, Level parentLevel){
        // Set this event to 'not finished'
        setFinished(false);
        // initialize the remaining unique variables
        this.parentLevel = parentLevel;
        this.numberOfSlicers = Integer.parseInt(event[2]);
        this.slicerType = event[3];
        this.delay = Integer.parseInt(event[4]);
        this.hasStarted = false;
        this.numReleased = 0;

    }

    /**
     * Updates the spawn event
     * @param timeModifier: The current in-game time modifier
     */
    @Override
    public void update(int timeModifier){
        // Start a timer for when to release each slicer and release the first slicer
        if (!hasStarted){
            this.releaseTimer = new Timekeeper(delay);
            parentLevel.addSlicer(createSlicerFromString(slicerType, parentLevel.getMap()));
            numReleased ++;
            hasStarted = true;
        }
        // update the slicer release timer and check if it is time to release a slicer
        if (numReleased < numberOfSlicers) {
            releaseTimer.update(timeModifier);
            if (releaseTimer.shouldRelease()){
                parentLevel.addSlicer(createSlicerFromString(slicerType, parentLevel.getMap()));
                numReleased ++;
            }
        }

        // if we have spawned all the slicers for this round, we can let the parent class know to start
        // the next wave
        if (numReleased == numberOfSlicers && releaseTimer.shouldRelease() && !isFinished()){
            setFinished(true);
        }
    }


    // Function for determining and creating the correct type of slicer
    private Slicer createSlicerFromString(String SlicerType, TiledMap map){
        switch (SlicerType) {
            case "slicer":
                return new RegularSlicer(map);
            case "superslicer":
                return new SuperSlicer(map);
            case "megaslicer":
                return new MegaSlicer(map);
            case "apexslicer":
                return new ApexSlicer(map);
            default:
                return null;
        }
    }
}
